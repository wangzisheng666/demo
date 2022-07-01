package com.lenovo.innovate.hook;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 5:23 PM
 */
public class MainHook implements IXposedHookLoadPackage {
    Application mApplication;

    //记录Activity的总个数
    private int actCount = 0;

    private boolean front = false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        hookApplication();
    }


    private void hookApplication() {
        try {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    mApplication = (Application) param.thisObject;
                    if ((mApplication.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                        return;
                    }
                    String packageResourcePath = mApplication.getPackageResourcePath();
                    boolean isDataApp = packageResourcePath.startsWith("/data/app/");
                    if (!isDataApp) {
                        return;
                    }
                    if ("com.oasisfeng.greenify".equals(mApplication.getPackageName())
                            || mApplication.getPackageName().contains("xposed")
                            || mApplication.getPackageName().contains("magisk")
                            || mApplication.getPackageName().startsWith("com.google")
                            || mApplication.getPackageName().startsWith("com.android")
                    ) {
                        return;
                    }
                    hookApi();
                }
            });
        } catch (Exception e) {
            XposedBridge.log(mApplication.getPackageName() + "--bindServiceException");
            XposedBridge.log(e);
        }
    }




    private void hookApi() {
        XposedBridge.hookAllMethods(Fragment.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (mApplication != null) {
                    XposedBridge.log(mApplication.getPackageName() + ".oncreate : " + param.thisObject.getClass().getName());
                }
            }
        });
        //TelephonyManager
        XposedBridge.hookAllMethods(TelephonyManager.class, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getLine1Number", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSimSerialNumber", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
        //https://blog.csdn.net/qq_43278826/article/details/95216504
        XposedBridge.hookAllMethods(TelephonyManager.class, "getImei", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });

        //sdcard
        XposedBridge.hookAllMethods(Environment.class, "getExternalStorageDirectory", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
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
                    XposedBridge.log("calendar");
                } else if (arg.contains("phone")) {
                    XposedBridge.log("phone");
                } else if (arg.contains("call")) {
                    XposedBridge.log("call");
                } else if (arg.contains("sms")) {
                    XposedBridge.log("sms");
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
                    XposedBridge.log("calendar");
                } else if (arg.contains("phone")) {
                    XposedBridge.log("phone");
                } else if (arg.contains("call")) {
                    XposedBridge.log("call");
                } else if (arg.contains("sms")) {
                    XposedBridge.log("sms");
                }
            }
        });

        //LocationManager
        XposedBridge.hookAllMethods(LocationManager.class, "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });

        //AccountManager
        XposedBridge.hookAllMethods(AccountManager.class, "addAccountExplicitly", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });

        //Camera
        XposedBridge.hookAllMethods(Camera.class, "open", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
        XposedBridge.hookAllMethods(Camera.class, "getNumberOfCameras", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log(param.method.getName());
            }
        });
    }

}