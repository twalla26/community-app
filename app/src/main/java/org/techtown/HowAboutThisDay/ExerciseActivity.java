package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseActivity.this, PlusExercise.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        androidx.appcompat.widget.AppCompatButton blank1 = (androidx.appcompat.widget.AppCompatButton) findViewById(R.id.blank1);
        blank1.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseActivity.this, ViewExercise.class);
                startActivity(intent); //액티비티 이동
            }
        }));
    }


}
