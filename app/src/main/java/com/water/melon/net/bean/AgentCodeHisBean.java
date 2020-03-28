package com.water.melon.net.bean;

import java.util.List;

public class AgentCodeHisBean {
    private String status;    //否	1	使用状态	1已使用/-1未使用(全部)/2失效/0正常
    private String handle;    //否	before	操作	before/after
    private String type;//是	1	卡类型
    private String keyword;//是	111	关键字
    private String page;//是	1	页码	默认1
    private String rows;//是	20	数量	默认20

    private List<CodeBean> list;
    private String count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public  List<CodeBean> getList() {
        return list;
    }

    public void setList(List<CodeBean> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public static class CodeBean{

        private String date;
        private String code;
        private String mobile;
        private String username;
        private String type;
        private String end_date;
        private String end_time;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getType() {
            return type;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

//        	"date": "2020-03-11",
//                    "code": "0572198677",
//                    "type": "月卡",
//                    "username": "",
//                    "mobile": "15212345677"

        //        //已使用返回
//        date:兑换日期
//        code:激活码
//        mobile:手机号码
//        username:用户名
//        type:卡类型
////未使用返回
//        end_date:截至日期
//        code:激活码
//        type:卡类型
    }

    public static class Types {
        private String type;
        private String title;

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
    }
}
