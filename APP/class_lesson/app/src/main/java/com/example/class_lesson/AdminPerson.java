package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.com.Computer;
import com.google.gson.Gson;
import com.zxy.pojo.Administrator;
import com.zxy.pojo.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminPerson extends AppCompatActivity {

    private EditText ID,Password,Name,Age,Sex,Phone,Dar,Position,Address;

    private Button StartSetting,FinishSetting;

    private Administrator Admin;

    private String AdminJson;
    private String ProjectPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_person);
        Intent intent = this.getIntent();
        AdminJson = intent.getStringExtra("AdminJson");
        Init();
        ButtonClick();
        UserShowMultithreading(ProjectPath+"AppAdminShowServlet");
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        ID = (EditText) findViewById(R.id.AdminShowMeID);
        Password = (EditText) findViewById(R.id.AdminShowMePWD);
        Name = (EditText) findViewById(R.id.AdminShowMeName);
        Age = (EditText) findViewById(R.id.AdminShowMeAge);
        Sex = (EditText) findViewById(R.id.AdminShowMeSex);
        Phone = (EditText) findViewById(R.id.AdminShowMePhone);
        Dar = (EditText) findViewById(R.id.AdminShowMeDp);
        Position = (EditText) findViewById(R.id.AdminShowMePosition);
        Address = (EditText) findViewById(R.id.AdminShowMeAddress);
        StartSetting = (Button) findViewById(R.id.AdminStartSetting);
        FinishSetting = (Button) findViewById(R.id.AdminFinishSetting);
    }

    /**
     * 设置管理员的信息
     */
    private void personSet(){
        Admin = new Gson().fromJson(AdminJson, Administrator.class);
        ID.setText(Admin.getID());
        Password.setText(Admin.getAdminPassword());
        Name.setText(Admin.getAdminName());
        String age = String.valueOf(Admin.getAge());
        Age.setText(age);
        Sex.setText(Admin.getSex());
        Phone.setText(Admin.getPhone());
        Dar.setText(Admin.getDepartment());
        Position.setText(Admin.getPosition());
        Address.setText(Admin.getAddress());
    }

    private void ButtonClick(){
        StartSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                ID.setFocusable(true);
                ID.setFocusableInTouchMode(true);
                ID.requestFocus();
                ID.findFocus();*/

                Password.setFocusable(true);
                Password.setFocusableInTouchMode(true);
                Password.requestFocus();
                Password.findFocus();

                Name.setFocusable(true);
                Name.setFocusableInTouchMode(true);
                Name.requestFocus();
                Name.findFocus();

                Age.setFocusable(true);
                Age.setFocusableInTouchMode(true);
                Age.requestFocus();
                Age.findFocus();

                Sex.setFocusable(true);
                Sex.setFocusableInTouchMode(true);
                Sex.requestFocus();
                Sex.findFocus();

                Phone.setFocusable(true);
                Phone.setFocusableInTouchMode(true);
                Phone.requestFocus();
                Phone.findFocus();

                Dar.setFocusable(true);
                Dar.setFocusableInTouchMode(true);
                Dar.requestFocus();
                Dar.findFocus();

                Position.setFocusable(true);
                Position.setFocusableInTouchMode(true);
                Position.requestFocus();
                Position.findFocus();

                Address.setFocusable(true);
                Address.setFocusableInTouchMode(true);
                Address.requestFocus();
                Address.findFocus();
            }
        });

        FinishSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID.setFocusable(false);
                Password.setFocusable(false);
                Name.setFocusable(false);
                Age.setFocusable(false);
                Sex.setFocusable(false);
                Phone.setFocusable(false);
                Dar.setFocusable(false);
                Position.setFocusable(false);
                Address.setFocusable(false);
                rqWebUpdate();
            }
        });
    }

    private void rqWeb(String Json) throws IOException {

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", Json)
                .build();

        // 构建HTTP请求对象
        Request request = new Request.Builder()
                .url(ProjectPath+"AppAdminUpdateServlet")
                .post(requestBody)
                .build();
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // 处理响应数据
            String responseData = response.body().string();
            // ...
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rqWebUpdate(){
        if(ID.getText().toString().equals(Admin.getID())
                && Password.getText().toString().equals(Admin.getAdminPassword())
                && Name.getText().toString().equals(Admin.getAdminName())
                && Age.getText().toString().equals(String.valueOf(Admin.getAge()))
                && Sex.getText().toString().equals(Admin.getSex())
                && Phone.getText().toString().equals(Admin.getPhone())
                && Position.getText().toString().equals(Admin.getPosition())
                && Dar.getText().toString().equals(Admin.getDepartment())
                && Address.getText().toString().equals(Admin.getAddress())){

        }else{
            Administrator rqAdmin = new Administrator();
            rqAdmin.setID(ID.getText().toString());
            rqAdmin.setAdminPassword(Password.getText().toString());
            rqAdmin.setAdminName(Name.getText().toString());
            rqAdmin.setAge(Integer.parseInt(Age.getText().toString()));
            rqAdmin.setSex(Sex.getText().toString());
            rqAdmin.setPhone(Phone.getText().toString());
            rqAdmin.setPosition(Position.getText().toString());
            rqAdmin.setDepartment(Dar.getText().toString());
            rqAdmin.setAddress(Address.getText().toString());
            String sendJson = new Gson().toJson(rqAdmin);

            //开多线程
            new Thread(new Runnable() {
                public void run() {
                    try {
                        rqWeb(sendJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.println(sendJson);
        }
    }
    private void ShowUserMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                personSet();
            }
        });
    }
    private void RqWeb(String url){
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", AdminJson)
                .build();

        // 构建HTTP请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String responseData= "";
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 处理响应数据
            responseData = response.body().string();
            AdminJson = responseData;
            ShowUserMessage();
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void UserShowMultithreading(String url){
        new Thread(new Runnable() {
            public void run() {
                RqWeb(url);
            }
        }).start();
    }
}