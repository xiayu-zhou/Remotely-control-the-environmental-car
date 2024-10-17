package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.zxy.pojo.ANotice;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminSendNotice extends AppCompatActivity {

    private EditText AdminXYNotice,AdminXYTitle;

    private Button AdminSendNoticeBtn;

    private String ProjectPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_notice);
        Init();
        ClickBtn();
    }

    /**
     * 初始化函数
     */
    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        AdminXYNotice = (EditText) findViewById(R.id.AdminXYNotice);
        AdminXYTitle = (EditText) findViewById(R.id.AdminXYTile);
        AdminSendNoticeBtn = (Button) findViewById(R.id.AdminSendNoticeBtn);
    }

    /**
     * 所有的按键的单击事件的监控
     */
    private void ClickBtn(){
        AdminSendNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ANotice notice = new ANotice();

                notice.setTitle(AdminXYTitle.getText().toString());

                notice.setContent(AdminXYNotice.getText().toString());

                NoticeUpdateMultithreading(ProjectPath+"AppUpdateNoticeServlet",
                        new Gson().toJson(notice));
            }
        });
    }

    /**
     * 返回按键
     * @param view
     */
    public void Back(View view) {
        finish();
    }


    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void NoticeUpdateMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                NoticeUpdate(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void NoticeUpdate(String url,String s){

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
            ShowFinish(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowFinish(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(str.equals("finish")){
                    Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}