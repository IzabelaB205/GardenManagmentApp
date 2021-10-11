package com.example.gardenmanagmentapp.repository;

import androidx.annotation.NonNull;

import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static FirebaseRepository instance;
    private FirebaseRepositoryListener listener;

    public static FirebaseRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }
        return instance;
    }

    private FirebaseRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public interface FirebaseRepositoryListener {

        void OnSignInSuccessful();

        void OnProfileUserSaved(User user);

        void OnProfileUserReceived(User user);
    }

    public void setListener(FirebaseRepositoryListener listener) {
        this.listener = listener;
    }

    public void SignIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        if (listener != null) {
                            listener.OnSignInSuccessful();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void SignOut() {
        firebaseAuth.signOut();
    }

    public void SaveUser(User user) {
        if (firebaseAuth.getCurrentUser() != null) {
            DatabaseReference userReference = databaseReference.child("users")
                    .child(firebaseAuth.getCurrentUser().getUid());

            userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        User updatedUser = task.getResult().getValue(User.class);
                        updatedUser.setEmail(user.getEmail());
                        updatedUser.setPhone(user.getPhone());
                        updatedUser.setPassword(user.getPassword());
                        userReference.setValue(updatedUser);

                        if (listener != null) {
                            listener.OnProfileUserSaved(updatedUser);
                        }
                    }
                }
            });
        }
    }

    public void GetProfileUserFromFirebase() {
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (listener != null) {
                    listener.OnProfileUserReceived(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void UploadNotificationToFirebase(Notification notification) {
        String uniqueId = databaseReference.child("notifications").push().getKey();
        databaseReference.child("notifications").child(uniqueId).setValue(notification);
    }

    public void UploadChatMessageToFirebase(ChatMessage chatMessage) {
        String uniqueId = databaseReference.child("chat").push().getKey();
        databaseReference.child("chat").child(uniqueId).setValue(chatMessage);
    }
}
