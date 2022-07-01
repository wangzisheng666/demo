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


import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lenovo.innovate.MyApp;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.prince.utils.ServiceUtils;

import java.util.List;

public  class AutoWeChat {

    private static final String TAG = "AutoWeChat";
    public int sleep = 1000;
    public AccessService service = MyApp.getInstance().getService();

    public void start(){

        while (true)
        {
            AccessibilityNodeInfo   accessibilityNodeInfo = ServiceUtils.findViewById(service,"com.tencent.mm:id/f1n");
            if(!AccUtils.isEmptyView(accessibilityNodeInfo)) {
                ServiceUtils.clickView(accessibilityNodeInfo);
                sleep(1000);
                Log.i(TAG,"点击微信");
                break;

            }else {
                Log.i(TAG,"f1n");
            }
            sleep(100);
        }


            List<AccessibilityNodeInfo> findViewClassName = ServiceUtils.findViewByIdList(service, "com.tencent.mm:id/hga");
            for (AccessibilityNodeInfo NodeInfo : findViewClassName) {
                if (NodeInfo.getText() != null) {
                    String aa = NodeInfo.getText().toString();
                    Log.i(TAG, "textView：" + aa);
                    if (aa.contains("Prince")) {
                        ServiceUtils.clickView(NodeInfo);
                    }
                }
            }


        /*List<AccessibilityNodeInfo> prince = ServiceUtils.findViewByEqualsText(service,"Prince");
        if(!Utils.isEmptyArray(prince)) {
            Log.i(TAG,"点击prince");
            ServiceUtils.clickView( prince.get(0));
            sleep(1000);
            List<AccessibilityNodeInfo> prince1 = ServiceUtils.findViewByEqualsText(service,"发消息");
            if(!Utils.isEmptyArray(prince1)) {
                Log.i(TAG,"点击发消息");
                ServiceUtils.clickView( prince1.get(0));
            }
        }*/

        while (true)
        {
            AccessibilityNodeInfo   accessibilityNodeInfo = ServiceUtils.findViewById(service,"com.tencent.mm:id/b3q");
            if(!AccUtils.isEmptyView(accessibilityNodeInfo)) {
                ServiceUtils.clickView(accessibilityNodeInfo);
                sleep(1000);
                Log.i(TAG,"点击+");
                break;

            }else {
                Log.i(TAG,"b3q");
            }
            sleep(100);
        }


        List<AccessibilityNodeInfo> findViewPicture = ServiceUtils.findViewByIdList(service,"com.tencent.mm:id/vg");
        for (int i = 0; i <findViewPicture.size() ; i++) {
            String text =  findViewPicture.get(i).getText().toString();
            if(text.equals("转账")){
                ServiceUtils.clickView(findViewPicture.get(i));
                sleep(1000);
                AccUtils.execRootCmdSilent("input tap " + 417 + " " + 1928);//执行linux命令点击屏幕某个点
                break;
            }else {
                Log.i(TAG,"没有转账按钮");
            }
        }

        while (true) {
            AccessibilityNodeInfo accessibilityNode = ServiceUtils.findViewById(service, "com.tencent.mm:id/lgl");
            if (!AccUtils.isEmptyView(accessibilityNode)) {
                ServiceUtils.setText(accessibilityNode, "1");
                Log.i(TAG, "输入金额");
                break;
            }else {
                AccUtils.execRootCmdSilent("input tap " + 417 + " " + 1928);//执行linux命令点击屏幕某个点
            }
        }

        while (true) {
            AccessibilityNodeInfo accessibilityNode = ServiceUtils.findViewById(service, "com.tencent.mm:id/ffw");
            if (!AccUtils.isEmptyView(accessibilityNode)) {
                ServiceUtils.clickView(accessibilityNode);
                Log.i(TAG, "输入金额");
                break;
            }else {
                AccUtils.execRootCmdSilent("input tap " + 917 + " " + 2071);//执行linux命令点击屏幕某个点
            }
        }


/*
        //搜索框里面输入人名或群名称
        List<AccessibilityNodeInfo> findViewByClassName;
        int a = 0;
        do {
            List<AccessibilityNodeInfo> findView= ServiceUtils.findViewByContainsText(service, "转账");
            if(!Utils.isEmptyArray(findView)) {
                Log.i(TAG,"点击转账");
               CharSequence aa = findView.get(0).getParent().getClassName();
                Log.i(TAG, String.valueOf(aa));
                ServiceUtils.clickView( findView.get(0).getParent());
                break;
            }
            sleep(500);
            a++;
        } while (a < 5);
*/


    }

    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    public static void sleep(int i)  {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }













}
