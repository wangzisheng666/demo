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

package com.lenovo.innovate.fragment.profile;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Process;
import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.R;
import com.lenovo.innovate.Sqlite.DatabaseHelper;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentCheckBinding;


import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.accessiblity.AutoWeChat;
import com.lenovo.innovate.prince.accessiblity.StringText;
import com.lenovo.innovate.prince.http.permissionUp;
import com.lenovo.innovate.prince.utils.Sdcard;
import com.lenovo.innovate.prince.utils.SettingSPUtils;
import com.lenovo.innovate.prince.view.ColoredToast;
import com.lenovo.innovate.prince.view.InfoDialog;
import com.lenovo.innovate.utils.Utils;
import com.lenovo.innovate.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuexiang
 * @since 2019-10-30 00:18
 */
@Page(anim = CoreAnim.none)
public class CheckFragment extends BaseFragment<FragmentCheckBinding> implements SuperTextView.OnSuperTextViewClickListener {

    private static final String TAG = "CheckFragment";

    @NonNull
    @Override
    protected FragmentCheckBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentCheckBinding.inflate(inflater, container, false);
    }

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

        get_per();





    }

    @Override
    protected void initListeners() {

    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
      /*  switch(superTextView.getId()) {
            case
                break;

            default:
                break;
        }*/

    }
    /**
     * 获取手机已安装应用列表
     * @param ctx
     * @param isFilterSystem 是否过滤系统应用
     * @return
     */
    public static ArrayList<AppInfo> getAllAppInfo(Context ctx,boolean isFilterSystem) {
        ArrayList<AppInfo> appBeanList = new ArrayList<>();
        AppInfo bean = null;

        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            bean = new AppInfo();
            bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setLabel(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setPackage_name(p.applicationInfo.packageName);
            int flags = p.applicationInfo.flags;
            // 判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0&&isFilterSystem) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(bean);
            }
        }
        return appBeanList;
    }


    public static String readFileContent(String filePath1) {
        File file = new File(filePath1);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    private void get_package(){

        if(SettingSPUtils.getInstance().get_String("isGetPackage", "0").equals("0")){
            ArrayList<AppInfo> appInfos =  getAllAppInfo(getActivity(),true);
            for (int i = 0; i < appInfos.size(); i++) {
                String packagename = appInfos.get(i).getPackage_name();
                String name = appInfos.get(i).getLabel();
                SettingSPUtils.getInstance().put_String(packagename,name);
            }
            SettingSPUtils.getInstance().put_String("isGetPackage","1");
        }
    }

    public void get_per(){
        XXPermissions.with(getActivity())
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            List<String> list_sql = new ArrayList<>();
                            get_package();
                            DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(getActivity(),"qxian",null,1);
                            final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();
                            Cursor cursor = db.query("qxian", new String[]{"id","time"}, null,
                                    null, null, null, null);
//利用游标遍历所有数据对象
                            while(cursor.moveToNext()){
                                String time = cursor.getString(cursor.getColumnIndex("time"));
                                list_sql.add(time);
                                Log.i("Mainactivity"," username: " + time );
                            }
// 关闭游标，释放资源
                            cursor.close();
                            String log = readFileContent("/storage/emulated/0/posed/log");
                            if (log.isEmpty()){
                                return;
                            }
                            Log.i("aaaaaa",log);
                            List<String> list = new ArrayList<>();
                            String[] strs = log.split("#");
                            for (int i = 0, len = strs.length; i < len; i++) {
                                if(!list.contains(strs[i].toString())){
                                    if(strs[i].toString().isEmpty()){
                                        if(strs[i].toString().isEmpty()){
                                            continue;
                                        }
                                    }
                                    list.add(strs[i].toString());
                                    ContentValues values = new ContentValues();
                                    String package_name = StringUtils.substringBetween(strs[i].toString(), "@", "&");

                                    String cn = SettingSPUtils.getInstance().get_String(package_name, null);
                                    if(cn.isEmpty()){
                                        values.put("app",StringUtils.substringBetween(strs[i].toString(), "@", "&"));
                                    }else {
                                        values.put("app",cn);
                                    }
                                    String time1 = StringUtils.substringBefore(strs[i].toString(), "@");
                                    values.put("time", time1);
                                    values.put("per",StringUtils.substringAfter(strs[i].toString(), "&"));
                                    if(!list_sql.contains(time1)){
                                        //数据库执行插入命令
                                        db.insert("qxian", null, values);
                                    }else {
                                        Log.i("Mainactivity"," 已经插入" + time1 );
                                    }

                                }
                            }

                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never){
                            XToastUtils.toast( "onDenied：被永久拒绝授权，请手动授予权限 ");
                            XXPermissions.startPermissionActivity(getActivity(),permissions);
                        }else {
                            XToastUtils.toast( "onDenied: 权限获取失败 ");
                        }
                    }
                });
    }

}
