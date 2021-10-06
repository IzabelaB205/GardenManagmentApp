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

import com.example.gardenmanagmentapp.adapters.ChatAdapter;
import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageView chatImageView;
    private TextView chatUsernameTextView;
    private FloatingActionButton floatingActionButton;
    private EditText messageEditText;

    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: reads the messages from DB and sent to chat adapter

        ChatAdapter chatAdapter = new ChatAdapter(messages);

        chatImageView = view.findViewById(R.id.chat_image_view);
        chatUsernameTextView = view.findViewById(R.id.chat_username_text_view);
        floatingActionButton = view.findViewById(R.id.floating_btn_send);
        messageEditText = view.findViewById(R.id.message_edit_text);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = messageEditText.getText().toString();
                ChatMessage message = new ChatMessage(context, "MAMA");
                messages.add(message);
                chatAdapter.notifyItemInserted(messages.size() - 1);
            }
        });

        recyclerView.setAdapter(chatAdapter);
        return view;
    }
}
