package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardenmanagmentapp.model.Notification;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<Notification> mNotification = new MutableLiveData<>();

    public LiveData<Notification> getNotification() { return mNotification; }

    public void setNotification(Notification notification) { mNotification.setValue(notification); }
}
