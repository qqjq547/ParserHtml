package com.hjq.parserhtml.activity;

import android.content.Intent;
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
    }

    @OnClick({R.id.btn_lsm, R.id.btn_mm131, R.id.btn_web})
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
        }
    }
}
