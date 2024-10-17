package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.zxy.pojo.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRMB extends AppCompatActivity {

    private Button c1,c2,c3,c4,c5,c6,c7,c8,c9,COK;

    private EditText CRMB;

    private User LoginUser;

    private String UserJson,ProjectPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rmb);
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        LoginUser = new Gson().fromJson(UserJson,User.class);
        Init();
        ClickBtn();
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        c1 = (Button) findViewById(R.id.c1);
        c2 = (Button) findViewById(R.id.c2);
        c3 = (Button) findViewById(R.id.c3);
        c4 = (Button) findViewById(R.id.c4);
        c5 = (Button) findViewById(R.id.c5);
        c6 = (Button) findViewById(R.id.c6);
        c7 = (Button) findViewById(R.id.c7);
        c8 = (Button) findViewById(R.id.c8);
        c9 = (Button) findViewById(R.id.c9);
        COK = (Button) findViewById(R.id.COK);
        CRMB = (EditText) findViewById(R.id.CRMB);
    }

    private void ClickBtn(){
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("6");
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("32");
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("64");
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("128");
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("256");
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("512");
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("1024");
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("2048");
            }
        });
        c9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRMB.setText("4096");
            }
        });
        COK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser.setRMB(LoginUser.getRMB()+Integer.parseInt(CRMB.getText().toString()));
                UserJson = new Gson().toJson(LoginUser);
                ADDRMBMultithreading(ProjectPath+"rqAppUserUpdate",UserJson);
            }
        });
    }

    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void ADDRMBMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                ADDRMBweb(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void ADDRMBweb(String url,String s){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", s)
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
            ShowFlowingSingle(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowFlowingSingle(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(str!=null){
                    Toast.makeText(getApplicationContext(), "充值成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "充值失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Back(View view) {
        finish();
    }

}