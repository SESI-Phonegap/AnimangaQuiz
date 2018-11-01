package com.sesi.chris.animangaquiz.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sesi.chris.animangaquiz.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        Runnable runnable = this::openMainActivity;
        handler.postDelayed(runnable,2500);

    }

    public void openMainActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
