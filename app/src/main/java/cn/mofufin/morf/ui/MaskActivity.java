package cn.mofufin.morf.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.githang.statusbar.StatusBarCompat;
import com.hope.paysdk.PaySdkEnvionment;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.entity.TabPagerInfo;
import cc.ruis.lib.event.RxBus;
import cn.jpush.android.api.JPushInterface;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityHomeBinding;
import cn.mofufin.morf.databinding.ActivityMaskBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Notifys;
import cn.mofufin.morf.ui.fragment.FoundFragment;
import cn.mofufin.morf.ui.fragment.HomeFragment;
import cn.mofufin.morf.ui.fragment.MaskHomeFragment;
import cn.mofufin.morf.ui.fragment.MaskMineFragment;
import cn.mofufin.morf.ui.fragment.MineFragment;
import cn.mofufin.morf.ui.fragment.ReceiVablesFragment;
import cn.mofufin.morf.ui.home.InvitationActivity;
import cn.mofufin.morf.ui.loan.MofuLoanHomeActivity;
import cn.mofufin.morf.ui.mine.MoreActivity;
import cn.mofufin.morf.ui.repayment.RepayMentManagerActivity;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.PaySDKListener;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MaskActivity extends BaseActivity {
    private ActivityMaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mask);
        setStatusBar(Color.TRANSPARENT);
        initView();
    }

    public void initView() {
        List<TabPagerInfo> tabPagerInfoList = new ArrayList<>();
        loadListData(tabPagerInfoList);
        binding.maskTab.setTabItems(getSupportFragmentManager(), tabPagerInfoList);
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean enableSliding() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * 初始化底部导航栏
     * 各个主页面;
     * 1.聊天消息列表
     * 2.通讯录列表
     * 3.游戏入口
     * 4.发现页面
     * 5.我的页面
     *
     * @param list
     */
    private void loadListData(List<TabPagerInfo> list) {
        TabPagerInfo homePage = new TabPagerInfo(
                getString(R.string.tab_mask_1), R.drawable.tab_mask_home_a, R.drawable.tab_mask_home_b, MaskHomeFragment.class);
        TabPagerInfo found = new TabPagerInfo(
                getString(R.string.tab_mask_2), R.drawable.tab_mask_mine_a, R.drawable.tab_mask_mine_b, MaskMineFragment.class);
        list.add(homePage);
        list.add(found);
    }


    public void getContent(){
        Subscription subscription = UtilsImpAPI.HomePageContent(HttpParam.OFFICE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code == 0){
//                            RxBus.getInstance().post(RxEvent.HOMEPAGE_CONTENT_GET,baseResult.result_Msg);
                            RxBus.getInstance().post(RxEvent.HOME_HORSE_LANTERN,baseResult.result_Msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }















}
