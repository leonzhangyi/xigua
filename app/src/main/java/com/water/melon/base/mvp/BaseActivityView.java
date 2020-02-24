package com.water.melon.base.mvp;

public interface BaseActivityView extends BaseNetView {
    void showLoadingDialog(boolean b);

    void showLoadingDialog(boolean b, String msg);

    boolean isActive();

}
