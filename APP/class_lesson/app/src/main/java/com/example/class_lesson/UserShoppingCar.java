package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.pojo.CarDetail;
import com.zxy.pojo.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserShoppingCar extends AppCompatActivity {
    private Button ShoppingCarDelete,UserBuyCommodity;

    private String UserJson;

    private User user;

    private String ProjectPath,ShoppingCarJson;

    private LinearLayout ShowMessage;

    private List<CarDetail> shoppingCars,UserBuyShoppingCar;

    private LinearLayout[] CarsLayouts;

    private CheckBox GoodsAllSelect;

    private TextView ShoppingCarCount,ShoppingCarAllRMB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        user = new Gson().fromJson(UserJson,User.class);
        Init();
        ClickBtn();
        ShowRecord();
    }
    private void Init(){
        UserBuyShoppingCar = new ArrayList<CarDetail>();
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        ShowMessage = (LinearLayout) findViewById(R.id.MyCar);
        ShoppingCarCount = (TextView) findViewById(R.id.ShoppingCarCount);
        ShoppingCarDelete = (Button) findViewById(R.id.ShoppingCarDelete);
        UserBuyCommodity = (Button) findViewById(R.id.UserBuyCommodity);
        ShoppingCarAllRMB = (TextView) findViewById(R.id.ShoppingCarAllRMB);
        GoodsAllSelect = (CheckBox) findViewById(R.id.GoodsAllSelect);
    }

    private void ClickBtn(){

    }

    private void ShowRecord(){
        Multithreading(ProjectPath+"AppUserCarShowServlet");
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
            ShoppingCarJson = responseData;
            showResponse(ShoppingCarJson);
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showResponse(String ShJson) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequiteRecord(CarsLayouts,ShJson);
            }
        });
    }

    public void RequiteRecord(LinearLayout[] linearLayouts,String BuyJson) {
        //先将之前的控件都移除
        ShowMessage.removeAllViews();

        Type type = new TypeToken<List<CarDetail>>() {
        }.getType();

        List<CarDetail> carDetails = new Gson().fromJson(BuyJson, type);
        linearLayouts = new LinearLayout[carDetails.size()];
        //最外层样式
        LinearLayout.LayoutParams class_params =
                new LinearLayout.LayoutParams(1000, 300);
        class_params.setMargins(0, 40, 0, 0);

        //图片样式
        LinearLayout.LayoutParams image1 =
                new LinearLayout.LayoutParams(444, 300);

        //TextView长宽高
        LinearLayout.LayoutParams In_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        final int[] Count = {0};
        final double[] AllRMB = {0};

        CheckBox[] checkBox = new CheckBox[carDetails.size()];

        for (int i = 0; i < carDetails.size(); i++) {
            linearLayouts[i] = new LinearLayout(this);
            linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            linearLayouts[i].setLayoutParams(class_params);  //使参数生效
            linearLayouts[i].setBackgroundColor(getResources().getColor(R.color.white));

            checkBox[i] = new CheckBox(this);
            linearLayouts[i].addView(checkBox[i]);

            //图片框
            ImageView image = new ImageView(this);
            image.setLayoutParams(image1);
            String url = ProjectPath
                    + carDetails.get(i).getGPSPicG();
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
            In.setText(carDetails.get(i).getCname());
            inner.addView(In);

            TextView In0 = new TextView(this);
            //设置参数
            In0.setLayoutParams(In_params);
            In0.setGravity(Gravity.CENTER);
            In0.setTextColor(getResources().getColor(R.color.black));
            In0.setBackgroundColor(getResources().getColor(R.color.In));
            In0.setText("￥"+ String.valueOf(carDetails.get(i).getRmb()));
            inner.addView(In0);

            TextView In1 = new TextView(this);
            //设置参数
            In1.setLayoutParams(In_params);
            In1.setTextColor(Color.RED);
            In1.setBackgroundColor(getResources().getColor(R.color.In1));
            In1.setGravity(Gravity.CENTER);
            In1.setText("x"+carDetails.get(i).getCount()+"   ￥:" + (carDetails.get(i).getRmb()*carDetails.get(i).getCount()));
            inner.addView(In1);

            ShowMessage.addView(linearLayouts[i]);

            int k = i;
            checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked == true){
                        AllRMB[0] += (carDetails.get(k).getRmb()*carDetails.get(k).getCount());
                        Count[0] += carDetails.get(k).getCount();
                        UserBuyShoppingCar.add(carDetails.get(k));

                    }else{
                        AllRMB[0] -= (carDetails.get(k).getRmb()*carDetails.get(k).getCount());
                        Count[0] -= carDetails.get(k).getCount();
                        UserBuyShoppingCar.remove(carDetails.get(k));
                    }
                    ShoppingCarCount.setText(String.valueOf(Count[0]));
                    ShoppingCarAllRMB.setText(String.valueOf(AllRMB[0]));
                }
            });
        }
        //全部按钮的监听
        GoodsAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    for (int i = 0; i < checkBox.length; i++) {
                        checkBox[i].setChecked(true);
                    }
                else{
                    for (int i = 0; i < checkBox.length; i++) {
                        checkBox[i].setChecked(false);
                    }
                }
            }
        });

        /**
         * 购物车的删除
         */
        ShoppingCarDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //开多线程
                new Thread(new Runnable() {
                    public void run() {
                        try {

                            rqWeb(new Gson().toJson(UserBuyShoppingCar),"AppUserCarDeleteServlet");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        UserBuyCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开多线程
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            UserBuyCommodity(new Gson().toJson(UserBuyShoppingCar),"PCBuyCommodityServlet");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 返回函数的实现方法
     * @param view
     */
    public void Back(View view) {
        finish();
    }


    private void rqWeb(String Json,String work) throws IOException {

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", Json)
                .build();

        // 构建HTTP请求对象
        Request request = new Request.Builder()
                .url("http://"+new Computer().getIP() +":8080/lesson_web/"+work)
                .post(requestBody)
                .build();
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 处理响应数据
            String responseData = response.body().string();
            ShowRecord();
            UserBuyShoppingCar.clear();

            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户购买商品的的网络请求
     * @param Json
     * @param work
     * @throws IOException
     */
    private void UserBuyCommodity(String Json,String work) throws IOException {

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("BuyJson", Json)
                .build();

        // 构建HTTP请求对象
        Request request = new Request.Builder()
                .url("http://"+new Computer().getIP() +":8080/lesson_web/"+work)
                .post(requestBody)
                .build();
        // 发送HTTP请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 处理响应数据
            String responseData = response.body().string();
            ShowRecord();
            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}