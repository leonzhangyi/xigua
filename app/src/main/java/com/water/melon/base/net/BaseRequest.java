package com.water.melon.base.net;

import com.water.melon.utils.GsonUtil;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class BaseRequest<T> implements Serializable {
    public Integer page;//当前页面 number @mock=1
    public Integer limit;//条数 number @mock=10
    private T parameter;//多对象参数包裹对象
    public Integer rows;

    public BaseRequest() {
    }

    public BaseRequest(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
