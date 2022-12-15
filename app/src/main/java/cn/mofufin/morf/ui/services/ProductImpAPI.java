package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;

import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.IntergalRank;
import cn.mofufin.morf.ui.entity.InverstMemtRecord;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class ProductImpAPI {


    /**
     * 初始化商户理财信息
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @return
     */
    public static Observable<ProductInfo> initMerProductInfo(@NonNull String officeCode,
                                                   @NonNull String appKey,
                                                   @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.initMerProductInfo(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 查询所有理财产品
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @return
     */
    public static Observable<ProductInfo> queryAllFundProduct(@NonNull String officeCode,
                                                             @NonNull String appKey,
                                                             @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.queryAllFundProduct(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询单个理财产品
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param fdNumber
     * @return
     */
    public static Observable<ProductDetails> querySingleProductDetails(@NonNull String officeCode,
                                                                       @NonNull String appKey,
                                                                       @NonNull String merchantCode,
                                                                       @NonNull String fdNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.querySingleProductDetails(officeCode,appKey,merchantCode,fdNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 商户购买理财
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param fdNumber
     * @param payPassword
     * @param money
     * @param mcbGoodsNumber
     * @return
     */
    public static Observable<BaseResult> merInvest(@NonNull String officeCode,
                                                   @NonNull String appKey,
                                                   @NonNull String merchantCode,
                                                   @NonNull String fdNumber, @NonNull String payPassword,
                                                   @NonNull String money, @NonNull String mcbGoodsNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.merInvest(officeCode,appKey,merchantCode,fdNumber,payPassword,money,mcbGoodsNumber);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询商户投资记录
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param orderBegin
     * @param orderEnd
     * @return
     */
    public static Observable<InverstMemtRecord> queryMerInvestRecode(@NonNull String officeCode,
                                                                     @NonNull String appKey,
                                                                     @NonNull String merchantCode,
                                                                     @NonNull String orderBegin, @NonNull String orderEnd){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.queryMerInvestRecode(officeCode,appKey,merchantCode,orderBegin,orderEnd);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询理财通知信息
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param queryType
     * @return
     */
    public static Observable<ProductNotice> queryFundInform(@NonNull String officeCode,
                                                            @NonNull String appKey,
                                                            @NonNull String merchantCode,
                                                            @NonNull String queryType){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable observable = productAPI.queryFundInform(officeCode,appKey,merchantCode,queryType);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 查询理财活动
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param version
     * @return
     */
    public static Observable<IntergalRank> queryFundActivity(@NonNull String officeCode,
                                                             @NonNull String appKey,
                                                             @NonNull String merchantCode,
                                                             @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        ProductAPI productAPI = retrofit.create(ProductAPI.class);
        Observable<IntergalRank> observable = productAPI.queryFundActivity(officeCode,appKey,merchantCode,version);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
