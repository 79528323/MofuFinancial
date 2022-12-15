package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.adapter.MemberPagerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.MofuMallActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.MemberBanner;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.CouponMemberDialog;
import cn.mofufin.morf.databinding.ActivityMemberInfoBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MemberInfoActivity extends BaseActivity {
    private ActivityMemberInfoBinding binding;
    private int[] res= {R.drawable.icon_ordinary,R.drawable.icon_golden,R.drawable.icon_diamonds};
    private final String[] names = {"普通会员","金牌会员","钻石会员"};
    private CouponMemberDialog dialog;
    private final static String type = "9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_member_info);
        binding.setHandlers(this);
        initView();
    }

    public void initView() {
        List<MemberBanner> viewList = new ArrayList<>();
        for (int i=0; i<res.length; i++){
            viewList.add(new MemberBanner(res[i],names[i]));
        }

        MemberPagerAdapter adapter = new MemberPagerAdapter(this,viewList);
        binding.memberViewpager.setAdapter(adapter);



        dialog = new CouponMemberDialog(this);
//        getMemberCoupon();

        rxManager.onRxEvent(RxEvent.MEMBER_COUPON_UPGRADE)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        upgrade((MerchantBag.DataBean.ListBean)o);
                    }
                });
    }

    @Override
    protected void onResume() {
        getMemberCoupon();
        super.onResume();
    }

    //摩富商城兑换升级券
    public void exMemberTicket(View view){
        startActivity(new Intent(this, MofuMallActivity.class));
    }

    //会员升级
    public void upgrade(MerchantBag.DataBean.ListBean bean){
        if (bean==null)
            return;
        Subscription subscription = MallImAPI.memberTicketGrade(
                HttpParam.MEMBER_UPGRADE_KEY,HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode,
                bean.mcbGoodsNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        Intent intent = new Intent(
                                MemberInfoActivity.this, BalanceTransferStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code==0);
                        intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_MEMBER_UPGRADE);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        intent.putExtra(IntentParam.TITLE,getString(R.string.my_info_6));
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable, true);
                    }
                });
        rxManager.add(subscription);
    }

    //获取会员卷
    public void getMemberCoupon(){
        Subscription subscription = MallImAPI.getMerchantBag(HttpParam.MERCHANT_BAG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<BaseResponse<MerchantBag.DataBean>>() {
                    @Override
                    public void call(BaseResponse<MerchantBag.DataBean> response) {
                        if (response.bool){
                            dialog.refreshData(response.data.getList());
                            binding.setInterval(response.data.getList().size()>0?1:0);
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

    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void upgradeMember(View view){
        dialog.showDialog();
    }
}
