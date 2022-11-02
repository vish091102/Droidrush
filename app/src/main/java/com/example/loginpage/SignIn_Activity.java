package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignIn_Activity extends AppCompatActivity {
    private TextView createAcc;
    private EditText edtTxtEmail, edtTxtPass;
    private FirebaseAuth authProfile;
    private static final String TAG = "SignIn_Activity";


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

        edtTxtEmail = findViewById(R.id.editTxtEmail);
        edtTxtPass = findViewById(R.id.editTextPass);

        authProfile = FirebaseAuth.getInstance();

        Button btnSignIn = findViewById(R.id.buttonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = edtTxtEmail.getText().toString();
                String txtPass = edtTxtPass.getText().toString();

                if(TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(SignIn_Activity.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    edtTxtEmail.setError("Email is required");
                    edtTxtEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(SignIn_Activity.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    edtTxtEmail.setError("Valid Email is required");
                    edtTxtEmail.requestFocus();
                }else if(TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(SignIn_Activity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    edtTxtPass.setError("Password is required");
                    edtTxtPass.requestFocus();
                } else{
                    loginUser(txtEmail, txtPass);
                }
            }
        });
    }

    private void loginUser(String email, String pass) {
        authProfile.signInWithEmailAndPassword(email, pass).addOnCompleteListener(SignIn_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(SignIn_Activity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                   //Open User Profile
                   startActivity(new Intent(SignIn_Activity.this, UserProfile_Activity.class));
                   finish();  //Close SignInActivity
               }else {
                   try {
                       throw task.getException();
                   } catch(FirebaseAuthInvalidUserException e) {
                       edtTxtEmail.setError("Email does not exists or is no longer valid. Please register again");
                       edtTxtEmail.requestFocus();
                   } catch(FirebaseAuthInvalidCredentialsException e) {
                       edtTxtEmail.setError("Invalid credentials. Kindly check and re-enter");
                       edtTxtEmail.requestFocus();
                   } catch (Exception e) {
                       Log.e(TAG, e.getMessage());
                       Toast.makeText(SignIn_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                   Toast.makeText(SignIn_Activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authProfile.getCurrentUser() != null) {
            Toast.makeText(SignIn_Activity.this, "Already Logged In!", Toast.LENGTH_SHORT).show();

            //Start the UserProfileActivity
            startActivity(new Intent(SignIn_Activity.this, UserProfile_Activity.class));
            finish();  //Close SignInActivity
        }else {
            Toast.makeText(SignIn_Activity.this, "You can login now!", Toast.LENGTH_SHORT).show();
        }
    }
}