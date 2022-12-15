package cn.mofufin.morf.ui.fragment;


import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.adapter.MallVirtualAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.MerchantDetailActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.databinding.FragmentMallVirtualBinding;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.GridDividerItemDecoration;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 虚拟物品
 */
public class MallVirtualFragment extends BaseFragment {
    private FragmentMallVirtualBinding binding;
    private int type;
    private MallVirtualAdapter adapter;
    private int coin=0;
    private GridLayoutManager layoutManager;
    private int spanCount = 2;
    //代付类，快捷类，境外类，扫码类，理财类，会员类
    private static final int TYPE_PAY = 1;
    private static final int TYPE_QUICK = 2;
    private static final int TYPE_OVER = 3;
    private static final int TYPE_SCAN = 4;
    private static final int TYPE_FINANCIAL = 5;
    private static final int TYPE_MEMBER = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mall_virtual,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);

        Bundle bundle = getArguments();
        type = bundle.getInt(IntentParam.CARD_TYPE,1);

//        coin = bundle.getInt(IntentParam.MERCHANT_DETAIL_COIN,coin);

        adapter = new MallVirtualAdapter();
        adapter.setOnClickListener(clickListener);
        binding.vritualList.setAdapter(adapter);

//        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
//        int spacing = metrics.widthPixels/50;
//        boolean includeEdge = false;
//        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(2,spacing,includeEdge);
////        binding.vritualList.addItemDecoration(decoration);

        layoutManager = new GridLayoutManager(getActivity(),2);

        rxManager.onRxEvent(RxEvent.REFRESH_MALL_VIRTUAL_INFO)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        getCoupons();
                    }
                });
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_MALL_VIRTUAL_INFO);
    }

    //获取商城虚拟物品
    public void getCoupons(){
        Subscription subscription = MallImAPI.getMerchandise(
                HttpParam.MERCHANDISE_KEY,HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RequestTransformer(this))
                .subscribe(new Action1<BaseResponse<Coupons.DataBean>>() {
                    @Override
                    public void call(BaseResponse<Coupons.DataBean> coupons) {
                        if (coupons.bool){
                            final List<Coupons.DataBean.ListBean> listBeanList = classifyCouponList(coupons.data.getList());
                            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    boolean istitle = listBeanList.get(position).isTitle;
                                    return istitle?2:1;
                                }
                            });
                            if (binding.vritualList.getItemDecorationCount() <= 0)
                                binding.vritualList.addItemDecoration(new GridDividerItemDecoration(listBeanList));
                            binding.vritualList.setLayoutManager(layoutManager);

                            adapter.refresh(listBeanList);
                            binding.setHasData(coupons.data.getList().size()>0);
                        }else
                            showTips(coupons.data.getReason());

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
            Coupons.DataBean.ListBean bean = (Coupons.DataBean.ListBean) v.getTag();
            Intent intent = new Intent(getActivity(), MerchantDetailActivity.class);
            intent.putExtra(IntentParam.MERCHANT_DETAIL_BEAN,bean);
//            intent.putExtra(IntentParam.MERCHANT_DETAIL_COIN,coin);
            startActivity(intent);
        }
    };

    /**
     * 商品分类
     * @param listBeanList
     */
    public List<Coupons.DataBean.ListBean> classifyCouponList(List<Coupons.DataBean.ListBean> listBeanList){
        List<Coupons.DataBean.ListBean> arrayListList = new ArrayList<>();
        if (listBeanList.size()>0){
            int size = listBeanList.size();
            int classes = 6;//分类数目

            while (true){
                if (classes == 0)//根据分类数循环
                    break;

                int odd = 0;
                boolean isTitleAdd = false;
                for (int index=0; index < size; index++){
                    //TODO 每次根据当前分类数把获取的列表循环取出放入新建集合
                    int type = listBeanList.get(index).gdGoodsBranchType;
                    if (classes == 6){//理财
                        if (type == 0){
                            if (!isTitleAdd){//如有新分类，首次先增加一个分类标题
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    } if (classes == 5){//快捷
                        if (type == 1 || type == 10){
                            if (!isTitleAdd){
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    } if (classes == 4){//境外
                        if (type == 3 || type == 8){
                            if (!isTitleAdd){
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    } if (classes == 3){//扫码
                        if (type == 9){
                            if (!isTitleAdd){
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    } if (classes == 2){//代付
                        if (type == 5){
                            if (!isTitleAdd){
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    } if (classes == 1){//会员
                        if (type == 14 || type == 15){
                            if (!isTitleAdd){
                                addTitle(classes,arrayListList);
                                isTitleAdd = true;
                            }
                            arrayListList.add(listBeanList.get(index));
                            odd++;
                        }
                    }
                }

                if (odd % 2!=0){//奇数就添加没有更多尾部
                    addNoDataFooter(arrayListList);
                }
                classes -=1;
            }
        }
        Logger.e("classifyCouponList=="+arrayListList.size());
        return arrayListList;
    }

    //增加分类标题
    public void addTitle(int classes,List<Coupons.DataBean.ListBean> listBeanList){
        String name = classes==6?"摩富券":classes==5?"快捷券":classes==4?"跨境券":classes==3?"扫码券":classes==2?"代付券":"会员券";
        Coupons.DataBean.ListBean noBean = new Coupons.DataBean.ListBean(true,name,true);
        noBean.setTitle(true);
        listBeanList.add(noBean);
    }

    //增加分类尾部
    public void addNoDataFooter(List<Coupons.DataBean.ListBean> listBeanList){
        Coupons.DataBean.ListBean noBean = new Coupons.DataBean.ListBean(false,"没有更多了",true);
        noBean.setTitle(false);
        listBeanList.add(noBean);
    }
}
