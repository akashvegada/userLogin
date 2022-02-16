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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText emailResetPassword;
    private TextView loginResetPassword;
    private Button buttonResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailResetPassword = (EditText) findViewById(R.id.emailResetPassword);
        loginResetPassword = (TextView) findViewById(R.id.loginResetPassword);
        buttonResetPassword = (Button) findViewById(R.id.buttonResetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBarResetPassword);
        mAuth = FirebaseAuth.getInstance();

        buttonResetPassword.setOnClickListener(this);
        loginResetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonResetPassword:
                resetPassword();
                break;

            case R.id.loginResetPassword:
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                break;
        }
    }

    private void resetPassword() {
        String email = emailResetPassword.getText().toString().trim();
        if(email.isEmpty()){
            emailResetPassword.setError("Email is required!");
            emailResetPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailResetPassword.setError("Enter a proper email address!");
            emailResetPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Please check your email to reset password!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}