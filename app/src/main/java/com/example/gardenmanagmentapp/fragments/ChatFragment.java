package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private EditText messageEditText;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference chats = database.getReference("chats");

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floatingActionButton = getActivity().findViewById(R.id.floating_btn_send);
        messageEditText = getActivity().findViewById(R.id.message_edit_text);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContext = messageEditText.getText().toString();

                chats.push().setValue(new ChatMessage(messageContext,
                        FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getDisplayName())
                );

                // Clear the edit text input
                messageEditText.setText("");
            }
        });
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

        ChatAdapter chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        return view;
    }
}
