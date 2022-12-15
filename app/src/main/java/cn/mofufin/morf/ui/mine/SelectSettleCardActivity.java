package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.adapter.BankCardAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivitySelectSettleCardBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 更换结算卡列表
 */
public class SelectSettleCardActivity extends BaseActivity {
    private ActivitySelectSettleCardBinding binding;
    private BankCardAdapter adapter;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_settle_card);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        adapter = new BankCardAdapter();
        adapter.setClickListener(onClickListener);
        binding.selectCardList.setLayoutManager(new LinearLayoutManager(this));
        binding.selectCardList.setAdapter(adapter);
        rxManager.onRxEvent(RxEvent.REFRESH_SELECT_CARD).subscribe(new Action1<Object>() {

            @Override
            public void call(Object o) {
                refreshBankCard();
            }
        });

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_SELECT_CARD);
    }

    public void refreshBankCard(){
        Subscription subscription = BankImpAPI.bankInfo(HttpParam.OFFICE_CODE
                ,HttpParam.BANK_INFO_APPKEY,MerchanInfoDB.queryInfo().merchantPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        if (dataBeanBaseResponse.bool){
                            List<User.DataBean.CardInfoBean> cardInfoBeanList = dataBeanBaseResponse.data.getCardInfo();
                            Iterator<User.DataBean.CardInfoBean> iterator = cardInfoBeanList.iterator();
                            User.DataBean.CardInfoBean bean = null;
                            while (iterator.hasNext()){
                                bean = iterator.next();
                                if (bean.cardType == 2)
                                    iterator.remove();
                            }
                            adapter.refresh(cardInfoBeanList);
                        }else {
                            showTips(dataBeanBaseResponse.data.getReason());
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

    public void addCard(View v){
        Intent intent = new Intent(this,SettlementCardAddActivity.class);
        startActivity(intent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            User.DataBean.CardInfoBean cardInfoBean = adapter.getDataList().get(pos);
            Intent intent = new Intent(SelectSettleCardActivity.this,BalanceTransferPasswordActivity.class);
            intent.putExtra(IntentParam.TYPE,4);
            intent.putExtra(IntentParam.BEAN,cardInfoBean);
            startActivity(intent);
        }
    };
}
