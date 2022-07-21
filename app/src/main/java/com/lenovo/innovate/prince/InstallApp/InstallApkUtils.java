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

package com.lenovo.innovate.prince.InstallApp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InstallApkUtils {
    public static Context mContext = null;
    public static String sdPath = "/sdcard/install.apk";//下载sd路径

    //判断是否update目录下有文件
    public static boolean isHasFile() {
        try {
            File f = new File(sdPath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static void excuteSuCMD(String currenttempfilepath) {
        if (isHasFile()) {
            Process process = null;
            OutputStream out = null;
            InputStream in = null;
            try {
                //请求root
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                //调用安装
                out.write(("pm install -r " + currenttempfilepath + "\n").getBytes());
                in = process.getInputStream();
                int len = 0;
                byte[] bs = new byte[256];
                while (-1 != (len = in.read(bs))) {
                    String state = new String(bs, 0, len);
                    if (state.equals("success\n")) {
                        //安装成功后的操作
                        //静态注册自启动广播
                        Intent intent = new Intent();
                        //与清单文件的receiver的anction对应
                        intent.setAction("android.intent.action.PACKAGE_REPLACED");
                        //发送广播
                        mContext.sendBroadcast(intent);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("install", "apk is not exist");
        }
    }
}