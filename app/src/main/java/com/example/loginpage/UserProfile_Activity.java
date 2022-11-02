package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile_Activity extends AppCompatActivity {
    private TextView txtViewWelcome, txtViewName, txtViewEmail, txtViewPhone;
    private String name, email, phone;
    private ImageView imageView;
    private Button btn;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Student Profile");

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

        btn = findViewById(R.id.btnToOwner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile_Activity.this, OwnersPage_Activity.class);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserProfile_Activity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        }else{
            showUserProfile(firebaseUser);
        }
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