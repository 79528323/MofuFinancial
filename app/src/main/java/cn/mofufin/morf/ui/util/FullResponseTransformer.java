package cn.mofufin.morf.ui.util;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.base.BaseResponse;
import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;

/**
 * Created by yhz on 2016/5/20.
 * 返回值转换
 */
public class FullResponseTransformer<T> implements Transformer<BaseResponse<T>, BaseResponse<T>> {
    @Override
    public Observable<BaseResponse<T>> call(Observable<BaseResponse<T>> responseObservable) {
        return responseObservable.flatMap(new Func1<BaseResponse<T>, Observable<BaseResponse<T>>>() {
            @Override
            public Observable<BaseResponse<T>> call(BaseResponse<T> response) {
                Logger.e("FullResponseTransformer = "+response.toString());
                return Observable.just(response);
//                if (response == null || response.stateCode == BaseResponse.STATUS_TIME_OUT) {
//                    Logger.e("response = null");
//                    return Observable.error(new Throwable(BaseApplication.context.getString(R.string.default_http_error)));
//                } else
//                if (response.stateCode == BaseResponse.STATUS_SUCCESS) {
//                    //请求成功
//                    return Observable.just(response);
//                }
////                else if (response.stateCode == BaseResponse.STATUS_LOGIN_TIMEOUT) {
////                    //在其他设备登录
//////                    DataUtils.showTimeOutDialog();
////                    return Observable.empty();
////                }
//                else {//请求失败
////                    if (response.code == 260 || response.code == 259 || response.code == 410)
////                        return Observable.just(response);
//                    Logger.e("response != null");
//                    String message ="";
//                    if (response!=null){
////                        response.data
//                    }
//                    return Observable.error(new Throwable(response.message));
//                }
            }
        });
    }
}
