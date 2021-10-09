package com.example.gardenmanagmentapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gardenmanagmentapp.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        builder.setView(view)
                .setTitle(R.string.sign_in_title)
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();

                        //TODO: sign-in to firebase
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {

        editTextEmail = view.findViewById(R.id.sign_in_email_edit_text);
        editTextPassword = view.findViewById(R.id.sign_in_password_edit_text);
    }
}
