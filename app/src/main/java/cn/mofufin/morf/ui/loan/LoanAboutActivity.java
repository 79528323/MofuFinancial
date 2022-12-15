package cn.mofufin.morf.ui.loan;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;

import java.io.InputStream;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAboutMineBinding;
import cn.mofufin.morf.databinding.ActivityLoanAboutBinding;
import cn.mofufin.morf.ui.base.BaseActivity;

public class LoanAboutActivity extends BaseActivity {
    private ActivityLoanAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_about);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
