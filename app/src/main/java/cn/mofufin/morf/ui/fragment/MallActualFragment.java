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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.mofufin.morf.adapter.MallVirtualAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.UnbindCardActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.GridSpacingItemDecoration;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentMallActualBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 实物
 */
public class MallActualFragment extends BaseFragment {
    private FragmentMallActualBinding binding;
    private int type;
    private MallVirtualAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mall_actual,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setEnableRefresh(false);
//        ClassicsHeader header = (ClassicsHeader) binding.refreshLayout.getRefreshHeader();
//        header.setEnableLastTime(false);
        rxManager.onRxEvent(RxEvent.REFRESH_MALL_ACTUAL_INFO)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        binding.refreshLayout.autoRefresh();
                    }
                });

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                getCoupons();
                binding.refreshLayout.finishRefresh();
            }
        });

        Bundle bundle = getArguments();
        type = bundle.getInt(IntentParam.CARD_TYPE,1);

        adapter = new MallVirtualAdapter();
        adapter.setOnClickListener(clickListener);
        int spanCount = 2;
        int spacing = 50;
        boolean includeEdge = false;
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(spanCount,spacing,includeEdge);
        binding.actualList.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
        binding.actualList.addItemDecoration(decoration);
        binding.actualList.setAdapter(adapter);
//        RxBus.getInstance().postEmpty(RxEvent.REFRESH_MALL_ACTUAL_INFO);
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
                            adapter.refresh(coupons.data.getList());
                            binding.setHasData(coupons.data.getList().size()>0);
                        }else
                            showTips(coupons.data.getReason());
                        binding.refreshLayout.finishRefresh();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        binding.refreshLayout.finishRefresh();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

//    public void refreshBankCard(){
//        Subscription subscription = BankImpAPI.bankInfo(HttpParam.OFFICE_CODE
//                ,HttpParam.BANK_INFO_APPKEY,MerchanInfoDB.queryInfo().merchantPhone)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
//                    @Override
//                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
//                        if (dataBeanBaseResponse.bool){
//                            List<User.DataBean.CardInfoBean> cardInfoBeanList = dataBeanBaseResponse.data.getCardInfo();
//                            Iterator<User.DataBean.CardInfoBean> iterator = cardInfoBeanList.iterator();
//                            User.DataBean.CardInfoBean bean = null;
//                            while (iterator.hasNext()){
//                                bean = iterator.next();
//                                if (bean.cardType == 2)
//                                    iterator.remove();
//                            }
//                            adapter.refresh(cardInfoBeanList);
//                            binding.setHasData(cardInfoBeanList.size()>0);
//                        }else {
//                            showTips(dataBeanBaseResponse.data.getReason());
//                        }
//                        binding.refreshLayout.finishRefresh();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
//    }
//
//    public void add(View view){
//        startActivity(new Intent(getActivity(), SettlementCardAddActivity.class));
//    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            int position = (int) v.getTag();
            User.DataBean.CardInfoBean bean = (User.DataBean.CardInfoBean) v.getTag();
            Intent intent = new Intent(getActivity(), UnbindCardActivity.class);
            intent.putExtra(IntentParam.UNBIND_CARD_INFO,bean);
            getActivity().startActivity(intent);
        }
    };
}
