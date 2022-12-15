package cn.mofufin.morf.contract;

import cn.mofufin.morf.ui.base.BasePresenter;
import cn.mofufin.morf.ui.base.BaseView;
import cn.mofufin.morf.ui.entity.Order;

public class OrderContract {

    public interface View extends BaseView{
        void onDataSuccess(Order order);

        void onDataFail(String tips);
    }

    public interface Presenter extends BasePresenter{
        void refreshData(String orderBegin,String orderEnd,int type);
    }
}
