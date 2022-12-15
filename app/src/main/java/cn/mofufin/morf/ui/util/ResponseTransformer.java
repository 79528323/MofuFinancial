package cn.mofufin.morf.ui.util;

import cn.mofufin.morf.ui.base.BaseResponse;
import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;

/**
 * Created by yhz on 2016/5/20.
 */
public class ResponseTransformer<T> implements Transformer<BaseResponse<T>, T> {
    @Override
    public Observable<T> call(Observable<BaseResponse<T>> responseObservable) {
        return responseObservable.flatMap(new Func1<BaseResponse<T>, Observable<T>>() {
            @Override
            public Observable<T> call(BaseResponse<T> response) {
                return Observable.just(response.data);
            }
        });
    }
}
