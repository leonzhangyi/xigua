package com.water.melon.ui.netresource;


import com.water.melon.base.net.BaseResponse;

import java.util.List;

public class NetResoutVideoInfo extends BaseResponse {
//    private String _id;
    private String id;
    private String title;
    private String year;
    private String runtime;
    private String released;
    private Torrents torrents;
    private Images images;
    private String banner;
    private String fanart;
    private String poster;
    private String synopsis;//简介

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getFanart() {
        return fanart;
    }

    public void setFanart(String fanart) {
        this.fanart = fanart;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
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


//    	"id": 266,
//                "title": "半个喜剧",
//                "year": "2019",
//                "synopsis": "　　《#驴得水#》导演周申、刘露新作《如果，我不是我》立项。影片同样由周申、刘露同名话剧改编而成，故事围绕两男三女的感情线索，及一对母子的亲情线索展开，讲述一群刚步入社会的年轻人，如何面对亲情、友情、爱情的冲突。\r\n　　三个自由浪漫的年轻人，过着各怀心思的人生：有人急着摆脱单身，有人想在结婚前放荡一番，有人想在大城市站稳脚跟。因为一次情感出轨，三人扭结成了一团“嬉笑怒骂”的乱麻。当各种价值观碰撞在一起，当一个人需要平衡亲情友情与爱情......他们慌乱的生活，就像是半个喜剧。",
//                "runtime": "111分钟",
//                "released": "2019-12-20(中国大陆)",
//                "fanart": "http:\/\/api.rinhome.com\/sites\/default\/files\/2020-01\/åä¸ªåå§.JPG",
//                "banner": "http:\/\/api.rinhome.com\/sites\/default\/files\/2020-01\/åä¸ªåå§.JPG",
//                "poster": "http:\/\/api.rinhome.com\/sites\/default\/files\/2020-01\/åä¸ªåå§.JPG"
}
