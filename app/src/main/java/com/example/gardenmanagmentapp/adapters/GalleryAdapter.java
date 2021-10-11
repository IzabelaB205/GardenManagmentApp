package com.example.gardenmanagmentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.GlideApp;
import com.example.gardenmanagmentapp.model.Picture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{

    private Context context;
    private List<Picture> pictures;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public GalleryAdapter(Context context, List<Picture> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryImageView;

        public GalleryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            galleryImageView = itemView.findViewById(R.id.gallery_image_view);
        }
    }

    @NonNull
    @NotNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_card_view, parent, false);
        return new GalleryAdapter.GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryAdapter.GalleryViewHolder holder, int position) {

        Picture picture = pictures.get(position);
        StorageReference picRef = storage.getReference().child(picture.getPictureUrl());
        //Log.d("Uploaded image: ","image number "+position);
        GlideApp.with(context).load(picRef).into(holder.galleryImageView);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }
}
