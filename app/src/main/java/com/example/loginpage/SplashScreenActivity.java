package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );


            Thread td = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(4000);

                        // After 5 seconds redirect to another intent
                        Intent i=new Intent(SplashScreenActivity.this , MainActivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();
                    } catch (Exception e) {
                    }
                }
            };
            // start thread
            td.start();
    }
}