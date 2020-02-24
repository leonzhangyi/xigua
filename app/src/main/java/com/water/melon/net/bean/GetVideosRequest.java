package com.water.melon.net.bean;


import com.water.melon.base.net.BaseRequest;

public class GetVideosRequest extends BaseRequest {
    private String videoId;
    private String bigTabId;
    private String smallTabId;
    private String searchWord;
    private String sort = "created";

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getBigTabId() {
        return bigTabId;
    }

    public void setBigTabId(String bigTabId) {
        this.bigTabId = bigTabId;
    }

    public String getSmallTabId() {
        return smallTabId;
    }

    public void setSmallTabId(String smallTabId) {
        this.smallTabId = smallTabId;
    }
}
