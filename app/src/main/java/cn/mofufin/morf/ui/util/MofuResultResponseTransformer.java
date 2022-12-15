package cn.mofufin.morf.ui.util;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;

/**
 * Created by yhz on 2016/5/20.
 */
public class MofuResultResponseTransformer<T> implements Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> responseObservable) {
        return responseObservable.flatMap(new Func1<T, Observable<T>>() {
            @Override
            public Observable<T> call(T response) {
                return Observable.just(response);
            }
        });
    }
}
