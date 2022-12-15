package cn.mofufin.morf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.ChannelListAdapter;
import cn.mofufin.morf.adapter.OverChannelListAdapter;
import cn.mofufin.morf.databinding.ActivityOverseasChancesBinding;
import cn.mofufin.morf.databinding.ActivitySelectionChannelBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
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
 * 选择通道
 */
public class OverseasChancesActivity extends BaseActivity {
    private ActivityOverseasChancesBinding binding;
    private OverChannelListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_overseas_chances);
        binding.setHandlers(this);

        adapter = new OverChannelListAdapter();
        adapter.setClickListener(overitemClickListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.overList.setLayoutManager(layoutManager);
        binding.overList.setAdapter(adapter);
        getOverChannel();
    }

    //查询境外通道
    public void getOverChannel(){
        Subscription subscription = QueryChannelImpAPI.queryOverChannelInfo(
                HttpParam.QUERY_OVER_APPKEY,HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode)
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
                .subscribe(new Action1<Overseans>() {
                    @Override
                    public void call(Overseans overseans) {
                        if (overseans.result_Code == 0){
                            List<Overseans.OverListBean> datas = new ArrayList<>();
                            Iterator<Overseans.OverListBean> iterator = overseans.overList.iterator();
                            binding.setHasData(overseans.overList.size()>0);

                            List<Overseans.OverListBean> list_H5 = new ArrayList<>();
                            List<Overseans.OverListBean> list_QR = new ArrayList<>();
                            while (iterator.hasNext()){
                                Overseans.OverListBean bean = iterator.next();
                                bean.setType(1);
                                if (bean.channelKind == 0){
                                    list_H5.add(bean);
                                }else
                                    list_QR.add(bean);
                            }

                            Overseans.OverListBean temp = null;
                            if (!list_H5.isEmpty()){
                                temp = new Overseans.OverListBean();
                                temp.setType(0);//TODO  0是title 1是数据
                                temp.setChannelName("线上h5收款");
                                list_H5.add(0,temp);
                                datas.addAll(list_H5);
                            }

                            if (!list_QR.isEmpty()){
                                temp = new Overseans.OverListBean();
                                temp.setType(0);
                                temp.setChannelName("二维码收款");
                                list_QR.add(0,temp);
                                datas.addAll(list_QR);
                            }

                            adapter.refresh(datas);
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


    View.OnClickListener overitemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Overseans.OverListBean bean = (Overseans.OverListBean) v.getTag();
            Intent intent = new Intent(OverseasChancesActivity.this,OverseasActivity.class);
            intent.putExtra(IntentParam.BEAN,bean);
            startActivity(intent);
        }
    };


    @Override
    protected boolean enableSliding() {
        return true;
    }

}
