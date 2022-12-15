package cn.mofufin.morf.ui.services;


import androidx.annotation.NonNull;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Deposit;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

public class ChargeImpAPI {

    /**
     * 获取MPOS计划
     * @param officeCode
     * @param appKey
     * @return
     */
    public static Observable<BaseResponse<Deposit.DataBean>> getMposPlan(@NonNull String officeCode, @NonNull String appKey){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ChargeAPI chargeAPI=retrofit.create(ChargeAPI.class);
        Observable<BaseResponse<Deposit.DataBean>> observable = chargeAPI.getMposPlan(officeCode,appKey);
        return observable.compose(new FullResponseTransformer<Deposit.DataBean>());
    }


    /**
     * 激活Mpos(充值保证金)
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param payPassword
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> activeMposPlan(@NonNull String officeCode, @NonNull String appKey,
                                                                                    @NonNull String merchantCode, @NonNull String payPassword,
                                                                                    @NonNull String addressNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ChargeAPI chargeAPI=retrofit.create(ChargeAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = chargeAPI.activeMposPlan(officeCode,appKey,merchantCode,payPassword,addressNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 支付宝补贴入账
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param orderCode
     * @return
     */
    public static Observable<BaseResult> merBalanceAliPayEnter(@NonNull String officeCode, @NonNull String appKey,
                                                                                    @NonNull String merchantCode, @NonNull String orderCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ChargeAPI chargeAPI=retrofit.create(ChargeAPI.class);
        Observable<BaseResult> observable = chargeAPI.merBalanceAliPayEnter(officeCode,appKey,merchantCode,orderCode);
        return observable.compose(new MofuResultResponseTransformer());
    }




}
