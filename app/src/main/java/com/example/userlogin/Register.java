package com.example.userlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, contactEditText, fullNameEditText;
    private ProgressBar progressBar;
    private Button registerButton;
    private TextView loginLink;
    private String email, fullName, contactNo, password, uid;
    private DatabaseReference databaseReference, usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usersReference = FirebaseDatabase.getInstance().getReference("Users");

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        contactEditText = (EditText) findViewById(R.id.accountContactNo);
        fullNameEditText = (EditText) findViewById(R.id.fullName);

        registerButton = (Button)findViewById(R.id.registerButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        loginLink = (TextView) findViewById(R.id.loginTextView);

        registerButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loginTextView:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.registerButton:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        email = emailEditText.getText().toString().trim();
        fullName = fullNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        contactNo = contactEditText.getText().toString().trim();

        if(fullName.isEmpty()){
            fullNameEditText.setError("Full Name is required!");
            fullNameEditText.requestFocus();
            return;
        }
        if(contactNo.isEmpty()){
            contactEditText.setError("Contact Number is required!");
            contactEditText.requestFocus();
            return;
        }
//        if(contactNo.length()!=10){
//            contactEditText.setError("Enter a proper Contact Number");
//            contactEditText.requestFocus();
//            return;
//        }
        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a proper email address!");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordEditText.setError("Enter a password of atleast 6 letters!");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullName,contactNo,email);
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    usersReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "User created successfully !", Toast.LENGTH_LONG);
                                startActivity(new Intent(Register.this, Account.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Register.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}