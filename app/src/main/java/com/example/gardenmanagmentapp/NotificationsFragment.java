package com.example.gardenmanagmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Notification> notificationList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference notifications = database.getReference("notifications");

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notifications_fragment, container, false);

        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: read from firebase database all the notifications and send to notificationsAdapter

        //notifications.child(notification.getUid())

        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(notificationsAdapter);

        return view;
    }
}
