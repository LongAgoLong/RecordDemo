package com.example.leo.recorddemo.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtil {

    private static Toast toast;

    public static void show(Context context,@NonNull String text) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(text);
        }
        toast.show();
    }

    public static void show(Context context, @StringRes int resId) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(resId);
        }
        toast.show();
    }

    /*
    * duration - Toast.LENGTH_LONG or Toast.LENGTH_SHORT
    * */
    public static void show(Context context,@NonNull String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            toast.setDuration(duration);
            toast.setText(text);
        }
        toast.show();
    }

    public static void show(Context context, @StringRes int resId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, duration);
        } else {
            toast.setDuration(duration);
            toast.setText(resId);
        }
        toast.show();
    }

    public static void cancel() {
        if (null != toast)
            toast.cancel();
    }

    public static void showBottom(Context context,@NonNull String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setText(text);
        toast.show();
    }
}
