package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardenmanagmentapp.model.Picture;
import com.example.gardenmanagmentapp.repository.FirebaseRepository;

public class GalleryViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    private MutableLiveData<Picture> mPicture = new MutableLiveData<>();

    public GalleryViewModel() {
        mFirebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<Picture> getPicture() { return mPicture; }

    public void setPicture(Picture picture) { mPicture.setValue(picture); }
}
