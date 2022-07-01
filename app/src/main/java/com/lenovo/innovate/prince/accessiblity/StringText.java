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


import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lenovo.innovate.MyApp;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class StringText {

    private static final String TAG = "StringText";
    public AccessService service = MyApp.getInstance().getService();
    List<String> list=new ArrayList<>();

    public void start(){
        sleep(2000);

        int  a = 0;
        String text = null;
        while (a<100) {
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

            Log.i(TAG,list.toString());
            sleep(1000);
            a++;
        }

        Log.i(TAG,list.toString());
    }

    private void sleep(int i) {
        try {
            Thread.sleep( i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
