package com.example.gardenmanagmentapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.adapters.NotificationsAdapter;
import com.example.gardenmanagmentapp.adapters.PictureStorageAdapter;
import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.model.Picture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class PictureStorageFragment extends Fragment implements PictureStorageAdapter.OnItemClickListener {

    private List<Picture> pictures;
    private PictureStorageListener listener;

    public PictureStorageFragment() {};

    public static PictureStorageFragment newInstance(List<Picture> pictures) {

        PictureStorageFragment fragment = new PictureStorageFragment();
        Bundle args = new Bundle();
        args.putSerializable("pictures", (Serializable) pictures);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pictures_storage, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.pictures_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressBar progressBar = view.findViewById(R.id.pictures_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        this.pictures = (List<Picture>) getArguments().getSerializable("pictures");

        PictureStorageAdapter pictureStorageAdapter = new PictureStorageAdapter(getContext(), pictures);
        recyclerView.setAdapter(pictureStorageAdapter);
        pictureStorageAdapter.setOnItemClickListener(this);

        FloatingActionButton picturesFloatingButton = view.findViewById(R.id.pictures_floating_button);
        picturesFloatingButton.setVisibility(View.VISIBLE);
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

        progressBar.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        Picture picture = pictures.get(position);

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (PictureStorageFragment.PictureStorageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement PictureStorageListener");
        }
    }

    public interface PictureStorageListener {
        void deletePictureInfo(Picture picture);
    }
}
