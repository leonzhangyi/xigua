package com.water.melon.net;

import android.text.TextUtils;
import android.util.Base64;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.water.melon.application.MyApplication;
import com.water.melon.base.mvp.BaseNetView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.net.bean.NetResourceRequest;
import com.water.melon.net.bean.TabBean;
import com.water.melon.net.utils.AESCipherforJiaMi;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.NetworkUtils;
import com.water.melon.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务器请求接口实现类
 */
public class ApiImp {
    public static final String TAG = "ApiImp";
    // 是否为线上环境
    public final static boolean IS_PRODUCTION_ENVIRONMENT = true;

    public static String APPBASEURL;//接口地址
    public static String Up_App;//更新版本

    static {
        if (IS_PRODUCTION_ENVIRONMENT) {
            //线上地址
            APPBASEURL = "http://api.rinhome.com";
        } else {
            //测试地址
            APPBASEURL = "http://api.rinhome.com";
        }
        Up_App = APPBASEURL + "/api/index";//更新版本
    }

    private static volatile ApiImp apiImp = null;
    private OkHttpClient okHttpClient;

    /**
     * 接口停止请求
     */
    private List<Disposable> apiDisposables;
    /**
     * 接口的服务类
     */
    private final ApiService apiService;

    public ApiImp() {
        final OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        //打印日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                LogUtil.e("RetrofitLog", "RetrofitLog = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mBuilder.addInterceptor(loggingInterceptor);
        //打印日志 End
        noSSLKey(mBuilder);
        //缓存
        File httpCacheDirectory = new File(FileUtil.HttpCache);//这里为了方便直接把文件放在了SD卡根目录的HttpCache中，一般放在context.getCacheDir()中
        int cacheSize = 20 * 1024 * 1024;//设置缓存文件大小为10M
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        okHttpClient = mBuilder
                .readTimeout(1, TimeUnit.MINUTES)//设置超时 3分钟
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时
                .writeTimeout(1, TimeUnit.MINUTES)//设置超时
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器
                .cache(cache)//把缓存添加进来
                .build();
        //用来请求非服务器的请求
        final Retrofit retrofit2 = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())  //TODO 解密操作再次进行  Retrofit 之加密和解密
//                .addConverterFactory(ConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(APPBASEURL)
                .build();
        //服务器请求接口
        apiService = retrofit2.create(ApiService.class);
        apiDisposables = new ArrayList<>();
    }

    //缓存拦截器
    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            boolean netAvailable = NetworkUtils.isConnected(MyApplication.getContext());
            Response response = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            if (netAvailable) {
                if (TextUtils.isEmpty(cacheControl) || "no-store".contains(cacheControl)) {
                    //响应头设置成无缓存
                    cacheControl = "no-store";
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        // 有网络时 设置缓存
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        // 无网络时
                        .header("Cache-Control", cacheControl)
                        .build();
            }
            return response;
        }
    };

    public static ApiImp getInstance() {
        ApiImp inst = apiImp;
        if (inst == null) {
            synchronized (ApiImp.class) {
                inst = apiImp;
                if (inst == null) {
                    inst = new ApiImp();
                    apiImp = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 信任所有证书
     *
     * @param mBuilder
     */
    void noSSLKey(OkHttpClient.Builder mBuilder) {
        try {
            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

            }};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            EmptyHostNameVerifier emptyHostNameVerifier = new EmptyHostNameVerifier();

            mBuilder.sslSocketFactory(sslContext.getSocketFactory());
            mBuilder.hostnameVerifier(emptyHostNameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class EmptyHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {

            return true;
        }
    }

    /**
     * 停止接口请求
     */
    public void stopRequst() {
        if (null != apiDisposables) {
            LogUtil.e("api停止接口请求", "停止接口数量===" + apiDisposables.size());
            for (Disposable apiDisposable : apiDisposables) {
                if (null != apiDisposable && !apiDisposable.isDisposed()) {
                    apiDisposable.dispose();
                    LogUtil.e("api停止接口请求", "停止接口");
                }
            }
            apiDisposables.clear();
            LogUtil.e("api停止接口请求", "停止接口请求完成");
        }
    }


    /**
     * 默认请求配置
     *
     * @param observable 请求的接口 apiService.testObservable(request)
     * @param callBack   结果监听 ApiSubscriber<TestResponse<TestPojo>> observer
     */
    private void baseObservableSetting(Observable observable, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, final IApiSubscriberCallBack callBack) {
        ApiSubscriber apiSubscriber = new ApiSubscriber(baseNetView, callBack);
        if (null != apiDisposables) {
            //添加请求句柄，可以停止请求
            apiDisposables.add(apiSubscriber.getDisposable());
        }
        observable.subscribeOn(Schedulers.io()) //在后台线程处理请求
                .observeOn(AndroidSchedulers.mainThread()) //请求结果到主线程
                .compose(lifecycleTransformer)
                .subscribe(apiSubscriber);
    }

    public static Map<String, String> getDefMap() {
        Map<String, String> params = new HashMap<>();
        params.put(NetConstant.XG_CLIENT, NetConstant.XG_ANDROID);
        params.put(NetConstant.XG_USER_ID, SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_USER_ID, NetConstant.XG_DEF_USER_ID));
//        params.put(NetConstant.XG_USER_ID, NetConstant.XG_DEF_USER_ID);
        params.put(NetConstant.XG_CHANNEL_ID, "1");
        params.put(NetConstant.XG_APP_VERSION, "1.0.0");

        return params;
    }

    public static Map<String, Object> setEcond(Map<String, Object> p) {
        Map<String, Object> map = new HashMap<>();
        String info = "";
        JSONObject obj = new JSONObject(p);
        String ecode = "";
        try {
            ecode = AESCipherforJiaMi.encrypt(obj.toString(), "utf-8");
            ecode = Base64.encodeToString(ecode.getBytes(), Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(NetConstant.XG_INFO, ecode);
//        info = "?info=" + ecode;
        return map;
    }

    public static String getEcond(Map<String, String> p) {
        String info = "";
        JSONObject obj = new JSONObject(p);
        String ecode = "";
        try {
            ecode = AESCipherforJiaMi.encrypt(obj.toString(), "utf-8");
            ecode = Base64.encodeToString(ecode.getBytes(), Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        info = "?info=" + ecode;
        return info;
    }

    //获取所有域名
    public void getDoMain(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack, String url) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(url + NetConstant.XG_APP_DOMAIN, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }
    //校验域名可行性
    public void doMainJudge(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack, String url) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(url + NetConstant.XG_APP_NETWORK, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }
    //初始化
    public void getInit(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(getDefMap());
        params.putAll(MyApplication.getSystemInfo().getMapInfo());
        LogUtil.e(TAG, "getInit.params() = " + params.toString());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_INIT, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //获取首页广告
    public void getHomeAdv(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_HOME_BANNEL, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //获取首页通知
    public void getAppNotice(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
//        params.put(NetConstant.XG_PAGE, request.getPage()+"");
//        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_APP_NOTICE, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //获取首页通知
    public void getHomeVIP(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_HOME_VIP, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //获取首页网络资源顶部类型
    public void getNetResourceBigTab(NetResourceRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData<List<TabBean>>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", request.getPage());
        params.put("limit", request.getLimit());
        baseObservableSetting(apiService.getNetResourceBigTab(APPBASEURL + "/api/term/fenlei", params), lifecycleTransformer, baseNetView, callBack);
    }

    //网络资源：获取视频列表
    public void getNetResourceList(GetVideosRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData<List<NetResoutVideoInfo>>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", request.getPage());
        params.put("limit", request.getLimit());
        params.put("category_id", request.getSmallTabId());
//        params.put("sort", request.getSort());
        baseObservableSetting(apiService.getNetResourceList(APPBASEURL + "/api/bt/list?genere_id&order&lang&keywords&sort", params), lifecycleTransformer, baseNetView, callBack);
    }


    //获取视频详情信息
    public void getNetVideoInfo(GetVideosRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData<SearchVideoInfoBean>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", request.getVideoId());
        baseObservableSetting(apiService.getNetVideoInfo(APPBASEURL + "/api/node/detail", params), lifecycleTransformer, baseNetView, callBack);
    }

    //获取福利顶部广告
    public void getWelAdv(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_WEL_HEADER, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //福利第一模块数据
    public void getWelPre(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_WEL_PRECINCT, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //福利中间广告
    public void getWelMidAdv(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_WEL_CENTER, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }

    //福利中间广告
    public void getWelBtnBean(BaseRequest request, LifecycleTransformer lifecycleTransformer, BaseNetView baseNetView, IApiSubscriberCallBack<BaseApiResultData> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put(NetConstant.XG_PAGE, request.getPage()+"");
        params.put(NetConstant.XG_ROWS, request.getRows()+"");
        params.putAll(getDefMap());
        baseObservableSetting(apiService.getDefResult(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_WEL_FOOTER, setEcond(params)), lifecycleTransformer, baseNetView, callBack);
    }
}
