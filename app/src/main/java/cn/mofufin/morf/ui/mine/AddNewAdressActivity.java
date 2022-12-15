package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAddNewAddressBinding;
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

public class AddNewAdressActivity extends BaseActivity {
    public static final int TYPE_ADD = 0X01;
    public static final int TYPE_DEL = 0X02;

    private ActivityAddNewAddressBinding binding;
    private int type;
    private Address.DataBean.AddressInfoBean address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_new_address);
        binding.setHandlers(this);

        type = getIntent().getIntExtra(IntentParam.TYPE,0);
        binding.setType(type);
        if (type==TYPE_ADD){
            binding.setTitle(getString(R.string.address_2));
        }else {
            binding.setTitle(getString(R.string.address_4));
            address = getIntent().getParcelableExtra(IntentParam.BEAN);
            binding.setAddress(address);
        }
        binding.executePendingBindings();

        binding.address.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        binding.address.setGravity(Gravity.TOP);
        binding.address.setSingleLine(false);
        binding.address.setHorizontallyScrolling(false);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void submit(View view) {
        if (TextUtils.isEmpty(binding.name.getText().toString())){
            showTips("请输入收货人姓名");
            return;
        }else if (TextUtils.isEmpty(binding.phone.getText().toString())){
            showTips("请输入收货人电话");
            return;
        }else if (binding.phone.getText().toString().length()!=11){
            showTips("请输入正确的收货人电话");
            return;
        }else if (TextUtils.isEmpty(binding.address.getText().toString())){
            showTips("请输入收货人地址");
            return;
        }

        if (address==null){
            address = new Address.DataBean.AddressInfoBean();
        }

        address.setTakeAddress(binding.address.getText().toString());
        address.setTakePersonName(binding.name.getText().toString());
        address.setTakePersonPhone(binding.phone.getText().toString());

        if (type == TYPE_ADD){
            mposAddress(ADD,address);
        }else
            mposAddress(UPD,address);

        super.submit(view);
    }

    public void del(View view){
        mposAddress(DEL,address);
    }


    /**
     * operType(操作类型可传参数):
     *       传入 “sel”表示查询所有收货地址
     *       传入 “add”表示增加收货地址
     *       takeAddress,takePersonName,takePersonPhone 三个参数不可为空
     *        传入 “upd”表示变更收货地址
     * takeAddress,takePersonName,takePersonPhone 三个参数不可为空
     * 传入   “del” 表示删除收货地址
     *  addressNumber 地址编号不可为空
     */
    public static final String SEL = "sel";
    public static final String ADD = "add";
    public static final String UPD = "upd";
    public static final String DEL = "del";

    public void mposAddress(final String operType,final Address.DataBean.AddressInfoBean address){
        String takeAddress="",takePersonName="",takePersonPhone="";
        int addressNumber = 0;

        if (TextUtils.equals(operType,ADD) || TextUtils.equals(operType,UPD)){
            takeAddress = address.getTakeAddress();
            takePersonName = address.getTakePersonName();
            takePersonPhone = address.getTakePersonPhone();
            if (TextUtils.equals(operType,UPD))
                addressNumber = address.getAddressNumber();
        }else if (TextUtils.equals(operType,DEL)){
            addressNumber = address.getAddressNumber();
        }

        Subscription subscription = UtilsImpAPI.sendPos(HttpParam.SENDPOS_ADDRESS_KEY,HttpParam.OFFICE_CODE,operType,
                addressNumber,takeAddress,takePersonName,MerchanInfoDB.queryInfo().merchantCode,takePersonPhone)
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
                        showTips(response.data.getReason());
                        if (response.bool){
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_RECEI_ADDRESS);
                            if (TextUtils.equals(operType,DEL)){
                                RxBus.getInstance().post(RxEvent.REFRESH_CLEAR_RECEI_ADDRESS,address.getAddressNumber());
                            }
                            finish();
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
