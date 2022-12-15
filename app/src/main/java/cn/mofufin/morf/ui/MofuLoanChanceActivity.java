package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityMofuLoanChanceBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;

/**
 * MPOS -
 */
public class MofuLoanChanceActivity extends BaseActivity{
    private ActivityMofuLoanChanceBinding binding;

    private final static String LOAN_URL="https://dcms.lianjintai.com/v1/sec/url/84ff1be6dc01e080ed14dd6a897d3b9b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mofu_loan_chance);
        binding.setHandlers(this);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //跳转
    public void loancoco(View view){
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra("HTML",LOAN_URL);
        intent.putExtra(IntentParam.TITLE,binding.name.getText().toString());
        startActivity(intent);
    }

    public void loan(View view){
        DataUtils.TipsDailog(this, getString(R.string.loan_chance_18), null,
                getString(R.string.confirm),
                null);
    }

}
