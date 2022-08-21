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

public class ViewExercise_modified extends AppCompatActivity {
    private TextView title_view, content_view, user_View, date_View;
    private RecyclerView comment_list;
    private EditText comment;
    private Button comment_btn, edit_btn;
    private String title, content, user, modified_date, comment_Text;
    private String URL_Content_Exercise = "http://39.124.122.32:5000/exercise_plan/detail/";
    private static final String URL_send_Comment_Exercise = "http://39.124.122.32:5000/exercise_plan/detail/";
    private String URL_Comment_Delete = "http://39.124.122.32:5000/exercise_plan/comment_delete/";

    // 댓글 담을 댓글 리스트 생성
    ArrayList<commentList_item> commentlist = new ArrayList<>();
    ArrayList<String> commentID_list = new ArrayList<>();
    Comment_Adapter comment_listAdapter = new Comment_Adapter();
    String comment_toList = new String();
    String user_toList = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_user_modified);

        // 레이아웃 뷰 연결
        title_view = findViewById(R.id.title_view);
        content_view = findViewById(R.id.content_view);
        comment = findViewById(R.id.comment_text);
        user_View = findViewById(R.id.user);
        date_View = findViewById(R.id.date);

        comment_list = findViewById(R.id.recycler_comment);

        // 이전 레이아웃에서 게시판 ID 받아오기
        Intent intent = getIntent();
        String ID = intent.getExtras().getString("id");
        System.out.println(ID);

        // 아이디 적용한 URL
        String URL_Content_Exercise_id = URL_Content_Exercise + String.format("%s/", ID);
        String URL_send_Comment_Exercise_id = URL_send_Comment_Exercise + String.format("%s/", ID);

        // 서버에서 제목, 내용 댓글 가져오기
        send_request_Server_Content_Exercise(URL_Content_Exercise_id);

        // 댓글 작성하기
        comment_btn = findViewById(R.id.comment_send);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_Text = comment.getText().toString();
                if (comment_Text.equals("")){
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                send_request_Server_Comment_Exercise(URL_Content_Exercise_id, URL_send_Comment_Exercise_id);
            }
        });

        // 글 수정하기
        edit_btn = findViewById(R.id.Edit_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewExercise_modified.this, EditExercise.class);
                intent.putExtra("id", ID);
                startActivity(intent);
            }
        });

    }


    // 서버에서 글 제목, 내용, 댓글 받아오기
    public void send_request_Server_Content_Exercise(String URL) {
        String URL_id = URL;
        commentlist.clear();

        class sendData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ViewExercise_modified.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_Content_Exercise));
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
                    modified_date = jsonObject.getString("modify_date");
                    JSONArray comment_list_exercise = jsonObject.getJSONArray("commentList");
                    for (int i = 0; i < comment_list_exercise.length(); i++) {
                        comment_toList = comment_list_exercise.getJSONObject(i).getString("content");
                        user_toList = comment_list_exercise.getJSONObject(i).getString("user");
                        commentID_list.add(comment_list_exercise.getJSONObject(i).getString("id"));
                        commentlist.add(new commentList_item(comment_toList, user_toList));
                    }
                    System.out.println(comment_list);
                    ViewExercise_modified.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title_view.setText(title);
                            content_view.setText(content);
                            user_View.setText(user);
                            date_View.setText(modified_date);


                            comment_list.setAdapter(comment_listAdapter);
                            comment_list.setLayoutManager(new LinearLayoutManager(ViewExercise_modified.this));
                            comment_listAdapter.setCommentList(commentlist);

                            comment_listAdapter.setOnItemClickListener(new Comment_Adapter.OnItemClickListener() {
                                @Override
                                public void onDeleteClick(View view, int position) {
                                    String deleteID = commentID_list.get(position);
                                    String URL_comment_Delete_id = URL_Comment_Delete + String.format("%s/", deleteID);
                                    send_request_Server_Comment_Delete(URL_id, URL_comment_Delete_id);
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public String getString(String key) {
                SharedPreferences prefs = ViewExercise_modified.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }

    public void send_request_Server_Comment_Exercise(String URL_content, String URL_comment) {
        String URL_Content_Study_id = URL_content;
        String URL_send_Comment_Study_id = URL_comment;
        EditText commentText = findViewById(R.id.comment_text);

        final String Comment = comment.getText().toString();

        class sendData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ViewExercise_modified.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_send_Comment_Study_id));
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

                    if (response.contains("success")) {
                        commentText.setText(null);
                        send_request_Server_Content_Exercise(URL_Content_Study_id);
                    } else {
                        ViewExercise_modified.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "오류발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public String getString(String key) {
                SharedPreferences prefs = ViewExercise_modified.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }

    public void send_request_Server_Comment_Delete(String URL_content, String URL_comment_delete) {
        String URL_Content_Study_id = URL_content;
        String URL_send_Comment_Delete_id = URL_comment_delete;


        class sendData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ViewExercise_modified.this));
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

                    if (response.contains("success")) {
                        send_request_Server_Content_Exercise(URL_Content_Study_id);
                    } else {
                        ViewExercise_modified.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "오류발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public String getString(String key) {
                SharedPreferences prefs = ViewExercise_modified.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
}
