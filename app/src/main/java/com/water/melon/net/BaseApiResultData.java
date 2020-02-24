package com.water.melon.net;

import com.water.melon.base.net.BaseResponse;
import com.water.melon.net.utils.AESCipherforJiaMi;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public class BaseApiResultData<T> extends BaseResponse {
//    "ok": true, "code": 0, "suc": "请求成功", "time": "1582083792", "result":""
    /**
     * 返回状态
     */
    private boolean ok = false;
    /**
     * 返回标志
     */
    private Integer code;
    private String suc;
    private String err;
    private String time;
    private String result;



    /**
     * 数据
     */

    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getSuc() {
        return suc;
    }

    public void setSuc(String suc) {
        this.suc = suc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return AESCipherforJiaMi.desEncrypt(result);
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }




    /**
     * 返回信息
     */
    private String msg;
    /**
     * 数据总量
     */
    private int total;

    /**
     * 数据
     */
    private T info;
    private T list;

    private boolean success;


    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }


    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
