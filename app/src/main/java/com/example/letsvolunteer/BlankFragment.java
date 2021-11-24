package com.example.letsvolunteer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.model.LatLng;
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
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    String[] permissions2 = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    int Request_Code = 12345;
    ImageView imageView;
    LinearLayout linearLayout;
    ArrayList<byte[]> datalist = new ArrayList<byte[]>() ;
    GridLayout gridLayout;
    Uri downloadUri;
    String documentid;
    int PLACE_REQUEST = 1;
    TextView textViewLocation;
    TextView showimagesuploaded;
    String count = "image:  ";
    ArrayList<String> categoriesinterest;
    Boolean isvalid;
    ImageView mapimageview;
    String dateselect1 = "";

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

        mapimageview = view.findViewById(R.id.mapimage);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                .build();
        showimagesuploaded = view.findViewById(R.id.showimagesuploaded);

        Button datebtn = view.findViewById(R.id.containedButtonfordate);
        textViewLocation = view.findViewById(R.id.eventLocation);
        Button locationaddbtn = view.findViewById(R.id.locationPicker);
        showimagesuploaded.setText("image:  "+datalist.size());

        locationaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                    ActivityCompat.requestPermissions( getActivity(),
                            permissions2, 1
                    );
                    return;
                }
                mapimageview.setVisibility(View.VISIBLE);
                startActivityForResult(new Intent(((MainActivity) getActivity()), PlaceMapsActivity.class),1014);
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
                        Log.d(TAG, "onPositiveButtonClick: "+ selection.toString());
                        dateselected.setText("Date : "+ datePicker.getHeaderText());
                        dateselect1 = datePicker.getHeaderText();
                        dateselected.setVisibility(View.VISIBLE);
                    }
                });
            }
        });



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

        TextInputLayout eventname = view.findViewById(R.id.EventNameEnter);
        TextInputLayout eventdescription = view.findViewById(R.id.textField);
        TextInputLayout eventphonenumber = view.findViewById(R.id.OrganiserPhoneEnter);
        TextInputLayout eventemail = view.findViewById(R.id.Enteremail);


        AutoCompleteTextView selectCatorgyinterst = view.findViewById(R.id.selectCatorgyinterst);

        // email validation
        textemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) v).getText().toString().length() <= 3 ){
                    eventemail.setError("enter valid email");
                }
            }
        });
        textemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z0-9!@#\\$%\\^\\&*\\)\\(+=._-]+@[a-zA-Z0-9]+\\.[a-zA-z0-9]+") ){
                    eventemail.setError("Enter value is not email");
                    return;
                }
                eventemail.setError(null);
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Phone number edit text validations
        textEventphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) v).getText().toString().length() < 10 ){
                    eventphonenumber.setError(" enter valid phone number ");
                }
            }
        });
        textEventphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 10 || s.equals("") ){
                    eventphonenumber.setError("phone number should be greater then 10 char");
                    return;
                }
                if (!s.toString().matches("[0-9+]+") ){
                    eventphonenumber.setError("phone number should be digits");
                    return;
                }


                eventphonenumber.setError("");
                return ;

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Event text description validation
        textEventDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) v).getText().toString().length() <= 3 ){
                    eventdescription.setError("Description should be greater then 3 char");
                }
            }
        });
        textEventDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 3 || s.equals("") ){
                    eventdescription.setError("Name should be greater then 3 char");
                    return;
                }

                eventdescription.setError("");
                return ;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Event Name validations
        textEventName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) v).getText().toString().length() <= 3 ){
                    eventname.setError("Name should be greater then 3 char");
                }
            }
        });
        textEventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 3 || s.equals("") ){
                    eventname.setError("Name should be greater then 3 char");
                    return;
                }
                if (!s.toString().matches("[a-zA-Z\\s0-9]+") ){
                    eventname.setError("Name should be alphabatic");
                    return;
                }
                int count3 = 0;
                for (char temp : s.toString().toCharArray()) {

                    if (temp == ' '  ){
                        count3++;
                    }
                    else{
                        count3 = 0;
                    }

                    if ( count3 > 1 ){
                        eventname.setError("Name should not have more than one blank");
                        return;
                    }
                }


                eventname.setError("");
                return ;
            }
        });


        Button upload = view.findViewById(R.id.uploadbutton);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference mountainImagesRef = storageRef.child("Events/firstimagetesting111.jpg");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // autotext completion fetch from firestore
        db.collection("EventCatorgy").document("CategoriesDoc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "onCreateView: "+ task.getResult().getData().get("Categories").getClass());
                        categoriesinterest = (ArrayList<String>) task.getResult().getData().get("Categories");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,categoriesinterest);
                        selectCatorgyinterst.setAdapter(arrayAdapter);
                    }
                });

        // upload btn clicked
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(datalist.size() == 0) {
                    new AlertDialog.Builder(getContext()).setTitle("Alert")
                            .setMessage("Event cannot be without image please upload images")
                            .setCancelable(true)
                            .create().show();
                    return;
                }
                if (eventname.getError() != "" && eventname.isDirty() || eventdescription.getError() != ""
                        && eventdescription.isDirty() || dateselected.getText().toString() != "" ||
                        eventphonenumber.getError() != "" && eventphonenumber.isDirty() ||
                        eventemail.isDirty() && eventemail.getError() != "" ||
                        selectCatorgyinterst.isDirty() && selectCatorgyinterst.getText().toString().length() > 0 ||
                        textViewLocation.getText().toString().length() > 0 ){

                    Log.d(TAG, "onClick: "+ eventdescription.isDirty());
                    new AlertDialog.Builder(getContext()).setTitle("Alert")
                            .setMessage("please enter valid input to textfields")
                            .setCancelable(true)
                            .create().show();
                    return;
                }


                // creating post object
                EventsPost eventPost = new EventsPost(textEventName.getText().toString(),
                        textEventDescription.getText().toString(),
                        textEventphone.getText().toString()
                        ,textemail.getText().toString(),
                        user.getUid(),
                        dateselect1,
                        selectCatorgyinterst.getText().toString()
                );

                // assign spinner
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.loadingspinner);
//                progressDialog.getWindow().setBackgroundDrawable(R.color.transparent);

                if(!categoriesinterest.contains(selectCatorgyinterst.getText().toString()) ){
                    Log.d(TAG, "onClick:  treudskdjdfg nm;ldfm ");
                    db.collection("EventCatorgy").document("CategoriesDoc")
                            .update("Categories",
                                    FieldValue.arrayUnion(selectCatorgyinterst.getText().toString()));
                }

                db.collection("Events")
                        .add(eventPost)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                               documentid = documentReference.getId();

                               for (int i = 0 ; i < datalist.size(); i++){
                                   StorageReference mountainImagesRef = storageRef.child("Events/" + UUID.randomUUID().toString() +".jpg");
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

        if (requestCode == 1014 && resultCode == RESULT_OK){
//            Log.d(TAG, "onActivityResult: " + data.getStringExtra("location"));
            Log.d(TAG, "onActivityResult: "+ data.getExtras());
            if (data.getExtras().get("geocodes") != null){
                textViewLocation.setText("Latitude :"+ ((LatLng) data.getExtras().get("geocodes")).latitude
                        + "\n" + "Longitude : " + ((LatLng) data.getExtras().get("geocodes")).longitude );
                textViewLocation.setVisibility(View.VISIBLE);
                String path = (String) data.getExtras().get("mapimage");
                Log.d(TAG, "onActivityResult: "+ (String) data.getExtras().get("mapimage"));

//                Glide.with(getActivity()).clear(mapimageview);
                Glide.with(getActivity())
                        .load(path)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(mapimageview);


                return;
            }
        }

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
                showimagesuploaded.setText(count + datalist.size());

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