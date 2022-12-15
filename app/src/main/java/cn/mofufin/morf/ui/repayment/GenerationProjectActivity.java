package cn.mofufin.morf.ui.repayment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.haibin.calendarview.Calendar;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityGenerationProjectBinding;
import cn.mofufin.morf.databinding.ActivityMposBinding;
import cn.mofufin.morf.ui.MPosIndustryActivity;
import cn.mofufin.morf.ui.MerchantPosIndustryActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.FallProvinceCity;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.services.RepayMentAPI;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.CalendarDialog;
import cn.mofufin.morf.ui.widget.MultiplePicker;
import cn.mofufin.morf.ui.widget.SinglePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 生成计划
 */
public class GenerationProjectActivity extends BaseActivity{
    private ActivityGenerationProjectBinding binding;
    private RepayChannel.ChannelListBean channel;
    private CalendarDialog calendarDialog;
    private int[] dates;
    private ArrayList<Calendar> calendarArryList;
    private MultiplePicker picker;
    private FallProvinceCity.ProCityListBean proCityListBean;
    private FallProvinceCity.ProCityListBean.CityListBean cityListBean;
    private StringBuffer calendarBuffer = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_generation_project);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        channel = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setTotalMoney("");
        binding.setChannel(channel);
        dates = getIntent().getIntArrayExtra(IntentParam.REPAY_DAYS);

        picker = new MultiplePicker(this);
        picker.setOnMultipleSelectProCityListener(new MultiplePicker.OnMultipleSelectProCityListener() {
            @Override
            public void multipleSelect(String proCityName, int options1, int options2,
                                       FallProvinceCity.ProCityListBean proCityListBean,
                                       FallProvinceCity.ProCityListBean.CityListBean cityListBean) {

                binding.setProcity(proCityName);
                GenerationProjectActivity.this.proCityListBean = proCityListBean;
                GenerationProjectActivity.this.cityListBean = cityListBean;

                refreshButton();
            }
        });

        calendarArryList = new ArrayList<>();
        calendarDialog = new CalendarDialog(this,dates[0],dates[1]);
        calendarDialog.setOnSelectDayListener(new CalendarDialog.OnSelectDayListener() {
            @Override
            public void selectDay(Set<Calendar> calendarList) {
                calendarArryList.clear();
                calendarBuffer.setLength(0);
                Iterator<Calendar> iterator = calendarList.iterator();
                while(iterator.hasNext()){
                    Calendar calendar = iterator.next();
                    calendarArryList.add(calendar);
                }
                sortList();

                for (int index=0; index < calendarArryList.size(); index++){
                    int month = calendarArryList.get(index).getMonth();
                    int day = calendarArryList.get(index).getDay();
                    String date = calendarArryList.get(index).getYear()+"-"
                            + (month < 10?String.valueOf("0"+month):month) + "-"+(day < 10?String.valueOf("0"+day):day);
                    if (calendarBuffer.length() == 0){
                        calendarBuffer.append(date);
                    }else {
                        calendarBuffer.append(",").append(date);
                    }
                }
//                Logger.e("buff="+calendarBuffer.toString());
                binding.setDaycount(calendarArryList.size());
                refreshButton();
            }
        });

        rxManager.onRxEvent(RxEvent.ON_GENERATION_PROJECT)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        generateProject(binding.getTotalMoney());
                    }
                });
        rxManager.onRxEvent(RxEvent.GENER_PROJECT_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                finish();
            }
        });
        queryProvince();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        calendarDialog = null;
    }

    @Override
    public void submit(View view) {
        Intent intent = new Intent(this,AboutRepayMentActivity.class);
        startActivity(intent);
        super.submit(view);
    }

    public void consumdays(View view){
        calendarDialog.showDialog();
    }

    public void clean(View view){
        binding.sum.setText("");
    }

    public void selectCity(View view){
        if (picker!=null){
            picker.show();
        }
    }


    public void next(View view){
        if (DataUtils.isFastClick())
            return;
        else if (TextUtils.isEmpty(binding.getTotalMoney())){
            showTips("请输入消费金额");
            return;
        }

        RxBus.getInstance().postEmpty(RxEvent.ON_GENERATION_PROJECT);
    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.setTotalMoney(s.toString());
        refreshButton();
    }


    /**
     * 按日期大小排序
     * @param list
     */
    public void sortList(){
        Collections.sort(calendarArryList, new Comparator<Calendar>() {
            @Override
            public int compare(Calendar o1, Calendar o2) {
                int day1 = matchs(o1.getYear(),o1.getMonth(),o1.getDay());
                int day2 = matchs(o2.getYear(),o2.getMonth(),o2.getDay());
                if (day1 > day2)
                    return 1;
                else if (day1 < day2)
                    return -1;

                return 0;
            }
        });
    }

    public static int matchs(int year ,int month ,int day){
        int temp=0;
        if (month > 0 && day > 0){
            String years = String.valueOf(year);
            String months = String.valueOf((month < 10) ? "0"+month : month);
            String days = String.valueOf((day < 10)?"0"+day:day);

            String str = years + months + days;
            temp = Integer.valueOf(str);
        }
        return temp;
    }

    /**
     * 解析省市
     */
    public void queryProvince(){
        Subscription subscription = RepayMentImpAPI.queryChannelFallProvinceCity(HttpParam.QUERY_CHANNEL_FALL_PROVINCE_KEY,
                String.valueOf(channel.rcNumber), Common.LOAN_VERSION)
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
                .subscribe(new Action1<FallProvinceCity>() {
                    @Override
                    public void call(FallProvinceCity province) {
                        if (province.getResult_Code() == 0){
                            picker.setRepayMentChannelFallProvinceCity(province);
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
     * 生成计划
     */
    public void generateProject(String money){
        String cardNo = getIntent().getStringExtra(IntentParam.BANKCARD_NO);
        try{
            Subscription subscription = RepayMentImpAPI.createRefundPlan(HttpParam.GENERRATION_PROJECT_KEY,HttpParam.OFFICE_CODE,
                    Common.LOAN_VERSION,MerchanInfoDB.queryInfo().merchantCode,String.valueOf(binding.getChannel().rcNumber),
                    cardNo,money, URLEncoder.encode(calendarBuffer.toString(),"UTF-8")
                    ,binding.getChannel().rcType == 1?null:proCityListBean.getProvinceCode()
                    ,binding.getChannel().rcType == 1?null:proCityListBean.getProvinceName()
                    ,binding.getChannel().rcType == 1?null:cityListBean.getCityCode()
                    ,binding.getChannel().rcType == 1?null:cityListBean.getCityName())
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
                    .subscribe(new Action1<ProjectResult>() {
                        @Override
                        public void call(ProjectResult result) {
                            if (result.result_Code == 0){
                                Intent intent = new Intent(GenerationProjectActivity.this,ShowProjectsActivity.class);
                                intent.putExtra(IntentParam.BEAN,result);
                                startActivity(intent);
                            }else {
                                showTips(result.result_Msg,2500);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            onError(throwable,true);
                        }
                    });
            rxManager.add(subscription);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void refreshButton(){
        boolean isEnable = true;
        if (TextUtils.isEmpty(binding.getTotalMoney())){
            isEnable = false;
        }else if (binding.getTotalMoney().endsWith(".")){
            isEnable = false;
        }else if (binding.getDaycount() <= 0){
            isEnable = false;
        }

        if (binding.getChannel().rcType == 0){
            if (TextUtils.isEmpty(binding.getProcity())){
                isEnable = false;
            }
        }

        binding.next.setEnabled(isEnable);
    }

}
