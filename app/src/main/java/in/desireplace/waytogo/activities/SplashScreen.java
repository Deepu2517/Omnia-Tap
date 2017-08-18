package in.desireplace.waytogo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.desireplace.waytogo.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread t = new Thread() {
            public void run() {
                try {
                    int time = 0;
                    while (time < 4000) {
                        sleep(100);
                        time += 100;
                    }
                }
                catch (InterruptedException e) {
                    // do nothing
                }
                finally {
                    finish();
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
        t.start();
    }
}
