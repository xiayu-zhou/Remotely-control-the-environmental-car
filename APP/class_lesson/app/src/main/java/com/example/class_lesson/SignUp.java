package com.example.class_lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.com.Computer;
import com.google.gson.Gson;
import com.zxy.pojo.Administrator;
import com.zxy.pojo.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    Button Login,SignUp,RightNowLogin;

    EditText UserName,Password1,Password2,Phone;

    RadioButton AdminIDSelect,UserIDSelect;

    RadioGroup radioGroup;

    TextView ShowID;

    LinearLayout SignUpSuccess,AllInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //按键初始化
        Init();

        //按钮的监控 及点击事件的实现
        buttonClick();
    }

    /**
     * 初始化
     */
    private void Init(){
        SignUpSuccess = (LinearLayout) findViewById(R.id.SignUpSuccess);
        AllInfo = (LinearLayout) findViewById(R.id.AllInfo);
        ShowID = (TextView) findViewById(R.id.ShowID);
        Login = (Button) findViewById(R.id.BackLoginButton);
        SignUp = (Button) findViewById(R.id.SignUpButton);
        RightNowLogin = (Button) findViewById(R.id.RightNowLogin);
        UserName = (EditText) findViewById(R.id.UserNameEdit);
        Password1 = (EditText) findViewById(R.id.PassWordEdit);
        Password2 = (EditText) findViewById(R.id.PassWordAgainEdit);
        Phone = (EditText) findViewById(R.id.Phone);
        AdminIDSelect = (RadioButton) findViewById(R.id.AdminIDSelect);
        UserIDSelect = (RadioButton) findViewById(R.id.UserIDSelect);
        radioGroup = findViewById(R.id.radioGroup);
    }

    /**
     * 按键的监控与事件
     */
    private void buttonClick(){

        RightNowLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //释放当前页面资源，返回上一级
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //返回登录
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //注册
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.UserIDSelect){
                    if(UserName.getText().toString().equals("")
                            || Phone.getText().toString().equals("")
                            || Password1.getText().toString().equals("")
                            || Password2.getText().toString().equals(""))
                        Toast.makeText(SignUp.this, "还有未填写的信息", Toast.LENGTH_SHORT).show();
                    else if(Password1.getText().toString().equals(Password2.getText().toString())){
                        User user = new User();

                        //先设置用户的个性签名为无
                        user.setPersonSign("无");

                        //获取名字
                        user.setUserName(UserName.getText().toString());

                        //获取地址
                        user.setAddress("无");

                        //获取年龄
                        user.setAge(18);

                        //获取电话
                        user.setPhone(Phone.getText().toString());

                        //获取职业
                        user.setOccupation("无");

                        //获取密码
                        user.setUserPassword(Password1.getText().toString());

                        //处理单选框
                        user.setSex("女");

                        user.setRMB(0);

                        System.out.println(user);

                        //先让所有的隐藏起来
                        AllInfo.setVisibility(View.GONE);

                        //显示出系统分配的ID
                        SignUpSuccess.setVisibility(View.VISIBLE);

                        //将用户变为Json字符串
                        Gson gson = new Gson();
                        String json = gson.toJson(user);

                        //开多线程
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    rqWeb(json,"rqAppUserRegister");
                                } catch (IOException e) {
                                    reCover();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{
                        Toast.makeText(SignUp.this, "输入的两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                    //管理员的注册
                }else{

                    if(UserName.getText().toString().equals("")
                            || Phone.getText().toString().equals("")
                            || Password1.getText().toString().equals("")
                            || Password2.getText().toString().equals(""))
                        Toast.makeText(SignUp.this, "还有未填写的信息", Toast.LENGTH_SHORT).show();
                    else if(Password1.getText().toString().equals(Password2.getText().toString())){
                        Administrator admin = new Administrator();

                        //获取名字
                        admin.setAdminName(UserName.getText().toString());

                        //获取地址
                        admin.setAddress("无");

                        //获取年龄
                        admin.setAge(20);

                        //获取电话
                        admin.setPhone(Phone.getText().toString());

                        //获取职业
                        admin.setPosition("无");

                        //获取密码
                        admin.setAdminPassword(Password1.getText().toString());

                        //处理单选框
                        admin.setSex("男");

                        admin.setDepartment("无");


                        System.out.println(admin);

                        //先让所有的隐藏起来
                        AllInfo.setVisibility(View.GONE);

                        //显示出系统分配的ID
                        SignUpSuccess.setVisibility(View.VISIBLE);

                        //将用户变为Json字符串
                        Gson gson = new Gson();
                        String json = gson.toJson(admin);

                        //开多线程
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    rqWeb(json,"AppAdminSignUpServlet");
                                } catch (IOException e) {
                                    reCover();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{
                        Toast.makeText(SignUp.this, "输入的两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 安卓发送数据到Web端的函数
     * @param Json
     * @throws IOException
     */
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
            // ...
            System.out.println(responseData);
            showResponse(responseData,ShowID,true);
        } catch (IOException e) {
            showResponse("",ShowID,false);
            e.printStackTrace();
        }
    }

    /**
     * 不从主UI线程显示TextView
     * @param response
     * @param textView
     */
    private void showResponse(final String response, TextView textView,boolean tof) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tof == true)
                    textView.setText("注册成功\nID:"+response);
                else
                    textView.setText("服务器暂无响应");
            }
        });
    }

    private void reCover(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignUp.this, "注册失败", Toast.LENGTH_SHORT).show();
                AllInfo.setVisibility(View.VISIBLE);
                SignUpSuccess.setVisibility(View.GONE);
            }
        });
    }
}