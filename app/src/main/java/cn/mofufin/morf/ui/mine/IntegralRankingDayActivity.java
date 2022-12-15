package cn.mofufin.morf.ui.mine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.IntergalRankAdapter;
import cn.mofufin.morf.adapter.IntergalRankDayAdapter;
import cn.mofufin.morf.databinding.ActivityIntergalRankingBinding;
import cn.mofufin.morf.databinding.ActivityIntergalRankingDayBinding;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.IntergalRank;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 理财积分排位 日榜
 */
public class IntegralRankingDayActivity extends BaseActivity {
    private ActivityIntergalRankingDayBinding binding;
    private IntergalRankDayAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_intergal_ranking_day);
        setStatusBar();
        binding.setHandlers(this);
        binding.setFaDayState(0);
        initView();
    }

    public void initView(){
        adapter = new IntergalRankDayAdapter();
        binding.rankingList.setLayoutManager(new LinearLayoutManager(this));
        binding.rankingList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        binding.rankingList.setAdapter(adapter);
        queryFund();
    }


    /**
     * 当日榜
     * @param view
     */
    public void currentDay(View view){
        switchBtnStatus(1);
        binding.setFaDayState(activityBean.faDayState);
        handler.sendEmptyMessage(0);
    }


    /**
     * 昨日榜
     * @param view
     */
    public void lastDay(View view){
        switchBtnStatus(2);
        binding.setFaDayState(1);
        handler.sendEmptyMessage(1);
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
                    binding.setFaDayState(activityBean.faDayState);


                    handler.sendEmptyMessage(0);
                }else
                    showTips(msg);

            }
        });
    }


    public void judgeRankingStatus(int when){
        ranking = 0;
        binding.setWhen(when);
        List<IntergalRank.ActivityBean.FdDayListBean> templist=null;
        if (when == 0){//当月
            if (activityBean.faCurrentDayList ==null || activityBean.faCurrentDayList.size() <= 0){
                binding.setHasData(false);
            }else {
                binding.setHasData(true);
                adapter.refresh(activityBean.faCurrentDayList);
                templist = activityBean.faCurrentDayList;
                String code = MerchanInfoDB.queryInfo().merchantCode;
                for (int index=0; index < activityBean.faCurrentDayList.size(); index++){
                    String mercode = activityBean.faCurrentDayList.get(index).merchantCode;
                    if (mercode.equals(code)){
                        ranking = index+1;
                    }
                }
            }
            if (ranking <=0 ){
                binding.tips.setText("很遗憾您今日未上榜");
            }else if (ranking < 4){
                binding.tips.setText("恭喜您今日排名第"+ranking+"名");
            }else {
                //获取季军积分
                IntergalRank.ActivityBean.FdDayListBean bean = activityBean.faCurrentDayList.get(2);
                int intergal = bean.integral;
                IntergalRank.ActivityBean.FdDayListBean  mine = activityBean.faCurrentDayList.get(ranking-1);
                binding.tips.setText("距离第三名相差"+(intergal - mine.integral)+"积分");
            }

//            binding.tips.setText(ranking>0?"恭喜您今日排名第"+ranking+"名":"很遗憾您今日未上榜");
        }else {
            if (activityBean.faUpDayList ==null || activityBean.faUpDayList.size() <= 0){
                binding.setHasData(false);
            }else {
                binding.setHasData(true);
                adapter.refresh(activityBean.faUpDayList);
                templist = activityBean.faUpDayList;
                String code = MerchanInfoDB.queryInfo().merchantCode;
                for (int index=0; index < activityBean.faUpDayList.size(); index++){
                    String mercode = activityBean.faUpDayList.get(index).merchantCode;
                    if (mercode.equals(code)){
                        ranking = index+1;
                    }
                }
            }
            binding.tips.setText(ranking>0?"恭喜您昨日排名第"+ranking+"名":"很遗憾您昨日未上榜");
        }

        setChampionRank(templist);
    }


    public void switchBtnStatus(int flag){
        switch (flag){
            case 1:
                binding.btn1.setBackgroundResource(R.drawable.intergral_btn);
                binding.btn1.setTextColor(Color.parseColor("#F02E32"));

                binding.btn2.setBackgroundResource(R.drawable.shape_intergal_day_storke_btn);
                binding.btn2.setTextColor(Color.parseColor("#ffffff"));

                binding.timeTip.setVisibility(View.VISIBLE);
                break;

            case 2:
                binding.btn2.setBackgroundResource(R.drawable.intergral_btn);
                binding.btn2.setTextColor(Color.parseColor("#F02E32"));

                binding.btn1.setBackgroundResource(R.drawable.shape_intergal_day_storke_btn);
                binding.btn1.setTextColor(Color.parseColor("#ffffff"));

                binding.timeTip.setVisibility(View.INVISIBLE);
                break;

        }
    }


    public void setChampionRank(List<IntergalRank.ActivityBean.FdDayListBean> list){
//        List<IntergalRank.ActivityBean.FdDayListBean> list =activityBean.faCurrentDayList;
        binding.setBean1st(null);
        binding.setBean2nd(null);
        binding.setBean3rd(null);
        if (list!=null){
            if (list.size() >= 3){
                binding.setBean1st(list.get(0));
                binding.setBean2nd(list.get(1));
                binding.setBean3rd(list.get(2));
            }else {
                if (list.size() > 1){
                    binding.setBean1st(list.get(0));
                    binding.setBean2nd(list.get(1));
                }else
                    binding.setBean1st(list.get(0));
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
