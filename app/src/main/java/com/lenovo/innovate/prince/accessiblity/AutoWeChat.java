package com.lenovo.innovate.prince.accessiblity;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.an.deviceinfo.device.model.App;
import com.lenovo.innovate.MyApp;
import com.lenovo.innovate.prince.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

public  class AutoWeChat {

    private static final String TAG = "AutoWeChat";
    public int sleep = 1000;
    public AccessService service = MyApp.getInstance().getService();

    public void start(){

        while (true)
        {
            AccessibilityNodeInfo   accessibilityNodeInfo = ServiceUtils.findViewById(service,"com.tencent.mm:id/f1n");
            if(!Utils.isEmptyView(accessibilityNodeInfo)) {
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
            if(!Utils.isEmptyView(accessibilityNodeInfo)) {
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
                Utils.execRootCmdSilent("input tap " + 417 + " " + 1928);//执行linux命令点击屏幕某个点
                break;
            }else {
                Log.i(TAG,"没有转账按钮");
            }
        }


        List<AccessibilityNodeInfo> findView = ServiceUtils.findViewByIdList(service,"com.tencent.mm:id/lgl");
        for (int i = 0; i <findView.size() ; i++) {
            String text =  findView.get(i).getText().toString();
            if(text.equals("转账")){
                ServiceUtils.clickView(findView.get(i));
                sleep(1000);
                Utils.execRootCmdSilent("input tap " + 417 + " " + 1928);//执行linux命令点击屏幕某个点
                break;
            }else {
                Log.i(TAG,"没有转账按钮");
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
