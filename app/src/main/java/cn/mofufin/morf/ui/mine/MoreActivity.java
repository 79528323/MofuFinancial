package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.framework.update.UpdateVersionUtil;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.databinding.ActivityMoreBinding;

public class MoreActivity extends BaseActivity {
    private ActivityMoreBinding binding;
    private boolean isDebug = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_more);
        binding.setHandlers(this);

        isDebug = DataUtils.isApkInDebug(this);
        binding.setVersion("版本 V"+UpdateVersionUtil.getAppCurrVersionName()+(isDebug?"（beta 20）":""));
    }

    public void modifyLogPassword(View view){
        startActivity(new Intent(this,ModifyLogPassWordActivity.class));
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void LogOut(View view){
        DataUtils.Logout();
        overridePendingTransition(0,R.anim.login_enter_anims);
    }

    public void trans(View view){
        startActivity(new Intent(this,ModifyTransPasswordActivity.class));
    }

    public void agreement(View view){
        startActivity(new Intent(this, AgreeMentActivity.class));
    }

    public void aboutmine(View view){
        startActivity(new Intent(this, AboutMineActivity.class));
    }

    public void changeAccount(View view){
        startActivity(new Intent(this,ChangeAccountActivity.class));
    }
}
