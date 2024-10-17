package com.com;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙适配器
 */
public class BlueTooth {

    private BluetoothAdapter mAdapet;

    /**
     * 蓝牙的初始化
     */
    public BlueTooth() {
        mAdapet = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 是否支持蓝牙
     * @return
     */
    public boolean isSupportBluTooth() {
        if (mAdapet != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前蓝牙状态
     * true打开   false关闭
     */
    public boolean getBlueToothStatus() {
        assert (mAdapet != null);
        return mAdapet.isEnabled();
    }

    /**
     * 开启蓝牙
     * @param activity
     * @param req
     */
    public void turnOnBlueTooth(Activity activity, int req) {
        //获取蓝牙
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivityForResult(intent, req);
    }

    /**
     * 蓝牙开启并且可以被扫描  使蓝牙可见
     * @param activity
     * @param req
     */
    public void canFindBlueTooth(Activity activity, int req) {
        //获取蓝牙
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //设置蓝牙300秒内可见
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(intent);
    }

    /**
     * 获取绑定设备
     * @param activity
     * @return
     */
    public List<BluetoothDevice> getBondedDeviceList(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            List<BluetoothDevice> TODO = new ArrayList<>();
            return TODO;
        }
        return new ArrayList<>(mAdapet.getBondedDevices());
    }

    /**
     * 查找设备
     * @param activity
     */
    public void findDevice(Activity activity) {
        assert (mAdapet != null);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mAdapet.startDiscovery();
    }
    public BluetoothAdapter getAdapter() {
        return mAdapet;
    }
}
