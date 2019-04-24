package com.unknownhackz.plas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SPHelper {
    public static final String LoginDetail = "LoginDetail";
    public static final String Name = "Name";
    public static final String UserName = "UserName";
    public static final String Email = "Email";
    public static final String SKey = "SKey";
    public static final String DeviceId = "DeviceId";

    public static final String TAG ="SPHelper";


    private Context context;
     SharedPreferences sharedPreferences;

     SPHelper(Context mContext){
       context =  mContext;
       sharedPreferences = context.getSharedPreferences(LoginDetail,Context.MODE_PRIVATE);
    }

     String getDeviceId() {
        return sharedPreferences.getString(DeviceId,"");
    }

    public String getEmail() {
        return sharedPreferences.getString(Email,"");
    }

    public String getName() {
        return sharedPreferences.getString(Name,"");
    }

    public String getSkey() {
        return sharedPreferences.getString(SKey,"");
    }

    public String getUsername() {
        return sharedPreferences.getString(UserName,"");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     String hashIt(String comb) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(comb.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return String.format("%064x",new BigInteger(1,digest));
    }

}
