package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.CommissionAdapter;
import cn.mofufin.morf.adapter.IntergalRankAdapter;
import cn.mofufin.morf.databinding.ActivityCommissionBinding;
import cn.mofufin.morf.databinding.ActivityIntergalRankingBinding;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.IntergalRank;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 理财积分排位
 */
public class IntegralRankingMonthActivity extends BaseActivity {
    private ActivityIntergalRankingBinding binding;
    private IntergalRankAdapter adapter;
    private int ranking;
    private int faState;
    private IntergalRank.ActivityBean activityBean;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            judgeRankingStatus(msg.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_intergal_ranking);
        setStatusBar();
        binding.setHandlers(this);
        binding.setFaState(0);
        initView();
    }

    public void initView(){
        String str = getString(R.string.intergal_rank_title1);
        setTitles(str);

        adapter = new IntergalRankAdapter();
        binding.rankingList.setLayoutManager(new LinearLayoutManager(this));
        binding.rankingList.setAdapter(adapter);

        queryFund();
    }


    /**
     * 当月榜
     * @param view
     */
    public void currentMonth(View view){
        switchBtnStatus(2);
        binding.setFaState(faState);
        handler.sendEmptyMessage(0);
        binding.bottomTip.setVisibility(View.VISIBLE);
    }


    /**
     * 上月榜
     * @param view
     */
    public void lastMonth(View view){
        switchBtnStatus(3);
        binding.setFaState(1);
        handler.sendEmptyMessage(1);
        binding.bottomTip.setVisibility(View.INVISIBLE);
    }


    /**
     * 规则奖励
     * @param view
     */
    public void Rules(View view){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("HTML",binding.getUrl());
        intent.putExtra(IntentParam.TITLE,getString(R.string.intergal_rank_btn1));
        startActivity(intent);
        switchBtnStatus(1);
    }


    public void queryFund(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("officeCode",HttpParam.OFFICE_CODE);
        formBody.add("appKey",HttpParam.QUERY_PRODUCT_FUND);
        formBody.add("merchantCode",MerchanInfoDB.queryInfo().merchantCode);
        formBody.add("version",Common.LOAN_VERSION);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Common.HOST+Common.HOSTTYPE+"/fund/queryFundActivity")
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onError(e.getCause(),true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String strBody = body.string();
                Logger.e("body ==="+strBody);
                JSONObject object = JSON.parseObject(strBody);
                String msg = object.getString("result_Msg");
                int code = object.getInteger("result_Code");



                if (code == 0){
                    activityBean = object.getObject("activity",IntergalRank.ActivityBean.class);

                    binding.setUrl(activityBean.faHref);
                    faState = activityBean.faState;
                    binding.setFaState(faState);
                    handler.sendEmptyMessage(0);
                }else
                    showTips(msg);

            }
        });



    }


    public void judgeRankingStatus(int when){
        ranking = 0;
        binding.setWhen(when);
        if (when == 0){//当月
            if (activityBean.fdCurrentMonthList ==null || activityBean.fdCurrentMonthList.size() <= 0){
                binding.setHasData(false);
            }else {
                binding.setHasData(true);
                adapter.refresh(activityBean.fdCurrentMonthList);
                String code = MerchanInfoDB.queryInfo().merchantCode;
                for (int index=0; index < activityBean.fdCurrentMonthList.size(); index++){
                    String mercode = activityBean.fdCurrentMonthList.get(index).merchantCode;
                    if (mercode.equals(code)){
                        ranking = index+1;
                    }
                }
            }

            binding.rankTip.setText(ranking>0?"恭喜您当月排名第"+ranking+"名":"很遗憾您当月未上榜");
        }else {
            if (activityBean.faUpMonthList ==null || activityBean.faUpMonthList.size() <= 0){
                binding.setHasData(false);
            }else {
                binding.setHasData(true);
                adapter.refresh(activityBean.faUpMonthList);
                String code = MerchanInfoDB.queryInfo().merchantCode;
                for (int index=0; index < activityBean.faUpMonthList.size(); index++){
                    String mercode = activityBean.faUpMonthList.get(index).merchantCode;
                    if (mercode.equals(code)){
                        ranking = index+1;
                    }
                }
            }
            binding.rankTip.setText(ranking>0?"恭喜您上月排名第"+ranking+"名":"很遗憾您上月未上榜");
        }

    }


    public void setTitles(String str){
        SpannableString builder = new SpannableString(str);
        //字体着色
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD226")),4,str.length(),SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //加粗
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,str.length(),SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        binding.titles1.setText(builder);
    }

    public void switchBtnStatus(int flag){
        switch (flag){
            case 1:
                binding.btn1.setBackgroundResource(R.drawable.intergral_btn);
                binding.btn1.setTextSize(19);
                binding.btn1.setTextColor(ContextCompat.getColor(this,R.color.rank_text_color));
                binding.btn1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                binding.btn2.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn2.setTextSize(14);
                binding.btn2.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                binding.btn3.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn2.setTextSize(14);
                binding.btn2.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;

            case 2:
                binding.btn2.setBackgroundResource(R.drawable.intergral_btn);
                binding.btn2.setTextSize(19);
                binding.btn2.setTextColor(ContextCompat.getColor(this,R.color.rank_text_color));
                binding.btn2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                binding.btn1.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn1.setTextSize(14);
                binding.btn1.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                binding.btn3.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn3.setTextSize(14);
                binding.btn3.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;

            case 3:
                binding.btn3.setBackgroundResource(R.drawable.intergral_btn);
                binding.btn3.setTextSize(19);
                binding.btn3.setTextColor(ContextCompat.getColor(this,R.color.rank_text_color));
                binding.btn3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                binding.btn2.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn2.setTextSize(14);
                binding.btn2.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                binding.btn1.setBackgroundResource(R.drawable.shape_intergal_storke_btn);
                binding.btn1.setTextSize(14);
                binding.btn1.setTextColor(ContextCompat.getColor(this,R.color.rank_btn_color));
                binding.btn1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        super.submit(view);
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
