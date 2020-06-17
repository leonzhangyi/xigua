package com.water.melon.ui.game.utils;

public class BaseBean {
    private boolean ok;
    private int code;
    private String suc;
    private String time;
    private String result;
    private String err = "err";
    private boolean success = false;


    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
        return result;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "ok=" + ok +
                ", code=" + code +
                ", suc='" + suc + '\'' +
                ", success='" + success + '\'' +
                ", time='" + time + '\'' +
                ", result='" + result + '\'' +
                ", err='" + err + '\'' +
                '}';
    }
}
