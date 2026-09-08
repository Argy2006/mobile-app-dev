package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Forgrid extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgridlayout);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                Toast.makeText(Forgrid.this, "Button " + clickedButton.getText() + " Clicked", Toast.LENGTH_SHORT).show();
            }
        };

        if (btn1 != null) btn1.setOnClickListener(clickListener);
        if (btn2 != null) btn2.setOnClickListener(clickListener);
        if (btn3 != null) btn3.setOnClickListener(clickListener);
        if (btn4 != null) btn4.setOnClickListener(clickListener);
        if (btn5 != null) btn5.setOnClickListener(clickListener);
        if (btn6 != null) btn6.setOnClickListener(clickListener);
        if (btn7 != null) btn7.setOnClickListener(clickListener);
        if (btn8 != null) btn8.setOnClickListener(clickListener);
        if (btn9 != null) btn9.setOnClickListener(clickListener);
    }
}
