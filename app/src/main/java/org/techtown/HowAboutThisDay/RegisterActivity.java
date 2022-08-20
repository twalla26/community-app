package org.techtown.HowAboutThisDay;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    private EditText UserEmail, Username, UserPW, PWcheck;
    private Button submit, idcheck;
    private AlertDialog dialog;
    private boolean valiate = false;
    private static final String URL_Signup = "http://39.124.122.32:5000/auth/signup/";
    private static final String URL_IDcheck = "http://39.124.122.32:5000/auth/signup/checkDup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserEmail = findViewById(R.id.et_email);
        Username = findViewById(R.id.et_username1);
        UserPW = findViewById(R.id.et_password1);
        PWcheck = findViewById(R.id.et_password2);

        idcheck = findViewById(R.id.validateButton1);
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_idcheck();
            }
        });

        submit = findViewById(R.id.btn_register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = Username.getText().toString();
                final String password = UserPW.getText().toString();
                final String email = UserEmail.getText().toString();
                final String passwordch = PWcheck.getText().toString();

                if (username.equals("") || password.equals("") || passwordch.equals("") || email.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                if (!email.matches("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([A-z]*)$")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("올바른 이메일을 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if (!password.equals(passwordch)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                if (!valiate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디 중복체크를 확인해주세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                send_request_Server_signup();
            }
        });
    }
    public void send_request_Server_signup() {
        final String username = Username.getText().toString();
        final String password = UserPW.getText().toString();
        final String email = UserEmail.getText().toString();
        final String passwordch = PWcheck.getText().toString();

        class sendData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
            }
            @Override
            protected void onProgressUpdate(Void... values){
                super.onProgressUpdate(values);
            }
            @Override
            protected void onCancelled(String s){
                super.onCancelled(s);
            }
            @Override
            protected void onCancelled(){
                super.onCancelled();
            }
            @Override
            protected String doInBackground(Void... voids){
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username);
                    jsonObject.put("password1", password);
                    jsonObject.put("password2", passwordch);
                    jsonObject.put("email", email);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(URL_Signup)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity1.class);
                        startActivity(intent);
                    } else {
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_idcheck() {
        final String username = Username.getText().toString();

        class sendData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
            }
            @Override
            protected void onProgressUpdate(Void... values){
                super.onProgressUpdate(values);
            }
            @Override
            protected void onCancelled(String s){
                super.onCancelled(s);
            }
            @Override
            protected void onCancelled(){
                super.onCancelled();
            }
            @Override
            protected String doInBackground(Void... voids){
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(URL_IDcheck)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("True")){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용가능한 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                valiate = true;
                                idcheck.setBackgroundColor(Color.GRAY);
                            }
                        });
                    } else {
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            }
                        });
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}