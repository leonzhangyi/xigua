package com.water.melon.net.bean;

import com.water.melon.net.BaseApiResultData;
import com.water.melon.utils.GsonUtil;

import java.lang.reflect.Type;

public class InitResultBean  {
    private String user_id;
    private String mobile;
    private String imei;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
