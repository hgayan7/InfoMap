package com.example.him.infomap;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity{
    private static final String TAG=LoginActivity.class.getSimpleName();
    public static final int MULTIPLE_PERMISSIONS = 10;
    Button button;
    EditText editText;
    //if location permissions are needed
    /*
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    List<String> listPermissionsNeeded;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.activity_login);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvaialable()) {

                        if(!editText.getText().toString().equals("")) {
                            Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }else {
                            Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Network not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }
/*
    private  boolean checkPermissions() {
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if(!editText.getText().toString().equals("")) {
                       Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                       startActivity(intent);
                   }else {
                       Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
                   }
                } else {
                    for (String per :listPermissionsNeeded ) {
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }}
*/
    private boolean isNetworkAvaialable(){

        ConnectivityManager mConnectivityManager;
        NetworkInfo mNetworkInfo = null;
        try {
           mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
           mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
       }catch (NullPointerException e){
           Log.d(TAG, "isNetworkAvaialable: "+ e);
       }
       return mNetworkInfo!=null && mNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();

    }
}
