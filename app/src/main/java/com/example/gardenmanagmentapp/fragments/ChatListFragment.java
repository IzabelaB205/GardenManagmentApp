package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.adapters.ChatListAdapter;
import com.example.gardenmanagmentapp.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> users = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.chat_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: load users list from firebase database

        ChatListAdapter chatListAdapter = new ChatListAdapter(getContext(), users);
        recyclerView.setAdapter(chatListAdapter);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }
}
