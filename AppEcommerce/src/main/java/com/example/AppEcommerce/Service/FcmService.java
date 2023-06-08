package com.example.AppEcommerce.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FcmService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public String sendNotification(Map<String, String> data, String token) throws FirebaseMessagingException {
        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .build();

        return firebaseMessaging.send(message);
    }


}