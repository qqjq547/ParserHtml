package com.hjq.parserhtml.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.hjq.parserhtml.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_lsm)
    Button btnLsm;
    @BindView(R.id.btn_mm131)
    Button btnMm131;
    @BindView(R.id.btn_web)
    Button btnWeb;
    @BindView(R.id.spinner)
    Spinner spinner;
    String curType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        spinner.setSelection(0);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @OnClick({R.id.btn_lsm, R.id.btn_mm131, R.id.btn_web,R.id.btn_web1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_lsm:
                startActivity(new Intent(this, LSMActivity.class));
                break;
            case R.id.btn_mm131:
                curType=(String) spinner.getSelectedItem();
                startActivity(new Intent(this, MM131Activity.class).putExtra("type",curType));
                break;
            case R.id.btn_web:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case R.id.btn_web1:
                startActivity(new Intent(this, Web1Activity.class));
                break;
        }
    }
}
