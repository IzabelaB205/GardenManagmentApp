package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.gardenmanagmentapp.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel implements FirebaseRepository.FirebaseRepositoryListener {

    private MutableLiveData<FirebaseUser> mFirebaseUser = new MutableLiveData<FirebaseUser>();

    public LiveData<FirebaseUser> getFirebaseUser() { return mFirebaseUser; }

    @Override
    public void OnSignInSuccessful() {
        mFirebaseUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }
}
