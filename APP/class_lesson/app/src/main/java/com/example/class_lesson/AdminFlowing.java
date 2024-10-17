package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.pojo.Commodity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminFlowing extends AppCompatActivity {

    private Spinner spinner;

    private TextView AllCount,AllRMB,FlowingName,FlowingDate,FlowingCount,FlowingRMB;

    private EditText AdminShowSousuoinput;

    private String ProjectPath;

    private Button AdminAlreadyBuyGoodsSouSuo;

    private ImageView FlowingPIC;

    private String Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flowing);
        Init();
        ClickBtn();
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        spinner = (Spinner) findViewById(R.id.spinner);
        AllCount = (TextView) findViewById(R.id.AllCount);
        AllRMB = (TextView) findViewById(R.id.AllRMB);
        FlowingDate = (TextView) findViewById(R.id.FlowingDate);
        FlowingName = (TextView) findViewById(R.id.FlowingName);
        FlowingCount = (TextView) findViewById(R.id.FlowingCount);
        FlowingRMB = (TextView) findViewById(R.id.FlowingRMB);
        AdminShowSousuoinput = (EditText) findViewById(R.id.AdminShowSousuoinput);
        AdminAlreadyBuyGoodsSouSuo = (Button) findViewById(R.id.AdminAlreadyBuyGoodsSouSuo);
        FlowingPIC = (ImageView) findViewById(R.id.FlowingPIC);
    }

    private void ClickBtn(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                if(selectedValue.equals("一周内")){
                    Flag = "1";
                    CommodityAllShowMultithreading(ProjectPath+"AppAdminFlowingServlet","1");
                }else if(selectedValue.equals("一月内")){
                    Flag = "2";
                    CommodityAllShowMultithreading(ProjectPath+"AppAdminFlowingServlet","2");
                }else if(selectedValue.equals("一年内")){
                    Flag = "3";
                    CommodityAllShowMultithreading(ProjectPath+"AppAdminFlowingServlet","3");
                } else{
                    Flag = "4";
                    CommodityAllShowMultithreading(ProjectPath+"AppAdminFlowingServlet","4");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AdminAlreadyBuyGoodsSouSuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommoditySingleShowMultithreading(ProjectPath+"AppAdminFlowingServlet",
                        AdminShowSousuoinput.getText().toString()+"#"+Flag);
            }
        });
    }

    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void CommodityAllShowMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                GoodsWebRecord(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void GoodsWebRecord(String url,String s){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Kiana", s)
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
            ShowFlowingAll(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowFlowingAll(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AllLoad(str);
            }
        });
    }

    private void AllLoad(String str){
        String s[] = str.split("#");
        AllCount.setText("商品销量："+s[0]);
        AllRMB.setText("商品流水："+s[1]);
    }

    /*----------------------------------------------------------------------*/

    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void CommoditySingleShowMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                SingleGoodsWebRecord(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void SingleGoodsWebRecord(String url,String s){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Kiana", s)
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
                SingleLoad(str);
            }
        });
    }

    private void SingleLoad(String str){
        String s[] = str.split("#");
        String url = ProjectPath
                +s[0];
        Glide.with(this)
                .load(url)
                .into(FlowingPIC);

        FlowingName.setText(s[1]);

        FlowingDate.setText(s[2]);

        FlowingCount.setText(s[3]);

        FlowingRMB.setText(s[4]);
    }

    public void Back(View view) {
        finish();
    }
}