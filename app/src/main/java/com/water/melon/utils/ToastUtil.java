package com.water.melon.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.water.melon.application.MyApplication;


/**
 * 中间打印
 */
public class ToastUtil {
    private static Toast toast;

    public static void cancleToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg 信息
     */
    public static void showToastShort(String msg) {
        showToastShort(msg, Gravity.CENTER);
    }
    /**
     * Toast
     * 对外调用
     *
     * @param msg     信息
     * @param gravity Toast显示的位置
     */
    public static void showToastShort(String msg, int gravity) {
        showToastShort(MyApplication.getContext(), msg, gravity);
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg 信息
     */
    public static void showToastLong(String msg) {
        showToastLong(msg, Gravity.CENTER);
    }
    /**
     * Toast
     * 对外调用
     *
     * @param msg     信息
     * @param gravity Toast显示的位置
     */
    public static void showToastLong(String msg, int gravity) {
        showToastLong(MyApplication.getContext(), msg, gravity);
    }

    private static void showToastShort(Context context, String msg, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (gravity > -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }
    private static void showToastLong(Context context, String msg, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        if (gravity > -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }


}