package com.water.melon.utils.update;

import com.water.melon.base.net.BaseResponse;

public class AppVersionInfo extends BaseResponse {
    private String client;//更新平台
    private String version;//更新版本
    private String download;//下载地址
    private String md5;//完整校验
    private String desc;//跟新内容
    private int force;//是否强制更新 1强制 0不强制

//    {
//        "client": "android",
//            "version":"1.0.1",
//            "download": "http:\/\/38.27.103.12\/app\/version",
//            "md5": "1",
//            "force": 1,
//            "desc": "1.更新测试"
//    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }
}
