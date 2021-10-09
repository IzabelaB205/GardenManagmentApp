package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardenmanagmentapp.model.Picture;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<Picture> mPicture = new MutableLiveData<>();

    public LiveData<Picture> getPicture() { return mPicture; }

    public void setPicture(Picture picture) { mPicture.setValue(picture); }
}
