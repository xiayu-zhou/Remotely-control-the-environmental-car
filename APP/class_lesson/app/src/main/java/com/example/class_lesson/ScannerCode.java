package com.example.class_lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.zxy.pojo.User;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScannerCode extends AppCompatActivity {


    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private BarcodeCallback callback ;

    private User user;

    private String ProjectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_code);
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";

        Intent intent = this.getIntent();

        user = new Gson().fromJson(intent.getStringExtra("UserJson"),User.class);
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        callback  = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String code = result.getText();
/*                System.out.println("12312312"+code);*/
                CodeMultithreading(ProjectPath+"CodeLoginServlet",code+"#"+user.getID()+"#"+user.getUserPassword());
                /*finish();*/
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                BarcodeCallback.super.possibleResultPoints(resultPoints);
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        barcodeScannerView.decodeSingle(callback); // 启动扫描
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeScannerView.pause(); // 停止扫描
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


/*------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void CodeMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                CodeWebRecord(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void CodeWebRecord(String url,String s){

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
            CodeWebRecordView(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CodeWebRecordView(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SingleLoad(str);
            }
        });
    }

    private void SingleLoad(String str) {
        if(str.equals("finish")){
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
/*        Toast.makeText(this, "返回"+str, Toast.LENGTH_SHORT).show();*/
}