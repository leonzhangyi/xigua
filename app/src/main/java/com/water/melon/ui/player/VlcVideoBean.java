package com.water.melon.ui.player;

import android.text.TextUtils;

import com.water.melon.base.net.BaseResponse;


/**
 * 需要播放的视频信息
 */
public class VlcVideoBean extends BaseResponse {
    private String videoId;//视频Id 要回去评论，标签等网络数据要用
    private String videoName;
    private String videoImageUrl;
    private String videoPlayUrl;//XGUtil自动转换后的播放地址
    private String videoHttpUrl;//网络播放地址,播放前设置
    private String adurl;//广告地址
    private String lastPlayTime;//上次播放时间
    private int tvPosition;//记录播放第几集
    private int tid;//P2P下载视频Id
    private boolean isDone;//下载完成的视频
    private String videoScreenImage;//截屏图

    public String getVideoScreenImage() {
        return videoScreenImage;
    }

    public void setVideoScreenImage(String videoScreenImage) {
        this.videoScreenImage = videoScreenImage;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getTvPosition() {
        return tvPosition;
    }

    public void setTvPosition(int tvPosition) {
        this.tvPosition = tvPosition;
    }

    public String getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(String lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getAdurl() {
        return adurl;
    }

    public void setAdurl(String adurl) {
        this.adurl = adurl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public String getVideoHttpUrl() {
        return TextUtils.isEmpty(videoHttpUrl) ? "" : videoHttpUrl;
    }

    public void setVideoHttpUrl(String videoHttpUrl) {
        this.videoHttpUrl = videoHttpUrl;
    }

}
