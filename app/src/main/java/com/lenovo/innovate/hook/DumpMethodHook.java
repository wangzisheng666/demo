package com.lenovo.innovate.hook;

import de.robv.android.xposed.XC_MethodHook;


/**
 * 自定义Hook回调监听器
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2021/7/6 9:15
 */
public abstract class DumpMethodHook extends XC_MethodHook {
    String p = " - sechegui - %s";
    protected DumpMethodHook(String pn){
        p = String.format(p, pn);
    }
    /**
     * 该方法会在Hook了指定方法后调用
     * @param param
     */
    @Override
    protected void afterHookedMethod(MethodHookParam param) {
        dump2(p);
    }

    private static void dump2(String p){
        //XposedBridge.log("[Dump Stack]: "+"---------------start----------------" + p);
        Throwable ex = new Throwable();

        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
           /* for (int i= 0; i < stackElements.length; i++) {
                StringBuilder sb=new StringBuilder("[方法栈调用]");
                sb.append(i);
                XposedBridge.log("[Dump Stack]"+i+": "+ stackElements[i].getClassName()
                        +"----"+stackElements[i].getFileName()
                        +"----" + stackElements[i].getLineNumber()
                        +"----" +stackElements[i].getMethodName() + p);
            }*/
        }
        //XposedBridge.log("DumpStack: "+ "---------------over----------------" + p);
    }
}
