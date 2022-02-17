package com.example.userlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity{

    private Button logoutButton;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private String userID;
    private String fullName, email, contactNo;
    private TextView fullNameTextView, emailTextView, contactTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currentUser.getUid();

        fullNameTextView = (TextView)findViewById(R.id.accountFullName);
        emailTextView = (TextView)findViewById(R.id.accountEmail);
        contactTextView = (TextView)findViewById(R.id.accountContactNo);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                     fullName = userProfile.fullName;
                     email = userProfile.email;
                     contactNo = userProfile.contactNo;

                    fullNameTextView.setText("Name : "+fullName);
                    emailTextView.setText("Email : "+email);
                    contactTextView.setText("Contact : "+contactNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Account.this, MainActivity.class));
                finish();
            }
        });
    }


}