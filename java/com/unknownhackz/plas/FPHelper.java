package com.unknownhackz.plas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FPHelper extends FingerprintManager.AuthenticationCallback {

    private Context context;

    SPHelper spHelper ;
    String ltime= null;
    private static final String TAG = "FPHelper";

    String url ;
    // Constructor
    public FPHelper(Context mContext) {
        context = mContext;
        spHelper = new SPHelper(context);
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject,
                                    String ltime) {


        this.ltime = ltime;

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        url = context.getString(R.string.url)+"/authenticator.php";

    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        UpdateThread ut = new UpdateThread();
        ut.start();

    }

    private void update(String e){
        TextView textView =  ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
    }

    class UpdateThread extends Thread{
        @Override
        public void run() {
            super.run();

            ConnHelper connHelper = new ConnHelper();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("username",spHelper.getName()));
            param.add(new BasicNameValuePair("ltime",ltime));
            param.add(new BasicNameValuePair("deviceid",spHelper.getDeviceId()));

            JSONObject jsonObject = connHelper.makeHttpRequest(url,"POST",param);
            try {
                Toast.makeText((Activity) context, "Status " + jsonObject.toString(), Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d(TAG,""+e);
            }
            Intent i =  new Intent(context,PreviousLoginDetails.class);
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }

}
