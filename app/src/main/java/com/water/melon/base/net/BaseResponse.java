package com.water.melon.base.net;

import com.water.melon.utils.GsonUtil;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class BaseResponse implements Serializable {
    @NonNull
    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
