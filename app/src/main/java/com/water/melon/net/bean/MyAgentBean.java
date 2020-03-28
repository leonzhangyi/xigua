package com.water.melon.net.bean;

import com.google.gson.annotations.SerializedName;
import com.water.melon.base.net.BaseResponse;

import java.util.List;
import java.util.Objects;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

public class MyAgentBean extends BaseResponse {
    private String proxy_name;//代理名称
    private String contacts;//联系人
    private String tel;//联系方式
    private String created_time;//开通日期
    private String count;// 代理个数

    private String id;//代理id
    private String buckle_u;//用户分成

    private String handle;//	否	before	事件	before/afeter
    private String prices;//	是	[{"type":1,"price":"11.00"},{"type":4,"price":"156.00"}]	价格	json数组
    private String wx;//	是	微信
    private List<Vips> vips;
    private boolean isDef;

    private List<Vips> def;


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

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public List<Vips> getVips() {
        return vips;
    }

    public boolean isDef() {
        return isDef;
    }

    public void setDef(boolean def) {
        isDef = def;
    }

    public void setVips(List<Vips> vips) {
        this.vips = vips;
    }

    public List<Vips> getDef() {
        return def;
    }

    public void setDef(List<Vips> def) {
        this.def = def;
    }

    public static class MyAgent {
        private String count;
        private List<MyAgentBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<MyAgentBean> getList() {
            return list;
        }

        public void setList(List<MyAgentBean> list) {
            this.list = list;
        }
    }

    public static class Vips extends BaseResponse {
        private String type;
        private String title;
        private String price;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

//        @Override
//        public boolean equals(@Nullable Object obj) {
//            return this.type.equals((((Vips) obj).getType()));
//        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vips vips = (Vips) o;
            return type.equals(vips.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }


        //        	"type": 1,
//                    "title": "体验卡",
//                    "price": "1.00"
    }
}
