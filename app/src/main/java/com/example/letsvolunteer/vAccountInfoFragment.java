package com.example.letsvolunteer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vAccountInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vAccountInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TAG";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public vAccountInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vAccountInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static vAccountInfoFragment newInstance(String param1, String param2) {
        vAccountInfoFragment fragment = new vAccountInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_v_account_info, container, false);

        Button saveBtn, editBtn, showBtn;
        EditText firstNameEt, lastNameEt, ageEt;
        String UserID = null;

        //Shared instance of firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Connection to firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference documentReference = db.collection("Volunteer").document(user.getUid());

        String KEY_FN = "firstName";
        String KEY_LN = "lastName";
        String KEY_age = "age";
        String KEY_photoUri = "photoUri";

        firstNameEt = view.findViewById(R.id.firstNameEt);
        lastNameEt = view.findViewById(R.id.lastNameEt);
        ageEt = view.findViewById(R.id.ageEt);
        saveBtn = view.findViewById(R.id.saveFieldsBtn);
        editBtn = view.findViewById(R.id.editFieldsBtn);
        showBtn = view.findViewById(R.id.showFieldsBtn);

        firstNameEt.setEnabled(false);
        lastNameEt.setEnabled(false);
        ageEt.setEnabled(false);

        editBtn.setOnClickListener(v -> {
         if(firstNameEt.isEnabled()) {
             firstNameEt.setEnabled(true);
             lastNameEt.setEnabled(true);
             ageEt.setEnabled(true);
         } else {
             firstNameEt.setEnabled(false);
             lastNameEt.setEnabled(false);
             ageEt.setEnabled(false);
         }
        });

        showBtn.setOnClickListener(v -> {

            documentReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String firstName = documentSnapshot.getString(KEY_FN);
                                String lastName = documentSnapshot.getString(KEY_LN);
                                String age = documentSnapshot.getString(KEY_age);

                                firstNameEt.setText(firstName);
                                lastNameEt.setText(lastName);
                                ageEt.setText(age);

                            } else {
                                Log.d(TAG, "No data exists");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });
        });

        saveBtn.setOnClickListener(v -> {
            String firstName = firstNameEt.getText().toString().trim();
            String lastName = lastNameEt.getText().toString().trim();
            String age = ageEt.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put(KEY_FN, firstName);
            data.put(KEY_LN, lastName);
            data.put(KEY_age, age);
            documentReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess SAVE: updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure SAVE: " + e.getMessage());
                }
            });

        });


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: Something went wrong");
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString(KEY_FN);
                    String lastName = documentSnapshot.getString(KEY_LN);
                    String age = documentSnapshot.getString(KEY_age);

                    firstNameEt.setText(firstName);
                    lastNameEt.setText(lastName);
                    ageEt.setText(age);
                }

            }
        });




        return view;
    }
}