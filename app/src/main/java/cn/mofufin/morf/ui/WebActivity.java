package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseWebActivity;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.NetUtil;
import cn.mofufin.morf.databinding.ActivityWebBinding;

public class WebActivity extends BaseWebActivity {
    private ActivityWebBinding binding;
    private WebView webView;
    private WebSettings settings;
    private String tag="" ,title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web);
        binding.setHandlers(this);
        title = getIntent().getStringExtra(IntentParam.TITLE);
        if (TextUtils.isEmpty(title)){
            title = getString(R.string.home_shortservice2);
        }
        binding.setTitleName(title);

//        initWebView();
        tag = getIntent().getStringExtra(IntentParam.ACTIVITY_FLAG);
        webView = binding.webDetail;
        settings = webView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true); //启用或禁用DOM缓存。
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (inspectNet()){
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
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
                binding.setProgress(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        String html = getIntent().getStringExtra("HTML");
//        if (TextUtils.isEmpty(html)){
//            loadUrl(Common.MOFU_LICAI);
//        }else {
            loadUrl(html);
//        }
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
//        super.initWebView();

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
                binding.setProgress(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                    return false;
                }

                try {
                    if (uri.getScheme().equals("mf")){
                        String path = uri.getPath();
                        Logger.e("path="+path);
                        if (path.equals("realnameAuth")){
                            //实名页面
                            startActivity(new Intent(WebActivity.this, RealNameIdentityActivity.class));
                        }else if (path.equals("quickPay")){
                            //快捷支付
                            startActivity(new Intent(WebActivity.this,SelectionChannelActivity.class));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    finish();
                }

                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }

                try {
                    if (url.startsWith("mf")){
                        String path = url.substring(5);
                        Logger.e("path="+path);
                        if (path.equals("realnameAuth")){
                            //实名页面
                            startActivity(new Intent(WebActivity.this, RealNameIdentityActivity.class));
                        }else if (path.equals("quickPay")){
                            //快捷支付
                            startActivity(new Intent(WebActivity.this,SelectionChannelActivity.class));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    finish();
                }

                return true;
            }
        });
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
//        if (webView!=null){
//            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            webView.clearHistory();
//            ViewGroup viewGroup = ((ViewGroup) webView.getParent());
//            if (viewGroup!=null){
//                viewGroup.removeView(webView);
//            }
//            webView.destroy();
//            webView =null;
//        }
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
