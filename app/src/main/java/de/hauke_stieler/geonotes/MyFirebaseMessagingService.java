package de.hauke_stieler.geonotes;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.util.Log;

import androidx.annotation.NonNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull @org.jetbrains.annotations.NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
