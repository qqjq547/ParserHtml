package com.hjq.parserhtml.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.hjq.parserhtml.CommonUtil;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.RxUtil;
import com.hjq.parserhtml.http.retrofit.ApiCallback;
import com.hjq.parserhtml.http.retrofit.ApiClient2;
import com.hjq.parserhtml.model.WLSM;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/8/4.
 */

public class Web1Activity extends AppCompatActivity {
    @BindView(R.id.wv_lsm)
    WebView wvLsm;
    String curUrl="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wvLsm.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        wvLsm.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvLsm.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        wvLsm.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        wvLsm.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        wvLsm.getSettings().setBuiltInZoomControls(true);
//        wvLsm.addJavascriptInterface(new Handler(),"handler");
        wvLsm.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                curUrl=url;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });
        wvLsm.loadUrl("https://www.mm131.net/");
////      wvLsm.loadUrl("http://m.mm131.com/xinggan/3300_1.html");
//        wvLsm.loadUrl("http://img1.mm131.com/pic/3300/10.jpg");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (wvLsm.canGoBack()) {
            wvLsm.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
