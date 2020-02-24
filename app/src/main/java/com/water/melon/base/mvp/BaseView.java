package com.water.melon.base.mvp;

public interface BaseView<T> extends BaseActivityView {
    void initView();

    void setPresenter(T presenter);
}
