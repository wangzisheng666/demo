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

package com.lenovo.innovate.hook.function;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WatchDogService extends Service {

    private List<String> packageList = new ArrayList<>();
    private static final String TAG = "WatchDogService";
    private static boolean flag = true;// 线程退出的标记
    private ActivityManager am;
    private SharedPreferences isOpenSP;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        // 服务一旦启动要在后台监视任务栈最顶端应用
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        isOpenSP = getSharedPreferences("isOpen", Context.MODE_PRIVATE);
        editor = isOpenSP.edit();

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (flag) {
                    synchronized (WatchDogService.class) {
                        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                        ComponentName topActivity = runningTaskInfo.topActivity;
                        String packageName = topActivity.getPackageName();

                        if (packageList.contains(packageName)) {
                            // 判断packageName是否已打开
                            boolean isOpen = isOpenSP.getBoolean(packageName, false);
                            if (!isOpen) {
                                editor.putBoolean(packageName, true);
                                editor.apply();
                                Log.i(TAG, packageName + "正在存储");
                            }
                        }
                        SystemClock.sleep(500);
                    }
                    Log.i(TAG, "服务在循环");
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    public class MyBinder extends Binder implements IMyBinder {

        @Override
        public void setPackageNames(List<String> packageNames) {
            packageList.clear();
            packageList.addAll(packageNames);
        }
    }

    MyBinder myBinder = new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
}
