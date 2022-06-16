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


import android.content.ComponentName;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.lenovo.innovate.MyApp;


public class AccessService extends BaseAccessibilityService {

    private static final String TAG = "AccessService";

    private static AccessService mInstance;

    public static AccessService get() {
        if (mInstance == null) {
            mInstance = new AccessService();
        }
        return mInstance;
    }

    private static boolean accessibility = false;

    private String currentWindowActivity;

    private volatile String accessibilityClassName = "";

    private volatile String accessibilityPackageName = "";

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        if (accessibility) {
            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                if (MyApp.getInstance().getService() == null) {
                    MyApp.getInstance().setService(this);
                }
            }

            if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && accessibilityEvent.getClassName() != null) {
                this.currentWindowActivity = accessibilityEvent.getClassName().toString();
//                Log.i("TAG", "currentWindow------->" + this.currentWindowActivity);

            }

            if (accessibilityAccess(eventType, accessibilityEvent)) {
                return;
            }
        }
    }

    public boolean accessibilityAccess(int eventType, AccessibilityEvent accessibilityEvent) {
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            accessibilityInfo(accessibilityEvent.getPackageName(), accessibilityEvent.getClassName());
            return true;
        }
        return false;
    }

    private void accessibilityInfo(CharSequence packageName, CharSequence className) {
        if (packageName != null && className != null) {
            String charSequence = packageName.toString();
            String charSequence2 = className.toString();
            if (!charSequence2.startsWith("android.view.") && !charSequence2.startsWith("android.widget.")) {
                try {
                    String classname = getPackageManager().getActivityInfo(new ComponentName(charSequence, charSequence2), 0).name;
                    String packagename = packageName.toString();
                    AccessService.get().setClassName(classname);
                    AccessService.get().setyPackageName(packagename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.e("qyh", "==========服务开启");
        accessibility = true;
        Toast.makeText(this, "服务开启", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterrupt() {
        super.onInterrupt();
        accessibility = false;
        //MyApp.getInstance().destroyWindows();
        Toast.makeText(this, "服务中断", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessibility = false;
        //MyApp.getInstance().destroyWindows();
    }

    public void setyPackageName(String accessibilityPackageName) {
        this.accessibilityPackageName = accessibilityPackageName;
    }

    public String getPackageName() {
        return this.accessibilityPackageName;
    }

    public void setClassName(String accessibilityClassName) {
        this.accessibilityClassName = accessibilityClassName;
    }

    public String getClassName() {
        return this.accessibilityClassName;
    }
}
