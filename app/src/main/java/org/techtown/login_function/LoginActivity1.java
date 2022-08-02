package org.techtown.login_function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        AppCompatButton btn_register = (AppCompatButton) findViewById(R.id.btn_register);
        btn_register.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity1.this, RegisterActivity.class);
                startActivity(intent); //액티비티 이동
            }
        }));
    }
}