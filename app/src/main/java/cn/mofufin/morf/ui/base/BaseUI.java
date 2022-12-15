package cn.mofufin.morf.ui.base;

import androidx.annotation.StringRes;

import cc.ruis.lib.event.RxManager;

/**
 * Created by yhz on 2016/4/20.
 */
public interface BaseUI {
    void showLoading();
    void hiddenLoading();
    void showTips(@StringRes int tipRes);
    void showTips(String tip);
    void showTips(String tip,int duration);
    void onError(Throwable throwable, boolean show);
    RxManager getRxManager();
}