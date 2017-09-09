package in.desireplace.waytogo.extras;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.desireplace.waytogo.Constants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        Log.d(Constants.TAG, "From: " + remoteMessage.getFrom());
        Log.d(Constants.TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
