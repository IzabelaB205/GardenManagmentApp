package com.example.gardenmanagmentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    List<ChatMessage> messages;

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView userTextView;
        TextView timeTextView;
        TextView messageTextView;

        public ChatViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            userTextView = itemView.findViewById(R.id.message_user);
            timeTextView = itemView.findViewById(R.id.message_time);
            messageTextView = itemView.findViewById(R.id.message_text);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card_view, parent, false);
        ChatAdapter.ChatViewHolder chatViewHolder = new ChatAdapter.ChatViewHolder(view);

        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.ChatViewHolder holder, int position) {

        ChatMessage message = messages.get(position);

        holder.userTextView.setText(message.getMessageUser());
        holder.timeTextView.setText(message.getMessageTime());
        holder.messageTextView.setText(message.getMessageText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
