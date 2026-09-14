package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;

public class Forlinear extends AppCompatActivity {

    ShapeableImageView imgProfile;
    EditText etName, etRole, etPhone, etEmail, etLocation, etEducation, etGithub, etWork, etAbout;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlay);

        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etRole = findViewById(R.id.etRole);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        etEducation = findViewById(R.id.etEducation);
        etGithub = findViewById(R.id.etGithub);
        etWork = findViewById(R.id.etWork);
        etAbout = findViewById(R.id.etAbout);
        btnLogout = findViewById(R.id.btnLogout);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Forlinear.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
