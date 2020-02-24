package com.water.melon.net;

import android.text.TextUtils;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.net.BaseResponse;

public class ErrorResponse extends BaseResponse {
    /**
     *  -1,-2系统返回错误
     *  -3,其他错误
     */
    private int code;
    private String message;
    private String url;
    private String err;

    public ErrorResponse() {

    }

    public ErrorResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return TextUtils.isEmpty(message) ? MyApplication.getStringByResId(R.string.net_error_for_link_exception) : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
