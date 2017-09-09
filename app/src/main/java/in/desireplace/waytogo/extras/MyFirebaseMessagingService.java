package in.desireplace.waytogo.extras;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.desireplace.waytogo.Constants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(Constants.TAG, "From: " + remoteMessage.getFrom());
        Log.d(Constants.TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
