package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Forcolor extends AppCompatActivity {

    TextView colorName;
    ProgressBar progressBar;
    Button btnRed, btnGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_colour);

        colorName = findViewById(R.id.ColorName);
        progressBar = findViewById(R.id.progressBar);
        btnRed = findViewById(R.id.BtnRed);
        btnGreen = findViewById(R.id.BtnGreen);

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor("RED", "#E72121");
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor("GREEN", "#0AA63A");
            }
        });
    }

    private void selectColor(final String name, final String colorHex) {
        progressBar.setVisibility(View.VISIBLE);
        btnRed.setEnabled(false);
        btnGreen.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                btnRed.setEnabled(true);
                btnGreen.setEnabled(true);

                colorName.setText("Selected: " + name);
                colorName.setTextColor(Color.parseColor(colorHex));
                Toast.makeText(Forcolor.this, name + " color selected!", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }
}
