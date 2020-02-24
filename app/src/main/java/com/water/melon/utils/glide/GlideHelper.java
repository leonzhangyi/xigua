package com.water.melon.utils.glide;


import com.sunfusheng.GlideImageView;

public class GlideHelper {

    //ImageView加载本地图片
    public static void showImage(GlideImageView view, String url, int defult) {
        view.load(url, defult);
    }

    //加载图片圆形无边框
    public static void showRoundImageNoStroke(GlideImageView view, String url, int defult) {
        view.loadCircle(url,defult);
    }

    public static void showRoundCorner(GlideImageView view, String url, int defult) {
        view.load(url, defult, 10);
    }

}
