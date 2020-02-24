package com.water.melon.utils;

/**
 * 防止用户快速点击
 * 创建者： feifan.pi 在 2017/5/16.
 */

public class ClickTooQucik {
    private static long lastClickTime = 0;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 自定义操作时间
     *
     * @param mills
     * @return
     */
    public synchronized static boolean isFastClick(int mills) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < mills) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
