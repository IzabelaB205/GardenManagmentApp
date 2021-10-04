package com.example.gardenmanagmentapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.adapters.NotificationsAdapter;
import com.example.gardenmanagmentapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContext;
    private NotificationsAdapter notificationsAdapter;
    private NotificationsFragment.AddNotificationDialogListener listener;
    private List<Notification> notificationList;

    public NotificationsFragment(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsAdapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(notificationsAdapter);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.notification_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNotificationDialog();
            }
        });

        Notification notification = new Notification("02.10.2021", "Hello", "Shlomit", "Hello this is a try message");
        notificationList.add(notification);

        return view;
    }

    private void openAddNotificationDialog() {
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

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);

                        Notification notification = new Notification(context, date, "GANENET", title);

                        notificationList.add(notification);
                        notificationsAdapter.notifyItemInserted(notificationList.size() - 1);
                        listener.applyNotificationInfo(notification);
                    }
                }).show();

        editTextTitle = view.findViewById(R.id.title_edit_text);
        editTextContext = view.findViewById(R.id.context_edit_text);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (NotificationsFragment.AddNotificationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddNotificationDialogListener");
        }
    }

    public interface AddNotificationDialogListener {
        void applyNotificationInfo(Notification notification);
    }
}
