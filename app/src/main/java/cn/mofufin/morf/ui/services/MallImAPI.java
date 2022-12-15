package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.ui.entity.ExchangeHistory;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

public class MallImAPI {

    /**
     * 商品发行
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<BaseResponse<Coupons.DataBean>> getMerchandise(@NonNull String appKey, @NonNull String officeCode, @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        MallAPI mallAPI = retrofit.create(MallAPI.class);
        Observable<BaseResponse<Coupons.DataBean>> observable = mallAPI.getMerchandise(appKey,officeCode,merchantCode);
        return observable.compose(new FullResponseTransformer<Coupons.DataBean>());
    }

    /**
     * 兑换商品
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param goodsCode
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> exchange(@NonNull String appKey, @NonNull String officeCode,
                                                                              @NonNull String merchantCode, @NonNull String goodsCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        MallAPI mallAPI = retrofit.create(MallAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = mallAPI.exchange(appKey,officeCode,merchantCode,goodsCode);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }


    /**
     * 商户商品背包
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param type
     * @return
     */
    public static Observable<BaseResponse<MerchantBag.DataBean>> getMerchantBag(@NonNull String appKey, @NonNull String officeCode,
                                                                                @NonNull String merchantCode, @NonNull String type){
        Retrofit retrofit = RetrofitUtils.getInstance();
        MallAPI mallAPI = retrofit.create(MallAPI.class);
        Observable<BaseResponse<MerchantBag.DataBean>> observable = mallAPI.getMerchantBag(appKey,officeCode,merchantCode,type);
        return observable.compose(new FullResponseTransformer<MerchantBag.DataBean>());
    }


    /**
     * 商品兑换记录
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<ExchangeHistory> getRecord(@NonNull String appKey, @NonNull String officeCode,
                                                        @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        MallAPI mallAPI = retrofit.create(MallAPI.class);
        Observable<ExchangeHistory> observable = mallAPI.getRecord(appKey,officeCode,merchantCode);
        return observable.compose(new MofuResultResponseTransformer<ExchangeHistory>());
    }


    /**
     * 会员券升级接口
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @param mcbGoodsNumber
     * @return
     */
    public static Observable<BaseResult> memberTicketGrade(@NonNull String appKey, @NonNull String officeCode,
                                                                           @NonNull String merchantCode,@NonNull String mcbGoodsNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        MallAPI mallAPI = retrofit.create(MallAPI.class);
        Observable<BaseResult> observable = mallAPI.memberTicketGrade(appKey,officeCode,merchantCode,mcbGoodsNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
