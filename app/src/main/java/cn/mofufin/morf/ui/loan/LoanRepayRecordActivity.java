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
import cn.mofufin.morf.adapter.LoanRepayRecordAdapter;
import cn.mofufin.morf.databinding.ActivityLoanAboutBinding;
import cn.mofufin.morf.databinding.ActivityLoanRepayRecordBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.LoanRepayCord;
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
public class LoanRepayRecordActivity extends BaseActivity {
    private ActivityLoanRepayRecordBinding binding;
    private LoanRepayRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_repay_record);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }


    public void initView(){
        adapter = new LoanRepayRecordAdapter();
        adapter.setContext(this);
        adapter.setOnClickListener(onClickListener);
        binding.repayRecordList.setLayoutManager(new LinearLayoutManager(this));
        binding.repayRecordList.setAdapter(adapter);
        getRepayCord();
    }

    public void getRepayCord(){
        Subscription subscription = LoanImAPI.queryRefundPlan(HttpParam.LOANS_QUERY_REPAY_RECORD_KEY,
                HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanRepayCord>() {
                    @Override
                    public void call(LoanRepayCord repayCord) {
                        if (repayCord.result_Code == 0){
                            adapter.refresh(repayCord.planList);
                            binding.setHasData(repayCord.planList.size() > 0);
                        }else
                            showTips(repayCord.result_Msg);
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
            LoanRepayCord.PlanListBean bean = (LoanRepayCord.PlanListBean) v.getTag();
            Intent intent = new Intent(LoanRepayRecordActivity.this,LoanRepayRecordDetailActivity.class);
            intent.putExtra(IntentParam.BEAN,bean);
            startActivity(intent);
        }
    };

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
