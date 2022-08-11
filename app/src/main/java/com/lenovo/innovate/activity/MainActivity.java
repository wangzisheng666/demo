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

package com.lenovo.innovate.activity;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lenovo.innovate.BuildConfig;
import com.lenovo.innovate.R;
import com.lenovo.innovate.Sqlite.DatabaseHelper;
import com.lenovo.innovate.core.BaseActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.ActivityMainBinding;
import com.lenovo.innovate.fragment.news.SettingsFragment;

import com.lenovo.innovate.hook.function.IMyBinder;
import com.lenovo.innovate.hook.function.WatchDogService;
import com.lenovo.innovate.prince.CameraManager;
import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.prince.accessiblity.StringText;
import com.lenovo.innovate.prince.http.permissionUp;
import com.lenovo.innovate.prince.utils.SettingSPUtils;
import com.lenovo.innovate.utils.Utils;
import com.lenovo.innovate.utils.XToastUtils;
import com.lenovo.innovate.utils.sdkinit.XUpdateInit;
import com.lenovo.innovate.utils.shell;
import com.lenovo.innovate.widget.GuideTipsDialog;

import com.lenovo.innovate.fragment.news.GuoDuFragment;
import com.lenovo.innovate.fragment.profile.CheckFragment;
import com.lenovo.innovate.fragment.trending.LanYongFragment;
import com.topjohnwu.superuser.Shell;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;
import com.xuexiang.xutil.tip.ToastUtils;

