package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.mine.BalanceTransferStatusActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityMerchantDetailBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MerchantDetailActivity extends BaseActivity {
    private ActivityMerchantDetailBinding binding;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_merchant_detail);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        Coupons.DataBean.ListBean details = getIntent().getParcelableExtra(IntentParam.MERCHANT_DETAIL_BEAN);
        binding.setType(details.gdGoodsType);
        binding.setBean(details);
        queryMerMobi();

        int coins = getIntent().getIntExtra(IntentParam.MERCHANT_DETAIL_COIN,0);
        binding.setCoin(coins);

        rxManager.onRxEvent(RxEvent.MERCHANT_DETAIL_FINISH)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void exchange(View view){
//        Intent intent = new Intent(this, MerchantExchangeActivity.class);
//        intent.putExtra(IntentParam.MERCHANT_DETAIL_BEAN,binding.getBean());
//        intent.putExtra(IntentParam.MERCHANT_DETAIL_COIN,binding.getCoin());
//        startActivity(intent);


        Subscription subscription = MallImAPI.exchange(HttpParam.MERCHANDISE_EXCHANGE_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,binding.getBean().gdGoodsCode)
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
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                               @Override
                               public void call(BaseResponse<GeneralResponse.DataBean> response) {
                                   Intent intent = new Intent(
                                           MerchantDetailActivity.this, BalanceTransferStatusActivity.class);
                                   intent.putExtra(IntentParam.STATUS,response.bool);
                                   intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_MERCHANT_EXCHANGE);
                                   if (response.bool){
                                       intent.putExtra(IntentParam.TIPS,
                                               getString(R.string.merchant_ex_success,binding.getBean().getGdGoodsName()));
                                       intent.putExtra(IntentParam.TITLE,getString(R.string.merchant_ex_5));
                                   }else {
                                       intent.putExtra(IntentParam.TIPS,response.data.reason);
                                       intent.putExtra(IntentParam.TITLE,getString(R.string.merchant_ex_6));
                                   }
                                   startActivity(intent);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                onError(throwable,true);
                            }
                        });
        rxManager.add(subscription);
    }

//    public void exchange(View view)

    public void queryMerMobi(){
        Subscription subscription = UserImpAPI.queryMerMobi(HttpParam.QUERY_MERMOBI,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                new SimpleDateFormat("yyyy-MM").format(new Date()))
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
                .subscribe(new Action1<Coin>() {
                    @Override
                    public void call(Coin coin) {
                        if (coin.result_Code==0){
                            binding.setCoin(coin.currentMoBi);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

}
