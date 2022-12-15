package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Deposit;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.ChargeImpAPI;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.databinding.ActivityDepositBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 押金
 */
public class DepositActivity extends BaseActivity {

    private ActivityDepositBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_deposit);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        binding.setEnsureMoeny(0);
    }

    @Override
    protected void onResume() {
        queryMposPlan();
        super.onResume();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SpannableString spannableString = (SpannableString) msg.obj;
            binding.depositExp.setText(spannableString);
        }
    };


    /**
     * 查询MPOS计划
     */
    public void queryMposPlan(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = Observable.zip(ChargeImpAPI.getMposPlan(HttpParam.OFFICE_CODE, HttpParam.MPOS_PLAN_KEY),
                SubMissionImpAPI.refreshMerchantInfo(HttpParam.OFFICE_CODE, HttpParam.REFRESH_MERCHANT_APPKEY,bean.merchantPhone, bean.merchantCode),
                new Func2<BaseResponse<Deposit.DataBean>, BaseResponse<User.DataBean>, String>() {
                    @Override
                    public String call(BaseResponse<Deposit.DataBean> dataBeanBaseResponse, BaseResponse<User.DataBean> baseResponse) {
                        binding.setEnsureMoeny(baseResponse.data.getMerchantInfo().ensureMoeny);
                        binding.setEnsureMoneyPlanPhase(baseResponse.data.getMerchantInfo().ensureMoneyPlanPhase);

                        Deposit.DataBean.PlanBean planBean = dataBeanBaseResponse.data.plan;
                        binding.setPlan(planBean);
                        binding.setRefund(getString(R.string.deposit_10,
                                planBean.csFirsetCondition,
                                planBean.csFirstMonth,
                                planBean.csCentreCondition,
                                planBean.csCentreMonth,
                                planBean.csLastCondition,
                                planBean.csLastMonth));

                        if (binding.getEnsureMoneyPlanPhase()>0){
                            binding.setLinetips(getString(R.string.deposit_14,planBean.csClearingDate,planBean.csClearingTime));
                        }else {
                            binding.setLinetips(getString(R.string.deposit_5));
                        }

                        String secondTips = getString(R.string.deposit_9,planBean.csClearingDate,planBean.csClearingTime);
                        SpannableString spannableString = new SpannableString(secondTips);
                        spannableString.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(DepositActivity.this,R.color.deposit_red)),15,17,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(DepositActivity.this,R.color.deposit_red)),23,35,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(
                                        ContextCompat.getColor(DepositActivity.this,R.color.deposit_red)),44,65,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        handler.sendMessage(handler.obtainMessage(0,spannableString));
                        return "";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RequestTransformer(this))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });

        rxManager.add(subscription);
    }

    public void charge(View view){
        if (binding.getEnsureMoneyPlanPhase() <= 0){
            Intent intent = new Intent(this,RechargeToDepositActivity.class);
            startActivity(intent);
        }
    }
//
//    public void USDollar(View view){
//        Intent intent = new Intent(this,DollarActivity.class);
//        intent.putExtra(IntentParam.DOLLAR_TYPE,DollarActivity.TYPE_CURRENT_US);
//        intent.putExtra(IntentParam.DOLLAR_BALANCE,binding.getUSDollar());
//        startActivity(intent);
//    }

//    @Override
//    public void submit(View view) {
//        super.submit(view);
//    }

//    public void recharge(View view){
//        Intent intent = new Intent(this,RechargeToBalanceActivity.class);
//        startActivity(intent);
//    }
//
//    public void cashWithDrawal(View view){
//        Intent intent = new Intent(this,CashWithdrawalActivity.class);
//        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.getRMB());
//        startActivity(intent);
//    }

}
