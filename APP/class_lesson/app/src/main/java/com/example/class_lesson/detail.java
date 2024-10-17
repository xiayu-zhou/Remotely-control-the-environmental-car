package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class detail extends AppCompatActivity {

    private ImageView MainPic,BackImage;
    private TextView Introduce,RMB,GoodsName,ShYu,Parameter;

    private EditText BuyCount;

    private String UserJson,GoodsJson,ProjectPath;

    private Commodity goods;
    private Button Finish;

    private User user;

    private Computer computer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        GoodsJson = intent.getStringExtra("SendGoodsJson");
        goods = new Gson().fromJson(GoodsJson,Commodity.class);
        if(!UserJson.equals("NULL"))
            user = new Gson().fromJson(UserJson,User.class);
        Init();
        setGoods();

        //按键的实现
        ButtonClick();
    }

    private void Init(){
        computer = new Computer();
        ProjectPath = "http://"+computer.getIP()+":8080/lesson_web/";
        Introduce = (TextView) findViewById(R.id.tit_desc_xiangqing);
        Finish = (Button) findViewById(R.id.add_cars);
        Parameter = (TextView) findViewById(R.id.cuxiao_icon);
        ShYu = (TextView) findViewById(R.id.ling_icons);
        RMB = (TextView) findViewById(R.id.price_xiangqing);
        GoodsName = (TextView) findViewById(R.id.bai_text);
        BackImage = (ImageView) findViewById(R.id.back_icons);
        MainPic = (ImageView) findViewById(R.id.shop_icons_xiangqing);
        BuyCount = (EditText) findViewById(R.id.CommodityCount);
    }

    private void setGoods(){
        Introduce.setText(goods.getIntroduce());
        Parameter.setText(goods.getParameter());
        ShYu.setText(String.valueOf(goods.getCount()));
        RMB.setText("￥ "+goods.getRMB());
        GoodsName.setText(goods.getCName());

        String url = ProjectPath
                +goods.getGPSPicG();

        Glide.with(this)
                .load(url)
                .into(MainPic);
    }

    /**
     * 返回按钮点击
     */
    private void ButtonClick(){
        BackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserJson.equals("NULL")){
                    UserJson = new Gson().toJson(user);
                    GoodsJson = new Gson().toJson(goods);
                    //开多线程
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                GoodsSubmit(ProjectPath+"AppShoppingCarServlet",UserJson,GoodsJson,BuyCount.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(detail.this, "管理员不可购买", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void GoodsSubmit(String url,String UserJson,String GoodsJson,String Count) throws IOException {

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("UserJson", UserJson)
                .add("GoodsJson",GoodsJson)
                .add("BuyCount",Count)
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
            if(responseData.equals("finish")){
                Looper.prepare();
                Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();

                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(this, "服务器暂无响应", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }
}