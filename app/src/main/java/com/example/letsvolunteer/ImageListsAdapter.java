package com.example.letsvolunteer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageListsAdapter extends RecyclerView.Adapter<ImageListsAdapter.ViewHolder> {

    private ArrayList<String> images;

    public ImageListsAdapter(ArrayList<String> images) {
        this.images = images;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagelistviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.eventImageView).load(images.get(position)).into(holder.eventImageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.itemimageView);
            int i = itemView.getResources().getDisplayMetrics().widthPixels;
//            eventImageView.setLayoutParams();

        }

    }
}
