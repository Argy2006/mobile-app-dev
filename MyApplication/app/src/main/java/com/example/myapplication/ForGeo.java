package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ForGeo extends AppCompatActivity {

    Button btnOpenLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geotech);

        btnOpenLocation = findViewById(R.id.btnOpenLocation);

        btnOpenLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = 28.6139;
                double longitude = 77.2090;
                String label = "New Delhi";

                Uri uri = Uri.parse("geo:"+latitude + ","+ longitude+ "?q="+latitude+ ","+ longitude + "("+label+")");

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}