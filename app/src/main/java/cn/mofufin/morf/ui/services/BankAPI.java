package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.SingleBankType;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface BankAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1868")
    Observable<BaseResponse<User.DataBean>> bankInfo(@Field("officeCode") String officeCode,
                                      @Field("appKey") String appKey,
                                      @Field("merchantPhone") String merchantPhone);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1869")
    Observable<BaseResponse<User.DataBean>> addbankInfo(@Field("officeCode") String officeCode,
                                                     @Field("appKey") String appKey,
                                                     @Field("merchantPhone") String merchantPhone,
                                                        @Field("cardholder")String cardholder,
                                                        @Field("debitCard")String debitCard,
                                                        @Field("bankName")String bankName,
                                                        @Field("cardType")String cardType,
                                                        @Field("mobile")String mobile);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1867")
    Observable<BaseResponse<GeneralResponse.DataBean>> bankCardType(@Field("officeCode") String officeCode,
                                                                    @Field("appKey") String appKey,
                                                                    @Field("merchantPhone") String merchantPhone,
                                                                    @Field("bankCardNo")String bankCardNo);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1870")
    Observable<BaseResponse<User.DataBean>> addCreditbankInfo(@Field("officeCode") String officeCode,
                                                        @Field("appKey") String appKey,
                                                        @Field("merchantPhone") String merchantPhone,
                                                        @Field("cardholder")String cardholder,
                                                        @Field("creditCard")String creditCard,
                                                        @Field("bankName")String bankName,
                                                        @Field("cardType")String cardType,
                                                              @Field("accountDay")String accountDay,
                                                              @Field("repaymentDay")String repaymentDay,
                                                              @Field("cardReDate")String cardReDate,
                                                              @Field("cardBackNo")String cardBackNo,
                                                              @Field("mobile")String phoneNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1873")
    Observable<BaseResponse<SingleBankType.DataBean>> singleBankCardType(@Field("officeCode") String officeCode,
                                                                         @Field("appKey") String appKey,
                                                                         @Field("bankCard") String bankCard);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1871")
    Observable<BaseResponse<GeneralResponse.DataBean>> UnbindbankCard(@Field("officeCode") String officeCode,
                                                                      @Field("appKey") String appKey,
                                                                      @Field("merchantPhone") String merchantPhone,
                                                                      @Field("payPassword")String payPassword,
                                                                      @Field("creditCard")String creditCard);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/sundry/mposUpdateCard")
    Observable<BaseResult> mposUpdateCard(@Field("officeCode") String officeCode,
                                                                      @Field("appKey") String appKey,
                                                                      @Field("merchantCode") String merchantCode,
                                                                      @Field("payPassword")String payPassword,
                                                                      @Field("cardCode")String cardCode);
}
