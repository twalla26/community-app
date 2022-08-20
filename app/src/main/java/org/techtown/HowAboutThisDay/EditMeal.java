package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class EditMeal extends AppCompatActivity {

    private TextView title_view, content_view;
    String title_base, content_base;
    private Button edit_btn, delete_btn;
    private String URL_Content_Meal = "http://39.124.122.32:5000/meal_plan/modify/";
    private String URL_Edit_Meal = "http://39.124.122.32:5000/meal_plan/modify/";
    private String URL_Delete_Meal = "http://39.124.122.32:5000/meal_plan/delete/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        title_view = findViewById(R.id.title_view);
        content_view = findViewById(R.id.content_view);

        Intent intent = getIntent();
        String ID = intent.getExtras().getString("id");
        System.out.println(ID);

        send_request_Server_Edit_Meal();

        URL_Content_Meal = URL_Content_Meal + String.format("%s/", ID);
        URL_Edit_Meal = URL_Edit_Meal + String.format("%s/", ID);
        URL_Delete_Meal = URL_Delete_Meal + String.format("%s/", ID);

        edit_btn = findViewById(R.id.btn_editstudy);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_Edit_content(ID);
            }
        });

        delete_btn = findViewById(R.id.btn_deletestudy);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_Delete_content(ID);
            }
        });

    }
    public void send_request_Server_Edit_Meal() {

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(EditMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_Content_Meal)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    JSONObject jsonObject = new JSONObject(response);
                    title_base = jsonObject.getString("subject");
                    content_base = jsonObject.getString("content");

                    EditMeal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title_view.setText(title_base);
                            content_view.setText(content_base);
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
                SharedPreferences prefs = EditMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_Edit_content(String id) {
        String ID = id;
        System.out.println(ID);


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
                final String title = title_view.getText().toString();
                final String content = content_view.getText().toString();

                try {
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(EditMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);

                    System.out.println(title);
                    System.out.println(content);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("subject", title);
                    jsonObject.put("content", content);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );
                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .post(requestBody)
                            .url(URL_Edit_Meal)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);


                    if (response.contains("success")){
                        EditMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(EditMeal.this, ViewMeal_modified.class);
                        intent.putExtra("id", ID);
                        startActivity(intent);
                    } else {
                        EditMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
            public String getString(String key) {
                SharedPreferences prefs = EditMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_Delete_content(String id) {
        String ID = id;

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(EditMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_Delete_Meal)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        EditMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(EditMeal.this, MealActivity.class);
                        intent.putExtra("id", ID);
                        startActivity(intent);
                    } else {
                        EditMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            public String getString(String key) {
                SharedPreferences prefs = EditMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}