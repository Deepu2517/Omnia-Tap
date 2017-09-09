package in.desireplace.waytogo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import in.desireplace.waytogo.R;

public class DetailedNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_notification);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String user;

        if (auth.getCurrentUser().getDisplayName() == null) {
            user = auth.getCurrentUser().getPhoneNumber();
        } else {
            user = auth.getCurrentUser().getDisplayName();
        }

        TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);
        welcomeTextView.setText(String.format("Welcome %s", user));
    }
}
