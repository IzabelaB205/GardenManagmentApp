package com.example.gardenmanagmentapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.Notification;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public NotificationsListAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView timeTextView;
        TextView senderTextView;
        TextView contentTextView;

        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.notification_title_text_view);
            timeTextView = itemView.findViewById(R.id.notification_time_text_view);
            senderTextView = itemView.findViewById(R.id.notification_sender_text_view);
            contentTextView = itemView.findViewById(R.id.notification_content_text_view);
        }
    }

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationsListAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.timeTextView.setText(notification.getDate());
        holder.senderTextView.setText(notification.getSender());
        holder.contentTextView.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
