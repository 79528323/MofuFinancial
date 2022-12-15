package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import java.util.List;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.AddressAdapter;
import cn.mofufin.morf.databinding.ActivityReceivingAddressBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Address;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 押金
 */
public class ReceivingAddressActivity extends BaseActivity {

    private ActivityReceivingAddressBinding binding;
    private AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_receiving_address);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        addressAdapter = new AddressAdapter();
        addressAdapter.setOnClickListener(selectClick);
        addressAdapter.setActivity(this);
        binding.addressList.setLayoutManager(new LinearLayoutManager(this));
        binding.addressList.setAdapter(addressAdapter);

        rxManager.onRxEvent(RxEvent.REFRESH_RECEI_ADDRESS)
                .subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                queryMposAddress(AddNewAdressActivity.SEL);
            }
        });

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_RECEI_ADDRESS);
    }

    public void add(View view){
        Intent intent = new Intent(ReceivingAddressActivity.this,AddNewAdressActivity.class);
        intent.putExtra(IntentParam.TYPE,AddNewAdressActivity.TYPE_ADD);
        startActivity(intent);
    }

    public void editor(View view){
        Address.DataBean.AddressInfoBean address = (Address.DataBean.AddressInfoBean) view.getTag();
        Intent intent = new Intent(ReceivingAddressActivity.this,AddNewAdressActivity.class);
        intent.putExtra(IntentParam.BEAN,address);
        intent.putExtra(IntentParam.TYPE,AddNewAdressActivity.TYPE_DEL);
        startActivity(intent);
    }


//    View.OnClickListener editorClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };

    View.OnClickListener selectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Address.DataBean.AddressInfoBean address = (Address.DataBean.AddressInfoBean) v.getTag();
            Intent intent = new Intent(ReceivingAddressActivity.this,AddNewAdressActivity.class);
            intent.putExtra(IntentParam.BEAN,address);
            intent.putExtra(IntentParam.TYPE,AddNewAdressActivity.TYPE_DEL);
            setResult(RechargeToDepositActivity.CHOOSE_RECEI_ADDRESS,intent);
            finish();
        }
    };

    public void queryMposAddress(String operType){
        Subscription subscription = UtilsImpAPI.sendPos(HttpParam.SENDPOS_ADDRESS_KEY,HttpParam.OFFICE_CODE,operType,
                null,null,null,MerchanInfoDB.queryInfo().merchantCode,null)
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
                .subscribe(new Action1<BaseResponse<Address.DataBean>>() {
                    @Override
                    public void call(BaseResponse<Address.DataBean> response) {
//                        showTips(response.data.getReason());
                        if (response.bool){
                            List<Address.DataBean.AddressInfoBean> list = response.data.getAddressInfo();
                            addressAdapter.refresh(list);
                            binding.setHasData(list.size()>0);
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
