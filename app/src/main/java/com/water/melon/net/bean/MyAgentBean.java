package com.water.melon.net.bean;

import com.water.melon.base.net.BaseResponse;

public class MyAgentBean extends BaseResponse {
    private String proxy_name;//代理名称
    private String contacts;//联系人
    private String tel;//联系方式
    private String created_time;//开通日期
    private String count;// 代理个数

    private String id;//代理id
    private String buckle_u;//用户分成


    public String getProxy_name() {
        return proxy_name;
    }

    public void setProxy_name(String proxy_name) {
        this.proxy_name = proxy_name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuckle_u() {
        return buckle_u;
    }

    public void setBuckle_u(String buckle_u) {
        this.buckle_u = buckle_u;
    }
}
