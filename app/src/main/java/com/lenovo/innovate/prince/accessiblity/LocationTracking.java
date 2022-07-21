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

import android.content.Context;
import android.provider.Settings;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.an.deviceinfo.location.DeviceLocation;
import com.an.deviceinfo.location.LocationInfo;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.utils.XToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationTracking {

    Context l_context;

    public  void get_location(Context context){
        XXPermissions.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)

                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
                            LocationInfo locationInfo = new LocationInfo(context);
                            l_context = context;
                            DeviceLocation deviceLocation = locationInfo.getLocation();
                            XToastUtils.toast( "位置："+deviceLocation.getAddressLine1());

                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never){
                            XToastUtils.toast( "onDenied：被永久拒绝授权，请手动授予权限 ");
                            XXPermissions.startPermissionActivity(context,permissions);
                        }else {
                            XToastUtils.toast( "onDenied: 权限获取失败 ");
                        }
                    }
                });
    }

    public String location(){
        LocationInfo locationInfo = new LocationInfo(l_context);
        DeviceLocation deviceLocation = locationInfo.getLocation();
        String location =  AccUtils.time() + "位置："+deviceLocation.getAddressLine1();
         return location;
    }

}
