package cn.mofufin.morf.ui.util;

import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.base.BaseUI;
import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by yhz on 2016/5/20.
 */
public class RequestTransformer<T> implements Transformer<BaseResponse<T>, BaseResponse<T>> {
    private BaseUI baseUI;

    public RequestTransformer(BaseUI baseUI){
        this.baseUI = baseUI;
    }

    @Override
    public Observable<BaseResponse<T>> call(Observable<BaseResponse<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        baseUI.showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        baseUI.hiddenLoading();
                    }
                });
    }
}
