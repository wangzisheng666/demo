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

package com.lenovo.innovate.prince.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Sdcard {

    private static final String TAG ="Sdcard" ;
    private String mResult = new String();

    public static String getSDPath() {

        File sdDir = null;

        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在

        if (sdCardExist) {

            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录

        }

        return sdDir.toString();

    }


    public   String dir(String dir){
        File flist = new File(dir);
        FileFilter ff = new FileFilter(){

            public boolean accept(File pathname) {

                return pathname.isDirectory();

            }

        };

        File[] fileDir = flist.listFiles(ff);

        for (int i = 0; i < fileDir.length; i++) {

            String str = fileDir[i].getName();

            mResult += str;

            mResult += "\n";

        }
        Log.i(TAG,mResult.toString());
        return mResult;
    }

}
