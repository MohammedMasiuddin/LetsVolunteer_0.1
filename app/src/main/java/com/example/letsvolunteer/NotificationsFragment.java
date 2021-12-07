package com.example.letsvolunteer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Notifications Fragment --> ";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<NewNotification> notificationsList = new ArrayList<NewNotification>();

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Query dbref = db.collection("Events").limit(10);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query dbref =  db.collection("Notification").whereEqualTo("eventCategory","soothing");
        dbref.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d(TAG, "onCreateView: "+ queryDocumentSnapshots.getDocuments());
            notificationsList.clear();
            queryDocumentSnapshots.getDocuments().forEach(
                    e -> {
                        notificationsList.add(new NewNotification((HashMap<String, Object>) e.getData()));
                    });
            Log.d(TAG, "onCreateView: "+ notificationsList);
        });



        Log.d(TAG, "onCreateView: ");
        return view;
    }
}