package cn.mofufin.morf.ui.loan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.adapter.LoanPagerAdapter;
import cn.mofufin.morf.adapter.LoanProductAdapter;
import cn.mofufin.morf.adapter.ProductInfoAdapter;
import cn.mofufin.morf.databinding.FragmentInverstmentBinding;
import cn.mofufin.morf.databinding.FragmentLoanMofuBinding;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Banner;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.LoanBanner;
import cn.mofufin.morf.ui.entity.LoanNotify;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.LoansControlInfo;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.loan.LoanDetailActivity;
import cn.mofufin.morf.ui.loan.LoanIdentityAuthActivity;
import cn.mofufin.morf.ui.loan.LoanRecordActivity;
import cn.mofufin.morf.ui.loan.LoanRepayRecordActivity;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func4;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class LoanMofuFragment extends BaseFragment {
    private FragmentLoanMofuBinding binding;
    private LoanProductAdapter adapter;
    private String queryType="0";
    private LoanPagerAdapter loanPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loan_mofu,container,false);
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
        binding.setIsValidate(false);
        adapter = new LoanProductAdapter();
        adapter.setClickListener(onClickListener);
        binding.loanProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.loanProductList.setAdapter(adapter);

        Zips();
    }

    public void applyRecord(View view){
        Intent intent = new Intent(getActivity(), LoanRecordActivity.class);
        startActivity(intent);
    }

    public void repayCord(View view){
        Intent intent = new Intent(getActivity(), LoanRepayRecordActivity.class);
        startActivity(intent);
    }

    public void back(View view){
        getActivity().finish();
    }

    public void authValidate(View view){
        Intent intent = new Intent(getActivity(), LoanIdentityAuthActivity.class);
        startActivity(intent);
    }

    public void toastNotice(View view){
        TextView text = (TextView) view;
        String content = text.getText().toString();
        if (!TextUtils.isEmpty(content))
            DataUtils.TipsDailog(getActivity(),content,null,getString(R.string.confirm),null);
    }


    //获取产品
    public void getProduct(){
        Subscription subscription = LoanImAPI.queryLoansProduct(HttpParam.LOANS_PRODUCT_KEY,HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanProduct>() {
                    @Override
                    public void call(LoanProduct loanProduct) {
                        if (loanProduct.result_Code == 0) {
                            adapter.refresh(loanProduct.productList);
                            binding.setHasData(loanProduct.productList.size() > 0);
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
            LoanProduct.ProductListBean bean = (LoanProduct.ProductListBean) v.getTag();
            intent.putExtra(IntentParam.BEAN,bean);
            startActivity(intent);
        }
    };

    //获取身份认证状态
    public void getSelfStatus(){
        Subscription subscription = LoanImAPI.querySelfSubmitState(HttpParam.LOANS_SELF_STATUS_KEY,HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION)
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
                .subscribe(new Action1<LoansControlInfo>() {
                    @Override
                    public void call(LoansControlInfo controlInfo) {
                        if (controlInfo.result_Code == 0) {
                            if (controlInfo.controlInfo.selfTextState == 0 && controlInfo.controlInfo.selfImgState == 0
                                    && controlInfo.controlInfo.friendTextState == 0 && controlInfo.controlInfo.businessImgState == 0) {
                                binding.setIsValidate(true);
                            } else
                                binding.setIsValidate(false);
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


    //获取通知消息
    public void getNotice(){
        Subscription subscription = LoanImAPI.queryLoansInform(HttpParam.LOANS_NOTIFY_KEY,queryType,
                Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanNotify>() {
                    @Override
                    public void call(LoanNotify loanNotify) {
                        if (loanNotify.result_Code == 0) {
                            binding.marquee.setText(loanNotify.informList.get(0).informContent);
                        }else
                            showTips(loanNotify.result_Msg);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void getBanner(){
        Subscription subscription = LoanImAPI.queryLoansImg(HttpParam.LOANS_BANNER_KEY,Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanBanner>() {
                    @Override
                    public void call(LoanBanner banner) {
                        if (banner.result_Code==0){
                            List<Banner> viewList = new ArrayList<>();
                            List<LoanBanner.ImgListBean> imgListBeans = banner.imgList;
                            if (imgListBeans.size() >0){
                                for (int i=0; i<imgListBeans.size(); i++){
                                    viewList.add(new Banner(imgListBeans.get(i).imgAddress,imgListBeans.get(i).hrefAddress));
                                }

                                loanPagerAdapter = new LoanPagerAdapter(getActivity(),viewList);
                                loanPagerAdapter.setClickListener(bannerClicklistener);
                                binding.mofuLoanViewpager.setAdapter(loanPagerAdapter);
                            }
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


    public void Zips(){
        Subscription subscription = Observable.zip(
                //轮播图
                LoanImAPI.queryLoansImg(HttpParam.LOANS_BANNER_KEY, Common.LOAN_VERSION),
                //通知
                LoanImAPI.queryLoansInform(HttpParam.LOANS_NOTIFY_KEY, queryType, Common.LOAN_VERSION),
                //身份状态
                LoanImAPI.querySelfSubmitState(HttpParam.LOANS_SELF_STATUS_KEY, HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION),
                //借贷列表
                LoanImAPI.queryLoansProduct(HttpParam.LOANS_PRODUCT_KEY, HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION),
                new Func4<LoanBanner, LoanNotify, LoansControlInfo, LoanProduct, List<Object>>() {
                    @Override
                    public List<Object> call(LoanBanner banner, LoanNotify loanNotify, LoansControlInfo controlInfo, LoanProduct loanProduct) {
                        List<Object> objectList = new ArrayList<>();
                        objectList.add(banner);
                        objectList.add(loanNotify);
                        objectList.add(controlInfo);
                        objectList.add(loanProduct);
                        return objectList;
                    }
                }).subscribeOn(Schedulers.io())
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
                .subscribe(new Action1<List<Object>>() {
                    @Override
                    public void call(List<Object> objectList) {
                        LoanBanner banner = (LoanBanner) objectList.get(0);
                        if (banner.result_Code == 0) {
                            List<Banner> viewList = new ArrayList<>();
                            List<LoanBanner.ImgListBean> imgListBeans = banner.imgList;
                            if (imgListBeans.size() > 0) {
                                for (int i = 0; i < imgListBeans.size(); i++) {
                                    viewList.add(new Banner(imgListBeans.get(i).imgAddress, imgListBeans.get(i).hrefAddress));
                                }

                                loanPagerAdapter = new LoanPagerAdapter(getActivity(), viewList);
                                loanPagerAdapter.setClickListener(bannerClicklistener);
                                binding.mofuLoanViewpager.setAdapter(loanPagerAdapter);
                            }
                        }


                        LoanNotify loanNotify = (LoanNotify) objectList.get(1);
                        if (loanNotify.result_Code == 0) {
                            binding.marquee.setText(loanNotify.informList.get(0).informContent);
                        }

                        LoansControlInfo controlInfo = (LoansControlInfo) objectList.get(2);
                        if (controlInfo.result_Code == 0) {
                            if (controlInfo.controlInfo.selfTextState == 0 && controlInfo.controlInfo.selfImgState == 0
                                    && controlInfo.controlInfo.friendTextState == 0 && controlInfo.controlInfo.businessImgState == 0) {
                                binding.setIsValidate(true);
                            } else
                                binding.setIsValidate(false);
                        }


                        LoanProduct loanProduct = (LoanProduct) objectList.get(3);
                        if (loanProduct.result_Code == 0) {
                            adapter.refresh(loanProduct.productList);
                            binding.setHasData(loanProduct.productList.size() > 0);
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


    View.OnClickListener bannerClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Banner banner = loanPagerAdapter.getItemData(position);
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("HTML",banner.href);
            intent.putExtra(IntentParam.TITLE,"");
            startActivity(intent);
        }
    };
}
