package com.unknownhackz.plas;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import org.json.JSONObject;



public class PreviousLoginDetails extends ListActivity {

    ArrayList<HashMap<String, String>> loginlist;
    JSONArray loginrecord = null;
    ConnHelper jsonParser = new ConnHelper();
    String username , deviceid = null;

    Context mContext ;

    public static final String TAG = "PreviousLoginDetails";

    String url ;

    int auth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_login_details);
      url =   getApplication().getString(R.string.url)+"/listLoginRecords.php";
        Toast.makeText(getApplicationContext(),"Login Successful ",Toast.LENGTH_SHORT).show();

        final SPHelper spHelper = new SPHelper(getApplicationContext());

        username = spHelper.getUsername();
        deviceid = spHelper.getDeviceId();
        Log.d(TAG,""+username);
        loginlist = new ArrayList<HashMap<String, String>>();

        new LoadInBackground().execute();

        ListView ls = getListView();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String auth = ((TextView) view.findViewById(R.id.auth)).getText().toString();
                String u = ((TextView) view.findViewById(R.id.username)).getText().toString();
                String t = ((TextView) view.findViewById(R.id.ltime)).getText().toString();

                if(auth.equals("1")){
                    Toast.makeText(getApplicationContext(),"You already logged in this session",Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(PreviousLoginDetails.this,FPAuth.class);
                     i.putExtra("ltime",t);
                     startActivity(i);
                    Toast.makeText(getApplicationContext(),"Verify Fingerprint to login now",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class LoadInBackground extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("username",username));
            param.add(new BasicNameValuePair("deviceid",deviceid));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);

            try{
                Log.d(TAG,"json value "+jsonObject.toString());
                loginrecord = jsonObject.getJSONArray("r");

                for(int i=0; i< loginrecord.length();i++)
                {
                    JSONObject jo = loginrecord.getJSONObject(i);

                    String usernme = jo.getString("username");
                    String email = jo.getString("email");
                    String ltime = jo.getString("time");
                    auth = jo.getInt("auth");

                    HashMap<String ,String> hashMap = new HashMap<>();

                    hashMap.put("username","Name : "+usernme);
                    hashMap.put("email","Email :"+email);
                    hashMap.put("time",ltime);
                    hashMap.put("auth",String.valueOf(auth));

                    loginlist.add(hashMap);
                }

            }catch (Exception e)
            {
                Log.d(TAG," "+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter listAdapter = new SimpleAdapter(PreviousLoginDetails.this,
                            loginlist,R.layout.list_item,new String[] {"username","email","time","auth"},
                            new int[] {R.id.username,R.id.email,R.id.ltime,R.id.auth});

                    setListAdapter(listAdapter);
                }
            });
        }
    }



}
