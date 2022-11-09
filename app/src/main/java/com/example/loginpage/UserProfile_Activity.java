package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile_Activity extends AppCompatActivity {
    private TextView txtViewWelcome, txtViewName, txtViewEmail, txtViewPhone;
    private String name, email, phone;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("User Profile");
//        //For back button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtViewWelcome = findViewById(R.id.textViewWelcome);
        txtViewName = findViewById(R.id.textViewShowName);
        txtViewEmail = findViewById(R.id.textViewShowEmail);
        txtViewPhone = findViewById(R.id.textViewShowPhone);

        imageView = findViewById(R.id.profileDp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile_Activity.this, UserProfilePic_Activity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(UserProfile_Activity.this, RecyclerView.class));
                        return true;
                    case R.id.info:
                        startActivity(new Intent(UserProfile_Activity.this,OwnersPage_Activity.class));
                        return true;
//                    case R.id.profile:
//                        startActivity(new Intent(UserProfile_Activity.this,UserProfile_Activity.class));
//                        finish();
//                        return true;
                }
                return false;
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserProfile_Activity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        }else{
            checkIfEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }
    }

    //User coming to UserProfile_Activity after successful registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile_Activity.this);
        builder.setTitle("Email not Verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification next time.");

        //Open Email Apps if User clicks Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //To email app in new window and not within our app
                startActivity(intent);
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    phone = readUserDetails.phone;

                    txtViewWelcome.setText("Welcome, " + name + "!");
                    txtViewName.setText(name);
                    txtViewEmail.setText(email);
                    txtViewPhone.setText(phone);

                    //Set User DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageView setImageURI() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(UserProfile_Activity.this).load(uri).into(imageView);
                }else{
                    Toast.makeText(UserProfile_Activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile_Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Creating Action Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if(id == android.R.id.home){
//            NavUtils.navigateUpFromSameTask(UserProfile_Activity.this);
//        }

        if(id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(UserProfile_Activity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserProfile_Activity.this, MainActivity.class);

            //Clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(UserProfile_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}