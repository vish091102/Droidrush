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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Activity extends AppCompatActivity {
    private TextView loginAcc;
    private EditText edtTxtName, edtTxtPhone, edtTxtEmail, edtTxtPass;
    private static final String TAG = "SingUp_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up");

        loginAcc = findViewById(R.id.createAccount);

        loginAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp_Activity.this , SignIn_Activity.class);
                startActivity(intent);
            }
        });

        edtTxtName = findViewById(R.id.editTextName);
        edtTxtPhone = findViewById(R.id.editTextPhone);
        edtTxtEmail = findViewById(R.id.editTextEmail);
        edtTxtPass = findViewById(R.id.editTextPass);

        Button btnRegister = findViewById(R.id.buttonSignIn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String txtName = edtTxtName.getText().toString();
                String txtPhone = edtTxtPhone.getText().toString();
                String txtEmail = edtTxtEmail.getText().toString();
                String txtPass = edtTxtPass.getText().toString();

                //Validate phone number
                String mobileRegex = "[6-9][0-9]{9}";  //first number can be {6,8,9} and rest 9 numbers can be any number
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(txtPhone);

                if(TextUtils.isEmpty(txtName)) {
                    Toast.makeText(SignUp_Activity.this, "Please enter your User Name", Toast.LENGTH_LONG).show();
                    edtTxtName.setError("User Name is required");
                    edtTxtName.requestFocus();
                }else if(TextUtils.isEmpty(txtPhone)) {
                    Toast.makeText(SignUp_Activity.this, "Please enter your Phone Number", Toast.LENGTH_LONG).show();
                    edtTxtPhone.setError("Phone Number is required");
                    edtTxtPhone.requestFocus();
                }else if(txtPhone.length() != 10) {
                    Toast.makeText(SignUp_Activity.this, "Please re-enter your Phone Number", Toast.LENGTH_LONG).show();
                    edtTxtPhone.setError("Phone Number should be 10 digits");
                    edtTxtPhone.requestFocus();
                }else if(!mobileMatcher.find()) {
                    edtTxtPhone.setError("Phone Number is not valid");
                    edtTxtPhone.requestFocus();
                }else if(TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(SignUp_Activity.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    edtTxtEmail.setError("Email is required");
                    edtTxtEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(SignUp_Activity.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    edtTxtEmail.setError("Valid Email is required");
                    edtTxtEmail.requestFocus();
                }else if(TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(SignUp_Activity.this, "Please enter your Password", Toast.LENGTH_LONG).show();
                    edtTxtPass.setError("Password is required");
                    edtTxtPass.requestFocus();
                }else if(txtPass.length() < 6) {
                    Toast.makeText(SignUp_Activity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    edtTxtPass.setError("Password too weak");
                    edtTxtPass.requestFocus();
                }else{
                    registerUser(txtName, txtPhone, txtEmail, txtPass);
                }
            }
        });
    }

    private void registerUser(String txtName, String txtPhone, String txtEmail, String txtPass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create User Profile
        auth.createUserWithEmailAndPassword(txtEmail, txtPass).addOnCompleteListener(SignUp_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //We can save name of the User in the user object itself
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txtName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into the Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtPhone);

                    //Extracting User reference from Database for "Registered Users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(SignUp_Activity.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                //Open User Profile after successful registration
                                Intent intent = new Intent(SignUp_Activity.this, UserProfile_Activity.class);
                                //To prevent User from returning back to registered Activity on pressing back button after Registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                finish();   //to close Registration Activity
                            }else {
                                Toast.makeText(SignUp_Activity.this, "User Registered failed. Please try again", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        edtTxtPass.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special character.");
                        edtTxtPass.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        edtTxtPass.setError("Your Email is invalid or already in use. Kindly re-enter.");
                        edtTxtPass.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        edtTxtPass.setError("User is already registered with this email. Kindly use another email.");
                        edtTxtPass.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUp_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}