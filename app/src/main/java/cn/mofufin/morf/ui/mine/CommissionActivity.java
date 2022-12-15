package cn.mofufin.morf.ui.mine;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.adapter.CommissionAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityCommissionBinding;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 佣金
 */
public class CommissionActivity extends BaseActivity {
    public static final String MIN_DATE="2018-10";
    private ActivityCommissionBinding binding;
    private CommissionAdapter adapter;
    private MyDatePickerDialog myDatePickerDialog;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_commission);
        setStatusBar();
        binding.setHandlers(this);
        binding.setPosition(-1);
        initView();
    }

    public void initView(){
        binding.rankingList.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        binding.rankingList.setNestedScrollingEnabled(false);
        binding.rankingList.setHasFixedSize(true);
        binding.rankingList.setFocusable(false);
        adapter = new CommissionAdapter();
        adapter.setMypos(-1);
        binding.rankingList.setAdapter(adapter);

        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        binding.setDate(date);

//        initDatePicker();
        rxManager.onRxEvent(RxEvent.REFRESH_QUERY_MERREBATE).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                queryMerRebate();
            }
        });

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_QUERY_MERREBATE);
    }

    private void getNowTime(String str) {
        if (!TextUtils.isEmpty(str)){
            year = Integer.valueOf(str.substring(0,str.indexOf("-")));
            monthOfYear = Integer.valueOf(str.substring(str.indexOf("-")+1,str.length()));
            monthOfYear -=1;
            dayOfMonth = 1;
            if (myDatePickerDialog!=null){
                myDatePickerDialog.updateDate(year,monthOfYear,dayOfMonth);
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
        startActivity(new Intent(this,CommissionDetailActivity.class));
    }

    public void transferBalance(View v){
        Intent intent = new Intent(this,BalanceTransferActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.getCommssionMoney());
        startActivity(intent);
    }

    public void selectTime(View view){
        initDatePicker();
        getNowTime(((CheckBox)view).getText().toString());
        showDatePicker2();
        binding.setOnPress(true);
    }

    /**
     * 展示选择时间的dialog
     * @Description: (这里用一句话描述这个方法的作用)
     * @Author: 郭永振
     * @Since: 2017-1-6下午3:33:51
     */
    public void showDatePicker2() {
        if (myDatePickerDialog!=null && !myDatePickerDialog.isShowing()){
            myDatePickerDialog.myShow();

            // 将对话框的大小按屏幕大小的百分比设置
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager.LayoutParams lp = myDatePickerDialog.getWindow().getAttributes();
            lp.width = (int)(metrics.widthPixels * 0.8); //设置宽度
            myDatePickerDialog.getWindow().setAttributes(lp);

        }
    }

    public void initDatePicker(){
        getNowTime(binding.getDate());
        if (myDatePickerDialog==null){
            showLoading();
            myDatePickerDialog = new MyDatePickerDialog(false,this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    new MyDatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;
                            String date = year + "-"+(month<10?"0":"") + month;
                            binding.setDate(date);
                            queryMerRebate();
                        }
                    }, year, monthOfYear, dayOfMonth);
            myDatePickerDialog.setMinDate(MIN_DATE);

            // 去掉显示日  只显示年月
            ((ViewGroup)((ViewGroup) myDatePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            myDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    binding.setOnPress(false);
                }
            });
            hiddenLoading();
        }
    }


    public void queryMerRebate(){
        Subscription subscription = UserImpAPI.queryMerRebate(HttpParam.QUERY_MERREBATE,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                binding.getDate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<Ranking>() {
                    @Override
                    public void call(Ranking ranking) {
                        if (ranking.result_Code==0){
                            binding.setCommssionMoney(DataUtils.numFormat(ranking.commssionMoney));
                            binding.setTotalCommssion(getString(R.string.commission_history,DataUtils.numFormat(ranking.totalCommssion)));
                            if (ranking.rankingList.size()>0){
                                String myPhone = MerchanInfoDB.queryInfo().merchantPhone;
                                for (int index=0; index < ranking.rankingList.size(); index++){
                                    String phone = ranking.rankingList.get(index).incomePersonPhone;
                                    if (TextUtils.equals(myPhone,phone)){
                                        adapter.setMypos(index);
                                        binding.setPosition(index+1);
                                        break;
                                    }
                                }

                                adapter.refresh(ranking.rankingList);
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


    public void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    /*| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.WHITE);
        }else {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
