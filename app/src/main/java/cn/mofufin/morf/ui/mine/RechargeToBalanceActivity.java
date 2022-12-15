package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.H5PayActivity;
import cn.mofufin.morf.ui.LockMoneyExplainActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.entity.WxPays;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityRechargeToBalanceBinding;
import cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 充值
 */
public class RechargeToBalanceActivity extends BaseActivity{
    private ActivityRechargeToBalanceBinding binding;
    private double avAmount = 0.00;
    private int tag=0;
    private static final int ALIPAY_FLAG = 1;
    private static final int WXPAY_FLAG = 2;
    private static final int ALIPAY_SCAN_FLAG = 3;
    private static final int CHARGE_CREDIT_FLAG = 4;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ALIPAY_FLAG:
                    String resultStatus="" , result="" ,memo="";
                    Map<String, String> rawResult = (Map<String, String>) msg.obj;
                    if (rawResult == null) {
                        return;
                    }

                    for (String key : rawResult.keySet()) {
                        if (TextUtils.equals(key, "resultStatus")) {
                            resultStatus = rawResult.get(key);
                        }
                    }

                    Logger.e("resultStatus="+resultStatus);
                    Logger.e("result="+result);
                    Logger.e("memo="+memo);

                    if (TextUtils.equals(resultStatus, "9000")) {
//                        showTips("支付成功");
                        updateMerchantInfo();
                        RxBus.getInstance().postEmpty(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH);
                        finish();
                    }
                    showTips(result);
                    break;

                case ALIPAY_SCAN_FLAG:

                    break;

                case WXPAY_FLAG:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_recharge_to_balance);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
//        EditInputFilter filter = new EditInputFilter();
//        filter.setMAX_VALUE(Integer.MAX_VALUE);
//        filter.setContext(this);
//        InputFilter[] filters = {filter};
//        binding.withdrawSum.setFilters(filters);

        binding.setClicklistener(clickListener);
        rxManager.onRxEvent(RxEvent.RECHARGE_TO_BALANCE_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                RechargeToBalanceActivity.this.finish();
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

    public void trunAll(View view){
        binding.withdrawSum.setText(avAmount+"");
        binding.withdrawSum.setSelection(binding.withdrawSum.getText().length());
    }

    //锁定资金
    public void riskmanager(View view){
        startActivity(new Intent(this, LockMoneyExplainActivity.class));
    }

    public void clean(View view){
        binding.withdrawSum.setText("");
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>0){
            binding.clear.setVisibility(View.VISIBLE);
        }else
            binding.clear.setVisibility(View.INVISIBLE);
    }

    public void onNext(View view){
        String str = binding.withdrawSum.getText().toString();

        if (tag == 0){
            showTips("请选择支付方式");
            return;
        }else {
            switch (tag){
                case ALIPAY_FLAG:
                    if (TextUtils.isEmpty(str)){
                        showTips("请输入金额");
                        return;
                    }
                    getAliPayOrder();
                    break;

                case WXPAY_FLAG:
                    if (TextUtils.isEmpty(str)){
                        showTips("请输入金额");
                        return;
                    }
                    getWXPayOrder();
                    break;

                case ALIPAY_SCAN_FLAG:
                    if (TextUtils.isEmpty(str)){
                        showTips("请输入金额");
                        return;
                    }
                    getAlipayScanOrder();
                    break;

                case CHARGE_CREDIT_FLAG:
                    chargeCrdit();
                    break;
            }
        }

//        if (!TextUtils.isEmpty(str)){
//
//
//        } else {
//            showTips("请输入金额");
//        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tag = Integer.valueOf((String) v.getTag());
            if (tag==4){
//                binding.confirm.setTextColor(ContextCompat.getColor(RechargeToBalanceActivity.this,R.color.chat_bg));
                binding.confirm.setBackgroundResource(R.drawable.deposit_ever_button);
                binding.confirm.setEnabled(false);
                chargeCrdit();
            }else {
//                binding.confirm.setTextColor(ContextCompat.getColor(RechargeToBalanceActivity.this,R.color.white));
                binding.confirm.setBackgroundResource(R.drawable.login_button);
                binding.confirm.setEnabled(true);
            }
            binding.alipayCheck.setChecked(tag==1?true:false);
            binding.wechatCheck.setChecked(tag==2?true:false);
            binding.alipayScanCheck.setChecked(tag==3?true:false);
            binding.chargeCreditCheck.setChecked(tag==4?true:false);
        }
    };


    //从后台获取订单信息
    public void getAliPayOrder(){
        Subscription subscription = UtilsImpAPI.createAliOrder(HttpParam.ALIPAY_ORDER_KEY, HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,binding.withdrawSum.getText().toString())
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
                            String orderInfo = baseResult.sign;
//                            Logger.e("orderInfo  ==== "+orderInfo);
                            alipay(orderInfo);
                        }else {
                            showTips(baseResult.result_Msg);
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


    public void getWXPayOrder(){
        Subscription subscription = UtilsImpAPI.createWxOrderAppPay(HttpParam.WX_PAY_ORDER_CREATE_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,binding.withdrawSum.getText().toString())
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
                .subscribe(new Action1<WxPays>() {
                    @Override
                    public void call(WxPays pays) {
                        if (pays.result_Code == 0){
                            wxpay(pays.params);
                        }else
                            showTips(pays.result_Msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    //支付宝扫码充值
    public void getAlipayScanOrder(){
        Subscription subscription = UtilsImpAPI.createAliOrderH5(HttpParam.ALIPAY_SCAN_ORDER_KEY, HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,binding.withdrawSum.getText().toString())
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
                            String url = baseResult.url;
//                            Logger.e("getAlipayScanOrder===="+url);
                            startActivity(new Intent(
                                    RechargeToBalanceActivity.this,H5PayActivity.class)
                                    .putExtra("HTML",url)
                                    .putExtra(IntentParam.TITLE,getString(R.string.recharge_5)));

                            updateMerchantInfo();
                        }
                    }
                });
        rxManager.add(subscription);
    }

    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
                        MerchanInfoDB.updateMerchanInfo(bean);
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
     * 支付宝调用
     */
    public void alipay(final String orderInfo){
        //异步处理
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建任务
                PayTask alipay = new PayTask(RechargeToBalanceActivity.this);
                //获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = ALIPAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread thread = new Thread(payRunnable);
        thread.start();

    }

    /**
     * 发起微信支付
     * @param bean
     */
    public void wxpay(final WxPays.ParamsBean bean){
        WechatPlatform.PayForWeChat(this,bean);
    }


    public void chargeCrdit(){
        Intent intent = new Intent(this,ChargeCreditPaymentActivity.class);
        intent.putExtra(IntentParam.AMOUNT,binding.withdrawSum.getText().toString());
        startActivity(intent);
    }


}
