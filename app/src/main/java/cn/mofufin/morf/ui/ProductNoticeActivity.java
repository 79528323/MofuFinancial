package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.mofufin.morf.adapter.ProductNoticeAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityProductNoticeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProductNoticeActivity extends BaseActivity {
    private ActivityProductNoticeBinding binding;
    private ProductNoticeAdapter adapter;
    private int queryType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_notice);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        queryType = 1;
        adapter = new ProductNoticeAdapter();
        binding.noticeList.setLayoutManager(new LinearLayoutManager(this));
        binding.noticeList.setAdapter(adapter);

        getNotice();

    }

    //获取通知消息
    public void getNotice(){
        Subscription subscription = ProductImpAPI.queryFundInform(HttpParam.OFFICE_CODE,HttpParam.QUERY_INVERST_NOTICE_KEY,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(queryType))
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
                            adapter.refresh(productNotice.getInformList());
                            binding.setHasData(productNotice.informList.size()>0);
                        }else{
                            showTips(productNotice.result_Msg);
                            binding.setHasData(false);
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
