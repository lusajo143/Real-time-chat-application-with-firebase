package org.techdealers.mchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Phone extends AppCompatActivity {

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        phone = findViewById(R.id.phone);

        if (!new dbHelper(this).getPhone().equals("null")) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }


    public void add(View view) {
        if (phone.getText().toString().equals("")) {
            phone.setError("Enter phone number");
        } else {
            phone.setText("");
            new dbHelper(this)
                    .insert(phone.getText().toString());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}