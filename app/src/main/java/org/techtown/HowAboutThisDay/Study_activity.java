package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AppCompatActivity;

//import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Study_activity extends AppCompatActivity {

    private ListView title_list;
    private final String URL_List_Study = "http://39.124.122.32:5000/study_plan/list/";

    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();

    public void send_request_Server_List_Study() {
        titleList.clear();
        idList.clear();

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

                    Request request = new Request.Builder()
                            .url(URL_List_Study)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray study_plan_list = jsonObject.getJSONArray("study_plan_list");
                    String user = jsonObject.getString("");
                    for(int i=0; i < study_plan_list.length(); i++){
                        idList.add(study_plan_list.getJSONObject(i).getString("id"));
                        titleList.add(study_plan_list.getJSONObject(i).getString("subject"));
                    }
                    Study_activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Study_activity.this, android.R.layout.simple_list_item_1, titleList);
                            title_list.setAdapter(adapter);
                            System.out.println(titleList);

                            title_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView,
                                                        View view, int position, long id){
                                    String ID = idList.get(position);
                                    if (user.equals("True")){
                                        Intent intent = new Intent(Study_activity.this, ViewStudy.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Study_activity.this, ViewStudy.class);
                                        intent.putExtra("id", ID);
                                        startActivity(intent);
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
        }
        sendData sendData = new sendData();
        sendData.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        title_list = findViewById(R.id.list);

        send_request_Server_List_Study();


        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Study_activity.this, PlusStudy.class);
                startActivity(intent); //액티비티 이동
            }
        }));

    }

}