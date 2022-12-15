package cn.mofufin.morf.ui.repayment.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.BankCardAdapter;
import cn.mofufin.morf.adapter.RepayMentProjectListAdapter;
import cn.mofufin.morf.databinding.FragmentRepaymentProjectBinding;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.RepayMentProject;
import cn.mofufin.morf.ui.mine.SettlementCardAddActivity;
import cn.mofufin.morf.ui.repayment.RepayMentProjectDetailsActivity;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.DoubleTimeSelectDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 延期
 */
public class RepayMentProjectFragment extends BaseFragment {
    private final String FORMATTED_DATE="yyyy-MM-dd";
    private FragmentRepaymentProjectBinding binding;
    private RepayMentProjectListAdapter adapter;

    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultBeginDate;
    /**
     * 默认的周结束时间为2100年，格式如：yyyy-MM-dd
     */
    public String defaultEndDate ="2100-12-31";
    public static final String doubleTimeEarliestTime = "1900-01-01";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_repayment_project,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        binding.setHasData(false);
        binding.setOnPress(false);
        initView();
    }


    public void initView(){
        RxBus.getInstance().post(RxEvent.REPAYMENT_TITLE,getString(R.string.repayment_2));
        binding.projectList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RepayMentProjectListAdapter();
        adapter.setClickListener(clickListener);
        binding.projectList.setAdapter(adapter);

        getCurCalendarDay();
        getProject(defaultBeginDate,defaultEndDate);

        rxManager.onRxEvent(RxEvent.REFRESH_PROJECT).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o!=null){
                    String[] arry = (String[]) o;
                    arry[0] = defaultBeginDate;
                    arry[1] = defaultEndDate;
                }
                getCurCalendarDay();
                getProject(defaultBeginDate,defaultEndDate);
            }
        });
    }

    public void getProject(String sd,String ed){
        Subscription subscription = RepayMentImpAPI.queryRefundPlan(HttpParam.QUERY_REFUND_PROJECT_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,sd,ed)
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
                .subscribe(new Action1<RepayMentProject>() {
                    @Override
                    public void call(RepayMentProject project) {
                        if (project.result_Code==0){
                            binding.setTotalConsumeMoney(project.totalConsumeMoney);
                            binding.setTotalRefundMoney(project.totalRefundMoney);
                            adapter.refresh(project.planList);
                            binding.setHasData(project.planList.size() > 0);
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


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RepayMentProject.PlanListBean project = (RepayMentProject.PlanListBean) v.getTag();
            Intent intent = new Intent(getActivity(), RepayMentProjectDetailsActivity.class);
            intent.putExtra(IntentParam.BEAN,project);
            startActivity(intent);
        }
    };

    public void showCustomTimePicker(View view) {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(getActivity(), doubleTimeEarliestTime, defaultBeginDate, defaultEndDate);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    binding.timepicker.setText(startTime+"~"+endTime);
                    getProject(startTime,endTime);
                    defaultBeginDate = startTime;
                    defaultEndDate = endTime;
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    binding.setOnPress(false);
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
            binding.setOnPress(true);
        }
    }


    public void getCurCalendarDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATTED_DATE, Locale.CHINESE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        defaultEndDate = simpleDateFormat.format(calendar.getTime());

        calendar.add(Calendar.MONTH,-1);
        Date beforeMonthDate=calendar.getTime();
        defaultBeginDate = simpleDateFormat.format(beforeMonthDate);
        binding.timepicker.setText(defaultBeginDate+"~"+defaultEndDate);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        RxBus.getInstance().post(RxEvent.REPAYMENT_TITLE,getString(R.string.repayment_title));
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
