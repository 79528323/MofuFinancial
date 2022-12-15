package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.mine.BalanceTransferStatusActivity;
import cn.mofufin.morf.ui.mine.RechargeToBalanceActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityProductPaymentDetailsBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 付款详情
 */
public class ProductPayMentDetailsActivity extends BaseActivity {
    private ActivityProductPaymentDetailsBinding binding;
    private ProductDetails.ProductDetailsBean bean;
    private MerchantBag.DataBean.ListBean merchantbag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_payment_details);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setDetails(bean);
        binding.setBalance(getString(R.string.product_pay_9,"0.00"));
        binding.setExpIncome(getString(R.string.product_pay_expincome,"0.00"));

        String hint = getString(R.string.product_pay_8,String.valueOf(bean.fdMinMoney));
        binding.inverstAmount.setHint(hint);

//        EditInputFilter filter = new EditInputFilter();
//        filter.setMAX_VALUE(Integer.MAX_VALUE);
//        filter.setToast("金额数过大");
//        filter.setContext(ProductPayMentDetailsActivity.this);
//        InputFilter[] filters = {filter};
//        binding.inverstAmount.setFilters(filters);

        rxManager.onRxEvent(RxEvent.PRODUCT_PAYMENT_FINISH)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });

        rxManager.onRxEvent(RxEvent.REFRESH_PAYMENT_DETAILS_INFO)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        merchantbag = null;
                        binding.selectCoupon.setText(getString(R.string.product_pay_5));
                        binding.selectCoupon.setTextColor(
                                ContextCompat.getColor(ProductPayMentDetailsActivity.this,R.color.app_blue));
                        binding.inverstAmount.setText("");
                        binding.setRecome("");
                    }
                });



        binding.setIsAgree(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    //协议
    public void financialAgreement(View view){
//        binding.setIsAgree(!binding.getIsAgree());
    }

    public void agree(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    //优惠卷
    public void coupon(View view){
        Intent intent = new Intent(this, BackpackActivity.class);
        intent.putExtra(IntentParam.BACK_PACK_SELECTION,BackpackActivity.SELECTION_TYPE_FINANCIAL);
        startActivityForResult(intent,BackpackActivity.SELECTION_TYPE_FINANCIAL);
    }

    //充值
    public void recharge(View view){
        Intent intent = new Intent(this,RechargeToBalanceActivity.class);
        startActivity(intent);
    }

    public void payMax(View view){
        binding.inverstAmount.setText(DataUtils.numFormat(MerchanInfoDB.queryInfo().RMB));
        int selection = binding.inverstAmount.getText().toString().length();
        binding.inverstAmount.setSelection(selection);
    }

    //立即投资
    public void inverst(View view){
        String amout = binding.inverstAmount.getText().toString();
        if (TextUtils.isEmpty(amout)){
            showTips("请输入投资金额");
            return;
        }else if (!binding.getIsAgree()){
            showTips("请同意相关协议");
            return;
        }else if (Double.valueOf(amout)>(bean.fdTotalCirculation - bean.fdResidueCirculation)){
            showTips("不能大于剩余可投数目");
            return;
        }else if (Double.valueOf(amout) < bean.fdMinMoney){
            showTips("投资金额不能小于起投金额");
            return;
        }
//        Intent intent = new Intent(this,BalanceTransferPasswordActivity.class);
//        intent.putExtra(IntentParam.BEAN,bean);
//        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,amout);
//        intent.putExtra(IntentParam.TYPE,3);
//        if (null!=merchantbag){
//            intent.putExtra(IntentParam.NUMBER,merchantbag.mcbGoodsNumber);
//        }

//        startActivity(intent);

        Subscription subscription = ProductImpAPI.merInvest(HttpParam.OFFICE_CODE,HttpParam.MER_INVERST_KEY,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(bean.fdNumber),
                null,amout,
                null!=merchantbag? merchantbag.mcbGoodsNumber:null)
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
//                        inverstMentPay(baseResult);
                        Intent intent = new Intent(ProductPayMentDetailsActivity.this,BalanceTransferStatusActivity.class);
                        intent.putExtra(IntentParam.BEAN,baseResult);
                        intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_INVERSTMENT_PAYMENT);
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }





    //获取当前余额
    public void getBalance(){
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

                        final User.DataBean.MerchantInfoBean beans = MerchanInfoDB.queryInfo();
                        binding.setBalance(getString(R.string.product_pay_9,DataUtils.numFormat(beans.RMB)));


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String amount = s.toString();
        if (amount.length() <= 0){
            binding.setExpIncome(getString(R.string.product_pay_expincome,"0.00"));
        }else {
            if (amount.endsWith("."))
                return;

            calculationExpIncome();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case BackpackActivity.SELECTION_TYPE_FINANCIAL:
                if (data!=null){
                    merchantbag=data.getParcelableExtra(IntentParam.MERCHANT_BAG);
                    if (merchantbag.gdGoodsBranchType == 11 || merchantbag.gdGoodsBranchType == 7){
                        binding.setRecome("投资成功后将返现"+merchantbag.gdGoodsDenomination+"元到余额");
                    }else {
                        binding.setRecome("");
                    }

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("已选 ");

                    if (merchantbag.gdGoodsBranchType == 0){
                        buffer.append("加息卷：")
                                .append(DataUtils.converOverNOPercent2financial(merchantbag.gdGoodsDenomination))
                                .append("%");
                    }else {
                        buffer.append("返现卷：").append(merchantbag.gdGoodsDenomination).append("元");
                    }

//                    binding.selectCoupon.setTag(merchantbag);
                    binding.selectCoupon.setText(buffer.toString());
                    binding.selectCoupon.setTextColor(
                            ContextCompat.getColor(ProductPayMentDetailsActivity.this,R.color.fin_home_text));

                    calculationExpIncome();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 计算预期收益
     */
    public void calculationExpIncome(){
        double interest = 0d;
        if (null!=merchantbag && merchantbag.gdGoodsBranchType == 0){
            interest += merchantbag.gdGoodsDenomination;
        }

        String amount = binding.inverstAmount.getText().toString();
        String result = DataUtils.matchExpIncome(
                Float.valueOf(TextUtils.isEmpty(amount)?"0" : amount),
                binding.getDetails().fdAnnualized,
                binding.getDetails().fdProductDate,
                interest);

        String content = getString(R.string.product_pay_expincome,result);
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.fin_home_text)),
                content.indexOf("：")+1,content.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.expIncome.setText(spannableString);
    }

}
