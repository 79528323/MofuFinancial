package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class QueryChannelImpAPI {

    /**
     * 查询通道
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @return
     */
    public static Observable<Channel> querychannelList(@NonNull String officeCode, @NonNull String appKey,
                                                                     @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable<Channel> observable = queryChannelAPI.querychannelList(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }

    /**
     * 查询通道支持银行列表
     * @param officeCode
     * @param appKey
     * @param tcType
     * @return
     */
    public static Observable<Channel> querychannelSupportBankList(@NonNull String officeCode, @NonNull String appKey,
                                                       @NonNull int tcType){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable<Channel> observable = queryChannelAPI.querychannelSupportBankList(officeCode,appKey,tcType);
        return observable.compose(new MofuResultResponseTransformer());
    }

    public static Observable<BaseResult> querychannelH5(@NonNull String officeCode, @NonNull String appKey,
                                                                  @NonNull int tcType,@NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable observable = queryChannelAPI.querychannelH5(officeCode,appKey,tcType,merchantCode);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }

    /**
     * 境外交易接口
     * @param officeCode
     * @param appKey
     * @param money
     * @param merchantCode
     * @param channelNumber
     * @param ovPayWay
     * @return
     */
    public static Observable<BaseResult> proceedPay(@NonNull String officeCode, @NonNull String appKey,
                                                    @NonNull String money,@NonNull String merchantCode,
                                                    @NonNull String channelNumber,@NonNull String ovPayWay,@Nullable String mcbGoodsNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable observable = queryChannelAPI.proceedPay(officeCode,appKey,money,merchantCode,channelNumber,ovPayWay,mcbGoodsNumber);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }


    public static Observable<BaseResult> proceedPayNew(@NonNull String officeCode, @NonNull String appKey,
                                                        @NonNull String payMoney,@NonNull String merchantCode,
                                                        @NonNull String channelNumber,@Nullable String mcbGoodsNumber,
                                                        @NonNull String version){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable observable = queryChannelAPI.proceedPayNew(officeCode,appKey,payMoney,merchantCode,channelNumber,mcbGoodsNumber,version);
        return observable.compose(new MofuResultResponseTransformer<BaseResult>());
    }


    /**
     * 查询境外通道信息
     * @param appKey
     * @param officeCode
     * @param merchantCode
     * @return
     */
    public static Observable<Overseans> queryOverChannelInfo(@NonNull String appKey,@NonNull String officeCode,
                                                             @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        QueryChannelAPI queryChannelAPI = retrofit.create(QueryChannelAPI.class);
        Observable observable = queryChannelAPI.queryOverChannelInfo(appKey,officeCode,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
