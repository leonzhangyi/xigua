package com.water.melon.base.mvp;

public interface BaseFragment extends BaseNetView {
    void showLoadingDialog(boolean b);

    void showLoadingDialog(boolean b, String msg);
}
