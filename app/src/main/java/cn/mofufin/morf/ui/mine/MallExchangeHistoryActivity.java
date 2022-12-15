package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.mofufin.morf.adapter.ExChangeHistoryAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ExchangeHistory;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityMallExchangeHistoryBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 兑换历史
 */
public class MallExchangeHistoryActivity extends BaseActivity {
    private ActivityMallExchangeHistoryBinding binding;
    private ExChangeHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mall_exchange_history);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new ExChangeHistoryAdapter();
        binding.historyList.setLayoutManager(new LinearLayoutManager(this));
        binding.historyList.setAdapter(adapter);
        gethistory();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }


    //商户商品背包
    public void gethistory(){
        Subscription subscription = MallImAPI.getRecord(HttpParam.MERCHANT_EXCHANGE_HISTORY_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<ExchangeHistory>() {
                    @Override
                    public void call(ExchangeHistory exchangeHistory) {
                        if (exchangeHistory.bool){
                            adapter.refresh(exchangeHistory.data.list);
                        }

                        binding.setHasData(exchangeHistory.data.list.size()>0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable, true);
                    }
                });
        rxManager.add(subscription);
    }
}
