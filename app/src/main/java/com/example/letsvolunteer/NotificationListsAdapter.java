package com.example.letsvolunteer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class NotificationListsAdapter extends RecyclerView.Adapter<NotificationListsAdapter.ViewHolder> {

    private static final String TAG = "in notificaton adapter";
    private final ArrayList<String> manageNotifications;
    private ArrayList<NewNotification> notifications;

    public NotificationListsAdapter(ArrayList<NewNotification> notifications, ArrayList<String> manageNotifications) {
        this.notifications = notifications;
        this.manageNotifications = manageNotifications;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            navigatetoeventdetails(notifications.get(position).getEventid());
        });
        manageNotifications.forEach(s -> {
            if (notifications.get(position).getEventid().equals(s) ){
               holder.itemView.setEnabled(false);
               holder.notificationcard.setBackgroundColor(holder.itemView.getResources().getColor(R.color.itemlightgrey));
            }
        });

        holder.notificationtext.setText("The " +notifications.get(position).getOrganiserName() + " is organising event " + notifications.get(position).getEventName() + " on date " + notifications.get(position).getEventDate() + " .");
        Glide.with(holder.notificationImageView).load(notifications.get(position).getEventImage()).into(holder.notificationImageView);

    }


    abstract public void navigatetoeventdetails(String eventid);

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
