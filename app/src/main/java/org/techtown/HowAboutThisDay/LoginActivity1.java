package org.techtown.HowAboutThisDay;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity1 extends AppCompatActivity {
    private EditText Username, UserPW;
    private Button login_progress;
    private static final String URL_Login = "http://39.124.122.32:5000/auth/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        Username = findViewById(R.id.et_id);
        UserPW = findViewById(R.id.et_pass);

        login_progress = findViewById(R.id.btn_login);
        login_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_login();
            }
        });

        AppCompatButton btn_register = (AppCompatButton) findViewById(R.id.btn_register);
        btn_register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity1.this, RegisterActivity.class);
                startActivity(intent); //액티비티 이동
            }
        }));
    }
    public void send_request_Server_login() {
        final String username = Username.getText().toString();
        final String password = UserPW.getText().toString();


        class sendData extends AsyncTask<Void, Void, String>{
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
                    PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(LoginActivity1.this));
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Login));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(URL_Login)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String res_cookie = responses.headers().get("Set-Cookie");
                    System.out.println(res_cookie);

                    String response = responses.body().string();

                    String id = res_cookie.split("=")[0];
                    String value = res_cookie.split("=")[1].toString().split(";")[0];
                    String session = id + "=" + value;

                    System.out.println(id);
                    System.out.println(value);

                    Cookie cookie = new Cookie.Builder()
                            .name(id)
                            .value(value)
                            .domain("39.124.122.32")
                            .path("/auth/checkSession/")
                                    .build();
                    cookieList.add(cookie);
                    System.out.println(cookieList);


                    setString(id, session);
                    String check = getString(id);
                    System.out.println(check);


                     if (response.contains("password")){
                        LoginActivity1.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (response.contains("user")) {
                        LoginActivity1.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "존재하지 않는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (response.contains("success")){
                         LoginActivity1.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(getApplicationContext(), String.format("%s님 환영합니다.", username), Toast.LENGTH_SHORT).show();
                             }
                         });
                         Intent intent = new Intent(LoginActivity1.this, MainActivity.class);

                         startActivity(intent);
                     } else {
                        LoginActivity1.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
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

            private void setString(String key, String value){
                SharedPreferences prefs = LoginActivity1.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value);
                editor.commit();
            }
            public String getString(String key) {
                SharedPreferences prefs = LoginActivity1.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}