package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.SingleBankType;
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
public class BankImpAPI {

    /**
     * 获取银行卡
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @return
     */
    public static Observable<BaseResponse<User.DataBean>> bankInfo(@NonNull String officeCode,
                                                                   @NonNull String appKey,
                                                                   @NonNull String merchantPhone){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable observable = bankAPI.bankInfo(officeCode,appKey,merchantPhone);
        return observable.compose(new FullResponseTransformer());
    }

    /**
     * 添加银行卡
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param cardholder
     * @param debitCard
     * @param bankName
     * @param cardType
     * @return
     */
    public static Observable<BaseResponse<User.DataBean>> addbankInfo(@NonNull String officeCode,
                                                                      @NonNull String appKey,
                                                                      @NonNull String merchantPhone,
                                                                      @NonNull String cardholder,
                                                                      @NonNull String debitCard,
                                                                      @NonNull String bankName,
                                                                      @NonNull String cardType,
                                                                      @NonNull String mobile){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable observable = bankAPI.addbankInfo(officeCode,appKey,merchantPhone,cardholder,debitCard,bankName,cardType,mobile);
        return observable.compose(new FullResponseTransformer());
    }

    public static Observable<BaseResponse<GeneralResponse.DataBean>> bankCardType(@NonNull String officeCode,
                                                                                  @NonNull String appKey,
                                                                                  @NonNull String merchantPhone,
                                                                                  @NonNull String bankCardNo){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = bankAPI.bankCardType(officeCode,appKey,merchantPhone,bankCardNo);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }


    /**
     * 添加信用卡
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param cardholder 持卡人姓名
     * @param creditCard 信用卡
     * @param bankName 银行名称
     * @param cardType 银行卡类型
     * @param accountDay 账单日
     * @param repaymentDay 还款日
     * @param cardReDate 信用卡到期时间
     * @param cardBackNo 背面
     * @param phoneNumber 预留手机号码
     * @return
     */
    public static Observable<BaseResponse<User.DataBean>> addCreditbankInfo(@NonNull String officeCode,
                                                                      @NonNull String appKey,
                                                                      @NonNull String merchantPhone,
                                                                      @NonNull String cardholder,
                                                                      @NonNull String creditCard,
                                                                      @NonNull String bankName,
                                                                      @NonNull String cardType,
                                                                            @NonNull String accountDay,
                                                                            @NonNull String repaymentDay,
                                                                            @NonNull String cardReDate,
                                                                            @NonNull String cardBackNo,
                                                                            @NonNull String phoneNumber){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable observable = bankAPI.addCreditbankInfo(officeCode,appKey,merchantPhone,cardholder,creditCard,bankName,cardType,
                accountDay,repaymentDay,cardReDate,cardBackNo,phoneNumber);
        return observable.compose(new FullResponseTransformer());
    }


    /**
     * 单独获取银行卡类别
     * @param officeCode
     * @param appKey
     * @param bankCard
     * @return
     */
    public static Observable<BaseResponse<SingleBankType.DataBean>> singleBankCardType(@NonNull String officeCode,
                                                                                       @NonNull String appKey,
                                                                                       @NonNull String bankCard){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable<BaseResponse<SingleBankType.DataBean>> observable = bankAPI.singleBankCardType(officeCode,appKey,bankCard);
        return observable.compose(new FullResponseTransformer<SingleBankType.DataBean>());
    }


    /**
     * 解绑银行卡
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param payPassword
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> UnbindbankCard(@NonNull String officeCode,
                                                                                  @NonNull String appKey,
                                                                                  @NonNull String merchantPhone,
                                                                                  @NonNull String payPassword,
                                                                                    @NonNull String creditCard){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = bankAPI.UnbindbankCard(
                officeCode,appKey,merchantPhone,payPassword,creditCard);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }


    /**
     * Mpos变更结算卡
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @param payPassword
     * @param cardCode
     * @return
     */
    public static Observable<BaseResult> mposUpdateCard(@NonNull String officeCode,
                                                                                    @NonNull String appKey,
                                                                                    @NonNull String merchantCode,
                                                                                    @NonNull String payPassword,
                                                                                    @NonNull String cardCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        BankAPI bankAPI = retrofit.create(BankAPI.class);
        Observable<BaseResult> observable = bankAPI.mposUpdateCard(
                officeCode,appKey,merchantCode,payPassword,cardCode);
        return observable.compose(new MofuResultResponseTransformer());
    }
}
