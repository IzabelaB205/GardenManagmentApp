package com.example.gardenmanagmentapp.database;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public FirebaseDatabaseHelper() {

        InitializeFirebase();
    }

    private void InitializeFirebase() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void Login(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

            }
        });
    }

    public void UpdateUsersDatabase(User user) {
        reference.child("users").child(this.user.getUid()).setValue(user);
    }

    public void UpdateNotificationsDatabase(Notification notification) {
        reference.child("notifications").child(user.getUid()).setValue(notification);
    }

    public List<Notification> LoadNotificationsData() {

        List<Notification> notifications = new ArrayList<>();

        reference.child("notifications").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(snapshot.exists()) {
                            for(DataSnapshot child : snapshot.getChildren()) {
                                Notification notification = child.getValue(Notification.class);
                                notifications.add(notification);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return notifications;
    }
}
