package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class ReceiVablesImpAPI {

    public static Observable<Order> QueryOrders(@NonNull String officeCode, @NonNull String appKey,
                                                              @NonNull String orderBegin, @NonNull String orderEnd,
                                                              @NonNull Integer findType, @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ReceiVablesAPI receiVablesAPI = retrofit.create(ReceiVablesAPI.class);
        Observable observable = receiVablesAPI.QueryOrders(
                officeCode,appKey,orderBegin,orderEnd,findType,merchantCode);
        return observable.compose(new MofuResultResponseTransformer<Order>());
    }

    public static Observable<BaseResult> QR(@NonNull String officeCode, @NonNull String appKey,
                                            @NonNull String payChannelType, @NonNull String ordAmt, @NonNull String merchantCode,
                                            @Nullable String mcbGoodsNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ReceiVablesAPI receiVablesAPI = retrofit.create(ReceiVablesAPI.class);
        Observable observable = receiVablesAPI.QR(officeCode,appKey,payChannelType,ordAmt,merchantCode,mcbGoodsNumber);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }


}
