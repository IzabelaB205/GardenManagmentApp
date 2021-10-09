package com.example.gardenmanagmentapp.fragments;

import android.content.Context;
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
import com.example.gardenmanagmentapp.adapters.GalleryAdapter;
import com.example.gardenmanagmentapp.database.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Picture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private List<Picture> pictures = new ArrayList<>();
    private FloatingActionButton picturesFloatingButton;
    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();

    public GalleryFragment() {};

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gallery_storage, container, false);
    }

    private void initViews(View view) {
        picturesFloatingButton = view.findViewById(R.id.gallery_floating_button);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO : load pictures from firebase storage
        //pictures = firebaseHelper.LoadPicturesData();

        GalleryAdapter pictureStorageAdapter = new GalleryAdapter(getContext(), pictures);
        recyclerView.setAdapter(pictureStorageAdapter);

        initViews(view);
        setListeners(view);


        //TODO: check if garden manager - true - make floating button visible.
        //otherwise, do nothing.
        picturesFloatingButton.setVisibility(View.VISIBLE);
    }

    private void setListeners(View view) {
        picturesFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelectionFragment pictureSelectionFragment = new PictureSelectionFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drawer_layout, pictureSelectionFragment, PictureSelectionFragment.PICTURE_SELECTION_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
