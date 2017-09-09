package in.desireplace.waytogo.extras;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.desireplace.waytogo.Constants;

public class FirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(Constants.TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }
}
