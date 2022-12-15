package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface SubMissionAPI {

    /**
     * 身份认证接口
     * @param body
     * @return
     */
    @POST(Common.HOSTTYPE +"/merchantApp/1866")
    Observable<BaseResponse<GeneralResponse.DataBean>> identityAuthen(@Body RequestBody body);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/merchantApp/2000")
    Observable<MofuResult> connectionMPos(@Field("officeCode") String officeCode, @Field("appKey")String appKey,
                                                        @Field("merchantCode")String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/merchantApp/1788")
    Observable<BaseResponse<User.DataBean>> refreshMerchantInfo(@Field("officeCode") String officeCode,
                                                                @Field("appKey")String appKey,
                                                                @Field("merchantPhone")String merchantPhone,
                                                                @Field("merchantCode")String merchantCode);

    @POST(Common.HOSTTYPE +"/remitPay/applyRemitPayFeedPart")
    Observable<BaseResult> identityScanPay(@Body RequestBody body);

    /**
     * 获取银行类别
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param bankCardNo
     * @return
     */
    @FormUrlEncoded
    @POST(Common.HOSTTYPE +"/merchantApp/1867")
    Observable<BaseResponse<GeneralResponse.DataBean>> acquireBankCategories(@Field("officeCode") String officeCode,
                                                                             @Field("appKey")String appKey,
                                                                             @Field("merchantPhone")String merchantPhone,
                                                                             @Field("bankCardNo")String bankCardNo);

}
