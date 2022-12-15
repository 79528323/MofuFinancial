package cn.mofufin.morf.contract;

import cn.mofufin.morf.ui.base.BasePresenter;
import cn.mofufin.morf.ui.base.BaseView;

public class MposContract {

    public interface View extends BaseView{
        void paySdkInitializationSuccess();
    }

    public interface Presenter extends BasePresenter{
        void InitializationSDK(String flag);
    }
}
