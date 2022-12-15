package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.Address;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.CreditApply;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.Member;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.Tree;
import cn.mofufin.morf.ui.entity.WxPays;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface UtilsAPI {

    /**
     *    查询系统apk
     * @param officeCode
     * @return
     */
    @FormUrlEncoded
    @POST(Common.SYSTEM_APK +"/queryApkFileVersion")
    Observable<MofuResult> QuerySystemAPK(@Field("officeCode") String officeCode);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/Share/queryAlert")
    Observable<BaseResult> HomePageContent(@Field("officeCode") String officeCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/tree/query")
    Observable<Member> treeMemberQuery(@Field("appKey") String appKey);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/tree/hit")
    Observable<Tree> hit(@Field("appKey") String appKey, @Field("merchantCode") String merchantCode);




    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/alipay/createAliOrderSDK")
    Observable<BaseResult> createAliOrder(@Field("appKey")String appKey,@Field("officeCode")String officeCode,
                                          @Field("merchantCode")String merchantCode,@Field("money")String money);


    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/credit/getCreditForApp")
    Observable<CreditApply> getCreditForApp(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                            @Field("merchantCode")String merchantCode);


    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/alipay/createAliOrderH5")
    Observable<BaseResult> createAliOrderH5(@Field("appKey")String appKey,@Field("officeCode")String officeCode,
                                          @Field("merchantCode")String merchantCode,@Field("money")String money);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/sundry/getRotationChart")
    Observable<BaseResponse<GeneralResponse.DataBean>> getRotationChart(
            @Field("appKey")String appKey, @Field("officeCode")String officeCode);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/merchantApp/sendPos")
    Observable<BaseResponse<Address.DataBean>> sendPos(
            @Field("appKey")String appKey, @Field("officeCode")String officeCode,@Field("merchantCode")String merchantCode,
            @Field("operType")String operType,
            @Field("addressNumber")Integer addressNumber,@Field("takeAddress")String takeAddress,
            @Field("takePersonName")String takePersonName,@Field("takePersonPhone")String takePersonPhone);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+"/balance/queryWithdrawDate")
    Observable<MofuResult> queryWithdrawDate(
            @Field("appKey")String appKey, @Field("version")String version);



    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/wechat/createWxOrderAppPay")
    Observable<WxPays> createWxOrderAppPay(@Field("appKey")String appKey, @Field("officeCode")String officeCode,
                                           @Field("merchantCode")String merchantCode, @Field("version")String version,
                                           @Field("money")String money);
}
