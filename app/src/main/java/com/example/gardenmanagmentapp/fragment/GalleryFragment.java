package com.example.gardenmanagmentapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.adapters.GalleryAdapter;
import com.example.gardenmanagmentapp.model.User;
import com.example.gardenmanagmentapp.repository.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Picture;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FloatingActionButton picturesFloatingButton;

    private User profileUser;
    private List<Picture> pictures;
    private RecyclerView recyclerView;
    private GalleryAdapter pictureStorageAdapter;
    GridLayoutManager gridLayoutManager;

    private AuthenticationViewModel authenticationViewModel;

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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners(view);

        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        pictures = new ArrayList<>();

        pictureStorageAdapter = new GalleryAdapter(getContext(), pictures);
        recyclerView.setAdapter(pictureStorageAdapter);

        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        authenticationViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                profileUser = user;
            }
        });

        profileUser = authenticationViewModel.getUser().getValue();

        if (profileUser != null) {
            picturesFloatingButton.setVisibility(View.VISIBLE);
            loadPicturesFromFirebase();
        } else {
            picturesFloatingButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews(View view) {

        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        picturesFloatingButton = view.findViewById(R.id.gallery_floating_button);
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
