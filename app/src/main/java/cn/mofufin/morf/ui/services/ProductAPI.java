package cn.mofufin.morf.ui.services;


import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.IntergalRank;
import cn.mofufin.morf.ui.entity.InverstMemtRecord;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface ProductAPI {

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/initMerProductInfo")
    Observable<ProductInfo> initMerProductInfo(@Field("officeCode") String officeCode,
                                               @Field("appKey") String appKey,
                                               @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/queryAllFundProduct")
    Observable<ProductInfo> queryAllFundProduct(@Field("officeCode") String officeCode,
                                               @Field("appKey") String appKey,
                                               @Field("merchantCode") String merchantCode);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/querySingleProductDetails")
    Observable<ProductDetails> querySingleProductDetails(@Field("officeCode") String officeCode,
                                                   @Field("appKey") String appKey,
                                                   @Field("merchantCode") String merchantCode,
                                                         @Field("fdNumber") String fdNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/merInvest")
    Observable<BaseResult> merInvest(@Field("officeCode") String officeCode,
                                     @Field("appKey") String appKey,
                                     @Field("merchantCode") String merchantCode,
                                     @Field("fdNumber") String fdNumber,
                                     @Field("payPassword") String payPassword,
                                     @Field("money") String money,
                                     @Field("mcbGoodsNumber") String mcbGoodsNumber);

    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/queryMerInvestRecode")
    Observable<InverstMemtRecord> queryMerInvestRecode(@Field("officeCode") String officeCode,
                                                       @Field("appKey") String appKey,
                                                       @Field("merchantCode") String merchantCode,
                                                       @Field("orderBegin") String fdNumber,
                                                       @Field("orderEnd") String payPassword);


    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/queryFundInform")
    Observable<ProductNotice> queryFundInform(@Field("officeCode") String officeCode,
                                              @Field("appKey") String appKey,
                                              @Field("merchantCode") String merchantCode,
                                              @Field("queryType") String queryType);



    @FormUrlEncoded
    @POST(Common.HOSTTYPE+ "/fund/queryFundActivity")
    Observable<IntergalRank> queryFundActivity(@Field("officeCode") String officeCode,
                                               @Field("appKey") String appKey,
                                               @Field("merchantCode") String merchantCode,
                                               @Field("version") String version);



}
