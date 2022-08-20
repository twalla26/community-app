package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_activity extends AppCompatActivity {
    TextView username_text, email_text;
    private Button logout, myPlan;
    private static final String URL_userInfo = "http://39.124.122.32:5000/userInfo/";
    private static final String URL_Logout = "http://39.124.122.32:5000/auth/logout/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        username_text = findViewById(R.id.login_user);
        email_text = findViewById(R.id.login_email);

        send_request_Server_UserInfo();

        myPlan = findViewById(R.id.btn_myPlan);
        myPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_activity.this, myPlan_activity.class);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_logout();
            }
        });
    }

    public void send_request_Server_logout() {

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(User_activity.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Logout));
                    System.out.println(sessionid);
                    System.out.println(cookieList);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_Logout)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        User_activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        removeKey("session");
                        System.out.println(sessionid);
                        Intent intent = new Intent(User_activity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        User_activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            public String getString(String key) {
                SharedPreferences prefs = User_activity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
            public void removeKey(String key) {
                SharedPreferences prefs = User_activity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove(key);
                edit.commit();
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_UserInfo() {

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(User_activity.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Logout));
                    System.out.println(sessionid);
                    System.out.println(cookieList);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_userInfo)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    JSONObject jsonObject = new JSONObject(response);


                    String username = jsonObject.getString("username");
                    String useremail = jsonObject.getString("email");

                    User_activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username_text.setText(username);
                            email_text.setText(useremail);
                        }
                    });



                } catch (JSONException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            public String getString(String key) {
                SharedPreferences prefs = User_activity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
            public void removeKey(String key) {
                SharedPreferences prefs = User_activity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove(key);
                edit.commit();
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}