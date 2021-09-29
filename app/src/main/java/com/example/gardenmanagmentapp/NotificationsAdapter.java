package com.example.gardenmanagmentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    public NotificationsAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView titleTextView;
        ImageView messageImageView;
        TextView senderTextView;
        TextView contextTextView;

        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.notification_date_text_view);
            titleTextView = itemView.findViewById(R.id.notification_title_text_view);
            messageImageView = itemView.findViewById(R.id.notification_image_view);
            senderTextView = itemView.findViewById(R.id.notification_sender_text_view);
            contextTextView = itemView.findViewById(R.id.notification_context_text_view);
        }
    }

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);
        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(view);

        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationsAdapter.NotificationViewHolder holder, int position) {

        Notification notification = notificationList.get(position);

        holder.dateTextView.setText(notification.getDate());
        holder.titleTextView.setText(notification.getTitle());
        holder.messageImageView.setImageResource(notification.getImage());
        holder.senderTextView.setText(notification.getSender());
        holder.contextTextView.setText(notification.getContext());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
