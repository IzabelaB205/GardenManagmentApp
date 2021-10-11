package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.repository.FirebaseRepository;

public class NotificationsViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    private MutableLiveData<Notification> mNotification = new MutableLiveData<>();

    public NotificationsViewModel() {
        mFirebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<Notification> getNotification() { return mNotification; }

    public void setNotification(Notification notification) { mNotification.setValue(notification); }

    public void UploadNotificationToFirebase(Notification notification) {
        mFirebaseRepository.UploadNotificationToFirebase(notification);
    }
}
