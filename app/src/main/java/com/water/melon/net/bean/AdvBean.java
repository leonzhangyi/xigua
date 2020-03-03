package com.water.melon.net.bean;

import com.water.melon.base.net.BaseResponse;

public class AdvBean extends BaseResponse {
    private String adno;//广告编号（后续统计接口需提供）
    private String title;//标题
    private String target;//跳转
    private String url; //图片链接
    private String desc;  //广告文字说明
    private String location_id; //广告位id
    private int handle; //跳转类型

    private String message; //内容
    private int type; //1 走马灯 ,2 弹框



    public String getAdno() {
        return adno;
    }

    public void setAdno(String adno) {
        this.adno = adno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
