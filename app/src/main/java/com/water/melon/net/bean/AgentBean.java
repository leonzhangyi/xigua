package com.water.melon.net.bean;

import com.water.melon.base.net.BaseResponse;

public class AgentBean extends BaseResponse {

    private String id;
    private String title;
    private String type;
    private String price;
    private String user_nums;
    private String proxy_nums;
    private String contacts;
    private String tel;
    private String profit;
    private String status;
    private String created_time;
    private String money;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_nums() {
        return user_nums;
    }

    public void setUser_nums(String user_nums) {
        this.user_nums = user_nums;
    }

    public String getProxy_nums() {
        return proxy_nums;
    }

    public void setProxy_nums(String proxy_nums) {
        this.proxy_nums = proxy_nums;
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    //        "id": 8,
//            "title": "代理商",
//            "type": 7,
//            "price": "0.00",
//            "user_nums": 0,
//            "proxy_nums": 0,
//            "contacts": "",
//            "tel": "",
//            "profit": 0,
//            "status": 0
}
