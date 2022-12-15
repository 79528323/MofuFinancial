package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.CoinInfo;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityMofuPlayBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MofuPlayActivity extends BaseActivity {
    private ActivityMofuPlayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mofu_play);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        Subscription subscription = UserImpAPI.getMoBiSetInfo(HttpParam.GET_MOBI_SET_INFO)
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
                .subscribe(new Action1<CoinInfo>() {
                    @Override
                    public void call(CoinInfo coinInfo) {
                        if (coinInfo.result_Code == 0){
                            binding.setBean(coinInfo.set);
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
