package com.example.gardenmanagmentapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    private ImageButton editImageButton;
    private ImageView profileImageView;
    private TextView textViewUsername;
    private TextInputEditText editTextId;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextPassword;
    private Button buttonSaveProfile;
    private Button buttonCancelProfile;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryPictureLauncher;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private User profileUser;
    private AuthenticationViewModel viewModel;
    private final static String PROFILE_IMAGE_STORAGE_PATH = "profile_image";

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        initViews(view);
        initLaunchers();
        setListeners();

        changeEditState(false);
        setButtonState(view.INVISIBLE);

        viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                profileUser = user;
            }
        });

        profileUser = viewModel.getUser().getValue();

        if(profileUser != null) {
            editImageButton.setVisibility(view.VISIBLE);
            populateView();
        }
        else {
            editImageButton.setVisibility(view.INVISIBLE);
        }
    }

    private void initViews(View view) {

        editImageButton = view.findViewById(R.id.profile_edit_image_view);
        profileImageView = view.findViewById(R.id.profile_image_view);
        textViewUsername = view.findViewById(R.id.profile_username_text_view);
        editTextId = view.findViewById(R.id.profile_id_edit_text);
        editTextEmail = view.findViewById(R.id.profile_email_edit_text);
        editTextPhone = view.findViewById(R.id.profile_phone_edit_text);
        editTextPassword = view.findViewById(R.id.profile_password_edit_text);
        buttonSaveProfile = view.findViewById(R.id.profile_save_button);
        buttonCancelProfile = view.findViewById(R.id.profile_cancel_button);
    }

    private void setListeners() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());

                builder.setTitle(R.string.profile_picture_alert)
                        .setMessage(R.string.picture_alert_message)
                        .setPositiveButton(R.string.via_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                galleryPictureLauncher.launch(null);
                            }
                        })
                        .setNegativeButton(R.string.via_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cameraLauncher.launch(null);
                            }
                        }).show();
            }
        });

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonState(View.VISIBLE);
                changeEditState(true);
            }
        });

        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonState(View.INVISIBLE);
                changeEditState(false);

                readProfileChanges();
            }
        });

        buttonCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonState(View.INVISIBLE);
                changeEditState(false);
            }
        });
    }

    private void initLaunchers() {

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                /** TODO: fix blurred picture */
                Uri uri = getImageUri(getContext(), result);
                Glide.with(getContext()).load(uri).circleCrop().into(profileImageView);
                profileUser.setPictureLink(uri.toString());
                uploadImageToFirebase(uri);
            }
        });

        galleryPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Glide.with(getContext()).load(result).circleCrop().into(profileImageView);
                profileUser.setPictureLink(result.toString());
                uploadImageToFirebase(result);
            }
        });
    }

    private void populateView() {

        textViewUsername.setText(profileUser.getFullName());
        editTextId.setText(profileUser.getId());
        editTextEmail.setText(profileUser.getEmail());
        editTextPhone.setText(profileUser.getPhone());
        editTextPassword.setText(profileUser.getPassword());

        Glide.with(this)
                .load(profileUser.getPictureLink())
                .placeholder(R.drawable.profile)
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(50)))
                .into(profileImageView);
    }

    private void readProfileChanges() {
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();

        profileUser.setEmail(email);
        profileUser.setPhone(phone);
        profileUser.setPassword(password);

        viewModel.SaveUser(profileUser);

    }

    private void uploadImageToFirebase(Uri uri) {
        StorageReference reference = storageReference
                .child(PROFILE_IMAGE_STORAGE_PATH)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference
                        .child(PROFILE_IMAGE_STORAGE_PATH)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference userReference =
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            User user = task.getResult().getValue(User.class);
                                            user.setPictureLink(uri.toString());
                                            userReference.setValue(user);
                                        }
                                    }
                                });
                            }
                        });
            }
        });
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void setButtonState(int state) {
        buttonSaveProfile.setVisibility(state);
        buttonCancelProfile.setVisibility(state);
    }

    private void changeEditState(boolean state) {
        profileImageView.setClickable(state);
        editTextEmail.setEnabled(state);
        editTextPhone.setEnabled(state);
        editTextPassword.setEnabled(state);
    }
}
