package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.BalanceTransferStatusActivity;
import cn.mofufin.morf.ui.mine.MofuPlayActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityMerchantExchangeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 商品兑换
 */
public class MerchantExchangeActivity extends BaseActivity {
    private ActivityMerchantExchangeBinding binding;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_merchant_exchange);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        binding.setIsAgree(true);
        binding.setCount(1);
        Coupons.DataBean.ListBean details = getIntent().getParcelableExtra(IntentParam.MERCHANT_DETAIL_BEAN);
        binding.setType(details.gdGoodsType);
        binding.setBean(details);
        int coins = getIntent().getIntExtra(IntentParam.MERCHANT_DETAIL_COIN,0);
        binding.setCoin(coins);

        rxManager.onRxEvent(RxEvent.MERCHANT_EXCHANGE_FINISH)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });

        binding.exEdit.clearFocus();
        binding.exEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    v.requestFocus();
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                }else {
                    v.clearFocus();
                }
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

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>0){
            binding.setCount(Integer.valueOf(s.toString()));
        }else{
            binding.setCount(0);
        }
//        binding.exEdit.setSelection(String.valueOf(binding.getCount()).length());
    }

    //兑换
    public void exchange(View view){
        if (binding.getCount() <= 0){
            showTips(R.string.merchant_ex_tips);
            return;
        }else if (!binding.getIsAgree()){
            showTips("请同意相关规则");
            return;
        }

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
                                           MerchantExchangeActivity.this, BalanceTransferStatusActivity.class);
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

    public void AddorSub(View view){
        int count = Integer.valueOf((String) view.getTag());
        if (count<0){ // sub
            if (binding.getCount() == 0)//为0时无需操作
                return;

            binding.setCount(binding.getCount() + count);
        }else {
            // add
            binding.setCount(binding.getCount() + count);
        }
    }

    public void readCoinRule(View view){
        startActivity(new Intent(this, MofuPlayActivity.class));
    }

    public void agree(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

}
