package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.RepayMentDay;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.repayment.EditorCardDateActivity;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.SinglePicker;
import cn.mofufin.morf.databinding.ActivityCreditCardAddBinding;
import cn.mofufin.morf.ui.widget.SingleRepayDayPicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CreditCardAddActivity extends BaseActivity {
    private ActivityCreditCardAddBinding binding;
    private int pos=1;
    private String cardType="" ,bankName="";
    private SinglePicker singlePicker;
    private SingleRepayDayPicker repayPicker;
//    private int pickType=1;
//    private String statement = "";
//    private String repayment = "";
    private String validity = "";
    private String CVN2 = "";
    private String name="";
    private String num = "";
    private String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_credit_card_add);
        binding.setHandlers(this);
        binding.setIsAgree(false);
        binding.setPos(pos);

        User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        if (bean!=null && !TextUtils.isEmpty(bean.fdMerIdentityCardName)){
            binding.name.setText(bean.fdMerIdentityCardName);
            binding.name.setEnabled(false);
        }

        singlePicker = new SinglePicker(this,SinglePicker.TYPE_CREDITDAY);
        singlePicker.listener = new SinglePicker.OnSingleSelectListener() {
            @Override
            public void singleSelect(String type, int num) {
                singlePicker.dimiss();
                binding.editStatement.setText(type);
                String stday = type.substring(2,type.length()-1);
                binding.setStatementDay(stday);
                getday(stday);
            }
        };
    }

    @Override
    public void exit(View view) {
        if (binding.getPos()>1){
            binding.setPos(binding.getPos()-1);
        }else
            super.exit(view);
    }

    @Override
    public void onBackPressed() {
        if (binding.getPos()>1){
            binding.setPos(binding.getPos()-1);
        }else
            super.onBackPressed();
    }

    public void next(View view){
        phone = binding.phone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showTips("请输入银行预留手机号");
            return;
        }

//        statement = statement.substring(2,statement.length()-1);
//        repayment = repayment.substring(2,repayment.length()-1);
        Subscription subscription = BankImpAPI.addCreditbankInfo(
                HttpParam.OFFICE_CODE,HttpParam.ADD_CREDIT_BANK_CARD,
                MerchanInfoDB.queryInfo().merchantPhone,name,num,binding.getName(),binding.getType(),
                binding.getStatementDay(),binding.getRepaymentDay(),validity,CVN2,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> baseResponse) {
                        showTips(baseResponse.data.getReason());
                        if (baseResponse.bool){
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_BANKCARD_INFO);
                            finish();
                        }
                        hiddenLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hiddenLoading();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void firnext(View view){
        name = binding.name.getText().toString();
        num = binding.num.getText().toString();
        if (TextUtils.isEmpty(name)){
            showTips("请输入持卡人姓名");
            return;
        }else if (TextUtils.isEmpty(num)){
            showTips("请输入银行卡号");
            return;
        }else if (!binding.getIsAgree()){
            showTips("请同意相关协议");
            return;
        }

        Subscription subscription = BankImpAPI.bankCardType(HttpParam.OFFICE_CODE,
                HttpParam.REQUEST_BANK_CARD_TYPE,
                MerchanInfoDB.queryInfo().merchantPhone,
                num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse.DataBean> dataBeanBaseResponse) {
                        if (dataBeanBaseResponse.bool){
                            binding.setPos(binding.getPos()+1);
                            bankName = dataBeanBaseResponse.data.dataMap.bankname;
                            cardType = dataBeanBaseResponse.data.dataMap.cardType2;

                            binding.setName(bankName);
                            binding.setType(cardType);
                        }else {
                            showTips(dataBeanBaseResponse.data.reason);
                        }

                        hiddenLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hiddenLoading();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void secnext(View view){
//        statement = binding.editStatement.getText().toString();
//        repayment = binding.editRepayment.getText().toString();
        validity = binding.validity.getText().toString();
        CVN2 = binding.cvn2.getText().toString();


        if (TextUtils.isEmpty(binding.getStatementDay())){
            showTips("请选择账单日");
            return;
        }else if (TextUtils.isEmpty(binding.getRepaymentDay())){
            showTips("请选择还款日");
            return;
        }else if (TextUtils.isEmpty(validity)){
            showTips("请填写有效期");
            return;
        }else if (TextUtils.isEmpty(CVN2)){
            showTips("请填写CVN2");
            return;
        }else if (validity.length()!=4){
            showTips("有效期格式为月年，如：1222");
            return;
        }else if (validity.length()==4){
            int month = Integer.valueOf(validity.substring(0,2));
            if (month>12){
                showTips("有效期有误!");
                return;
            }
        }

        binding.setPos(binding.getPos()+1);
    }

    public void repaySelect(View view){
        if (repayPicker!=null){
            repayPicker.setTitle(getString(R.string.credit_card_6));
            repayPicker.show();
        }
    }

    public void statementSelet(View view){
        singlePicker.setTitle(getString(R.string.credit_card_5));
        singlePicker.show();
    }

    public void agreement(View view){
        startActivity(new Intent(this, AgreeMentActivity.class));
    }

    public void Agreement(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    public void tips(View view) {
        DataUtils.TipsDailog(
                this, "提示",
                getString(R.string.tips_settlement),
                getString(R.string.ok), null, null);
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
                            repayPicker = new SingleRepayDayPicker(CreditCardAddActivity.this,repayMentDay.getRefundDay());
                            repayPicker.listener = new SingleRepayDayPicker.OnSingleSelectListener() {
                                @Override
                                public void singleSelect(String type, int num) {
                                    if (TextUtils.isEmpty(type))
                                        return;

                                    binding.editRepayment.setText(type);
                                    String rpday = type.substring(2,type.length()-1);
                                    binding.setRepaymentDay(rpday);
                                    repayPicker.dimiss();
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

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
