package com.lenovo.innovate.prince.utils;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;


import com.lenovo.innovate.prince.accessiblity.AccessService;
import com.lenovo.innovate.prince.accessiblity.AccUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServiceUtils {

    public static Rect mRecycleRect = new Rect();

    public static boolean clickView(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        if (accessibilityNodeInfo.isClickable()) {
            return accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
        if (parent == null) {
            return false;
        }
        boolean clickView = clickView(parent);
        parent.recycle();
        return clickView;
    }

    public static boolean clickScrollView(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        if (accessibilityNodeInfo.isScrollable()) {
            return accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
        if (parent == null) {
            return false;
        }
        boolean clickView = clickScrollView(parent);
        parent.recycle();
        return clickView;
    }

    public static boolean findViewByIdAndClickView(AccessibilityService accessibilityService, String str) {
        try {
            List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = ServiceUtils.findViewByIdList(accessibilityService, str);
            if (!AccUtils.isEmptyArray(accessibilityNodeInfoList1)) {
                return clickView(accessibilityNodeInfoList1.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean findViewByIdIndexAndClickView(AccessibilityService accessibilityService, String str, int i) {
        try {
            AccessibilityNodeInfo accessibilityNodeInfo = ServiceUtils.findViewByIdIndex(accessibilityService, str, i);
            if (accessibilityNodeInfo == null) {
                return false;
            }
            return clickView(accessibilityNodeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean findViewByTextAndClickView(AccessibilityService accessibilityService, String str) {
        try {
            List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = ServiceUtils.findViewByEqualsText(accessibilityService, str);
            if (!AccUtils.isEmptyArray(accessibilityNodeInfoList1)) {
                return clickView(accessibilityNodeInfoList1.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean findViewByIdAndClickScrollView(AccessibilityService accessibilityService, String str) {
        try {
            List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = ServiceUtils.findViewByIdList(accessibilityService, str);
            if (!AccUtils.isEmptyArray(accessibilityNodeInfoList1)) {
                return clickScrollView(accessibilityNodeInfoList1.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sortList(List<AccessibilityNodeInfo> list) {
        Collections.sort(list, new order());
    }

    public static class order implements Comparator<AccessibilityNodeInfo> {
        public int compare(AccessibilityNodeInfo accessibilityNodeInfo, AccessibilityNodeInfo accessibilityNodeInfo2) {
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            Rect rect2 = new Rect();
            accessibilityNodeInfo2.getBoundsInScreen(rect2);
            return (rect.left + rect.top) - (rect2.left + rect2.top);
        }
    }

    public static List<AccessibilityNodeInfo> filterErrData(List<AccessibilityNodeInfo> list) {
        if (AccUtils.isEmptyArray(list)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (AccessibilityNodeInfo next : list) {
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            next.getBoundsInParent(new Rect());
            if (rect.left > 1 && rect.left != rect.right) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static void setText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, str);
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
    }


    public static List<AccessibilityNodeInfo> findViewByContainsText(AccessibilityService accessibilityService, String str) {
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            accessibilityService.getWindows();
            return null;
        }
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(str);
        rootInActiveWindow.recycle();
        return findAccessibilityNodeInfosByText;
    }

    public static List<AccessibilityNodeInfo> findViewByContainsText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        return accessibilityNodeInfo.findAccessibilityNodeInfosByText(str);
    }

    public static AccessibilityNodeInfo view(AccessibilityService accessibilityService) {
        List<AccessibilityNodeInfo> findView = ServiceUtils.findViewByEqualsText(accessibilityService, "我的客户");
        if (findView != null && !findView.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : findView) {

            }
        }
        return findView.get(0).getParent();
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityService accessibilityService, String str) {
        List<AccessibilityNodeInfo> findViewByContainsText = findViewByContainsText(accessibilityService, str);
        if (AccUtils.isEmptyArray(findViewByContainsText)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (AccessibilityNodeInfo next : findViewByContainsText) {
            if (next.getText() == null || !str.equals(next.getText().toString())) {
                next.recycle();
            } else {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText = accessibilityNodeInfo.findAccessibilityNodeInfosByText(str);
        if (AccUtils.isEmptyArray(findAccessibilityNodeInfosByText)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByText) {
            if (next.getText() == null || !str.equals(next.getText().toString())) {
                next.recycle();
            } else {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityService accessibilityService, String str, String str2) {
        List<AccessibilityNodeInfo> findViewByContainsText = findViewByContainsText(accessibilityService, str);
        if (AccUtils.isEmptyArray(findViewByContainsText)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (AccessibilityNodeInfo next : findViewByContainsText) {
            if (next.getText() == null || !str.equals(next.getText().toString()) || !str2.equals(next.getClassName())) {
                next.recycle();
            } else {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static AccessibilityNodeInfo findViewById(AccessibilityService accessibilityService, String str, String str2) {
        return findViewById(accessibilityService, str + ":id/" + str2);
    }

    public static AccessibilityNodeInfo findViewById(AccessibilityService accessibilityService, String str) {
        List<AccessibilityNodeInfo> findViewByIdList = findViewByIdList(accessibilityService, str);
        if (AccUtils.isEmptyArray(findViewByIdList)) {
            return null;
        }
        return findViewByIdList.get(0);
    }


    public static AccessibilityNodeInfo findViewByIdIndex(AccessibilityService accessibilityService, String str, int i) {
        List<AccessibilityNodeInfo> findViewByIdList = findViewByIdList(accessibilityService, str);
        if (AccUtils.isEmptyArray(findViewByIdList)) {
            return null;
        }
        return i < findViewByIdList.size() ? findViewByIdList.get(i) : findViewByIdList.get(findViewByIdList.size() - 1);
    }

    public static AccessibilityNodeInfo findViewByTopRect(AccessibilityService accessibilityService, String str) {
        List<AccessibilityNodeInfo> findViewByIdList = findViewByIdList(accessibilityService, str);
        if (AccUtils.isEmptyArray(findViewByIdList)) {
            return null;
        }
        for (AccessibilityNodeInfo next : findViewByIdList) {
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            if (rect.top != 0) {
                return next;
            }
        }
        return null;
    }

    public static List<AccessibilityNodeInfo> findViewByIdList(AccessibilityService accessibilityService, String str) {
        try {

            AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
            if (rootInActiveWindow == null) {
                return null;
            }

            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId;

            int n = 0;
            do {

                findAccessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId(str);
                Thread.sleep(100);
                n++;
            } while (n < 20 && AccUtils.isEmptyArray(findAccessibilityNodeInfosByViewId));

            rootInActiveWindow.recycle();

            return findAccessibilityNodeInfosByViewId;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AccessibilityNodeInfo findViewByFirstClassName(AccessibilityService accessibilityService, String str) {
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return null;
        }
        AccessibilityNodeInfo findViewByFirstClassName = findViewByFirstClassName(rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return findViewByFirstClassName;
    }

    public static AccessibilityNodeInfo findViewByFirstClassName(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo == null) {
            return null;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child != null) {
                if (str.equals(child.getClassName().toString())) {
                    return child;
                }
                AccessibilityNodeInfo findViewByFirstClassName = findViewByFirstClassName(child, str);
                child.recycle();
                if (findViewByFirstClassName != null) {
                    return findViewByFirstClassName;
                }
            }
        }
        return null;
    }

    public static List<AccessibilityNodeInfo> findAllView(AccessibilityService accessibilityService) {
        ArrayList arrayList = new ArrayList();
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return arrayList;
        }
        findViewByClassName((List<AccessibilityNodeInfo>) arrayList, rootInActiveWindow);
        rootInActiveWindow.recycle();
        return arrayList;
    }

    public static void findViewByClassName(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    list.add(child);
                    findViewByClassName(list, child);
                    child.recycle();
                }
            }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByClassName(AccessibilityService accessibilityService, String str) {
        ArrayList arrayList = new ArrayList();
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return arrayList;
        }
        findViewByClassName(arrayList, rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByClassNameInGroup(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        ArrayList arrayList = new ArrayList();
        findViewByClassName(arrayList, accessibilityNodeInfo, str);
        return arrayList;
    }

    public static void findViewByClassName(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    if (str.equals(child.getClassName().toString())) {
                        list.add(child);
                    } else {
                        findViewByClassName(list, child, str);
                        child.recycle();
                    }
                }
            }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByClassNameDesc(AccessibilityService accessibilityService, String str) {
        ArrayList arrayList = new ArrayList();
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return arrayList;
        }
        findViewByClassNameInFrame(arrayList, rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return arrayList;
    }

    public static void findViewByClassNameInFrame(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo != null) {
            int childCount = getChildCount(accessibilityNodeInfo);
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    if (str.equals(child.getClassName().toString())) {
                        list.add(child);
                    } else {
                        findViewByClassName(list, child, str);
                        child.recycle();
                    }
                }
            }
        }
    }

    public static int getChildCount(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        if (!accessibilityNodeInfo.getClassName().equals("android.widget.FrameLayout")) {
            return accessibilityNodeInfo.getChildCount();
        }
        int i = 0;
        while (i < Integer.MAX_VALUE) {
            try {
                if (accessibilityNodeInfo.getChild(i) == null) {
                    return i;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return i;
            }
        }
        return 0;
    }

    public static AccessibilityNodeInfo findViewByFirstEqualsContentDescription(AccessibilityService accessibilityService, String str) {
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return null;
        }
        AccessibilityNodeInfo findViewByFirstEqualsContentDescription = findViewByFirstEqualsContentDescription(rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return findViewByFirstEqualsContentDescription;
    }

    public static AccessibilityNodeInfo findViewByFirstEqualsContentDescription(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo == null) {
            return null;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child != null && child.isVisibleToUser()) {
                CharSequence contentDescription = child.getContentDescription();
                if (contentDescription != null && str.equals(contentDescription.toString())) {
                    return child;
                }
                AccessibilityNodeInfo findViewByFirstEqualsContentDescription = findViewByFirstEqualsContentDescription(child, str);
                child.recycle();
                if (findViewByFirstEqualsContentDescription != null) {
                    return findViewByFirstEqualsContentDescription;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByFirstContainsContentDescription(AccessibilityService accessibilityService, String str) {
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return null;
        }
        AccessibilityNodeInfo findViewByFirstContainsContentDescription = findViewByFirstContainsContentDescription(rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return findViewByFirstContainsContentDescription;
    }

    public static AccessibilityNodeInfo findViewByFirstContainsContentDescription(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo == null) {
            return null;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child != null) {
                CharSequence contentDescription = child.getContentDescription();
                if (contentDescription != null && contentDescription.toString().contains(str)) {
                    return child;
                }
                AccessibilityNodeInfo findViewByFirstContainsContentDescription = findViewByFirstContainsContentDescription(child, str);
                child.recycle();
                if (findViewByFirstContainsContentDescription != null) {
                    return findViewByFirstContainsContentDescription;
                }
            }
        }
        return null;
    }

    public static List<AccessibilityNodeInfo> findViewByContentDescription(AccessibilityService accessibilityService, String str) {
        ArrayList arrayList = new ArrayList();
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return arrayList;
        }
        findViewByContentDescription(arrayList, rootInActiveWindow, str);
        rootInActiveWindow.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsDescription(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        ArrayList arrayList = new ArrayList();
        if (accessibilityNodeInfo == null) {
            return arrayList;
        }
        findViewByEquialsDescription(arrayList, accessibilityNodeInfo, str);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static void findViewByContentDescription(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    CharSequence contentDescription = child.getContentDescription();
                    if (contentDescription == null || !contentDescription.toString().contains(str)) {
                        findViewByContentDescription(list, child, str);
                        child.recycle();
                    } else {
                        list.add(child);
                    }
                }
            }
        }
    }

    public static void findViewByEquialsDescription(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    CharSequence contentDescription = child.getContentDescription();
                    if (contentDescription == null || !contentDescription.toString().equals(str)) {
                        findViewByEquialsDescription(list, child, str);
                        child.recycle();
                    } else {
                        list.add(child);
                    }
                }
            }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByRect(AccessibilityService accessibilityService, Rect rect) {
        ArrayList arrayList = new ArrayList();
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return arrayList;
        }
        findViewByRect(arrayList, rootInActiveWindow, rect);
        rootInActiveWindow.recycle();
        return arrayList;
    }

    public static void findViewByRect(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo accessibilityNodeInfo, Rect rect) {
        if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    child.getBoundsInScreen(mRecycleRect);
                    if (mRecycleRect.contains(rect)) {
                        list.add(child);
                    } else {
                        findViewByRect(list, child, rect);
                        child.recycle();
                    }
                }
            }
        }
    }

    public static void recycleAccessibilityNodeInfo(List<AccessibilityNodeInfo> list) {
        if (!AccUtils.isEmptyArray(list)) {
            for (AccessibilityNodeInfo recycle : list) {
                recycle.recycle();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean findViewTextAndPasteContent(AccessibilityService accessibilityService, String text, String content) {
        AccessibilityNodeInfo rootNode = accessibilityService.getRootInActiveWindow();
        if (rootNode != null) {
            List<AccessibilityNodeInfo> editInfo = rootNode.findAccessibilityNodeInfosByText(text);
            if (editInfo != null && !editInfo.isEmpty()) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
                editInfo.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                return true;
            }
            return false;
        }
        return false;
    }


    public static void findViewByTextAndClick(AccessService service, String text) {


        List<AccessibilityNodeInfo> findViewByEqualsText;
        do {
            findViewByEqualsText = ServiceUtils.findViewByEqualsText(service, text);
        } while (AccUtils.isEmptyArray(findViewByEqualsText));

        ServiceUtils.clickView(findViewByEqualsText.get(0));

    }

    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }
}

