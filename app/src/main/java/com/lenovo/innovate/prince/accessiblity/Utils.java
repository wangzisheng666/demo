package com.lenovo.innovate.prince.accessiblity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    private static final int MIN_CLICK_DELAY_TIME = 3000;
    private static final String TAG ="Utils" ;
    private static long lastClickTime;
    private static boolean mHaveRoot = false;

    public static int dp2Px(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static boolean isSettingOpen(Class cls, Context context) {
        try {
            if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_enabled", 0) != 1) {
                return false;
            }
            String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
            if (!TextUtils.isEmpty(string)) {
                TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    String next = simpleStringSplitter.next();
                    if (next.equalsIgnoreCase(context.getPackageName() + "/" + cls.getName())) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            Log.e("qyh", "isSettingOpen: " + th.getMessage());
        }
        return false;
    }

    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isEmptyArray(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmptyView(AccessibilityNodeInfo accessibilityNodeInfo) {
        return accessibilityNodeInfo == null;
    }

    public static int getTime() {
        return Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000));
    }

    public Boolean checkSusPermiss(Context context) {
        return false;
    }

    public static void LogV(String str) {
        Log.v("xx", str);
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("WrongConstant")
    public static void jumpToSetting(Context context) {
        try {
            if (context != null) {
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeTxt(String parent, String file, String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File("/storage/emulated/0/" + parent);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = dir + File.separator + file;
            File f = new File(fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(fileName, true);
                fileOutputStream.write(json.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void writeTxt1(String parent, String file, String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File("/storage/emulated/0/AutoSend" + parent);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = dir + File.separator + file;
            File f = new File(fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(fileName);
                fileOutputStream.write(json.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void writeThrowableTxt(Throwable th) {
        String crash = "crash-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            printWriter.flush();
            printWriter.close();
            stringBuffer.append(stringWriter.toString());
            writeTxt("AutoSend", crash, stringBuffer.toString());
        } catch (Exception e) {
            stringBuffer.append("an error occured while writing file...\r\n");
            writeTxt("AutoSend", crash, stringBuffer.toString());
        }
    }

    public static String readFileContent(String filePath1) {
        File file = new File(filePath1);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }


    public static String time() {

        // 获得当前时间
        Date getDate = Calendar.getInstance().getTime();

        // 2018-07-22
    //    String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(getDate);
      //  System.out.println("单纯日期:" + dateStr);
        // 2018-07-22 14:26:12
        String dateStr1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDate);
        System.out.println("日期加时间:" + dateStr1);
        // 07/22 14:26
      //  String dateStr2 = new SimpleDateFormat("MM/dd HH:mm").format(getDate);
       // System.out.println("日期加时间:" + dateStr1);

        return dateStr1;
    }


    public static boolean delFile(String strPath) {
        boolean filebool = false;
        File file = new File(strPath);
        if (file.exists() && file.isDirectory()) {
            if (file.listFiles().length == 0) {
                file.delete();
            } else {
                File[] ff = file.listFiles();
                for (int i = 0; i < ff.length; i++) {
                    if (ff[i].isDirectory()) {
                        delFile(strPath);
                    }
                    ff[i].delete();

                }
            }
        }
        file.delete();// 如果要把文件夹也删除。。就去掉注释。。
        filebool = true;
        return filebool;

    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    // 判断机器Android是否已经root，即是否获取root权限
    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                Log.i(TAG, "have root!");
                mHaveRoot = true;
            } else {
                Log.i(TAG, "not root!");
            }
        } else {
            Log.i(TAG, "mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }

    // 执行命令并且输出结果
    public static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 执行命令但不关注结果输出
    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean RootCommand(String command)
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e)
        {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    public static void tap(int x, int y) {
        execShellCmd("input tap " + x + " " + y);
    }

    private static void execShellCmd(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            System.out.println("ScriptHelper->execShellCmd->" + cmd + ":" + e.getMessage());
        }
    }
}

