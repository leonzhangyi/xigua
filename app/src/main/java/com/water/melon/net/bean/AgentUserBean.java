package com.water.melon.net.bean;

import java.util.List;

public class AgentUserBean {
    private String start_date;
    private String end_date;
    private String keyword;

    private String count;//        count: 代理个数
    private List<UserInfo> list; //代理用户数

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<UserInfo> getList() {
        return list;
    }

    public void setList(List<UserInfo> list) {
        this.list = list;
    }

   public class UserInfo {
        private String id;//        id:用户id
        private String nickname;//        nickname:用户昵称
        private String username;//        username:用户名
        private String mobile;//        mobile:手机号码
        private String created_date;//        created_date:注册日期
        private String end_date;
        private String vip;
        private String group_id;


       public String getEnd_date() {
           return end_date;
       }

       public void setEnd_date(String end_date) {
           this.end_date = end_date;
       }

       public String getVip() {
           return vip;
       }

       public void setVip(String vip) {
           this.vip = vip;
       }

       public String getGroup_id() {
           return group_id;
       }

       public void setGroup_id(String group_id) {
           this.group_id = group_id;
       }

       public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
