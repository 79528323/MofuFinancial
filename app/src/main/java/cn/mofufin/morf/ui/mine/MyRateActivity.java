package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.adapter.MyOverSeansRateAdapter;
import cn.mofufin.morf.adapter.MyRateAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.MposRatio;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityMyRateBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.schedulers.Schedulers;

public class MyRateActivity extends BaseActivity {
    private ActivityMyRateBinding binding;
    private int merberType;
    private MyRateAdapter adapter;
    private MyOverSeansRateAdapter overSeansRateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_rate);
        binding.setHandlers(this);

        merberType = MerchanInfoDB.queryInfo().memberType;
        initRate();
//        getChannelList();
//        getOverSeansChannel();
        queryZips();
    }

    public void initRate(){
//        String life = "" , other ="" ,  percent="%";
//        String charge = percent+" + "+Common.MPOS_CHARGE;//手续费

//        if (merberType == User.ORDINARY_MEMBER){
//            life = String.valueOf(Common.RATE_MPOS_ORDINARY_LIFE);
//            other = String.valueOf(Common.RATE_MPOS_ORDINARY_STANDARD);
//        }else if (merberType == User.GOLDEN_MEMBER){
//            life = String.valueOf(Common.RATE_MPOS_GOLDEM_LIFE);
//            other = String.valueOf(Common.RATE_MPOS_GOLDEM_STANDARD);
//        }else {
//            life = String.valueOf(Common.RATE_MPOS_DIAMONDS_LIFE);
//            other = String.valueOf(Common.RATE_MPOS_DIAMONDS_STANDARD);
//        }
//        binding.setLife(life+charge);
//        binding.setOther(other+charge);

        adapter = new MyRateAdapter();
        binding.rateList.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        binding.rateList.setNestedScrollingEnabled(false);
        binding.rateList.setHasFixedSize(true);
        binding.rateList.setFocusable(false);

        binding.rateList.setAdapter(adapter);


        overSeansRateAdapter = new MyOverSeansRateAdapter();
        binding.overRateList.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        binding.overRateList.setNestedScrollingEnabled(false);
        binding.overRateList.setHasFixedSize(true);
        binding.overRateList.setFocusable(false);

        binding.overRateList.setAdapter(overSeansRateAdapter);
    }

    //数据获取集合接口
    public void queryZips(){
        Subscription subscription = Observable.zip(
                UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                    HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY, MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION, "0"),
                UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                    HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY, MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION, "1"),
                QueryChannelImpAPI.queryOverChannelInfo(
                    HttpParam.QUERY_OVER_APPKEY,HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode),
                QueryChannelImpAPI.querychannelList(HttpParam.OFFICE_CODE,HttpParam.QUERY_CHANNEL_LIST_APPKEY,
                        MerchanInfoDB.queryInfo().merchantCode)
                , new Func4<MposRatio, MposRatio,Overseans,Channel, List<Object>>() {
            @Override
            public List<Object> call(MposRatio mposRatio, MposRatio mposRatio2,Overseans overseans,Channel channel) {
                List<Object> objectList = new ArrayList<>();
                double[] arry = new double[]{mposRatio.ratio,mposRatio.fee,mposRatio2.ratio,mposRatio2.fee};
                objectList.add(arry);

                if (channel!=null && channel.getChannelList().size() >0){
                    List<Channel.ChannelListBean> listBeans = channel.getChannelList();
                    objectList.add(listBeans);
//                    adapter.refresh(listBeans);
                }


                if (overseans.result_Code == 0){
                    List<Overseans.OverListBean> list = overseans.overList;
                    Overseans.OverListBean temp=null;
                    List<Overseans.OverListBean> datas = new ArrayList<>();
                    for (int index=0; index < list.size(); index++){
                        temp = list.get(index);
                        Overseans.OverListBean d0 = temp;
                        d0.setType(0);
                        datas.add(d0);

                        if (temp.isSupportT1 == 1){
                            Overseans.OverListBean t1 = new Overseans.OverListBean();
                            t1.setType(1);
                            t1.setChannelKind(temp.getChannelKind());
                            t1.setChannelMegMoney(temp.getChannelMegMoney());
                            t1.setChannelName(temp.getChannelName());
                            t1.setChannelNumber(temp.getChannelNumber());
                            t1.setChannelPayBegin(temp.getChannelPayBegin());
                            t1.setChannelPayEnd(temp.getChannelPayEnd());
                            t1.setChannelQuotaBegin(temp.getChannelQuotaBegin());
                            t1.setChannelQuotaEnd(temp.getChannelQuotaEnd());
                            t1.setMerCommonCostringD0(temp.getMerCommonCostringD0());
                            t1.setMerCommonCostringT1(temp.getMerCommonCostringT1());
                            t1.setMerGoldCostringD0(temp.getMerGoldCostringD0());
                            t1.setMerGoldCostringT1(temp.getMerGoldCostringT1());
                            t1.setMerDrillCostringD0(temp.getMerDrillCostringD0());
                            t1.setMerDrillCostringT1(temp.getMerDrillCostringT1());
                            datas.add(t1);
                        }
                    }
                    objectList.add(datas);
//                    overSeansRateAdapter.refresh(datas);
                }

                return objectList;
            }
        })
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
                    public void call() {hiddenLoading();
                    }
                })
                .subscribe(new Action1<List<Object>>() {
                    @Override
                    public void call(List<Object> objectList) {
                        double[] doubles = (double[]) objectList.get(0);
                        binding.setLife(DataUtils.converOverPercent(
                                doubles[0])+"+"+DataUtils.numFormat(doubles[1]));
                        binding.setOther(DataUtils.converOverPercent(
                                doubles[2])+"+"+DataUtils.numFormat(doubles[3]));

                        binding.setAlipay(DataUtils.converOverPercent(Common.RATE_SCAN_ALIPAY)+" + 0");
                        binding.setWechat(DataUtils.converOverPercent(Common.RATE_SCAN_WEIXIN)+" + 0");

                        List<Channel.ChannelListBean> listBeans = (List<Channel.ChannelListBean>) objectList.get(1);
                        adapter.refresh(listBeans);

                        List<Overseans.OverListBean> datas = (List<Overseans.OverListBean>) objectList.get(2);
                        overSeansRateAdapter.refresh(datas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
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

    public void getOverSeansChannel(){
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
                            List<Overseans.OverListBean> list = overseans.overList;
                            Overseans.OverListBean temp=null;
                            List<Overseans.OverListBean> datas = new ArrayList<>();
                            for (int index=0; index < list.size(); index++){
                                temp = list.get(index);
                                Overseans.OverListBean d0 = temp;
                                d0.setType(0);
                                datas.add(d0);

                                if (temp.isSupportT1 == 1){
                                    Overseans.OverListBean t1 = new Overseans.OverListBean();
                                    t1.setType(1);
                                    t1.setChannelKind(temp.getChannelKind());
                                    t1.setChannelMegMoney(temp.getChannelMegMoney());
                                    t1.setChannelName(temp.getChannelName());
                                    t1.setChannelNumber(temp.getChannelNumber());
                                    t1.setChannelPayBegin(temp.getChannelPayBegin());
                                    t1.setChannelPayEnd(temp.getChannelPayEnd());
                                    t1.setChannelQuotaBegin(temp.getChannelQuotaBegin());
                                    t1.setChannelQuotaEnd(temp.getChannelQuotaEnd());
                                    t1.setMerCommonCostringD0(temp.getMerCommonCostringD0());
                                    t1.setMerCommonCostringT1(temp.getMerCommonCostringT1());
                                    t1.setMerGoldCostringD0(temp.getMerGoldCostringD0());
                                    t1.setMerGoldCostringT1(temp.getMerGoldCostringT1());
                                    t1.setMerDrillCostringD0(temp.getMerDrillCostringD0());
                                    t1.setMerDrillCostringT1(temp.getMerDrillCostringT1());
                                    datas.add(t1);
                                }
                            }

                            overSeansRateAdapter.refresh(datas);
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

}
