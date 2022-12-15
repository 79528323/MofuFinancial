package cn.mofufin.morf.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cc.ruis.lib.adapter.TabsFragmentPagerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.CardCreditFragment;
import cn.mofufin.morf.ui.fragment.CardSettleMentFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.databinding.ActivityCardManagerBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardManagerActivity extends BaseActivity {
    private ActivityCardManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_card_manager);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(), binding.nearTabs, binding.nearViewpager);
        tabsAdapter.setCacheFragment(false);
        onSetupTabAdapter(tabsAdapter);
        tabsAdapter.notifyDataSetChanged();
    }

    private void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentParam.CARD_TYPE,1);
        adapter.addTab(getString(R.string.card_manager_1), getString(R.string.card_manager_1), CardSettleMentFragment.class, bundle);
        bundle = new Bundle();
        bundle.putInt(IntentParam.CARD_TYPE,2);
        adapter.addTab(getString(R.string.card_manager_2), getString(R.string.card_manager_2), CardCreditFragment.class, bundle);
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        if (bean.mposFeedPart != 1){
            DataUtils.TipsDailog(this, "你尚未开通MPOS收款", getString(R.string.cancel), "申请开通"
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            subMissionMPos(bean.merchantCode);
                        }
                    });
        }else
            startActivity(new Intent(this,ChangeSettleCardActivity.class));
    }

    public void subMissionMPos(String code){
        Subscription subscription = SubMissionImpAPI.connectionMPos(
                HttpParam.OFFICE_CODE,HttpParam.SUBMISSION_MPOS_APPKEY,
                code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RequestTransformer(this))
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        DataUtils.realNameTipsDailog(CardManagerActivity.this,mofuResult.result_Msg,getString(R.string.ok),"");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
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
