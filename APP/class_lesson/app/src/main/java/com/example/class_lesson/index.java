package com.example.class_lesson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.com.Computer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zxy.pojo.ANotice;
import com.zxy.pojo.Administrator;
import com.zxy.pojo.AlreadyBuy;
import com.zxy.pojo.Commodity;
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

public class index extends AppCompatActivity {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton tab1,tab2,tab3;  //四个单选按钮
    private List<View> mViews;   //存放视图

    private User user;

    private Administrator Admin;

    private List<Commodity> goods;

    private String UserJson,AdminJson;

    private String CommodityJson;

    /*------------------------------------------------主页商品---------------------------------------------------------*/
    private ImageView Hot1,Hot2,Hot3,NoticeBack,MeBack,UserShaoMa;
    private TextView Hot1t,Hot2t,Hot3t,XYTile,XYNotice;

    private String ProjectPath,AlreadyBuyJson;
    //父LinearLayout
    private LinearLayout classLL,ShowMessage;
    private LinearLayout[] layouts,buysLayouts;
    private Computer computer;

    private List<AlreadyBuy> Buys;

    private Button ToPerson,IndexAlreadyBuy,MeToShoppingCar,UserMyDEV,AdminGoods,AdminFlowing,AdminNotice,IndexFindBtn,UserCong;

    private LinearLayout User1,User2,User3,User4,Admin1,Admin2,Admin3,JIA,CHUAN,CHU,QI;

    private EditText IndexFind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();//初始化数据
        Init();
        //获取登陆界面的json数据
        Intent intent = this.getIntent();
        UserJson = intent.getStringExtra("UserJson");
        AdminJson = intent.getStringExtra("AdminJson");
        CommodityJson = intent.getStringExtra("CommodityJson");
        if(UserJson!=null){
            user = new Gson().fromJson(UserJson,User.class);
            UserShow();
        }
        else if(AdminJson!=null){
            Admin = new Gson().fromJson(AdminJson,Administrator.class);
            AdminShow();
        }


