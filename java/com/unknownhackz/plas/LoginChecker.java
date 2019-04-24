package com.unknownhackz.plas;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LoginChecker extends AppCompatActivity {


    public static final String TAG ="LoginChecker";
    SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_checker);

    }

    @Override
    protected void onResume() {
        super.onResume();
        spHelper = new SPHelper(getApplicationContext());

        if(spHelper.sharedPreferences.contains(SPHelper.Name)
                && spHelper.sharedPreferences.contains(SPHelper.UserName)
                && spHelper.sharedPreferences.contains(SPHelper.Email)
                && spHelper.sharedPreferences.contains(SPHelper.SKey)
                && spHelper.sharedPreferences.contains(SPHelper.DeviceId)){

        String getName = spHelper.getName();
        String getUserName = spHelper.getUsername();
        String getEmail = spHelper.getEmail();
        String getSKey = spHelper.getSkey();
        String getDeviceId = spHelper.getDeviceId();
        Log.d(TAG,""+getName);

        if (getName != null
                && getUserName != null
                && getEmail != null
                && getSKey !=null
                && getDeviceId !=null) {
            if (!getEmail.isEmpty()
                    && !getUserName.isEmpty()
                    && !getName.isEmpty()
                    && !getSKey.isEmpty()
                    && !getDeviceId.isEmpty()) //get local user details
            {
                //TODO : Login with user details (in app login)
                Intent i = new Intent(LoginChecker.this, PreviousLoginDetails.class);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), "Login Successful! Welcome " + getName, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    }

    public void existingUser(View view) {
        Intent i = new Intent(LoginChecker.this,ExistingUser.class);
        startActivity(i);

    }

    public void newUser(View view) {
        Intent i = new Intent(LoginChecker.this,MainActivity.class);
        startActivity(i);

    }
}
