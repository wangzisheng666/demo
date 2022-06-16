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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.R;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentPermissionBinding;
import com.lenovo.innovate.prince.PermissionUtils;
import com.lenovo.innovate.prince.activity.PermissionActivity;
import com.lenovo.innovate.prince.utils.SettingSPUtils;
import com.lenovo.innovate.utils.RandomUtils;
import com.lenovo.innovate.utils.SettingUtils;
import com.lenovo.innovate.utils.TokenUtils;
import com.lenovo.innovate.utils.XToastUtils;
import com.lenovo.innovate.utils.sdkinit.UMengInit;

import com.xuexiang.rxutil2.lifecycle.RxLifecycle;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xhttp2.utils.Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.DrawableUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.NetworkUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(name = "隐私安全合规")
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
       // titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setLeftImageDrawable(DrawableUtils.setTint(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close), ThemeUtils.getMainThemeColor(getContext())));
        titleBar.setTitle("隐私安全合规");
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
        EditText editText =findViewById(R.id.et_api_url);
        editText.setText(SettingSPUtils.getInstance().getApiURL());
        Button button=findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString().trim();
                if (NetworkUtils.isUrlValid(url) && XHttpSDK.verifyBaseUrl(url)) {
                    XHttpSDK.setBaseUrl(url);
                    SettingSPUtils.getInstance().setApiURL(url);
                    ToastUtils.toast("地址保存成功！");
                } else {
                    ToastUtils.toast("输入的地址不合法！");
                }
            }
        });

    }

    @Override
    protected void initListeners() {
        binding.superContact.setOnClickListener(this);
        binding.superMessage.setOnClickListener(this);
        binding.superPhoneMessage.setOnClickListener(this);
        binding.superLocation.setOnClickListener(this);
        binding.superCalendar.setOnClickListener(this);
        binding.superStorage.setOnClickListener(this);
        binding.superMicrophone.setOnClickListener(this);
        binding.superCamera.setOnClickListener(this);
        binding.superEx.setOnClickListener(this);



    }

    @SingleClick
    @Override
    public void onClick(View v) {
        PermissionUtils permissionUtils =new  PermissionUtils();
        int id = v.getId();
        if (id == R.id.super_contact) {
            permissionUtils.get_contact(context,getActivity());
        } else if (id == R.id.super_message) {
            permissionUtils.get_message(context);
        } else if (id == R.id.super_phone_message) {
            //
            permissionUtils.get_phoneMessage(context);
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
        }else if (id == R.id.super_ex) {
            openNewPage(ExploitationFragment.class);
        }

    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    String mPicturePath;
    private IProgressLoader mIProgressLoader;
    Uri mPictureUri;
    private boolean mIsEditSuccess;
    @SuppressLint("CheckResult")
    @Permission(PermissionConsts.STORAGE)
    private void uploadPicture() {
        if (StringUtils.isEmpty(mPicturePath)) {
            ToastUtils.toast("请先选择需要上传的图片!");
            selectPicture();
            return;
        }
        mIProgressLoader.updateMessage("上传中...");
        if (com.xuexiang.xhttp2.utils.Utils.isScopedStorageMode() && Utils.isPublicPath(mPicturePath)) {
            XHttp.post("/book/uploadBookPicture")
                    .params("bookId", "")
                    .uploadFile("file", getInputStreamByUri(mPictureUri), FileUtils.getFileByPath(mPicturePath).getName(), new IProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                        }
                    }).execute(Boolean.class)
                    .compose(RxLifecycle.with(this).<Boolean>bindToLifecycle())
                    .subscribeWith(new ProgressLoadingSubscriber<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mIsEditSuccess = true;
                            ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                        }
                    });
        } else {
            XHttp.post("/book/uploadBookPicture")
                    .params("bookId","")
                    .uploadFile("file", FileUtils.getFileByPath(mPicturePath), new IProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                        }
                    }).execute(Boolean.class)
                    .compose(RxLifecycle.with(this).<Boolean>bindToLifecycle())
                    .subscribeWith(new ProgressLoadingSubscriber<Boolean>(mIProgressLoader) {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            mIsEditSuccess = true;
                            ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                        }
                    });
        }
    }

    @Permission(PermissionConsts.STORAGE)
    private void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2000);
    }

    private InputStream getInputStreamByUri(Uri uri) {
        try {
            return XUtil.getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}