package cn.mofufin.morf.ui.loan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanPaymentDetailsBinding;
import cn.mofufin.morf.databinding.ActivityLoanRepayMentBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.RecordDetails;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.LoanRepayDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * TODO 还款
 */
public class LoanRePayMentActivity extends BaseActivity {
    private ActivityLoanRepayMentBinding binding;
    private LoanRepayDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_repay_ment);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        RecordDetails.PlanDetailsListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setBean(bean);

        dialog = new LoanRepayDialog(this);
    }

    public void repay(View view){
        if (dialog.isShowing()){
            dialog.dismiss();
        }else
            dialog.showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

}
