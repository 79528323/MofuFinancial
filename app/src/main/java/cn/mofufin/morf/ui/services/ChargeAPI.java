package cn.mofufin.morf.ui.services;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Deposit;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
public interface ChargeAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/mposPlan/getMposPlan")
    Observable<BaseResponse<Deposit.DataBean>> getMposPlan(@Field("officeCode") String officeCode,
                                                                    @Field("appKey")String appKey);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/mposPlan/activeMposPlan")
    Observable<BaseResponse<GeneralResponse.DataBean>> activeMposPlan(@Field("officeCode") String officeCode,
                                                           @Field("appKey")String appKey,@Field("merchantCode")String merchantCode,
                                                                      @Field("payPassword")String payPassword,
                                                                      @Field("addressNumber")String addressNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/balance/merBalanceAliPayEnter")
    Observable<BaseResult> merBalanceAliPayEnter(@Field("officeCode") String officeCode,
                                                                      @Field("appKey")String appKey,
                                                 @Field("merchantCode")String merchantCode,
                                                                      @Field("orderCode")String orderCode);

}
