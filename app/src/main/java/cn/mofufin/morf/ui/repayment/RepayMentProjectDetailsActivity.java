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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.RepayMentProjectDetailsAdapter;
import cn.mofufin.morf.adapter.ShowProjectAdapter;
import cn.mofufin.morf.databinding.ActivityRepaymentProjectDetailsBinding;
import cn.mofufin.morf.databinding.ActivityShowProjectsBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.ProjectDetails;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.RepayMentProject;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RepayMentProjectDetailsActivity extends BaseActivity {
    private ActivityRepaymentProjectDetailsBinding binding;
    private RepayMentProjectDetailsAdapter adapter;
    private List<List<ProjectDetails.PlanDetailsListBean>> planlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_repayment_project_details);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new RepayMentProjectDetailsAdapter();
        binding.projectDetailsList.setLayoutManager(new LinearLayoutManager(this));
        binding.projectDetailsList.setAdapter(adapter);

        RepayMentProject.PlanListBean project = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setTotalBean(project);
//        binding.setStatus(binding.getTotalBean().rpState);
        queryRefundPlanDetail(project.rpOrderCode);
    }


    public void queryRefundPlanDetail(final String code){
        Subscription subscription = RepayMentImpAPI.queryRefundPlanDetails(HttpParam.QUERY_REFUND_PROJECT_DETAILS_KEY,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                code)
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
                .subscribe(new Action1<ProjectDetails>() {
                    @Override
                    public void call(ProjectDetails details) {
                        if (details.result_Code == 0){
                            planlist = new ArrayList<>();
                            if (!details.planDetailsList.isEmpty()){
                                List<ProjectDetails.PlanDetailsListBean> temp = new ArrayList<>();
                                List<ProjectDetails.PlanDetailsListBean> planDetailsListBeanList = details.planDetailsList;
                                if (!planDetailsListBeanList.isEmpty()){
                                    for (int index=0; index < planDetailsListBeanList.size(); index++){
                                        ProjectDetails.PlanDetailsListBean bean = planDetailsListBeanList.get(index);
                                        if (temp.size() <= 0){
                                            temp.add(bean);
                                            continue;
                                        }

                                        String groups = temp.get(0).deExecuteGroup;
                                        if (TextUtils.equals(groups,bean.deExecuteGroup)){
                                            temp.add(bean);
                                            if (index+1 == planDetailsListBeanList.size()){
                                                planlist.add(temp);
                                            }
                                        }else{
                                            planlist.add(temp);
                                            temp = new ArrayList<>();
                                            temp.add(bean);
                                        }
                                    }
                                }
                                adapter.refresh(planlist);
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

    public void cancel(View view){
        new TipsDialog(this, "确定取消计划？",
                getString(R.string.confirm), getString(R.string.cancel),
                new TipsDialog.OnButtonClickListener() {
            @Override
            public void buttonClicked() {
                projectCancel();
            }
        }).show();
    }

    public void projectCancel(){
        Subscription subscription = RepayMentImpAPI.cancelRefundPlan(HttpParam.OFFICE_CODE,HttpParam.CANCEL_REPAYMENT_PROJECT_KEY,
                MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION,binding.getTotalBean().rpOrderCode)
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
                        if (mofuResult.result_Code==0){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            String defaultEndDate = simpleDateFormat.format(calendar.getTime());

                            calendar.add(Calendar.MONTH,-1);
                            Date beforeMonthDate=calendar.getTime();
                            String defaultBeginDate = simpleDateFormat.format(beforeMonthDate);

                            RxBus.getInstance().post(RxEvent.REFRESH_PROJECT,new String[]{defaultBeginDate,defaultEndDate});
                            finish();
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
