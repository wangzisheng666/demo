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

import com.lenovo.innovate.prince.http.entity.PerInfo;
import com.xuexiang.xhttp2.annotation.RequestParams;
import com.xuexiang.xhttp2.model.ApiResult;
import com.xuexiang.xhttp2.model.XHttpRequest;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class TestApi {

    /**
     * 使用的是retrofit的接口定义
     */
    public interface PermissionService {

        @POST("/permission/")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Observable<ApiResult<PerInfo>> per_post(@Body RequestBody jsonBody);
    }


    @RequestParams(url = "/permission/", accessToken = false)
    public static class PerService_post extends XHttpRequest {

        /**
         *
         */
        public Object request;

        @Override
        protected Boolean getResponseEntityType() {
            return null;
        }
    }
}
