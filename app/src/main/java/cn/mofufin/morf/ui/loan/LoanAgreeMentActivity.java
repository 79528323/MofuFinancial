package cn.mofufin.morf.ui.loan;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;

import java.io.InputStream;

import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAgreeMentBinding;
import cn.mofufin.morf.databinding.ActivityLoanAgreeMentBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoanAgreeMentActivity extends BaseActivity {
    private ActivityLoanAgreeMentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_agree_ment);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        getUserProtocl();
    }



    public void getUserProtocl(){
        Subscription subscription = LoanImAPI.queryUserLoansProtocol(HttpParam.LOANS_QUERY_USER_PROTOCOL_KEY,
                HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
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
                            binding.setTextput(baseResult.content);
                        }else
                            showTips(baseResult.result_Msg);
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
