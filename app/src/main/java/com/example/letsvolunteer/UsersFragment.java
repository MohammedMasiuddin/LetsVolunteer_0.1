package com.example.letsvolunteer;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UsersFragment extends Fragment {

    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUser> userList;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        // Initiate
        recyclerView = view.findViewById(R.id.users_recyclerView);
        //set the properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        userList = new ArrayList<>();

        getAllUsers();

        return view;
    }


    private void getAllUsers() {
        //get current user
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //get path of database named "Volunteers" containing Users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        DocumentReference dref = db.collection("Volunteer").document(fUser.getUid());
//        ListenerRegistration lref = db.collection("Volunteer").document(fUser.getUid())
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.d(TAG, "onEvent: Something went wrong");
//                        }
//
//                        int count = userList.size();
//
//                        if (value != null) {
//
//                            if (value != null && value.exists()) {
//                                ModelVolunteer modelVolunteer = new ModelVolunteer();
//                                modelVolunteer.age = value.getString("age");
//                                modelVolunteer.email = value.getString("email");
//                                modelVolunteer.firstName = value.getString("firstName");
//                                modelVolunteer.lastName = value.getString("lastName");
//                                modelVolunteer.photoUri = value.getString("photoUri");
//                                userList.add(modelVolunteer);
//
//                            }
//                            if (count == 0) {
//                                adapterUsers = new AdapterUsers(getContext(), userList);
//                                adapterUsers.notifyDataSetChanged();
//                                recyclerView.setAdapter(adapterUsers);
//                            } else {
//                                adapterUsers.notifyItemRangeInserted(userList.size(), userList.size());
//                                recyclerView.smoothScrollToPosition(userList.size() - 1);
//                            }
//                            recyclerView.setVisibility(View.VISIBLE);
//                        }
//                        }
//                    });




//        get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    if (!modelUser.getUid().equals(fUser.getUid())) {
                        userList.add(modelUser);
                    }

                    //adapter
                    adapterUsers = new AdapterUsers(getActivity(), userList);

                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}