package com.example.letsvolunteer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication extends AppCompatActivity {

    Button registerBtn, loginBtn;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        registerBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null ){
            startActivity(new Intent(Authentication.this, MainActivity.class));
            finish();
        }

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(Authentication.this, LoginActivity.class));
        });
        loginBtn.setOnClickListener(v -> {
           startActivity(new Intent(Authentication.this, OLoginActivity.class));
        });
    }
}