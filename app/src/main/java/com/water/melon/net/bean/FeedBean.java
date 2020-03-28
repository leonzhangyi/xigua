package com.water.melon.net.bean;

import java.io.Serializable;
import java.util.List;

public class FeedBean {
    private String id;//意见反馈id
    private String content;//内容
    private String images;//图片列表
    private String status;//0是为未回复,1已回复

    private String count;
    private List<FeekItemBean> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<FeekItemBean> getList() {
        return list;
    }

    public void setList(List<FeekItemBean> list) {
        this.list = list;
    }

    public static class FeekItemBean implements Serializable {
        private String id;
        private String user_id;
        private String content;
        private String answer;
        private String manger_id;
        private String read;
        private String images;
        private String status;
        private String created_time;
        private String updated_time;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getManger_id() {
            return manger_id;
        }

        public void setManger_id(String manger_id) {
            this.manger_id = manger_id;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
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

        public String getUpdated_time() {
            return updated_time;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }

        //         "id": 129,
//                 "user_id": 15963,
//                 "content": "本次为测试数据",
//                 "answer": "",
//                 "manger_id": 0,
//                 "read": 0,
//                 "images": "",
//                 "status": 0,
//                 "created_time": "2020-03-16 17:52:25",
//                 "updated_time": "2020-03-16 17:52:25"

    }


}
