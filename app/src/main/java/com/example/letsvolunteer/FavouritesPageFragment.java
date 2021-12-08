package com.example.letsvolunteer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = "FavouritesPageFragment";
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayList1 = new ArrayList();

    ListView myinterestList;
    ArrayList<String> categoriesinterest = new ArrayList<String>();
    ArrayList<String> categoriesinterestnew = new ArrayList<String>();

    public FavouritesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesPageFragment newInstance(String param1, String param2) {
        FavouritesPageFragment fragment = new FavouritesPageFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourites_page, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.myfavouriterecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        TextView myInterestTxt = view.findViewById(R.id.myInterestTxt);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.loadingspinner);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<EventsPost> eventsResults = new ArrayList<EventsPost>();
        Query dbref = db.collection("Events").limit(10);

        TextView myEventsTxt = view.findViewById(R.id.myEventsTxt);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Context context = getContext();

        LinearLayout linearLayoutMyinterest = view.findViewById(R.id.myInterests);

        myinterestList = view.findViewById(R.id.myinterestlist);

        MaterialButton buttonMyInterest = view.findViewById(R.id.AddinterestBtn);
        MaterialAutoCompleteTextView addinterestEdittext = view.findViewById(R.id.AddinterestEdittext);
        TextInputLayout AddinterestTextInputlayouttext = view.findViewById(R.id.AddinterestTextInputlayouttext);

        addinterestEdittext.setOnClickListener(v -> {
            categoriesinterestnew.clear();
            categoriesinterest.forEach(s -> {
                if (!arrayList1.contains(s)){
                    categoriesinterestnew.add(s);
                }
            });
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, categoriesinterestnew);
            addinterestEdittext.setAdapter(arrayAdapter);
            addinterestEdittext.showDropDown();
        });


        db.collection("EventCatorgy").document("CategoriesDoc").get()
                .addOnSuccessListener(documentSnapshot -> {
                    documentSnapshot.getData();
                    Log.d(TAG, "onCreateView: fetch categories"+ documentSnapshot.getData().get("Categories"));

                    categoriesinterest = (ArrayList<String>) documentSnapshot.getData().get("Categories");

                    ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, categoriesinterest);
                    addinterestEdittext.setAdapter(arrayAdapter);
                });

        AddinterestTextInputlayouttext.setEndIconOnClickListener(v -> {
                    Log.d(TAG, "onCreateView: ");
                    buttonMyInterest.setVisibility(View.VISIBLE);
                    db.collection("Volunteer").document(user.getUid())
                            .update("MyInterestCategories",
                                    FieldValue.arrayUnion(addinterestEdittext.getText().toString()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    db.collection("Volunteer").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.getData() != null) {
                                            myEventsTxt.setText("My Interested Events");
                                            if (documentSnapshot.getData().get("MyInterestCategories") != null) {
                                                arrayList1 = (ArrayList) documentSnapshot.getData().get("MyInterestCategories");
                                                Log.d(TAG, "onCreateView: volunteer category interest " + arrayList1);
                                                InterestItemAdapter arrayAdapter = new InterestItemAdapter(context, R.layout.interest_list_item,R.id.interestlistitem, arrayList1);
                                                myinterestList.setAdapter(arrayAdapter);
                                                myinterestList.measure(0, 0);
                                                int totalheight = myinterestList.getMeasuredHeight() * arrayList1.size() + myinterestList.getDividerHeight() * (arrayList1.size() - 1);
                                                myinterestList.getLayoutParams().height = totalheight;
                                                AddinterestTextInputlayouttext.setVisibility(View.GONE);


                                            }

                                        }
                                    });

                                }
                            });
                });
        
        buttonMyInterest.setOnClickListener(v -> {
            AddinterestTextInputlayouttext.setVisibility(View.VISIBLE);
            buttonMyInterest.setVisibility(View.GONE);
        });

        db.collection("Organizers").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot1 -> {
                    if (documentSnapshot1.getData() != null ){
                        myEventsTxt.setText("My Events Posted");
                        myInterestTxt.setVisibility(View.GONE);
                        Query dbreforg = db.collection("Events")
                                .whereEqualTo("organiserid",user.getUid());

                        dbreforg.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                queryDocumentSnapshots.getDocuments().forEach(
                                        e -> {
                                            eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                                        }
                                );

                                EventListsAdapter eventListsAdapter = new EventListsAdapter(eventsResults) {
                                    @Override
                                    public void navigatetodetails(EventsPost eventsPost) {
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container_view_tag, EventDetailsFragment.newInstance(eventsPost.eventid))
                                                .addToBackStack(null)
                                                .commit();
                                    }

                                    @Override
                                    public void loadNewEventsfnc() {

                                    }

                                };
                                recyclerView.setAdapter(eventListsAdapter);
                                progressDialog.dismiss();
                                Log.d(TAG, "onSuccess: "+ eventsResults);
                                linearLayoutMyinterest.setVisibility(View.GONE);

                            }
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                        });

                    }
                    else{
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> progressDialog.dismiss());

        db.collection("Volunteer").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData() != null) {
                myEventsTxt.setText("My Interested Events");
                if (documentSnapshot.getData().get("MyInterestCategories") != null){
                    arrayList1 = (ArrayList) documentSnapshot.getData().get("MyInterestCategories");
                    Log.d(TAG, "onCreateView: volunteer category interest "+ arrayList1);
                    InterestItemAdapter arrayAdapter = new InterestItemAdapter(context, R.layout.interest_list_item, R.id.interestlistitem, arrayList1);
                    myinterestList.setAdapter(arrayAdapter);
                    myinterestList.measure(0,0);
                    int totalheight = myinterestList.getMeasuredHeight() * arrayList1.size() + myinterestList.getDividerHeight() * (arrayList1.size() - 1);
                    myinterestList.getLayoutParams().height = totalheight;

                }

                if (documentSnapshot.getData().get("MyEventsignup") != null){
                    arrayList = (ArrayList) documentSnapshot.getData().get("MyEventsignup");
                    Query dbrefvol = db.collection("Events").whereIn(FieldPath.documentId(),arrayList);
                    dbrefvol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            queryDocumentSnapshots.getDocuments().forEach(
                                    e -> {
                                        eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                                    }
                            );

                            EventListsAdapter eventListsAdapter = new EventListsAdapter(eventsResults) {
                                @Override
                                public void navigatetodetails(EventsPost eventsPost) {
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container_view_tag, EventDetailsFragment.newInstance(eventsPost.eventid))
                                            .addToBackStack(null)
                                            .commit();
                                }

                                @Override
                                public void loadNewEventsfnc() {

                                }

                            };
                            recyclerView.setAdapter(eventListsAdapter);
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: "+ eventsResults);


                        }
                    }).addOnFailureListener(e -> progressDialog.dismiss());

                }


            }
        }).addOnFailureListener(e -> progressDialog.dismiss());

//        dbref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                queryDocumentSnapshots.getDocuments().forEach(
//                        e -> {
//                            eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
//                        }
//                );
//
//                EventListsAdapter eventListsAdapter = new EventListsAdapter(eventsResults) {
//                    @Override
//                    public void navigatetodetails(EventsPost eventsPost) {
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container_view_tag, EventDetailsFragment.newInstance(eventsPost.eventid))
//                                .addToBackStack(null)
//                                .commit();
//                    }
//
//                    @Override
//                    public void loadNewEventsfnc() {
//
//                    }
//
//                };
//                recyclerView.setAdapter(eventListsAdapter);
//                progressDialog.dismiss();
//                Log.d(TAG, "onSuccess: "+ eventsResults);
//
//
//            }
//        });

        return view;
    }
}