package org.techtown.HowAboutThisDay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class Only_ViewStudy extends AppCompatActivity {
    private TextView title_view, content_view;
    private RecyclerView comment_list;
    private EditText comment;
    private Button comment_btn;
    private String title, content;
    private String URL_Content_Study = "http://39.124.122.32:5000/study_plan/detail/";
    private static final String URL_send_Comment_Study = "http://39.124.122.32:5000/study_plan/detail/";

    ArrayList<commentList_item> commentlist = new ArrayList<>();
    Comment_Adapter comment_listAdapter = new Comment_Adapter();
    String comment_toList = new String();
    String user_toList = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_study);

        title_view = findViewById(R.id.title_view);
        content_view = findViewById(R.id.content_view);
        comment_list = findViewById(R.id.recycler_comment);
        comment = findViewById(R.id.comment_text);

        Intent intent = getIntent();
        String ID = intent.getExtras().getString("id");
        System.out.println(ID);

        String URL_Content_Study_id = URL_Content_Study + String.format("%s/", ID);
        String URL_send_Comment_Study_id = URL_send_Comment_Study + String.format("%s/", ID);

        send_request_Server_Content_Study(URL_Content_Study_id);


        comment_btn = findViewById(R.id.comment_send);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_Comment_study(URL_Content_Study_id, URL_send_Comment_Study_id);
            }
        });

    }

    public void send_request_Server_Content_Study(String URL) {
        String URL_id = URL;
        commentlist.clear();

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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Only_ViewStudy.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Study));
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
                    JSONArray comment_list_study = jsonObject.getJSONArray("commentList");
                    for(int i=0; i < comment_list_study.length(); i++){
                        comment_toList = comment_list_study.getJSONObject(i).getString("content");
                        user_toList = comment_list_study.getJSONObject(i).getString("user");
                        commentlist.add(new commentList_item(comment_toList, user_toList));
                    }
                    System.out.println(comment_list);
                    Only_ViewStudy.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title_view.setText(title);
                            content_view.setText(content);

                            comment_list.setAdapter(comment_listAdapter);
                            comment_list.setLayoutManager(new LinearLayoutManager(Only_ViewStudy.this));
                            comment_listAdapter.setCommentList(commentlist);

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
                SharedPreferences prefs = Only_ViewStudy.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_Comment_study(String URL, String URL_comment) {
        String URL_Content_Study_id = URL;
        String URL_send_Comment_Study_id = URL_comment;
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
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Only_ViewStudy.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Study));
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
                            .url(URL_send_Comment_Study_id)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        commentText.setText(null);
                        send_request_Server_Content_Study(URL_Content_Study_id);
                    }
                    else {
                        Only_ViewStudy.this.runOnUiThread(new Runnable() {
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
                SharedPreferences prefs = Only_ViewStudy.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}
