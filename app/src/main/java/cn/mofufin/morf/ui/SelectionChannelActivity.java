package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.adapter.ChannelListAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivitySelectionChannelBinding;
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
 * 选择通道
 */
public class SelectionChannelActivity extends BaseActivity {
    private ActivitySelectionChannelBinding binding;
    private ChannelListAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_selection_channel);
        binding.setHandlers(this);

        adapter = new ChannelListAdapter(this);
        adapter.setHorizontalCount(4);
        adapter.setClickListener(clickListener);
        layoutManager = new LinearLayoutManager(this);
        binding.channelList.setLayoutManager(layoutManager);
        binding.channelList.setAdapter(adapter);

        getChannelList();
    }

    public void getChannelList(){
        Subscription subscription = QueryChannelImpAPI.querychannelList(HttpParam.OFFICE_CODE,HttpParam.QUERY_CHANNEL_LIST_APPKEY,
                MerchanInfoDB.queryInfo().merchantCode)
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
                .subscribe(new Action1<Channel>() {
                    @Override
                    public void call(Channel channel) {
                        hiddenLoading();
                        if (channel!=null && channel.getChannelList().size() >0){
                            List<Channel.ChannelListBean> listBeans = channel.getChannelList();
                            adapter.refresh(listBeans);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hiddenLoading();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Channel.ChannelListBean channel = (Channel.ChannelListBean) v.getTag();
            postH5(channel);
        }
    };


    public void postH5(final Channel.ChannelListBean bean){
        //1.创建OkHttpClient对象
        OkHttpClient  okHttpClient = new OkHttpClient();
        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        RequestBody requestBody = new FormBody.Builder()
                .add("officeCode",HttpParam.OFFICE_CODE)
                .add("appKey",HttpParam.QUERY_CHANNEL_H5_APPKEY)
                .add("tcType",String.valueOf(bean.tcType))
                .add("merchantCode",MerchanInfoDB.queryInfo().merchantCode)
                .build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder()
//                .url("http://120.78.213.181/system/shortcut/initChannnel?")
                .url(Common.HOST+Common.HOS_SYSTEM_TYPE+"/shortcut/initChannnel?")
                .post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                String json = new String(bytes);
                Logger.e("okHttp",json);

                if (!TextUtils.isEmpty(json)){
                    startActivity(new Intent(
                            SelectionChannelActivity.this,H5PayActivity.class)
                            .putExtra("HTML",json).putExtra(IntentParam.ACTIVITY_FLAG,SelectionChannelActivity.this.TAG)
                            .putExtra(IntentParam.TITLE,bean.tcName));
                }
            }
        });
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
}
