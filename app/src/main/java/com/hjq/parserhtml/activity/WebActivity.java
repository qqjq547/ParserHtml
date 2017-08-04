package com.hjq.parserhtml.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjq.parserhtml.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/4.
 */

public class WebActivity extends AppCompatActivity {
    @BindView(R.id.wv_lsm)
    WebView wvLsm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        wvLsm.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        wvLsm.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvLsm.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        wvLsm.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        wvLsm.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        wvLsm.getSettings().setBuiltInZoomControls(false);
        wvLsm.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvLsm.loadUrl("http://www.lesmao.com/");
    }
}
