package cn.mofufin.morf.ui.binding;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import cn.mofufin.morf.ui.Listener.GlideRequestOptions;

/**
 * Created by yhz on 2016/8/5.
 * data binding的工具类
 */
public class ImageViewAttrAdapter {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView, int resId) {
        if (resId == 0) {
            imageView.setImageResource(android.R.color.transparent);
        } else {
            imageView.setImageResource(resId);
        }
    }

    @BindingAdapter("icon")
    public static void setIcon(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url)
                //.transform(DataUtils.circleTransform)
                .apply(new GlideRequestOptions())
                .into(imageView);
    }

    @BindingAdapter({"uploadIcon", "empty"})
    public static void setUploadIcon(ImageView imageView, String url, boolean empty) {
        if (empty && TextUtils.isEmpty(url)) {
            return;
        }
        setUploadIcon(imageView, url);
    }

    @BindingAdapter("uploadIcon")
    public static void setUploadIcon(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url)
                .apply(new GlideRequestOptions())
                .into(imageView);
    }

    @BindingAdapter({"icon", "empty"})
    public static void setIcon(ImageView imageView, String url, boolean empty) {
        if (empty && TextUtils.isEmpty(url)) {
            return;
        }
//        setIcon(imageView, url);
    }

    @BindingAdapter("url")
    public static void setUrl(ImageView imageView, String url) {
        setUrl(imageView, url, null);
    }

    @BindingAdapter({"url", "placeholder"})
    public static void setUrl(ImageView imageView, String url, Drawable drawable) {
        setUrl(imageView, url, drawable, false);
    }

    @BindingAdapter({"url", "centerCrop"})
    public static void setUrl(ImageView imageView, String url, boolean centerCrop) {
        setUrl(imageView, url, null, centerCrop, -1);
    }

    @BindingAdapter({"url", "placeholder", "centerCrop"})
    public static void setUrl(ImageView imageView, String url, Drawable drawable, boolean centerCrop) {
        setUrl(imageView, url, drawable, centerCrop, -1);
    }

    @BindingAdapter({"url", "placeholder", "centerCrop", "maxSize"})
    public static void setUrl(ImageView imageView, String url, Drawable drawable, boolean centerCrop, int maxSize) {
        RequestOptions options = new RequestOptions();
        RequestBuilder request = Glide.with(imageView.getContext()).load(url);

        if (drawable != null) {
            options.placeholder(drawable)
                    .error(drawable);
        }
        if (centerCrop) {
            options.centerCrop();
        }
        if (maxSize > 0) {
            options.override(maxSize, maxSize);
        }
        options.dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        request.apply(options).into(imageView);
    }

//    @BindingAdapter("group")
//    public static void setGroupIcon(ImageView imageView, Group group) {
//        if (group != null) {
//            if (group.user_upfiles == null) {
//                group.user_upfiles = new ArrayList<>();
//            }
//            if (group.number > group.user_upfiles.size()) {
//                int size = group.number - group.user_upfiles.size();
//                for (int i = 0; i < size; i++) {
//                    group.user_upfiles.add("");
//                }
//            }
//            ComposeImage.display(imageView, GroupIconAdapter.getAdapter(), group, R.drawable.default_icon);
//        } else {
//            imageView.setImageResource(R.drawable.default_icon);
//        }
//    }

//    @BindingAdapter({"icon", "group"})
//    public static void setGroupIcon(ImageView imageView, String icon, Group group) {
//        if (group != null) {
//            if (group.user_upfiles == null) {
//                group.user_upfiles = new ArrayList<>();
//            }
//            if (group.number > group.user_upfiles.size()) {
//                int size = group.number - group.user_upfiles.size();
//                for (int i = 0; i < size; i++) {
//                    group.user_upfiles.add("");
//                }
//            }
//            ComposeImage.display(imageView, GroupIconAdapter.getAdapter(), group, R.drawable.default_icon);
//        } else if (icon != null) {
//            setIcon(imageView, icon);
//        } else {
//            imageView.setImageResource(R.drawable.default_icon);
//        }
//    }

//    @BindingAdapter("captcha")
//    public static void setCaptcha(ImageView imageView, Captcha captcha) {
//        if (captcha == null) {
//            return;
//        }
//        Bitmap bitmap = DataUtils.base64ToBitmap(Captcha.getImage64(captcha));
//        imageView.setImageBitmap(bitmap);
//    }

//    @BindingAdapter("photo")
//    public static void setPhoto(ImageView imageView, Dynamic dynamic) {
//        ComposeImage.display(imageView, PhotoImageAdapter.getAdapter(), dynamic, 0);
//    }
}