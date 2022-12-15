package cn.mofufin.morf.ui.loan;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.io.InputStream;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.LoanRepayRecordDetailAdapter;
import cn.mofufin.morf.databinding.ActivityLoanAboutBinding;
import cn.mofufin.morf.databinding.ActivityLoanRecordDetailsBinding;
import cn.mofufin.morf.databinding.ActivityLoanRepayRecordBinding;
import cn.mofufin.morf.databinding.ActivityLoanRepayRecordDetailBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.entity.LoanRepayCord;
import cn.mofufin.morf.ui.entity.RecordDetails;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * TODO 还款记录
 */
public class LoanRepayRecordDetailActivity extends BaseActivity {
    private ActivityLoanRepayRecordDetailBinding binding;
    private LoanRepayRecordDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_repay_record_detail);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }


    public void initView(){
        adapter = new LoanRepayRecordDetailAdapter();
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordDetails.PlanDetailsListBean bean = (RecordDetails.PlanDetailsListBean) v.getTag();
                Intent intent = new Intent(LoanRepayRecordDetailActivity.this,LoanRePayMentActivity.class);
                intent.putExtra(IntentParam.BEAN,bean);
                startActivity(intent);
            }
        });

        binding.recordList.setLayoutManager(new LinearLayoutManager(this));
        binding.recordList.setAdapter(adapter);

        getDetails();
    }

    public void getDetails(){
        LoanRepayCord.PlanListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        Subscription subscription = LoanImAPI.queryRefundPlanDetails(HttpParam.LOANS_QUERY_REFUND_DETAIL_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,bean.refundPlanCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RecordDetails>() {
                    @Override
                    public void call(RecordDetails recordDetails) {
                        if (recordDetails.result_Code == 0) {
                            adapter.refresh(recordDetails.planDetailsList);
                            binding.setHasData(recordDetails.planDetailsList.size() > 0);
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
