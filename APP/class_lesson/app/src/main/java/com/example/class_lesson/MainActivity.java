package com.example.class_lesson;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.com.Computer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.pojo.Administrator;
import com.zxy.pojo.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    private TextView mBtnLogin,mSignUp;

    private AnimatorSet set;

    private View progress;

    private View mInputLayout;

    private EditText Password,UserName;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private String Json;

    private String AdminJson;

    private String CommodityJson;

    private String IP;

    private RadioGroup LoginSelect;

    private RadioButton AdminLoginSelect,UserLoginSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        IP = new Computer().getIP();
        //开多线程
        new Thread(new Runnable() {
            public void run() {
                try {
                    OKHttpBackJson("http://"+IP+":8080/lesson_web/rqAppUserLogin");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //管理员的登录线程
        new Thread(new Runnable() {
            public void run() {
                try {
                    AdminOKHttpBackJson("http://"+IP+":8080/lesson_web/AppAdminLoginServlet");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //开多线程
        new Thread(new Runnable() {
            public void run() {
                try {
                    RequestCommodity("http://"+IP+":8080/lesson_web/APPCommodityServlet");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //调用控件的初始化方法
        Init();

        //开始监听界面的点击事件
        MyClickEvent();
    }

    private void MyClickEvent(){

        /**
         * 登录按键的单击事件
         */
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                inputAnimator(mInputLayout, mWidth, mHeight);
                System.out.println(Json);

                //登陆的用户还是管理员的选择
                int selectedId = LoginSelect.getCheckedRadioButtonId();
                if(selectedId == R.id.UserLoginSelect){
                    if (Json != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<User>>() {
                        }.getType();
                        List<User> users = gson.fromJson(Json, type);
                        System.out.println(users);
                        int i;
                        for (i = 0; i < users.size(); i++) {
                            if (users.get(i).getID().equals(UserName.getText().toString()) &&
                                    users.get(i).getUserPassword().equals(Password.getText().toString())) {
                                set.cancel();
                                recovery();
                                Intent intent = new Intent(MainActivity.this, index.class);

                                //让当前用户转变为Json数据
                                String userJson = new Gson().toJson(users.get(i));
                                //让当前用户的Json数据传输到下一个UI
                                intent.putExtra("UserJson", userJson);
                                intent.putExtra("CommodityJson", CommodityJson);
                                startActivity(intent);
                                break;
                            }
                        }
                        if (i >= users.size()) {
                            // 倒计时器 倒计时更新UI
                            new CountDownTimer(3000, 10000) { // 倒计时30秒，并且每秒执行一次onTick
                                public void onTick(long millisUntilFinished) {
                                    // 更新UI
                                }

                                public void onFinish() {
                                    // 完成倒计时后执行
                                    recovery();
                                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                        }

                    }else{
                        // 倒计时器 倒计时更新UI
                        new CountDownTimer(3000, 10000) { // 倒计时30秒，并且每秒执行一次onTick
                            public void onTick(long millisUntilFinished) {
                                // 更新UI
                            }

                            public void onFinish() {
                                // 完成倒计时后执行
                                recovery();
                                Toast.makeText(MainActivity.this, "服务器暂无开启", Toast.LENGTH_SHORT).show();
                            }
                        }.start();
                    }

                    //管理员的登录
                }else{
                    if (AdminJson != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Administrator>>() {
                        }.getType();
                        List<Administrator> administrators = gson.fromJson(AdminJson, type);
                        System.out.println(administrators);
                        int i;
                        for (i = 0; i < administrators.size(); i++) {
                            if (administrators.get(i).getID().equals(UserName.getText().toString()) &&
                                    administrators.get(i).getAdminPassword().equals(Password.getText().toString())) {
                                set.cancel();
                                recovery();
                                Intent intent = new Intent(MainActivity.this, index.class);

                                //让当前用户转变为Json数据
                                String LoginAdminJson = new Gson().toJson(administrators.get(i));
                                //让当前用户的Json数据传输到下一个UI
                                intent.putExtra("AdminJson", LoginAdminJson);
                                intent.putExtra("CommodityJson", CommodityJson);
                                startActivity(intent);
                                break;
                            }
                        }
                        if (i >= administrators.size()) {
                            // 倒计时器 倒计时更新UI
                            new CountDownTimer(3000, 10000) { // 倒计时30秒，并且每秒执行一次onTick
                                public void onTick(long millisUntilFinished) {
                                    // 更新UI
                                }

                                public void onFinish() {
                                    // 完成倒计时后执行
                                    recovery();
                                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                        }

                    }else{
                        // 倒计时器 倒计时更新UI
                        new CountDownTimer(3000, 10000) { // 倒计时30秒，并且每秒执行一次onTick
                            public void onTick(long millisUntilFinished) {
                                // 更新UI
                            }

                            public void onFinish() {
                                // 完成倒计时后执行
                                recovery();
                                Toast.makeText(MainActivity.this, "服务器暂无开启", Toast.LENGTH_SHORT).show();
                            }
                        }.start();
                    }
                }
            }
        });


        /**
         * 注册的登录按键
         */
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化函数
     */
    private void Init(){
        Password = (EditText)findViewById(R.id.Password);
        UserName = (EditText)findViewById(R.id.UserName);
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        mSignUp = (TextView) findViewById(R.id.main_btn_Sign);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        LoginSelect = (RadioGroup) findViewById(R.id.LoginSelect);
        AdminLoginSelect = (RadioButton) findViewById(R.id.AdminLoginSelect);
        UserLoginSelect = (RadioButton) findViewById(R.id.UserLoginSelect);
    }

    /**
     * 用户的登录的信息网络请求
     * @param url
     * @return
     * @throws IOException
     */
    public String OKHttpBackJson(String url) throws IOException{

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("Json", "KianaWxhn")
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
            Json = responseData;
            // ...
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Json;
    }

    /**
     * 管理员的信息网络请求
     * @param url
     * @return
     * @throws IOException
     */
    public String AdminOKHttpBackJson(String url) throws IOException{

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
            AdminJson = responseData;
            // ...
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AdminJson;
    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                mInputLayout.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }
    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
    /**
     * 恢复初始状态
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
    public String RequestCommodity(String url) throws IOException{

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
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommodityJson;
    }

    /**
     * 找回账号
     * @param view
     */
    public void FindOld(View view) {
        Intent intent = new Intent(this, FindID.class);
        startActivity(intent);
    }
}