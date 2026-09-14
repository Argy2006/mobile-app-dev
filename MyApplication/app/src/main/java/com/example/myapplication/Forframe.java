package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Forframe extends AppCompatActivity {

    ImageView imgBackground;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelay);

        imgBackground = findViewById(R.id.imgBackground);
        tvTitle = findViewById(R.id.tvTitle);
    }
}
