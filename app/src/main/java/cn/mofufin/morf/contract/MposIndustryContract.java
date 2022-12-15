package cn.mofufin.morf.contract;

import com.hope.paysdk.framework.beans.Industry;

import java.util.List;

import cn.mofufin.morf.ui.base.BasePresenter;
import cn.mofufin.morf.ui.base.BaseView;

public class MposIndustryContract {

    public interface View extends BaseView{
        void refresh(List<Industry> industryList);
//        void paySdkInitializationSuccess();
    }

    public interface Presenter extends BasePresenter{
        void refresh(String... params);
//        void InitializationSDK();

    }
}
