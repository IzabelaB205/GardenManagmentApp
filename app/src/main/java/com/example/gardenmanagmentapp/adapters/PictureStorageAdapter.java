package com.example.gardenmanagmentapp.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.Picture;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PictureStorageAdapter extends RecyclerView.Adapter<PictureStorageAdapter.PictureStorageViewHolder>{

    private Context context;
    private List<Picture> pictures;
    private OnItemClickListener listener;

    public PictureStorageAdapter(Context context, List<Picture> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    public class  PictureStorageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            ,View.OnCreateContextMenuListener
            ,MenuItem.OnMenuItemClickListener {

        TextView pictureTitleTextView;
        ImageView pictureContent;

        public PictureStorageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            pictureTitleTextView = itemView.findViewById(R.id.picture_title);
            pictureContent = itemView.findViewById(R.id.picture_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
            MenuItem deleteItem = menu.add(Menu.NONE, 1, 1, "Delete");
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {

                    switch(item.getItemId()) {
                        case 1:
                            listener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    @NonNull
    @NotNull
    @Override
    public PictureStorageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_card_view, parent, false);
        return new PictureStorageAdapter.PictureStorageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PictureStorageAdapter.PictureStorageViewHolder holder, int position) {

            Picture picture = pictures.get(position);

            holder.pictureTitleTextView.setText(picture.getPictureName());
            Glide.with(context)
                    .load(picture.getPictureUrl())
                    .placeholder(R.drawable.ic_profile)
                    .circleCrop()
                    .into(holder.pictureContent);

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
