package cn.mofufin.morf.ui.base;

import android.app.Dialog;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.collection.ArrayMap;
import androidx.appcompat.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.githang.statusbar.StatusBarCompat;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Map;

import cc.ruis.lib.base.LibBaseWebActivity;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.dailog.LoadingDialog;
import cn.mofufin.morf.ui.widget.SlidingLayout;

/**
 * Created by yhz on 2016/6/28.
 */
public abstract class BaseWebActivity extends LibBaseWebActivity implements BaseBinding {
    protected LoadingDialog loadingDialog;
    protected int netMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseApplication.activityStackManager.pushActivity(this);
        hasInstanceState(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_blue));
    }


    protected boolean enableSliding() {
        return true;
    }

    public void setStatusBar(boolean isSet) {
        if (isSet) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //透明状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                // 激活状态栏
                tintManager.setStatusBarTintEnabled(true);
                //设置系统栏设置颜色
                tintManager.setTintColor(R.color.app_blue);
                //给状态栏设置颜色
                tintManager.setStatusBarTintResource(R.color.app_blue);
                //Apply the specified drawable or color resource to the system navigation bar.
                switch (Build.MANUFACTURER.toLowerCase()) {
                    case "vivo":
                        tintManager.setNavigationBarTintEnabled(false);
                        break;
                    default:
                        // enable navigation bar tint 激活导航栏
                        tintManager.setNavigationBarTintEnabled(true);
                        //给导航栏设置资源
                        tintManager.setNavigationBarTintResource(R.color.app_blue);
                        break;
                }
            }
        }
    }

    @Override
    protected void initWebView() {
        super.initWebView();

        getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Dialog dialog = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogCustom)
                        .setTitle(cc.ruis.lib.R.string.tip_title)
                        .setMessage(message)
                        .setPositiveButton(cc.ruis.lib.R.string.confirm, null)
                        .create();
                dialog.show();
                result.confirm();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hiddenLoading();
                }
            }
        });

        getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    @Override
    protected Map<String, String> getHeader() {
        Map<String, String> map = new ArrayMap<>();
        map.put("Cache-Control", "max-age=" + getCacheMaxAge());
        return map;
    }

    protected long getCacheMaxAge() {
        return 60 * 60 * 24;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
        BaseApplication.foregroundActivityManager.pushForegroundActivity(this);
        super.onResume();
//        Bugtags.onResume(this);
        //   MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        BaseApplication.foregroundActivityManager.popForegroundActivity(this);
        super.onPause();
//        Bugtags.onPause(this);
        // MobclickAgent.onPause(this);
    }

    @Override
    public void finish() {
        hiddenLoading();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        BaseApplication.activityStackManager.popActivity(this);
        super.onDestroy();
    }

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
    public void exit(View view) {
        finish();
    }

    @Override
    public void submit(View view) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        if (User.mine != null) {
//            outState.putParcelable(IntentParam.USER, User.mine);
//        }
//        getWebView().saveState(outState);
    }

    private void hasInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            User user = savedInstanceState.getParcelable(IntentParam.USER);
//            if (user != null && User.mine == null) {
//                User.mine = user;
//            }
//            getWebView().restoreState(savedInstanceState);
//        }
    }
}
