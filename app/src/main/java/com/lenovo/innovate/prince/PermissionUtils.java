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

package com.lenovo.innovate.prince;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.prince.message.GetMessageInfo;
import com.lenovo.innovate.utils.XToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtils {

    public  void get_contact(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_CONTACTS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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

    public void get_message(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_SMS)
                .permission(Permission.RECEIVE_SMS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
                            GetMessageInfo getMessageInfo = new GetMessageInfo(context);
                            getMessageInfo.getSmsInfos();
                            obtainPhoneMessage();
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

    public  void get_location(Context context){
        XXPermissions.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)

                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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

    public  void get_calendar(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_CALENDAR)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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

    public  void get_storage(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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

    public  void get_microphone(Context context){
        XXPermissions.with(context)
                .permission(Permission.RECORD_AUDIO)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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


    public  void get_camera(Context context){
        XXPermissions.with(context)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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

  /*  public static void get_storage(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
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
    }*/

    private Uri SMS_INBOX = Uri.parse("content://sms/");

    private  void obtainPhoneMessage() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("ooc", "************cur == null");
            return;
        }
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("num", number);
            map.put("mess", body);
            //list.add(map);
        }
    }

}
