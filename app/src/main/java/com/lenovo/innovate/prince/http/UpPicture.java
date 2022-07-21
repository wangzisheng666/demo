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

package com.lenovo.innovate.prince.http;

import static com.xuexiang.xutil.XUtil.getContentResolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

import com.xuexiang.rxutil2.lifecycle.RxLifecycle;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xhttp2.utils.Utils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpPicture {

    @SuppressLint("CheckResult")
    @Permission(PermissionConsts.STORAGE)
    public void uploadPicture(Context context, String url,String mPicturePath) {
       /* if (StringUtils.isEmpty(mPicturePath)) {
            ToastUtils.toast("请先选择需要上传的图片!");
         //   selectPicture();
            return;
        }else {
            ToastUtils.toast("没有图片!");
        }*/
        //   mIProgressLoader.updateMessage("上传中...");
      /*  if (com.xuexiang.xhttp2.utils.Utils.isScopedStorageMode() && Utils.isPublicPath(mPicturePath)) {
            XHttp.post(url)
                    .params("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                    .uploadFile("file", getInputStreamByUri(null), FileUtils.getFileByPath(mPicturePath).getName(), new IProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                        }
                    }).execute(Boolean.class)
                    .compose(RxLifecycle.with(context).<Boolean>bindToLifecycle())
                    .subscribeWith(new ProgressLoadingSubscriber<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                           // mIsEditSuccess = true;
                           // ToastUtils.toast("图片上传" + (aBoolean ? "成功" : "失败") + "！");
                        }
                    });
        } else {*/
            XHttp.post(url)
                    .params("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                    .uploadFile("file", FileUtils.getFileByPath(mPicturePath), new IProgressResponseCallBack() {
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
      //  }
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
