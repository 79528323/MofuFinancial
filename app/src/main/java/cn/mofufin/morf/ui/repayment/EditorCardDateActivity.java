package cn.mofufin.morf.ui.repayment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAboutMineBinding;
import cn.mofufin.morf.databinding.ActivityEditorCardDateBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.RepayMentDay;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.widget.SinglePicker;
import cn.mofufin.morf.ui.widget.SingleRepayDayPicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 修改当前信用卡账单日和还款日
 */
public class EditorCardDateActivity extends BaseActivity {

    private ActivityEditorCardDateBinding binding;
    private SinglePicker statePicker;
    private SingleRepayDayPicker repayPicker;
    private int pickType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_editor_card_date);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        User.DataBean.CardInfoBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setBean(bean);
        binding.setRepayday(Integer.valueOf(bean.repaymentDay));
        binding.setStateday(Integer.valueOf(bean.accountDay));
        binding.repayday.setText(getIntent().getStringExtra(IntentParam.REPAY_DAYS));
        matchBankIcon(bean);

        statePicker = new SinglePicker(this,SinglePicker.TYPE_CREDITDAY);
        statePicker.listener = new SinglePicker.OnSingleSelectListener() {
            @Override
            public void singleSelect(String type, int num) {
                binding.setStateday(num+1);
                getday(String.valueOf(binding.getStateday()));
                statePicker.dimiss();
                binding.confirm.setEnabled(true);
                binding.repayday.setText("还款日");
                binding.repayDays.setText("");
                binding.setRepayday(-1);
            }
        };

        getday(String.valueOf(binding.getStateday()));
    }

    public void repaySelect(View view){
        repayPicker.setTitle(getString(R.string.credit_card_6));
        repayPicker.show();
    }

    public void statementSelet(View view){
        statePicker.setTitle(getString(R.string.credit_card_5));
        statePicker.show();
    }

    public void confirm(View view){
        if (binding.getRepayday() < 0){
            showTips("请选择还款日期");
            return;
        }

        Subscription subscription = RepayMentImpAPI.updateCreditCardInfo(HttpParam.OFFICE_CODE,HttpParam.UPDATE_CREDIT_CARD_INFO_KEY,
                MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION,binding.getBean().cardCode,
                binding.getStateday(),binding.getRepayday())
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
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        if (mofuResult.result_Code == 0){
                            Intent intent = new Intent(EditorCardDateActivity.this,RepayMentManagerActivity.class);
                            intent.putExtra("state",binding.getStateday());
                            intent.putExtra("repay",binding.repayday.getText().toString());
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }else {
                            showTips(mofuResult.result_Msg);
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

    @Override
    protected boolean enableSliding() {
        return true;
    }


    public void getday(String day){
        Subscription subscription = RepayMentImpAPI.getRefundDay(HttpParam.GET_REFUND_DAY_KEY,day, Common.LOAN_VERSION)
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
                .subscribe(new Action1<RepayMentDay>() {
                    @Override
                    public void call(RepayMentDay repayMentDay) {
                        if (repayMentDay.getResult_Code() == 0
                                && !repayMentDay.getRefundDay().isEmpty()){
                            repayPicker = new SingleRepayDayPicker(EditorCardDateActivity.this,repayMentDay.getRefundDay());
                            repayPicker.listener = new SingleRepayDayPicker.OnSingleSelectListener() {
                                @Override
                                public void singleSelect(String type, int num) {
                                    if (TextUtils.isEmpty(type))
                                        return;

                                    String rpday = type.substring(2,type.length()-1);
                                    binding.setRepayday(Integer.valueOf(rpday));
                                    binding.repayday.setText("还款日 "+type);
                                    binding.repayDays.setText(type);
                                    repayPicker.dimiss();
                                    binding.confirm.setEnabled(true);
                                }
                            };
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

    //适配银行图标
    public void matchBankIcon(User.DataBean.CardInfoBean cardInfoBean){
        if (cardInfoBean.cardBankName.contains("平安")){
            binding.cardIcon.setImageResource(R.drawable.logo_pingan);
            binding.bankBg.setBackgroundResource(R.drawable.bank_pingan);

        }else if (cardInfoBean.cardBankName.contains("北京")){
            binding.cardIcon.setImageResource(R.drawable.logo_beijing);
            binding.bankBg.setBackgroundResource(R.drawable.bank_beijing);

        }else if (cardInfoBean.cardBankName.contains("工商")){
            binding.cardIcon.setImageResource(R.drawable.logo_gongshang);
            binding.bankBg.setBackgroundResource(R.drawable.bank_gongshang);

        }else if (cardInfoBean.cardBankName.contains("光大")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangda);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangda);
        }else if (cardInfoBean.cardBankName.contains("广发")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangfa);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangfa);

        }else if (cardInfoBean.cardBankName.contains("华夏")){
            binding.cardIcon.setImageResource(R.drawable.logo_huaxia);
            binding.bankBg.setBackgroundResource(R.drawable.bank_huaxia);

        }else if (cardInfoBean.cardBankName.contains("建设")){
            binding.cardIcon.setImageResource(R.drawable.logo_jianshe);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jianshe);

        }else if (cardInfoBean.cardBankName.contains("交通")){
            binding.cardIcon.setImageResource(R.drawable.logo_jiaotong);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jiaotong);

        }else if (cardInfoBean.cardBankName.contains("民生")){
            binding.cardIcon.setImageResource(R.drawable.logo_minsheng);
            binding.bankBg.setBackgroundResource(R.drawable.bank_minsheng);

        }else if (cardInfoBean.cardBankName.contains("农业")){
            binding.cardIcon.setImageResource(R.drawable.logo_nongye);
            binding.bankBg.setBackgroundResource(R.drawable.bank_nongye);

        }else if (cardInfoBean.cardBankName.contains("浦发")){
            binding.cardIcon.setImageResource(R.drawable.logo_pufa);
            binding.bankBg.setBackgroundResource(R.drawable.bank_pufa);

        }else if (cardInfoBean.cardBankName.contains("上海")){
            binding.cardIcon.setImageResource(R.drawable.logo_shanghai);
            binding.bankBg.setBackgroundResource(R.drawable.bank_shanghai);

        }else if (cardInfoBean.cardBankName.contains("兴业")){
            binding.cardIcon.setImageResource(R.drawable.logo_xingye);
            binding.bankBg.setBackgroundResource(R.drawable.bank_xingye);

        }else if (cardInfoBean.cardBankName.contains("邮政")){
            binding.cardIcon.setImageResource(R.drawable.logo_youzheng);
            binding.bankBg.setBackgroundResource(R.drawable.bank_youzheng);

        }else if (cardInfoBean.cardBankName.contains("招商")){
            binding.cardIcon.setImageResource(R.drawable.logo_zhaoshang);
            binding.bankBg.setBackgroundResource(R.drawable.bank_zhaoshang);

        }else if (cardInfoBean.cardBankName.contains("中信")){
            binding.cardIcon.setImageResource(R.drawable.logo_zhongxin);
            binding.bankBg.setBackgroundResource(R.drawable.bank_zhongxin);

        }else if (cardInfoBean.cardBankName.contains("广州")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangzhou);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangzhou);

        }else if (cardInfoBean.cardBankName.contains("花旗")){
            binding.cardIcon.setImageResource(R.drawable.logo_huaqi);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jiaotong);

        }else {
            if (TextUtils.equals("中国银行",cardInfoBean.cardBankName)){
                binding.cardIcon.setImageResource(R.drawable.logo_china);
                binding.bankBg.setBackgroundResource(R.drawable.bank_china);
            }else {
//                binding.cardIcon.setVisibility(View.INVISIBLE);
                binding.cardIcon.setImageResource(R.drawable.logo_other_bank);
            }
        }
    }
}
