package com.water.melon.ui.game.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

public abstract class BaseBeanCallback extends Callback<BaseBean> {
    @Override
    public BaseBean parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        BaseBean user = new Gson().fromJson(string, BaseBean.class);
        return user;
    }


}
