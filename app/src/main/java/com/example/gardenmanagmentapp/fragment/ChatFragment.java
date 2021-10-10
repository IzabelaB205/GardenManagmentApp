package com.example.gardenmanagmentapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gardenmanagmentapp.adapters.ChatAdapter;
import com.example.gardenmanagmentapp.database.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private EditText chatMessageEditText;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private User profileUser = new User("izabela", "123456", "0546375664", "izabelab@gmail.com", "1234567", null);
    private List<ChatMessage> messages = new ArrayList<>();

    private BroadcastReceiver receiver;
    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();
    private FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    private final String API_TOKEN_KEY = "AAAA8YUodb0:APA91bGxuNltYGPCRyAIQnk1nMMEREGs3UNJVKOviuhuQzg0sKAEZ72_WzdrXmiTRn06NJZfiB-4R8FtELucE2AGZPUerqkPmX0LWk3lONGie79Tt1Rqlke6KT-Fm7P7vfgbCtlyE-z1";

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

        firebaseMessaging.subscribeToTopic("Chat");

        initViews(view);
        setListeners(view);

        // TODO: load user's messages from firebase database

        chatAdapter = new ChatAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        messages = firebaseHelper.LoadChatMessagesFromFirebase();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String content = intent.getStringExtra("message");
                ChatMessage message = new ChatMessage(content, "Ganenet");
                messages.add(message);
                chatAdapter.notifyItemInserted(messages.size() - 1);

                //TODO: write message to database
            }
        };

        IntentFilter filter = new IntentFilter("message_received");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void initViews(View view) {

        floatingActionButton = view.findViewById(R.id.chat_send_floating_button);
        chatMessageEditText = view.findViewById(R.id.chat_message_edit_text);
    }

    private void setListeners(View view) {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Read user input from EditText
                String content = chatMessageEditText.getText().toString();

                // Clear the message input EditText
                chatMessageEditText.setText("");

                // Create message and notify the chat adapter
                ChatMessage newMessage = new ChatMessage(content, profileUser.getFullName());
                messages.add(newMessage);
                chatAdapter.notifyItemInserted(messages.size() - 1);

                // Write message to firebase real-time database
                firebaseHelper.UploadChatMessageToFirebase(newMessage);

                // Create json object for sending the POST message using Volley
                JSONObject rootObject = new JSONObject();
                try {
                    rootObject.put("to", "/topics/Chat");
                    rootObject.put("data", new JSONObject().put("message", content));

                    String url = "https://fcm.googleapis.com/fcm/send";

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", "key=" + API_TOKEN_KEY);
                            return headers;
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            return rootObject.toString().getBytes();
                        }
                    };

                    queue.add(request);
                    queue.start();

                } catch(JSONException ex) {
                    ex.printStackTrace();
                }


                // TODO: 1. add message to firebase database
                //      2.user notification to all garden's users
            }
        });
    }
}
