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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MM131Activity extends AppCompatActivity {
    String dir = Environment.getExternalStorageDirectory() + File.separator + "mm131" + File.separator;
    ArrayList<Model> dataArr = new ArrayList<>();

    @BindView(R.id.listview)
    ListView listview;
    MyAdapter adapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mm131);
        ButterKnife.bind(this);
        if (!new File(dir).exists()) {
            new File(dir).mkdir();
        }
        adapter = new MyAdapter(dataArr);
        listview.setAdapter(adapter);

    }

    public void startDown() {
        for (Model model : dataArr) {
            downPic(model.getArrarId(), 1);
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

    public void getSize(final int id) {
        addSubscription(RxUtil.createHttpObservable(ApiClient.getInstance1().getApiStores1().getSize(id)).subscribe(new ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String tag = "<span class=\"rw\">1/";
                int index = data.indexOf(tag);
                int start = index + tag.length();
                String endStr = data.substring(start);
                String[] array = endStr.split("ҳ</span>");
                int count = Integer.parseInt(array[0]);
                dataArr.add(new Model(id, count, 0));
                adapter.notifyDataSetChanged();
                Log.d("hjq", "count=" + count);
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
                    for (Model mm:dataArr){
                        count=count+mm.getCount();
                    }
                    tvTotal.setText("共"+count+"个文件");
                }

            }
        }));

    }

    public void downPic(final int arrayId, final int index) {
        String url = "http://img1.mm131.com/pic/" + arrayId + "/" + index + ".jpg";
        addSubscription(RxUtil.createBmpObservable(ApiClient.getInstance().getApiStores().downloadPicFromNet(url)).subscribe(new ApiCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap data) {
                saveBitmap(data, arrayId + "_" + index + ".png");
                Model model = dataArr.get(arrayId - start);
                model.setDownNum(model.getDownNum() + 1);
                adapter.notifyDataSetChanged();
                if (index < model.getCount()) {
                    downPic(arrayId, index + 1);
                }
            }

            @Override
            public void onFailure(String exception) {

            }

            @Override
            public void onFinish() {

            }
        }));

    }
    public void saveBitmap(final Bitmap bm, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                }

            }
        }).start();

    }

    @OnClick({R.id.btn_get, R.id.btn_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                String startStr = evStart.getText().toString().trim();
                String endStr = evEnd.getText().toString().trim();
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
