package com.water.melon.constant;

public class XGConstant {
    public static String BaseUrl = "http://anan.client51.com:8088/v2/";
    public static String AdUrl = BaseUrl + "ad.asp";

    public static String userSeeVideoId = "";//用户在看的电影，切当时并未打开下载界面过

    //屏幕大小和状态栏
    public static int Screen_Width;
    public static int Screen_Height;
    public static int Screen_Status_Height;

    //传单个数据键
    public static String KEY_DATA = "KEY_DATA";
    //传单个数据键2
    public static String KEY_DATA_2 = "KEY_DATA_2";
    //传单个数据键3
    public static String KEY_DATA_3 = "KEY_DATA_3";

    //传列表数据键
    public static String KEY_LIST_DATA = "KEY_LIST_DATA";

    //是否初始化P2P
    public static boolean hasInitP2p = false;

    //    视频下载模块
//是否清除缓存，通知下载管理更新内存大小
    public static boolean showSDSizeByUserClear = false;

    //是否有新的版本
    public static boolean HasNewVersion;
}
