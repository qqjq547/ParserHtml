package com.hjq.parserhtml;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.parserhtml.http.retrofit.ApiCallback;
import com.hjq.parserhtml.http.retrofit.ApiClient;
import com.hjq.parserhtml.http.retrofit.ApiClient2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LSMActivity extends AppCompatActivity {
    String dir = Environment.getExternalStorageDirectory() + File.separator + "lesmao" + File.separator;
    ArrayList<Model2> dataArr = new ArrayList<>();
    ArrayList<Integer> typeArr = new ArrayList<>();
    @BindView(R.id.listview)
    ListView listview;
    MyAdapter2 adapter;
    int pageNum = 0;
    int count = 0;
    @BindView(R.id.ev_page)
    EditText evPage;
    @BindView(R.id.ev_count)
    EditText evCount;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsm);
        ButterKnife.bind(this);
        if (!new File(dir).exists()) {
            new File(dir).mkdir();
        }
        adapter = new MyAdapter2(dataArr);
        listview.setAdapter(adapter);

    }

    public void startDown() {
        Model2 model=dataArr.get(0);
        downPic(0, 1, model.getUrlArr().get(0));
//        for (int i=0;i<dataArr.size();i++) {
//            Model2 model=dataArr.get(i);
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
    public void getPageData(final int pageNum,final int count) {
        addSubscription(RxUtil.createHttpObservable(ApiClient2.getInstance1().getApiStores1().getPageData(pageNum)).subscribe(new ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String tag = "<div class=\"photo\"><a href=\"http://www.lesmao.com/thread-";
                String[] textArr=data.split(tag);
                List<String> urlArr=new ArrayList<String>();
                for (int i=1;i<count+1;i++){
                    String typeid=textArr[i].split("-")[0];
                    Log.d("hjq", "typeid=" + typeid);
                    typeArr.add(Integer.parseInt(typeid));
                }

            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                for (int i=0;i<typeArr.size();i++){
                      dataArr.add(new Model2(typeArr.get(i),0,0,new ArrayList<String>()));
                      getSize(i,typeArr.get(i),1);
                 }
            }
        }));

    }

    public void getSize(final int position, final int id, final int page) {
        addSubscription(RxUtil.createHttpObservable(ApiClient2.getInstance1().getApiStores1().getSize2(id,page)).subscribe(new ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String tag = "\" /></a></li>";
                String[] textArr=data.split(tag);
                List<String> urlArr=new ArrayList<String>();
                for (int i=0;i<textArr.length-1;i++){
                    String[] srcArr=textArr[i].split("src=\"");
                    String url=srcArr[srcArr.length-1];
                    Log.d("hjq", "url=" + url);
                    String reg="(?i).+?\\.(jpg|png|jpeg)";
                    if (url.matches(reg)) {
                        urlArr.add(url);
                    }
                }
                Model2 model2=dataArr.get(position);
                model2.setCount(model2.getCount()+urlArr.size());
                model2.getUrlArr().addAll(urlArr);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                if (page < 5) {
                    getSize(position,id,page + 1);
                } else {
//                    int count=0;
//                    for (Model2 mm:dataArr){
//                        count=count+mm.getCount();
//                    }
//                    tvTotal.setText("共"+count+"个文件");
                }

            }
        }));

    }

    public void downPic(final int position, final int index,String url) {
        addSubscription(RxUtil.createCompressBmpObservable(ApiClient.getInstance().getApiStores().downloadPicFromNet(url)).subscribe(new ApiCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap data) {
                Model2 model = dataArr.get(position);
                saveBitmap(data, model.getArrarId() + "_" + index + ".png");
                model.setDownNum(model.getDownNum() + 1);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {
                Model2 model = dataArr.get(position);
                if (index < model.getCount()) {
                    downPic(position, index + 1,model.getUrlArr().get(index));
                }else{
                    if (position<(dataArr.size()-1)) {
                        downPic(position + 1, 1, model.getUrlArr().get(0));
                    }
                }
            }
        }));

    }

    public void saveBitmap(Bitmap bm, final String fileName) {
        File f = new File(dir, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
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
        switch (view.getId()) {
            case R.id.btn_get:
                String pageNumStr = evPage.getText().toString().trim();
                String countStr = evCount.getText().toString().trim();
                if (TextUtils.isEmpty(pageNumStr) || TextUtils.isEmpty(countStr)) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    pageNum=Integer.parseInt(pageNumStr);
                    count=Integer.parseInt(countStr);
                    getPageData(pageNum,count);

                }
                break;
            case R.id.btn_down:
                startDown();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }
}
