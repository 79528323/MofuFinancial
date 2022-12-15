package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class RepayMentImpAPI {


    /**
     * 查询卡延期通道
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param version
     * @return
     */
    public static Observable<RepayChannel> queryChannel(@NonNull String officeCode,
                                                        @NonNull String appKey,
                                                        @NonNull String merchantCode,
                                                        @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<RepayChannel> observable = repayMentAPI.queryChannel(officeCode,appKey,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询通道落地省市
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<FallProvinceCity> queryChannelFallProvinceCity(
                                                        @NonNull String appKey,
                                                        @NonNull String rcNumber,
                                                        @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<FallProvinceCity> observable = repayMentAPI.queryChannelFallProvinceCity(appKey,rcNumber,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 生成还款计划
     * @param appKey
     * @param officeCode
     * @param version
     * @param merchantCode
     * @param rcNumber
     * @param creditCardCode
     * @param consumeMoney
     * @param refundDate
     * @param provinceCode
     * @param provinceName
     * @param cityCode
     * @param cityName
     * @return
     */
    public static Observable<ProjectResult> createRefundPlan(
            @NonNull String appKey,
            @NonNull String officeCode,
            @NonNull String version,
            @NonNull String merchantCode,
            @NonNull String rcNumber,
            @NonNull String creditCardCode,
            @NonNull String consumeMoney,
            @NonNull String refundDate,
            @Nullable String provinceCode,
            @Nullable String provinceName,
            @Nullable String cityCode,
            @Nullable String cityName){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<ProjectResult> observable = repayMentAPI.createRefundPlan(appKey,officeCode,merchantCode,version,
                rcNumber,creditCardCode,consumeMoney,refundDate,provinceCode,provinceName,cityCode,cityName);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 获取还款日
     * @param appKey
     * @param billDay
     * @param version
     * @return
     */
    public static Observable<RepayMentDay> getRefundDay(
            @NonNull String appKey,@NonNull String billDay,@NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<RepayMentDay> observable = repayMentAPI.getRefundDay(appKey,billDay,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 执行还款计划
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param rpOrderCode
     * @return
     */
    public static Observable<MofuResult> executeRefundPlan(
            @NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode,@NonNull String version,
            @NonNull String rpOrderCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.executeRefundPlan(appKey,officeCode,merchantCode,version,rpOrderCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询还款计划详情
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param rpOrderCode
     * @return
     */
    public static Observable<ProjectDetails> queryRefundPlanDetails(
            @NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode,@NonNull String version,
            @NonNull String rpOrderCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<ProjectDetails> observable = repayMentAPI.queryRefundPlanDetails(appKey,officeCode,merchantCode,version,rpOrderCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询还款计划
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Observable<RepayMentProject> queryRefundPlan(
            @NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode,@NonNull String version,
            @NonNull String beginDate,@NonNull String endDate){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<RepayMentProject> observable = repayMentAPI.queryRefundPlan(appKey,officeCode,merchantCode,version,beginDate,endDate);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询卡延期通道支持银行
     * @param officeCode
     * @param appKey
     * @param rcNumber
     * @param version
     * @return
     */
    public static Observable<SupportBank> queryChannelSupportBank(@NonNull String officeCode,
                                                                  @NonNull String appKey,
                                                                  @NonNull String rcNumber,
                                                                  @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<SupportBank> observable = repayMentAPI.queryChannelSupportBank(officeCode,appKey,rcNumber,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 取消还款计划
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param rpOrderCode
     * @return
     */
    public static Observable<MofuResult> cancelRefundPlan(@NonNull String officeCode,
                                                        @NonNull String appKey,
                                                        @NonNull String merchantCode,
                                                        @NonNull String version,@NonNull String rpOrderCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.cancelRefundPlan(officeCode,appKey,merchantCode,version,rpOrderCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 变更账单日还款日
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param creditCardCode
     * @param billDay
     * @return
     */
    public static Observable<MofuResult> updateCreditCardInfo(@NonNull String officeCode,
                                                          @NonNull String appKey,
                                                          @NonNull String merchantCode,
                                                          @NonNull String version,@NonNull String creditCardCode,
                                                              @NonNull Integer billDay,@NonNull Integer refundDay){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.updateCreditCardInfo(officeCode,appKey,merchantCode,version,creditCardCode,billDay,refundDay);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 初始化还款通道
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param creditCardCode
     * @param rcNumber
     * @return
     */
    public static Observable<MofuResult> initRefundChannel(@NonNull String officeCode,
                                                              @NonNull String appKey,
                                                              @NonNull String merchantCode,
                                                              @NonNull String version,
                                                            @NonNull String creditCardCode,
                                                              @NonNull String rcNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.initRefundChannel(officeCode,appKey,merchantCode,version,creditCardCode,rcNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 初始化还款通道绑卡短信发送
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param creditCardCode
     * @param rcNumber
     * @return
     */
    public static Observable<MofuResult> initRefundChannelBindCardSmsSend(@NonNull String officeCode,
                                                           @NonNull String appKey,
                                                           @NonNull String merchantCode,
                                                           @NonNull String version,
                                                           @NonNull String creditCardCode,
                                                           @NonNull String rcNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.initRefundChannelBindCardSmsSend(officeCode,appKey,merchantCode,version,creditCardCode,rcNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 初始化还款通道绑卡短信确认
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param creditCardCode
     * @param rcNumber
     * @return
     */
    public static Observable<MofuResult> initRefundChannelBindCardSmsAffirm(@NonNull String officeCode,
                                                                          @NonNull String appKey,
                                                                          @NonNull String merchantCode,
                                                                          @NonNull String version,
                                                                          @NonNull String creditCardCode,
                                                                          @NonNull String rcNumber,@NonNull String smsCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        RepayMentAPI repayMentAPI = retrofit.create(RepayMentAPI.class);
        Observable<MofuResult> observable = repayMentAPI.initRefundChannelBindCardSmsAffirm(officeCode,appKey,merchantCode,version,creditCardCode,rcNumber,smsCode);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
