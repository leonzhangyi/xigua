package com.water.melon.net.bean;


import com.water.melon.base.net.BaseResponse;

import java.util.List;

public class TabBean extends BaseResponse {
    private String id;
    private String name;
    private List<Sub> sub;

    public List<Sub> getSub() {
        return sub;
    }

    public void setSub(List<Sub> sub) {
        this.sub = sub;
    }

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

    public static class Sub extends BaseResponse {
        private String id;
        private String name;
        private String pid;

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

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }
    }
}
