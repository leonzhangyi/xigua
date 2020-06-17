package com.water.melon.ui.game.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;
import java.io.IOException;

import okhttp3.Response;


public abstract class GamePlayCallBack extends Callback<GameBean> {
    @Override
    public GameBean parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        GameBean user = new Gson().fromJson(string, GameBean.class);
        return user;
    }


}