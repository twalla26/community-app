package org.techtown.HowAboutThisDay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlusStudy extends AppCompatActivity {

    private static final String URL_Plus_Study = "http://59.25.242.66:5000/";
    private EditText Title, Content;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_study);

        Title = findViewById(R.id.title_text);
        Content = findViewById(R.id.content_text);

        Register = findViewById(R.id.btn_plusstudy);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request_Server_PlusStudy();
            }
        });
    }
    public void send_request_Server_PlusStudy() {
        final String title = Title.getText().toString();
        final String content = Content.getText().toString();

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
                    jsonObject.put("title", title);
                    jsonObject.put("content", content);

                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json; charset=uft-8"),
                            jsonObject.toString()
                    );
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(URL_Plus_Study)
                            .build();
                    Response responses = null;
                    responses = client.newCall(request).execute();
                    String response = responses.body().string();
                    System.out.println(response);

                    if (response.contains("success")){
                        PlusStudy.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "작성 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(PlusStudy.this, Study_activity.class);
                        startActivity(intent);
                    } else {
                        PlusStudy.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "오류 발생. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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