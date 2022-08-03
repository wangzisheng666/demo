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

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.lenovo.innovate.prince.http.entity.PerInfo;
import com.xuexiang.xhttp2.annotation.RequestParams;
import com.xuexiang.xhttp2.model.ApiResult;
import com.xuexiang.xhttp2.model.XHttpRequest;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class TestApi {



    @RequestParams(url = "/permission/", accessToken = false)
    public static class PerService_post extends XHttpRequest {

        public Object request;

        @Override
        protected Boolean getResponseEntityType() {
            return null;
        }
    }


    /**
     * 使用的是retrofit的接口定义
     */
    public interface PerService {

        @POST("/App-Privacy/index.php/Home/Permission/")
        @Headers({ "Accept: application/json, text/javascript, */*; q=0.01",
                "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
                "X-Requested-With: XMLHttpRequest",
                "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
                "Accept-Encoding: gzip, deflate",
                "Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> login(@Body JSONObject jsonObject);
    }


    /**
     * 使用的是retrofit的接口定义
     */
    public interface UserService {
        //通讯录
        @POST("/App-Privacy/index.php/Home/Permission/")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_contact(@Body RequestBody jsonBody);
        //通话记录
        @POST("/App-Privacy/index.php/Home/Permission/phonelog")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_contact_log(@Body RequestBody jsonBody);
        //短信
        @POST("/App-Privacy/index.php/Home/Permission/message")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_message(@Body RequestBody jsonBody);
        //手机参数
        @POST("/App-Privacy/index.php/Home/Permission/parameters")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_phone(@Body RequestBody jsonBody);
        //位置
        @POST("/App-Privacy/index.php/Home/Permission/location")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_location(@Body RequestBody jsonBody);
        //日历
        @POST("/App-Privacy/index.php/Home/Permission/calender")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_cal(@Body RequestBody jsonBody);
        //删除
        @POST("/App-Privacy/index.php/Home/Permission/del")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_delete(@Body RequestBody jsonBody);

        //无障碍
        @POST("/App-Privacy/index.php/Home/Permission/server")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_access(@Body RequestBody jsonBody);

        //位置利用
        @POST("/App-Privacy/index.php/Home/Permission/start_gz")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_exp_location(@Body RequestBody jsonBody);
        //文件
        @POST("/App-Privacy/index.php/Home/Permission/ex_file")
        @Headers({"Accept: application/json, text/javascript, */*; q=0.01","Content-Type: application/x-www-form-urlencoded; charset=UTF-8","X-Requested-With: XMLHttpRequest","Accept-Encoding: gzip, deflate","Upgrade-Insecure-Requests: 1"})
        Observable<ApiResult<String>> post_exp_file(@Body RequestBody jsonBody);
    }


}
