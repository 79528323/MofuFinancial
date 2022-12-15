package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.databinding.ActivityChangeAccountExplainBinding;

/**
 * 更换账户说明
 */
public class ChangeAccountExplainActivity extends BaseActivity {
    private ActivityChangeAccountExplainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_account_explain);
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

}
