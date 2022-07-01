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

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.lenovo.innovate.R;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentLanyongBinding;
import com.lenovo.innovate.prince.accessiblity.AccUtils;
import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.accessiblity.AutoWeChat;
import com.lenovo.innovate.prince.accessiblity.StringText;
import com.lenovo.innovate.prince.utils.Sdcard;
import com.lenovo.innovate.prince.view.ColoredToast;
import com.lenovo.innovate.prince.view.InfoDialog;
import com.lenovo.innovate.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

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
        binding.menuScreenHijack.setOnSuperTextViewClickListener(this);
        binding.menuScreen.setOnSuperTextViewClickListener(this);
        binding.menuLocation.setOnSuperTextViewClickListener(this);
        binding.menuDirectory.setOnSuperTextViewClickListener(this);
        binding.menuAutomaticKz.setOnSuperTextViewClickListener(this);
        binding.menuService.setOnSuperTextViewClickListener(this);
        binding.menuInstall.setOnSuperTextViewClickListener(this);
    }

    @Override
    public void onClick(SuperTextView superTextView) {
        switch(superTextView.getId()) {
            case R.id.menu_screen_hijack:

                break;
            case R.id.menu_screen:

                break;
            case R.id.menu_service:
                OpenServer();
                if (!isSettingOpen(AccessService.class, getActivity())) {
                    return;
                }

              /*  String apkRoot = "chmod 777 " + getActivity().getPackageCodePath();
                Utils.RootCommand(apkRoot);
                //  haveRoot();
                Utils.execRootCmdSilent("settings put secure enabled_accessibility_services " + getActivity().getPackageName() + "/com.lenovo.innovate.prince.accessiblity.AccessService");
                Utils.execRootCmdSilent("settings put secure accessibility_enabled 1");

                AppUtils.launchApp("com.tencent.mm");*/

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
                break;
            case R.id.menu_location:
                // LocationTracking locationTracking = new LocationTracking();
                XToastUtils.toast(   AccUtils.time() + "位置："+"北京市海淀区软件园西三路");
                break;
            case R.id.menu_directory:
                Sdcard sdcard = new Sdcard();
                XToastUtils.toast(sdcard.dir("/storage/emulated/0/"));

                //  Sdcard.getStorageDirectories(getActivity());
                break;
            case R.id.menu_install:

              /*  Sdcard sdcard1 = new Sdcard();
                XToastUtils.toast( sdcard1.dir("/data/adb/lspd/log/"));*/
                break;

            case R.id.menu_automatic_kz:
                String apkRoot1 = "chmod 777 " + getActivity().getPackageCodePath();
                AccUtils.RootCommand(apkRoot1);
                OpenServer();
                if (!isSettingOpen(AccessService.class, getActivity())) {
                    return;
                }
                String apkRoot = "chmod 777 " + getActivity().getPackageCodePath();
                AccUtils.RootCommand(apkRoot);

                AppUtils.launchApp("com.tencent.mm");
                ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
                    @Override
                    public String doInBackground() throws Throwable {
                        AutoWeChat autoWeChat = new AutoWeChat();
                        autoWeChat.start();
                        return null;
                    }
                    @Override
                    public void onSuccess(String result) {

                    }
                });
                break;

            default:
                break;
        }
    }

    private void OpenServer() {
        //判断权限是否开启
        if (!isSettingOpen(AccessService.class, getActivity())) {
            InfoDialog infoDialog = new InfoDialog.Builder(getActivity())
                    .setTitle("")
                    .setMessage("按照上图打开关键权限")
                    .setButton("开启权限", view ->
                            new ColoredToast.Maker(getActivity())
                                    .makeToast("Clicked OK", Toast.LENGTH_SHORT, getActivity())
                                    .show()
                    ).create();
            infoDialog.show();
        }

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
}
