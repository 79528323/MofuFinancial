package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.ruis.lib.adapter.TabsFragmentPagerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.fragment.MallActualFragment;
import cn.mofufin.morf.ui.fragment.MallVirtualFragment;
import cn.mofufin.morf.ui.mine.MallExchangeHistoryActivity;
import cn.mofufin.morf.ui.mine.MofuCoinActivity;
import cn.mofufin.morf.ui.mine.MofuPlayActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityMofuMallBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *摩富商城
 */
public class MofuMallActivity extends BaseActivity{
    private ActivityMofuMallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mofu_mall);
        binding.setHandlers(this);
        initView();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        startActivity(new Intent(this, MallExchangeHistoryActivity.class));
    }

    public void initView(){
        queryMerMobi();
        onSetupTabAdapter();
        rxManager.onRxEvent(RxEvent.REFRESH_MALL)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        queryMerMobi();
                    }
                });

    }

    private void onSetupTabAdapter() {
        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(), binding.mallTabs, binding.mallViewpager);
        tabsAdapter.setCacheFragment(false);

        Bundle bundle = new Bundle();
        bundle.putInt(IntentParam.CARD_TYPE,0);
        tabsAdapter.addTab(getString(R.string.mall_tabs_1), getString(R.string.mall_tabs_1), MallVirtualFragment.class, bundle);

        bundle = new Bundle();
        bundle.putInt(IntentParam.CARD_TYPE,1);
        tabsAdapter.addTab(getString(R.string.mall_tabs_2), getString(R.string.mall_tabs_2), MallActualFragment.class, bundle);
        tabsAdapter.notifyDataSetChanged();
    }

    //摩富币
    public void coins(View view){
        startActivity(new Intent(this, MofuCoinActivity.class));
    }


    //玩转摩富币
    public void playMofuCoin(View view){
        startActivity(new Intent(this, MofuPlayActivity.class));
    }

    public void queryMerMobi(){
        DataUtils.refreshMerchantInfo(getRxManager(), new OnRefreshMerchantInfoListener() {
            @Override
            public void onSuccess(User.DataBean user) {
                binding.setCoins(String.valueOf(user.getMerchantInfo().rubCurrenty));
            }

            @Override
            public void onErrors(Throwable throwable) {
                onError(throwable,true);
            }

            @Override
            public void onToast(String msg) {
                showTips(msg);
            }
        });

//        Subscription subscription = UserImpAPI.queryMerMobi(HttpParam.QUERY_MERMOBI,
//                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
//                new SimpleDateFormat("yyyy-MM").format(new Date()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .doOnCompleted(new Action0() {
//                    @Override
//                    public void call() {
//                        hiddenLoading();
//                    }
//                })
//                .subscribe(new Action1<Coin>() {
//                    @Override
//                    public void call(Coin coin) {
//                        if (coin.result_Code==0){
//                            binding.setCoins(String.valueOf(coin.currentMoBi));
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
    }

}
