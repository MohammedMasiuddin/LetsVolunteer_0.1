package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.letsvolunteer.databinding.ActivityPlaceMapsBinding;

import java.io.IOException;
import java.util.List;

public class PlaceMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPlaceMapsBinding binding;
    TextView locationtext;
    String location;
    LatLng latLngTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView mapbackbutton = findViewById(R.id.mapbackbutton);
        ImageButton mapselectlocation = findViewById(R.id.mapselectlocation);
        locationtext = findViewById(R.id.locationtext);

        mapbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapselectlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("location",location);
                intent.putExtra("geocodes", latLngTemp);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43, -79);
        mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        mMap.setMaxZoomPreference(12.0f);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));

                try {
                    List<Address> temp = new Geocoder(getBaseContext()).getFromLocation(latLng.latitude, latLng.longitude, 1);
                    location = temp.get(0).getAddressLine(0);
                    latLngTemp = latLng;
                    locationtext.setText(temp.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto,10.0f));
    }
}