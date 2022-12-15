package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.databinding.ActivityLockmoneyExplainBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 锁定资金说明
 */
public class LockMoneyExplainActivity extends BaseActivity {
    private ActivityLockmoneyExplainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lockmoney_explain);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        SpannableString spannableString = new SpannableString(getString(R.string.lock_4));
        spannableString.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(this,R.color.app_blue)),14,16,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.explain.setText(spannableString);
        updateMerchantInfo();
    }


    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RequestTransformer(this))
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        if (dataBeanBaseResponse.bool){
                            binding.setCurrentBalance(dataBeanBaseResponse.data.getMerchantInfo().RMB);
                            binding.setLockmoney(dataBeanBaseResponse.data.getMerchantInfo().lockMoney);
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