        Type type = new TypeToken<List<Commodity>>() {
        }.getType();
        goods = new Gson().fromJson(CommodityJson, type);
        //初始化
        //me
        BtnClick();
        HotCommodity();
        CommodityShowMultithreading(ProjectPath+"APPCommodityServlet");
        //对单选按钮进行监听，选中、未选中

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_find:
                    {
                        CommodityShowMultithreading(ProjectPath+"APPCommodityServlet");
                        mViewPager.setCurrentItem(0);
                        break;
                    }
                    case R.id.rb_msg:
                    {
                        NoticeMultithreading(ProjectPath+"AppShowNoticeServlet","Kiana");
                        mViewPager.setCurrentItem(1);
                        break;
                    }
                    case R.id.rb_me:
                    {
     /*                   UserShowMultithreading(ProjectPath+"AppShowUserServlet");*/
                        mViewPager.setCurrentItem(2);
                        break;
                    }
                }
            }
        });

    }

    private void initView() {
        //初始化控件
        mViewPager=findViewById(R.id.viewpager);
        mRadioGroup=findViewById(R.id.rg_tab);
        tab1=findViewById(R.id.rb_find);
        tab2=findViewById(R.id.rb_msg);
        tab3=findViewById(R.id.rb_me);

        mViews=new ArrayList<View>();//加载，添加视图
        mViews.add(LayoutInflater.from(this).inflate(R.layout.find,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.msg,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.me,null));


        mViewPager.setAdapter(new MyViewPagerAdapter());//设置一个适配器
        //对viewpager监听，让分页和底部图标保持一起滑动
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override   //让viewpager滑动的时候，下面的图标跟着变动
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        CommodityShowMultithreading(ProjectPath+"APPCommodityServlet");
                        tab1.setChecked(true);
                        tab2.setChecked(false);
                        tab3.setChecked(false);
                        break;
                    case 1:
                        NoticeMultithreading(ProjectPath+"AppShowNoticeServlet","Kiana");
                        tab1.setChecked(false);
                        tab2.setChecked(true);
                        tab3.setChecked(false);
                        break;
                    case 2:
/*                        UserShowMultithreading(ProjectPath+"AppShowUserServlet");*/
                        tab1.setChecked(false);
                        tab2.setChecked(false);
                        tab3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //ViewPager适配器
    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount()  {
            return mViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }

    private void Init(){
        computer = new Computer();
        classLL = (LinearLayout) mViews.get(0).findViewById(R.id.CommodityShow);
        ProjectPath = "http://"+computer.getIP()+":8080/lesson_web/";
        Hot1 = (ImageView)mViews.get(0).findViewById(R.id.Hot1);
        Hot2 = (ImageView) mViews.get(0).findViewById(R.id.Hot2);
        Hot3 = (ImageView) mViews.get(0).findViewById(R.id.Hot3);
        Hot1t = (TextView) mViews.get(0).findViewById(R.id.Hot1t);
        Hot2t = (TextView) mViews.get(0).findViewById(R.id.Hot2t);
        Hot3t = (TextView) mViews.get(0).findViewById(R.id.Hot3t);
        IndexFindBtn = (Button) mViews.get(0).findViewById(R.id.IndexFindBtn);
        IndexFind = (EditText) mViews.get(0).findViewById(R.id.IndexFind);
        JIA = (LinearLayout) mViews.get(0).findViewById(R.id.JIA);
        CHUAN = (LinearLayout) mViews.get(0).findViewById(R.id.CHUAN);
        CHU = (LinearLayout) mViews.get(0).findViewById(R.id.CHU);
        QI = (LinearLayout) mViews.get(0).findViewById(R.id.QI);



        XYTile = (TextView) mViews.get(1).findViewById(R.id.XYTileMsg);
        XYNotice = (TextView) mViews.get(1).findViewById(R.id.XYNoticeMsg);
        NoticeBack = (ImageView) mViews.get(1).findViewById(R.id.NoticeBack);


        ToPerson = (Button) mViews.get(2).findViewById(R.id.ToPerson);
        IndexAlreadyBuy = (Button) mViews.get(2).findViewById(R.id.IndexAlreadyBuy);
        MeToShoppingCar = (Button) mViews.get(2).findViewById(R.id.MeToShoppingCar);
        MeToShoppingCar = (Button) mViews.get(2).findViewById(R.id.MeToShoppingCar);
        UserMyDEV = (Button) mViews.get(2).findViewById(R.id.UserMYDEV);
        AdminGoods = (Button) mViews.get(2).findViewById(R.id.AdminGoods);
        AdminFlowing = (Button) mViews.get(2).findViewById(R.id.AdminFlowing);
        AdminNotice = (Button) mViews.get(2).findViewById(R.id.AdminNoticeSend);
        UserCong = (Button) mViews.get(2).findViewById(R.id.UserCong);

        User1 = (LinearLayout) mViews.get(2).findViewById(R.id.User1);
        User2 = (LinearLayout) mViews.get(2).findViewById(R.id.User2);
        User3 = (LinearLayout) mViews.get(2).findViewById(R.id.User3);
        User4 = (LinearLayout) mViews.get(2).findViewById(R.id.User4);

        Admin1 = (LinearLayout) mViews.get(2).findViewById(R.id.Admin1);
        Admin2 = (LinearLayout) mViews.get(2).findViewById(R.id.Admin2);
        Admin3 = (LinearLayout) mViews.get(2).findViewById(R.id.Admin3);
        MeBack = (ImageView) mViews.get(2).findViewById(R.id.MeBack);
        UserShaoMa = (ImageView) mViews.get(2).findViewById(R.id.UserShaoMa);
    }

    /**
     * 各个按钮的单击事件
     */
    public void BtnClick(){

        ToPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    Intent intent = new Intent(index.this, Person.class);
                    intent.putExtra("UserJson",UserJson);
                    startActivity(intent);
                }else if(Admin!=null){
                    Intent intent = new Intent(index.this, AdminPerson.class);
                    intent.putExtra("AdminJson",AdminJson);
                    startActivity(intent);
                }

            }
        });

        IndexAlreadyBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, AlreadyBuyCommodity.class);
                intent.putExtra("UserJson",UserJson);
                startActivity(intent);
            }
        });

        MeToShoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, UserShoppingCar.class);
                intent.putExtra("UserJson",UserJson);
                startActivity(intent);
            }
        });

        UserMyDEV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        AdminGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, CommoditySH.class);
                startActivity(intent);
            }
        });

        AdminFlowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, AdminFlowing.class);
                startActivity(intent);
            }
        });

        AdminNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, AdminSendNotice.class);
                startActivity(intent);
            }
        });
        NoticeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        JIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateText(layouts,CommodityJson,"智能家居");
            }
        });
        CHUAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateText(layouts,CommodityJson,"智能穿戴");
            }
        });
        CHU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateText(layouts,CommodityJson,"智能出行");
            }
        });
        QI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateText(layouts,CommodityJson,"其他");
            }
        });

        IndexFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateText(layouts,CommodityJson,IndexFind.getText().toString());
            }
        });
        UserCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserJson!=null){
                    Intent intent = new Intent(index.this, AddRMB.class);
                    intent.putExtra("UserJson",UserJson);
                    startActivity(intent);
                }
            }
        });

        UserShaoMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, ScannerCode.class);
                intent.putExtra("UserJson",UserJson);
                startActivity(intent);
            }
        });
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null && scanResult.getContents() != null) {
                String result = scanResult.getContents();
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    /**
     * 热门商品的加载
     */
    public void HotCommodity(){
        int k = 3;
        if(goods.size()<3)
            k = goods.size();
        for (int i = 0; i < k; i++) {
            System.out.println(goods.get(i).getCName());
            if(i == 0){
                Hot1t.setText(goods.get(i).getCName());
                String url = ProjectPath
                        +goods.get(i).getGPSPicG();

                Glide.with(this)
                        .load(url)
                        .into(Hot1);

                System.out.println(Hot1t.getText());
            }else if (i == 1){
                Hot2t.setText(goods.get(i).getCName());
                String url = ProjectPath
                        +goods.get(i).getGPSPicG();

                Glide.with(this)
                        .load(url)
                        .into(Hot2);
                System.out.println(Hot2t.getText());
            }else if (i == 2){
                Hot3t.setText(goods.get(i).getCName());
                String url = ProjectPath
                        +goods.get(i).getGPSPicG();

                Glide.with(this)
                        .load(url)
                        .into(Hot3);
                System.out.println(Hot3t.getText());
            }
        }
    }

    /**
     * 动态控件的加载
     * @param linearLayouts
     * @param Json
     */
    @SuppressLint("ResourceAsColor")
    public void CreateText(LinearLayout[] linearLayouts,String Json,String name){

        classLL.removeAllViews();

        Type type = new TypeToken<List<Commodity>>() {
        }.getType();
        goods = new Gson().fromJson(Json, type);

        linearLayouts = new LinearLayout[goods.size()];

        //最外层样式
        LinearLayout.LayoutParams class_params =
                new LinearLayout.LayoutParams(1000,300);
        class_params.setMargins(0, 40, 0, 0);

        //图片样式
        LinearLayout.LayoutParams image1 =
                new LinearLayout.LayoutParams(444,300);

        for (int i = 0; i < goods.size(); i++) {
            if(goods.get(i).getCName().contains(name)||name.contains(goods.get(i).getCName())
                    ||name.equals("")||name.equals(goods.get(i).getType())){
                linearLayouts[i] = new LinearLayout(index.this);
                linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
                linearLayouts[i].setLayoutParams(class_params);  //使参数生效

                //图片框
                ImageView image = new ImageView(index.this);
                image.setLayoutParams(image1);
                String url = ProjectPath
                        +goods.get(i).getGPSPicG();
                Glide.with(this)
                        .load(url)
                        .into(image);
                linearLayouts[i].addView(image); //把TextView加入new_class


                LinearLayout inner = new LinearLayout(index.this);
                LinearLayout.LayoutParams inner_params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,300);
                inner.setLayoutParams(inner_params);
                inner.setOrientation(LinearLayout.VERTICAL);
                linearLayouts[i].addView(inner); //把TextView加入new_class


                TextView In = new TextView(index.this);
                //设置参数
                LinearLayout.LayoutParams In_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                In.setLayoutParams(In_params);
                In.setGravity(Gravity.CENTER);
                In.setTextColor(getResources().getColor(R.color.black));
                In.setBackgroundColor(getResources().getColor(R.color.In));
                In.setText(goods.get(i).getCName());
                inner.addView(In);

                TextView In1 = new TextView(index.this);
                //设置参数
                LinearLayout.LayoutParams In1_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                In1.setLayoutParams(In1_params);
                In1.setTextColor(Color.RED);
                In1.setBackgroundColor(getResources().getColor(R.color.In1));
                In1.setGravity(Gravity.CENTER);
                In1.setText("￥:"+goods.get(i).getRMB());
                inner.addView(In1);

                classLL.addView(linearLayouts[i]); //把new_class加入classLL

                int finalI = i;//调用循环里面的i
                linearLayouts[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String SendGoodsJson = new Gson().toJson(goods.get(finalI));
                        Intent intent = new Intent(index.this, detail.class);
                        intent.putExtra("SendGoodsJson",SendGoodsJson);
                        if(UserJson == null){
                            intent.putExtra("UserJson","NULL");
                        }else{
                            intent.putExtra("UserJson",UserJson);
                        }
                        startActivity(intent);
                    }
                });
            }
        }
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
                .add("Json", "KianaWan")
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
            CommodityJson = responseData;
            ShowCommodity(responseData);
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CommodityShowMultithreading(String url){
        new Thread(new Runnable() {
            public void run() {
                GoodsWebRecord(url);
            }
        }).start();
    }

    private void ShowCommodity(String GJson) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreateText(layouts,GJson,"");
            }
        });
    }

    /**
     * 用户Me的显示
     */
    private void UserShow(){
        User1.setVisibility(View.VISIBLE);
        User2.setVisibility(View.VISIBLE);
        User3.setVisibility(View.VISIBLE);
        User4.setVisibility(View.VISIBLE);

        Admin1.setVisibility(View.GONE);
        Admin2.setVisibility(View.GONE);
        Admin3.setVisibility(View.GONE);
    }

    /**
     * 管理员的Me的展示
     */
    private void AdminShow(){
        User1.setVisibility(View.GONE);
        User2.setVisibility(View.GONE);
        User3.setVisibility(View.GONE);
        User4.setVisibility(View.GONE);

        Admin1.setVisibility(View.VISIBLE);
        Admin2.setVisibility(View.VISIBLE);
        Admin3.setVisibility(View.VISIBLE);
    }
    /*-----------------------------------------------------------------------*/


    /**
     * 开启网络请求的多线程
     * @param url
     */
    private void NoticeMultithreading(String url,String s){
        new Thread(new Runnable() {
            public void run() {
                NoticeWebRecord(url,s);
            }
        }).start();
    }

    /**
     * 向网络请求数据
     * @param url
     */
    private void NoticeWebRecord(String url,String s){

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
            ShowNotice(responseData.toString());
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowNotice(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SingleLoad(str);
            }
        });
    }

    private void SingleLoad(String str){
        ANotice notice = new Gson().fromJson(str,ANotice.class);
        XYTile.setText(notice.getTitle().toString());
        XYNotice.setText(notice.getContent().toString());
    }



    private void RqUserWeb(String url){
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
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void UserShowMultithreading(String url){
        new Thread(new Runnable() {
            public void run() {
                RqUserWeb(url);
            }
        }).start();
    }
}
