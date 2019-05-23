package com.cc.rd.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cc.rd.R;

import butterknife.OnClick;

public class UpdateUserActivity extends AppCompatActivity {

    LinearLayout UserLayout;
    LinearLayout TelLayout;
    LinearLayout PwdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        init();
    }

    private void init() {
        UserLayout = (LinearLayout) findViewById(R.id.update_user_linear);
        TelLayout = (LinearLayout) findViewById(R.id.update_pwd_linear);
        PwdLayout = (LinearLayout) findViewById(R.id.update_pwd_linear);

        UserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateUserActivity.this, UpdateDetailActivity.class));
            }
        });

        TelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        PwdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }
}
