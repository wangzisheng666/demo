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

package com.lenovo.innovate.prince.message;

public class MessageInfo {
    String SmsDate;
    String name;
    String SmsContent;

    @Override
    public String toString() {
        return "MessageInfo{" +
                "SmsDate='" + SmsDate + '\'' +
                ", name='" + name + '\'' +
                ", SmsContent='" + SmsContent + '\'' +
                '}';
    }

    public String getSmsDate() {
        return SmsDate;
    }

    public void setSmsDate(String smsDate) {
        SmsDate = smsDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmsContent() {
        return SmsContent;
    }

    public void setSmsContent(String smsContent) {
        SmsContent = smsContent;
    }
}
