package com.example.letsvolunteer;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * Use the {@link oAccountInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class oAccountInfoFragment extends Fragment {

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
    String KEY_ON = "organizationName";
    String KEY_contactEmail = "contactEmail";
    String KEY_email = "email";
    String KEY_address = "address";
    String KEY_phoneNumber = "phoneNumber";
    String KEY_description = "description";
    String KEY_photoUri = "photoUri";

    //Shared instance of firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Connection to firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();
    DocumentReference documentReference = db.collection("Organizers").document(user.getUid());
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

    public oAccountInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment oAccountInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static oAccountInfoFragment newInstance(String param1, String param2) {
        oAccountInfoFragment fragment = new oAccountInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_o_account_info, container, false);

        Button saveBtn, editBtn, showBtn;
        EditText organizationNameEt, contactEmailEt, emailEt, addressEt, phoneNumberEt, descriptionEt ;
        ImageView profileImage, changeProfile;
        String UserID = null;

        //Shared instance of firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Connection to firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference documentReference = db.collection("Organizers").document(user.getUid());
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference displayImageRef;


        String KEY_ON = "organizationName";
        String KEY_contactEmail = "contactEmail";
        String KEY_email = "email";
        String KEY_address = "address";
        String KEY_phoneNumber = "phoneNumber";
        String KEY_description = "description";
        String KEY_photoUri = "photoUri";

        //init
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        organizationNameEt = view.findViewById(R.id.organizationNameEt);
        contactEmailEt = view.findViewById(R.id.contactEmailEt);
        emailEt = view.findViewById(R.id.emailEt);
        addressEt = view.findViewById(R.id.addressEt);
        phoneNumberEt = view.findViewById(R.id.phoneNumberEt);
        descriptionEt = view.findViewById(R.id.descriptionEt);
        saveBtn = view.findViewById(R.id.saveFieldsBtn2);
        editBtn = view.findViewById(R.id.editFieldsBtn2);
        imageView = view.findViewById(R.id.profileIv2);
        changeProfile = view.findViewById(R.id.changeProfileIv2);
        fab = view.findViewById(R.id.fab2);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        organizationNameEt.setEnabled(false);
        contactEmailEt.setEnabled(false);
        emailEt.setEnabled(false);
        addressEt.setEnabled(false);
        phoneNumberEt.setEnabled(false);
        descriptionEt.setEnabled(false);

        editBtn.setOnClickListener(v -> {
            if(!organizationNameEt.isEnabled()) {
                organizationNameEt.setEnabled(true);
                contactEmailEt.setEnabled(true);
                emailEt.setEnabled(true);
                addressEt.setEnabled(true);
                phoneNumberEt.setEnabled(true);
                descriptionEt.setEnabled(true);
            } else {
                organizationNameEt.setEnabled(false);
                contactEmailEt.setEnabled(false);
                emailEt.setEnabled(false);
                addressEt.setEnabled(false);
                phoneNumberEt.setEnabled(false);
                descriptionEt.setEnabled(false);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String organizationName = organizationNameEt.getText().toString().trim();
                String contactEmail = contactEmailEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String phoneNumber = phoneNumberEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();
                data.put(KEY_ON, organizationName);
                data.put(KEY_contactEmail, contactEmail);
                data.put(KEY_email, email);
                data.put(KEY_address, address);
                data.put(KEY_phoneNumber, phoneNumber);
                data.put(KEY_description, description);
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

                organizationNameEt.setEnabled(false);
                contactEmailEt.setEnabled(false);
                emailEt.setEnabled(false);
                addressEt.setEnabled(false);
                phoneNumberEt.setEnabled(false);
                descriptionEt.setEnabled(false);
            }
        });

        changeProfile.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_CODE);
        });


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: Something went wrong");
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String organizationName = documentSnapshot.getString(KEY_ON);
                    String contactEmail = documentSnapshot.getString(KEY_contactEmail);
                    String email = documentSnapshot.getString(KEY_email);
                    String address = documentSnapshot.getString(KEY_address);
                    String phoneNumber = documentSnapshot.getString(KEY_phoneNumber);
                    String description = documentSnapshot.getString(KEY_description);
                    String image = documentSnapshot.getString(KEY_photoUri);

                    organizationNameEt.setText(organizationName);
                    contactEmailEt.setText(contactEmail);
                    emailEt.setText(email);
                    addressEt.setText(address);
                    phoneNumberEt.setText(phoneNumber);
                    descriptionEt.setText(description);
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
        String options[] = {"Edit Profile Pic", "Edit Organization Name", "Edit Contact Email", "Edit Email", "Edit Address", "Edit Phone Number", "Edit Description"};

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
                    pd.setMessage("Updating Organization Name");
                    showNameAgeUpdateDialog("organizationName");
                }
                else if (which == 2) {
                    // Edit Last Name
                    pd.setMessage("Updating contact email");
                    showNameAgeUpdateDialog("contactEmail");
                }
                else if (which == 3) {
                    // Edit Age
                    pd.setMessage("Updating email");
                    showNameAgeUpdateDialog("email");
                }
                else if (which == 4) {
                    // Edit First Name
                    pd.setMessage("Updating address");
                    showNameAgeUpdateDialog("address");
                }
                else if (which == 5) {
                    // Edit Last Name
                    pd.setMessage("Updating phone number");
                    showNameAgeUpdateDialog("phoneNumber");
                }
                else if (which == 6) {
                    // Edit Age
                    pd.setMessage("Updating description");
                    showNameAgeUpdateDialog("description");
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