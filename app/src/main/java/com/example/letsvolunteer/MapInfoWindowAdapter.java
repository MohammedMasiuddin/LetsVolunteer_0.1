package com.example.letsvolunteer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "in info window";
    Context context;
    HashMap<Marker, EventsPost> hashMap = new HashMap<>();
    ImageView imageViewMap;

    public MapInfoWindowAdapter(Context context, HashMap<Marker, EventsPost> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        EventsPost eventsPost1 = hashMap.get(marker);
        View view = LayoutInflater.from(context).inflate(R.layout.map_window_info_layout,null);
        TextView textView = view.findViewById(R.id.mapEventName);
        textView.setText(eventsPost1.getEventName());

        TextView textView2 = view.findViewById(R.id.mapLocationAddress);
        textView2.setText(eventsPost1.getLocationAddress());

        TextView textView3 = view.findViewById(R.id.mapEventDate);
        textView3.setText(eventsPost1.getEventDate());

        imageViewMap = view.findViewById(R.id.mapEventImage);

        String s = eventsPost1.getImageUrlLists().get(0);
        Log.d(TAG, "getInfoWindow: "+ s);

        Picasso.get().load(s).into(imageViewMap, new Callback() {
            @Override
            public void onSuccess() {
                if (marker != null && marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    marker.showInfoWindow();

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}
