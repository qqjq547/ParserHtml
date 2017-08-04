package com.hjq.parserhtml.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.hjq.parserhtml.CommonUtil;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.RxUtil;
import com.hjq.parserhtml.http.retrofit.ApiCallback;
import com.hjq.parserhtml.http.retrofit.ApiClient2;
import com.hjq.parserhtml.model.LSM;
import com.hjq.parserhtml.model.WLSM;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

public class WebActivity extends AppCompatActivity {
    @BindView(R.id.wv_lsm)
    WebView wvLsm;
    @BindView(R.id.btn_download)
    Button btnDownload;
    private CompositeSubscription mCompositeSubscription;
    String dir = Environment.getExternalStorageDirectory() + File.separator + "lesmao" + File.separator;
    public static final String FORMAT_DATE_All = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_FILE = "yyyyMMdd_HHmmss";
    public static final String FORMAT_DATE_DIR = "yyyyMMdd";
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
        wvLsm.addJavascriptInterface(new Handler(),"handler");
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
        });
        wvLsm.loadUrl("http://www.lesmao.com/portal.php?page=1&mobile=no");
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
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @OnClick(R.id.btn_download)
    public void onViewClicked() {
        btnDownload.setEnabled(false);
        wvLsm.loadUrl("javascript:window.handler.show(document.body.innerHTML);");

    }

     class Handler {
         @JavascriptInterface
        public void show(String data) {
             String tag = "<li><img alt=";
             String[] textArr=data.split(tag);
             Log.d("hjq","textArr="+textArr.length);
             List<String> urlArr=new ArrayList<String>();
             String time=data.split("<em>")[1].split("</em>")[0];
             for (int i=1;i<textArr.length;i++){
//                 String[] srcArr=textArr[i].split("\"");
                 String url=textArr[i].split("\"")[3];
                 Log.d("hjq", "url=" + url);
                 String reg="(?i).+?\\.(jpg|png|jpeg)";
                 if (url.matches(reg)) {
                     urlArr.add(url);
                 }
             }
            Log.e("hjq","curUrl="+curUrl);
             String[] sp=curUrl.split("-");
             int arrid=Integer.parseInt(sp[1]);
             int page=Integer.parseInt(sp[2]);;
             WLSM model2=new WLSM(arrid,"",time,(page-1),urlArr);
             downPic(model2,0);
        }
    }
    public void downPic(final WLSM model, final int index) {
        String url=model.getUrlArr().get(index);
        Log.d("hjq","downUrl="+url);
        addSubscription(RxUtil.createCompressBmpObservable(ApiClient2.getInstance().getApiStores().downloadPicFromNet(url)).subscribe(new ApiCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap data) {
                saveBitmap(data, model,index);
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                if ((index+1) < model.getUrlArr().size()) {
                    btnDownload.setText("正在下载  "+index+"/"+model.getUrlArr().size());
                    downPic(model, index + 1);
                }else{
                    //下载完成
                    btnDownload.setText("下载完成");
                    btnDownload.setEnabled(true);
                }
            }
        }));
    }
    public void saveBitmap(Bitmap bm,WLSM model,int index ) {
        Date date= CommonUtil.stringToDate(model.getTime(),FORMAT_DATE_All);
        String timeStr=CommonUtil.dateToString(date,FORMAT_DATE_FILE);
        String dayStr=CommonUtil.dateToString(date,FORMAT_DATE_DIR);
        String fileName=timeStr+"_" + ((model.getPage()*5)+index+1) + ".jpg";
        String dirName=dir+File.separator+dayStr;
        if (!new File(dirName).exists()){
            new File(dirName).mkdirs();
        }
        File f = new File(dirName, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i("hjq", "已经保存:" + f.getAbsolutePath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("hjq", "保存失败:" + f.getAbsolutePath());
        }finally {
            bm.recycle();
            bm=null;
        }
    }
    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }
}
