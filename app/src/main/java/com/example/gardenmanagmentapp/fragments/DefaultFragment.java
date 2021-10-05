package com.example.gardenmanagmentapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.dialogs.SignInDialog;

import org.jetbrains.annotations.NotNull;

public class DefaultFragment extends Fragment {

    public DefaultFragment() {}

    public static DefaultFragment newInstance(int item, String title, String context) {

        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putInt("item" , item);
        args.putString("title", title);
        args.putString("context", context);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_default, container, false);

        ImageView defaultImageView = view.findViewById(R.id.default_image_view);
        TextView titleTextView = view.findViewById(R.id.default_title);
        TextView contextTextView = view.findViewById(R.id.default_context);
        TextView signInTextView = view.findViewById(R.id.default_sign_in);

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInDialog();
            }
        });

        int image = 0;
        int item = getArguments().getInt("item");
        String title = getArguments().getString("title");
        String context = getArguments().getString("context");

        switch (item)
        {
            case R.id.item_notifications:
                image = R.drawable.notification;
                break;
            case R.id.item_chat:
                image = R.drawable.chat;
                break;
            case R.id.item_calendar:
                image = R.drawable.calendar;
                break;
            case R.id.item_pictures:
                image = R.drawable.camera;
                break;
            case R.id.item_profile:
                image = R.drawable.profile;
                break;
        }

        defaultImageView.setImageResource(image);
        titleTextView.setText(title);
        contextTextView.setText(context);

        return view;
    }

    private void openSignInDialog() {
        SignInDialog signInDialog = new SignInDialog();
        signInDialog.show(getActivity().getSupportFragmentManager(), SignInDialog.TAG);
    }
}
