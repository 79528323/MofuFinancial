package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.Address;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.CreditApply;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.Member;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.Tree;
import cn.mofufin.morf.ui.entity.WxPays;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class UtilsImpAPI {

    /**
     * 获取版本更新
     * @param officeCode
     * @return
     */
    public static Observable<MofuResult> QuerySystemAPK(@NonNull String officeCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.QuerySystemAPK(officeCode);
        return observable.compose(new MofuResultResponseTransformer<MofuResult>());
    }

    /**
     * 首页通知内容
     * @param officeCode
     * @return
     */
    public static Observable<BaseResult> HomePageContent(@NonNull String officeCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.HomePageContent(officeCode);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }

    /**
     * 查询会员日
     * @param appKey
     * @return
     */
    public static Observable<Member> treeMemberQuery(@NonNull String appKey){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.treeMemberQuery(appKey);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 脚踹摇钱树
     * @param appKey
     * @param merchantCode
     * @return
     */
    public static Observable<Tree> hit(@NonNull String appKey,@NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.hit(appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 创建Alipay订单
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param money
     * @return
     */
    public static Observable<BaseResult> createAliOrder(@NonNull String appKey,@NonNull String officeCode,@NonNull String merchantCode,
                                                        @NonNull String money){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.createAliOrder(appKey,officeCode,merchantCode,money);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }


    /**
     * 获取（信用卡申请）银行通道
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<CreditApply> getCreditForApp(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.getCreditForApp(appKey,officeCode,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 支付宝扫码充值余额
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param money
     * @return
     */
    public static Observable<BaseResult> createAliOrderH5(@NonNull String appKey,@NonNull String officeCode,@NonNull String merchantCode,
                                                        @NonNull String money){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable observable = utilsAPI.createAliOrderH5(appKey,officeCode,merchantCode,money);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }


    /**
     * 轮播图
     * @param appKey
     * @param officeCode
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> getRotationChart(@NonNull String appKey, @NonNull String officeCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = utilsAPI.getRotationChart(appKey,officeCode);
        return observable.compose(new MofuResultResponseTransformer());
    }



    public static Observable<BaseResponse<Address.DataBean>> sendPos(@NonNull String appKey, @NonNull String officeCode,
                                                                     @NonNull String operType, @Nullable Integer addressNumber,
                                                                     @Nullable String takeAddress, @Nullable String takePersonName,
                                                                     @NonNull String merchantCode, @Nullable String takePersonPhone){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable<BaseResponse<Address.DataBean>> observable = utilsAPI.sendPos(appKey,officeCode, merchantCode,operType,addressNumber,takeAddress,takePersonName,takePersonPhone);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 查询代付时间
     * @param appKey
     * @param officeCode
     * @return
     */
    public static Observable<MofuResult> queryWithdrawDate(@NonNull String appKey, @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable<MofuResult> observable = utilsAPI.queryWithdrawDate(appKey,version);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 微信app支付
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param version
     * @param money
     * @return
     */
    public static Observable<WxPays> createWxOrderAppPay(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode
            , @NonNull String version, @NonNull String money){
        Retrofit retrofit = RetrofitUtils.getInstance();
        UtilsAPI utilsAPI = retrofit.create(UtilsAPI.class);
        Observable<WxPays> observable = utilsAPI.createWxOrderAppPay(appKey,officeCode,merchantCode,version,money);
        return observable.compose(new MofuResultResponseTransformer());
    }

}
