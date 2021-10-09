package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.adapters.NotificationsListAdapter;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.Notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationsListFragment extends Fragment {

    private List<Notification> notifications;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NotificationsListAdapter notificationsAdapter;

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
        notificationsAdapter = new NotificationsListAdapter();
        recyclerView.setAdapter(notificationsAdapter);

        initViews(view);
        setListeners(view);

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

                //TODO: open notification fragment - maybe gives adapter for notify on notification addition

            }
        });
    }
}
