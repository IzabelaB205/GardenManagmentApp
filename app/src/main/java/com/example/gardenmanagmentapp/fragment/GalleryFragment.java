package com.example.gardenmanagmentapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.adapters.GalleryAdapter;
import com.example.gardenmanagmentapp.repository.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Picture;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private List<Picture> pictures;
    private FloatingActionButton picturesFloatingButton;
    private GalleryAdapter pictureStorageAdapter;
    GridLayoutManager gridLayoutManager;

    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();

    public GalleryFragment() {};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
    }

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
        recyclerView.setLayoutManager(gridLayoutManager);

        pictures = new ArrayList<>();

        pictureStorageAdapter = new GalleryAdapter(getContext(), pictures);
        recyclerView.setAdapter(pictureStorageAdapter);

        initViews(view);
        setListeners(view);
        loadPicturesFromFirebase();

        picturesFloatingButton.setVisibility(View.VISIBLE);
    }

    private void setListeners(View view) {
        picturesFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.id_to_fill, new PictureSelectionFragment(), PictureSelectionFragment.PICTURE_SELECTION_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadPicturesFromFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("uploads");


        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {

                            Picture picture = new Picture();
                            picture.setPictureUrl(item.getPath().toString());
                            pictures.add(picture);;
                        }
                        pictureStorageAdapter.notifyDataSetChanged();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

    }
}
