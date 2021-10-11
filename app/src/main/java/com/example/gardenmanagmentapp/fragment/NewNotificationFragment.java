package com.example.gardenmanagmentapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.adapters.NotificationsListAdapter;
import com.example.gardenmanagmentapp.model.User;
import com.example.gardenmanagmentapp.repository.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.example.gardenmanagmentapp.viewmodel.NotificationsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewNotificationFragment extends Fragment {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private Button buttonSend;
    private Button buttonCancel;

    private User profileUser;

    private AuthenticationViewModel authenticationViewModel;
    private NotificationsViewModel notificationsViewModel;

    private FirebaseDatabaseHelper firebaseHelper;

    private final String API_TOKEN_KEY = "AAAA8YUodb0:APA91bGxuNltYGPCRyAIQnk1nMMEREGs3UNJVKOviuhuQzg0sKAEZ72_WzdrXmiTRn06NJZfiB-4R8FtELucE2AGZPUerqkPmX0LWk3lONGie79Tt1Rqlke6KT-Fm7P7vfgbCtlyE-z1";
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

        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        profileUser = authenticationViewModel.getUser().getValue();
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

                // Read notification content from user input EditText
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();
                String date = createNotificationDate();

                // Create new notification object
                Notification notification = new Notification(title, date, profileUser.getFullName(), content);

                // Notify Notification fragment that new notification was created
                notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
                notificationsViewModel.setNotification(notification);

                // Clear all notification's fragment EditTexts
                editTextTitle.setText("");
                editTextContent.setText("");

                //TODO: 1. send broadcast message to all Garden's members

                // Create json object for sending the POST message using Volley
                JSONObject rootObject = new JSONObject();
                try {
                    rootObject.put("to", "/topics/Notifications");
                    rootObject.put("data", new JSONObject().put("message", content));

                    String url = "https://fcm.googleapis.com/fcm/send";

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", "key=" + API_TOKEN_KEY);
                            return headers;
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            return rootObject.toString().getBytes();
                        }
                    };

                    queue.add(request);
                    queue.start();

                } catch(JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private String createNotificationDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}