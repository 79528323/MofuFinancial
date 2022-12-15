package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.databinding.ActivityMyInfoBinding;

public class MyInfoActivity extends BaseActivity {
    private ActivityMyInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_info);
        binding.setHandlers(this);

        User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        binding.setMpos(bean.mposFeedPart);
        binding.setRealname(bean.realName);
        binding.setScqr(bean.remitPayFeedPart);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void Rate(View view){
        startActivity(new Intent(this,MyRateActivity.class));
    }

    public void push(View view){
        startActivity(new Intent(this,PushMerchantActivity.class));
    }

    public void member(View view){
        startActivity(new Intent(this,MemberInfoActivity.class));
    }
}
