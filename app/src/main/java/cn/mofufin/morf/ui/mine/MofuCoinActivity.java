package cn.mofufin.morf.ui.mine;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.mofufin.morf.adapter.CoinAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityMofuCoinBinding;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 摩富币
 */
public class MofuCoinActivity extends BaseActivity {
    public static final String MIN_DATE="2018-11";
    private ActivityMofuCoinBinding binding;
    private CoinAdapter adapter;
    private MyDatePickerDialog myDatePickerDialog;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mofu_coin);
        setStatusBar();
        binding.setHandlers(this);
        binding.setPosition(-1);
        initView();
    }

    public void initView(){
        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        binding.setDate(date);
//        initDatePicker();

        binding.rankingList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoinAdapter();
        binding.rankingList.setAdapter(adapter);
        queryMerMobi();
    }

    public void queryMerMobi(){
        Subscription subscription = UserImpAPI.queryMerMobi(HttpParam.QUERY_MERMOBI,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,binding.getDate())
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
                .subscribe(new Action1<Coin>() {
                    @Override
                    public void call(Coin coin) {
                        if (coin.result_Code==0){
                            binding.setCurrentMoBi(String.valueOf(coin.currentMoBi));
                            setMofuCoinRecord(coin.record);
                            if (coin.moBiList.size()>0){
                                String myPhone = MerchanInfoDB.queryInfo().merchantPhone;
                                for (int index=0; index < coin.moBiList.size(); index++){
                                    String phone = coin.moBiList.get(index).phone;
                                    if (TextUtils.equals(myPhone,phone)){
                                        binding.setPosition(index+1);
                                        break;
                                    }
                                }

                                adapter.refresh(coin.moBiList);
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

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        startActivity(new Intent(this,MofuPlayActivity.class));
    }

    public void coindetail(View v){
        Intent intent = new Intent(this,CoinDetailActivity.class);
        startActivity(intent);
    }


    public void selectTime(View view){
        initDatePicker();
        getNowTime(((CheckBox)view).getText().toString());
        showDatePicker2();
        binding.setOnPress(true);
    }

    private void getNowTime(String str) {
        if (!TextUtils.isEmpty(str)){
            year = Integer.valueOf(str.substring(0,str.indexOf("-")));
            monthOfYear = Integer.valueOf(str.substring(str.indexOf("-")+1,str.length()));
            monthOfYear -=1;
            dayOfMonth = 1;
            if (myDatePickerDialog!=null && !myDatePickerDialog.isShowing()){
                myDatePickerDialog.updateDate(year,monthOfYear,dayOfMonth);
            }
        }
    }

    public void setMofuCoinRecord(Coin.Record record){
        String name = CoinAdapter.coverNamePhone(record.merchantName);
        String types="";
        //mrGetType ：摩币获取来源
        // (0:快捷 1:扫码 2:mpos 3:理财 4:推广注册 5:推广实名 6:境外快捷 7:还款  8:摇钱树 9:官方调控)
        switch (record.mrGetType){
            case 0:
                types = getString(R.string.Coin_detail_type_0);
                break;

            case 1:
                types = getString(R.string.Coin_detail_type_1);
                break;

            case 2:
                types = getString(R.string.Coin_detail_type_2);
                break;

            case 3:
                types = getString(R.string.Coin_detail_type_3);
                break;

            case 4:
                types = getString(R.string.Coin_detail_type_4);
                break;

            case 5:
                types = getString(R.string.Coin_detail_type_5);
                break;

            case 6:
                types = getString(R.string.Coin_detail_type_6);
                break;

            case 7:
                types = getString(R.string.Coin_detail_type_7);
                break;

            case 8:
                types = getString(R.string.Coin_detail_type_8);
                break;

            case 9:
                types = getString(R.string.Coin_detail_type_9);
                break;

            case 10:
                types = getString(R.string.Coin_detail_type_10);
                break;

            case 11:
                types = getString(R.string.Coin_detail_type_11);
                break;

            case 12:
                types = getString(R.string.Coin_detail_type_12);
                break;
        }

        binding.marquee.setText(getString(R.string.coin_4,name,types,String.valueOf(record.mrNumber)));
    }

    /**
     * 展示选择时间的dialog
     *@Description: (这里用一句话描述这个方法的作用)
     *@Author: 郭永振
     *@Since: 2017-1-6下午3:33:51
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
            myDatePickerDialog = new MyDatePickerDialog(false,this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    new MyDatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;
                            String date = year + "-"+(month<10?"0":"") + month;
                            binding.setDate(date);
                            queryMerMobi();
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
        }
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
