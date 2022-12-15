package cn.mofufin.morf.ui.fragment;


import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.mine.BalanceActivity;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.mine.IntegralRankingDayActivity;
import cn.mofufin.morf.ui.mine.IntegralRankingMonthActivity;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentFinancialHomeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 首页
 */
public class FinancialHomeFragment extends BaseFragment {
    private FragmentFinancialHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_financial_home, container, false);
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

    public void initView(){
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getProductInfo();
            }
        });

        rxManager.onRxEvent(RxEvent.REFRESH_PRODUCT_INFO).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                binding.refreshLayout.autoRefresh();
//                getProductInfo();
            }
        });
        binding.refreshLayout.autoRefresh();
        getNotice();

//        binding.rightText.setText("积分榜");
//        binding.rightText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), IntegralRankingMonthActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    public void toastNotice(View view){
        TextView text = (TextView) view;
        String content = text.getText().toString();
        if (!TextUtils.isEmpty(content))
            DataUtils.TipsDailog(getActivity(),content,null,getString(R.string.confirm),null);
    }


    public void getProductInfo(){
        Subscription subscription= ProductImpAPI.initMerProductInfo(HttpParam.OFFICE_CODE,HttpParam.INIT_MERPRODUCT_INFO_KEY,
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
                            binding.setAboutEarnings(DataUtils.numFormat(productInfo.aboutEarnings));
                            binding.setAlreadyEarnings(DataUtils.numFormat(productInfo.alreadyEarnings));
                            binding.setBalance(DataUtils.numFormat(productInfo.balance));
                            binding.setInCastAsset(DataUtils.numFormat(productInfo.inCastAsset));
                            binding.setInfo(productInfo.Fundproduct);
                        }else
                            showTips(productInfo.result_Msg);

                        binding.refreshLayout.finishRefresh();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                        binding.refreshLayout.finishRefresh();
                    }
                });
        rxManager.add(subscription);
    }

    public void inverstment(View view){
        ProductInfo.ProductListBean listBean = new ProductInfo.ProductListBean();
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        listBean.setFdAnnualized(binding.getInfo().fdAnnualized);
        listBean.setFdMinMoney(binding.getInfo().fdMinMoney);
        listBean.setFdName(binding.getInfo().fdName);
        listBean.setFdProductDate(binding.getInfo().fdProductDate);
        listBean.setFdNumber(binding.getInfo().fdNumber);
        intent.putExtra(IntentParam.BEAN,listBean);
        startActivity(intent);
    }

    //获取通知消息
    public void getNotice(){
        Subscription subscription = ProductImpAPI.queryFundInform(HttpParam.OFFICE_CODE,HttpParam.QUERY_INVERST_NOTICE_KEY,
                MerchanInfoDB.queryInfo().merchantCode,"0")
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
                .subscribe(new Action1<ProductNotice>() {
                    @Override
                    public void call(ProductNotice productNotice) {
                        if (productNotice.result_Code == 0) {
                            binding.marquee.setText(productNotice.informList.get(0).getInformMessage());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,false);
                    }
                });
        rxManager.add(subscription);
    }

    public void balance(View view){
        startActivity(new Intent(getActivity(), BalanceActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void back(View view){
        getActivity().finish();
    }

    public void day(View view){
        Intent intent = new Intent(getActivity(), IntegralRankingDayActivity.class);
        startActivity(intent);
    }

    public void month(View view){
        Intent intent = new Intent(getActivity(), IntegralRankingMonthActivity.class);
        startActivity(intent);
    }
}
