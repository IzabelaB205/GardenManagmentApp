package com.example.gardenmanagmentapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gardenmanagmentapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignInDialog extends AppCompatDialogFragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextPassword;
    private SignInDialogListener listener;

    public static final String TAG = "SIGN_IN_DIALOG";

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sign_in_layout, null);

        builder.setView(view)
                .setTitle(R.string.login_title)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = editTextEmail.getText().toString();
                        String username = editTextUsername.getText().toString();
                        String password = editTextPassword.getText().toString();

                        listener.applyUserInfo(email, username, password);
                    }
                });

        editTextEmail = view.findViewById(R.id.email_edit_text);
        editTextUsername = view.findViewById(R.id.username_edit_text);
        editTextPassword = view.findViewById(R.id.password_edit_text);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (SignInDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SignInDialogListener");
        }
    }

    public interface SignInDialogListener {
        void applyUserInfo(String email, String username, String password);
    }
}
