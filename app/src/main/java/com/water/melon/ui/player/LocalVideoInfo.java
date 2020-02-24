package com.water.melon.ui.player;


import com.water.melon.base.net.BaseResponse;

import androidx.annotation.Nullable;

public class LocalVideoInfo extends BaseResponse {
    public static final String running_Star = "started";
    public static final String running_Stop = "stopped";
    public static final String running_finish = "finished";
    private String running;
    private String speed_info;
    private String info;
    private String percent;
    private String url;
    private String tid;
    private String vod_id;
    private String title;
    private String duration;
    private String size;
    private String videoImage;
    private String videoScreenImage;//截屏图
    private int videoImageByResour;//手动添加的默认图片
    private boolean isCheck;//编辑时是否选中
    private int tvPosition;//电视剧第几集
    private boolean isDone;//下载完成的视频

    public int getVideoImageByResour() {
        return videoImageByResour;
    }

    public void setVideoImageByResour(int videoImageByResour) {
        this.videoImageByResour = videoImageByResour;
    }

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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getTvPosition() {
        return tvPosition;
    }

    public void setTvPosition(int tvPosition) {
        this.tvPosition = tvPosition;
    }

    public static String getRunning_Star() {
        return running_Star;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

    public String getSpeed_info() {
        return speed_info;
    }

    public void setSpeed_info(String speed_info) {
        this.speed_info = speed_info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LocalVideoInfo videoInfo = (LocalVideoInfo) obj;
        if (url == null) {
            return videoInfo.url == null;
        } else return url.equals(videoInfo.getUrl());

    }
}
