package com.example.letsvolunteer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    ArrayList<OrganiserNotifcation> organiserNotifcations = new ArrayList<OrganiserNotifcation>();

    ArrayList<String> manageNotifications = new ArrayList<String>();
    TextView textView;

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

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.actionBar.setTitle("Notifications");
        MenuItem menuItem = mainActivity.menuItemNotification;
        View view1 = mainActivity.menuItemNotification.getActionView();
        textView = view1.findViewById(R.id.cart_badge);
        Log.d(TAG, " menu text : " + textView);


        RecyclerView listNotification = view.findViewById(R.id.notificationlistview);
        listNotification.setLayoutManager(new LinearLayoutManager(getContext()));

        db.collection("Volunteer").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            notificationsList.clear();

            if (documentSnapshot.getData() != null && documentSnapshot.getData().get("MyInterestCategories") != null) {
                if (documentSnapshot.getData().get("MyManageNotifications") != null) {
                    manageNotifications = (ArrayList) documentSnapshot.getData().get("MyManageNotifications");
                }
                ArrayList<String> arrayListCate = (ArrayList<String>) documentSnapshot.getData().get("MyInterestCategories");
                Log.d(TAG, " arraylsit of categories : " + arrayListCate);
                Query dbref =  db.collection("Notification").whereIn("eventCategory", arrayListCate);
                dbref.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, " doc count : "+ queryDocumentSnapshots.getDocuments());
                    queryDocumentSnapshots.getDocuments().forEach(
                            e -> {
                                notificationsList.add(new NewNotification((HashMap<String, Object>) e.getData()));
                            });

                    Log.d(TAG, "notification size: "+ notificationsList.size());
                    NotificationListsAdapter notificationListsAdapter = new NotificationListsAdapter(notificationsList,manageNotifications) {
                        @Override
                        public void navigatetoeventdetails(String eventid) {
                            db.collection("Volunteer").document(user.getUid()).update("MyManageNotifications", FieldValue.arrayUnion(eventid));

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_view_tag, EventDetailsFragment.newInstance(eventid))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    };
                    listNotification.setAdapter(notificationListsAdapter);
                    Log.d(TAG, " ++ : " + notificationsList.size());
                    Log.d(TAG, " bagde " + (notificationsList.size() - manageNotifications.size()) );
                    textView.setText("" + (notificationsList.size() - manageNotifications.size()));
                });



            }
        });

        db.collection("Organizers").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            notificationsList.clear();
            if (documentSnapshot.getData() != null) {

                Query dbref =  db.collection("OrganiserNotification").whereEqualTo("organiserid", user.getUid());
                dbref.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    queryDocumentSnapshots.getDocuments().forEach(
                            e -> {
                                organiserNotifcations.add(new OrganiserNotifcation((HashMap<String, Object>) e.getData()));
                            });

                    OrganiserNotificationListsAdapter organiserNotificationListsAdapter = new OrganiserNotificationListsAdapter(organiserNotifcations);
                    listNotification.setAdapter(null);
                    listNotification.setAdapter(organiserNotificationListsAdapter);
                    textView.setText("" + (organiserNotifcations.size()));
                });



            }
        });


        return view;
    }
}