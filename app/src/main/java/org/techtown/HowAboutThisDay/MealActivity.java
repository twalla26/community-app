package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealActivity.this, PlusMeal.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        androidx.appcompat.widget.AppCompatButton blank1 = (androidx.appcompat.widget.AppCompatButton) findViewById(R.id.blank1);
        blank1.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealActivity.this, ViewMeal.class);
                startActivity(intent); //액티비티 이동
            }
        }));
    }


}