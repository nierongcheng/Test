package com.codi.testjsinteraction;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    private final String URL = "https://m.baidu.com";
    private final String URL = "https://mobile.payeco.com/ppi/h5/plugin/itf.do?tradeId=h5Init&Version=2.0.0&MerchantId=502050001670&MerchOrderId=55a7990f0cf2fbc420fe13fb2&Amount=20&TradeTime=20150716194415&OrderId=502015071657074162&VerifyTime=DA1B59E60EFE8ACA285D3590B3D95FE88477AAE2EC30502A7E13FE587EB128ABE860116D2259C459959EF785E7DFC3B8&Sign=BZXJkOGoO3T7yBQBYCg6sjjxWcJdSJHt7RAl0jthCxgUXc+fSBngLzp2ytLtskDOmFlwreguD5WitmaVTc+JW+oWAet0csgUH88LHywFFo73IXNJb+bTb9KzHzw5I0YJLmIGh8oweFwmNCgcbviKxuVVJTX3c8Ny5T6+pbhFaj0=";

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webView);
        initWebView(mWebView);

        mWebView.loadUrl(URL);
    }

    private void initWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new MyJavascriptInterface(this), "androidInterface");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Toast.makeText(MainActivity.this, "load url--->" + url, Toast.LENGTH_SHORT).show();
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("-------->" + url);
                Toast.makeText(MainActivity.this, "load url--->" + url, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    private void processJsReturnValue(String value) {
        Toast.makeText(this, "get value from html: " + value, Toast.LENGTH_LONG).show();
    }

    public void getValueFromHtml(View view) {
//        mWebView.loadUrl("javascript:alert(document.getElementsByClassName(\"pay-btn pay-btn2\")[0].innerHtml)");
//        mWebView.loadUrl("javascript:alert(document.getElementsByClassName(\"pay-btn pay-btn2\")[0].childNodes[1].defaultValue)");
//        mWebView.loadUrl("javascript:alert(document.getElementsByClassName(\"pay-btn pay-btn2\")[0].childNodes[1].defaultValue)");
        mWebView.loadUrl("javascript:function() { androidInterface.processReturnValue(document.getElementsByClassName(\"pay-btn pay-btn2\")[0].childNodes[1].defaultValue); }");
    }

    private static class MyJavascriptInterface {
        private MainActivity activity;

        public MyJavascriptInterface(MainActivity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void processReturnValue(String value) {
            activity.processJsReturnValue(value);
        }
    }
}
