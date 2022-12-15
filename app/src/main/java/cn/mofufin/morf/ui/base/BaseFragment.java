package cn.mofufin.morf.ui.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.TakePhotoOptions;

import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import cc.ruis.lib.base.LibBaseFragment;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.util.Common;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by yhz on 2016/5/13.
 */
public class BaseFragment extends LibBaseFragment implements BaseUI, LoadingUI,View.OnTouchListener {
    protected RxManager rxManager=new RxManager();

    public BaseFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        rxManager.clear();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        if(loadingView != null && loadingView.getVisibility() == View.GONE){
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void showLoading(String tips){
        if(loadingView != null && loadingView.getVisibility() == View.GONE){
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hiddenLoading() {
        if(loadingView != null && loadingView.getVisibility() == View.VISIBLE){
            loadingView.setVisibility(View.GONE);
        }
    }


    @Override
    public void showTips(@StringRes int tipRes) {
        try {
            Toast.makeText(getActivity(), tipRes, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(getActivity(), tipRes, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    @Override
    public void showTips(String tip){
        if(TextUtils.isEmpty(tip)){
            return;
        }
        Logger.i(TAG, tip);
        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTips(String tip, int duration) {
        Toast.makeText(getActivity(), tip, duration).show();
    }

    @Override
    public void onError(Throwable throwable, boolean show) {
        if(throwable instanceof SocketTimeoutException || throwable instanceof HttpException){
            showTips(R.string.default_http_time_out);
        } else if(throwable instanceof SSLHandshakeException || throwable instanceof UnknownHostException){
            showTips(R.string.default_http_error);
        } else{
            if(show){
                try{
                    showTips(throwable.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public RxManager getRxManager() {
        return rxManager;
    }

    protected View loadingView, errorView, contentView;
    /*protected View inflateView(LayoutInflater inflater, @Nullable ViewGroup container, int layoutRes){
        FragmentBaseBinding binding= DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
        binding.setHandlers(this);
        loadingView=binding.loading.fragmentLoading;
        AnimationDrawable animationDrawable=(AnimationDrawable)binding.loading.loadingAnim.getBackground();
        animationDrawable.start();
        errorView=binding.error.fragmentError;
        contentView=inflater.inflate(layoutRes, binding.fragmentContent, false);
        binding.fragmentContent.addView(contentView);
        errorView.setOnClickListener(null);
        loadingView.setOnClickListener(null);
        return binding.getRoot();
    }*/

    protected void showError() {
        if(errorView != null && errorView.getVisibility() == View.GONE) {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    protected void hiddenError() {
        if(errorView != null && errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void reload(View view) {
        showLoading();
        hiddenError();
    }

    @Override
    public void loadSuccess() {//下载成功
        if(loadingView != null && loadingView.getVisibility() == View.VISIBLE){
            hiddenLoading();
            hiddenError();
        }
    }

    @Override
    public void loadFailure() {
        if(loadingView != null && loadingView.getVisibility() == View.VISIBLE){
            showError();
            hiddenLoading();
        }
    }

    protected BaseActivity getFragmentActivity(){
        return (BaseActivity)getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        saveStateToArguments();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this,null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final int MIN_DELAY_TIME= 1500;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }



    /**
     * 照片设置
     * @param takePhoto
     */
    public void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(true);//纠正照片角度旋转
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 照片压缩
     * @param takePhoto
     */
    public void configCompress(TakePhoto takePhoto) {
        int maxSize = 1024 * 3 * 100;//500kb

        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)//文件大小
                .setMaxPixel(Common.IMAGE_MAX_SIZE)//最大像素
                .enablePixelCompress(true)
                .enableQualityCompress(true)
                .enableReserveRaw(false)//拍照压缩后是否保存原图
                .create();
        takePhoto.onEnableCompress(config,false);
    }

    public void configCompress(TakePhoto takePhoto,int maxSize) {
//        int maxSize = 1024 * 3 * 100;//500kb

        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)//文件大小
                .setMaxPixel(Common.IMAGE_MAX_SIZE)//最大像素
                .enablePixelCompress(true)
                .enableQualityCompress(true)
                .enableReserveRaw(false)//拍照压缩后是否保存原图
                .create();
        takePhoto.onEnableCompress(config,false);
    }

}
