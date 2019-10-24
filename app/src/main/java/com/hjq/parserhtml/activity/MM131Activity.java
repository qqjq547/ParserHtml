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
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.parserhtml.CommonUtil;
import com.hjq.parserhtml.adapter.LSMAdapter;
import com.hjq.parserhtml.adapter.MM131Adapter;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.RxUtil;
import com.hjq.parserhtml.http.retrofit.ApiCallback;
import com.hjq.parserhtml.http.retrofit.ApiClient;
import com.hjq.parserhtml.model.MM131;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MM131Activity extends AppCompatActivity {
    String type;
    String basedir = Environment.getExternalStorageDirectory() + File.separator + "mm131" + File.separator;
    ArrayList<MM131> dataArr = new ArrayList<>();
    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    MM131Adapter adapter;
    int start = 0;
    int end = 0;
    @BindView(R.id.ev_start)
    EditText evStart;
    @BindView(R.id.ev_end)
    EditText evEnd;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    private CompositeSubscription mCompositeSubscription;
    String curUrl;
    int curPos;
    int index;
    String downDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mm131);
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("type");
        if (!new File(basedir).exists()) {
            new File(basedir).mkdirs();
        }
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setBlockNetworkImage(true);
        wvContent.addJavascriptInterface(new MM131Activity.Handler(),"handler");
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                downPic(curPos, index);
                 if (index < dataArr.get(curPos).getCount()) {
                     loadUrl(curPos, index + 1);
                }else{
                    if (curPos<(dataArr.size()-1)) {
                        loadUrl(curPos+1, 1);
                    }else {
                        Toast.makeText(MM131Activity.this,"下载完成啦",Toast.LENGTH_SHORT).show();
                        String startStr=String.valueOf(start+10);
                        String endStr=String.valueOf(start+19);
                        evStart.setText(startStr);
                        evEnd.setText(endStr);
                    }
                }
             
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new MM131Adapter(this,dataArr);
        rvList.setAdapter(adapter);

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

    public void getSize(final int id) {
        addSubscription(RxUtil.createHttpObservable(ApiClient.getInstance1().getApiStores1().getSize(type,id)).subscribe(new ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String tag = "<span class=\"page-ch\">��";
                int index = data.indexOf(tag);
                int start = index + tag.length();
                String endStr = data.substring(start);
                String[] array = endStr.split("ҳ</span>");
                Log.e("hjq","array="+array[0]);
                int count = Integer.parseInt(array[0]);
                String title=data.split("<title>")[1].split("</title>")[0];
                Log.e("hjq","title="+title);
                String time=data.split("<div class=\"content-msg\">����ʱ�䣺")[1].split(" <a href")[0];
                MM131 model=new MM131(id,title,count, 0);
                model.setTime(time);
                dataArr.add(model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                if (id < end) {
                    getSize(id + 1);
                } else {
                    int count=0;
                    for (MM131 mm:dataArr){
                        count=count+mm.getCount();
                    }
                    tvTotal.setText("共"+count+"个文件");
                }

            }
        }));

    }
    public void downPic(final int position, final int index) {
        String url = "https://img1.mm131.me/pic/" + dataArr.get(position).getArrarId() + "/" +index+ ".jpg";
        String referer="https://m.mm131.net/"+type+"/"+dataArr.get(position).getArrarId()+".html";
        addSubscription(RxUtil.createBmpObservable(ApiClient.getInstance().getApiStores1().downloadPicFromNet(referer,url)).subscribe(new ApiCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap data) {
                MM131 model = dataArr.get(position);
                saveBitmap(data, model.getArrarId() + "_" + CommonUtil.unitFormat(index) + ".jpg");
                model.setDownNum(model.getDownNum() + 1);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
//                if (index < dataArr.get(position).getCount()) {
//                    downPic(position, index + 1);
//                }else{
//                    if (position<(dataArr.size()-1)) {
//                        downPic(position+1, 1);
//                    }
//                }
            }
        }));

    }
    public void saveBitmap(Bitmap bm, final String fileName) {
        File f = new File(downDir, fileName);
        if (f.exists()) {
            Log.i("hjq", "已经存在:" + f.getAbsolutePath());
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

    @OnClick({R.id.btn_get, R.id.btn_down})
    public void onViewClicked(View view) {
        String startStr = evStart.getText().toString().trim();
        String endStr = evEnd.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_get:
                dataArr.clear();
                adapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(startStr) || TextUtils.isEmpty(endStr)) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    start = Integer.parseInt(startStr);
                    end = Integer.parseInt(endStr);
                    if (start>end) {
                        Toast.makeText(this, "大小不一致", Toast.LENGTH_SHORT).show();
                    }else{
                        getSize(start);
                    }
                }
                break;
            case R.id.btn_down:
                downDir=basedir+(start-start%100)+File.separator+startStr+"-"+endStr+File.separator;
                if (!new File(downDir).exists()) {
                    new File(downDir).mkdirs();
                }
                loadUrl(0,1);
                break;
        }

    }
    public void loadUrl(int pos,int index){
        MM131 model = dataArr.get(pos);
        this.curPos=pos;
        this.index=index;
        if (index>1) {
            wvContent.loadUrl("https://m.mm131.com/" + type + "/" + model.getArrarId() + "_" + index + ".html");
        }else {
            wvContent.loadUrl("https://m.mm131.com/" + type + "/" + model.getArrarId()  + ".html");
        }
    }

    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }

    public class Handler {
        @JavascriptInterface
        public void show(String data) {
            
        }
    }
}
//https://img1.mm131.me/pic/5096/51.jpg
//https://img1.mm131.me/pic/5095/2.jpg

//https://m.mm131.net/xinggan/5096.html
//https://m.mm131.net/xinggan/5095.html
//User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36
