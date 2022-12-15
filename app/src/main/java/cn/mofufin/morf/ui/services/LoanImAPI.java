package cn.mofufin.morf.ui.services;

import java.io.File;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.ExchangeHistory;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.entity.LoanBanner;
import cn.mofufin.morf.ui.entity.LoanNotify;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.LoanRepayCord;
import cn.mofufin.morf.ui.entity.LoansControlInfo;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.RecordDetails;
import cn.mofufin.morf.ui.entity.RefundPlan;
import cn.mofufin.morf.ui.entity.SelfImgInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import rx.Observable;

public class LoanImAPI {

    /**
     * 查询借贷轮播图
     * @param appKey
     * @param version
     * @return
     */
    public static Observable<LoanBanner> queryLoansImg(@NonNull String appKey, @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoanBanner> observable = loanAPI.queryLoansImg(appKey,version);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 查询借贷通知
     * @param appKey
     * @param type 查询类型:0:查询一条,1:查询所有
     * @param version
     * @return
     */
    public static Observable<LoanNotify> queryLoansInform(@NonNull String appKey, @NonNull String type, @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoanNotify> observable = loanAPI.queryLoansInform(appKey,type,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询借贷产品
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<LoanProduct> queryLoansProduct(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoanProduct> observable = loanAPI.queryLoansProduct(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询商户提交资料状态
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<LoansControlInfo> querySelfSubmitState(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoansControlInfo> observable = loanAPI.querySelfSubmitState(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 提交申请贷款
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param lpCode
     * @param loansMoney
     * @param loansPeriod
     * @param loansInterestWay
     * @return
     */
    public static Observable<BaseResult> sumitApplyLoans(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode ,
                                                          @NonNull String version,@NonNull String lpCode,@NonNull String loansMoney,
                                                          @NonNull String loansPeriod,@NonNull String loansInterestWay,@NonNull String addressBook){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<BaseResult> observable = loanAPI.sumitApplyLoans(appKey,officeCode,merchantCode,version,lpCode,loansMoney,loansPeriod,loansInterestWay,addressBook);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 展示还款计划
     * @param appKey
     * @param version
     * @param period
     * @param way
     * @param money
     * @param dayRete
     * @return
     */
    public static Observable<RefundPlan> showRefundPlan(@NonNull String appKey , @NonNull String version, @NonNull Integer period,
                                                        @NonNull Integer way, @NonNull Integer money, @NonNull Double dayRate){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<RefundPlan> observable = loanAPI.showRefundPlan(appKey,version,period,way,money,dayRate);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询本人文本资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfTextInfo> queryMerSelfText(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfTextInfo> observable = loanAPI.queryMerSelfText(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 上传本人文本资料
     */
    public static Observable<BaseResult> uploadSelfText(@NonNull String appKey, @NonNull String officeCode,
                                                        @NonNull String merchantCode, @NonNull String version,
                                                        @NonNull String debitCardCode, @NonNull String mtCityCd,
                                                        @NonNull String mtCityCdName, @NonNull String mtStateCd,
                                                        @NonNull String mtStateCdName, @NonNull String dtIssue,
                                                        @NonNull String dtExpiry, @NonNull String isLongEffec,
                                                        @NonNull String dtRegistered, @NonNull String mtGenderCd,
                                                        @NonNull String mtMaritalStsCd, @NonNull String mtEduLvlCd,
                                                        @NonNull String mtResidenceStsCd, @NonNull String mtIndvMobileUsageStsCd,
                                                        @NonNull String email, @NonNull String qq, @NonNull String weChat,
                                                        @NonNull String yearIncAmt, @NonNull String isFamily, @NonNull String hasCar,
                                                        @NonNull String plateNo, @NonNull String hasCreditCard, @NonNull String creditCardLines,
                                                        @NonNull String isBizEntit, @Nullable String employerNm, @Nullable String mtJobSectorCd,
                                                        @Nullable String mtJobSectorCdName, @Nullable String mtPosHeldCd, @Nullable String mtPosHeldCdName,@Nullable String employerPhone,
                                                        @Nullable String isComb, @Nullable String bizRegNo, @Nullable String bizAddr,@Nullable String isBussLongEffec,
                                                        @Nullable String bizRegDtExpiry, @Nullable String isLegalRep,
                                                        @Nullable String currentTotal, @Nullable String yearSaleMarginsRate, @Nullable String Ratal){

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<BaseResult> observable = loanAPI.uploadSelfText(appKey,officeCode,merchantCode,version,debitCardCode,mtCityCd,mtCityCdName,mtStateCd,mtStateCdName,
                dtIssue,dtExpiry,isLongEffec,dtRegistered,mtGenderCd,mtMaritalStsCd,mtEduLvlCd,mtResidenceStsCd,mtIndvMobileUsageStsCd,email,
                qq,weChat,yearIncAmt,isFamily,hasCar,plateNo,hasCreditCard,creditCardLines,isBizEntit,employerNm,mtJobSectorCd,mtJobSectorCdName,mtPosHeldCd,mtPosHeldCdName,employerPhone,
                isComb,bizRegNo,bizAddr,isBussLongEffec,bizRegDtExpiry,isLegalRep,currentTotal,yearSaleMarginsRate,Ratal);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 上传本人图片信息
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResult> uploadSelfImg(@NonNull Map<String,String> params, Map<String,File> fileMap){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params!=null && params.size() >0){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        Logger.e("identityScanPay=="+fileMap.size());
        if (fileMap!=null && fileMap.size() >0){
            for (Map.Entry<String,File> entry:fileMap.entrySet()){
                builder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName(),RequestBody.create(null,entry.getValue()));
            }
        }
        requestBody = builder.build();

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        return loanAPI.uploadSelfImg(requestBody);
    }


    /**
     * 查询本人图片资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfImgInfo> queryMerSelfImg(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfImgInfo> observable = loanAPI.queryMerSelfImg(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }



    /**
     * 上传配偶文本资料
     */
    public static Observable<BaseResult> uploadSpouseText(@NonNull String appKey, @NonNull String officeCode,
                                                        @NonNull String merchantCode, @NonNull String version,
                                                        @NonNull String nm, @NonNull String idNo,@NonNull String mtCityCd,
                                                        @NonNull String mtCityCdName, @NonNull String mtStateCd,
                                                        @NonNull String mtStateCdName, @NonNull String dtIssue,
                                                        @NonNull String dtExpiry, @NonNull String isLongEffec,
                                                        @NonNull String dtRegistered, @NonNull String mtGenderCd,
                                                        @NonNull String mtMaritalStsCd, @NonNull String mtEduLvlCd,
                                                        @NonNull String mtResidenceStsCd, @NonNull String mobileNo,@NonNull String mtIndvMobileUsageStsCd,
                                                        @NonNull String email, @NonNull String qq, @NonNull String weChat,
                                                        @NonNull String yearIncAmt, @NonNull String isFamily, @NonNull String hasCar,
                                                        @NonNull String plateNo, @NonNull String hasCreditCard, @NonNull String creditCardLines){

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<BaseResult> observable = loanAPI.uploadSpouseText(appKey,officeCode,merchantCode,version,nm,idNo,mtCityCd,mtCityCdName,mtStateCd,mtStateCdName,
                dtIssue,dtExpiry,isLongEffec,dtRegistered,mtGenderCd,mtMaritalStsCd,mtEduLvlCd,mtResidenceStsCd,mobileNo,mtIndvMobileUsageStsCd,email,
                qq,weChat,yearIncAmt,isFamily,hasCar,plateNo,hasCreditCard,creditCardLines);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询配偶文本资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfTextInfo> queryMerSpouseText(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfTextInfo> observable = loanAPI.queryMerSpouseText(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 上传亲朋文本资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param mtCifRelCd
     * @param mtCifRelCdName
     * @param nm
     * @param idNo
     * @param mtGenderCd
     * @param mobileNo
     * @param mtIncSourceCd
     * @param mtIncSourceCdName
     * @param yearIncAmt
     * @return
     */
    public static Observable<BaseResult> uploadFriend(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version,
                                                              @NonNull String mtCifRelCd, @NonNull String mtCifRelCdName, @NonNull String nm,
                                                        @NonNull String idNo, @NonNull String mtGenderCd, @NonNull String mobileNo,
                                                        @NonNull String mtIncSourceCd, @NonNull String mtIncSourceCdName, @NonNull String yearIncAmt){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<BaseResult> observable = loanAPI.uploadFriend(appKey,officeCode,merchantCode,version,mtCifRelCd,mtCifRelCdName,nm,idNo,
                                                                mtGenderCd,mobileNo,mtIncSourceCd,mtIncSourceCdName,yearIncAmt);
        return observable.compose(new MofuResultResponseTransformer());
    }




    /**
     * 查询配偶文本资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfTextInfo> queryMerFriendText(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfTextInfo> observable = loanAPI.queryMerFriendText(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }




    /**
     * 上传本人配偶图片资料
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResult> uploadSpouseImg(@NonNull Map<String,String> params, Map<String,File> fileMap){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params!=null && params.size() >0){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        Logger.e("identityScanPay=="+fileMap.size());
        if (fileMap!=null && fileMap.size() >0){
            for (Map.Entry<String,File> entry:fileMap.entrySet()){
                builder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName(),RequestBody.create(null,entry.getValue()));
            }
        }
        requestBody = builder.build();

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        return loanAPI.uploadSpouseImg(requestBody);
    }


    /**
     * 查询本人配偶图片资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfImgInfo> queryMerSpouseImg(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfImgInfo> observable = loanAPI.queryMerSpouseImg(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }



    /**
     * 上传本人私营业主资料
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResult> uploadSelfPrivateImg(@NonNull Map<String,String> params, Map<String,File> fileMap){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params!=null && params.size() >0){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        Logger.e("identityScanPay=="+fileMap.size());
        if (fileMap!=null && fileMap.size() >0){
            for (Map.Entry<String,File> entry:fileMap.entrySet()){
                builder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName(),RequestBody.create(null,entry.getValue()));
            }
        }
        requestBody = builder.build();

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        return loanAPI.uploadSelfPrivateImg(requestBody);
    }



    /**
     * 查询本人配偶图片资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfImgInfo> queryMerSelfPrivateImg(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfImgInfo> observable = loanAPI.queryMerSelfPrivateImg(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询业务资料图片
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<SelfImgInfo> queryBusinessImg(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<SelfImgInfo> observable = loanAPI.queryBusinessImg(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }



    /**
     * 上传本人私营业主资料
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResult> uploadBusinessImg(@NonNull Map<String,String> params, Map<String,File> fileMap){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params!=null && params.size() >0){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        Logger.e("identityScanPay=="+fileMap.size());
        if (fileMap!=null && fileMap.size() >0){
            for (Map.Entry<String,File> entry:fileMap.entrySet()){
                builder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName(),RequestBody.create(null,entry.getValue()));
            }
        }
        requestBody = builder.build();

        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        return loanAPI.uploadBusinessImg(requestBody);
    }



    /**
     * 查询申请贷款记录
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<LoanApplyRecord> queryLoansApplyRecord(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoanApplyRecord> observable = loanAPI.queryLoansApplyRecord(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }



    /**
     * 查询本人文本资料
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<LoanRepayCord> queryRefundPlan(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<LoanRepayCord> observable = loanAPI.queryRefundPlan(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }




    /**
     * 查询还款计划详情
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<RecordDetails> queryRefundPlanDetails(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version, @NonNull String refundPlanCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<RecordDetails> observable = loanAPI.queryRefundPlanDetails(appKey,officeCode,merchantCode,version,refundPlanCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询用户借贷协议
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<BaseResult> queryUserLoansProtocol(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode , @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        LoanAPI loanAPI = retrofit.create(LoanAPI.class);
        Observable<BaseResult> observable = loanAPI.queryUserLoansProtocol(appKey,officeCode,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
