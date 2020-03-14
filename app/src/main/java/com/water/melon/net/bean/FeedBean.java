package com.water.melon.net.bean;

public class FeedBean {
    private String id;//意见反馈id
    private String content;//内容
    private String images;//图片列表
    private String status;//0是为未回复,1已回复

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
}
