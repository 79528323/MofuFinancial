package cn.mofufin.morf.ui.services;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.ExchangeHistory;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MallAPI {


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/moCoinsMall/getMerchandise")
    Observable<BaseResponse<Coupons.DataBean>> getMerchandise(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                                              @Field("merchantCode")String merchantCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/moCoinsMall/exchange")
    Observable<BaseResponse<GeneralResponse.DataBean>> exchange(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                                                @Field("merchantCode")String merchantCode,@Field("goodsCode")String goodsCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/moCoinsMall/getMerchantBag")
    Observable<BaseResponse<MerchantBag.DataBean>> getMerchantBag(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                                                  @Field("merchantCode")String merchantCode, @Field("type")String type);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/moCoinsMall/getRecord")
    Observable<ExchangeHistory> getRecord(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                          @Field("merchantCode")String merchantCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/memberTicketGrade")
    Observable<BaseResult> memberTicketGrade(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                                             @Field("merchantCode")String merchantCode,@Field("mcbGoodsNumber")String mcbGoodsNumber);
}
