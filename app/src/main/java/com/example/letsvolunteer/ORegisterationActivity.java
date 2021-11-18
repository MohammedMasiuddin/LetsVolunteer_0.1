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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ORegisterationActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText emailEt, passwordEt, organizationNameEt, addressEt, phoneNumberEt, confirmPasswordEt;
    Button registerBtn;
    TextView haveAccountTv;

    ProgressDialog progressDialog;

    public static final String KEY_ON = "organizationName";
    public static final String KEY_contactEmail = "contactEmail";
    public static final String KEY_email = "email";
    public static final String KEY_address = "address";
    public static final String KEY_phoneNumber = "phoneNumber";
    public static final String KEY_description = "description";
    public static final String KEY_photoUri = "photoUri";

    //Shared instance of firebase
    private FirebaseAuth mAuth;

    // Connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oregisteration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        emailEt = findViewById(R.id.oEmailEditText);
        passwordEt = findViewById(R.id.oPasswordEditText);
        organizationNameEt = findViewById(R.id.oOrganizationNameEditText);
        addressEt = findViewById(R.id.oAddressEditText);
        phoneNumberEt = findViewById(R.id.oPhoneNumberEditText);
        confirmPasswordEt = findViewById(R.id.oConfirmpasswordEditText);
        registerBtn = findViewById(R.id.oRegisterButton);
        haveAccountTv = findViewById(R.id.oHave_accountTv);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String organizationName = organizationNameEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String phoneNumber = phoneNumberEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // set focus to editText and send error
                    emailEt.setError("Invalid Email");
                    emailEt.setFocusable(true);
                }
                else if (TextUtils.isEmpty(organizationName)) {
                    organizationNameEt.setError("Required field");
                    organizationNameEt.setFocusable(true);
                }
                else if (TextUtils.isEmpty(address)) {
                    addressEt.setError("Required field");
                    addressEt.setFocusable(true);
                }
                else if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 7) {
                    phoneNumberEt.setError("invalid phone number");
                    phoneNumberEt.setFocusable(true);
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
                    registerUser(email, password, organizationName, address, phoneNumber); // register the user
                }
            }
        });

        haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ORegisterationActivity.this, OLoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password, String organizationName, String address, String phoneNumber) {
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
                            Toast.makeText(ORegisterationActivity.this, "Registered \n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                            UserID = user.getUid();
                            DocumentReference documentReference = db.collection("Organizers").document(UserID);
                            Map<String, Object> data = new HashMap<>();
                            data.put(KEY_ON, organizationName);
                            data.put(KEY_contactEmail, "");
                            data.put(KEY_email, email);
                            data.put(KEY_address, address);
                            data.put(KEY_phoneNumber, phoneNumber);
                            data.put(KEY_description, "");
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
                            startActivity(new Intent(ORegisterationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(ORegisterationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ORegisterationActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity
        return super.onSupportNavigateUp();
    }
}