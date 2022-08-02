package org.techtown.HowAboutThisDay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.techtown.HowAboutThisDay.Study_activity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    int i=0;
    ImageButton imageButton3=null, imageButton4=null, imageButton5=null;
    ImageButton imageButton2;
    ImageButton imageButton_left;
    ImageButton imageButton_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton2=(ImageButton)findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity1.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        ImageButton button1 = (ImageButton)findViewById(R.id.imageButton_right);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButton3.setImageResource(R.drawable.study_main);
        imageButton4.setImageResource(R.drawable.meal_main);
        imageButton5.setImageResource(R.drawable.exercise_main);

        imageButton3.setVisibility(View.VISIBLE);
        imageButton4.setVisibility(View.INVISIBLE);
        imageButton5.setVisibility(View.INVISIBLE);


        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                i = 1 - i;

                if (i == 0) {
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                }
                else if(i==1) {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.VISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                    i=i+2;
                }
                else {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.VISIBLE);
                    i=1;
                }
            }
        });


        ImageButton button2 = (ImageButton)findViewById(R.id.imageButton_left);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButton3.setImageResource(R.drawable.study_main);
        imageButton4.setImageResource(R.drawable.meal_main);
        imageButton5.setImageResource(R.drawable.exercise_main);

        imageButton3.setVisibility(View.VISIBLE);
        imageButton4.setVisibility(View.INVISIBLE);
        imageButton5.setVisibility(View.INVISIBLE);


        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                i = 1 - i;

                if (i == 0) {
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                }
                else if(i==1) {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.VISIBLE);
                    i=i+2;
                }
                else {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.VISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                    i=1;
                }
            }
        });


        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Study_activity.class);
                startActivity(intent); //액티비티 이동
            }
        }));


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        }));

        Button btn_close = (Button) findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });


        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    DrawerLayout.DrawerListener listener=new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


}


