package com.example.gardenmanagmentapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.adapters.NotificationsListAdapter;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.example.gardenmanagmentapp.repository.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.example.gardenmanagmentapp.viewmodel.NotificationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationsListFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private User profileUser;
    private List<Notification> notifications;
    private RecyclerView recyclerView;
    private NotificationsListAdapter notificationsAdapter;

    private NotificationsViewModel notificationViewModel;
    private AuthenticationViewModel authenticationViewModel;

    private BroadcastReceiver receiver;

    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();
    private FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    private final String NOTIFICATION_FRAGMENT_TAG = "NOTIFICATION_FRAGMENT_TAG";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notifications_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners(view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notifications = new ArrayList<>();

        notificationsAdapter = new NotificationsListAdapter(notifications);
        recyclerView.setAdapter(notificationsAdapter);

        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        profileUser = authenticationViewModel.getUser().getValue();

        if(profileUser != null) {
            floatingActionButton.setVisibility(view.VISIBLE);
            loadNotificationsFromFirebase();
        }
        else {
            floatingActionButton.setVisibility(view.INVISIBLE);
        }

        notificationViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        notificationViewModel.getNotification().observe(getViewLifecycleOwner(), new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                notifications.add(notification);
                notificationsAdapter.notifyItemInserted(notifications.size() - 1);
                notificationViewModel.UploadNotificationToFirebase(notification);
            }
        });

        firebaseMessaging.subscribeToTopic("Notifications");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String content = intent.getStringExtra("message");
                String date = createNotificationDate();
                Notification notification = new Notification(content, date, profileUser.getFullName(),"Hello");
                notifications.add(notification);
                notificationsAdapter.notifyItemInserted(notifications.size() - 1);
            }
        };

        IntentFilter filter = new IntentFilter("notification_received");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    private void initViews(View view) {

        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        floatingActionButton = view.findViewById(R.id.notifications_floating_button);
    }

    private void setListeners(View view) {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.id_to_fill, new NewNotificationFragment(), NOTIFICATION_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();

                //TODO: open notification fragment - maybe gives adapter for notify on notification addition

            }
        });
    }

    private void loadNotificationsFromFirebase() {

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Notification notification = child.getValue(Notification.class);
                        notifications.add(notification);
                    }
                }
                notificationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    private String createNotificationDate() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
