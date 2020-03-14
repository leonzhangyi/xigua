package com.water.melon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.water.melon.application.MyApplication;

public class SharedPreferencesUtil {
    /**
     * SharedPreferencesUtil 名称
     */
    private static final String SP_NAME = "water_melon_sharepreferences";

    /**
     * 获取域名
     */
    public static final String get_domain = "water_melon_domain";

    /**
     * 片库列表
     */
    public static final String KEY_Big_Tab = "water_melon_net_resource_header";

    /**
     * 片库小类对应的第一个数据列表
     */
    public static final String KEY_Small_Tab_List = "KEY_Small_Tab_List";


    /**
     * 第一次启动app
     */
    public static final String KEY_FIRST_START = "do_first_start";

    /**
     * 每次校验的域名
     */
    public static final String XG_DOMAIN = "xg_domain";


    /**
     * 用户ID
     */
    public static final String XG_USER_ID = "xg_user_id";
    /**
     * 手机号
     */
    public static final String XG_PHONE = "xg_phone";


    /**
     * 电视剧上一次播到哪一集，主要在网络资源进来点击
     */
    public static final String KEY_Tv_PLAY_RECORD_POSITION = "KEY_Tv_PLAY_RECORD_POSITION";

    /**
     * 播放记录
     */
    public static final String KEY_PLAY_RECORD_INFO = "KEY_PLAY_RECORD_INFO";
    /**
     * 正在下载
     */
    public static final String KEY_Dowanload_off= "KEY_Dowanload_off";
    /**
     * 已下载完
     */
    public static final String KEY_Dowanload_done= "KEY_Dowanload_done";
    /**
     * 播放历史
     */
    public static final String KEY_Play_Record= "KEY_Play_Record";


    /**
     * 版本检查更新一天一次提醒
     */
    public static final String KEY_CHECK_APP_VERSION = "KEY_CHECK_APP_VERSION";

    /**
     * 是否有强制版本
     */
    public static final String KEY_CHECK_APP_VERSION_FORCS = "KEY_CHECK_APP_VERSION_FORCS";


    public static final String KEY_WATER_USER_INFO = "KEY_WATER_USER_INFO";  //所有个人中心数据
    public static final String KEY_WATER_ALL_ROADS = "KEY_WATER_ALL_ROADS";//所有可切换路线
    public static final String KEY_WATER_WEIXIN = "KEY_WATER_WEIXIN";//微信
    public static final String KEY_WATER_QQ = "KEY_WATER_QQ";//QQ
    public static final String KEY_WATER_HELP = "KEY_WATER_HELP";//帮助中心




    public static final String KEY_WATER_TEST_IME = "KEY_WATER_TEXT_IMEI";

    private static SharedPreferencesUtil instance = new SharedPreferencesUtil();

    public SharedPreferencesUtil() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreferencesUtil();
        }
    }

    public static SharedPreferencesUtil getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private SharedPreferences getSp() {
        return MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除单个数据
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.remove(key);
                return e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除所有本地数据
     *
     * @return
     */
    public boolean clear() {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                return e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
