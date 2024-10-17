package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.pojo.AlreadyBuy;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlreadyBuyCommodity extends AppCompatActivity {
    private Button find;

    private String UserJson;

    private String ProjectPath,AlreadyBuyJson;

    private LinearLayout ShowMessage;

    private List<AlreadyBuy> Buys;

    private LinearLayout[] buysLayouts;

    private EditText UserShowSousuoinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_buy_commodity);
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        Init();
        ClickBtn();
        ShowRecord();
    }

    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        ShowMessage = (LinearLayout) findViewById(R.id.record);
        find = (Button) findViewById(R.id.AlreadyBuyGoodsSouSuo);
        UserShowSousuoinput = (EditText) findViewById(R.id.UserShowSousuoinput);
    }

    private void ClickBtn(){

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequiteRecord(buysLayouts,AlreadyBuyJson,UserShowSousuoinput.getText().toString());
            }
        });
    }

    private void ShowRecord(){
        Multithreading(ProjectPath+"AppAlreadyBuyServlet");
    }
    private void Multithreading(String url){
        new Thread(new Runnable() {
            public void run() {
                WebRecord(url);
            }
        }).start();
    }

    private void WebRecord(String url){
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
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 处理响应数据
            String responseData = response.body().string();
            AlreadyBuyJson = responseData;
            showResponse(AlreadyBuyJson);
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showResponse(String ShJson) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequiteRecord(buysLayouts,ShJson,"");
            }
        });
    }

    public void RequiteRecord(LinearLayout[] linearLayouts,String BuyJson,String find) {

        //先将之前的控件都移除
        ShowMessage.removeAllViews();

        Type type = new TypeToken<List<AlreadyBuy>>() {
        }.getType();
        List<AlreadyBuy> alreadyBuys = new Gson().fromJson(BuyJson, type);
        linearLayouts = new LinearLayout[alreadyBuys.size()];
        //最外层样式
        LinearLayout.LayoutParams class_params =
                new LinearLayout.LayoutParams(1000, 300);
        class_params.setMargins(0, 40, 0, 0);

        //图片样式
        LinearLayout.LayoutParams image1 =
                new LinearLayout.LayoutParams(444, 300);

        //TextView长宽高
        LinearLayout.LayoutParams In_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);

        for (int i = 0; i < alreadyBuys.size(); i++) {
            if(alreadyBuys.get(i).getGoodsname().contains(find)||find.contains(alreadyBuys.get(i).getGoodsname())
                ||alreadyBuys.get(i).getBuyDate().contains(find)||find.contains(alreadyBuys.get(i).getBuyDate())
                    ||find.equals("")){
                linearLayouts[i] = new LinearLayout(this);
                linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
                linearLayouts[i].setLayoutParams(class_params);  //使参数生效

                //图片框
                ImageView image = new ImageView(this);
                image.setLayoutParams(image1);
                String url = ProjectPath
                        + alreadyBuys.get(i).getPic();
                Glide.with(this)
                        .load(url)
                        .into(image);
                linearLayouts[i].addView(image); //把TextView加入new_class


                LinearLayout inner = new LinearLayout(this);
                LinearLayout.LayoutParams inner_params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
                inner.setLayoutParams(inner_params);
                inner.setOrientation(LinearLayout.VERTICAL);
                linearLayouts[i].addView(inner); //把TextView加入new_class


                TextView In = new TextView(this);
                //设置参数

                In.setLayoutParams(In_params);
                In.setGravity(Gravity.CENTER);
                In.setTextColor(getResources().getColor(R.color.black));
                In.setBackgroundColor(getResources().getColor(R.color.In));
                In.setText(alreadyBuys.get(i).getGoodsname());
                inner.addView(In);

                TextView In0 = new TextView(this);
                //设置参数
                In0.setLayoutParams(In_params);
                In0.setGravity(Gravity.CENTER);
                In0.setTextColor(getResources().getColor(R.color.black));
                In0.setBackgroundColor(getResources().getColor(R.color.In));
                In0.setText(alreadyBuys.get(i).getBuyDate());
                inner.addView(In0);

                TextView In1 = new TextView(this);
                //设置参数
                In1.setLayoutParams(In_params);
                In1.setTextColor(Color.RED);
                In1.setBackgroundColor(getResources().getColor(R.color.In1));
                In1.setGravity(Gravity.CENTER);
                In1.setText("x"+alreadyBuys.get(i).getCount()+"   ￥:" + alreadyBuys.get(i).getExpense());
                inner.addView(In1);

                ShowMessage.addView(linearLayouts[i]); //把new_class加入classLL
            }
        }
    }

    /**
     * 返回方法
     */
    public void UserShowGoodsBack(View view) {
        finish();
    }
}