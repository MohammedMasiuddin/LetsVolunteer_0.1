package com.example.letsvolunteer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public  class OrganiserNotificationListsAdapter extends RecyclerView.Adapter<OrganiserNotificationListsAdapter.ViewHolder> {

    private static final String TAG = "in notificaton adapter";
    private ArrayList<OrganiserNotifcation> notifications;

    public OrganiserNotificationListsAdapter(ArrayList<OrganiserNotifcation> notifications) {
        this.notifications = notifications;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.notificationtext.setText(" " +notifications.get(position).getFirstName() + " has subcribe for event " + notifications.get(position).getEventName() + " .");
        Glide.with(holder.notificationImageView).load(notifications.get(position).getPhotoUri()).into(holder.notificationImageView);

    }



    @Override
    public int getItemCount() {
        return notifications.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView notificationImageView;
        TextView notificationtext;
        CardView notificationcard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationImageView = itemView.findViewById(R.id.notificationimage);
            notificationtext = itemView.findViewById(R.id.notificationtext);
            notificationcard = itemView.findViewById(R.id.notificationcard);

        }

    }
}
