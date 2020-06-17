package com.water.melon.net.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.water.melon.R;
import com.water.melon.net.bean.SystemInfo;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;

import java.util.UUID;


public class MobileUtil {
    /**
     * 獲取手機信息
     */
    @SuppressLint({"CheckResult", "MissingPermission"})
    public static SystemInfo getSystemInfo(final Context context) {
        final SystemInfo info = new SystemInfo();
        final TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                info.imei = getAndroidId(context);
                info.imsi = getAndroidId(context);
                info.iccid = getAndroidId(context);
            } else {
//                info.imei = mTm.getDeviceId() == null ? getAndroidId(context) : mTm.getDeviceId();
                info.imsi = mTm.getSubscriberId() == null ? "" : mTm.getSubscriberId();
            }

            if (getUUID() == null || getUUID().trim().equals("")) {
                info.imei = getAndroidId(context);
            } else {
                info.imei = getUUID();
            }
//            info.imei = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_TEST_IME,"");
//            info.imsi = mTm.getSubscriberId() == null ? "" : mTm.getSubscriberId();
//            info.iccid = mTm.getSimSerialNumber() == null ? "" : mTm.getSimSerialNumber();

//            info.imei = info.f + "499";
        }

        info.net = GetNetworkType(context);
        info.appversion = getVersionCode(context) + "";
        info.mobile_model = Build.MODEL;
        info.mobile_apn = "";
        info.mobile_brand = Build.BRAND;
        info.appname = context.getString(R.string.app_name);
        info.mip = getLocalIpAddress(context);
        info.ua = Build.BRAND + "_" + Build.MANUFACTURER
                + "_" + Build.MODEL;
        info.os = Build.MODEL + "_" + Build.VERSION.SDK + "_"
                + Build.VERSION.RELEASE;
        info.os_version = Build.MODEL + "_" + Build.VERSION.SDK + "_"
                + Build.VERSION.RELEASE;
        info.android_id = getAndroidId(context);

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //info.mac = wifiInfo.getMacAddress();
        if (wifiInfo != null) {
            String tmpbt = wifiInfo.getMacAddress();
            if (tmpbt != null) {
                for (int i = 0; i < tmpbt.length(); i++) {
                    char c = tmpbt.charAt(i);
                    if (c != ':')
                        info.mac = info.mac + c;
                }
            }
        }


        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            info.net_mode = mNetworkInfo.getTypeName();
        }

        info.screen = context.getResources().getDisplayMetrics().widthPixels + "*" + context.getResources().getDisplayMetrics().heightPixels;

        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            info.packageName = packInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GsmCellLocation gsmCellLocation = null;
        try {
            gsmCellLocation = (GsmCellLocation) mTm.getCellLocation();
        } catch (Exception e) {
        }
        if (gsmCellLocation != null) {
            int lac1 = gsmCellLocation.getLac();
            int lac2 = gsmCellLocation.getCid();
            if (lac1 != -1) {
                info.lac = "" + lac1;
                if (lac2 != -1) {
                    info.lac += "#" + lac2;
                }
            }
        }
        if (info.lac == null) {
            info.lac = "0000#00";
        }
        return info;
    }

    public static String GetNetworkType(Context context) {
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();


                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }


        return strNetworkType;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("getPackageInfo", e.getLocalizedMessage());
        }
        return new PackageInfo();
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 獲取IP出錯了！！！請保證是WIFI,或者重新打開網絡!\n" + ex.getMessage();
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public static String getAndroidId(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }

    private static String getUUID() {
        String uuid = "";
        if (FileUtil.isImeiExist()) {
            uuid = FileUtil.ReadIMEIFile();
            LogUtil.e("getuuid", "save uuid = " + uuid);
        } else {
            uuid = UUID.randomUUID().toString();
            if (uuid != null && !uuid.trim().equals("")) {
                FileUtil.saveImei(uuid);
            }
            LogUtil.e("getuuid", "first uuid = " + uuid);
        }
        return uuid;
    }

}
