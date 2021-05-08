package org.techdealers.mchat;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ImageView image = findViewById(R.id.full_image);

        Glide.with(this)
                .load(getIntent().getStringExtra("img_url"))
                .into(image);
    }
}