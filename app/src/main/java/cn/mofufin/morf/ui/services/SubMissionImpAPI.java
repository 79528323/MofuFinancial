package cn.mofufin.morf.ui.services;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Map;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.FullResponseTransformer;
import cn.mofufin.morf.ui.util.MofuResultResponseTransformer;
import cn.mofufin.morf.ui.util.RetrofitUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * author：created by Liubd on 2018/8/30 09:31
 * e-mail:79528323@qq.com
 */
public class SubMissionImpAPI {


    /**
     * 扫友支付进件
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResult> identityScanPay(@NonNull Map<String,String> params, Map<String,File> fileMap){
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
        SubMissionAPI subMissionAPI = retrofit.create(SubMissionAPI.class);
        return subMissionAPI.identityScanPay(requestBody);
    }


    /**
     * 认证上传接口
     * @param params
     * @param fileMap
     * @return
     */
    public static Observable<BaseResponse<GeneralResponse.DataBean>> identityAuthen(@NonNull Map<String,String> params, Map<String,File> fileMap){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params!=null && params.size() >0){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }

        if (fileMap!=null && fileMap.size() >0){
            for (Map.Entry<String,File> entry:fileMap.entrySet()){
                builder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName(),RequestBody.create(null,entry.getValue()));
            }
        }
        requestBody = builder.build();

        Retrofit retrofit = RetrofitUtils.getInstance();
        SubMissionAPI subMissionAPI = retrofit.create(SubMissionAPI.class);
        return subMissionAPI.identityAuthen(requestBody);
    }


    /**
     * 申请开通MPos
     * @param officeCode
     * @param appKey
     * @param merchantCode
     * @return
     */
    public static Observable<MofuResult> connectionMPos(@NonNull String officeCode, @NonNull String appKey,
                                                                      @NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        SubMissionAPI subMissionAPI = retrofit.create(SubMissionAPI.class);
        Observable observable = subMissionAPI.connectionMPos(officeCode,appKey,merchantCode);
        return observable.compose(new MofuResultResponseTransformer());
    }


    /**
     * 刷新用户信息
     * @param officeCode
     * @param appKey
     * @param merchantPhone
     * @param merchantCode
     * @return
     */
    public static Observable<BaseResponse<User.DataBean>> refreshMerchantInfo(@NonNull String officeCode, @NonNull String appKey,
                                                                              @NonNull String merchantPhone,@NonNull String merchantCode){
        Retrofit retrofit = RetrofitUtils.getInstance();
        SubMissionAPI subMissionAPI = retrofit.create(SubMissionAPI.class);
        Observable<BaseResponse<User.DataBean>> observable = subMissionAPI.refreshMerchantInfo(officeCode,appKey,merchantPhone,merchantCode);
        return observable.compose(new FullResponseTransformer<User.DataBean>());
    }

    public static Observable<BaseResponse<GeneralResponse.DataBean>> acquireBankCategories(@NonNull String officeCode, @NonNull String appKey,
                                                                                           @NonNull String merchantPhone,@NonNull String bankCardNo){
        Retrofit retrofit = RetrofitUtils.getInstance();
        SubMissionAPI subMissionAPI = retrofit.create(SubMissionAPI.class);
        Observable<BaseResponse<GeneralResponse.DataBean>> observable = subMissionAPI.acquireBankCategories(officeCode,appKey,merchantPhone,bankCardNo);
        return observable.compose(new FullResponseTransformer<GeneralResponse.DataBean>());
    }
}
