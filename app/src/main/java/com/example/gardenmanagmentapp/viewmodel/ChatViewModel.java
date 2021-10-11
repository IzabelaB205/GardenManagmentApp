package com.example.gardenmanagmentapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardenmanagmentapp.model.ChatMessage;
import com.example.gardenmanagmentapp.repository.FirebaseRepository;

public class ChatViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    private MutableLiveData<ChatMessage> mChatMessage = new MutableLiveData<>();

    public ChatViewModel() {
        mFirebaseRepository = FirebaseRepository.getInstance();
    }

    public void setNotification(ChatMessage chatMessage) { mChatMessage.setValue(chatMessage); }

    public void UploadChatMessageToFirebase(ChatMessage chatMessage) {
        mFirebaseRepository.UploadChatMessageToFirebase(chatMessage);
    }
}
