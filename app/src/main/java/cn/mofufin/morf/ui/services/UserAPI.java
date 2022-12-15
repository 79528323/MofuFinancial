package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.Audit;
import cn.mofufin.morf.ui.entity.BalanceDetail;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.entity.CoinDetail;
import cn.mofufin.morf.ui.entity.CoinInfo;
import cn.mofufin.morf.ui.entity.CommissionDetail;
import cn.mofufin.morf.ui.entity.ExChanged;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.IndustryInfos;
import cn.mofufin.morf.ui.entity.MposArea;
import cn.mofufin.morf.ui.entity.MposRatio;
import cn.mofufin.morf.ui.entity.PushMerchant;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface UserAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/merchantApp/1686")
    Observable<BaseResponse<GeneralResponse.DataBean>> register(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                                @Field("merchantPhone")String merchantPhone, @Field("smsCode")String smsCode,
                                                                @Field("password")String password, @Field("spreadPhone")String spreadPhone);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/merchantApp/1001")
    Observable<BaseResponse<GeneralResponse.DataBean>> getSMSCode(@Field("merchantPhone")String merchantPhone
            ,@Field("appKey")String appKey,@Field("officeCode")String officeCode
            ,@Field("spreadPhone")String spreadPhone,@Field("newMerchantPhone")String newMerchantPhone);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/merchantApp/1690")
    Observable<BaseResponse<User.DataBean>> Logins(@Field("appKey")String appKey, @Field("officeCode")String officeCode
            , @Field("loginName")String loginName, @Field("password")String password,@Field("currentFacility")String currentFacility);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE + "/merchantApp/1688")
    Observable<BaseResponse<GeneralResponse.DataBean>> resetLoginPassword(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                                 @Field("merchantPhone")String merchantPhone
                                                                ,@Field("smsCode") String smsCode,
                                                                 @Field("newPassword")String newPassword,
                                                                 @Field("repeatPassword")String repeatPassword);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE + "/merchantApp/2002")
    Observable<PushMerchant> getPushmerchant(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                      @Field("merchantCode")String merchantPhone,@Field("version") String version,
                                                                    @Field("queryType") String queryType,@Field("queryDate") String queryDate);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE + "/merchantApp/1688")
    Observable<BaseResponse<GeneralResponse.DataBean>> resetTransPassword(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                                          @Field("merchantPhone")String merchantPhone,
                                                                          @Field("smsCode") String smsCode,
                                                                          @Field("newPassword")String newPassword,
                                                                          @Field("repeatPassword")String repeatPassword,
                                                                          @Field("IDName")String IDName,
                                                                          @Field("IDNumber")String IDNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMerNewRebate")
    Observable<Ranking> queryMerRebate(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                       @Field("merchantCode") String merchantCode,@Field("version")String version,@Field("date")String date);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMerRebateDetail")
    Observable<CommissionDetail> queryMerRebateDetail(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                      @Field("merchantCode") String merchantCode, @Field("dateBegin")String dateBegin);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/queryMerBalanceDetail")
    Observable<BalanceDetail> queryBalanceDetail(@Field("appKey")String appKey, @Field("officeCode") String officeCode,
                                                    @Field("merchantCode") String merchantCode, @Field("orderBegin")String orderBegin,
                                                    @Field("orderEnd")String orderEnd, @Field("cdWay")int cdWay);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/merRebateTrunBalance")
    Observable<BaseResult> merRebateTrunBalance(@Field("appKey")String appKey, @Field("officeCode") String officeCode,
                                                @Field("merchantCode") String merchantCode, @Field("money")String money,
                                                @Field("password")String password);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMerNewMoBi")
    Observable<Coin> queryMerMobi(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                    @Field("merchantCode") String merchantCode,@Field("version")String version, @Field("date")String date);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMerMoBiDetail")
    Observable<CoinDetail> queryMerMoBiDetail(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                              @Field("merchantCode") String merchantCode,@Field("dateBegin")String dateBegin);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/getMoBiSetInfo")
    Observable<CoinInfo> getMoBiSetInfo(@Field("appKey")String appKey);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/getUSDBuyCash")
    Observable<ExChanged> getUSDBuyCash(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                        @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/getHKDBuyCash")
    Observable<ExChanged> getHKDBuyCash(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                        @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/getUSDToBalance")
    Observable<ExChanged> getUSDToBalance(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                        @Field("merchantCode") String merchantCode,@Field("USD")String USD);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/getHKDToBalance")
    Observable<ExChanged> getHKDToBalance(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                          @Field("merchantCode") String merchantCode,@Field("HKD")String HKD);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/merBalanceWithd")
    Observable<BaseResult> merBalanceWithd(@Field("appKey")String appKey, @Field("officeCode") String officeCode,
                                           @Field("merchantCode") String merchantCode, @Field("money")String money,
                                           @Field("password")String password,@Field("bankCode") String bankCode,
                                           @Field("mcbGoodsNumber")String mcbGoodsNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/merchantApp/updateAccountName")
    Observable<BaseResponse<GeneralResponse>> updateAccountName(@Field("appKey")String appKey, @Field("officeCode") String officeCode,
                                           @Field("oldMerchantPhone") String oldMerchantPhone,
                                             @Field("newMerchantPhone")String newMerchantPhone,
                                           @Field("idCardNo")String idCardNo,@Field("smsCode") String smsCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/mPosMercQuery")
    Observable<IndustryInfos> mPosMercQuery(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                            @Field("merchantCode") String merchantCode,@Field("version")String version,
                                            @Field("longitude") String longitude,@Field("latitude") String latitude,
                                            @Field("queryType") String queryType,@Field("payMoney") String payMoney);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMerMposRatio")
    Observable<MposRatio> queryMerMposRatio(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                            @Field("merchantCode") String merchantCode, @Field("version")String version,
                                            @Field("queryType") String queryType);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/iosAudit")
    Observable<Audit> iosAudit(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                               @Field("operType") String operType, @Field("version")String version,
                               @Field("appVersion") String appVersion, @Field("appState") String appState);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/queryMposArea")
    Observable<MposArea> queryMposArea(@Field("appKey")String appKey, @Field("version")String version);

}
