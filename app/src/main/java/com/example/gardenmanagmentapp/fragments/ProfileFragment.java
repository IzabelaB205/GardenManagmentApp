package com.example.gardenmanagmentapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


import de.hdodenhof.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements DialogInterface.OnClickListener {

    //boolean byCamera;
   // Bitmap bitmap;
    //File file;

//    CircleImageView circleImageView;

    ImageView circleImageView;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryPictureLauncher;
    String profileImageLink = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user application user.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initLaunchers();

        circleImageView = view.findViewById(R.id.profile_image);
        TextView textViewUsername = view.findViewById(R.id.profile_username_text_view);
        TextInputEditText editTextId = view.findViewById(R.id.profile_id_edit_text);
        TextInputEditText editTextEmail = view.findViewById(R.id.profile_email_edit_text);
        TextInputEditText editTextPhone = view.findViewById(R.id.profile_phone_edit_text);
        TextInputEditText editTextPassword = view.findViewById(R.id.profile_password_edit_text);

        User user = (User) getArguments().getSerializable("user");

        textViewUsername.setText(user.getFullName());
        editTextId.setText(user.getId());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhone());
        editTextPassword.setText(user.getPassword());

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProfilePicture();

            }
        });

        //TODO: add user photo by using glide library

        return view;
    }

    private void initLaunchers() {

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                  /** TODO: fix blurred picture */
                  Uri uri = getImageUri(getContext(),result);

                  Glide.with(getContext()).load(uri).circleCrop().into(circleImageView);

                  profileImageLink = uri.toString();
            }
        });

        galleryPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Glide.with(getContext()).load(result).circleCrop().into(circleImageView);
                //circleImageView.setImageURI(result);
                profileImageLink = result.toString();
            }
        });

    }

    void UploadProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload Your Picture").setMessage("Choose which way to upload your picture:")
                .setPositiveButton("via Camera", new DialogInterface.OnClickListener() {
                    @Override
                    // upload picture with camera
                    public void onClick(DialogInterface dialog, int which) {

                        cameraLauncher.launch(null);

                    }
                })

                .setNegativeButton("via Gallery", new DialogInterface.OnClickListener() {

                    @Override
                    // upload picture with gallery
                    public void onClick(DialogInterface dialog, int which) {
                        galleryPictureLauncher.launch("image/*");
                    }
                })

                .show();
    }





    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}


