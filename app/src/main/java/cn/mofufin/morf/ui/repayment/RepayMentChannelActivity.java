package cn.mofufin.morf.ui.repayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.ChannelListAdapter;
import cn.mofufin.morf.adapter.RepayMentChannelListAdapter;
import cn.mofufin.morf.databinding.ActivityRepaymentChannelBinding;
import cn.mofufin.morf.databinding.ActivitySelectionChannelBinding;
import cn.mofufin.morf.ui.H5PayActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 卡延期选择通道
 */
public class RepayMentChannelActivity extends BaseActivity {
    private ActivityRepaymentChannelBinding binding;
    private RepayMentChannelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_repayment_channel);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){

        adapter = new RepayMentChannelListAdapter();
        adapter.setClickListener(clickListener);
        adapter.setSupportListener(supportListener);

        binding.channelList.setLayoutManager(new LinearLayoutManager(this));
        binding.channelList.setAdapter(adapter);
        getChannelList();
    }

    public void getChannelList(){
        Subscription subscription = RepayMentImpAPI.queryChannel(HttpParam.OFFICE_CODE,
                HttpParam.QUERY_REPAY_CHANNEL_KEY,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RepayChannel>() {
                    @Override
                    public void call(RepayChannel channel) {
                        if (channel.result_Code==0){
                            List<RepayChannel.ChannelListBean> listBeans = channel.channelList;
//                            for (int i=0; i<3;i++){
//                                listBeans.addAll(listBeans);
//                            }
                            adapter.refresh(listBeans);
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
            RepayChannel.ChannelListBean channel = (RepayChannel.ChannelListBean) v.getTag();
            Intent intent = new Intent(RepayMentChannelActivity.this,RepayMentManagerActivity.class);
            intent.putExtra(IntentParam.BEAN,channel);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    };

    View.OnClickListener supportListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int rcNumber = (int) v.getTag();
            Intent intent = new Intent(RepayMentChannelActivity.this,RepayMentSupportBankListActivity.class);
            intent.putExtra(IntentParam.NUMBER,String.valueOf(rcNumber));
            startActivity(intent);
        }
    };

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
