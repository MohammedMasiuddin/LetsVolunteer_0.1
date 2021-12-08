package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterationActivity extends AppCompatActivity {

    public static final String TAG= "TAG";
    EditText emailEt, passwordEt, firstNameEt, lastNameEt, confirmPasswordEt;
    Button registerBtn;
    TextView haveAccountTv;
    String UserID;

    public static final String KEY_FN = "firstName";
    public static final String KEY_LN = "lastName";
    public static final String KEY_email = "email";
    public static final String KEY_age = "age";
    public static final String KEY_photoUri = "photoUri";


    ProgressDialog progressDialog;

    //Shared instance of firebase
    private FirebaseAuth mAuth;

    // Connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        emailEt = findViewById(R.id.emailEditText);
        passwordEt = findViewById(R.id.passwordEditText);
        registerBtn = findViewById(R.id.registerButton);
        haveAccountTv = findViewById(R.id.have_accountTv);
        firstNameEt = findViewById(R.id.vFirstNameEditText);
        lastNameEt = findViewById(R.id.vLastNameEditText);
        confirmPasswordEt = findViewById(R.id.vConfirmpasswordEditText);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String firstName = firstNameEt.getText().toString().trim();
                String lastName = lastNameEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // set focus to editText and send error
                    emailEt.setError("Invalid Email");
                    emailEt.setFocusable(true);
                }
                else if (TextUtils.isEmpty(firstName)) {
                    firstNameEt.setError("Required field");
                    firstNameEt.setFocusable(true);
                }
                else if (TextUtils.isEmpty(lastName)) {
                    lastNameEt.setError("Required field");
                    lastNameEt.setFocusable(true);
                }
                else if (password.length() < 6) {
                    // set focus to editText and send error
                    passwordEt.setError("Password should be at least 6 characters");
                    passwordEt.setFocusable(true);
                }
                else if (!confirmPassword.equals(password)) {
                    confirmPasswordEt.setError("Passwords do not match");
                    confirmPasswordEt.setFocusable(true);
                }
                else {
                    registerUser(email, password, firstName, lastName); // register the user
                }
            }
        });

        haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterationActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password, String firstName, String lastName) {
      //  email and password are valid, show progress and register user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dimiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterationActivity.this, "Registered \n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                            UserID = user.getUid();
                            DocumentReference documentReference = db.collection("Volunteer").document(UserID);
                            Map<String, Object> data = new HashMap<>();
                            data.put(KEY_FN, firstName);
                            data.put(KEY_LN, lastName);
                            data.put("onlineStatus", "online");
                            data.put("typingTo", "noOne");
                            data.put(KEY_email, email);
                            data.put(KEY_age, "");
                            data.put(KEY_photoUri, "");
                            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user profile created for"+ UserID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", UserID);
                            userData.put("name", firstName);
                            userData.put(KEY_photoUri, "");
                            userData.put(KEY_email, email);
                            userData.put("onlineStatus", "online");
                            userData.put("typingTo", "noOne");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(UserID).setValue(userData);

                            startActivity(new Intent(RegisterationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterationActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
    }
}