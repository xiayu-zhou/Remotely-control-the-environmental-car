package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class CommoditySH extends AppCompatActivity {

    private EditText AdminUpdateSou,GoodsUpdatePrice,GoodsUpdateCount;

    private Button AdminUpdateGoodsSou,GoodsUpdateFinish;

    private ImageView GoodsUpdateImg;

    private TextView GoodsUpdateName;

    private RadioGroup GoodsUpdateStatus;

    private RadioButton CommodityH,CommodityX;

    private List<Commodity> Goods;

    private String GoodsJson,ProjectPath;

    private String SendJson;

    private Commodity SendGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_sh);
        Init();
        CommodityShowMultithreading(ProjectPath+"APPCommodityServlet");
        //先初始化
        ClickBtn();
    }

    /**
     * 控件的初始化
     */
    private void Init(){
        ProjectPath = "http://"+new Computer().getIP()+":8080/lesson_web/";
        AdminUpdateSou = (EditText) findViewById(R.id.AdminUpdateSou);
        GoodsUpdateCount = (EditText) findViewById(R.id.GoodsUpdateCount);
        GoodsUpdatePrice = (EditText) findViewById(R.id.GoodsUpdatePrice);
        AdminUpdateGoodsSou = (Button) findViewById(R.id.AdminUpdateGoodsSou);
        GoodsUpdateFinish = (Button) findViewById(R.id.GoodsUpdateFinish);
        GoodsUpdateImg = (ImageView) findViewById(R.id.GoodsUpdateImg);
        GoodsUpdateName = (TextView) findViewById(R.id.GoodsUpdateName);
        GoodsUpdateStatus = (RadioGroup) findViewById(R.id.GoodsUpdateStatus);
        CommodityH = (RadioButton) findViewById(R.id.CommodityH);
        CommodityX = (RadioButton) findViewById(R.id.CommodityX);
    }

    /**
     * 按钮的监听事件
     */
    private void ClickBtn(){
        AdminUpdateGoodsSou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Goods!=null){
                    for (int i = 0; i < Goods.size(); i++) {
                        if(Goods.get(i).getID().equals(AdminUpdateSou.getText().toString())){
                            SendGoods = new Commodity(Goods.get(i));
                            ControlLoad(Goods.get(i));
                            break;
                        }
                    }
                }else{
                    Toast.makeText(CommoditySH.this, "暂无商品/未连接上服务器", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoodsUpdateFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SendGoods != null){
                    SendGoods.setCount(Integer.parseInt(GoodsUpdateCount.getText().toString()));
                    SendGoods.setRMB(Integer.parseInt(GoodsUpdatePrice.getText().toString()));
                    if(CommodityH.isChecked() == true)
                        SendGoods.setCStatus("1");
                    else {
                        SendGoods.setCStatus("0");
                    }
                    CommodityUpdateMultithreading(ProjectPath+"AppCommodityUpdateServlet",new Gson().toJson(SendGoods));
                }else{
                    Toast.makeText(CommoditySH.this, "没有选择商品", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 控件的加载
     * @param goods
     */
    private void ControlLoad(Commodity goods){
        String url = ProjectPath
                +goods.getGPSPicG();

        Glide.with(this)
                .load(url)
                .into(GoodsUpdateImg);
        GoodsUpdateCount.setText(String.valueOf(goods.getCount()));
        GoodsUpdatePrice.setText(String.valueOf(goods.getRMB()));
        GoodsUpdateName.setText(goods.getCName().toString());
        if(goods.getCStatus().equals("1")){
            CommodityH.setChecked(true);
        }else{
            CommodityX.setChecked(true);
        }
    }

    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void CommodityShowMultithreading(String url){
        new Thread(new Runnable() {
            public void run() {
                GoodsWebRecord(url);
            }
        }).start();
    }

    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void CommodityUpdateMultithreading(String url,String Json){
        new Thread(new Runnable() {
            public void run() {
                GoodsUpdate(url,Json);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void GoodsWebRecord(String url){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", "KianaAAA")
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
            GoodsJson = responseData;
            Type type = new TypeToken<List<Commodity>>() {
            }.getType();
            Goods = new Gson().fromJson(GoodsJson, type);
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求数据修改
     * @param url
     */
    private void GoodsUpdate(String url,String Json){

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", Json)
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
/*            GoodsJson = responseData;
            Type type = new TypeToken<List<Commodity>>() {
            }.getType();
            Goods = new Gson().fromJson(GoodsJson, type);*/
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回按钮
     * @param view
     */
    public void Back(View view) {
        finish();
    }
}