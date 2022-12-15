package cn.mofufin.morf.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.LockMoneyExplainActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.EditInputFilter;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.SettlementCardDialog;
import cn.mofufin.morf.databinding.ActivityCashWithdrawalBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 提现
 */
public class CashWithdrawalActivity extends BaseActivity{
    private ActivityCashWithdrawalBinding binding;
    private double avAmount = 0.00;
    private SettlementCardDialog dialog;
//    private List<User.DataBean.CardInfoBean> cardInfoBeanList;
    private double RMB = 0.00;
    private double fee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cash_withdrawal);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        SpannableString spannableString = new SpannableString("锁定资金提现将收取1%的手续费");
        spannableString.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.app_blue)),9,11,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.explaint.setText(spannableString);

        binding.setLockMoney(MerchanInfoDB.queryInfo().lockMoney);

        String amount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);
        avAmount = TextUtils.isEmpty(amount)?avAmount:Double.valueOf(amount);
        binding.setAmount(getString(R.string.withdraw_3,DataUtils.numFormat(avAmount)));

        EditInputFilter filter = new EditInputFilter();
        filter.setMAX_VALUE(avAmount);
        filter.setContext(this);
        InputFilter[] filters = {filter};
        binding.withdrawSum.setFilters(filters);

        dialog = new SettlementCardDialog(this);
        dialog.setListener(new SettlementCardDialog.onSettleCardClickListener() {
            @Override
            public void onClickListener(User.DataBean.CardInfoBean bean) {
                binding.setBank(bean);
                matchBankIcon(bean);
            }
        });

        queryDate();

        rxManager.onRxEvent(RxEvent.CASH_WITHDRAWAL_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                CashWithdrawalActivity.this.finish();
            }
        });

        rxManager.onRxEvent(RxEvent.REFRESH_WITHDRAW_INFO)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        updateMerchantInfo();
                    }
                });
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_WITHDRAW_INFO);
        queryCashWithCoupon();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fee =0;
        Logger.e(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.e(TAG,"onStop");
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void lockMoneyExplaint(View view){
        startActivity(new Intent(this, LockMoneyExplainActivity.class));
    }

    public void trunAll(View view){
        binding.withdrawSum.setText(String.valueOf(avAmount));
        binding.withdrawSum.setSelection(binding.withdrawSum.getText().length());
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

        try {
            if (!TextUtils.isEmpty(str)){
                if (!isCanWithDrawal()){//TODO 禁止不在时间段内提现操作
                    showTips(binding.tips.getText().toString());
                    return;
                }

                if (str.substring(str.length()-1 ,str.length()).equals(".")){//过滤掉格式错误
                    showTips("金额格式错误");
                    return;
                }

                if (Double.valueOf(str) <= 0){
                    showTips("请输入正确金额");
                    return;
                }

//            fee += 2;
                withDrawal(str);

            } else {
                showTips("请输入金额");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void withDrawal(final String str){

        final double with = Double.valueOf(str);//当前客户需要提现金额
        final double lockMoney = binding.getLockMoney();//锁定资金
        double nolockMoney = Math.abs(avAmount - lockMoney);//未锁定资金


        if (with > nolockMoney){//大于时提示客户包含锁定资金
            String content = "您当前提现金额包含锁定资金"+
                    DataUtils.numFormat(with - (avAmount - lockMoney))+"元,如果提现该部分锁定资金将收取1%手续费";
            DataUtils.TipsDailog(this, content, "返回", "继续",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            jumpIntent(with);
                        }
                    });
        }else {
            fee = 2;
            jumpIntent(with);
        }
    }

    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
//        DataUtils.refreshMerchantInfo(rxManager, new OnRefreshMerchantInfoListener() {
//            @Override
//            public void onSuccess(User.DataBean user) {
//
//            }
//
//            @Override
//            public void onErrors(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onToast(String msg) {
//
//            }
//        });


        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
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
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        if (!dataBeanBaseResponse.bool){
                            DataUtils.TipsDailog(CashWithdrawalActivity.this,
                                    dataBeanBaseResponse.data.getReason(),
                                    null, getString(R.string.confirm),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DataUtils.Logout();
                                        }
                                    },false);
                            return;
                        }
                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
                        MerchanInfoDB.updateMerchanInfo(bean);

                        List<User.DataBean.CardInfoBean> bankList = dataBeanBaseResponse.data.getCardInfo();
                        if (bankList.size() > 0){
                            dialog.setCardInfoBeanList(bankList);
                            Iterator<User.DataBean.CardInfoBean> iterator = bankList.iterator();
                            while (iterator.hasNext()){
                                User.DataBean.CardInfoBean cardInfoBean = iterator.next();
                                if (cardInfoBean.cardType==1 && cardInfoBean.cardDef == 1){
                                    binding.setBank(cardInfoBean);
                                    matchBankIcon(cardInfoBean);
                                    break;
                                }
                            }
                        }

                        binding.setLockMoney(bean.lockMoney);

                        String amount = String.valueOf(bean.RMB);
                        avAmount = TextUtils.isEmpty(amount)?avAmount:Double.valueOf(amount);
                        binding.setAmount(getString(R.string.withdraw_3,DataUtils.numFormat(avAmount)));

                        EditInputFilter filter = new EditInputFilter();
                        filter.setMAX_VALUE(avAmount);
                        filter.setContext(CashWithdrawalActivity.this);
                        InputFilter[] filters = {filter};
                        binding.withdrawSum.setFilters(filters);


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    Intent intent = null;
    //跳转
    public void jumpIntent(final double amount){
        intent = new Intent(CashWithdrawalActivity.this,BalanceTransferPasswordActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.withdrawSum.getText().toString());
        intent.putExtra(IntentParam.CARD_INFO_BEAN,binding.getBank());
        intent.putExtra(IntentParam.IS_BACK_CASH_WITHDRAWAL,true);
        intent.putExtra(IntentParam.TYPE,5);

        MerchantBag.DataBean.ListBean bean = binding.getBean();
        if (bean!=null){
            double condition = Double.valueOf(bean.gdGoodsUseCondition);
            if (condition > amount){//TODO 小于优惠券面额 应提示用户
                DataUtils.TipsDailog(this, "您输入的提现金额未满足代付券1000元以上的使用条件，\n" +
                                "代付券无法生效",
                        "取消", "继续交易",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO 无法满足条件  去掉优惠券
                                intent = calutionFee(intent);
                                startActivity(intent);
                            }
                        });
            }else {
                intent = calutionFee(intent);
                intent.putExtra(IntentParam.MERCHANT_BAG,binding.getBean());
                startActivity(intent);
            }
        }else {
            //TODO 默认无券  直接跳转
            intent = calutionFee(intent);
            startActivity(intent);
        }

    }


    //计算手续费
    public Intent calutionFee(Intent intent){
        final double with = Double.valueOf(binding.withdrawSum.getText().toString());//当前客户需要提现金额
        final double lockMoney = binding.getLockMoney();//锁定资金
        double nolockMoney = Math.abs(avAmount - lockMoney);//未锁定资金

        fee = 2;
        if (with > nolockMoney){
            fee += (Double.valueOf(DataUtils.numFormat(with - (avAmount - lockMoney)))) * 0.01;
        }

        if (binding.getBean()!=null) {
            double denomination = binding.getBean().gdGoodsDenomination;
            if (denomination <= with){
                fee -= denomination;
            }
        }

        intent.putExtra(IntentParam.FEE,fee);
        return intent;
    }

    public void showbankCard(View view){
        if (!dialog.isShowing()){
            dialog.showDialog();
        }else{
            dialog.dismiss();
        }
    }

    //适配银行图标
    public void matchBankIcon(User.DataBean.CardInfoBean cardInfoBean){
        binding.cardImg.setVisibility(View.VISIBLE);
        if (cardInfoBean.cardBankName.contains("平安")){
            binding.cardImg.setImageResource(R.drawable.logo_pingan);

        }else if (cardInfoBean.cardBankName.contains("北京")){
            binding.cardImg.setImageResource(R.drawable.logo_beijing);

        }else if (cardInfoBean.cardBankName.contains("工商")){
            binding.cardImg.setImageResource(R.drawable.logo_gongshang);

        }else if (cardInfoBean.cardBankName.contains("光大")){
            binding.cardImg.setImageResource(R.drawable.logo_guangda);

        }else if (cardInfoBean.cardBankName.contains("广发")){
            binding.cardImg.setImageResource(R.drawable.logo_guangfa);

        }else if (cardInfoBean.cardBankName.contains("华夏")){
            binding.cardImg.setImageResource(R.drawable.logo_huaxia);

        }else if (cardInfoBean.cardBankName.contains("建设")){
            binding.cardImg.setImageResource(R.drawable.logo_jianshe);

        }else if (cardInfoBean.cardBankName.contains("交通")){
            binding.cardImg.setImageResource(R.drawable.logo_jiaotong);

        }else if (cardInfoBean.cardBankName.contains("民生")){
            binding.cardImg.setImageResource(R.drawable.logo_minsheng);

        }else if (cardInfoBean.cardBankName.contains("农业")){
            binding.cardImg.setImageResource(R.drawable.logo_nongye);

        }else if (cardInfoBean.cardBankName.contains("浦发")){
            binding.cardImg.setImageResource(R.drawable.logo_pufa);

        }else if (cardInfoBean.cardBankName.contains("上海")){
            binding.cardImg.setImageResource(R.drawable.logo_shanghai);

        }else if (cardInfoBean.cardBankName.contains("兴业")){
            binding.cardImg.setImageResource(R.drawable.logo_xingye);

        }else if (cardInfoBean.cardBankName.contains("邮政")){
            binding.cardImg.setImageResource(R.drawable.logo_youzheng);

        }else if (cardInfoBean.cardBankName.contains("招商")){
            binding.cardImg.setImageResource(R.drawable.logo_zhaoshang);

        }else if (cardInfoBean.cardBankName.equals("中信")){
            binding.cardImg.setImageResource(R.drawable.logo_zhongxin);

        }else {
            if (TextUtils.equals("中国银行",cardInfoBean.cardBankName)){
                binding.cardImg.setImageResource(R.drawable.logo_china);
            }else {
                binding.cardImg.setImageResource(R.drawable.logo_other_bank);
            }
        }
    }


    public void selectionCoupon(View view){
        Intent intent = new Intent(this, BackpackActivity.class);
        intent.putExtra(IntentParam.BACK_PACK_SELECTION,BackpackActivity.SELECTION_TYPE_PAYMENT);
        startActivityForResult(intent,BackpackActivity.SELECTION_TYPE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case BackpackActivity.SELECTION_TYPE_PAYMENT:
                if (data!=null){
                    MerchantBag.DataBean.ListBean bean=data.getParcelableExtra(IntentParam.MERCHANT_BAG);
                    binding.setBean(bean);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 是否在提现时间段内
     * @return
     */
    public boolean isCanWithDrawal() throws ParseException {
        long beginTime ,endTime;
        if (TextUtils.isEmpty(binding.getPayDateBegin()))
            return false;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(binding.getPayDateBegin()));
        beginTime = calendar.getTimeInMillis();

        calendar.setTime(format.parse(binding.getPayDateEnd()));
        endTime = calendar.getTimeInMillis();

        long curHour = getCurhour();
        if (curHour >= beginTime && curHour < endTime){//已经大于3点
            return true;
        }

        return false;
    }


    //查询是否有优惠卷  默认添加使用
    public void queryCashWithCoupon(){
        Subscription subscription = MallImAPI.getMerchantBag(HttpParam.MERCHANT_BAG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(BackpackActivity.SELECTION_TYPE_PAYMENT))
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
                .subscribe(new Action1<BaseResponse<MerchantBag.DataBean>>() {
                    @Override
                    public void call(BaseResponse<MerchantBag.DataBean> response) {
                        if (response.bool){
                            List<MerchantBag.DataBean.ListBean> list = response.data.getList();
                            if (!list.isEmpty()){
                                binding.setBean(list.get(0));
                            }
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


    public void queryDate(){
        Subscription subscription = UtilsImpAPI.queryWithdrawDate(HttpParam.QUERY_WITHDRAW_DATE_KEY,
                Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        if (mofuResult.result_Code==0){
                            binding.setPayDateBegin(mofuResult.payDateBegin.substring(0,mofuResult.payDateBegin.lastIndexOf(":")));
                            binding.setPayDateEnd(mofuResult.payDateEnd.substring(0,mofuResult.payDateEnd.lastIndexOf(":")));
                        }else {
                            binding.tips.setText(mofuResult.result_Msg);
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


    public long getCurhour() throws ParseException {//TODo 获取当前几点钟
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(new Date());
        calendar.setTime(format.parse(date));
        long hour = calendar.getTimeInMillis();
        return hour;
    }


    /**
     * 获取本人账户下的银行卡
     */
//    public void getBankList(){
//        Subscription subscription = BankImpAPI.bankInfo(HttpParam.OFFICE_CODE,
//                HttpParam.BANK_INFO_APPKEY,MerchanInfoDB.queryInfo().merchantCode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                }).doOnCompleted(new Action0() {
//                    @Override
//                    public void call() {
//                        hiddenLoading();
//                    }
//                })
//                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
//                    @Override
//                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
//                        List<User.DataBean.CardInfoBean> bankList = dataBeanBaseResponse.data.getCardInfo();
//                        if (bankList.size() > 0){
//                            dialog.setCardInfoBeanList(bankList);
//                            Iterator<User.DataBean.CardInfoBean> iterator = bankList.iterator();
//                            while (iterator.hasNext()){
//                                User.DataBean.CardInfoBean bean = iterator.next();
//                                if (bean.cardType==1 && bean.cardDef == 1){
//                                    binding.setBank(bean);
//                                    matchBankIcon(bean);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
//    }

}
