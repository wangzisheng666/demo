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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.an.deviceinfo.location.DeviceLocation;
import com.an.deviceinfo.location.LocationInfo;
import com.an.deviceinfo.usercontacts.UserContactInfo;
import com.an.deviceinfo.usercontacts.UserContacts;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.prince.calander.CalenderDataStruct;
import com.lenovo.innovate.prince.calander.calendarUtil;
import com.lenovo.innovate.prince.message.GetMessageInfo;
import com.lenovo.innovate.utils.XToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtils {

    public  void get_contact(Context context,Activity activity){
        XXPermissions.with(context)
                .permission(Permission.READ_CONTACTS)

                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            Activity a = (Activity) context ;
                            XToastUtils.toast( "onGranted: 获取权限成功！");
                            UserContactInfo userContactInfo = new UserContactInfo(a);
                            List<UserContacts> userContacts = userContactInfo.getContacts();
                            XToastUtils.toast( "电话号码"+userContacts.get(1).getMobileNumber());
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
                            /*GetMessageInfo getMessageInfo = new GetMessageInfo(context);
                            getMessageInfo.getSmsInfos();
                            obtainPhoneMessage();*/
                            Uri uri=Uri.parse("content://sms/");
                            ContentResolver resolver = getContentResolver();
                            Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type"}, null, null, null);
                            if(cursor!=null&&cursor.getCount()>0){
                                int _id;
                                String address;
                                String body;
                                String date;
                                int type;
                                while (cursor.moveToNext()){
                                    Map<String,Object>map=new HashMap<String,Object>();
                                    _id=cursor.getInt(0);
                                    address=cursor.getString(1);
                                    body=cursor.getString(2);
                                    date=cursor.getString(3);
                                    type=cursor.getInt(4);
                                    map.put("names",body);
                                 //   data.add(map);
                                    XToastUtils.toast( "_id="+_id+" address="+address+" body="+body+" date="+date+" type="+type);
                                    //通知适配器发生改变
                                  //  simpleAdapter.notifyDataSetChanged();
                                }

                            }
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
                            LocationInfo locationInfo = new LocationInfo(context);
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

    public  void get_calendar(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_CALENDAR)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");
                            ArrayList<CalenderDataStruct> calenderDataStructs = calendarUtil.GetCalenderSchedule(context);
                         //   Log.i("1234", "onCreate: calenderDataStructs "+ calenderDataStructs.size());
                            List<String> list = new ArrayList<>();
                            for (CalenderDataStruct item : calenderDataStructs){
                                XToastUtils.toast("onCreate: CalenderDataStruct "+ item.toString());
                                list.add(item.toString());
                            }
                            new AlertDialog.Builder((Activity)context).setTitle("信息提示")//设置对话框标题
                                    .setMessage(list.toString())
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                        }
                                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {//添加返回按钮

                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件

                                }

                            }).show();//在按
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
