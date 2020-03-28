package com.water.melon.net.bean;

public class AddVipBean {
    private String handle;    //否	before	操作	before/after
    private String type;    //是	1	卡类型	会员卡类型
    private String other_id;//是	1	被增加人id
    private String code;//是	112213	激活码

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
