package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseWebActivity;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.NetUtil;
import cn.mofufin.morf.databinding.ActivityOverseansWebBinding;

public class OverseansWebActivity extends BaseWebActivity {
    private ActivityOverseansWebBinding binding;
    private WebView webView;
    private WebSettings settings;
    private String tag="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_overseans_web);
        binding.setHandlers(this);
        initWebView();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected WebView getWebView() {
        return binding.webDetail;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initWebView() {
        super.initWebView();

        tag = getIntent().getStringExtra(IntentParam.ACTIVITY_FLAG);
        webView = binding.webDetail;
        settings = webView.getSettings();
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(false); //启用或禁用DOM缓存。
        settings.setDatabaseEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Logger.e("url==="+url);
                Logger.e("message==="+message);
                Logger.e("result==="+result.toString());
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                if (!TextUtils.isEmpty(tag)){
//                    view.loadUrl(request.getUrl().toString());
//                    Logger.e("shouldOverrideUrlLoading1");
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (!TextUtils.isEmpty(tag)){
//                    view.loadUrl(url);
//                    Logger.e("shouldOverrideUrlLoading2");
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }
        });

        String html = getIntent().getStringExtra("HTML");
        loadUrl(html);
    }

    @Override
    public void title(View view) {

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 检查网络
     *
     * @return
     */
    public boolean inspectNet() {
        netMobile = NetUtil.getNetWorkState(this);
        return isNetConnect();

    }

    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }
}
