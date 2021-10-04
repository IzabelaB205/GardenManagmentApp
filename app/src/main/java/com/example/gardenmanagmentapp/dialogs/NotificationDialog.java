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
import com.example.gardenmanagmentapp.model.Notification;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationDialog extends AppCompatDialogFragment {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContext;
    private NotificationDialog.AddNotificationDialogListener listener;

    public static final String TAG = "ADD_NOTIFICATION_DIALOG";

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_notification_layout, null);

        builder.setView(view)
                .setTitle(R.string.notification_title)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = editTextTitle.getText().toString();
                        String context = editTextContext.getText().toString();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);

                        listener.applyNotification(new Notification(context, date, "GANENET", title));
                    }
                });

        editTextTitle = view.findViewById(R.id.title_edit_text);
        editTextContext = view.findViewById(R.id.context_edit_text);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (NotificationDialog.AddNotificationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddNotificationDialogListener");
        }
    }

    public interface AddNotificationDialogListener {
        void applyNotification(Notification notification);
    }
}
