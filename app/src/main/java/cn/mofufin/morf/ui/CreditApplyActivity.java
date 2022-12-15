package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import cn.mofufin.morf.adapter.CreditApplyAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.CreditApply;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.GridSpacingItemDecoration;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityCreditApplyBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *申请信用卡
 */
public class CreditApplyActivity extends BaseActivity{
    private ActivityCreditApplyBinding binding;
    private CreditApplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_credit_apply);
        binding.setHandlers(this);
        initView();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void initView(){
        int spanCount = 2;
        int spacing = 1;
        boolean includeEdge = false;
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(spanCount,spacing,includeEdge);
        binding.creditApplyList.setLayoutManager(new GridLayoutManager(this,spanCount));
        binding.creditApplyList.addItemDecoration(decoration);
        adapter = new CreditApplyAdapter();
        binding.creditApplyList.setAdapter(adapter);
        adapter.setOnClickListener(onClickListener);
        getCredit();
    }


    public void getCredit(){
        Subscription subscription = UtilsImpAPI.getCreditForApp(HttpParam.CREDIT_APPLY_KEY,HttpParam.OFFICE_CODE,
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
                .subscribe(new Action1<CreditApply>() {
                    @Override
                    public void call(CreditApply apply) {
                        if (apply.bool){
                            adapter.refresh(apply.getData().list);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable, true);
                    }
                });
        rxManager.add(subscription);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreditApply.DataBean.ListBean bean = (CreditApply.DataBean.ListBean) v.getTag();
            Intent intent = new Intent(CreditApplyActivity.this,WebActivity.class);
            intent.putExtra("HTML",bean.cardUrl);
            intent.putExtra(IntentParam.TITLE,getString(R.string.credit_apply_title));
            startActivity(intent);
        }
    };
}
