package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
ImageView img1,img2;
TextView txt1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
       img1=findViewById(R.id.img1);
       img2=findViewById(R.id.img2);
       txt1=findViewById(R.id.txt1);
        Animation top,bottom;
        top= AnimationUtils.loadAnimation(this,R.anim.top);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom);
        img1.setAnimation(top);
        img2.setAnimation(bottom);
        txt1.setAnimation(top);

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