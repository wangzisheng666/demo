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

package com.lenovo.innovate.prince.calander;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class calendarUtil {

    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";
    private static TelephonyManager mTm;


    public static ArrayList<CalenderDataStruct> GetCalenderSchedule(Context context){

        String startTime = "";
        String endTime = "";
        String eventTitle = "";
        String description = "";
        String location = "";
        String week = "";

        ArrayList<CalenderDataStruct> arr=new ArrayList<CalenderDataStruct>();
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null,
                null, null,  "dtstart"+" DESC");
        while (eventCursor.moveToNext()){

            eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
            description = eventCursor.getString(eventCursor.getColumnIndex("description"));
            location = eventCursor.getString(eventCursor.getColumnIndex("eventLocation"));
            startTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))));
            endTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtend"))));
            week = ""+ (getWeek(startTime));
            CalenderDataStruct item=new CalenderDataStruct(eventTitle, startTime, endTime,description, location,week);

            arr.add(item);
        }
        return arr;
    }


    private static JSONArray getcalendar(Context context){
        String startTime = "";
        String endTime = "";
        String eventTitle = "";
        String description = "";
        String location = "";

        JSONArray arr=new JSONArray();
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null,
                null, null, null);
        while (eventCursor.moveToNext()){
            JSONObject json=new JSONObject();
            eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
            description = eventCursor.getString(eventCursor.getColumnIndex("description"));
            location = eventCursor.getString(eventCursor.getColumnIndex("eventLocation"));
            startTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))));
            endTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtend"))));
            try {
                json.put("eventTitle",eventTitle);
                json.put("description",description);
                json.put("location",location);
                json.put("startTime",startTime);
                json.put("endTime",endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr.put(json);
        }
        return arr;
    }

    /**
     * 时间戳转换为字符串
     * @param time:时间戳
     * @return
     */
    private static String timeStamp2Date(long time) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 判断当前日期是星期几
     *
     * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
     *

     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    private static int getWeek(String pTime) {


        int Week = 0;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week = 0;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week = 1;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week = 2;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week = 3;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week = 4;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week = 5;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week = 6;
        }



        return Week;
    }
}
