package cn.mofufin.morf.ui.repayment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.TabsFragmentPagerAdapter;
import cc.ruis.lib.entity.TabPagerInfo;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityCardManagerBinding;
import cn.mofufin.morf.databinding.ActivityRepaymentManagerBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.fragment.CardCreditFragment;
import cn.mofufin.morf.ui.fragment.CardSettleMentFragment;
import cn.mofufin.morf.ui.loan.fragment.LoanMineFragment;
import cn.mofufin.morf.ui.loan.fragment.LoanMofuFragment;
import cn.mofufin.morf.ui.mine.ChangeSettleCardActivity;
import cn.mofufin.morf.ui.repayment.fragment.RepayMentHomeFragment;
import cn.mofufin.morf.ui.repayment.fragment.RepayMentProjectFragment;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RepayMentManagerActivity extends BaseActivity {
    private ActivityRepaymentManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_repayment_manager);
        binding.setHandlers(this);
        initView();
    }

    public void initView() {
        List<TabPagerInfo> tabPagerInfoList = new ArrayList<>();
        loadListData(tabPagerInfoList);
        binding.repayTab.setTabItems(getSupportFragmentManager(), tabPagerInfoList);

        rxManager.onRxEvent(RxEvent.REPAYMENT_TITLE).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                binding.setTitle((String) o);
            }
        });

        rxManager.onRxEvent(RxEvent.SHOW_FRAGMENT_REPAY).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                binding.repayTab.selectItem(1);
            }
        });

        binding.setTitle(getString(R.string.repayment_title));
    }


    private void loadListData(List<TabPagerInfo> list) {
        TabPagerInfo receivable = new TabPagerInfo(
                getString(R.string.repayment_1), R.drawable.tab_repayment_a, R.drawable.tab_repayment_b, RepayMentHomeFragment.class);
        TabPagerInfo mine = new TabPagerInfo(
                getString(R.string.repayment_2), R.drawable.tab_project_a, R.drawable.tab_project_b, RepayMentProjectFragment.class);
        list.add(receivable);
        list.add(mine);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
