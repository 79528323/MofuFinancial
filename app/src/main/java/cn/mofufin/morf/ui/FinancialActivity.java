package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.entity.TabPagerInfo;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.FinancialHomeFragment;
import cn.mofufin.morf.ui.fragment.InverstMentFragment;
import cn.mofufin.morf.ui.fragment.MyInverstFragment;
import cn.mofufin.morf.databinding.ActivityFinancialBinding;

/**
 * 摩富理财
 */
public class FinancialActivity extends BaseActivity {
    private ActivityFinancialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_financial);
        setStatusBar(Color.TRANSPARENT);
        initView();
    }

    public void initView() {
        List<TabPagerInfo> tabPagerInfoList = new ArrayList<>();
        loadListData(tabPagerInfoList);
        binding.financialTab.setTabItems(getSupportFragmentManager(), tabPagerInfoList);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    private void loadListData(List<TabPagerInfo> list) {
        TabPagerInfo homePage = new TabPagerInfo(
                getString(R.string.financial_tab_1), R.drawable.financial_tab_1, R.drawable.financial_tab_1_sele, FinancialHomeFragment.class);
//        TabPagerInfo found = new TabPagerInfo(
//                getString(R.string.financial_tab_2), R.drawable.icon_found, R.drawable.icon_found_select, FoundFragment.class);
        TabPagerInfo receivable = new TabPagerInfo(
                getString(R.string.financial_tab_2), R.drawable.financial_tab_2, R.drawable.financial_tab_2_sele, InverstMentFragment.class);
        TabPagerInfo mine = new TabPagerInfo(
                getString(R.string.financial_tab_3), R.drawable.financial_tab_3, R.drawable.financial_tab_3_sele, MyInverstFragment.class);
        list.add(homePage);
//        list.add(found);
        list.add(receivable);
        list.add(mine);
    }



}
