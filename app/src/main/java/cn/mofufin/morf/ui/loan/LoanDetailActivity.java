package cn.mofufin.morf.ui.loan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.ProductRecordAdapter;
import cn.mofufin.morf.databinding.ActivityLoanDetailBinding;
import cn.mofufin.morf.databinding.ActivityProductDetailBinding;
import cn.mofufin.morf.ui.ProductPayMentDetailsActivity;
import cn.mofufin.morf.ui.ProductRiskManagementActivity;
import cn.mofufin.morf.ui.ProductdescriptionActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.ProductDetails;
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
 * TODO 贷款产品详情
 */
public class LoanDetailActivity extends BaseActivity {
    private ActivityLoanDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_detail);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        LoanProduct.ProductListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setDetails(bean);
        binding.setTitle(bean.productName);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //立即申请
    public void apply(View view){
        Intent intent = new Intent(this,LoanPayMentDetailsActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getDetails());
        startActivity(intent);
    }

}
