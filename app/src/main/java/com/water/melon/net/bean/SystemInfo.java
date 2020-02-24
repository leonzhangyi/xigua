package com.water.melon.net.bean;


import java.util.HashMap;
import java.util.Map;

/**
 * 手机信息
 */
public class SystemInfo {
    public String mobile_model = ""; // 手機品牌
    public String os_version = ""; //版本號
    public String net_mode = ""; //網絡類型
    public String packageName = ""; //包名
    public String appname = ""; //應用名
    public String imsi = ""; //IMSI
    public String imei = ""; //IMEI
    public String screen = ""; //屏幕分辨率
    public String iccid = ""; //ICCID
    public String ua = ""; //ua
    public String lac = ""; //基站信息
    public String mac = ""; //MAC地址
    public String mip = ""; //手機IP
    public String appversion = "";//app版本號
    public String mobile_apn = "";
    public String mobile_brand = "";
    public String net = "";
    public String os = "";
    public String android_id = "";


    @Override
    public String toString() {
        return "SystemInfo{" +
                "mobile_model='" + mobile_model + '\'' +
                ", os_version='" + os_version + '\'' +
                ", net_mode='" + net_mode + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appname='" + appname + '\'' +
                ", imsi='" + imsi + '\'' +
                ", imei='" + imei + '\'' +
                ", screen='" + screen + '\'' +
                ", iccid='" + iccid + '\'' +
                ", ua='" + ua + '\'' +
                ", lac='" + lac + '\'' +
                ", mac='" + mac + '\'' +
                ", mip='" + mip + '\'' +
                ", appversion='" + appversion + '\'' +
                ", mobile_apn='" + mobile_apn + '\'' +
                ", mobile_brand='" + mobile_brand + '\'' +
                ", net='" + net + '\'' +
                ", os='" + os + '\'' +
                ", android_id='" + android_id + '\'' +
                '}';
    }

    public Map<String, String> getMapInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile_model", mobile_model);
        map.put("os_version", os_version);
        map.put("net_mode", net_mode);
        map.put("packageName", packageName);
        map.put("appname", appname);
        map.put("imsi", imsi);
        map.put("imei", imei);
        map.put("screen", screen);
        map.put("iccid", iccid);
        map.put("ua", ua);
        map.put("lac", lac);
        map.put("mac", mac);
        map.put("mip", mip);
        map.put("net", net);
        map.put("appversion", appversion);
        map.put("mobile_apn", mobile_apn);
        map.put("mobile_brand", mobile_brand);
        map.put("os", os);
        map.put("android_id", android_id);
        map.put("client", "android");
        return map;
    }
}
