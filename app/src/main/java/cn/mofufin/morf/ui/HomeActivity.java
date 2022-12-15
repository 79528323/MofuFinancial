package cn.mofufin.morf.ui;

import android.Manifest;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;

import android.os.Process;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.hope.paysdk.PaySdkEnvionment;
import com.hope.paysdk.PaySdkListener;
import com.hope.paysdk.framework.BusiInfo;
import com.hope.paysdk.framework.mposdriver.MposDriverService;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.entity.TabPagerInfo;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.jpush.android.api.JPushInterface;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Notifys;
import cn.mofufin.morf.ui.fragment.FoundFragment;
import cn.mofufin.morf.ui.fragment.HomeFragment;
import cn.mofufin.morf.ui.fragment.MineFragment;
import cn.mofufin.morf.ui.fragment.ReceiVablesFragment;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.home.InvitationActivity;
import cn.mofufin.morf.ui.loan.MofuLoanHomeActivity;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.mine.BalanceActivity;
import cn.mofufin.morf.ui.mine.CardManagerActivity;
import cn.mofufin.morf.ui.mine.CommissionActivity;
import cn.mofufin.morf.ui.mine.MofuCoinActivity;
import cn.mofufin.morf.ui.mine.MoreActivity;
import cn.mofufin.morf.ui.mine.MyInfoActivity;
import cn.mofufin.morf.ui.repayment.RepayMentManagerActivity;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.PaySDKListener;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityHomeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;
    private RxPermissions rxPermissions;
    private Notifys notifys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_blue));
        rxPermissions = new RxPermissions(this);
        initView();
    }


    public void initView() {
        JPushInterface.setAlias(BaseApplication.context,reformSequence(), MerchanInfoDB.queryInfo().merchantCode);
        JPushInterface.resumePush(BaseApplication.context);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE
                ,Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean permission) {
                        if (permission){//同意权限
                            PaySDKListener.InitializationSDK();
                        }else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            DataUtils.setPermissionDailog(HomeActivity.this,getString(R.string.permissions_location));
                        }
                    }
                });

        List<TabPagerInfo> tabPagerInfoList = new ArrayList<>();
        loadListData(tabPagerInfoList);
        binding.homeTab.setTabItems(getSupportFragmentManager(), tabPagerInfoList);

//        rxManager.onRxEvent(RxEvent.HOMEPAGE_CONTENT_GET).subscribe(new Action1<Object>() {
//            @Override
//            public void call(Object o) {
//                final String content = (String) o;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        DataUtils.TipsDailog(HomeActivity.this,
//                                "通知", content, getString(R.string.ok), null, null);
//                    }
//                },500);
//            }
//        });
        getContent();

    }


    @Override
    public void finish() {
//        CityBeanDao dao = new CityBeanDao(this);
//        dao.deleteBean();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PaySdkEnvionment.destorySdk();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (null == getIntent())
            return;

        notifys = getIntent().getParcelableExtra(IntentParam.NOTIFYS);
        if (notifys!=null){
            Intent intent = null;
            Class<?> mClass = null;
            if (notifys.way.equals("0")){//跳转内部
                int params = Integer.valueOf(notifys.params);
                if (params == 1){
                    mClass = MofuMallActivity.class;
                }else if (params == 7){
                    mClass = MoreActivity.class;
                }else if (params == 8){
                    mClass = MofuLoanHomeActivity.class;
                }else if (params == 10){
                    mClass = InvitationActivity.class;
                }else if (params == 13){
                    mClass = CreditApplyActivity.class;
                }else{
                    if (params == 14 || params == 15 || params == 16 ||params == 17||params == 9){
                        //首页4大入口
                        binding.homeTab.selectItem(0);
                        RxBus.getInstance().post(RxEvent.GOTO_NOTIFY_ENTRANCE,params);
                    }else if (params == 6 || params == 11 || params == 3 ||params == 4||params == 5||params == 2){
                        //我的
                        binding.homeTab.selectItem(2);
                        RxBus.getInstance().post(RxEvent.GOTO_MINE_NOTIFY_ENTRANCE,params);
                    }

                    mClass = HomeActivity.class;
                    if (params == 9)
                        mClass = RepayMentManagerActivity.class;

                }

                intent = new Intent(this,mClass);
                if (params == 12){
                    binding.homeTab.selectItem(1);
                }else {
                    startActivity(intent);
                }


            }else {
                intent = new Intent(this,WebActivity.class);
                intent.putExtra("HTML",notifys.params);
                intent.putExtra(IntentParam.TITLE,notifys.title);
                startActivity(intent);
            }

            notifys = null;
            setIntent(null);
        }
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
                getString(R.string.home_page), R.drawable.icon_homepage, R.drawable.icon_homepage_select, HomeFragment.class);
        TabPagerInfo found = new TabPagerInfo(
                getString(R.string.home_found), R.drawable.icon_found, R.drawable.icon_found_select, FoundFragment.class);
        TabPagerInfo receivable = new TabPagerInfo(
                getString(R.string.home_receivable), R.drawable.icon_recvable, R.drawable.icon_recvable_select, ReceiVablesFragment.class);
        TabPagerInfo mine = new TabPagerInfo(
                getString(R.string.home_mine), R.drawable.icon_mine, R.drawable.icon_mine_select, MineFragment.class);
        list.add(homePage);
//        list.add(found);
        list.add(receivable);
        list.add(mine);
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



    private static final int MIN_DELAY_TIME= 1500;  // 两次点击间隔不能大于1500ms
    private static long lastClickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - lastClickTime > MIN_DELAY_TIME){
                showTips("再按一下退出应用",MIN_DELAY_TIME);
                lastClickTime = currentClickTime;
            }else {
                finish();

                BaseApplication.activityStackManager.cleanAllActivity();
                Process.killProcess(Process.myPid());
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public int reformSequence(){
        String phone = MerchanInfoDB.queryInfo().merchantPhone;
        if (!TextUtils.isEmpty(phone)){
            phone=phone.substring(0,3) + phone.substring(7,phone.length());
        }
        return Integer.valueOf(phone);
    }















}
