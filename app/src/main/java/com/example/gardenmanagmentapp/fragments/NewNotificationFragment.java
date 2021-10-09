package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.database.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Notification;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

public class NewNotificationFragment extends Fragment {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private Button buttonSend;
    private Button buttonCancel;

    private FirebaseDatabaseHelper firebaseHelper = new FirebaseDatabaseHelper();

    //private final static String NOTIFICATION_PATH = "notifications";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners(view);
    }

    private void initViews(View view) {
        editTextTitle = view.findViewById(R.id.new_notification_title_edit_text);
        editTextContent = view.findViewById(R.id.new_notification_content_edit_text);
        buttonSend = view.findViewById(R.id.new_notification_sent_button);
        buttonCancel = view.findViewById(R.id.new_notification_cancel_button);
    }

    private void setListeners(View view) {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();


                firebaseHelper.UploadNotificationToFirebase(new Notification(title, "Ganenet", content));
                //TODO: 1. send broadcast message to all Garden's members
                //      2. add notification to firebase database
                //      3. add notification to notifications list using adapter
            }
        });
    }
}