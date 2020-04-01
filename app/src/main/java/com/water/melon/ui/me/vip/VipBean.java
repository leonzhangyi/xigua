package com.water.melon.ui.me.vip;

import java.util.List;

public class VipBean {

    private String id;
    private String title;
    private String type;
    private String price;
    private String expiry;
    private List<PayMethod> method;
    private String order_id;
    private boolean isSelect;


//    [{
//        "id": 5,
//                "title": "体验卡",
//                "type": 1,
//                "price": "1.00",
//                "expiry": 1
//    }]


//
//    "method": [{
//        "id": 1,
//                "method": 0,
//                "title": "微信支付",
//                "icon": "http:\/\/38.27.103.12\/upload\/20200306\/u_290_TK268053.png"
//    }], "order_id": "P10EIXPF3QGZVIDLNM4X"
//}


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

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

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public List<PayMethod> getMethod() {
        return method;
    }

    public void setMethod(List<PayMethod> method) {
        this.method = method;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public class PayMethod{
        private String id;
        private String method;
        private String title;
        private String icon;
        private boolean isSelct;

//         "id": 1,
//                "method": 0,
//                "title": "微信支付",
//                "icon": "ht


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isSelct() {
            return isSelct;
        }

        public void setSelct(boolean selct) {
            isSelct = selct;
        }
    }
}
