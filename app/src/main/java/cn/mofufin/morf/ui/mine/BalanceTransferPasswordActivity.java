package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.jungly.gridpasswordview.GridPasswordView;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.services.ChargeImpAPI;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityBalanceTransferPasswordBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 余额转存 输入密码
 */
public class BalanceTransferPasswordActivity extends BaseActivity{
    private ActivityBalanceTransferPasswordBinding binding;
    private String avAmount = "";
    private User.DataBean.CardInfoBean bean = null;
    private int type=0;

    //购买理财
    private ProductDetails.ProductDetailsBean productDetailsBean;
    private String mcbGoodsNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_transfer_password);
        binding.setHandlers(this);

        initView();
    }

    public void initView(){
        type = getIntent().getIntExtra(IntentParam.TYPE,type);
        if (type == 2){
            binding.title.setText(getString(R.string.recharge_deposit_title));
            binding.setTitle(getString(R.string.recharge_deposit_title));
        }else if (type == 3){
            binding.title.setText(getString(R.string.financial_tab_2));
            binding.setTitle(getString(R.string.product_pay_title));
            productDetailsBean = getIntent().getParcelableExtra(IntentParam.BEAN);
            mcbGoodsNumber = getIntent().getStringExtra(IntentParam.NUMBER);
        }else if (type == 4){
            binding.title.setText(getString(R.string.change_card_14));
            binding.setTitle(getString(R.string.change_card_14));
        }else if (type == 5){
            binding.title.setText(getString(R.string.withdraw_cash));
            binding.setTitle(getString(R.string.withdraw_cash));
        }
        avAmount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);
        if (!TextUtils.isEmpty(avAmount)){
            double amount = Double.valueOf(avAmount);
            avAmount = DataUtils.numFormat(amount);
        }

        Logger.e("avAmount=="+avAmount);
        binding.passView.setOnPasswordChangedListener(passwordChangedListener);
        bean = getIntent().getParcelableExtra(IntentParam.CARD_INFO_BEAN);

        rxManager.onRxEvent(RxEvent.EDITEXT_GET_FOCUS).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showKeyboard();
                    }
                },200);

            }
        });
        RxBus.getInstance().postEmpty(RxEvent.EDITEXT_GET_FOCUS);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private GridPasswordView.OnPasswordChangedListener passwordChangedListener =
            new GridPasswordView.OnPasswordChangedListener() {
        @Override
        public void onTextChanged(String psw) {

        }

        @Override
        public void onInputFinish(String psw) {
            if (type == 2){
                activeMposPlan();
            }else if (type == 3){
                productPayment();
            }else if (type == 4){
                mposUpdateCard();
            }else {
                if (bean==null){
                    transferBalance();
                }else {
                    merBalanceWithd();
                }
            }
        }
    };


    public void finishActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },800);
    }

    /**
     * 余额转存
     * @param str
     * @param respone
     */
    public void moveToStatus(String str,String respone){
        Intent intent = new Intent(BalanceTransferPasswordActivity.this,BalanceTransferStatusActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,str);
        intent.putExtra(IntentParam.BALANCE_RESPONE,respone);
        startActivity(intent);
        finishActivity();
    }

    /**
     * 余额提现申请
     */
    public void applySubmit(){
        MerchantBag.DataBean.ListBean bag = getIntent().getParcelableExtra(IntentParam.MERCHANT_BAG);
        Intent intent = new Intent(BalanceTransferPasswordActivity.this,CashWithdrawalApplyActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,avAmount);
        intent.putExtra(IntentParam.CARD_INFO_BEAN,bean);
        intent.putExtra(IntentParam.MERCHANT_BAG,bag);
        intent.putExtra(IntentParam.FEE,getIntent().getDoubleExtra(IntentParam.FEE,0));
        startActivity(intent);
        finishActivity();
    }

    /**
     * 理财
     * @param result
     */
    public void inverstMentPay(BaseResult result){
        Intent intent = new Intent(this,BalanceTransferStatusActivity.class);
        intent.putExtra(IntentParam.BEAN,result);
        intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_INVERSTMENT_PAYMENT);
        startActivity(intent);
        finishActivity();
    }


    @Override
    public void submit(View view) {
        super.submit(view);
        startActivity(new Intent(this,ModifyTransPasswordActivity.class));
    }


    /**
     * 激励金转至余额
     */
    public void transferBalance(){
        Subscription subscription = UserImpAPI.merRebateTrunBalance(HttpParam.TRUN_BALANCE_APPKEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(avAmount),
                binding.passView.getPassWord())
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code==0){
                            moveToStatus(avAmount,"");
                        }else{
                            moveToStatus("",baseResult.result_Msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        moveToStatus("",throwable.getMessage());
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }



    /**
     * 理财投资
     */
    public void productPayment(){
        Subscription subscription = ProductImpAPI.merInvest(HttpParam.OFFICE_CODE,HttpParam.MER_INVERST_KEY,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(productDetailsBean.fdNumber),
                binding.passView.getPassWord(),avAmount,
                mcbGoodsNumber)
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        inverstMentPay(baseResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    /**
     * 余额提现
     */
    public void merBalanceWithd(){
        MerchantBag.DataBean.ListBean merchantbean = getIntent().getParcelableExtra(IntentParam.MERCHANT_BAG);
        Subscription subscription = UserImpAPI.merBalanceWithd(HttpParam.BALANCE_WITHDARW_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, avAmount,binding.passView.getPassWord(),
                bean.cardCode,merchantbean!=null?merchantbean.mcbGoodsNumber:"")
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code == 0){
                            applySubmit();
                        }else {
                            showTips(baseResult.result_Msg);
                            binding.passView.clearPassword();
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


    /**
     * 更换PMPOS结算卡
     */
    public void mposUpdateCard(){
        User.DataBean.CardInfoBean cardInfoBean = getIntent().getParcelableExtra(IntentParam.BEAN);
        Subscription subscription = BankImpAPI.mposUpdateCard(HttpParam.OFFICE_CODE,HttpParam.MPOS_UPDATE_CARD_KEY,
                MerchanInfoDB.queryInfo().merchantCode, binding.passView.getPassWord(),cardInfoBean.cardCode)
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        mposUpdateSuccess(baseResult.result_Code==0,baseResult.result_Msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    /**
     * 激活Mpos(充值保证金)
     */
    public void activeMposPlan(){
        String addressNumber = getIntent().getStringExtra(IntentParam.NUMBER);
        Subscription subscription = ChargeImpAPI.activeMposPlan(HttpParam.OFFICE_CODE,HttpParam.ACTIVE_MPOS_PLAN_KEY,
                MerchanInfoDB.queryInfo().merchantCode,binding.passView.getPassWord(),addressNumber)
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
                    public void call(BaseResponse<GeneralResponse.DataBean> dataBeanBaseResponse) {
                        mposChargeSubmit(dataBeanBaseResponse.bool,dataBeanBaseResponse.data.reason);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void mposChargeSubmit(boolean isOk,String respone){
        Intent intent = new Intent(BalanceTransferPasswordActivity.this,BalanceTransferStatusActivity.class);
        intent.putExtra(IntentParam.TYPE,2);
        intent.putExtra(IntentParam.BALANCE_RESPONE,respone);
        intent.putExtra(IntentParam.CHARGE_DEPOSIT_STATUS,isOk);
        startActivity(intent);
        finishActivity();
    }


    public void mposUpdateSuccess(boolean isOk ,String respone){
        Intent intent = new Intent(BalanceTransferPasswordActivity.this,BalanceTransferStatusActivity.class);
        intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_MPOS_UPDATE_CARD);
        intent.putExtra(IntentParam.BALANCE_RESPONE,respone);
        intent.putExtra(IntentParam.STATUS,isOk);
        startActivity(intent);
        finishActivity();
    }


}
