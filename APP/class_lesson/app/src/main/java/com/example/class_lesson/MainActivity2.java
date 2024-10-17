package com.example.class_lesson;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.com.BlueTooth;
import com.com.Permission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    LinearLayout DataShow,FunctionLe;
    TextView wendu,shidu,rentihongwai,juli,TestView;

    static int RFlag = 5,LFlag = 5;

    static int AllPre = 0,AllBackFlag = 0,LeftPre = 0,LeftBack = 0,RightPre = 0,RightBack = 0,DataUpdateFlag = 0,OpenLightFlag = 0,
            CloseLightFlag = 0,OpenWarningFlag = 0,CloseWarningFlag = 0,TanLeftFlag = 0,TanRightFlag = 0,TanUpFlag = 0,TanDownFlag = 0,TanRestFlag = 0;

    SeekBar LSeekBar,RSeekBar;

    //***********************************************************************************************
    BluetoothDevice clickDevice;

    static int BlueToothFindFlag = 0;

    static int findBluFlag = 0;

    //********************************************
    private Toast mToast;
    //创建广播接收者的对象
    MyReceiver myReceiver = new MyReceiver();
    //获取蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //////////////////////////////////////////////创建搜索蓝牙列表的///////////////////////////////////////////////////////////////////
    public ArrayAdapter arrayAdapter;  //这个适配器列表是用来加载到列表的数据
    public ArrayList<BluetoothDevice> deviceAdress = new ArrayList<>();  //存放蓝牙设备（这里Adress我忘了改过来了，这是存放设备不是设备地址）
    public ArrayList<String> deviceName = new ArrayList<>();  //存放蓝牙名称和地址
    public ListView listView;   //定义展示列表
    //手机连接的UUID
    //设备连接的UUID由厂商决定。
    private final String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //蓝牙通信的UUID，必须为这个，如果换成其他的UUID会无法通信  串口通信UUID

    //蓝牙socket通信
    private BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream;

    InputStream inputStream;

    EditText data;

    Permission permission = new Permission();
    BlueTooth blueTooth = new BlueTooth();
    //**********************************************************************************************************************************
    Button back,AllAdvance,AllBack,LAllAdvance,LAllBack,RAllAdvance,RAllBack,DataUpdate,OpenLight,CloseLight,OpenWarning,CloseWarning,
            TanLeft,TanRight,TanUp,TanDown,TanRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land);
        Init();
        permission.checkPermissions(this);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, deviceName);  //实例化ArrayAdapter对象deviceName集合数据放入arrayAdapter适配器集合内
        listView = (ListView) findViewById(R.id.device_list);  //获取列表框的
        listView.setAdapter(arrayAdapter);  //将arrayAdapter集合内的数据加载到列表框 就是适配器对象与ListView关联
        back = (Button)findViewById(R.id.Back);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //上面是添加动作事件
        //过滤器
        intentFilter.addAction("com.mingrisoft");
        //注册广播接收者的对象
        registerReceiver(myReceiver, intentFilter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "为您返回上一级", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //全部前进
        AllAdvance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(AllPre == 0){
                    AllPre++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@00*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@22*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    AllPre--;
                    System.out.println("按键松开咯");
                }
                return false;
            }
        });

        //全部后退
        AllBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(AllBackFlag == 0){
                    AllBackFlag++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@11*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@22*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    AllBackFlag--;
                    System.out.println("按键松开咯");
                }

                return false;
            }
        });

        //左边前进
        LAllAdvance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(LeftPre == 0){
                    LeftPre++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@$0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@$2*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    LeftPre--;
                    System.out.println("按键松开咯");
                }
                return false;
            }
        });

        //左边向后退
        LAllBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(LeftBack == 0){
                    LeftBack++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@$1*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@$2*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    LeftBack--;
                    System.out.println("按键松开咯");
                }
                return false;
            }
        });

        //右边前进
        RAllAdvance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(RightPre == 0){
                    RightPre++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@0$*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@2$*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    RightPre--;
                    System.out.println("按键松开咯");
                }
                return false;
            }
        });

        //右边后退
        RAllBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(RightBack == 0){
                    RightBack++;
                    System.out.println("按键按下咯");
                    try {
                        String data = "@1$*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@2$*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                    }
                    RightBack--;
                    System.out.println("按键松开咯");
                }
                return false;
            }
        });

        RSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(((progress/10) - RFlag) >= 1 || ((progress/10) - RFlag) <= -1){
                    RFlag = progress/10;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + progress / 10) + "*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("滑动条开始滑动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RFlag =  5;
                seekBar.setProgress(50);
                try {
                    String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "*";
                    try {
                        outputStream = bluetoothSocket.getOutputStream();
                        outputStream.write(data.getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
                System.out.println("滑动条停止滑动");
            }
        });

        //左边滑动条
        LSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(((progress/10) - LFlag) >= 1 || ((progress/10) - LFlag) <= -1){
                    LFlag = progress/10;
                    try {
                        String data = "@!!" + (char) (65 + progress / 10)+ (char) (65 + RSeekBar.getProgress() / 10)  + "*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    System.out.println("ASCII码值为：" + (char) (65 + RSeekBar.getProgress() / 10) +RSeekBar.getProgress()+" | "+
                            "   " + (char) (65 + progress / 10) + "   " + progress);
                    System.out.println("滑动条发生改变");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("滑动条开始滑动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LFlag =  5;
                seekBar.setProgress(50);
                try {
                    String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "*";
                    try {
                        outputStream = bluetoothSocket.getOutputStream();
                        outputStream.write(data.getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
                System.out.println("滑动条停止滑动");
            }
        });

        //温湿度数据刷新
        DataUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(DataUpdateFlag == 0){
                    DataUpdateFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "+*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    DataUpdateFlag--;
                }
                return false;
            }
        });

        //开灯
        OpenLight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(OpenLightFlag == 0){
                    OpenLightFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "5*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    OpenLightFlag--;
                }
                return false;
            }
        });

        //关灯
        CloseLight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(CloseLightFlag == 0){
                    CloseLightFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "6*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    CloseLightFlag--;
                }
                return false;
            }
        });

        //打开警报灯
        OpenWarning.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(OpenWarningFlag == 0){
                    OpenWarningFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "7*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    OpenWarningFlag--;
                }
                return false;
            }
        });

        //关闭警报灯
        CloseWarning.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(CloseWarningFlag == 0){
                    CloseWarningFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "8*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    CloseWarningFlag--;
                }
                return false;
            }
        });

        //探头左移
        TanLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TanLeftFlag == 0){
                    TanLeftFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "3*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    TanLeftFlag--;
                }
                return false;
            }
        });
        //探头右移
        TanRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TanRightFlag == 0){
                    TanRightFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "4*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    TanRightFlag--;
                }
                return false;
            }
        });
        //探头抬头
        TanUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TanUpFlag == 0){
                    TanUpFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "2*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    TanUpFlag--;
                }
                return false;
            }
        });

        //探头低头
        TanDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TanDownFlag == 0){
                    TanDownFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "1*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    TanDownFlag--;
                }
                return false;
            }
        });

        //探头置位
        TanRest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TanRestFlag == 0){
                    TanRestFlag++;
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "9*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "0*";
                        try {
                            outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                    TanRestFlag--;
                }
                return false;
            }
        });

    }


    public void Init(){
        TanRest = (Button)findViewById(R.id.TanReset);
        TanUp = (Button)findViewById(R.id.TanUp);
        TanDown = (Button)findViewById(R.id.TanDown);
        TanLeft = (Button)findViewById(R.id.TanLeft);
        TanRight = (Button)findViewById(R.id.TanRight);
        CloseWarning = (Button)findViewById(R.id.CloseWarning);
        OpenWarning = (Button)findViewById(R.id.OpenWarning);
        DataUpdate = (Button)findViewById(R.id.DataUpdate);
        OpenLight = (Button)findViewById(R.id.OpenLight);
        CloseLight = (Button)findViewById(R.id.CloseLight);
        TestView = (TextView)findViewById(R.id.TestView);
        FunctionLe = (LinearLayout)findViewById(R.id.FunctionLe) ;
        DataShow = (LinearLayout)findViewById(R.id.DHDataShow);
        wendu = (TextView)findViewById(R.id.wendu);
        rentihongwai = (TextView)findViewById(R.id.rntihonwgai);
        shidu = (TextView)findViewById(R.id.shidu);
        juli = (TextView)findViewById(R.id.juli);
        RSeekBar = (SeekBar) findViewById(R.id.SeekBar2);
        LSeekBar = (SeekBar) findViewById(R.id.SeekBar1);
        AllAdvance = (Button)findViewById(R.id.AllAdvance);
        LAllAdvance = (Button)findViewById(R.id.LAllAdvance);
        RAllAdvance = (Button)findViewById(R.id.RAllAdvance);
        AllBack = (Button)findViewById(R.id.AllBack);
        LAllBack = (Button)findViewById(R.id.LAllBack);
        RAllBack = (Button) findViewById(R.id.RAllBack);
    }
