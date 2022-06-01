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

import com.lenovo.innovate.core.http.subscriber.TipRequestSubscriber;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xutil.tip.ToastUtils;

public class permissionUp {
    public static void post(){
        XHttp.post("/user/deleteUser")
                .params("userId","")
                .execute(Boolean.class)
                .subscribeWith(new TipRequestSubscriber<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean aBoolean) {
                        ToastUtils.toast("删除成功！");

                    }
                });


    }
}
