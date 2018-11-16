package com.sesi.chris.animangaquiz.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.sesi.chris.animangaquiz.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       // printKeyHash();

        Handler handler = new Handler();
        Runnable runnable = this::openMainActivity;
        handler.postDelayed(runnable,1200);

    }

    public void printKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sesi.chris.animangaquiz",PackageManager.GET_SIGNATURES);
            for (Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHas",Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
