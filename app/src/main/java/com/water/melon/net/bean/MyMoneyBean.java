package com.water.melon.net.bean;

import android.widget.LinearLayout;

import java.util.List;

public class MyMoneyBean {
    private String handle;//	否	before	默认before	before/after
    private String type;//	否	user	默认proxy	proxy/user
    private String page;//	否	1	1
    private String rows;//	否	20	1
    private String start_date;//		是	2020-03-20	开始日期
    private String stop_date;//		是	2020-03-25	截止日期
    private String keyword;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStop_date() {
        return stop_date;
    }

    public void setStop_date(String stop_date) {
        this.stop_date = stop_date;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public static class BeforeBean {
        private String current;
        private String total;

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }

    public static class MyMoneyHistory {
        private String data;
        private String money;
        private String status;
        private String auth_status;
        private String msg;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAuth_status() {
            return auth_status;
        }

        public void setAuth_status(String auth_status) {
            this.auth_status = auth_status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        //        "date": "2020-03-28",
//                "money": "1.00",
//                "status": 0,
//                "auth_status": 0
//                "msg": "申请中"

    }


    public static class MyUserMoney {
        private String profit;
        private String count;
        private List<UserMoney> list;
//       "profit": 85,
//               "count": 4


        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<UserMoney> getList() {
            return list;
        }

        public void setList(List<UserMoney> list) {
            this.list = list;
        }
    }

    public static class  UserMoney{
     private String  date;
     private String  money;
     private String  profit;
     private String  username;
     private String  mobile;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        //     "date": "2020-03-27",
//                "money": "3000.00",
//                "profit": "25.00",
//                "username": "0146652503",
//                "mobile": "13255550042"
    }
}
