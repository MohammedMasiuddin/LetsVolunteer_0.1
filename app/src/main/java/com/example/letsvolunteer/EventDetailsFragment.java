package com.example.letsvolunteer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EventDetails";


    // TODO: Rename and change types of parameters
    private String mParam1;
    EventsPost event;
    private String mParam2;
    int likecounter = 0;
    ArrayList arrayList = new ArrayList();


    public EventDetailsFragment() {
        // Required empty public constructor
    }

    public static EventDetailsFragment newInstance(String param1) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        ImageView emailicon = view.findViewById(R.id.emailicon);
        ImageView phoneicon = view.findViewById(R.id.phoneicon);
        RecyclerView recyclerView = view.findViewById(R.id.eventImagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dbref = db.collection("Events").document(mParam1);
        MaterialButton eventsigninBtn = view.findViewById(R.id.eventsigninBtn);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Context context = getContext();



        eventsigninBtn.setOnClickListener(v -> {
            db.collection("Volunteer").document(user.getUid())
                    .update("MyEventsignup",
                    FieldValue.arrayUnion(mParam1)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+ " event is not there");
                    Toast.makeText(getContext(),
                            "You are not allowed to sign an event create a voluteer account",
                            Toast.LENGTH_LONG).show();
                    eventsigninBtn.setEnabled(false);
                }
            });
        });

        ImageView likebtn = view.findViewById(R.id.favouriteBtn);
        ImageView chatBtn = view.findViewById(R.id.chatBtn);
        TextView likescount = view.findViewById(R.id.likescount);
        likescount.setText("Likes :"+ likecounter);


//
//        // code to check wheather he is volunteer
//        db.collection("Volunteer").document(user.getUid()).get().addOnSuccessListener(documentSnapshot1 -> {
//
//            Log.d(TAG, "onClick: "+ documentSnapshot1.getData() );
//            arrayList = (ArrayList) documentSnapshot1.getData().get("likeEvents");
//
//            if (! arrayList.contains(mParam1)){
//                db.collection("Events").document(mParam1)
//                        .update("likes", FieldValue.increment(1))
//                        .addOnCompleteListener(command -> {
//                            likescount.setText("Likes :" + (event.getLikes() + 1 ));
//                        });
//                db.collection("Volunteer").document(user.getUid())
//                        .update("LikeEvents",
//                                FieldValue.arrayUnion(mParam1));
//            }
//
//
//        });
//



        chatBtn.setOnClickListener(v -> {

            dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String hisUid = documentSnapshot.getString("organiserid");
                    Log.d("Chat", "onSuccess:" + hisUid);
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("hisUid", hisUid);
                    context.startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: No data Records found");
                }
            });

        });

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                db.collection("Volunteer").document(user.getUid()).get().addOnSuccessListener(documentSnapshot1 -> {
                    arrayList = new ArrayList();

                    Log.d(TAG, "onClick: "+ documentSnapshot1.getData() );
                    if ( documentSnapshot1.getData().get("LikeEvents") != null){
                        arrayList = (ArrayList) documentSnapshot1.getData().get("LikeEvents");
                        Log.d(TAG, "onClick: " + arrayList);
                    }


                    if (! arrayList.contains(mParam1)){
                        Log.d(TAG, "Add like to events: ");
                        db.collection("Events").document(mParam1)
                                .update("likes", FieldValue.increment(1))
                                .addOnCompleteListener(command -> {
                                    likecounter++;
                                    likescount.setText("Likes :" + likecounter);
                                });
                        db.collection("Volunteer").document(user.getUid())
                                .update("LikeEvents",
                                        FieldValue.arrayUnion(mParam1));
                    }
                    else {
                        Log.d(TAG, "Already liked ");
                        Toast.makeText(context, " Already Liked ", Toast.LENGTH_LONG).show();
                        likebtn.setEnabled(false);
                    }


                }).addOnFailureListener(e -> {
                    Toast.makeText(context, " You are not allowed to sign an event create a voluteer account ", Toast.LENGTH_LONG).show();
                    likebtn.setEnabled(false);
                });


            }
        });


        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess: " + documentSnapshot );

                EventsPost event = documentSnapshot.toObject(EventsPost.class);

                ImageListsAdapter imageListsAdapter = new ImageListsAdapter((ArrayList<String>) event.getImageUrlLists());
                TextView evtNameText = view.findViewById(R.id.eveNameTxt);
                evtNameText.setText(event.getEventName());

                TextView descContent = view.findViewById(R.id.descContent);
                descContent.setText(event.getEventDescription());

                TextView dateTXt = view.findViewById(R.id.dateTxt);
                dateTXt.setText(event.getEventDate());

                TextView likescount = view.findViewById(R.id.likescount);
                likescount.setText("Likes :"+ event.getLikes());

                TextView locationAddress = view.findViewById(R.id.locationAddress);
                locationAddress.setText(event.getLocationAddress());

                likecounter = (int) event.getLikes();

                TextView organisername = view.findViewById(R.id.organisername);
                db.collection("Organizers").document(event.getOrganiserid()).get()
                        .addOnSuccessListener(documentSnapshot1 -> {
                    if (documentSnapshot1.getData().get("organizationName") != null ){
                        organisername.setText(documentSnapshot1.getData().get("organizationName").toString());
                    }
                });


                getParentFragmentManager().beginTransaction().replace(R.id.showmapfragment,new MapShowLocationFragment(event.getLocation())).commit();



                recyclerView.setAdapter(imageListsAdapter);
                TextView textView = view.findViewById(R.id.textView3);
                textView.setText("Images: "+ event.getImageUrlLists().size());



                emailicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + event.getEmailId()));
                        startActivity(intent);
                    }
                });

                phoneicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + event.getPhoneNumber()));
                        startActivity(intent);
                    }
                });
            }
        });



        return view;
    }
}