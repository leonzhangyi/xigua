package com.water.melon.net.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private TypeAdapter<T> adapter;
    private Gson gson;

    public DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public DecodeResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
//        return adapter.fromJson(Base64Utils.decode(responseBody.string()));
        return adapter.fromJson(AESCipherforJiaMi.desEncrypt(responseBody.string()));
    }
}
