package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("TAG", "onNavigationItemSelected: "+ item.getItemId());
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new BlankFragment()
//            @Override
//            public void checkpermissions() {
//                if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                    isPermissionGrandted = true;
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,"Choose from bellow"),requestCode);
//                }else {
//                    ActivityCompat.requestPermissions(getActivity(),permissions,requestCode);
//                }
//            }
//
//            @Override
//            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////                super.onActivityResult(requestCode, resultCode, data);
//                if (data != null){
//                    Uri filepath = data.getData();
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(filepath);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        ImageView imageView = findViewById(R.id.uploadimageView);
//                        imageView.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    Toast.makeText(getContext().getApplicationContext(),"nothing selected",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    isPermissionGrandted = true;
//
//                }else{
//                    return;
//                }
//            }

        ).commit();
    }
}