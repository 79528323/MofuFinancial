package cn.mofufin.morf.ui.base;

import android.view.View;

/**
 * Created by yhz on 2016/7/25.
 */
public interface LoadingUI {
    void reload(View view);
    void loadSuccess();
    void loadFailure();
}
