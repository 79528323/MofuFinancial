package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.CardInfoDB;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.TransPassWordDialog;
import cn.mofufin.morf.databinding.ActivityUnbindCardBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UnbindCardActivity extends BaseActivity {
    private ActivityUnbindCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_unbind_card);
        binding.setHandlers(this);

        User.DataBean.CardInfoBean cardInfoBean = getIntent().getParcelableExtra(IntentParam.UNBIND_CARD_INFO);
        binding.setBean(cardInfoBean);
        initView(cardInfoBean);

        rxManager.onRxEvent(RxEvent.UNBIND_CARD_TRAN_PASSWORD).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                String password = (String) o;
                unbind(password);
            }
        });
    }

    public void initView(User.DataBean.CardInfoBean cardInfoBean){
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
                binding.cardIcon.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        passwordDailog();
    }

    @SuppressLint("RestrictedApi")
    public void passwordDailog(){
        TransPassWordDialog dialog = new TransPassWordDialog();
        dialog.show(getSupportFragmentManager(),"TransPassWordDialog");
    }

    public void unbind(String password){
        Subscription subscription = BankImpAPI.UnbindbankCard(HttpParam.OFFICE_CODE,HttpParam.UNBIND_BANK_CARD_APPKEY,
                MerchanInfoDB.queryInfo().merchantPhone,password,binding.getBean().cardCode)
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
                        if (dataBeanBaseResponse.bool){
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_BANKCARD_INFO);
                            CardInfoDB.deleteBankCard(binding.getBean().cardCode);
                            finish();
                        }else {
                            showTips(dataBeanBaseResponse.data.reason);
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
}
