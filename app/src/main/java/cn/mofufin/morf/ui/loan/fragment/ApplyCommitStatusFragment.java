package cn.mofufin.morf.ui.loan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.databinding.FragmentApplyCommitStatusBinding;
import cn.mofufin.morf.databinding.FragmentPersonalStepOneBinding;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class ApplyCommitStatusFragment extends BaseFragment {
    private FragmentApplyCommitStatusBinding binding;
//    private ProductInfoAdapter adapter;
    private int[] res= {R.drawable.loan_banner,R.drawable.loan_banner};
    private HomesPagerAdapter adapter =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apply_commit_status,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(){
//        binding.inverstmentList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new ProductInfoAdapter();
//        adapter.setClickListener(onClickListener);
//        binding.inverstmentList.setAdapter(adapter);
        getProductInfo();
    }

    public void back(View view){
        getActivity().finish();
    }

    public void getProductInfo(){
        Subscription subscription= ProductImpAPI.queryAllFundProduct(HttpParam.OFFICE_CODE,HttpParam.QUERY_ALL_MERPRODUCT_INFO_KEY,
                MerchanInfoDB.queryInfo().merchantCode)
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
                .subscribe(new Action1<ProductInfo>() {
                    @Override
                    public void call(ProductInfo productInfo) {
                        if (productInfo.result_Code == 0){
//                            binding.setHasData(productInfo.productList.size() >0);
//                            adapter.refresh(productInfo.productList);
                        }else
                            showTips(productInfo.result_Msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            ProductInfo.ProductListBean bean = (ProductInfo.ProductListBean) v.getTag();
            intent.putExtra(IntentParam.BEAN,bean);
            startActivity(intent);
        }
    };

}
