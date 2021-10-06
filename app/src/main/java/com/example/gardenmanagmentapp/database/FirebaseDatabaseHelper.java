package com.example.gardenmanagmentapp.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.model.Picture;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public FirebaseDatabaseHelper() {

        InitializeFirebase();
    }

    private void InitializeFirebase() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    public void Login(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

            }
        });
    }

    public void UpdateUsersDatabase(User user) {
        databaseReference.child("users").child(this.user.getUid()).setValue(user);
    }

    public void UpdateNotificationsDatabase(Notification notification) {
        databaseReference.child("notifications").child(user.getUid()).setValue(notification);
    }

    public void UploadPicturesDatabase(Picture picture, String fileExtension) {
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + fileExtension);
        fileReference.putFile(Uri.parse(picture.getPictureUrl())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String uniqueId = databaseReference.push().getKey();
                databaseReference.child("uploads").child(uniqueId).setValue(picture);
            }
        });
    }

    public List<Notification> LoadNotificationsData() {

        List<Notification> notifications = new ArrayList<>();

        databaseReference.child("notifications").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifications.clear();

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

    public List<Picture> LoadPicturesData() {

        List<Picture> pictures = new ArrayList<>();

        databaseReference.child("uploads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                pictures.clear();

                for(DataSnapshot child : snapshot.getChildren()) {
                    Picture picture = child.getValue(Picture.class);
                    picture.setKey(snapshot.getKey());
                    pictures.add(picture);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return pictures;
    }

    public void DeletePictureFromDatabase(Picture picture) {
        StorageReference pictureRef = storageReference.getStorage().getReference(picture.getPictureUrl());
        pictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child("uploads").child(picture.getKey()).removeValue();
            }
        });
    }
}
