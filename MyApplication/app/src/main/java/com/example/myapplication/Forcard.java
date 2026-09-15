package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Forcard extends AppCompatActivity {

    CardView cardProfile, cardCourses, cardSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);

        cardProfile = findViewById(R.id.cardProfile);
        cardCourses = findViewById(R.id.cardCourses);
        cardSettings = findViewById(R.id.cardSettings);

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Forcard.this, "Student Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Forcard.this, "My Courses Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Forcard.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
