package com.water.melon.net;


import com.water.melon.net.bean.TabBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.ui.netresource.SearchVideoInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author by PI
 * @date on 2018-05-27
 * describe 服务器请求接口
 * @Headers("Cache-Control:public , max-age=600") 设置要缓存的接口 秒/单位
 */

public interface ApiService {
    //获取首页网络资源顶部类型
    @GET()
    Observable<BaseApiResultData> getDefResult(@Url String url, @QueryMap Map<String, Object> params);

    //获取首页网络资源顶部类型
    @GET()
    Observable<BaseApiResultData<List<TabBean>>> getNetResourceBigTab(@Url String url, @QueryMap Map<String, Object> params);

    //获取首页网络资源
    @GET()
    Observable<BaseApiResultData<List<NetResoutVideoInfo>>> getNetResourceList(@Url String url, @QueryMap Map<String, Object> params);

    //获取视频详情信息
    @GET()
    Observable<BaseApiResultData<SearchVideoInfoBean>> getNetVideoInfo(@Url String url, @QueryMap Map<String, Object> params);


    //获取首页网络资源顶部类型
    @GET()
    Observable<GameBean> getGameToken(@Url String url, @QueryMap Map<String, Object> params);
}

