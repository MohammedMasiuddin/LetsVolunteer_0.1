package com.example.letsvolunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class EventListsAdapter extends RecyclerView.Adapter<EventListsAdapter.ViewHolder> {

    private ArrayList<EventsPost> events;

    public EventListsAdapter(ArrayList<EventsPost> eventsResults) {
        this.events = eventsResults;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).getEventName());
        holder.eventDate.setText(events.get(position).getEventDate());

        Glide.with(holder.eventImageView).load(events.get(position).imageUrlLists.get(0)).into(holder.eventImageView);
    }

    @Override
    public int getItemCount() {
        return events.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImageView;
        TextView eventName;
        TextView eventDate;
        TextView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.eventImg);
            eventName = itemView.findViewById(R.id.eventNameTxt);
            eventDate = itemView.findViewById(R.id.eventDateTxt);
            location = itemView.findViewById(R.id.location);

        }
    }
}
