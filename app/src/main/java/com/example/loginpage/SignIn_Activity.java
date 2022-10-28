package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignIn_Activity extends AppCompatActivity {
    TextView createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        getSupportActionBar().setTitle("Sign In");

        createAcc = findViewById(R.id.createAccount);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn_Activity.this , SignUp_Activity.class);
                startActivity(intent);
            }
        });
    }
}