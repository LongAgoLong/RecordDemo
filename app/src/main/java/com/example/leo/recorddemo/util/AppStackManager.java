package com.example.leo.recorddemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import com.example.leo.recorddemo.App;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by NyatoLEO on 2017/2/4.
 * 管理栈中的activity
 */

public class AppStackManager {

    public Stack<Activity> mStack;
    public ArrayList<Activity> mActivities;
    private static AppStackManager mInstance;

    public static AppStackManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppStackManager();
        }
        return mInstance;
    }

    /**
     * 添加Activity到栈中
     */
    public void addActivity(Activity activity) {
        if (null == mStack) {
            mStack = new Stack<>();
        }
        mStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity getCurrentActivity() {
        return mStack.lastElement();
    }

    /**
     * 结束指定的Activity
     */
    public void killSingleActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        mStack.remove(activity);
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public void killSingleActivity(Class<?> cls) {
        try {
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 结束指定Activity外其余Activity
     * */
    public void keepOnlyActivity(Class<?> cls) {
        try {
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (!activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keepOnlyActivity(Activity activity) {
        if (null == activity) {
            return;
        }
        try {
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity act = iterator.next();
                if (act != activity) {
                    iterator.remove();
                    act.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束多个Activity
     */
    public void killMoreActivity(ArrayList<Class<?>> activities) {
        if (mActivities == null) {
            mActivities = new ArrayList<>();
        }
        for (Class<?> cls : activities) {
            Activity activity = isHere(cls);
            if (activity != null) {
                mActivities.add(activity);
            }
        }
        mStack.removeAll(mActivities);
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 是否在栈中
     */
    public Activity isHere(Class<?> cls) {
        for (Activity activity : mStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有
     */
    public void killAllActivity() {
        for (Activity activity : mStack) {
            activity.finish();
        }
        mStack.clear();
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        killAllActivity();
        System.exit(0);
    }

    /**
     * 安装APK
     */
    public void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /*
     * 通知Media扫描
     * */
    public void notifyMediaScan(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        App.getContext().sendBroadcast(intent);
    }

    /**
     * 权限检查
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return 返回检查结果
     */
    public boolean checkPermissions(Context context, String... permissions) {
        if (null == permissions || permissions.length == 0)
            return true;
        boolean isGranted = true;
        for (String permission : permissions) {
            isGranted = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (!isGranted)
                break;
        }
        return isGranted;
    }
}
