package com.water.melon.ui.netresource;


import com.water.melon.base.net.BaseResponse;

import java.util.List;

public class NetResoutVideoInfo extends BaseResponse {
    private String _id;
    private String title;
    private String year;
    private String runtime;
    private String released;
    private Torrents torrents;
    private Images images;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public Torrents getTorrents() {
        return torrents;
    }

    public void setTorrents(Torrents torrents) {
        this.torrents = torrents;
    }

    public static class Torrents {
        private List<Zh> zh;

        public List<Zh> getZh() {
            return zh;
        }

        public void setZh(List<Zh> zh) {
            this.zh = zh;
        }
    }

    public static class Zh {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Images {
        private String poster;

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }
    }
}
