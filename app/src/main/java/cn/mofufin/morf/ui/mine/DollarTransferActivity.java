package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ExChanged;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.EditInputFilter;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityDollarTransferBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 余额转存
 */
public class DollarTransferActivity extends BaseActivity{
    private ActivityDollarTransferBinding binding;
    private double avAmount = 0.00;
    private double fee = 0.00d;//计算汇率
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dollar_transfer);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        type = getIntent().getIntExtra(IntentParam.TYPE,type);
        binding.setType(type);
        String amount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);
        avAmount = TextUtils.isEmpty(amount)?avAmount:Double.valueOf(amount);
        if (type ==0){
            binding.setTips(getString(R.string.dollar_tlse_7,DataUtils.numFormat(avAmount)));
        }else {
            binding.setTips(getString(R.string.dollar_tlse_6,DataUtils.numFormat(avAmount)));
        }

        String currentExchange=getIntent().getStringExtra(IntentParam.DOLLAR_BALANCE);
        binding.setExchange(currentExchange);

        fee = Double.valueOf(binding.getExchange())/100;

        EditInputFilter filter = new EditInputFilter();
        filter.setMAX_VALUE(avAmount);
        filter.setContext(this);
        InputFilter[] filters = {filter};
        binding.dollSum.setFilters(filters);

        rxManager.onRxEvent(RxEvent.DOLLAR_TRANSFER_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                DollarTransferActivity.this.finish();
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

    public void toBalance(){
        Observable<ExChanged> observable = null;
        if (type == 0){
            observable = UserImpAPI.getHKDToBalance(HttpParam.HKD_TO_BALANCE_KEY,HttpParam.OFFICE_CODE
                    , MerchanInfoDB.queryInfo().merchantCode,binding.dollSum.getText().toString());
        }else {
            observable = UserImpAPI.getUSDToBalance(HttpParam.USD_TO_BALANCE_KEY,HttpParam.OFFICE_CODE
                    , MerchanInfoDB.queryInfo().merchantCode,binding.dollSum.getText().toString());
        }

        Subscription subscription = observable.subscribeOn(Schedulers.io())
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
                            jumpIntent(binding.dollSum.getText().toString(),"");
                        }else
                            jumpIntent("",exChanged.getData().reason);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void trunAll(View view){
        binding.dollSum.setText(avAmount+"");
        binding.dollSum.setSelection(binding.dollSum.getText().length());
    }

    public void clean(View view){
        binding.dollSum.setText("");
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>0){
            binding.clear.setVisibility(View.VISIBLE);
            conversionAmount(s);
        }else{
            binding.clear.setVisibility(View.INVISIBLE);
            binding.setTips(getString(R.string.dollar_tlse_7,DataUtils.numFormat(avAmount)));
        }
    }

    public void onNext(View view){
        String str = binding.dollSum.getText().toString();

        if (!TextUtils.isEmpty(str)){
            if (str.substring(str.length()-1 ,str.length()).equals(".")){
                return;
            }else if (Double.valueOf(str)<=0){
                return;
            }

            toBalance();
        } else {
            showTips("请输入金额");
        }
    }


    //跳转
    public void jumpIntent(String str,String respone){
        Intent intent = new Intent(this,BalanceTransferStatusActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,str);
        intent.putExtra(IntentParam.BALANCE_RESPONE,respone);
        intent.putExtra(IntentParam.TYPE,type);
        startActivity(intent);
        finish();
    }

    //根据转金额换算
    public void conversionAmount(CharSequence s){
        if (s.length() >0){
            String str = s.toString();
            if (TextUtils.equals(str.substring(str.length()-1),".")){
                return;
            }
            double amount = Double.valueOf(s.toString());
            binding.setTips(getString(R.string.dollar_tlse_3,DataUtils.numFormat(amount*fee)));
        }
    }
}
