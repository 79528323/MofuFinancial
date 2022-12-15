package cn.mofufin.morf.ui.fragment;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.mofufin.morf.adapter.BackpackUsedExpiredAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.FragmentBackpackUsedExpiredBinding;

/**
 * 虚拟物品
 */
public class BackPackUsedExpiredFragment extends BaseFragment {
    private FragmentBackpackUsedExpiredBinding binding;
    private List<MerchantBag.DataBean.ListBean> listBeanList;
    private int type=0;
    private BackpackUsedExpiredAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_backpack_used_expired,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);


        Bundle bundle = getArguments();
        type = bundle.getInt(IntentParam.CARD_TYPE,1);
        listBeanList = bundle.getParcelableArrayList(IntentParam.MERCHANT_BAG_ARRYLIST);
        binding.setHasData(listBeanList.size()>0);

        adapter = new BackpackUsedExpiredAdapter();
        binding.usedList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.usedList.setAdapter(adapter);
        adapter.refresh(listBeanList);
    }

    //获取商城虚拟物品
//    public void getCoupons(){
//        Subscription subscription = MallImAPI.getMerchandise(
//                HttpParam.MERCHANDISE_KEY,HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new RequestTransformer(this))
//                .subscribe(new Action1<BaseResponse<Coupons.DataBean>>() {
//                    @Override
//                    public void call(BaseResponse<Coupons.DataBean> coupons) {
//                        if (coupons.bool){
////                            adapter.refresh(coupons.data.getList());
//                            binding.setHasData(coupons.data.getList().size()>0);
//                        }else
//                            showTips(coupons.data.getReason());
//
//
//                    }
//
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
//    }


//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Coupons.DataBean.ListBean bean = (Coupons.DataBean.ListBean) v.getTag();
//            Intent intent = new Intent(getActivity(), MerchantDetailActivity.class);
//            intent.putExtra(IntentParam.MERCHANT_DETAIL_BEAN,bean);
//            intent.putExtra(IntentParam.MERCHANT_DETAIL_COIN,coin);
//            startActivity(intent);
//        }
//    };
}
