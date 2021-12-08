package com.example.letsvolunteer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public abstract class EventListsAdapter extends RecyclerView.Adapter<EventListsAdapter.ViewHolder> {

    private static final String TAG = "EventList";
    public ArrayList<EventsPost> events;
    public Boolean lastElement = false;

    public EventListsAdapter(ArrayList<EventsPost> eventsResults) {
        this.events = eventsResults;

    }

    public ArrayList<EventsPost> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventsPost> events) {
        this.events = events;
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
        holder.location.setText(events.get(position).getLocationAddress());

        Glide.with(holder.eventImageView).load(events.get(position).imageUrlLists.get(0))
                .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                .into(holder.eventImageView);
        holder.onClickcalled(position);

        if (position == events.size() -1 && !lastElement ){
            Log.d(TAG, "onBindViewHolder: ");
            loadNewEventsfnc();
        }
    }

    @Override
    public int getItemCount() {
        return events.size() ;
    }

    public abstract void navigatetodetails(EventsPost eventsPost);
    public abstract void loadNewEventsfnc();


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView eventImageView;
        TextView eventName;
        TextView eventDate;
        TextView location;
        int position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.eventImg);
            eventName = itemView.findViewById(R.id.eventNameTxt);
            eventDate = itemView.findViewById(R.id.eventDateTxt);
            location = itemView.findViewById(R.id.locationTxt);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            navigatetodetails(events.get(position));
        }


        public void onClickcalled(int position) {
            this.position = position;
        }
    }
}
