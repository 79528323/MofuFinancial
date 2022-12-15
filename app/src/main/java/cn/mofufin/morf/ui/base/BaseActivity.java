package cn.mofufin.morf.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.multidex.MultiDex;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.TakePhotoOptions;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import cc.ruis.lib.base.LibBaseFragmentActivity;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.dailog.LoadingDialog;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.widget.SlidingLayout;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by yhz on 2015/5/22.
 * Activity 基类
 */
public class BaseActivity extends LibBaseFragmentActivity implements BaseUI, BaseBinding {
    protected LoadingDialog loadingDialog;//加载用到Dialog
    protected RxManager rxManager = new RxManager();
//    protected ImageFragment imageFragment;
    private boolean loadingDialogDisabled;
    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseApplication.activityStackManager.pushActivity(this);
        hasInstanceState(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_blue));
    }

    /**
     * 开启滑动返回
     * @return
     */
    protected boolean enableSliding() {
        return true;
    }

    public String getCurProcessName(Context context) {
        String processName = "";
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = mActivityManager.getRunningAppProcesses();
        if (processInfoList != null && !processInfoList.isEmpty() && processInfoList.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
                if (processInfo.pid == pid) {
                    processName = processInfo.processName;
                }
            }
        }
        return processName;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (!TextUtils.equals(":init", getCurProcessName(base))) {
            MultiDex.install(this);
        }

    }

    protected void setStatusBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    /*| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
//            window.setNavigationBarColor(Color.WHITE);

        }else {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }

        StatusBarCompat.setLightStatusBar(getWindow(),true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置系统状态栏颜色
     *
     * @param isSet
     */
    @SuppressLint("ResourceAsColor")
    public void setStatusBar(boolean isSet) {
        if (isSet) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }
    }

    @Override
    protected void onResume() {
        BaseApplication.foregroundActivityManager.pushForegroundActivity(this);

        super.onResume();
        // MobclickAgent.onResume(this);
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobileInfo.isConnected() && !wifiInfo.isConnected()){
            Toast.makeText(this,"当前网络不可用，请检查", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        BaseApplication.foregroundActivityManager.popForegroundActivity(this);
        super.onPause();
        //注：回调 2

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        BaseApplication.activityStackManager.popActivity(this);
        rxManager.clear();
        super.onDestroy();
    }

    @Override
    public void finish() {
        hiddenLoading();
        super.finish();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            hideKeyboard();
//        }
//        return super.onTouchEvent(event);
//    }


    /**
     * 显示进度条
     */
    @Override
    public void showLoading() {
        if (!loadingDialog.isShowing() && !isFinishing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hiddenLoading() {
        if (loadingDialog.isShowing() && !isFinishing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showTips(@StringRes int tipRes) {
        Toast.makeText(this, tipRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTips(String tip) {
        if (TextUtils.isEmpty(tip)) {
            return;
        }
        Logger.i(TAG, tip);
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTips(String tip, int duration) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable throwable, boolean show) {
        Logger.w("xxx", "网络异常信息：" + throwable.toString());
        hiddenLoading();
        if (throwable instanceof SocketTimeoutException || throwable instanceof HttpException) {
            showTips(R.string.default_http_time_out);
        } else if (throwable instanceof SSLHandshakeException || throwable instanceof UnknownHostException) {
            showTips(R.string.default_http_error);
        } else if (throwable instanceof ConnectException) {
            showTips("连接失败");
        } else {
            if (show) {
                try {
                    showTips(throwable.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public RxManager getRxManager() {
        return rxManager;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        if (User.mine != null) {
//            outState.putParcelable(IntentParam.USER, User.mine);
//        }
    }

    private void hasInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            User user = savedInstanceState.getParcelable(IntentParam.USER);
//            if (user != null && User.mine == null) {
//                User.mine = user;
//            }
//        }
    }

    @Override
    public void exit(View view) {
        finish();
    }

    @Override
    public void submit(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (imageFragment != null) {
//            if (!imageFragment.isResumed()) {
//                return;
//            }
//            imageFragment.runExitAnimation(new Runnable() {
//                public void run() {
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .remove(imageFragment)
//                            .commitAllowingStateLoss();
//                    imageFragment = null;
//                }
//            });
//        } else {
//            super.onBackPressed();
//        }
    }


    public void title(View view) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        //注：回调 3
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                clickTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    if (System.currentTimeMillis() - clickTime <= 350) {
                        hideKeyboard();
                    }

                }
                break;
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {//TODO 没有焦点 直接隐藏键盘
            if (v instanceof EditText){
                //TODO 如果为输入框，判断点击位置隐藏键盘
                int[] l = {0, 0};
                v.getLocationInWindow(l);

                int left = l[0],
                    top = l[1] - 20,
                    bottom = top + v.getMeasuredHeight() + 20,
                    right = left + v.getMeasuredWidth();

                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    return false;
                } else {
                    return true;
                }
            }else
                return true;
        }else
            return true;
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
//        return false;
    }

//    private static final int MIN_DELAY_TIME= 1500;  // 两次点击间隔不能少于1000ms
//    private static long lastClickTime;
//
//    public static boolean isFastClick() {
//        boolean flag = true;
//        long currentClickTime = System.currentTimeMillis();
//        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
//            flag = false;
//        }
//        lastClickTime = currentClickTime;
//        return flag;
//    }


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
}
