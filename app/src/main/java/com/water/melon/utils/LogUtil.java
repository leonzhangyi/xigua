package com.water.melon.utils;

import android.text.TextUtils;
import android.util.Log;


public class LogUtil {
    private static boolean SHOW_LOG = true;
//    private static boolean SHOW_LOG = true;

    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (SHOW_LOG) {
            if (msg.length() > 200 * 1024) {
                Log.e(tag, "日志太大了 size=" + msg.length());
                return;
            }
            int showLength = 500;
            if (msg.length() > showLength) {
                String show = msg.substring(0, showLength);
                Log.e(tag, show);
                /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
                if ((msg.length() - showLength) > showLength) {
                    String partLog = msg.substring(showLength, msg.length());
                    e(tag, partLog);
                } else {
                    String printLog = msg.substring(showLength, msg.length());
                    Log.e(tag, printLog);
                }

            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (SHOW_LOG) {
            Log.w(tag, msg);
        }
    }

    /**
     * 如果不是正式地址--显示Log
     *
     * @param tag
     * @param msg
     */
    public static void showLog(String tag, String msg) {
        if (SHOW_LOG) {
            Log.e(tag, msg);
        }
    }
}
