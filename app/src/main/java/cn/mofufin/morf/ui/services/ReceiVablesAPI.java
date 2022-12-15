package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface ReceiVablesAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/2001")
    Observable<Order> QueryOrders(@Field("officeCode") String officeCode,
                                                @Field("appKey") String appKey,
                                                @Field("orderBegin") String orderBegin,
                                                @Field("orderEnd") String orderEnd,
                                                @Field("findType") Integer findType,
                                                @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/remitPay/requestDynamicCode")
    Observable<BaseResult> QR(@Field("officeCode") String officeCode,
                              @Field("appKey") String appKey,
                              @Field("payChannelType") String payChannelType,
                              @Field("ordAmt") String ordAmt,
                              @Field("merchantCode") String merchantCode,
                              @Field("mcbGoodsNumber") String mcbGoodsNumber);


}
