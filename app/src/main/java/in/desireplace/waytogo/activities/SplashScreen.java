package in.desireplace.waytogo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.desireplace.waytogo.R;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    private TextView mNoInternetTextView;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        setContentView(R.layout.activity_splash_screen);

        mNoInternetTextView = (TextView) findViewById(R.id.no_internet_text_view);

        checkConnection();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkConnection() {
        if (!isNetworkAvailable()) {
            mNoInternetTextView.setText(R.string.no_internet_message);
            mNoInternetTextView.setBackgroundColor(Color.RED);
        } else {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReference.getDatabase();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            };
            handler.postDelayed(runnable, 4000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkConnection();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
