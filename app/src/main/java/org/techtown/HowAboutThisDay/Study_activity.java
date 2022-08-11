package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Study_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Study_activity.this, PlusStudy.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        androidx.appcompat.widget.AppCompatButton blank1 = (androidx.appcompat.widget.AppCompatButton) findViewById(R.id.blank1);
        blank1.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Study_activity.this, ViewStudy.class);
                startActivity(intent); //액티비티 이동
            }
        }));
    }


}