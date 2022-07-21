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

import com.lenovo.innovate.hook.DumpMethodHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Phone_hook {

    private static final String filterStr = "隐私权限调用 包名：";
    public Phone_hook(XC_LoadPackage.LoadPackageParam lpparam){
        XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager",
                lpparam.classLoader,
                "getDeviceId",
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        XposedBridge.log(filterStr+lpparam.packageName + " ---获取imei");
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager",
                lpparam.classLoader,
                "getDeviceId",
                int.class,
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        XposedBridge.log(filterStr+lpparam.packageName + " ---获取imei");
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager",
                lpparam.classLoader,
                "getSubscriberId",
                int.class,
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        XposedBridge.log(filterStr+lpparam.packageName + " ---获取imsi");
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager",
                lpparam.classLoader,
                "getImei",
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        XposedBridge.log(filterStr+lpparam.packageName + " ---获取imei");
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager",
                lpparam.classLoader,
                "getImei",
                int.class,
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        XposedBridge.log(filterStr+lpparam.packageName + " ---获取imei");
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "android.os.SystemProperties",
                lpparam.classLoader,
                "get",
                String.class,
                String.class,
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        if (param.args[0].equals("ro.serialno")) {
                            XposedBridge.log(filterStr+lpparam.packageName + " ---获取序列号");
                        }
                    }
                }
        );


        XposedHelpers.findAndHookMethod(
                "android.content.ClipboardManager",
                lpparam.classLoader,
                "getPrimaryClip",
                new DumpMethodHook(lpparam.packageName) {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                        if (param.args[0].equals("ro.serialno")) {
                            XposedBridge.log(filterStr+lpparam.packageName + " ---获取剪贴板信息");
                        }
                    }
                }
        );

    }
}
