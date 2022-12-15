package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.ArrayList;

import cc.ruis.lib.adapter.TabsFragmentPagerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.BackPackUsedExpiredFragment;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityBackpackHistoryManagerBinding;

public class BackPackHistoryManagerActivity extends BaseActivity {
    private ActivityBackpackHistoryManagerBinding binding;
    private ArrayList<MerchantBag.DataBean.ListBean> usedlist = new ArrayList<>();
    private ArrayList<MerchantBag.DataBean.ListBean> expiredlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_backpack_history_manager);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        ArrayList<MerchantBag.DataBean.ListBean> arrayList = getIntent().getParcelableArrayListExtra(IntentParam.MERCHANT_BAG_ARRYLIST);
        if (!arrayList.isEmpty()){
            for (int index=0; index < arrayList.size(); index++){
                MerchantBag.DataBean.ListBean bean = arrayList.get(index);
                if (bean.mcbGoodsState == 1){//TODO 已使用
                    usedlist.add(bean);
                }else if (bean.mcbGoodsState == 2){//TODO 已过期
                    expiredlist.add(bean);
                }
            }
        }

        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(), binding.backHistoryTabs, binding.backHistoryViewpager);
        tabsAdapter.setCacheFragment(false);
//        onSetupTabAdapter(tabsAdapter);


        Bundle bundle = new Bundle();
        bundle.putInt(IntentParam.TYPE,1);
        bundle.putParcelableArrayList(IntentParam.MERCHANT_BAG_ARRYLIST,usedlist);
        tabsAdapter.addTab(getString(R.string.back_his_used), getString(R.string.back_his_used), BackPackUsedExpiredFragment.class, bundle);

        bundle = new Bundle();
        bundle.putInt(IntentParam.TYPE,2);
        bundle.putParcelableArrayList(IntentParam.MERCHANT_BAG_ARRYLIST,expiredlist);
        tabsAdapter.addTab(getString(R.string.back_his_expirl), getString(R.string.back_his_expirl), BackPackUsedExpiredFragment.class, bundle);


        tabsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