import org.json.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序主页面,只是一个简单的Tab例子
 *
 * @author xuexiang
 * @since 2019-07-07 23:53
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ClickUtils.OnClick2ExitListener, Toolbar.OnMenuItemClickListener {

    private String[] mTitles;
    static {
        // Set settings before the main shell can be created
        Shell.enableVerboseLogging = BuildConfig.DEBUG;
        Shell.setDefaultBuilder(Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        );
    }
    private SharedPreferences isOpennSP;
    private IMyBinder iMyBinder;
    private List<String> packageList = new ArrayList<>();
    private MyServiceConnection connection;


    @Override
    protected ActivityMainBinding viewBindingInflate(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        initListeners();
        if(SettingSPUtils.getInstance().get_String("防护","2").equals("1")){
            //XToastUtils.success("隐私安全开启");
        }else {
           // XToastUtils.error("隐私安全关闭");
            SettingSPUtils.getInstance().put_String("防护", "0");
        }
     //  AccUtils.execRootCmd("");





/*        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);

            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);

            ComponentName topActivity = runningTaskInfo.topActivity;

            String packageName = topActivity.getPackageName();

            Log.i("sunzn", packageName);

            SystemClock.sleep(2000);*/

    /*    AccUtils.execRootCmd("");

        String apkRoot = "chmod 777 " + getPackageCodePath();;
        String cmd12 = "settings put secure enabled_accessibility_services " + getPackageName() + "/com.lenovo.innovate.prince.accessiblity.AccessService";
        String cmd13 = "settings put secure accessibility_enabled 1";
        String cmd14 = "settings put secure accessibility_enabled 0";
        shell.cmd_shell(apkRoot);
        shell.cmd_shell(cmd12);
        shell.cmd_shell(cmd13);
        shell.cmd_shell(cmd14);
        shell.cmd_shell(cmd13);*/
/*
        String cachePath = getExternalCacheDir() + "";
        String fileCache = getFilesDir().getAbsolutePath();
        File codeCacheDir = getCodeCacheDir();
        File cacheDir = getCacheDir();
        File obbDir = getObbDir();
        File xiayiye5 = getDir("xiayiye5", MODE_PRIVATE);
        Log.e("打印文件夹：", cachePath);
        Log.e("打印文件夹：", fileCache);
        Log.e("打印文件夹：", codeCacheDir.getAbsolutePath());
        Log.e("打印文件夹：", cacheDir.getAbsolutePath());
        Log.e("打印文件夹：", obbDir.getAbsolutePath());
        Log.e("打印文件夹：", xiayiye5.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            File dataDir = getDataDir();
            Log.e("打印文件夹：", dataDir.getAbsolutePath());
        }
        File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.e("打印文件夹", "路径！" + download.getAbsolutePath());*/
        //getAllFiles("/data/adb/lspd/log/",".log");
    }



    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        WidgetUtils.clearActivityBackground(this);

        mTitles = ResUtils.getStringArray(R.array.home_titles);
        binding.includeMain.toolbar.setTitle(mTitles[0]);
        binding.includeMain.toolbar.inflateMenu(R.menu.menu_main);
        binding.includeMain.toolbar.setOnMenuItemClickListener(this);

        initHeader();

        //主页内容填充
        BaseFragment[] fragments = new BaseFragment[]{
                new GuoDuFragment(),
                new LanYongFragment(),
                new CheckFragment()
        };
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        binding.includeMain.viewPager.setOffscreenPageLimit(mTitles.length - 1);
        binding.includeMain.viewPager.setAdapter(adapter);



    }




    private void initData() {
       // GuideTipsDialog.showTips(this);
       // XUpdateInit.checkUpdate(this, false);
    }

    private void initHeader() {
        binding.navView.setItemIconTintList(null);
        View headerView = binding.navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        TextView tvSign = headerView.findViewById(R.id.tv_sign);

        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            tvSign.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            tvSign.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }

        // TODO: 2019-10-09 初始化数据
        ivAvatar.setImageResource(R.drawable.ic_default_head);
        tvAvatar.setText(R.string.app_name);
        tvSign.setText("权限申请，谨慎同意～～");
        navHeader.setOnClickListener(this);
    }

    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.includeMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏点击事件
        binding.navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                binding.drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                int id = menuItem.getItemId();
                if (id == R.id.nav_delete) {
                    GuoDuFragment.already_up1.clear();
                    GuoDuFragment.list_already_picture.clear();
                    SettingSPUtils.getInstance().put_String("防护", "0");
                    DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(this, "qxian", null, 1);
                    final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();
                    db.delete("qxian",null,null);
                    db.close();

                    permissionUp permissionUp = new permissionUp();
                    JSONObject jsonObject  = new JSONObject();
                    jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    permissionUp.Up(7,jsonObject);

                  //  openNewPage(ExploitationFragment.class);
                }else if (id == R.id.nav_settings) {
                     openNewPage(SettingsFragment.class);
                }
            }
            return true;
        });
        //主页事件监听
        binding.includeMain.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MenuItem item = binding.includeMain.bottomNavigation.getMenu().getItem(position);
                binding.includeMain.toolbar.setTitle(item.getTitle());
                item.setChecked(true);
                updateSideNavStatus(item);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.includeMain.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            binding.includeMain.toolbar.setTitle(menuItem.getTitle());
            binding.includeMain.viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_del) {
            DatabaseHelper dbsqLiteOpenHelper = new DatabaseHelper(this, "qxian", null, 1);
            final SQLiteDatabase db = dbsqLiteOpenHelper.getWritableDatabase();
             db.delete("qxian",null,null);
             db.close();
            XToastUtils.success("权限监控清空");
        }
        if (id == R.id.action_fangyu) {

            GuoDuFragment.already_up1.clear();
            GuoDuFragment.list_already_picture.clear();

            permissionUp permissionUp = new permissionUp();
            JSONObject jsonObject  = new JSONObject();
            jsonObject.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            permissionUp.Up(7,jsonObject);

                if(SettingSPUtils.getInstance().get_String("防护", "0").equals("1")){

                    SettingSPUtils.getInstance().put_String("防护", "0");
                    XToastUtils.error("隐私安全关闭");
                }else {
                    XToastUtils.success("隐私安全保护中");
                    SettingSPUtils.getInstance().put_String("防护", "1");
                    isOpennSP = getSharedPreferences("isOpen", Context.MODE_PRIVATE);
                    connection = new MyServiceConnection();
                    bindService(new Intent(this, WatchDogService.class), connection, Context.BIND_AUTO_CREATE);

                }

        }
        return false;
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav_header) {
          //  XToastUtils.toast("点击头部！");
        }
    }

    //================Navigation================//

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            binding.includeMain.toolbar.setTitle(menuItem.getTitle());
            binding.includeMain.viewPager.setCurrentItem(index, false);

            updateSideNavStatus(menuItem);
            return true;
        }
        return false;
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = binding.navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
         showSimpleTipDialog_all();
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }

    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyBinder = (IMyBinder) iBinder;
            iMyBinder.setPackageNames(packageList);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }



    private void showSimpleTipDialog_all() {
        String appName1 =  SettingSPUtils.getInstance().get_String("心灵云伙伴", "0");
        String appName2 =  SettingSPUtils.getInstance().get_String("百合婚恋", "0");
        String appName3 =  SettingSPUtils.getInstance().get_String("闪电素材", "0");

        if(appName1.equals("1")){
            show_app("心灵云伙伴");
            SettingSPUtils.getInstance().put_String("心灵云伙伴", "0");
        }else if(appName2.equals("1")){
            show_app("百合婚恋");
            SettingSPUtils.getInstance().put_String("百合婚恋", "0");
        }else if(appName3.equals("1")){
            show_app("闪电素材");
            SettingSPUtils.getInstance().put_String("闪电素材", "0");
        }/*else {
            if(SettingSPUtils.getInstance().get_String("防护", "0").equals("1")){
                show_app("该应用");
            }
        }*/

    }

    private void show_app(String app){
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .iconRes(R.drawable.tanhao)
                .title("警告")
                .content("检测到 "+app+" 存在违规或过度收集个人隐私信息，请谨慎使用！")
                .positiveText("确定")
                .build();
        StatusBarUtils.showDialog(MainActivity.this, dialog);
    }

}
