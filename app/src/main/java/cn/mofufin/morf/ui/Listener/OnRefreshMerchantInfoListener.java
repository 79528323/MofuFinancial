package cn.mofufin.morf.ui.Listener;

import cn.mofufin.morf.ui.entity.User;

public interface OnRefreshMerchantInfoListener {
        void onSuccess(User.DataBean user);
        void onErrors(Throwable throwable);
        void onToast(String msg);
}
