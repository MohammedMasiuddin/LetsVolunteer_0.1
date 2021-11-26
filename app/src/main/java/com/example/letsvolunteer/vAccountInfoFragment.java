package com.example.letsvolunteer;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    private static final int GALLERY_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Uri imageUri;
    String KEY_FN = "firstName";
    String KEY_LN = "lastName";
    String KEY_age = "age";
    String KEY_photoUri = "photoUri";

    //Shared instance of firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Connection to firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();
    DocumentReference documentReference = db.collection("Volunteer").document(user.getUid());
    StorageReference storageReference;

    ImageView imageView;
    LinearLayout linearLayout;
    FloatingActionButton fab;

    //progress dialog
    ProgressDialog pd;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;

    // arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

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
        ImageView profileImage, changeProfile;
        String UserID = null;

        //Shared instance of firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Connection to firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference documentReference = db.collection("Volunteer").document(user.getUid());
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference displayImageRef;


        String KEY_FN = "firstName";
        String KEY_LN = "lastName";
        String KEY_age = "age";
        String KEY_photoUri = "photoUri";

        //init
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firstNameEt = view.findViewById(R.id.firstNameEt);
        lastNameEt = view.findViewById(R.id.lastNameEt);
        ageEt = view.findViewById(R.id.ageEt);
        saveBtn = view.findViewById(R.id.saveFieldsBtn);
        editBtn = view.findViewById(R.id.editFieldsBtn);
        imageView = view.findViewById(R.id.profileIv);
        changeProfile = view.findViewById(R.id.changeProfileIv);
        fab = view.findViewById(R.id.fab);
        Context context = getContext();
        //init progress dialog
        pd = new ProgressDialog(getActivity());

        firstNameEt.setEnabled(false);
        lastNameEt.setEnabled(false);
        ageEt.setEnabled(false);

        editBtn.setOnClickListener(v -> {
         if(!firstNameEt.isEnabled()) {
             firstNameEt.setEnabled(true);
             lastNameEt.setEnabled(true);
             ageEt.setEnabled(true);
         } else {
             firstNameEt.setEnabled(false);
             lastNameEt.setEnabled(false);
             ageEt.setEnabled(false);
         }
        });

//        showBtn.setOnClickListener(v -> {
//
//            documentReference.get()
//                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            if (documentSnapshot.exists()) {
//                                String firstName = documentSnapshot.getString(KEY_FN);
//                                String lastName = documentSnapshot.getString(KEY_LN);
//                                String age = documentSnapshot.getString(KEY_age);
//
//                                firstNameEt.setText(firstName);
//                                lastNameEt.setText(lastName);
//                                ageEt.setText(age);
//
//                            } else {
//                                Log.d(TAG, "No data exists");
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, "onFailure: " + e.toString());
//                }
//            });
//        });

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
                    Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure SAVE: " + e.getMessage());
                }
            });

            firstNameEt.setEnabled(false);
            lastNameEt.setEnabled(false);
            ageEt.setEnabled(false);
        });

        changeProfile.setOnClickListener(v -> {
//            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            galleryIntent.setType("image/*");
//            startActivityForResult(galleryIntent, GALLERY_CODE);
            pd.setMessage("Updating Profile Pic");
            showImagePicDialog();
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
                    String image = documentSnapshot.getString(KEY_photoUri);

                    firstNameEt.setText(firstName);
                    lastNameEt.setText(lastName);
                    ageEt.setText(age);
                    try {
                        Picasso.get().load(image).into(imageView);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.add_image).into(imageView);
                    }
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });


        return view;
    }

    private boolean checkStoragePermissions() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }
    private void requestCameraPermission(){
        requestPermissions( cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit Profile Pic", "Edit FirstName", "Edit LastName", "Edit Age"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        //set items for dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Edit Profile
                    pd.setMessage("Updating Profile Pic");
                    showImagePicDialog();
                }
                else if (which == 1) {
                    // Edit First Name
                    pd.setMessage("Updating First Name");
                    showNameAgeUpdateDialog("firstName");
                }
                else if (which == 2) {
                    // Edit Last Name
                    pd.setMessage("Updating Last name");
                    showNameAgeUpdateDialog("lastName");
                }
                else if (which == 3) {
                    // Edit Age
                    pd.setMessage("Updating Age");
                    showNameAgeUpdateDialog("age");
                }
            }
        });
        //create dialog
        builder.create().show();
    }

    private void showNameAgeUpdateDialog(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + key);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(getActivity());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                //validate empty field
                if(!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<String, Object>();
                    result.put(key, value);
                    documentReference.update(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please Enter " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void showImagePicDialog() {

        String options[] = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image");
        //set items for dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Camera clicked
                    if(!checkCameraPermissions()){
                        requestCameraPermission();;
                    } else {
                        pickFromCamera();
                    }
                }
                else if (which == 1) {
                    // Gallery clicked
                    if(!checkStoragePermissions()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create dialog
        builder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permission enabled
                        pickFromCamera();
                    }
                    else {
                        //permission denied
                        Toast.makeText(getActivity(), "Please enable the camera & storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permission enabled
                        pickFromGallery();
                    }
                    else {
                        //permission denied
                        Toast.makeText(getActivity(), "Please enable the storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        //Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                View view2 = getLayoutInflater().inflate(R.layout.imagesampletemplate, linearLayout,false);
                ImageView image4 = view2.findViewById(R.id.imageViewsample);
                imageUri = data.getData();
                imageView.findViewById(R.id.changeProfileIv);
                imageView.setImageURI(imageUri);

            }
        }
        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE) {
                imageUri = data.getData();
                uploadProfilePhoto(imageUri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                uploadProfilePhoto(imageUri);
            }
        }
    }

    private void uploadProfilePhoto(Uri uri) {

        pd.show();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference displayImageRef = storageReference.child("Profile_images")
        .child("user_"+ user.getUid() +".jpeg");
        displayImageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();


                        if (uriTask.isSuccessful()) {
                            //image uploaded so add/update url
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(KEY_photoUri, downloadUri.toString());

                            documentReference.update(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image updated", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Error updating image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}