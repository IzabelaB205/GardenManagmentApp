package com.example.gardenmanagmentapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.adapters.NotificationsListAdapter;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.database.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.model.Notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationsListFragment extends Fragment {

    private List<Notification> notifications = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NotificationsListAdapter notificationsAdapter;

    private BroadcastReceiver receiver;

    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();
    private FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notifications_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notifications = firebaseHelper.LoadNotificationsFromFirebase();

        notificationsAdapter = new NotificationsListAdapter(notifications);
        recyclerView.setAdapter(notificationsAdapter);

        initViews(view);
        setListeners(view);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String content = intent.getStringExtra("message");
                String date = createNotificationDate();
                Notification notification = new Notification(content, date, "ganenet","Hello");
                notifications.add(notification);
                notificationsAdapter.notifyItemInserted(notifications.size() - 1);

                //TODO: write message to database
            }
        };

        IntentFilter filter = new IntentFilter("message_received");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);

        Toast.makeText(view.getContext(), "NotificationsListFragment", Toast.LENGTH_SHORT).show();

        //TODO: Check if user is garden manager:
        // if true - make floating action button visible
        // by default the button is invisible
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void initViews(View view) {
        floatingActionButton = view.findViewById(R.id.notifications_floating_button);
    }

    private void setListeners(View view) {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.id_to_fill, new NewNotificationFragment(notificationsAdapter, notifications), "CHAT_FRAGMENT_TAG")
                        .addToBackStack(null)
                        .commit();

                //TODO: open notification fragment - maybe gives adapter for notify on notification addition

            }
        });
    }

    private String createNotificationDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
