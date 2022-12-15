package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class UserImpAPI {

    /**
     * 注册
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param smsCode
     * @param password
     * @param spreadPhone
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> register(@NonNull String officeCode, @NonNull String appKey,
                                                                              @NonNull String merchantPhone, @NonNull String smsCode,
                                                                              @NonNull String password, @NonNull String spreadPhone){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = userAPI.register(officeCode,appKey,merchantPhone,smsCode,password,spreadPhone);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }

    /**
     * 短信验证码
     * @param merchantPhone
     * @param appKey
     * @param officeCode
     * @param spreadPhone
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> getSMSCode(@NonNull String merchantPhone, @NonNull String appKey,
                                                                                @NonNull String officeCode, @Nullable String spreadPhone,
                                                                                @Nullable String newMerchantPhone){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = userAPI.getSMSCode(merchantPhone,appKey,officeCode,spreadPhone,newMerchantPhone);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }

    /**
     * 登录
     * @param appKey
     * @param officeCode
     * @param loginName
     * @param password
     * @param currentFacility
     * @return
     */
    public static Observable<BaseResponse<User.DataBean>> Logins(@NonNull String appKey,@NonNull String officeCode
            , @NonNull String loginName,@NonNull String password,@NonNull String currentFacility){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<User.DataBean>> observable = userAPI.Logins(appKey,officeCode,loginName,password,currentFacility);
        return observable.compose(new FullResponseTransformer<User.DataBean>());
    }

    /**
     * 修改登录密码
     * @param appKey
     * @param officeCode
     * @param merchantPhone
     * @param smsCode
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> resetLoginPassword(@NonNull String appKey,@NonNull String officeCode,
                                                                               @NonNull String merchantPhone,@NonNull String smsCode,
                                                                               @NonNull String newPassword,@NonNull String repeatPassword){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = userAPI.resetLoginPassword(officeCode,appKey,merchantPhone,smsCode,newPassword,repeatPassword);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }

    /**
     * 取直推商户
     * @param appKey
     * @param officeCode
     * @param merchantPhone
     * @return
     */
    public static Observable<PushMerchant> getPushMerchant(@NonNull String appKey,@NonNull String officeCode,
                                                                    @NonNull String merchantCode,@NonNull String version,@NonNull String queryType,
                                                                                  @NonNull String queryDate){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<PushMerchant> observable = userAPI.getPushmerchant(officeCode,appKey,merchantCode,version,queryType,queryDate);
        return observable.compose(new MofuResultResponseTransformer());
    }

    public static Observable<BaseResponse<GeneralResponse.DataBean>> resetTransPassword(@NonNull String appKey,@NonNull String officeCode,
                                                                                        @NonNull String merchantPhone,@NonNull String smsCode,
                                                                                        @NonNull String newPassword,@NonNull String repeatPassword,
                                                                                        @NonNull String IDName,@NonNull String IDNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = userAPI.resetTransPassword(officeCode,appKey,merchantPhone,smsCode,newPassword,repeatPassword,IDName,IDNumber);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }

    /**
     * 查询商户返佣信息
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<Ranking> queryMerRebate(@NonNull String appKey,@NonNull String officeCode,@NonNull String merchantCode,
                                                     @NonNull String version,@NonNull String date){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<Ranking> observable = userAPI.queryMerRebate(officeCode,appKey,merchantCode,version,date);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 查询商户返佣明细
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param dateBegin
     * @return
     */
    public static Observable<CommissionDetail> queryMerRebateDetail(@NonNull String appKey, @NonNull String officeCode,
                                                                    @NonNull String merchantCode,@NonNull String dateBegin){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<CommissionDetail> observable = userAPI.queryMerRebateDetail(officeCode,appKey,merchantCode,dateBegin);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 商户余额明细查询
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param orderBegin
     * @param orderEnd
     * @param cdWay
     * @return
     */
    public static Observable<BalanceDetail> queryBalanceDetail(@NonNull String appKey, @NonNull String officeCode,
                                                               @NonNull String merchantCode,@NonNull String orderBegin,
                                                               @NonNull String orderEnd, @NonNull int cdWay){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BalanceDetail> observable = userAPI.queryBalanceDetail(appKey,officeCode,merchantCode,orderBegin,orderEnd,cdWay);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 商户返佣转余额
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param money
     * @param password
     * @return
     */
    public static Observable<BaseResult> merRebateTrunBalance(@NonNull String appKey, @NonNull String officeCode,
                                                              @NonNull String merchantCode, @NonNull String money,
                                                              @NonNull String password){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResult> observable = userAPI.merRebateTrunBalance(appKey,officeCode,merchantCode,money,password);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询商户摩币信息
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<Coin> queryMerMobi(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode,
                                                @NonNull String version,@NonNull String date){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<Coin> observable = userAPI.queryMerMobi(officeCode,appKey,merchantCode,version,date);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询商户摩币明细
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param dateBegin
     * @return
     */
    public static Observable<CoinDetail> queryMerMoBiDetail(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode,@NonNull String dateBegin){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<CoinDetail> observable = userAPI.queryMerMoBiDetail(officeCode,appKey,merchantCode,dateBegin);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 获取系统摩币设置信息
     * @param appKey
     * @return
     */
    public static Observable<CoinInfo> getMoBiSetInfo(@NonNull String appKey){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<CoinInfo> observable = userAPI.getMoBiSetInfo(appKey);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 美元汇率
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<ExChanged> getUSDBuyCash(@NonNull String appKey,
                                                      @NonNull String officeCode, @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<ExChanged> observable = userAPI.getUSDBuyCash(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 港元汇率
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<ExChanged> getHKDBuyCash(@NonNull String appKey,
                                                      @NonNull String officeCode, @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<ExChanged> observable = userAPI.getHKDBuyCash(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 港币转账到余额
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param HKD
     * @return
     */
    public static Observable<ExChanged> getHKDToBalance(@NonNull String appKey,
                                                      @NonNull String officeCode, @NonNull String merchantCode,@NonNull String HKD){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<ExChanged> observable = userAPI.getHKDToBalance(officeCode,appKey,merchantCode,HKD);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 美元转账到余额
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param USD
     * @return
     */
    public static Observable<ExChanged> getUSDToBalance(@NonNull String appKey,
                                                        @NonNull String officeCode, @NonNull String merchantCode,@NonNull String USD){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<ExChanged> observable = userAPI.getUSDToBalance(officeCode,appKey,merchantCode,USD);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 商户余额提现
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param money
     * @param password
     * @param bankCode
     * @return
     */
    public static Observable<BaseResult> merBalanceWithd(@NonNull String appKey, @NonNull String officeCode,
                                                              @NonNull String merchantCode, @NonNull String money,
                                                              @NonNull String password,@NonNull String bankCode,@Nullable String mcbGoodsNumber){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResult> observable = userAPI.merBalanceWithd(appKey,officeCode,merchantCode,money,password,bankCode,mcbGoodsNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 账号修改
     * @param appKey
     * @param officeCode
     * @param oldMerchantPhone
     * @param newMerchantPhone
     * @param idCardNo
     * @param smsCode
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse>> updateAccountName(@NonNull String appKey, @NonNull String officeCode,
                                                         @NonNull String oldMerchantPhone, @NonNull String newMerchantPhone,
                                                         @NonNull String idCardNo,@NonNull String smsCode){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<BaseResponse<GeneralResponse>> observable = userAPI.updateAccountName(appKey,officeCode,oldMerchantPhone,newMerchantPhone,idCardNo,smsCode);
        return observable.compose(new FullResponseTransformer());
    }


    /**
     * Mpos商户查询
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param longitude
     * @param latitude
     * @param queryType
     * @param payMoney
     * @return
     */
    public static Observable<IndustryInfos> mPosMercQuery(@NonNull String officeCode, @NonNull String appKey,
                                                              @NonNull String merchantCode, @NonNull String version,
                                                              @NonNull String longitude, @NonNull String latitude,
                                                          @NonNull String queryType,@NonNull String payMoney){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<IndustryInfos> observable = userAPI.mPosMercQuery(officeCode,appKey,merchantCode,version,longitude,latitude,queryType,payMoney);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * Mpos商户费率查询
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @param queryType
     * @return
     */
    public static Observable<MposRatio> queryMerMposRatio(@NonNull String officeCode, @NonNull String appKey,
                                                      @NonNull String merchantCode, @NonNull String version,
                                                      @NonNull String queryType){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<MposRatio> observable = userAPI.queryMerMposRatio(officeCode,appKey,merchantCode,version,queryType);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 控制MPOS新老版本
     * @param officeCode
     * @param appKey
     * @param operType
     * @param version
     * @param appVersion
     * @param appState
     * @return
     */
    public static Observable<Audit> iosAudit(@NonNull String officeCode, @NonNull String appKey,
                                             @NonNull String operType, @NonNull String version,
                                             @Nullable String appVersion, @Nullable String appState){

        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<Audit> observable = userAPI.iosAudit(officeCode,appKey,operType,version,appVersion,appState);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询MPOS刷卡位置城市
     * @param appKey
     * @param version
     * @return
     */
    public static Observable<MposArea> queryMposArea(@NonNull String appKey, @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UserAPI userAPI = retrofit.create(UserAPI.class);
        Observable<MposArea> observable = userAPI.queryMposArea(appKey,version);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
