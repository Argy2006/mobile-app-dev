package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Forcheckbox extends AppCompatActivity {

    CheckBox cbCoding, cbGaming, cbMusic, cbReading, cbSports, cbTraveling, cbTerms;
    Button btnSubmitHobbies;
    TextView tvSelectedHobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobbies_checkbox);

        cbCoding = findViewById(R.id.cbCoding);
        cbGaming = findViewById(R.id.cbGaming);
        cbMusic = findViewById(R.id.cbMusic);
        cbReading = findViewById(R.id.cbReading);
        cbSports = findViewById(R.id.cbSports);
        cbTraveling = findViewById(R.id.cbTraveling);
        cbTerms = findViewById(R.id.cbTerms);

        btnSubmitHobbies = findViewById(R.id.btnSubmitHobbies);
        tvSelectedHobbies = findViewById(R.id.tvSelectedHobbies);

        // Initially disable submit button until terms checkbox is checked
        btnSubmitHobbies.setEnabled(cbTerms.isChecked());

        // Toggle submit button state when terms checkbox changes
        cbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnSubmitHobbies.setEnabled(isChecked);
            }
        });

        btnSubmitHobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedHobbies = new ArrayList<>();

                if (cbCoding.isChecked()) selectedHobbies.add("Coding & Programming");
                if (cbGaming.isChecked()) selectedHobbies.add("Gaming");
                if (cbMusic.isChecked()) selectedHobbies.add("Listening to Music");
                if (cbReading.isChecked()) selectedHobbies.add("Reading Books");
                if (cbSports.isChecked()) selectedHobbies.add("Playing Sports");
                if (cbTraveling.isChecked()) selectedHobbies.add("Traveling");

                if (!selectedHobbies.isEmpty()) {
                    String result = "Selected Hobbies:\n• " + String.join("\n• ", selectedHobbies);
                    tvSelectedHobbies.setText(result);
                    Toast.makeText(Forcheckbox.this, "Hobbies saved!", Toast.LENGTH_SHORT).show();
                } else {
                    tvSelectedHobbies.setText("No hobbies selected.");
                    Toast.makeText(Forcheckbox.this, "Please select at least one hobby", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
