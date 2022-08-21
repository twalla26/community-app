package org.techtown.HowAboutThisDay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Only_ViewMeal extends AppCompatActivity {
    private TextView title_view, content_view, user_view, date_view;
    private RecyclerView comment_list;
    private EditText comment;
    private Button comment_btn;
    private String title, content, user, date, comment_Text;
    private String URL_Content_Meal = "http://39.124.122.32:5000/meal_plan/detail/";
    private static final String URL_send_Comment_Meal = "http://39.124.122.32:5000/meal_plan/detail/";
    private String URL_Comment_Delete = "http://39.124.122.32:5000/meal_plan/comment_delete/";


    ArrayList<commentList_item> commentlist = new ArrayList<>();
    ArrayList<String> commentID_list = new ArrayList<>();
    Comment_Adapter comment_listAdapter = new Comment_Adapter();
    String comment_toList = new String();
    String user_toList = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        title_view = findViewById(R.id.title_view);
        content_view = findViewById(R.id.content_view);
        comment_list = findViewById(R.id.recycler_comment);
        comment = findViewById(R.id.comment_text);
        user_view = findViewById(R.id.user);
        date_view = findViewById(R.id.date);

        Intent intent = getIntent();
        String ID = intent.getExtras().getString("id");
        System.out.println(ID);

        String URL_Content_Meal_id = URL_Content_Meal + String.format("%s/", ID);
        String URL_send_Comment_Meal_id = URL_send_Comment_Meal + String.format("%s/", ID);

        send_request_Server_Content_Meal(URL_Content_Meal_id);


        comment_btn = findViewById(R.id.comment_send);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_Text = comment.getText().toString();
                if (comment_Text.equals("")){
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                send_request_Server_Comment_Meal(URL_Content_Meal_id, URL_send_Comment_Meal_id);
            }
        });

    }

    public void send_request_Server_Content_Meal(String URL) {
        String URL_id = URL;
        commentlist.clear();
        commentID_list.clear();

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Only_ViewMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_id)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    JSONObject jsonObject = new JSONObject(response);
                    title = jsonObject.getString("subject");
                    content = jsonObject.getString("content");
                    user = jsonObject.getString("user");
                    date = jsonObject.getString("create_date");
                    JSONArray comment_list_meal = jsonObject.getJSONArray("commentList");
                    for(int i=0; i < comment_list_meal.length(); i++){
                        comment_toList = comment_list_meal.getJSONObject(i).getString("content");
                        user_toList = comment_list_meal.getJSONObject(i).getString("user");
                        commentID_list.add(comment_list_meal.getJSONObject(i).getString("id"));
                        commentlist.add(new commentList_item(comment_toList, user_toList));
                    }
                    System.out.println(commentID_list);
                    Only_ViewMeal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title_view.setText(title);
                            content_view.setText(content);
                            user_view.setText(user);
                            date_view.setText(date);

                            comment_list.setAdapter(comment_listAdapter);
                            comment_list.setLayoutManager(new LinearLayoutManager(Only_ViewMeal.this));
                            comment_listAdapter.setCommentList(commentlist);

                            comment_listAdapter.setOnItemClickListener(new Comment_Adapter.OnItemClickListener() {
                                @Override
                                public void onDeleteClick(View view, int position) {
                                    System.out.println("Delete_Click");
                                    String deleteID = commentID_list.get(position);
                                    String URL_comment_Delete_id = URL_Comment_Delete + String.format("%s/", deleteID);
                                    send_request_Server_Comment_Delete(URL_id, URL_comment_Delete_id);
                                }
                            });

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
                SharedPreferences prefs = Only_ViewMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_Comment_Meal(String URL, String URL_comment) {
        String URL_Content_Meal_id = URL;
        String URL_send_Comment_Meal_id = URL_comment;
        EditText commentText = findViewById(R.id.comment_text);

        final String Comment = comment.getText().toString();

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Only_ViewMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", Comment);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .post(requestBody)
                            .url(URL_send_Comment_Meal_id)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        commentText.setText(null);
                        send_request_Server_Content_Meal(URL_Content_Meal_id);
                    }
                    else {
                        Only_ViewMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "오류발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
                SharedPreferences prefs = Only_ViewMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_Comment_Delete(String URL_content, String URL_comment_delete) {
        String URL_Content_Meal_id = URL_content;
        String URL_send_Comment_Delete_id = URL_comment_delete;


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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Only_ViewMeal.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_send_Comment_Delete_id));
                    System.out.println(sessionid);
                    System.out.println(cookieList);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();


                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_send_Comment_Delete_id)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        send_request_Server_Content_Meal(URL_Content_Meal_id);
                    } else if (response.contains("Permission")){
                        Only_ViewMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Only_ViewMeal.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "오류발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
            public String getString(String key) {
                SharedPreferences prefs = Only_ViewMeal.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}
