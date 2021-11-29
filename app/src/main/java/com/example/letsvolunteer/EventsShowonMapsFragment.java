package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class EventsShowonMapsFragment extends Fragment {

    private static final String TAG = "EventMapsFragment";
    ArrayList<EventsPost> eventsResults = new ArrayList<EventsPost>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
//

            eventsResults.forEach(eventsPost -> {
                LatLng sydney = new LatLng(eventsPost.getLocation().getLatitude(), eventsPost.getLocation().getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title(eventsPost.getLocationAddress()));
            });
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(moveCamera));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_events_showon_maps, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query dbref = db.collection("Events").limit(10);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.loadingspinner);
        dbref.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().forEach(
                    e -> {
                        eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                    }
            );
            progressDialog.dismiss();
            Log.d(TAG, "onCreateView: "+ eventsResults.size());
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }).addOnFailureListener(e ->{
            progressDialog.dismiss();
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        });


    }
}