//按键事件*************************************************************************

    public class ReceiveDataThread extends Thread{

        boolean isRunning = true;

        private InputStream inputStream;

        public ReceiveDataThread(BluetoothSocket bluetoothSocket) {
            super();
            try {
                //获取连接socket的输入流
                inputStream = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            super.run();
            int len = 0;
            byte[] buffer = new byte[256];
            while (isRunning){
                try {
                    int num = inputStream.read(buffer);
                    //设置GBK格式可以获取到中文信息，不会乱码
                    String a = new String(buffer,0,num,"UTF-8");//为什么-3 看文章最后注意部分

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //将收到的数据显示在TextView上
                            //kianaView.setText(a);

                            TestView.append(a);
                            System.out.println(a);
                            //kianaView.setText(a);
                            String str = TestView.getText().toString();
                            System.out.println("jiehsou:"+str);
                            int star = 0,stop = 0;
                            int flag = 0;
                            for (int i = 0; i < str.length(); i++) {
                                if(str.charAt(i) == '@'){
                                    flag++;
                                    star = i;
                                }

                                if(flag != 0 && str.charAt(i) == '*'){
                                    flag = 0;
                                    stop = i;

                                    String s = str.substring(star+1,stop);
                                    System.out.println(stop-star-1);
                                    String[] ch = s.split("#");
                                    System.out.println(ch[0]);
                                    System.out.println(ch[1]);
                                    wendu.setText("温度："+ch[0]+"°c");
                                    shidu.setText("湿度："+ch[1]+"%RH");
                                    juli.setText("距离："+ch[2]+"cm");
                                    TestView.setText("");


                                    break;
                                }
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void shuax(View view) {
        deviceAdress.clear();         //
        deviceName.clear();           //
        startDiscovery();             //
    }


    public void findBlu(View view) {
        if (findBluFlag == 0) {
            if (blueTooth.getBlueToothStatus() == true) {
                blueTooth.canFindBlueTooth(this, 0);
                startDiscovery();
                if (BlueToothFindFlag == 0) {
                    BlueToothFindFlag++;
                    findBluFlag++;
                }
            } else {
                Toast.makeText(this, "蓝牙尚未打开", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "不允许操作，请点击刷新", Toast.LENGTH_SHORT).show();
        }
    }


    static int OpenOrCloseFlag = 0;
    /**
     * 连接页面关闭或开启
     * @param view
     */
    public void OorC(View view) {
        if(listView.getVisibility() == View.VISIBLE){
            listView.setVisibility(View.GONE);
        }else{
            OpenOrCloseFlag = 0;
            DataShow.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
    public void ShowData(View view) {
        if(DataShow.getVisibility() == View.GONE){
            listView.setVisibility(View.GONE);
            FunctionLe.setVisibility(View.GONE);
            DataShow.setVisibility(View.VISIBLE);
        }else{
            DataShow.setVisibility(View.GONE);
        }
    }

    /**
     * 温湿度和一堆传感器的数据刷新按钮点击事件
     * @param view
     */
/*    public void DataUpdate(View view) {
        try {
            String data = "@!!" + (char) (65 + LSeekBar.getProgress() / 10) + (char) (65 + RSeekBar.getProgress() / 10) + "+*";
            try {
                outputStream = bluetoothSocket.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }*/

    /**
     * 功能按键的开启与关闭
     * @param view
     */
    public void Function(View view) {
        if(FunctionLe.getVisibility() == View.GONE){
            listView.setVisibility(View.GONE);
            DataShow.setVisibility(View.GONE);
            FunctionLe.setVisibility(View.VISIBLE);
        }else{
            FunctionLe.setVisibility(View.GONE);
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Toast.makeText(context, "开始", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //////////////////////////////////////////////创建搜索蓝牙列表的///////////////////////////////////////////////////////////////////
                for (int i = 0; i < deviceAdress.size(); i++) {
                    if (deviceAdress.get(i).getAddress().equals(device.getAddress())) return;
                    //上面if语句就是去除已经获取的蓝牙设备
                }
                // 不是重复的就添加到列表中(获取未配对的蓝牙设备)
                deviceAdress.add(device);  //添加地址到列表中   用于鉴别是否已经添加列表和点击事件用的
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                deviceName.add("地址：" + device.getAddress() + "\n" + "名称：" + device.getName());  //存放蓝牙名称和地址用于显示到列表上的
                arrayAdapter.notifyDataSetChanged();  //更新列表
                Connect_BT(deviceAdress);
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            }
        }
    }

    public void startDiscovery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Toast.makeText(this, "已经刷新", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "搜索器没打开", Toast.LENGTH_SHORT).show();
        }
        mBluetoothAdapter.startDiscovery();
    }

    //连接蓝牙
    public void Connect_BT(ArrayList<BluetoothDevice> deviceAdress) {
        //MainActivity 实现OnItemClickListener 然后重写方法
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final BluetoothDevice[] romoteDevice = new BluetoothDevice[1];
                if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                clickDevice = (BluetoothDevice) deviceAdress.get(position);
                String s1 = String.valueOf(position);  //编号
                Toast.makeText(MainActivity2.this, s1 + "--" + clickDevice.getName() + "--" + clickDevice.getAddress(), Toast.LENGTH_SHORT).show();
                //在连接前需要先关闭搜索
                //点击列表，去请求服务器
                if (clickDevice != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                bluetoothSocket = clickDevice.createRfcommSocketToServiceRecord(UUID.fromString(BLUETOOTH_UUID));
                                bluetoothSocket.connect();

                                /*
                                 * 接收线程必须要在这里启动
                                 * 不然没有对象
                                 * */
                                ReceiveDataThread thread = new ReceiveDataThread(bluetoothSocket);
                                thread.start();
                                Looper.prepare();
                                Toast.makeText(MainActivity2.this, "已连接", Toast.LENGTH_SHORT).show();
                                Looper.loop();

                            } catch (IOException e) {
                                Looper.prepare();
                                Toast.makeText(MainActivity2.this, "连接失败", Toast.LENGTH_SHORT).show();                                Looper.loop();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}