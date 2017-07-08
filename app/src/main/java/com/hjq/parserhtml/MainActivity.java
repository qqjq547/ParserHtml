package com.hjq.parserhtml;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_lsm)
    Button btnLsm;
    @BindView(R.id.btn_mm131)
    Button btnMm131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_lsm, R.id.btn_mm131})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_lsm:
                startActivity(new Intent(this,LSMActivity.class));
                break;
            case R.id.btn_mm131:
                startActivity(new Intent(this,MM131Activity.class));
                break;
        }
    }
}
