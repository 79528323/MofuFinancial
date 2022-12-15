package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface QueryChannelAPI {

    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/shortcut/queryChannel")
    Observable<Channel> querychannelList(@Field("officeCode") String officeCode,
                                               @Field("appKey") String appKey,
                                               @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/shortcut/queryChannelSupportBankInfo")
    Observable<Channel> querychannelSupportBankList(@Field("officeCode") String officeCode,
                                         @Field("appKey") String appKey,
                                         @Field("tcType") int tcType);

    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/shortcut/initChannnel")
    Observable<BaseResult> querychannelH5(@Field("officeCode") String officeCode,
                                          @Field("appKey") String appKey,
                                          @Field("tcType") int tcType, @Field("merchantCode")String merchantCode);


    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/over/proceedPay")
    Observable<BaseResult> proceedPay(@Field("officeCode") String officeCode,
                                          @Field("appKey") String appKey,
                                          @Field("money") String money, @Field("merchantCode")String merchantCode,
                                      @Field("channelNumber") String channelNumber,@Field("ovPayWay") String ovPayWay,
                                      @Field("mcbGoodsNumber") String mcbGoodsNumber);


    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/over/proceedPayNew")
    Observable<BaseResult> proceedPayNew(@Field("officeCode") String officeCode,
                                      @Field("appKey") String appKey,
                                      @Field("payMoney") String payMoney, @Field("merchantCode")String merchantCode,
                                      @Field("channelNumber") String channelNumber,
                                      @Field("mcbGoodsNumber") String mcbGoodsNumber,@Field("version") String version);



    @FormUrlEncoded
    @POST(Common.HOS_SYSTEM_TYPE+"/over/queryOverChannelInfo")
    Observable<Overseans> queryOverChannelInfo(@Field("appKey") String appKey,@Field("officeCode") String officeCode
            ,@Field("merchantCode")String merchantCode);
}
