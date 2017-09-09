package in.desireplace.waytogo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import in.desireplace.waytogo.R;

public class NotificationsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        mAuth = FirebaseAuth.getInstance();

        String user = "";

        TextView heading = (TextView) findViewById(R.id.heading_view);
        TextView body = (TextView) findViewById(R.id.body_view);
        View container = findViewById(R.id.container_notifications);

        if (mAuth.getCurrentUser().getDisplayName() == null) {
            user = mAuth.getCurrentUser().getPhoneNumber();
        } else {
            user = mAuth.getCurrentUser().getDisplayName();
        }

        heading.setText(String.format("Welcome %s", user));
        body.setText("And thanks for joining our student community!\n" +
                "                                       Here’s a quick guide to help you get started.\n" +
                "\n" +
                "Laundry\n" +
                "Experience the perfect way to get your clothes clean.\n" +
                "We pick and drop your clothes at door step.");

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsActivity.this, DetailedNotificationActivity.class));
            }
        });
    }
}
