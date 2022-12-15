package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityH5PayBinding;

public class H5PayActivity extends BaseActivity {
    private ActivityH5PayBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_h5_pay);
        binding.setHandlers(this);
        String html = getIntent().getStringExtra("HTML");
        String tcName = getIntent().getStringExtra(IntentParam.TITLE);
        binding.setTitle(tcName);
        init();
        binding.webview.loadDataWithBaseURL(Common.HOST,html,"text/html" ,"utf-8",null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init(){
        WebSettings settings = binding.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                    return false;
                }
                try {
                    //通过意图调起支付宝
                    Logger.e("uri.getAuthority()====="+uri.getAuthority());
                    Logger.e("uri.toString()====="+uri.toString());
//                    Logger.e("uri.getAuthority()====="+uri.getAuthority());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    //通过意图调起支付宝
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        binding.webview.setWebChromeClient(new WebChromeClient());
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
}
