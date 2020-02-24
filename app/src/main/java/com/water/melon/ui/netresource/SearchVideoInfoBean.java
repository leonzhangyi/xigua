package com.water.melon.ui.netresource;


import com.water.melon.base.net.BaseResponse;

import java.util.List;

public class SearchVideoInfoBean extends BaseResponse {
    private String id;
    private String thumbnail;
    private String title;
    private String score;
    private String mask;
    private String description;
    private String douban_id;
    private String original_name;
    private String duration;
    private List<Year> year;
    private List<Year> areas;
    private List<PeopleName> directors;
    private List<PeopleName> actors;
    private List<Torrents> btbo_downlist;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDouban_id() {
        return douban_id;
    }

    public void setDouban_id(String douban_id) {
        this.douban_id = douban_id;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public List<Year> getYear() {
        return year;
    }

    public void setYear(List<Year> year) {
        this.year = year;
    }

    public List<Year> getAreas() {
        return areas;
    }

    public void setAreas(List<Year> areas) {
        this.areas = areas;
    }

    public List<PeopleName> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PeopleName> directors) {
        this.directors = directors;
    }

    public List<PeopleName> getActors() {
        return actors;
    }

    public void setActors(List<PeopleName> actors) {
        this.actors = actors;
    }

    public List<Torrents> getBtbo_downlist() {
        return btbo_downlist;
    }

    public void setBtbo_downlist(List<Torrents> btbo_downlist) {
        this.btbo_downlist = btbo_downlist;
    }

    public static class Torrents extends BaseResponse {
        private String title;
        private String url;
        //本地辅助字段
        private boolean isCheck;//是否被选中

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }

    public static class Year extends BaseResponse {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class PeopleName extends BaseResponse {
        private String id;
        private String name;

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
    }
}
