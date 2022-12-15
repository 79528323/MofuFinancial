package cn.mofufin.morf.ui.loan;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.entity.TabPagerInfo;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityFinancialBinding;
import cn.mofufin.morf.databinding.ActivityMofuloanHomeBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.FinancialHomeFragment;
import cn.mofufin.morf.ui.fragment.InverstMentFragment;
import cn.mofufin.morf.ui.fragment.MyInverstFragment;
import cn.mofufin.morf.ui.loan.fragment.LoanMineFragment;
import cn.mofufin.morf.ui.loan.fragment.LoanMofuFragment;

/**
 * 摩富理财
 */
public class MofuLoanHomeActivity extends BaseActivity {
    private ActivityMofuloanHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mofuloan_home);
        setStatusBar(Color.TRANSPARENT);
        initView();
    }

    public void initView() {
        List<TabPagerInfo> tabPagerInfoList = new ArrayList<>();
        loadListData(tabPagerInfoList);
        binding.mofuLoanTab.setTabItems(getSupportFragmentManager(), tabPagerInfoList);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    private void loadListData(List<TabPagerInfo> list) {
        TabPagerInfo receivable = new TabPagerInfo(
                getString(R.string.mofuloan_home_2), R.drawable.loan_tab_b, R.drawable.loan_tab_a, LoanMofuFragment.class);
        TabPagerInfo mine = new TabPagerInfo(
                getString(R.string.financial_tab_3), R.drawable.loan_mine_tab_b, R.drawable.loan_mine_tab_a, LoanMineFragment.class);
        list.add(receivable);
        list.add(mine);
    }



}
