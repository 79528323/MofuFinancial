package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BalanceDetail;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.entity.CoinDetail;
import cn.mofufin.morf.ui.entity.CoinInfo;
import cn.mofufin.morf.ui.entity.CommissionDetail;
import cn.mofufin.morf.ui.entity.ExChanged;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.entity.LoanBanner;
import cn.mofufin.morf.ui.entity.LoanNotify;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.LoanRepayCord;
import cn.mofufin.morf.ui.entity.LoansControlInfo;
import cn.mofufin.morf.ui.entity.PushMerchant;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.entity.RecordDetails;
import cn.mofufin.morf.ui.entity.RefundPlan;
import cn.mofufin.morf.ui.entity.SelfImgInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface LoanAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryLoansInform")
    Observable<LoanNotify> queryLoansInform(@Field("appKey") String appKey,
                                                          @Field("type") String type, @Field("version") String version);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryLoansImg")
    Observable<LoanBanner> queryLoansImg(@Field("appKey") String appKey, @Field("version") String version);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryLoansProduct")
    Observable<LoanProduct> queryLoansProduct(@Field("appKey") String appKey, @Field("officeCode")String officeCode, @Field("merchantCode")String merchantCode, @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/sumitApplyLoans")
    Observable<BaseResult> sumitApplyLoans(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                           @Field("merchantCode")String merchantCode, @Field("version") String version,
                                           @Field("lpCode")String lpCode, @Field("loansMoney") String loansMoney,
                                           @Field("loansPeriod")String loansPeriod,
                                           @Field("loansInterestWay") String loansInterestWay,
                                           @Field("addressBook") String addressBook);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/showRefundPlan")
    Observable<RefundPlan> showRefundPlan(@Field("appKey") String appKey, @Field("version") String version,
                                          @Field("period") Integer period, @Field("way") Integer way,
                                          @Field("money") Integer money, @Field("dayRate") Double dayRate);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/querySelfSubmitState")
    Observable<LoansControlInfo> querySelfSubmitState(@Field("appKey") String appKey,@Field("officeCode")String officeCode,
                                                      @Field("merchantCode")String merchantCode,@Field("version") String version);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerSelfText")
    Observable<SelfTextInfo> queryMerSelfText(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/uploadSelfText")
    Observable<BaseResult> uploadSelfText(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                          @Field("merchantCode")String merchantCode, @Field("version") String version,
                                          @Field("debitCardCode")String debitCardCode,@Field("mtCityCd")String mtCityCd,
                                          @Field("mtCityCdName")String mtCityCdName,@Field("mtStateCd")String mtStateCd,
                                          @Field("mtStateCdName")String mtStateCdName,@Field("dtIssue")String dtIssue,
                                          @Field("dtExpiry")String dtExpiry,@Field("isLongEffec")String isLongEffec,
                                          @Field("dtRegistered")String dtRegistered,@Field("mtGenderCd")String mtGenderCd,
                                          @Field("mtMaritalStsCd")String mtMaritalStsCd,@Field("mtEduLvlCd")String mtEduLvlCd,
                                          @Field("mtResidenceStsCd")String mtResidenceStsCd,@Field("mtIndvMobileUsageStsCd")String mtIndvMobileUsageStsCd,
                                          @Field("email")String email,@Field("qq")String qq,@Field("weChat")String weChat,
                                          @Field("yearIncAmt")String yearIncAmt,@Field("isFamily")String isFamily,@Field("hasCar")String hasCar,
                                          @Field("plateNo")String plateNo,@Field("hasCreditCard")String hasCreditCard,@Field("creditCardLines")String creditCardLines,
                                          @Field("isBizEntit")String isBizEntit,@Field("employerNm")String employerNm,@Field("mtJobSectorCd")String mtJobSectorCd,
                                          @Field("mtJobSectorCdName")String mtJobSectorCdName,@Field("mtPosHeldCd")String mtPosHeldCd,@Field("mtPosHeldCdName")String mtPosHeldCdName,@Field("employerPhone") String employerPhone,
                                          @Field("isComb")String isComb,@Field("bizRegNo")String bizRegNo,@Field("bizAddr")String bizAddr,@Field("isBussLongEffec")String isBussLongEffec,
                                          @Field("bizRegDtExpiry")String bizRegDtExpiry,@Field("isLegalRep")String isLegalRep,
                                          @Field("currentTotal")String currentTotal,@Field("yearSaleMarginsRate")String yearSaleMarginsRate,@Field("ratal")String Ratal);



    @POST(Common.HOSTTYPE +"/loans/uploadSelfImg")
    Observable<BaseResult> uploadSelfImg(@Body RequestBody body);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerSelfImg")
    Observable<SelfImgInfo> queryMerSelfImg(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                            @Field("merchantCode")String merchantCode, @Field("version") String version);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerSpouseText")
    Observable<SelfTextInfo> queryMerSpouseText(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/uploadSpouseText")
    Observable<BaseResult> uploadSpouseText(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                          @Field("merchantCode")String merchantCode, @Field("version") String version,
                                          @Field("nm")String nm,@Field("idNo")String idNo,@Field("mtCityCd")String mtCityCd,
                                          @Field("mtCityCdName")String mtCityCdName,@Field("mtStateCd")String mtStateCd,
                                          @Field("mtStateCdName")String mtStateCdName,@Field("dtIssue")String dtIssue,
                                          @Field("dtExpiry")String dtExpiry,@Field("isLongEffec")String isLongEffec,
                                          @Field("dtRegistered")String dtRegistered,@Field("mtGenderCd")String mtGenderCd,
                                          @Field("mtMaritalStsCd")String mtMaritalStsCd,@Field("mtEduLvlCd")String mtEduLvlCd,
                                          @Field("mtResidenceStsCd")String mtResidenceStsCd,@Field("mobileNo")String mobileNo,@Field("mtIndvMobileUsageStsCd")String mtIndvMobileUsageStsCd,
                                          @Field("email")String email,@Field("qq")String qq,@Field("weChat")String weChat,
                                          @Field("yearIncAmt")String yearIncAmt,@Field("isFamily")String isFamily,@Field("hasCar")String hasCar,
                                          @Field("plateNo")String plateNo,@Field("hasCreditCard")String hasCreditCard,@Field("creditCardLines")String creditCardLines);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/uploadFriend")
    Observable<BaseResult> uploadFriend(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                            @Field("merchantCode")String merchantCode, @Field("version") String version,
                                            @Field("mtCifRelCd")String mtCifRelCd,@Field("mtCifRelCdName")String mtCifRelCdName,
                                            @Field("nm")String nm,
                                            @Field("idNo")String idNo,@Field("mtGenderCd")String mtGenderCd,
                                            @Field("mobileNo")String mobileNo,@Field("mtIncSourceCd")String mtIncSourceCd,
                                            @Field("mtIncSourceCdName")String mtIncSourceCdName,@Field("yearIncAmt")String yearIncAmt);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerFriendText")
    Observable<SelfTextInfo> queryMerFriendText(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                                @Field("merchantCode")String merchantCode, @Field("version") String version);




    @POST(Common.HOSTTYPE +"/loans/uploadSpouseImg")
    Observable<BaseResult> uploadSpouseImg(@Body RequestBody body);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerSpouseImg")
    Observable<SelfImgInfo> queryMerSpouseImg(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                            @Field("merchantCode")String merchantCode, @Field("version") String version);



    @POST(Common.HOSTTYPE +"/loans/uploadSelfPrivateImg")
    Observable<BaseResult> uploadSelfPrivateImg(@Body RequestBody body);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryMerSelfPrivateImg")
    Observable<SelfImgInfo> queryMerSelfPrivateImg(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);




    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryBusinessImg")
    Observable<SelfImgInfo> queryBusinessImg(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);




    @POST(Common.HOSTTYPE +"/loans/uploadBusinessImg")
    Observable<BaseResult> uploadBusinessImg(@Body RequestBody body);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryLoansApplyRecord")
    Observable<LoanApplyRecord> queryLoansApplyRecord(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                                      @Field("merchantCode")String merchantCode, @Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryRefundPlan")
    Observable<LoanRepayCord> queryRefundPlan(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryRefundPlanDetails")
    Observable<RecordDetails> queryRefundPlanDetails(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                                     @Field("merchantCode")String merchantCode, @Field("version") String version ,
                                                     @Field("refundPlanCode") String refundPlanCode);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/loans/queryUserLoansProtocol")
    Observable<BaseResult> queryUserLoansProtocol(@Field("appKey") String appKey, @Field("officeCode")String officeCode,
                                              @Field("merchantCode")String merchantCode, @Field("version") String version);

}
