package cn.mofufin.morf.ui.fragment;


import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Iterator;
import java.util.List;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.adapter.BankCardAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.SettlementCardAddActivity;
import cn.mofufin.morf.ui.mine.UnbindCardActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentCardSettleBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 结算卡/信用卡
 */
public class CardSettleMentFragment extends BaseFragment {
    private FragmentCardSettleBinding binding;
    private int type;
    private BankCardAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_card_settle,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        binding.refreshLayout.setEnableLoadMore(false);
//        ClassicsHeader header = (ClassicsHeader) binding.refreshLayout.getRefreshHeader();
//        header.setEnableLastTime(false);
        rxManager.onRxEvent(RxEvent.REFRESH_BANKCARD_INFO)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        binding.refreshLayout.autoRefresh();
                    }
                });

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshBankCard();
            }
        });

        Bundle bundle = getArguments();
        type = bundle.getInt(IntentParam.CARD_TYPE,1);
        adapter = new BankCardAdapter();
        adapter.setClickListener(clickListener);
        adapter.setType(type);

        layoutManager = new LinearLayoutManager(getActivity());
        binding.settleList.setLayoutManager(layoutManager);
        binding.settleList.setAdapter(adapter);
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_BANKCARD_INFO);
    }

    public void refreshBankCard(){
        Subscription subscription = BankImpAPI.bankInfo(HttpParam.OFFICE_CODE
                ,HttpParam.BANK_INFO_APPKEY,MerchanInfoDB.queryInfo().merchantPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        if (dataBeanBaseResponse.bool){
                            List<User.DataBean.CardInfoBean> cardInfoBeanList = dataBeanBaseResponse.data.getCardInfo();
                            Iterator<User.DataBean.CardInfoBean> iterator = cardInfoBeanList.iterator();
                            User.DataBean.CardInfoBean bean = null;
                            while (iterator.hasNext()){
                                bean = iterator.next();
                                if (bean.cardType == 2)
                                    iterator.remove();
                            }
                            adapter.refresh(cardInfoBeanList);
                            binding.setHasData(cardInfoBeanList.size()>0);
                        }else {
                            showTips(dataBeanBaseResponse.data.getReason());
                        }
                        binding.refreshLayout.finishRefresh();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void add(View view){
        startActivity(new Intent(getActivity(), SettlementCardAddActivity.class));
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            User.DataBean.CardInfoBean bean = adapter.getDataList().get(position);
            if (bean.cardDef <=1 )
                return;

            Intent intent = new Intent(getActivity(), UnbindCardActivity.class);
            intent.putExtra(IntentParam.UNBIND_CARD_INFO,bean);
            getActivity().startActivity(intent);
        }
    };
}
