package com.example.letsvolunteer;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Events";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
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
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);


        MaterialButton btn = view.findViewById(R.id.addEventsbtn);
        RecyclerView recyclerView = view.findViewById(R.id.EventListrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.loadingspinner);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<EventsPost> eventsResults = new ArrayList<EventsPost>();
        Query dbref = db.collection("Events").limit(10);
        dbref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                    .replace(R.id.fragment_container_view_tag, new EventDetailsFragment()).commit();
                }
            };
            recyclerView.setAdapter(eventListsAdapter);
            progressDialog.dismiss();
            Log.d(TAG, "onSuccess: "+ eventsResults);

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActionBarTitle mainActivity = (SetActionBarTitle) getActivity();
                mainActivity.ChangeActionBarTitle("Create Event");
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new BlankFragment()).addToBackStack(null).commit();            }
        });


        return view;

    }
}