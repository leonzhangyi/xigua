package com.water.melon.net;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.mvp.BaseNetView;
import com.water.melon.utils.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author by PI
 * @date on 2018-05-27
 * describe 服务器返回数据处理类
 */

public class ApiSubscriber<T> implements Observer<T> {
    private IApiSubscriberCallBack<T> iApiSubscriberCallBack;
    private BaseNetView baseNetView;
    //取消订阅
    private Disposable disposable;

    public ApiSubscriber(BaseNetView baseNetView, IApiSubscriberCallBack<T> iApiSubscriberCallBack) {
        this.baseNetView = baseNetView;
        this.iApiSubscriberCallBack = iApiSubscriberCallBack;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onComplete() {
        iApiSubscriberCallBack.onCompleted();
    }


    @Override
    public void onError(final Throwable e) {
        LogUtil.e("Subscriber onError", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorResponse.setCode(httpException.code());
            switch (httpException.code()) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    //Token失效
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_unauthorized));
                    if (null != baseNetView) {
                        //返回给界面处理
                        baseNetView.catchApiSubscriberError(errorResponse);
                        return;
                    }
                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_not_find));
                    break;
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                default:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_link_exception));
                    break;
            }
//            //取出错误代码里面服务器返回的数据
//            ResponseBody body = httpException.response().errorBody();
//            try {
//                BaseApiResultData resultData = (BaseApiResultData) GsonUtil.toClass(body.string(), BaseApiResultData.class);
//                if (null != resultData) {
//                    errorResponse.setCode(httpException.code());
//                    errorResponse.setMessage(resultData.getMessage());
//                }
//            } catch (IOException e1) {
//                e1.printStackTrace();
//                errorResponse.setCode(-3);
//                errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_link_exception));
//            }

        } else if (e instanceof JsonParseException || e instanceof JSONException
                || e instanceof ParseException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_json));
        } else if (e instanceof ConnectException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
        } else if (e instanceof ResultErrorException) {
            //系统返回的异常
            String errorMes = e.getMessage();
            if (!TextUtils.isEmpty(errorMes)) {
                errorResponse.setMessage(errorMes);
            }
        } else if (e instanceof SocketTimeoutException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
        }
        iApiSubscriberCallBack.onError(errorResponse);
        onComplete();
    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseApiResultData) {
            BaseApiResultData baseApiResultData = (BaseApiResultData) t;
            //TODO ==1为测试环境
            if (baseApiResultData.getCode() == NetConstant.XG_HTTPS_SUC_CODE||baseApiResultData.getCode() == 1) {
                //成功
                iApiSubscriberCallBack.onNext(t);
            } else {
                //用户级错误
                ErrorResponse errorResponse = new ErrorResponse();
                String errorMessage = ErrorCodeMessageUtil.getMessageByErrorCode(baseApiResultData.getCode());
                errorResponse.setMessage(TextUtils.isEmpty(errorMessage) ? baseApiResultData.getSuc() : errorMessage);
                errorResponse.setCode(baseApiResultData.getCode());
                errorResponse.setErr(baseApiResultData.getErr());
                if (baseApiResultData.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED && null != baseNetView) {
                    //重新登录
                    baseNetView.catchApiSubscriberError(errorResponse);
                }
                iApiSubscriberCallBack.onError(errorResponse);
            }
        } else {
            //第三方接口回调自己处理
            iApiSubscriberCallBack.onNext(t);
        }

    }

}
