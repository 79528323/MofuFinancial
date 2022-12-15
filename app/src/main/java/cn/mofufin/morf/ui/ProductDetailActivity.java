package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.adapter.ProductRecordAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityProductDetailBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProductDetailActivity extends BaseActivity {
    private ActivityProductDetailBinding binding;
    private ProductInfo.ProductListBean bean;
    private ProductRecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_detail);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        Logger.e("bean === "+bean.fdNumber);

        adapter = new ProductRecordAdapter();
        binding.productRecordList.setLayoutManager(new LinearLayoutManager(this));
        binding.productRecordList.setAdapter(adapter);
        details();

        binding.contentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public float touchY = 0;
    public boolean isTouchScreenHalf(){
        boolean isHalf = false;
        int haflY = getResources().getDisplayMetrics().heightPixels/2;
        if (touchY < haflY){
            isHalf = true;
        }
        return isHalf;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            touchY = ev.getRawY();
            if (!isTouchScreenHalf()){
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void details(){
        Subscription subscription = ProductImpAPI.querySingleProductDetails(
                HttpParam.OFFICE_CODE,HttpParam.QUERY_SINGLE_MERPRODUCT_INFO_KEY, MerchanInfoDB.queryInfo().merchantCode,
                String.valueOf(bean.fdNumber))
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
                .subscribe(new Action1<ProductDetails>() {
                    @Override
                    public void call(ProductDetails productDetails) {
                            if (productDetails.getResult_Code()==0){
                            ProductDetails.ProductDetailsBean detailsBean = productDetails.getProductDetails();
                            int max = ((int) (detailsBean.fdTotalCirculation)/10000);
                            binding.progress.setMax(max);

                            int progress = ((int) (detailsBean.fdResidueCirculation)/10000);
                            binding.progress.setProgress(progress);
                            binding.setDetails(productDetails.getProductDetails());
                            adapter.refresh(productDetails.recordList);
                        }else {
                            showTips(productDetails.getResult_Msg());
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

    //产品描述
    public void description(View view){
        Intent intent = new Intent(this,ProductdescriptionActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getDetails());
        startActivity(intent);
    }

    //风险控制
    public void risk(View view){
        Intent intent = new Intent(this,ProductRiskManagementActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getDetails());
        startActivity(intent);
    }

    //付款详情
    public void payment(View view){
        Intent intent = new Intent(this,ProductPayMentDetailsActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getDetails());
        startActivity(intent);
    }

}
