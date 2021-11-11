package com.example.letsvolunteer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Authentication extends AppCompatActivity {

    Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        registerBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);


        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(Authentication.this, RegisterationActivity.class));
        });
    }
}