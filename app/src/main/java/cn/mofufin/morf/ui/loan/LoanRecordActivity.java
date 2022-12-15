package cn.mofufin.morf.ui.loan;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.j256.ormlite.stmt.query.In;

import java.io.InputStream;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.LoanApplyRecordAdapter;
import cn.mofufin.morf.databinding.ActivityLoanAboutBinding;
import cn.mofufin.morf.databinding.ActivityLoanRecordBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * TODO 贷款记录
 */
public class LoanRecordActivity extends BaseActivity {
    private ActivityLoanRecordBinding binding;
    private LoanApplyRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_record);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new LoanApplyRecordAdapter();
        adapter.setOnClickListener(onClickListener);
        binding.loanRecordList.setLayoutManager(new LinearLayoutManager(this));
        binding.loanRecordList.setAdapter(adapter);
        getRecord();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void getRecord(){
        Subscription subscription = LoanImAPI.queryLoansApplyRecord(HttpParam.LOANS_QUERY_APPLY_RECORD_KEY,
                HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoanApplyRecord>() {
                    @Override
                    public void call(LoanApplyRecord applyRecord) {
                        if (applyRecord.result_Code == 0){
                            adapter.refresh(applyRecord.applyLoansList);
                            binding.setHasData(adapter.getDataList().size() > 0);
                        }else
                            showTips(applyRecord.result_Msg);
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
            LoanApplyRecord.ApplyLoansListBean bean = (LoanApplyRecord.ApplyLoansListBean) v.getTag();
            Intent intent = new Intent(LoanRecordActivity.this,LoanRecordDetailsActivity.class);
            intent.putExtra(IntentParam.BEAN,bean);
            startActivity(intent);
        }
    };
}
