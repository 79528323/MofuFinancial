package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.mofufin.morf.adapter.PushMerchantAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.PushMerchant;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityPushMerchantBinding;
import cn.mofufin.morf.ui.widget.MerchanPushPopWindow;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PushMerchantActivity extends BaseActivity {
    private static final String MIN_DATE="2018-08";
    private ActivityPushMerchantBinding binding;
    private PushMerchantAdapter adapter;
    private int queryType = 0;
    private String queryDate;

    private MyDatePickerDialog myDatePickerDialog;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private MerchanPushPopWindow pushPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_push_merchant);
        binding.setHandlers(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.pushList.setLayoutManager(layoutManager);
        binding.setDatePress(false);
        binding.setCountPress(false);
        binding.setType(queryType);

        pushPopWindow = new MerchanPushPopWindow(this);
        pushPopWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView) v;
                queryType = Integer.valueOf((String)view.getTag());
                binding.setType(queryType);
                pushPopWindow.dismiss();
                getPushMerchant(binding.getDate());
            }
        });
        pushPopWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                binding.setCountPress(false);
            }
        });

        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        binding.setDate(date);

        adapter = new PushMerchantAdapter();
        binding.pushList.setAdapter(adapter);
        getPushMerchant(binding.getDate());
    }

    public void selectDirct(View view){
        if (!pushPopWindow.isShowing()){
            pushPopWindow.show();
            binding.setCountPress(true);
        }else
            pushPopWindow.dismiss();
    }

    public void selectTime(View view){
        getNowTime(((CheckBox)view).getText().toString());
        showDatePicker2();
        binding.setDatePress(true);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void getPushMerchant(final String date){
        Subscription subscription = UserImpAPI.getPushMerchant(HttpParam.REQUEST_PUSH_MERCHANTS,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                String.valueOf(queryType),date.equals("全部")?MIN_DATE:date)
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
                .subscribe(new Action1<PushMerchant>() {
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void call(PushMerchant pushMerchant) {
                        adapter.refresh(null);
                        if (pushMerchant.result_Code==0){
                            if (pushMerchant.queryType.equals("0")){
                                String direct = getString(R.string.direct_push_count,pushMerchant.directList.size());
                                SpannableString directSpannable = new SpannableString(direct);
                                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
                                directSpannable.setSpan(relativeSizeSpan,7,direct.length()-1,directSpannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.btn.setText(directSpannable);
                                adapter.refresh(pushMerchant.directList);
                                binding.setHasData(pushMerchant.directList.size()>0);
                            }else {
                                binding.setIndirect(String.valueOf(pushMerchant.indirectTotalPersonNumber));
                                binding.setOfficeName(pushMerchant.officeName);
                            }

                        }else
                            showTips(pushMerchant.result_Msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    private void getNowTime(String str) {
        if (!TextUtils.isEmpty(str)){
            if (!str.equals("全部")){
                year = Integer.valueOf(str.substring(0,str.indexOf("-")));
                monthOfYear = Integer.valueOf(str.substring(str.indexOf("-")+1,str.length()));
                monthOfYear -=1;
                dayOfMonth = 1;
            }else {
                Calendar now = Calendar.getInstance();
                year = now.get(Calendar.YEAR);
                monthOfYear = now.get(Calendar.MONTH) ;
                dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
            }
        }
    }

    /**
     * 展示选择时间的dialog
     *@Description: (这里用一句话描述这个方法的作用)
     *@Author: 郭永振
     *@Since: 2017-1-6下午3:33:51
     */
    public void showDatePicker2() {
        myDatePickerDialog = new MyDatePickerDialog(true,this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new MyDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String date = year + "-"+(month<10?"0":"") + month;
                        binding.setDate(date);
                        getPushMerchant(binding.getDate());
                    }
                }, year, monthOfYear, dayOfMonth);
        myDatePickerDialog.setQueryALLClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setDate("全部");
                myDatePickerDialog.dismiss();
                getPushMerchant(binding.getDate());
            }
        });
        myDatePickerDialog.setMinDate(MIN_DATE);
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
                binding.setDatePress(false);
            }
        });
    }
}
