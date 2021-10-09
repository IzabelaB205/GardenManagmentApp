package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.gardenmanagmentapp.adapters.ChatAdapter;
import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ImageView chatMessageImageView;
    private TextView chatUsernameTextView;
    private FloatingActionButton floatingActionButton;
    private EditText chatMessageEditText;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private User profileUser;
    private List<ChatMessage> messages;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initViews(view);

        // TODO: load user's messages from firebase database

        chatAdapter = new ChatAdapter(messages);
        recyclerView.setAdapter(chatAdapter);
    }

    private void initViews(View view) {

        chatMessageImageView = view.findViewById(R.id.chat_image_view);
        chatUsernameTextView = view.findViewById(R.id.chat_username_text_view);
        floatingActionButton = view.findViewById(R.id.chat_send_floating_button);
        chatMessageEditText = view.findViewById(R.id.chat_message_edit_text);
    }

    private void setListeners(View view) {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = chatMessageEditText.getText().toString();
                ChatMessage newMessage = new ChatMessage(content, profileUser.getFullName());
                chatAdapter.notifyItemInserted(messages.size() - 1);

                // TODO: 1. add message to firebase database
            }
        });
    }

    private void populateView() {
        chatUsernameTextView.setText(profileUser.getFullName());

        if (profileUser.getPictureLink() != null) {
            Glide.with(this)
                    .load(profileUser.getPictureLink())
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(50)))
                    .into(chatMessageImageView);
        }
    }
}
