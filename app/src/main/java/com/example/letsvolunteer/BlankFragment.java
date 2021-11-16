package com.example.letsvolunteer;

import android.Manifest;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Firebase drf";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Boolean isPermissionGrandted = true;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    int requestCode = 12345;
    ImageView imageView;
    LinearLayout linearLayout;
//    GridLayout gridLayout;


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



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbref = firebaseDatabase.getReference("Events");


        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        EventsPost eventPost = new EventsPost("Somename","somedescription","098788908","someemail@gmail.com");

        Button upload = view.findViewById(R.id.uploadbutton);
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dbref.setValue("Testing");

                db.collection("users")
                        .document("user1")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: ");
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
            startActivityForResult(Intent.createChooser(intent,"Choose from bellow"),requestCode);
        }else {
            ActivityCompat.requestPermissions(getActivity(),permissions,requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//                super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                View view2 = View.inflate(getContext(),R.layout.imagesampletemplate,linearLayout);
                ImageView image4 = view2.findViewById(R.id.imageViewsample);
                image4.setImageBitmap(bitmap);

                ImageView tempImageView = new ImageView(getContext());


                int height = getContext().getResources().getDimensionPixelSize(R.dimen.imageheight1);
                int width = getContext().getResources().getDimensionPixelSize(R.dimen.imagewidth1);
                int margin = getContext().getResources().getDimensionPixelSize(R.dimen.imagemargin);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                layoutParams.setMargins(margin,margin,margin,margin);

                tempImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT ));

                CardView cardView = new CardView(getContext());
                cardView.setLayoutParams(layoutParams);
                cardView.addView(tempImageView);


                linearLayout.addView(cardView);

                tempImageView.setImageBitmap(bitmap);
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