/*
 *
 * New User Registration from mobile
 *
 */


package com.unknownhackz.plas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.AsyncTask;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.hardware.fingerprint.FingerprintManager;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.util.List;


@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends Activity {


    private String getName,getUserName, getEmail, getSKey, deviceId;


    public static final String TAG = "MainActivity";

    JSONArray result = null;
    ConnHelper jsonParser = new ConnHelper();

    String message ,reason;
    String url;

    EditText editName, editEmail,editUserName;
    SPHelper spHelper;

    final int PERMISSION_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editUserName = findViewById(R.id.username);

        spHelper = new SPHelper(getApplicationContext());
         url = getApplication().getString(R.string.url)+"/register.php";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_STATE);


            Toast.makeText(getApplicationContext(),"Permission Not Granted Please Check",Toast.LENGTH_SHORT).show();
            return ;
        }



    }

    protected void onResume() {
        super.onResume();

    }


    public void authDetails(View view) throws NoSuchAlgorithmException {

        getName = editName.getText().toString();
        getUserName = editUserName.getText().toString();
        getEmail = editEmail.getText().toString();
        getSKey = spHelper.hashIt(getUserName + " " + getEmail);
        deviceId = spHelper.hashIt(getDeviceId());

        if(deviceId == null
                || getEmail.equals("")
                || getName.isEmpty()
                || getSKey.isEmpty()
                || getUserName.isEmpty()) {
         Toast.makeText(getApplicationContext(),"Something went wrong ,please make sure your entered all fileds",Toast.LENGTH_SHORT).show();
            return;
        }else{
            new LoadInBackground().execute();
        }

        }



    class LoadInBackground extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("name",getName));
            param.add(new BasicNameValuePair("username",getUserName));
            param.add(new BasicNameValuePair("email",getEmail));
            param.add(new BasicNameValuePair("skey",getSKey));
            param.add(new BasicNameValuePair("deviceid",deviceId));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
           Log.d(TAG,"json value "+jsonObject);
            try {
                result = jsonObject.getJSONArray("r");

                for(int i=0; i< result.length();i++) {
                    JSONObject jo = result.getJSONObject(i);

                    reason = jo.getString("reason");
                    message = jo.getString("message");

                }
            }catch (Exception e){
                Log.d(TAG," "+e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(reason.equals("Successful")) {
                Toast.makeText(getApplicationContext(),"Welcome \n "+reason+" : "+message,Toast.LENGTH_SHORT).show();
                saveDetails();
            }
            else{
                Toast.makeText(getApplicationContext(),reason+" : "+message,Toast.LENGTH_SHORT).show();
         }

        }
    }

    //New User , registation from mobile
    //saving details in local
    public void saveDetails() {

        SharedPreferences.Editor editor = spHelper.sharedPreferences.edit();
        editor.putString(SPHelper.Name,getName);
        editor.putString(SPHelper.UserName,getUserName);
        editor.putString(SPHelper.Email,getEmail);
        editor.putString(SPHelper.SKey,getSKey);
        editor.putString(SPHelper.DeviceId,deviceId);

        if(editor.commit())
        {
            Toast.makeText(getApplicationContext(),"Login Details Added Sucessfully",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,PreviousLoginDetails.class);
            startActivity(i);
            finish();
        }

    }
    @SuppressLint("MissingPermission")
    private String getDeviceId(){
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        return   tm.getDeviceId();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STATE:{
                if(grantResults.length >0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(getApplicationContext(),"Grant permission, for more detials see Help!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }




    @Deprecated
    public void onAuthenticationSuceed(FingerprintManager.AuthenticationResult result)
    {

    }


}
