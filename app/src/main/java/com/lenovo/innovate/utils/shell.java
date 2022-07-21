


package com.lenovo.innovate.utils;

import android.util.Log;

import com.topjohnwu.superuser.Shell;

import java.util.List;

public class shell {

    public static List<String> cmd_shell(String cmd) {
        Shell.Result result;
        result = Shell.cmd(cmd).exec();
// Aside from commands, you can also load scripts from InputStream.
// This is NOT like executing a script like "sh script.sh", but rather
// more similar to sourcing the script (". script.sh").
     //   result = Shell.cmd(getResources().openRawResource(R.raw.script)).exec();
        String aa = result.toString();
        List<String> out = result.getOut();  // stdout
        int code = result.getCode();         // return code of the last command
        boolean ok = result.isSuccess();     // return code == 0?
       // Log.i("111",aa);
        return out;

    }
}
