package cn.mofufin.morf.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.adapter.BalanceDetailAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BalanceDetail;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;
import cn.mofufin.morf.ui.widget.DoubleTimeSelectDialog;
import cn.mofufin.morf.databinding.ActivityBalanceBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 我的 - 余额
 */
public class BalanceActivity extends BaseActivity {

    private static final String BALANCE_DEFAULT_FINDTYPE="balance_default_findtype";
    private LinearLayoutManager layoutManager;
    private BalanceDetailAdapter adapter;
    private boolean isfilterShow =false;
    private int findType =100;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间为2100年，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd="2100-12-31";

//    public String paramsStrTime = "";

    public final String beginDeadTime = "1900-01-01";

    private String orderBegin="" , orderEnd="";

    private TextView button;
    private ActivityBalanceBinding binding;
    private int offset=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        findType = SharedPreferencesUtils.getInt(this,BALANCE_DEFAULT_FINDTYPE,findType);
        binding.setType(findType);

        adapter = new BalanceDetailAdapter();
        adapter.setOnClickListener(clickListener);
        layoutManager = new LinearLayoutManager(this);
        binding.orderList.setLayoutManager(layoutManager);
        binding.orderList.setAdapter(adapter);

        orderEnd = DataUtils.GetDateOrTime("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        defaultWeekEnd = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.MONTH,-1);
        Date beforeMonthDate=calendar.getTime();
        orderBegin = new SimpleDateFormat("yyyy-MM-dd").format(beforeMonthDate);
        defaultWeekBegin = orderBegin;

        binding.timepicker.setText(defaultWeekBegin+"~"+defaultWeekEnd);

        rxManager.onRxEvent(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        queryBalanceDetail();
                        updateMerchantInfo();
                    }
                });
    }

    @Override
    protected void onResume() {
        RxBus.getInstance().postEmpty(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH);
        super.onResume();
    }

    /**
     * 筛选
     * @param view
     */
    public void filterOrderQuery(View view){
        Animation animation;
        if (!isfilterShow){
            binding.filterWindow.setVisibility(View.VISIBLE);
            animation= AnimationUtils.loadAnimation(this,R.anim.recei_enter_anims);
            binding.filterWindow.startAnimation(animation);
            isfilterShow = true;
        }else {
            animation= AnimationUtils.loadAnimation(this,R.anim.recei_exit_anims);
            binding.filterWindow.startAnimation(animation);
            binding.filterWindow.setVisibility(View.GONE);
            isfilterShow = false;
            selectorClear();
        }
        binding.setOnPress(isfilterShow);
    }

    /**
     * 查询类型
     * @param view
     */
    public void getfindType(View view){
        selectorClear();
        button = (TextView) view;
        button.setTextColor(ContextCompat.getColor(this,R.color.app_blue));
        button.setSelected(true);
        setDefualtSelect();
    }

    /**
     * 查看
     * @param view
     */
    public void checkOrder(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.filterWindow.startAnimation(
                        AnimationUtils.loadAnimation(BalanceActivity.this,R.anim.recei_exit_anims));
                binding.filterWindow.setVisibility(View.GONE);
                isfilterShow = false;
                binding.setOnPress(isfilterShow);
                selectorClear();
            }
        });

        if (button!=null){
            findType = Integer.valueOf((String) button.getTag());
            binding.setType(findType);
            RxBus.getInstance().postEmpty(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH);
        }

    }

    /**
     * 设置默认类型
     * @param view
     */
    public void setDefaultfindtype(View view){
        if (button!=null){
            SharedPreferencesUtils.setInt(this,BALANCE_DEFAULT_FINDTYPE,Integer.valueOf((String) button.getTag()));
            showTips("设置成功");
            setDefaultClear();
        }
    }

    /**
     * 打开日期选择
     */
    public void showCustomTimePicker(View view) {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    orderBegin = startTime;
                    orderEnd = endTime;
                    binding.timepicker.setText(orderBegin+"~"+orderEnd);
                    RxBus.getInstance().postEmpty(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH);
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }

    public void selectorClear(){
        binding.btn1.setSelected(false);
        binding.btn1.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn2.setSelected(false);
        binding.btn2.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn3.setSelected(false);
        binding.btn3.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn4.setSelected(false);
        binding.btn4.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn5.setSelected(false);
        binding.btn5.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn6.setSelected(false);
        binding.btn6.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn7.setSelected(false);
        binding.btn7.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn8.setSelected(false);
        binding.btn8.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn9.setSelected(false);
        binding.btn9.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn10.setSelected(false);
        binding.btn10.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        binding.btn11.setSelected(false);
        binding.btn11.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
    }

    public void setDefaultClear(){
        binding.setdefault.setBackgroundColor(ContextCompat.getColor(this,R.color.line));
    }

    public void setDefualtSelect(){
        binding.setdefault.setBackgroundColor(ContextCompat.getColor(this,R.color.app_blue));
    }

    //更新商户信息
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
                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
                        MerchanInfoDB.updateMerchanInfo(bean);

                        final User.DataBean.MerchantInfoBean beans = MerchanInfoDB.queryInfo();
                        binding.setRMB(DataUtils.numFormat(beans.RMB));
                        binding.setHKDollar(DataUtils.numFormat(beans.HKDollar));
                        binding.setUSDollar(DataUtils.numFormat(beans.USDollar));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        binding.refreshLayout.finishRefresh();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    /**
     * 查询余额明细
     */
    public void queryBalanceDetail(){
        Subscription subscription = UserImpAPI.queryBalanceDetail(HttpParam.QUERY_BALANCE_DETAIL ,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,orderBegin,orderEnd,findType)
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
                .subscribe(new Action1<BalanceDetail>() {
                               @Override
                               public void call(BalanceDetail balanceDetail) {
                                    if (balanceDetail.result_Code == 0){
                                        binding.setIncome(DataUtils.numFormat(balanceDetail.income));
                                        binding.setExpend(DataUtils.numFormat(balanceDetail.expend));

                                        binding.setHasData(balanceDetail.detailList.size()>0);
                                        adapter.refresh(balanceDetail.detailList);
                                    }else {
                                        showTips(balanceDetail.result_Msg);
                                        binding.setIncome("0.00");
                                        binding.setExpend("0.00");

                                        binding.setHasData(false);
                                    }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                onError(throwable,true);
                            }
                        });
        rxManager.add(subscription);
    }

    public void HKDollar(View view){
        Intent intent = new Intent(this,DollarActivity.class);
        intent.putExtra(IntentParam.DOLLAR_TYPE,DollarActivity.TYPE_CURRENT_HK);
        intent.putExtra(IntentParam.DOLLAR_BALANCE,binding.getHKDollar());
        startActivity(intent);
    }

    public void USDollar(View view){
        Intent intent = new Intent(this,DollarActivity.class);
        intent.putExtra(IntentParam.DOLLAR_TYPE,DollarActivity.TYPE_CURRENT_US);
        intent.putExtra(IntentParam.DOLLAR_BALANCE,binding.getUSDollar());
        startActivity(intent);
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        Intent intent = new Intent(this,DepositActivity.class);
        startActivity(intent);
    }

    public void recharge(View view){
        Intent intent = new Intent(this,RechargeToBalanceActivity.class);
        startActivity(intent);
    }

    public void cashWithDrawal(View view){
        Intent intent = new Intent(this,CashWithdrawalActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.getRMB());
        startActivity(intent);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BalanceActivity.this,BalanceDetailActivity.class);
            intent.putExtra(IntentParam.BALANCE_DETAIL_BEAN,(BalanceDetail.DetailLists)v.getTag());
            startActivity(intent);
        }
    };
}
