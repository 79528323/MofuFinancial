package cn.mofufin.morf.ui.repayment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.RepayMentSupportBankListAdapter;
import cn.mofufin.morf.adapter.ShowProjectAdapter;
import cn.mofufin.morf.databinding.ActivityRepaymentSupportbankListBinding;
import cn.mofufin.morf.databinding.ActivityShowProjectsBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.SupportBank;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RepayMentSupportBankListActivity extends BaseActivity {
    private ActivityRepaymentSupportbankListBinding binding;
    private RepayMentSupportBankListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_repayment_supportbank_list);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new RepayMentSupportBankListAdapter();
        binding.bankList.setLayoutManager(new LinearLayoutManager(this));
        binding.bankList.setAdapter(adapter);
        queryBankList(getIntent().getStringExtra(IntentParam.NUMBER));
    }


    public void queryBankList(String rcNumber){
        Subscription subscription = RepayMentImpAPI.queryChannelSupportBank(
                HttpParam.OFFICE_CODE,HttpParam.QUERY_REPAYMENT_BANK_LIST_KEY,rcNumber,Common.LOAN_VERSION)
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
                .subscribe(new Action1<SupportBank>() {
                    @Override
                    public void call(SupportBank bank) {
                        if (bank.result_Code == 0){
                            adapter.refresh(bank.supportBankList);
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
