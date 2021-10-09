package com.example.gardenmanagmentapp.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.Picture;

import org.jetbrains.annotations.NotNull;

public class PictureSelectionFragment extends Fragment {

    private Uri pictureUri = null;
    private ImageView pictureImageView;
    private PictureUploadListener listener;
    private ActivityResultLauncher<String> galleryPictureLauncher;

    public static final String PICTURE_SELECTION_FRAGMENT = "picture_selection_fragment";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.picture_selection_layout, container, false);

        initLaunchers();

        Button pictureSelectButton = view.findViewById(R.id.picture_select_button);
        EditText fileNameEditText = view.findViewById(R.id.file_name_edit_text);
        pictureImageView = view.findViewById(R.id.picture_image_view);
        ProgressBar pictureUploadProgressBar = view.findViewById(R.id.picture_upload_progress_bar);
        Button pictureUploadButton = view.findViewById(R.id.picture_upload_button);
        Button pictureCancelButton = view.findViewById(R.id.picture_cancel_button);

        pictureSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryPictureLauncher.launch("image/*");
            }
        });

        pictureUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pictureName = fileNameEditText.getText().toString();
                String uri = pictureUri.toString();
                String fileExtension = getFileExtension(pictureUri);

                listener.applyPictureInfo(new Picture(uri), fileExtension);
            }
        });

        pictureCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (PictureSelectionFragment.PictureUploadListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SignInDialogListener");
        }
    }

    private void initLaunchers() {
        galleryPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Glide.with(getContext()).load(result).circleCrop().into(pictureImageView);
                //circleImageView.setImageURI(result);
                pictureUri = result;
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface PictureUploadListener {
        void applyPictureInfo(Picture picture, String fileExtension);
    }
}
