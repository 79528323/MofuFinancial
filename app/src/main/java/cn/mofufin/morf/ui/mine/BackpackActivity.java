package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.mofufin.morf.adapter.BackpackAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.services.MallImAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityBackpackBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BackpackActivity extends BaseActivity {
    public final static int SELECTION_TYPE_MPOS = 4;
    public final static int SELECTION_TYPE_SCAN_QR = 6;
    public final static int SELECTION_TYPE_OVER_SEAN = 5;
//    public final static int SELECTION_TYPE_CASH_WITHDRAWAL = 4;
    public final static int SELECTION_TYPE_FINANCIAL = 1;
    public final static int SELECTION_TYPE_REPAY = 3;
    public final static int SELECTION_TYPE_SHORTCUT = 2;
    public final static int SELECTION_TYPE_TELEPHONE = 7;
    public final static int SELECTION_TYPE_PAYMENT = 8;
    public final static int SELECTION_TYPE_MEMBER = 9;


    private ActivityBackpackBinding binding;
    private final String type = "0";
    private BackpackAdapter adapter;

    private ArrayList<MerchantBag.DataBean.ListBean> usexpdList = new ArrayList<>();
    private int selection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_backpack);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        selection = getIntent().getIntExtra(IntentParam.BACK_PACK_SELECTION,selection);
        selection = selection<0?0:selection;
        binding.setIsShowRight(selection<=0);
        adapter = new BackpackAdapter();
        if (selection > 0)
            adapter.setOnClickListener(onClickListener);

        binding.couponList.setLayoutManager(new LinearLayoutManager(this));
        binding.couponList.setAdapter(adapter);
        getBag();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        super.submit(view);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(IntentParam.MERCHANT_BAG_ARRYLIST, (ArrayList<? extends Parcelable>) adapter.getDataList());
        Intent intent = new Intent(this,BackPackHistoryManagerActivity.class);
        intent.putExtra(IntentParam.MERCHANT_BAG_ARRYLIST,usexpdList);
        startActivity(intent);
    }


    //商户商品背包
    public void getBag(){
        Subscription subscription = MallImAPI.getMerchantBag(HttpParam.MERCHANT_BAG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,String.valueOf(selection))
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
                            List<MerchantBag.DataBean.ListBean> list = response.data.getList();
                            Iterator<MerchantBag.DataBean.ListBean> iterator = list.iterator();
                            while (iterator.hasNext()){
                                MerchantBag.DataBean.ListBean bean = iterator.next();
                                if (bean.mcbGoodsState == 1 || bean.mcbGoodsState == 2){
                                    usexpdList.add(bean);
                                    iterator.remove();
                                }
                            }

//                            list = filterList(list);

                            adapter.refresh(list);
                            binding.setHasData(list.size()>0);
                        }else{
                            binding.setHasData(false);
                            showTips(response.data.getReason());
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MerchantBag.DataBean.ListBean bean = (MerchantBag.DataBean.ListBean) v.getTag();
            Intent intent = new Intent();
            intent.putExtra(IntentParam.MERCHANT_BAG,bean);
            setResult(selection,intent);
            finish();
        }
    };


    /**
     * 根据类型过滤票卷
     * @param list
     * @return
     */
//    public List<MerchantBag.DataBean.ListBean> filterList(final List<MerchantBag.DataBean.ListBean> list){
//        if (selection > 0 && !list.isEmpty()){
//            Iterator<MerchantBag.DataBean.ListBean> iterator = list.iterator();
//            while (iterator.hasNext()){
//                MerchantBag.DataBean.ListBean bean = iterator.next();
//                if (bean.mcbGoodsState != 0){
//                    iterator.remove();
//                }else if (selection == SELECTION_TYPE_MPOS){
//                    if (bean.gdGoodsBranchType != 6 && bean.gdGoodsBranchType != 13 && bean.gdGoodsBranchType !=7)
//                        iterator.remove();
//                }else if (selection == SELECTION_TYPE_SCAN_QR){
//                    if (bean.gdGoodsBranchType != 9 && bean.gdGoodsBranchType !=7)
//                        iterator.remove();
//                }else if (selection == SELECTION_TYPE_OVER_SEAN){
//                    if (bean.gdGoodsBranchType != 3 && bean.gdGoodsBranchType != 8 && bean.gdGoodsBranchType !=7)
//                        iterator.remove();
//                }else if (selection == SELECTION_TYPE_CASH_WITHDRAWAL){
//                    if (bean.gdGoodsBranchType != 8)
//                        iterator.remove();
//                }else if (selection == SELECTION_TYPE_FINANCIAL){
//                    if (bean.gdGoodsBranchType != 0 && bean.gdGoodsBranchType != 11 && bean.gdGoodsBranchType !=7)
//                        iterator.remove();
//                }
//            }
//        }
//
//        return list;
//    }
}
