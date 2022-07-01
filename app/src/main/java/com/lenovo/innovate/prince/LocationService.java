/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *//*


package com.lenovo.innovate.prince;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationService extends Service implements AMapLocationListener {
    private final String TAG = "LocationService";
    private AMapLocationClient aMapLocationClient = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocation();

        return super.onStartCommand(intent, flags, startId);
    }

    */
/**
     * 配置定位服务参数
     *
     * @param start true表示开启定位服务 false表示销毁定位服务
     *//*

    private void locationConfigure(boolean start) {
        if (start) {
            Log.i(TAG, "aMapLocation：开启定位服务");
            try {
                aMapLocationClient = new AMapLocationClient(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
            aMapLocationClient.setLocationListener(this);
            //初始化定位参数
            AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
            //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            aMapLocationClientOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            aMapLocationClientOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            aMapLocationClientOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            aMapLocationClientOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,半小时定位一次
            aMapLocationClientOption.setInterval(1000 * 60 * 30);
            //给定位客户端对象设置定位参数
            aMapLocationClient.setLocationOption(aMapLocationClientOption);
            //启动定位
            aMapLocationClient.startLocation();
        } else {
            if (aMapLocationClient != null) {
                aMapLocationClient.onDestroy();
                Log.i(TAG, "aMapLocation：关闭定位服务");
            }
        }
    }

    */
/**
     * 位置信息
     *
     * @param aMapLocation 具体位置信息对象
     *//*

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String device_addr = aMapLocation.getCountry()//国家信息
                        + aMapLocation.getProvince()//省份信息
                        + aMapLocation.getCity()//城市信息
                        + aMapLocation.getDistrict()//区信息
                        + aMapLocation.getStreet()//街道信息
                        + aMapLocation.getStreetNum();//门牌号信息
                Log.i(TAG, "设备定位成功，设备所在位置：" + device_addr);
            } else {
                Log.e(TAG, "设备定位失败-》》》》》》错信息-》》》》" + aMapLocation.getErrorCode() + ", 错误信息:" + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onDestroy() {
        locationConfigure(false);
        super.onDestroy();
    }
}
*/
