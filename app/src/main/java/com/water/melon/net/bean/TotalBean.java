package com.water.melon.net.bean;

import com.water.melon.base.net.BaseResponse;

public class TotalBean extends BaseResponse {
    private String date;//:日期
    private String add;//:新增数
    private String live;//:活跃数（不包含新增用户）
    private String total;//:累计用户

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
