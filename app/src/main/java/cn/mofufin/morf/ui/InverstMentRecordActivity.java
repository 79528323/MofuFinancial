package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import cc.ruis.lib.adapter.TabsFragmentPagerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.RecordInverstMentedFragment;
import cn.mofufin.morf.ui.fragment.RecordInverstMentingFragment;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityInverstmentRecordBinding;

public class InverstMentRecordActivity extends BaseActivity {
    private ActivityInverstmentRecordBinding binding;
    public static final int DIFFER_MONTH=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_inverstment_record);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(), binding.recordTabs, binding.recordViewpager);
        tabsAdapter.setCacheFragment(false);
        onSetupTabAdapter(tabsAdapter);
        tabsAdapter.notifyDataSetChanged();
    }

    private void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentParam.TYPE,1);
        adapter.addTab(getString(R.string.inverst_record_title1), getString(R.string.inverst_record_title1), RecordInverstMentingFragment.class, bundle);
        bundle = new Bundle();
        bundle.putInt(IntentParam.TYPE,2);
        adapter.addTab(getString(R.string.inverst_record_title2), getString(R.string.inverst_record_title2), RecordInverstMentedFragment.class, bundle);
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
