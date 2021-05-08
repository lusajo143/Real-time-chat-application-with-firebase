package org.techdealers.mchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Username extends AppCompatActivity {

    private EditText Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        Username = findViewById(R.id.username);

    }


    public void add(View view) {
        if (Username.getText().toString().equals("")) {
            Username.setError("Enter Username here");
        } else {
            new dbHelper(this)
                    .insert(Username.getText().toString());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}