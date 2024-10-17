package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.com.Computer;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindID extends AppCompatActivity {

    private EditText FindID,FindName;

    private TextView FindResultID,FindResultPwd;

    private Button FindSubmit;

    private String ProjectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        //初始化
        Init();

        ButtonClick();
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        FindID = (EditText) findViewById(R.id.FindID);
        FindName = (EditText) findViewById(R.id.FindName);
        FindResultID = (TextView) findViewById(R.id.FindResultID);
        FindResultPwd = (TextView) findViewById(R.id.FindResultPwd);
        FindSubmit = (Button) findViewById(R.id.FindSubmit);
    }

    private void ButtonClick(){
        FindSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FindID.getText().toString().equals("")||FindName.getText().toString().equals("")){
                    Toast.makeText(FindID.this, "还有信息未填", Toast.LENGTH_SHORT).show();
                }else{
                    UserFindBackMultithreading(ProjectPath+"AppFindBackServlet",
                            FindID.getText().toString(),FindName.getText().toString());
                }
            }
        });
    }

    /*-------------------------------------------------------------------------------------*/
    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void UserFindBackMultithreading(String url,String p,String n){
        new Thread(new Runnable() {
            public void run() {
                UserFindBackWebRecord(url,p,n);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void UserFindBackWebRecord(String url,String p,String n){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Phone", p)
                .add("Name", n)
                .build();

        // 构建HTTP请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 处理响应数据
            String responseData = response.body().string();
            UserFindBack(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UserFindBack(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserFindBackLoad(str);
            }
        });
    }

    private void UserFindBackLoad(String str){
        if(str.equals("False")){
            Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
        }else{
            String[] s = str.split("#");
            FindResultID.setText(s[0]);
            FindResultPwd.setText(s[1]);
        }
    }
}