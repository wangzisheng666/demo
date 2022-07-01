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
 */

package com.lenovo.innovate;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.amap.api.location.AMapLocationClient;
import com.an.deviceinfo.device.model.App;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.utils.sdkinit.ANRWatchDogInit;
import com.lenovo.innovate.utils.sdkinit.UMengInit;
import com.lenovo.innovate.utils.sdkinit.XBasicLibInit;
import com.lenovo.innovate.utils.sdkinit.XUpdateInit;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    static MyApp instance;
    public AccessService service;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLibs();
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        // X系列基础库初始化
        XBasicLibInit.init(this);
        // 版本更新初始化
        XUpdateInit.init(this);
        // 运营统计数据
        UMengInit.init(this);
        // ANR监控
        ANRWatchDogInit.init();

        AMapLocationClient.updatePrivacyShow(getApplicationContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(), true);
    }

    public static MyApp getInstance() {
        return instance;
    }



    public void setService(AccessService accessService) {
        service = accessService;
    }

    public AccessService getService() {
        return service;
    }

    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }


}
