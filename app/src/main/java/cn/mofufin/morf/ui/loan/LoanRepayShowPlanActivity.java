package cn.mofufin.morf.ui.loan;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.githang.statusbar.StatusBarCompat;
import com.j256.ormlite.stmt.query.In;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.CommissionDetailAdapter;
import cn.mofufin.morf.adapter.RefundPlanAdapter;
import cn.mofufin.morf.databinding.ActivityCommissionDetailBinding;
import cn.mofufin.morf.databinding.ActivityLoanRepayShowplanBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.CommissionDetail;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.RefundPlan;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 查看还款计划
 */
public class LoanRepayShowPlanActivity extends BaseActivity {
    private ActivityLoanRepayShowplanBinding binding;
    private RefundPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_repay_showplan);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new RefundPlanAdapter();
        binding.planList.setLayoutManager(new LinearLayoutManager(this));
        binding.planList.setAdapter(adapter);

        int period = getIntent().getIntExtra(IntentParam.PERIOD,0);
        int way = getIntent().getIntExtra(IntentParam.WAY,0);
        int money = getIntent().getIntExtra(IntentParam.AMOUNT,0);
        double ratio = getIntent().getDoubleExtra(IntentParam.RATIO,0d);
//
        binding.setMonth(period);
        binding.setRatio(DataUtils.scientificMatchPercent(ratio,5));
        binding.setRepayType(way);

        Subscription subscription = LoanImAPI.showRefundPlan(HttpParam.LOANS_REPAY_SHOWPLAN_KEY, Common.LOAN_VERSION,
                period,way,money,ratio)
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
                .subscribe(new Action1<RefundPlan>() {
                    @Override
                    public void call(RefundPlan productDetails) {
                        if (productDetails.result_Code==0){
                            adapter.refresh(productDetails.refundPlanDetail);
                            double amount = 0d;
                            for (int index=0; index<productDetails.refundPlanDetail.size(); index++){
                                amount += productDetails.refundPlanDetail.get(index).countRefundMoney;
                            }
                            binding.setAmount(DataUtils.numFormat(amount));
//                            binding.setMonth(productDetails.refundPlanDetail.size());
                        }else
                            showTips(productDetails.result_Msg);
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
