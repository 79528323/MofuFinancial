package cn.mofufin.morf.ui.repayment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.ShowProjectAdapter;
import cn.mofufin.morf.databinding.ActivityDollarBinding;
import cn.mofufin.morf.databinding.ActivityShowProjectsBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ExChanged;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.mine.DollarTransferActivity;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShowProjectsActivity extends BaseActivity {
    private ActivityShowProjectsBinding binding;
    private ShowProjectAdapter adapter;
    private TotalCostWindow window;
    private RotateAnimation rotateAnimation;
    public List<List<ProjectResult.PlanDataListBean.DetailsBean>> detailsGrounp = null;
    public boolean isRequest = false;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                binding.icons.clearAnimation();
                binding.refreshLayout.finishRefresh();
                if (rotateAnimation !=null){
                    rotateAnimation.cancel();
                    rotateAnimation = null;
                }

//                hiddenLoading();
            }else {
//                showLoading();
                rotations();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_projects);
        binding.setHandlers(this);
        Logger.e(TAG,"onCreate");
        initView();
    }

    public void initView(){
        adapter = new ShowProjectAdapter();
        binding.projectList.setLayoutManager(new LinearLayoutManager(this));
        binding.projectList.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh(null);
            }
        });
        binding.refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        ProjectResult result = intent.getParcelableExtra(IntentParam.BEAN);
        splitPackResult(result);
    }

    public void showPop(View view){
        if (binding.getTotalBean()==null)
            return;

        window = new TotalCostWindow(this,binding.getTotalBean().rpConsumeTotalFee,binding.getTotalBean().rpTotalFee);
        View contentView = window.getContentView();
        contentView.measure(0,0);
        int width = contentView.getMeasuredWidth();
        int height = contentView.getMeasuredHeight();
        window.showAsDropDown(view,-width,-(height/2 + 20), Gravity.LEFT);
    }


    public void splitPackResult(final ProjectResult result){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (result!=null){
                    detailsGrounp = new ArrayList<>();
                    binding.setTotalBean(result.planDataList.total);
                    List<ProjectResult.PlanDataListBean.DetailsBean> temp = new ArrayList<>();
                    List<ProjectResult.PlanDataListBean.DetailsBean> detailsBeanList = result.planDataList.details;
                    if (!detailsBeanList.isEmpty()){
                        for (int index=0; index < detailsBeanList.size(); index++){
                            ProjectResult.PlanDataListBean.DetailsBean projectbean = detailsBeanList.get(index);
                            if (temp.size() <= 0){
                                temp.add(projectbean);
                                continue;
                            }

                            String groups = temp.get(0).deExecuteGroup;
                            if (TextUtils.equals(groups , projectbean.deExecuteGroup)){
                                temp.add(projectbean);
                                if (index+1 == detailsBeanList.size()){
                                    detailsGrounp.add(temp);
                                }
                            }else{
                                detailsGrounp.add(temp);
                                temp = new ArrayList<>();
                                temp.add(projectbean);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.refresh(detailsGrounp);
                            }
                        });

                    }
                }else
                    showTips(getIntent().getStringExtra(IntentParam.TIPS));

                handler.sendEmptyMessageDelayed(0,200);
            }
        }).start();

    }

    public void executeProject(View view){
        Subscription subscription = RepayMentImpAPI.executeRefundPlan(HttpParam.REPAYMENT_PROJECT_EXECUTE_KEY,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                binding.getTotalBean().rpOrderCode)
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
                        if (mofuResult.result_Code == 0){
//                            showTips(mofuResult.result_Msg);
                            new TipsDialog(ShowProjectsActivity.this,
                                    mofuResult.result_Msg, getString(R.string.confirm), null,
                                    new TipsDialog.OnButtonClickListener() {
                                        @Override
                                        public void buttonClicked() {
                                            RxBus.getInstance().postEmpty(RxEvent.SHOW_FRAGMENT_REPAY);

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(new Date());
                                            String defaultEndDate = simpleDateFormat.format(calendar.getTime());

                                            calendar.add(Calendar.MONTH,-1);
                                            Date beforeMonthDate=calendar.getTime();
                                            String defaultBeginDate = simpleDateFormat.format(beforeMonthDate);

                                            RxBus.getInstance().post(RxEvent.REFRESH_PROJECT,new String[]{defaultBeginDate,defaultEndDate});
                                            RxBus.getInstance().postEmpty(RxEvent.GENER_PROJECT_FINISH);
                                            finish();
                                        }
                                    }).show();
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

    /**
     * 刷新计划
     * @param view
     */
    public void refresh(View view){
        if (isRequest)
            return;

        isRequest =true;
//        rotations();
        handler.sendEmptyMessage(1);
    }


    class TotalCostWindow extends PopupWindow{

        public TotalCostWindow(Context context,double rpConsumeTotalFee,double rpTotalFee) {
            super(context);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setOutsideTouchable(true);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_popup_totalcost,null,false);
            setContentView(view);
            ((TextView)view.findViewById(R.id.total_consume)).setText(getString(R.string.show_pro_3,DataUtils.numFormat(rpTotalFee)));
            ((TextView)view.findViewById(R.id.total_repay)).setText(getString(R.string.show_pro_4,DataUtils.numFormat(rpConsumeTotalFee)));
            ((TextView)view.findViewById(R.id.total_statistics)).setText(getString(R.string.show_pro_5,DataUtils.numFormat(rpConsumeTotalFee + rpTotalFee)));
        }
    }


    public void rotations(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setDuration(750);
                rotateAnimation.setFillAfter(false);
                rotateAnimation.setRepeatMode(Animation.RESTART);
                //让旋转动画一直转，不停顿的重点
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setRepeatCount(100);//重复次数
                rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        binding.refreshLayout.autoRefresh();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RxBus.getInstance().postEmpty(RxEvent.ON_GENERATION_PROJECT);
                            }
                        },500);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isRequest = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                binding.icons.startAnimation(rotateAnimation);
            }
        });
    }

}
