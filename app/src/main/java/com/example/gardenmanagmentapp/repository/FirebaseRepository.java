package com.example.gardenmanagmentapp.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static FirebaseRepository instance;
    private FirebaseRepositoryListener listener;

    public static FirebaseRepository getInstance() {
        if(instance == null) {
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
}
