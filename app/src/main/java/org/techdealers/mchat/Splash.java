package org.techdealers.mchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (new dbHelper(this).getPhone().equals("null")) {
                startActivity(new Intent(Splash.this, MainActivity.class));
            } else {
                startActivity(new Intent(Splash.this, Username.class));
            }
        }, 2000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}