package com.lenovo.innovate.hook;

import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.graphics.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.innovate.hook.function.Browser_hook;
import com.lenovo.innovate.hook.function.Calender_hook;
import com.lenovo.innovate.hook.function.Contact_hook;
import com.lenovo.innovate.hook.function.Images_hook;
import com.lenovo.innovate.hook.function.Install_hook;
import com.lenovo.innovate.hook.function.Internet_hook;
import com.lenovo.innovate.hook.function.Location_hook;
import com.lenovo.innovate.hook.function.Message_hook;
import com.lenovo.innovate.hook.function.Phone_hook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class HookTrack implements IXposedHookLoadPackage {
    private static final String TAG = "HookTrack";

    private static final String filterStr = "隐私权限调用--权限：";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (lpparam == null) {
            return;
        }
        if ( lpparam.packageName.contains("xposed")
                ||lpparam.packageName.contains("magisk")
                || lpparam.packageName.startsWith("com.google")
                || lpparam.packageName.startsWith("com.android")
                || lpparam.packageName.startsWith("com.motorola")
                || lpparam.packageName.equals("android")
        ) {
            return;
        }
      /*  new Install_hook(lpparam);
        new Phone_hook(lpparam);
        new Internet_hook(lpparam);
        new Location_hook(lpparam);
        new Contact_hook(lpparam);
        new Images_hook(lpparam);
        new Browser_hook(lpparam);
        new Calender_hook(lpparam);
        new Message_hook(lpparam);*/
        hookApi(lpparam);
    }

    private void hookApi(XC_LoadPackage.LoadPackageParam lpparam) {

        //TelephonyManager
        XposedBridge.hookAllMethods(TelephonyManager.class, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","手机信息");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&手机信息");
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getLine1Number", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","手机信息");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&手机信息");
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSimSerialNumber", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","手机信息");
                map.put("package",lpparam.packageName);
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&手机信息");
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","手机信息");
                map.put("package",lpparam.packageName);
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&手机信息");
            }
        });
        //https://blog.csdn.net/qq_43278826/article/details/95216504
        XposedBridge.hookAllMethods(TelephonyManager.class, "getImei", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","手机信息");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&手机信息");
            }
        });

        //sdcard
        XposedBridge.hookAllMethods(Environment.class, "getExternalStorageDirectory", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","文件存储");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&文件读写");
            }
        });

        //calendar phone
        XposedBridge.hookAllMethods(ContentResolver.class, "query", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Uri argUri = (Uri) param.args[0];
                String arg = argUri.toString();
                if (arg.contains("calendar")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","日历");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&日历");
                } else if (arg.contains("phone")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","通讯录");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&通讯录");
                } else if (arg.contains("call")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","通话记录");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&通话记录");
                } else if (arg.contains("sms")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","短信");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&短信");
                }
            }
        });
        XposedBridge.hookAllMethods(ContentResolver.class, "insert", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Uri argUri = (Uri) param.args[0];
                String arg = argUri.toString();
                if (arg.contains("calendar")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","日历");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&日历");
                } else if (arg.contains("phone")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","通讯录");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&通讯录");
                } else if (arg.contains("call")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","通话记录");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&通话记录");
                } else if (arg.contains("sms")) {
                    Map<String,String> map = new HashMap<>();
                    map.put("time",get_time());
                    map.put("type","短信");
                    map.put("package",lpparam.packageName);
                    XposedBridge.log("<"+map+">");
                    writeTxt("#"+get_time()+"@"+lpparam.packageName+"&短信");
                }
            }
        });

        //LocationManager
        XposedBridge.hookAllMethods(LocationManager.class, "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","位置");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&位置");
            }
        });

        //AccountManager
        XposedBridge.hookAllMethods(AccountManager.class, "addAccountExplicitly", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","账户");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&账户");
            }
        });

        //Camera
        XposedBridge.hookAllMethods(Camera.class, "open", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","相机");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&相机");
            }
        });
        XposedBridge.hookAllMethods(Camera.class, "getNumberOfCameras", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Map<String,String> map = new HashMap<>();
                map.put("time",get_time());
                map.put("type","相机");
                map.put("package",lpparam.packageName);
                XposedBridge.log("<"+map+">");
                writeTxt("#"+get_time()+"@"+lpparam.packageName+"&相机");
            }
        });

    }

    public String get_time(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd HH:mm:ss");
          return dateFormat.format(new Date());


    }


    public static void writeTxt(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File("/storage/emulated/0/posed");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = dir + File.separator + "log";
            File f = new File(fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(fileName, true);
                fileOutputStream.write(json.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
