package com.example.letsvolunteer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Firebase drf";
    private static final int RESULT_OK = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Boolean isPermissionGrandted = true;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    String[] permissions1 = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    int Request_Code = 12345;
    ImageView imageView;
    LinearLayout linearLayout;
    ArrayList<byte[]> datalist = new ArrayList<byte[]>() ;
    GridLayout gridLayout;
    Uri downloadUri;
    String documentid;
    int PLACE_REQUEST = 1;
    TextView textViewLocation;

    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        ImageButton btn = (ImageButton) view.findViewById(R.id.AddimageButton);
//        imageView = view.findViewById(R.id.uploadimageView);
        linearLayout = view.findViewById(R.id.uploadImagesContainer);
        TextView dateselected = view.findViewById(R.id.dateselected);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();

        Button datebtn = view.findViewById(R.id.containedButtonfordate);
        textViewLocation = view.findViewById(R.id.eventLocation);
        Button locationaddbtn = view.findViewById(R.id.locationPicker);
        locationaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
//                    try {
//                        startActivityForResult(intentBuilder.build(getActivity()), PLACE_REQUEST);
//                    } catch (GooglePlayServicesRepairableException e) {
//                        e.printStackTrace();
//                    } catch (GooglePlayServicesNotAvailableException e) {
//                        e.printStackTrace();
//                    }
//
            }
        });


        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: databtn");
                datePicker.show(getParentFragmentManager(),"MATERIAL");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateselected.setText("Event Date : "+ datePicker.getHeaderText());
                        dateselected.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

//        gridLayout = view.findViewById(R.id.uploadImagesContainer);


//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference dbref = firebaseDatabase.getReference("Events");

//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        TextInputEditText textEventName = view.findViewById(R.id.EventNameEnteredittext);
        TextInputEditText textEventDescription = view.findViewById(R.id.Evendescriptionentered);
        TextInputEditText textEventphone = view.findViewById(R.id.PhonenumberEnter);
        TextInputEditText textemail = view.findViewById(R.id.emailgiven);




        Button upload = view.findViewById(R.id.uploadbutton);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainImagesRef = storageRef.child("Events/firstimagetesting111.jpg");


        upload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                EventsPost eventPost = new EventsPost(textEventName.getText().toString(),
                        textEventDescription.getText().toString(),
                        textEventphone.getText().toString(),textemail.getText().toString(), user.getUid()
                );

                if (datalist.size() == 0){
                    Log.d(TAG, "onCreateView: "+ textEventName.getText().toString() );
                    Log.d(TAG, "onCreateView: "+ textEventDescription.getText().toString() );
                    Log.d(TAG, "onCreateView: "+ textEventphone.getText().toString() );
                    Log.d(TAG, "onCreateView: "+ textemail.getText().toString() );

                    Log.d(TAG, "onClick: "+ eventPost);

                    return;
                }


                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.loadingspinner);
//                progressDialog.getWindow().setBackgroundDrawable(R.color.transparent);


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Events")
                        .add(eventPost)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                               documentid = documentReference.getId();

                               for (int i = 0 ; i < datalist.size(); i++){
                                   StorageReference mountainImagesRef = storageRef.child("Events/firstimagetesting"+ i +".jpg");
                                   int finalI = i;
                                   mountainImagesRef.putBytes(datalist.get(i))
                                           .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                               @Override
                                               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                   if (!task.isSuccessful()) {
                                                       throw task.getException();
                                                   }

                                                   // Continue with the task to get the download URL
                                                   return mountainImagesRef.getDownloadUrl();


                                               }
                                           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Uri> task) {
                                           if (task.isSuccessful()) {
                                               downloadUri = task.getResult();
                                               eventPost.getImageUrlLists().add(downloadUri.toString());
                                               Log.d(TAG, "onComplete: "+ downloadUri);
                                               db.collection("Events")
                                                           .document(documentid)
                                                       .update("imageUrlLists",
                                                               FieldValue.arrayUnion(downloadUri.toString()));



                                           } else {
                                               // Handle failures
                                               // ...
                                               Log.d(TAG, "onComplete: Failed to upload");
                                           }
                                            if ( finalI == datalist.size() -1 ){
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext().getApplicationContext(),"Data is uploaded ",Toast.LENGTH_LONG).show();
                                            }
                                       }
                                   });
                               }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });


            }
        });

        return view;
    }




    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            isPermissionGrandted = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Choose from bellow"),Request_Code);
        }else {
            ActivityCompat.requestPermissions(getActivity(),permissions,Request_Code);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//                super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PLACE_REQUEST){
//            if (resultCode == RESULT_OK ){
//                Place place = PlacePicker.getPlace(data,getContext());
//                textViewLocation.setText("" +place.getLatLng().latitude + place.getLatLng().longitude);
//            }
//        }

        if (data != null && data.getData() != null && requestCode == Request_Code ){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);



                View view2 = getLayoutInflater().inflate(R.layout.imagesampletemplate,linearLayout,false);
                ImageView image4 = view2.findViewById(R.id.imageViewsample);
                image4.setImageBitmap(bitmap);
                linearLayout.addView(view2);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] data1 = stream.toByteArray();

                datalist.add(data1);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(getContext().getApplicationContext(),"nothing selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isPermissionGrandted = true;

        }else{
            return;
        }
    }

}