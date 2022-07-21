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

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Message_hook {
    private static final String filterStr = "隐私权限调用 包名：";
    public Message_hook(XC_LoadPackage.LoadPackageParam lpparam){
        findAndHookMethod("com.android.internal.telephony.gsm.SmsMessage$PduParser", lpparam.classLoader, "getUserDataUCS2", int.class, new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(filterStr+lpparam.packageName + "------------获取短信");

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
                try {

                    //新增其他操作
                } catch (Exception e) {
                    XposedBridge.log(e);
                }
            }
        });
    }
    }

