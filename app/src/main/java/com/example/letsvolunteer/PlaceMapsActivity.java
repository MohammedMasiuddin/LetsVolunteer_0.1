package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.letsvolunteer.databinding.ActivityPlaceMapsBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;
import java.util.List;

public class PlaceMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPlaceMapsBinding binding;
    TextView locationtext;
    String location;
    LatLng latLngTemp;
    Bitmap mapimage ;
    boolean flag = false;

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

                if (flag){

                    Log.d("TAG", "onClick: start intend");

                    Intent intent = new Intent();
                    intent.putExtra("geocodes", latLngTemp);
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/mapimage.png";
                    File f3 = new File(path);

                    OutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(f3,false);
                        mapimage.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                        outStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("mapimage", path);
                    setResult(RESULT_OK,intent);
                    finish();
                    Log.d("TAG", "onClick: finished intent");
                }
                else{
                    // add alert here to select location
                     new AlertDialog.Builder(PlaceMapsActivity.this)
                            .setTitle("Map location not selected")
                            .setMessage("Please Tap on the map to select the location of the map " +
                                    "and tap on marker for place address")
                            .setCancelable(true)
                             .create()
                            .show();
                }
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
                flag = true;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                locationtext.setText("Latitude : "+ String.format("%.2f", latLng.latitude)
                        + ", Longitude : "+ String.format("%.2f", latLng.longitude));
                latLngTemp = latLng;
                Log.d("TAG", "onMapClick: ");

                mMap.snapshot(bitmap -> {

                    mapimage = bitmap;
                    Log.d("TAG", "onMapClick: image snapshot taken");
                });
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                List<Address> addressList = null;
                try {
                    addressList = new Geocoder(getApplicationContext()).getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,1);
                    marker.setTitle(addressList.get(0).getAddressLine(0).toString());
                    Log.d("TAG", "onMarkerClick: when marker touch " );

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto,10.0f));
    }
}