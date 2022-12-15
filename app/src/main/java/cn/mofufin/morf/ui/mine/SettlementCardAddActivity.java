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
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivitySettlementCardAddBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SettlementCardAddActivity extends BaseActivity {
    private ActivitySettlementCardAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settlement_card_add);
        binding.setHandlers(this);
        binding.setIsAgree(false);

        User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        if (bean!=null && !TextUtils.isEmpty(bean.fdMerIdentityCardName)){
            binding.name.setText(bean.fdMerIdentityCardName);
            binding.name.setEnabled(false);
        }
    }

    public void rquestBankType(final String name, final String no,final String mobile){
        Subscription subscription = BankImpAPI.bankCardType(
                HttpParam.OFFICE_CODE,
                HttpParam.REQUEST_BANK_CARD_TYPE,
                MerchanInfoDB.queryInfo().merchantPhone,
                no)
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
                .flatMap(new Func1<BaseResponse<GeneralResponse.DataBean>,
                        Observable<BaseResponse<User.DataBean>>>() {
                    @Override
                    public Observable<BaseResponse<User.DataBean>> call(
                            final BaseResponse<GeneralResponse.DataBean> response) {
                        if (!response.bool){
                            showTips(response.data.reason);
                            return null;
                        }else
                            return BankImpAPI.addbankInfo(HttpParam.OFFICE_CODE,
                                HttpParam.ADD_BANK_INFO_APPKEY,MerchanInfoDB.queryInfo().merchantPhone,name,
                                no,response.data.dataMap.bankname,
                                    response.data.dataMap.cardType2,mobile)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .flatMap(new Func1<BaseResponse<User.DataBean>, Observable<BaseResponse<User.DataBean>>>() {
                    @Override
                    public Observable<BaseResponse<User.DataBean>> call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        return Observable.just(dataBeanBaseResponse);
                    }
                }).subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> baseResponse) {
                        showTips(baseResponse.data.getReason());
                        if (baseResponse.bool){
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_BANKCARD_INFO);
                            RxBus.getInstance().postEmpty(RxEvent.CASH_WITHDRAWAL_UPDATE_CARDINFO);
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_SELECT_CARD);
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_WITHDRAW_INFO);
                            finish();
                        }else
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

    public void next(View view){
        String name=binding.name.getText().toString();
        String num = binding.num.getText().toString();
        String phone = binding.phone.getText().toString();
        if (TextUtils.isEmpty(name)){
            showTips("请输入持卡人姓名");
            return;
        }else if (TextUtils.isEmpty(num)){
            showTips("请输入银行卡号");
            return;
        }else if (TextUtils.isEmpty(phone)){
            showTips("请填写银行预留手机号");
            return;
        }else if (!binding.getIsAgree()){
            showTips("请同意相关协议");
            return;
        }

        rquestBankType(name,num,phone);
    }

    public void agreement(View view){
        startActivity(new Intent(this, AgreeMentActivity.class));
    }

    public void Agreement(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    public void tips(View view){
        DataUtils.TipsDailog(
                this,"提示",
                getString(R.string.tips_settlement),
                getString(R.string.ok),null,null);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
}
