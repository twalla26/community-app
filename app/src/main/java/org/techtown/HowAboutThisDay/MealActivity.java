package org.techtown.HowAboutThisDay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MealActivity extends AppCompatActivity {
    int page=1;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private RecyclerView plan_list;
    private final String URL_List_Meal = "http://39.124.122.32:5000/meal_plan/list/";

    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<String> owntextList = new ArrayList<>();
    ArrayList<planList_item> planItems = new ArrayList<planList_item>();
    ArrayList<String> modifiedList = new ArrayList<>();

    CustomAdapter plan_listAdapter = new CustomAdapter();


    public void send_request_Server_List_Meal() {
        titleList.clear();
        idList.clear();
        userList.clear();

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
                    String URL_page_List = URL_List_Meal + String.format("?page=%d", page);
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MealActivity.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_List_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_page_List)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray meal_plan_list = jsonObject.getJSONArray("meal_plan_list");
                    for(int i=0; i < meal_plan_list.length(); i++){
                        titleList.add(meal_plan_list.getJSONObject(i).getString("subject"));
                        userList.add(meal_plan_list.getJSONObject(i).getString("username"));
                        idList.add(meal_plan_list.getJSONObject(i).getString("id"));
                        owntextList.add(meal_plan_list.getJSONObject(i).getString("plan_writer"));
                        String title = meal_plan_list.getJSONObject(i).getString("subject");
                        String user = meal_plan_list.getJSONObject(i).getString("username");
                        String date = meal_plan_list.getJSONObject(i).getString("create_date");
                        modifiedList.add(meal_plan_list.getJSONObject(i).getString("modified"));
                        planItems.add(new planList_item(title, user, date));
                    }
                    MealActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println(planItems);
                            plan_list.setAdapter(plan_listAdapter);
                            plan_list.setLayoutManager(new LinearLayoutManager(MealActivity.this));
                            plan_listAdapter.setPlanList(planItems);
                            progressBar.setVisibility(View.GONE);

                            System.out.println(titleList);
                            System.out.println(userList);
                            System.out.println(owntextList);

                            plan_listAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String ID = idList.get(position);
                                    String check = owntextList.get(position);
                                    System.out.println(check);
                                    if (owntextList.get(position).equals("True") & modifiedList.get(position).equals("True")){
                                        Intent intent = new Intent(MealActivity.this, ViewMeal_modified.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("True") & modifiedList.get(position).equals("False")){
                                        Intent intent = new Intent(MealActivity.this, ViewMeal.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("False") & modifiedList.get(position).equals("True")){
                                        Intent intent = new Intent(MealActivity.this, Only_ViewMeal_modified.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("False") & modifiedList.get(position).equals("False")){
                                        Intent intent = new Intent(MealActivity.this, Only_ViewMeal.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else {
                                        MealActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "오류 발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

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
                SharedPreferences prefs = MealActivity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }
    public void send_request_Server_List_Meal_Scroll() {

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
                    String URL_page_List = URL_List_Meal + String.format("?page=%d", page);
                    CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MealActivity.this));
                    String sessionid = getString("session");
                    List<Cookie> cookieList = cookieJar.loadForRequest(HttpUrl.parse(URL_List_Meal));
                    System.out.println(sessionid);
                    System.out.println(cookieList);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(cookieJar)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Cookie", sessionid)
                            .url(URL_page_List)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray meal_plan_list = jsonObject.getJSONArray("meal_plan_list");
                    for(int i=0; i < meal_plan_list.length(); i++){
                        titleList.add(meal_plan_list.getJSONObject(i).getString("subject"));
                        userList.add(meal_plan_list.getJSONObject(i).getString("username"));
                        idList.add(meal_plan_list.getJSONObject(i).getString("id"));
                        owntextList.add(meal_plan_list.getJSONObject(i).getString("plan_writer"));
                        String title = meal_plan_list.getJSONObject(i).getString("subject");
                        String user = meal_plan_list.getJSONObject(i).getString("username");
                        String date = meal_plan_list.getJSONObject(i).getString("create_date");
                        modifiedList.add(meal_plan_list.getJSONObject(i).getString("modified"));
                        planItems.add(new planList_item(title, user, date));
                    }
                    MealActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            plan_list.setAdapter(plan_listAdapter);
                            plan_list.setLayoutManager(new LinearLayoutManager(MealActivity.this));
                            plan_listAdapter.setPlanList(planItems);
                            progressBar.setVisibility(View.GONE);


                            plan_listAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String ID = idList.get(position);
                                    String check = owntextList.get(position);
                                    System.out.println(check);
                                    if (owntextList.get(position).equals("True") & modifiedList.get(position).equals("True")){
                                        Intent intent = new Intent(MealActivity.this, ViewMeal_modified.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("True") & modifiedList.get(position).equals("False")){
                                        Intent intent = new Intent(MealActivity.this, ViewMeal.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("False") & modifiedList.get(position).equals("True")){
                                        Intent intent = new Intent(MealActivity.this, Only_ViewMeal_modified.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else if (owntextList.get(position).equals("False") & modifiedList.get(position).equals("False")){
                                        Intent intent = new Intent(MealActivity.this, Only_ViewMeal.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else {
                                        MealActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "오류 발생 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
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
                SharedPreferences prefs = MealActivity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String value = prefs.getString(key, " ");
                return value;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        plan_list = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressCircle);
        scrollView = findViewById(R.id.scrollview);

        send_request_Server_List_Meal();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);

                    send_request_Server_List_Meal_Scroll();
                }
            }
        });

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealActivity.this, PlusMeal.class);
                startActivity(intent); //액티비티 이동
            }
        }));

    }


}