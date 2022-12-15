package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ExChanged;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityDollarBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DollarActivity extends BaseActivity {
    public static int TYPE_CURRENT_HK=0;
    public static int TYPE_CURRENT_US=1;
    private ActivityDollarBinding binding;
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dollar);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        type = getIntent().getIntExtra(IntentParam.DOLLAR_TYPE,type);
        binding.setType(type);
        String balance = getIntent().getStringExtra(IntentParam.DOLLAR_BALANCE);
        binding.setBalance(balance);

        exchange();
    }


    /**
     * 刷新汇率
     * @param view
     */
    public void refresh(View view){
        exchange();
    }


    public void exchange(){
        Observable<ExChanged> observable = null;
        if (type==0){
            observable = UserImpAPI.getHKDBuyCash(HttpParam.HKD_BUY_CASH_KEY,
                    HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode);
        }else {
            observable = UserImpAPI.getUSDBuyCash(HttpParam.USD_BUY_CASH_KEY,
                    HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode);
        }

        Subscription subscription = observable
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
                .subscribe(new Action1<ExChanged>() {
                    @Override
                    public void call(ExChanged exChanged) {
                        if (exChanged.isBool()){
                            binding.setExchange(exChanged.getData().spotPurchasePrice);
                        }else
                            showTips(exChanged.getData().reason);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });

        rxManager.add(subscription);
    }

    public void translate(View view){
        Intent intent = new Intent(this,DollarTransferActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.getBalance());
        intent.putExtra(IntentParam.DOLLAR_BALANCE,binding.getExchange());
        intent.putExtra(IntentParam.TYPE,type);
        startActivity(intent);
    }
}
