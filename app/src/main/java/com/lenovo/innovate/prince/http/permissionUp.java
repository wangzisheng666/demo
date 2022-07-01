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

import android.os.UserManager;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.innovate.R;
import com.lenovo.innovate.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.utils.HttpUtils;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;

public class permissionUp {


    public void Up(int permission, JSONObject jsonObject) {
        CustomRequest request = XHttp.custom().accessToken(true);
        switch(permission) {
            case 1:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_contact(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 2:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_message(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 3:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_phone(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 4:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_location(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 5:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_cal(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 6:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_contact_log(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            case 7:
                request.apiCall(request.create(TestApi.UserService.class)
                        .post_delete(HttpUtils.getJsonRequestBody(jsonObject)), new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        ToastUtils.toast(JsonUtil.toJson(response));
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.toast(e.getDisplayMessage());
                    }
                });
                break;
            default:
                break;
        }

    }
}
