package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.com.Computer;
import com.google.gson.Gson;
import com.zxy.pojo.Commodity;
import com.zxy.pojo.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Person extends AppCompatActivity {

    private EditText ID,Password,Name,Age,Sex,Phone,Occ,PersonS,Address,RMB;

    private Button StartSetting,FinishSetting;

    private User user;

    private String UserJson;
    private String ProjectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pserson);
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        Init();
        personSet();
        ButtonClick();
        UserShowMultithreading(ProjectPath+"AppShowUserServlet");
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        ID = (EditText) findViewById(R.id.ShowMeID);
        Password = (EditText) findViewById(R.id.ShowMePWD);
        Name = (EditText) findViewById(R.id.ShowMeName);
        Age = (EditText) findViewById(R.id.ShowMeAge);
        Sex = (EditText) findViewById(R.id.ShowMeSex);
        Phone = (EditText) findViewById(R.id.ShowMePhone);
        Occ = (EditText) findViewById(R.id.ShowMeOcc);
        PersonS = (EditText) findViewById(R.id.ShowMePS);
        Address = (EditText) findViewById(R.id.ShowMeAddress);
        RMB = (EditText) findViewById(R.id.ShowMeRMB);
        StartSetting = (Button) findViewById(R.id.StartSetting);
        FinishSetting = (Button) findViewById(R.id.FinishSetting);
    }

    /**
     * 设置用户的信息
     */
    private void personSet(){
        user = new Gson().fromJson(UserJson, User.class);
        ID.setText(user.getID());
        Password.setText(user.getUserPassword());
        Name.setText(user.getUserName());
        String age = String.valueOf(user.getAge());
        Age.setText(age);
        Sex.setText(user.getSex());
        Phone.setText(user.getPhone());
        Occ.setText(user.getOccupation());
        PersonS.setText(user.getPersonSign());
        Address.setText(user.getAddress());
        String rmb = String.valueOf(user.getRMB());
        RMB.setText(rmb);
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

                Occ.setFocusable(true);
                Occ.setFocusableInTouchMode(true);
                Occ.requestFocus();
                Occ.findFocus();

                PersonS.setFocusable(true);
                PersonS.setFocusableInTouchMode(true);
                PersonS.requestFocus();
                PersonS.findFocus();

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
                Occ.setFocusable(false);
                PersonS.setFocusable(false);
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
                .url(ProjectPath+"rqAppUserUpdate")
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
        if(ID.getText().toString().equals(user.getID())
                && Password.getText().toString().equals(user.getUserPassword())
                && Name.getText().toString().equals(user.getUserName())
                && Age.getText().toString().equals(String.valueOf(user.getAge()))
                && Sex.getText().toString().equals(user.getSex())
                && Phone.getText().toString().equals(user.getPhone())
                && Occ.getText().toString().equals(user.getOccupation())
                && PersonS.getText().toString().equals(user.getPersonSign())
                && Address.getText().toString().equals(user.getAddress())){

        }else{
            User rqUser = new User();
            rqUser.setID(ID.getText().toString());
            rqUser.setUserPassword(Password.getText().toString());
            rqUser.setUserName(Name.getText().toString());
            rqUser.setAge(Integer.parseInt(Age.getText().toString()));
            rqUser.setSex(Sex.getText().toString());
            rqUser.setPhone(Phone.getText().toString());
            rqUser.setOccupation(Occ.getText().toString());
            rqUser.setPersonSign(PersonS.getText().toString());
            rqUser.setAddress(Address.getText().toString());
            rqUser.setRMB(user.getRMB());
            String sendJson = new Gson().toJson(rqUser);

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
                .add("Json", UserJson)
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
            UserJson = responseData;
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