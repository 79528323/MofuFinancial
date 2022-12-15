package cn.mofufin.morf.ui.Listener;

import androidx.annotation.NonNull;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import cn.mofufin.morf.ui.util.DataUtils;

//@GlideExtension
public class GlideRequestOptions extends RequestOptions {
    RequestOptions options  = new RequestOptions();

    public GlideRequestOptions() {

    }

    @Override
    public RequestOptions diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        return options.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RequestOptions signature(@NonNull Key signature) {
        return options.signature(new ObjectKey(DataUtils.GetNowTime()));
    }

    @Override
    public RequestOptions encodeQuality(int quality) {
        return options.encodeQuality(100);
    }

    @NonNull
    @Override
    public RequestOptions circleCrop() {
        return options;
    }

    @NonNull
    @Override
    public RequestOptions optionalCircleCrop() {
        return options;
    }


}
