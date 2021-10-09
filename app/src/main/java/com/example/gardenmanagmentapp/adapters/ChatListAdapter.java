package com.example.gardenmanagmentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder>{
    private Context context;
    private List<User> users;

    public ChatListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public class ChatListHolder extends RecyclerView.ViewHolder{

        ImageView chatListImageview;
        TextView chatListUsernameTextView;

        public ChatListHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            chatListImageview = itemView.findViewById(R.id.chat_list_image_view);
            chatListUsernameTextView = itemView.findViewById(R.id.chat_list_username_text_view);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_card_view, parent, false);
        return new ChatListAdapter.ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatListAdapter.ChatListHolder holder, int position) {

        User user = users.get(position);

        holder.chatListUsernameTextView.setText(user.getFullName());
        Glide.with(context)
                .load(user.getPictureLink())
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(holder.chatListImageview);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
