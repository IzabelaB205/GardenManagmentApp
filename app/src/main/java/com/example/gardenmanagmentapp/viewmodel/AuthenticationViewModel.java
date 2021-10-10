package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.gardenmanagmentapp.model.User;
import com.example.gardenmanagmentapp.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel extends ViewModel implements FirebaseRepository.FirebaseRepositoryListener {

    private FirebaseRepository mFirebaseRepository;

    private MutableLiveData<User> mUser = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> mFirebaseUser = new MutableLiveData<FirebaseUser>();

    public AuthenticationViewModel() {
        this.mFirebaseRepository = FirebaseRepository.getInstance();
        mFirebaseRepository.setListener(this);
    }

    @Override
    public void OnSignInSuccessful() {
        mFirebaseUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    public LiveData<User> getUser() { return mUser; }

    public LiveData<FirebaseUser> getFirebaseUser() { return mFirebaseUser; }

    public void SignIn(String email, String password) {
        mFirebaseRepository.SignIn(email, password);
    }

    public void SignOut() {
        mFirebaseUser.setValue(null);
    }
}
