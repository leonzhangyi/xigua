package com.water.melon.net.bean;

import com.water.melon.base.net.BaseResponse;

public class RoadBean extends BaseResponse {
    private String id;
    private String name;
    private String url;
    private boolean isSelct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelct() {
        return isSelct;
    }

    public void setSelct(boolean selct) {
        isSelct = selct;
    }

    //    "id": 8,
//            "name": "高清蓝光",
//            "url": "https:\/\/wawa.ha123.club\/bd9\/?url="
}
