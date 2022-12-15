package cn.mofufin.morf.ui.mine;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.githang.statusbar.StatusBarCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.mofufin.morf.adapter.CommissionDetailAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.CommissionDetail;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import cn.mofufin.morf.databinding.ActivityCommissionDetailBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 佣金明细
 */
public class CommissionDetailActivity extends BaseActivity {
    private ActivityCommissionDetailBinding binding;
    private CommissionDetailAdapter adapter;
    private MyDatePickerDialog myDatePickerDialog;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_commission_detail);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_blue));
        binding.setHandlers(this);
        initView();
    }

    public void initView(){

        adapter = new CommissionDetailAdapter();
        binding.commissionList.setLayoutManager(new LinearLayoutManager(this));
        binding.commissionList.setAdapter(adapter);

        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        binding.setDate(date);
        queryMerRebateDetail();

        getNowTime();
    }

    /**
     *获取当前时间
     */
    private void getNowTime() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        monthOfYear = now.get(Calendar.MONTH) ;
        dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
    }

    public void queryMerRebateDetail(){
        Subscription subscription = UserImpAPI.queryMerRebateDetail(HttpParam.QUERY_MERREBATE_DETAIL,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode,binding.getDate())
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
                .subscribe(new Action1<CommissionDetail>() {
                    @Override
                    public void call(CommissionDetail commissionDetail) {

                        if (commissionDetail.result_Code==0){
                            binding.setMonthRebate(commissionDetail.monthRebate);
                            adapter.refresh(commissionDetail.detailList);
                            binding.setHasData(commissionDetail.detailList.size()>0);
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

    @TargetApi(Build.VERSION_CODES.N)
    public void selectTime(View view){
        showDatePicker2();
        binding.setOnPress(true);
    }

    /**
     * 展示选择时间的dialog
     *@Description: (这里用一句话描述这个方法的作用)
     *@Author: 郭永振
     *@Since: 2017-1-6下午3:33:51
     */
    public void showDatePicker2() {
        myDatePickerDialog = new MyDatePickerDialog(false,this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new MyDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String date = year + "-"+(month<10?"0":"") + month;
                        binding.setDate(date);
                        queryMerRebateDetail();
                    }
                }, year, monthOfYear, dayOfMonth);
        myDatePickerDialog.myShow();

        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = myDatePickerDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
        myDatePickerDialog.getWindow().setAttributes(lp);

        // 去掉显示日  只显示年月
        ((ViewGroup)((ViewGroup) myDatePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        myDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                binding.setOnPress(false);
            }
        });
    }
}
