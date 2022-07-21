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

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Browser_hook {
    private static final String filterStr = "隐私权限调用 包名：";
    public Browser_hook(XC_LoadPackage.LoadPackageParam lpparam){

        final Class<?> clazz = XposedHelpers.findClass("android.provider.BrowserContract", lpparam.classLoader);
        //Hook无参构造函数，啥也不干。。。。
        XposedHelpers.findAndHookConstructor(clazz, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(filterStr+lpparam.packageName + " 获取浏览器内容");

            }
        });
    }
}
