package com.example.letsvolunteer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        ButterKnife.bind(this);

    }
}