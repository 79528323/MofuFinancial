package cn.mofufin.morf.presenter;

import cc.ruis.lib.event.RxManager;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.services.ReceiVablesImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.contract.OrderContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OrderPresenter implements OrderContract.Presenter {

    private OrderContract.View view;
    private RxManager rxManager;


    public OrderPresenter(OrderContract.View view) {
        this.view = view;
        this.rxManager = view.getRxManager();
    }

    @Override
    public void refreshData(String orderBegin, String orderEnd, int type) {
        Subscription subscription = ReceiVablesImpAPI.QueryOrders(
                HttpParam.OFFICE_CODE,
                HttpParam.QUERY_ORDERS_APPKEY ,
                orderBegin,
                orderEnd,
                type,
                MerchanInfoDB.queryInfo().merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        view.showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        view.hiddenLoading();
                    }
                })
                .subscribe(new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        if (order.result_Code > 0){
                            view.onDataFail(order.result_Msg);
                        }else {
                            view.onDataSuccess(order);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }
}
