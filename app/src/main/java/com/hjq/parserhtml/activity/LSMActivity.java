package com.hjq.parserhtml.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hjq.parserhtml.CommonUtil;
import com.hjq.parserhtml.adapter.LSMAdapter;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.RxUtil;
import com.hjq.parserhtml.http.retrofit.ApiCallback;
import com.hjq.parserhtml.http.retrofit.ApiClient2;
import com.hjq.parserhtml.model.LSM;

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

public class LSMActivity extends AppCompatActivity {
    String dir = Environment.getExternalStorageDirectory() + File.separator + "lesmao" + File.separator;
    public static final String FORMAT_DATE_All = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_FILE = "yyyyMMdd_HHmmss";
    public static final String FORMAT_DATE_DIR = "yyyyMMdd";
    ArrayList<LSM> dataArr = new ArrayList<>();
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    LSMAdapter adapter;
    int pageNum = 0;
    int start = 0;
    int end = 0;
    @BindView(R.id.ev_page)
    EditText evPage;
    @BindView(R.id.ev_start)
    EditText evStart;
    @BindView(R.id.ev_end)
    EditText evEnd;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.wv_content)
    WebView wvContent;
    private CompositeSubscription mCompositeSubscription;
    String curUrl="";
    int curPos=0;
    int curPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsm);
        ButterKnife.bind(this);
        if (!new File(dir).exists()) {
            new File(dir).mkdir();
        }
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new LSMAdapter(this,dataArr);
        rvList.setAdapter(adapter);
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setBlockNetworkImage(true);
        wvContent.addJavascriptInterface(new Handler(),"handler");
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                curUrl=url;
                view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
                super.onPageFinished(view, url);

            }
        });
    }


    public void startDown(int curPos) {
        LSM model=dataArr.get(curPos);
        if (model.getUrlArr().size()>0) {
            downPic(curPos, 1, model.getUrlArr().get(0));
        }else {
            startDown(curPos+1);
        }
//        for (int i=0;i<dataArr.size();i++) {
//            LSM model=dataArr.get(i);
//            downPic(i,model.getArrarId(), 1, model.getUrlArr().get(0));
//            if (model.getUrlArr().size()>0) {
//                downPic(i,model.getArrarId(), 1, model.getUrlArr().get(0));
//            }
//        }
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
    public void getPageData(final int pageNum,final  int start,final int end) {
        this.pageNum=pageNum;
        this.start=start;
        this.end=end;
        String format="http://www.lesmao.com/portal.php?page=%d&mobile=no";
        wvContent.loadUrl(String.format(format,pageNum));
//        addSubscription(RxUtil.createHttpObservable(ApiClient2.getInstance1().getApiStores1().getPageData(pageNum)).subscribe(new ApiCallback<String>() {
//            @Override
//            public void onSuccess(String data) {
//                String tag = "<div class=\"photo\"><a href=\"http://www.lesmao.com/thread-";
//                String[] textArr=data.split(tag);
//                List<String> urlArr=new ArrayList<String>();
//                for (int i=start;i<end+1;i++){
//                    int typeid=Integer.parseInt(textArr[i].split("-")[0]);
//                    String title=textArr[i].split("alt=\"")[1].split("\" />")[0];
//                    String thumb=textArr[i].split("<img src=\"")[1].split("\"")[0];
//                    dataArr.add(new LSM(typeid,title,thumb,0,0,new ArrayList<String>()));
//                }
//                adapter.notifyDataSetChanged();
//                for (int i=0;i<dataArr.size();i++){
//                    getSize(i,dataArr.get(i).getArrarId(),1);
//                }
//            }
//
//            @Override
//            public void onFailure(String exception) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }));

    }
   public void getNextTime(final int position, final int id){
            addSubscription(RxUtil.createHttpObservable(ApiClient2.getInstance1().getApiStores1().getSize2(id,1)).subscribe(new ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String time=data.split("<em>")[4].split("</em>")[0];
                LSM model2=dataArr.get(position);
                model2.setTime(time);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
            }
        }));
   }
    public void getSize(final int position, final int id, final int page) {
        String format="http://www.lesmao.com/thread-%d-%d-1.html";
        curPos=position;
        curPage=page;
        wvContent.loadUrl(String.format(format,id,page));
    }

    public void downPic(final int position, final int index,String url) {
        Log.d("hjq","downUrl="+url);
        addSubscription(RxUtil.createCompressBmpObservable(ApiClient2.getInstance().getApiStores().downloadPicFromNet(url)).subscribe(new ApiCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap data) {
                LSM model = dataArr.get(position);
                saveBitmap(data, model,index,position);
                model.setDownNum(model.getDownNum() + 1);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                LSM model = dataArr.get(position);
                if (index < model.getCount()) {
                    downPic(position, index + 1,model.getUrlArr().get(index));
                }else{
                    if (position<(dataArr.size()-1)) {
                        int curpos=nextPosition(position);
                        downPic(curpos, 1, dataArr.get(curpos).getUrlArr().get(0));
                    }
                }
            }
        }));

    }
    public int nextPosition(int position){
        if (dataArr.get(position+1).getUrlArr().size()>0){
            return position+1;
        }else {
            if (position<(dataArr.size()-1)) {
                return nextPosition(position+1);
            }else {
                return position+1;
            }
        }
    }
   public String getNextTime(int position){
       if (CommonUtil.stringToDate(dataArr.get(position+1).getTime(),FORMAT_DATE_All)==null){
          return getNextTime(position+1);
       }else{
           return dealTime(dataArr.get(position+1).getTime(),true);
       }
   }
    public void saveBitmap(Bitmap bm,LSM model,int index,int position ) {
        Date date=CommonUtil.stringToDate(model.getTime(),FORMAT_DATE_All);
        if (date==null){
            if (position>1){
                model.setTime(dealTime(dataArr.get(position-1).getTime(),false));
            }else{
                model.setTime(getNextTime(position));
            }
        }
        String timeStr=CommonUtil.dateToString(date,FORMAT_DATE_FILE);
        String dayStr=CommonUtil.dateToString(date,FORMAT_DATE_DIR);
        String fileName=timeStr+"_" + CommonUtil.unitFormat(index) + ".jpg";
        String dirName=dir+File.separator+dayStr;
        if (!new File(dirName).exists()){
            new File(dirName).mkdirs();
        }
        File f = new File(dirName, fileName);
        if (f.exists()) {
//            f.delete();
            model.setTime(dealTime(model.getTime(),false));
            saveBitmap(bm,model,index,position);
            return;
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
    public String dealTime(String dateStr,boolean add){
        Date date=CommonUtil.stringToDate(dateStr,FORMAT_DATE_All);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,add?1:-1);
        return CommonUtil.dateToString(calendar.getTime(),FORMAT_DATE_All);
    }

    @OnClick({R.id.btn_get, R.id.btn_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                String pageNumStr = evPage.getText().toString().trim();
                String startStr = evStart.getText().toString().trim();
                String endStr = evEnd.getText().toString().trim();
                if (TextUtils.isEmpty(pageNumStr) || TextUtils.isEmpty(startStr)||TextUtils.isEmpty(endStr)) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    pageNum=Integer.parseInt(pageNumStr);
                    start=Integer.parseInt(startStr);
                    end=Integer.parseInt(endStr);
                    if (end>36||start<1||start>end){
                        Toast.makeText(this, "内容不规范", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getPageData(pageNum,start,end);
                }
                break;
            case R.id.btn_down:
                startDown(0);
                break;
        }

    }
    class Handler {
        @JavascriptInterface
        public void show(String data) {
            Log.e("hjq","curUrl="+curUrl);
            if (curUrl.startsWith("http://www.lesmao.com/portal.php")){
                String tag = "<div class=\"photo\"><a href=\"thread-";
                String[] textArr=data.split(tag);
                List<String> urlArr=new ArrayList<String>();
                if(textArr.length<end){
                    end=textArr.length-1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            evEnd.setText(""+end);
                            Toast.makeText(LSMActivity.this,"调整end",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                for (int i=start;i<end+1;i++){
                    int typeid=Integer.parseInt(textArr[i].split("-")[0]);
                    String title=textArr[i].split("alt=\"")[1].split("\"")[0];
                    String thumb=textArr[i].split("<img src=\"")[1].split("\"")[0];
                    dataArr.add(new LSM(typeid,title,thumb,0,0,new ArrayList<String>()));
                    getNextTime(dataArr.size()-1,typeid);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        getSize(0,dataArr.get(0).getArrarId(),1);
                    }
                });

            }else if(curUrl.startsWith("http://www.lesmao.com/thread")){
                String tag = "<li><img alt=";
                String[] textArr=data.split(tag);
                List<String> urlArr=new ArrayList<String>();
                String time=data.split("<em>")[1].split("</em>")[0];
                int totalPage=1;
                if (data.split("<span title=\"共 ").length>1){
                    String pagestr=data.split("<span title=\"共 ")[1].split(" ")[0];
                    totalPage=Integer.parseInt(pagestr);
                }
                Log.e("hjq","position="+curPos+",totalPage="+totalPage);
                for (int i=0;i<textArr.length;i++){
                    String url=textArr[i].split("\"")[3];
                    String reg="(?i).+?\\.(jpg|png|jpeg)";
                    if (url.matches(reg)) {
                        Log.d("hjq", "url=" + url);
                        urlArr.add(url);
                    }
                }
                final LSM model2=dataArr.get(curPos);
                if (model2.getCount()==0){
                    Date date=CommonUtil.stringToDate(time,FORMAT_DATE_All);
                    if (date==null){
                        if (curPos>0) {
                            time = dealTime(dataArr.get(curPos - 1).getTime(), false);
                        }else{
                            time = getNextTime(curPos);
                        }
                    }else{
                        if(curPos>0&&TextUtils.isEmpty(dataArr.get(curPos - 1).getTime())){
                            String preTime = dealTime(time, true);
                            dataArr.get(curPos -1).setTime(preTime);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemChanged(curPos-1);
                                }
                            });
                        }
                    }
                    model2.setTime(time);
                }
                model2.setCount(model2.getCount()+urlArr.size());
                model2.getUrlArr().addAll(urlArr);
                final int finalTotalPage = totalPage;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(curPos);
                        if (curPage< finalTotalPage){
                            getSize(curPos,model2.getArrarId(),curPage+1);
                        }else{
                            if (curPos<(end-start)) {
                               getSize(curPos + 1, dataArr.get(curPos + 1).getArrarId(), 1);
                            }
                        }
                    }
                });

            }

        }
    }
    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }
}
