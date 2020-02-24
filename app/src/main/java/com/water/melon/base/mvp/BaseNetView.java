package com.water.melon.base.mvp;

import com.water.melon.net.ErrorResponse;

public interface BaseNetView {
    //检测请求的的错误代码（这里只做app关键错误代码，如Token失效时同一统一处理）
    void catchApiSubscriberError(ErrorResponse error);
}
