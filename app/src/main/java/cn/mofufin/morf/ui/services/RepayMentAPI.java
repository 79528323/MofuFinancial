package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.FallProvinceCity;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.ProjectDetails;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.entity.RepayMentDay;
import cn.mofufin.morf.ui.entity.RepayMentProject;
import cn.mofufin.morf.ui.entity.SingleBankType;
import cn.mofufin.morf.ui.entity.SupportBank;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface RepayMentAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/queryChannel")
    Observable<RepayChannel> queryChannel(@Field("officeCode") String officeCode,
                                          @Field("appKey") String appKey,
                                          @Field("merchantCode") String merchantCode,
                                          @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/queryChannelFallProvinceCity")
    Observable<FallProvinceCity> queryChannelFallProvinceCity(@Field("appKey") String appKey,
                                                              @Field("rcNumber") String rcNumber,
                                                              @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/createRefundPlan")
    Observable<ProjectResult> createRefundPlan(@Field("appKey") String appKey, @Field("officeCode") String officeCode,
                                               @Field("merchantCode") String merchantCode,
                                               @Field("version") String version,
                                               @Field("rcNumber") String rcNumber,
                                               @Field("creditCardCode") String creditCardCode,
                                               @Field("consumeMoney") String consumeMoney,
                                               @Field("refundDate") String refundDate,
                                               @Field("provinceCode") String provinceCode,
                                               @Field("provinceName") String provinceName,
                                               @Field("cityCode") String cityCode,
                                               @Field("cityName") String cityName);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/getRefundDay")
    Observable<RepayMentDay> getRefundDay(@Field("appKey") String appKey,
                                          @Field("billDay") String billDay,
                                          @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/executeRefundPlan")
    Observable<MofuResult> executeRefundPlan(@Field("appKey") String appKey,
                                             @Field("officeCode") String officeCode,
                                             @Field("merchantCode") String merchantCode,
                                             @Field("version") String version,
                                             @Field("rpOrderCode") String rpOrderCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/queryRefundPlanDetails/")
    Observable<ProjectDetails> queryRefundPlanDetails(@Field("appKey") String appKey,
                                                      @Field("officeCode") String officeCode,
                                                      @Field("merchantCode") String merchantCode,
                                                      @Field("version") String version,
                                                      @Field("rpOrderCode") String rpOrderCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/queryRefundPlan/")
    Observable<RepayMentProject> queryRefundPlan(@Field("appKey") String appKey,
                                                 @Field("officeCode") String officeCode,
                                                 @Field("merchantCode") String merchantCode,
                                                 @Field("version") String version,
                                                 @Field("beginDate") String beginDate,
                                                 @Field("endDate") String endDate);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/queryChannelSupportBank")
    Observable<SupportBank> queryChannelSupportBank(@Field("officeCode") String officeCode,
                                                    @Field("appKey") String appKey,
                                                    @Field("rcNumber") String rcNumber,
                                                    @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/cancelRefundPlan")
    Observable<MofuResult> cancelRefundPlan(@Field("officeCode") String officeCode,
                                          @Field("appKey") String appKey,
                                          @Field("merchantCode") String merchantCode,
                                          @Field("version") String version,
                                            @Field("rpOrderCode") String rpOrderCode);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/updateCreditCardInfo")
    Observable<MofuResult> updateCreditCardInfo(@Field("officeCode") String officeCode,
                                            @Field("appKey") String appKey,
                                            @Field("merchantCode") String merchantCode,
                                            @Field("version") String version,
                                            @Field("creditCardCode") String creditCardCode,
                                                @Field("billDay") Integer billDay,
                                                @Field("refundDay") Integer refundDay);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/initRefundChannel")
    Observable<MofuResult> initRefundChannel(@Field("officeCode") String officeCode,
                                                @Field("appKey") String appKey,
                                                @Field("merchantCode") String merchantCode,
                                                @Field("version") String version,
                                                @Field("creditCardCode") String creditCardCode,
                                                @Field("rcNumber") String rcNumber);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/initRefundChannelBindCardSmsSend")
    Observable<MofuResult> initRefundChannelBindCardSmsSend(@Field("officeCode") String officeCode,
                                             @Field("appKey") String appKey,
                                             @Field("merchantCode") String merchantCode,
                                             @Field("version") String version,
                                             @Field("creditCardCode") String creditCardCode,
                                             @Field("rcNumber") String rcNumber);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/refund/initRefundChannelBindCardSmsAffirm")
    Observable<MofuResult> initRefundChannelBindCardSmsAffirm(@Field("officeCode") String officeCode,
                                                            @Field("appKey") String appKey,
                                                            @Field("merchantCode") String merchantCode,
                                                            @Field("version") String version,
                                                            @Field("creditCardCode") String creditCardCode,
                                                            @Field("rcNumber") String rcNumber,
                                                              @Field("smsCode") String smsCode);

}
