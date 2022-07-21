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
import android.os.Environment;
import android.provider.CallLog;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.lenovo.innovate.utils.shell;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xui.widget.shadow.ShadowTextView;
import com.xuexiang.xui.widget.textview.LoggerTextView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.DensityUtils;
import com.xuexiang.xutil.net.NetworkUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-10-30 00:18
 */
@Page(anim = CoreAnim.none)
public class CheckFragment extends BaseFragment<FragmentCheckBinding> {

    private static final String TAG = "CheckFragment";
    TextView lenovo_text;
    ShadowTextView button;
    private String[] Install_App = null;


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
        lenovo_text = findViewById(R.id.check_lenovo);
        button = findViewById(R.id.checkApp);

        get_package();
        red_log();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Install_App != null) {
                    showPickerView(Install_App);
                }

            }
        });

    }
    private void red_log(){
        String ls = "ASH_STANDALONE=1 /data/adb/magisk/busybox ls /data/adb/lspd/log/";
       // List<String> ls1 = AccUtils.execRootCmd_list(ls);
        List<String> ls1 = shell.cmd_shell(ls);
                String moudles = null;
        for (int i = 0; i < ls1.size(); i++) {
            if (ls1.get(i).contains("modules")) {
                moudles = ls1.get(i);
            }
        }

        if (moudles == null) {
            return;
        }
        Log.i("22222222", moudles);

        String cmd = "ASH_STANDALONE=1 /data/adb/magisk/busybox   cat /data/adb/lspd/log/" + moudles;

       // List<String> aa = removeDuplicate(execRootCmd_per(cmd));
        List<String> read_log = shell.cmd_shell(cmd);
        List<String> read_log_sub = new ArrayList<>();
        for(int i = read_log.size() - 1; i >= 0; i--){
            String item = read_log.get(i);
            if(!item.contains("#")){
                read_log.remove(item);
            }else {
                if(item.contains("@")){
                    String text = StringUtils.substringAfter(read_log.get(i), "#");
                    read_log_sub.add(text);
                }

            }
        }

        List<String> aa = removeDuplicate(read_log_sub);
        for (int i = 0; i < aa.size(); i++) {
            Log.i("22222222", aa.get(i));
            insert(aa.get(i));
        }

        get_sql_app();
        CSH_per();

        String del = "ASH_STANDALONE=1 /data/adb/magisk/busybox   cat /dev/null  > /data/adb/lspd/log/" + moudles;
        shell.cmd_shell(del);
        //AccUtils.execRootCmd(del);
    }

    private void insert(String pe) {
        DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(getActivity(), "qxian", null, 1);
        final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();

        Cursor cursor1 = db.query("qxian", new String[]{"id", "time", "per"}, null,
                null, null, null, null);
//利用游标遍历所有数据对象
        ArrayList<String> list_sql = new ArrayList<String>();
        while (cursor1.moveToNext()) {
            String time = cursor1.getString(cursor1.getColumnIndex("time"));
            String per = cursor1.getString(cursor1.getColumnIndex("per"));
            list_sql.add(time + per);
        }


        ContentValues values = new ContentValues();
        String package_name = StringUtils.substringBetween(pe, "@", "&");
        String cn = SettingSPUtils.getInstance().get_String(package_name, null);
        if (cn == null) {
            values.put("app", StringUtils.substringBetween(pe, "@", "&"));
        } else {
            values.put("app", cn);
        }
        String time1 = StringUtils.substringBefore(pe, "@");
        values.put("time", time1);
        String per1 = StringUtils.substringAfter(pe, "&");
        values.put("per", per1);

        if (!list_sql.contains(time1 + per1)) {
            //数据库执行插入命令
            db.insert("qxian", null, values);
            Log.i("Mainactivity", "------插入" + time1 + cn + per1);
        } else {
            Log.i("Mainactivity", " 已经插入" + time1 + per1);
        }
        cursor1.close();
    }

    public List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    // 执行命令并且输出结果
    public List<String> execRootCmd_per(String cmd) {
        DataOutputStream dos = null;
        BufferedReader dis = null;

        List<String> list_per = new ArrayList<>();
        List<String> list_per1 = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令

            dos = new DataOutputStream(p.getOutputStream());
            InputStream is = p.getInputStream();
            dis = new BufferedReader(new InputStreamReader(is));
            // Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                if (line.contains("#")) {
                    list_per.add(line);
                    String text = StringUtils.substringAfter(line.toString(), "#");
                    list_per1.add(text);
                    //  Log.i("22222222",text);
                }
            }

            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list_per1;
    }

    private String[] get_sql_app() {
        DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(getActivity(), "qxian", null, 1);
        final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();
        Cursor cursor1 = db.query("qxian", new String[]{"app"}, null,
                null, null, null, null);
//利用游标遍历所有数据对象
        ArrayList<String> list_app = new ArrayList<String>();
        while (cursor1.moveToNext()) {
            String app = cursor1.getString(cursor1.getColumnIndex("app"));
            if (!list_app.contains(app)) {
                list_app.add(app);
            }

        }
        Install_App = new String[list_app.size()];
        for (int i = 0; i < list_app.size(); i++) {
            Install_App[i] = list_app.get(i);

        }

        cursor1.close();
        return new String[list_app.size()];
    }

    @Override
    protected void initListeners() {

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
           // refreshLayout.getLayout().postDelayed(() -> {
                red_log();
                button.setText("请选择需查看应用");
                refreshLayout.finishRefresh();
          //  }, 1000);
        });
        binding.refreshLayout.autoRefresh();

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

    private int SelectOption = 0;

    private void showPickerView(String[] app1) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            if(app1.length != 0){
                String aa = app1[options1];
                //XToastUtils.toast(aa);
                select_app(aa);
                button.setText(app1[options1]);
                SelectOption = options1;
            }

            return false;
        })
                .setTitleText("应用选择")
                .setSelectOptions(SelectOption)
                .build();
        pvOptions.setPicker(app1);
        pvOptions.show();

    }




    private void select_app(String install_app) {


        DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(getActivity(), "qxian", null, 1);
        final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();
       // db.delete("qxian",null,null);
        Log.i("123456", String.valueOf(install_app));
        Cursor cursor1 = db.query("qxian", new String[]{"id", "time", "app", "per"}, "app=?", new String[]{install_app}
                , null, null, null);
//利用游标遍历所有数据对象
        ArrayList<String> list_app = new ArrayList<String>();

        while (cursor1.moveToNext()) {
            String time = cursor1.getString(cursor1.getColumnIndex("time"));
            String app = cursor1.getString(cursor1.getColumnIndex("app"));
            String per = cursor1.getString(cursor1.getColumnIndex("per"));
            list_app.add(time + "  " + app + "  " + per);

        }
        cursor1.close();

        log_text(list_app);
    }


    private void log_text(ArrayList<String> list) {

        String str = "";
        for (int i = list.size(); i-- > 0; ) {
            Log.i("123456", list.get(i));
            str += list.get(i) + "\n";

        }

        lenovo_text.setText(str);

    }

    private void CSH_per() {


        DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(getActivity(), "qxian", null, 1);
        final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();

        Cursor cursor1 = db.query("qxian", new String[]{"id", "time", "app", "per"}, null,
                null, null, null, null);
        ArrayList<String> list_app = new ArrayList<String>();
        while (cursor1.moveToNext()) {
            String time = cursor1.getString(cursor1.getColumnIndex("time"));
            String app = cursor1.getString(cursor1.getColumnIndex("app"));
            String per = cursor1.getString(cursor1.getColumnIndex("per"));
            list_app.add(time + "  " + app + "  " + per);
        }
        cursor1.close();

        String str = "";
        for (int i = list_app.size(); i-- > 0; ) {
            Log.i("123456", list_app.get(i));
            str += list_app.get(i) + "\n";


        }

        lenovo_text.setText(str);
    }

    private void get_package() {

        if (SettingSPUtils.getInstance().get_String("isGetPackage", "0").equals("0")) {
            ArrayList<AppInfo> appInfos = getAllAppInfo(getActivity(), true);
            for (int i = 0; i < appInfos.size(); i++) {
                String packagename = appInfos.get(i).getPackage_name();
                String name = appInfos.get(i).getLabel();
                SettingSPUtils.getInstance().put_String(packagename, name);
            }
            SettingSPUtils.getInstance().put_String("isGetPackage", "1");
        }
    }
    public static ArrayList<AppInfo> getAllAppInfo(Context ctx, boolean isFilterSystem) {
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
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0 && isFilterSystem) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(bean);
            }
        }
        return appBeanList;
    }
}
