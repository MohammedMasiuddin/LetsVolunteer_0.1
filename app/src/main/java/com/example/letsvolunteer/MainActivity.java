package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView profileTv;
    int AUTOCOMPLETE_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("TAG", "onNavigationItemSelected: "+ item.getItemId());
                switch(item.getItemId()) {
                    case R.id.page_1 :
                        // code

                        return true;

                    case R.id.page_2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                                new EventListFragment()).commit();
                        // code block
                        return true;

                    case R.id.page_5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                                new vAccountInfoFragment()).commit();
                        // code block
                        return true;

                    default:
                        // code block
                        break;
                }


                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                new EventListFragment()).commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
//                new BlankFragment()).commit();


    }
    public void init(){
        try {
            String apiKey = getString(R.string.google_maps_key);
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
            }
        } catch (Exception ignored) {
        }
    }
    public void location(){
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user is signed in, stay in this page
            profileTv.setText(user.getEmail());

        }
        else {
            // user not signed in, goto main activity
            startActivity(new Intent(MainActivity.this, Authentication.class));
            finish();
        }
    }

//
//    @Override
//    protected void onStart() {
//        checkUserStatus();
//        super.onStart();
//    }
//
//    /

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get the id
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }


        return super.onOptionsItemSelected(item);
    }
}