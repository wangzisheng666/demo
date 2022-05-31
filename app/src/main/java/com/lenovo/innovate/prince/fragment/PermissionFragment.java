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

package com.lenovo.innovate.prince.fragment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.R;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentPermissionBinding;
import com.lenovo.innovate.prince.PermissionUtils;
import com.lenovo.innovate.utils.RandomUtils;
import com.lenovo.innovate.utils.SettingUtils;
import com.lenovo.innovate.utils.TokenUtils;
import com.lenovo.innovate.utils.Utils;
import com.lenovo.innovate.utils.XToastUtils;
import com.lenovo.innovate.utils.sdkinit.UMengInit;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.List;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class PermissionFragment extends BaseFragment<FragmentPermissionBinding> implements View.OnClickListener {


    private static final String TAG = "permission";
    Context context;

    @NonNull
    @Override
    protected FragmentPermissionBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentPermissionBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        return titleBar;
    }

    @Override
    protected void initViews() {
        context = getActivity();
           /*    XXPermissions.with(this)
                //单个权限
               // .permission(Permission.RECORD_AUDIO)
               // .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                // .interceptor(new IPermissionInterceptor() {})
                //  .unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            Log.e(TAG, "onGranted: 获取权限成功！");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never){
                            Log.e(TAG, "onDenied：被永久拒绝授权，请手动授予权限 " );
                            XXPermissions.startPermissionActivity(getActivity(),permissions);
                        }else {
                            Log.e(TAG, "onDenied: 权限获取失败");
                        }
                    }
                });*/
    }

    @Override
    protected void initListeners() {
        binding.superContact.setOnClickListener(this);
        binding.superMessage.setOnClickListener(this);
        binding.superLocation.setOnClickListener(this);
        binding.superCalendar.setOnClickListener(this);
        binding.superStorage.setOnClickListener(this);
        binding.superMicrophone.setOnClickListener(this);
        binding.superCamera.setOnClickListener(this);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        PermissionUtils permissionUtils =new  PermissionUtils();
        int id = v.getId();
        if (id == R.id.super_contact) {
            permissionUtils.get_contact(context);
        } else if (id == R.id.super_message) {
            permissionUtils.get_message(context);
        }else if (id == R.id.super_location) {
            permissionUtils.get_location(context);
        }else if (id == R.id.super_calendar) {
            permissionUtils.get_calendar(context);
        }else if (id == R.id.super_storage) {
            permissionUtils.get_storage(context);
        } else if (id == R.id.super_microphone) {
            permissionUtils.get_microphone(context);
        }else if (id == R.id.super_camera) {
            permissionUtils.get_camera(context);
        }

    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}

