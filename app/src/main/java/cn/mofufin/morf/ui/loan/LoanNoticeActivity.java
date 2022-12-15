package cn.mofufin.morf.ui.loan;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.LoanNoticeAdapter;
import cn.mofufin.morf.adapter.ProductNoticeAdapter;
import cn.mofufin.morf.databinding.ActivityLoanNoticeBinding;
import cn.mofufin.morf.databinding.ActivityProductNoticeBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.LoanNotify;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoanNoticeActivity extends BaseActivity {
    private ActivityLoanNoticeBinding binding;
    private LoanNoticeAdapter adapter;
    private String queryType="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_notice);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new LoanNoticeAdapter();
        binding.noticeList.setLayoutManager(new LinearLayoutManager(this));
        binding.noticeList.setAdapter(adapter);
        getNotice();
    }

    //获取通知消息
    public void getNotice(){
        Subscription subscription = LoanImAPI.queryLoansInform(HttpParam.LOANS_NOTIFY_KEY,queryType,
                Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanNotify>() {
                    @Override
                    public void call(LoanNotify loanNotify) {
                        if (loanNotify.result_Code == 0) {
                            adapter.refresh(loanNotify.informList);
                            binding.setHasData(loanNotify.informList.size()>0);
                        }else
                            showTips(loanNotify.result_Msg);
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
