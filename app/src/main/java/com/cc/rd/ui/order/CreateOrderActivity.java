package com.cc.rd.ui.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.cc.rd.R;
import com.cc.rd.ui.map.MapActivity;

import butterknife.BindView;

public class CreateOrderActivity extends AppCompatActivity {

    LinearLayout selectOrderStart;

    @BindView(R.id.order_start_time)
    TextView orderStartTime;

    LinearLayout selectOrderEnd;

    @BindView(R.id.order_end_time)
    TextView orderEndTime;

    @BindView(R.id.edit_order_title)
    EditText editOrderTitle;

    @BindView(R.id.edit_order_content)
    EditText getEditOrderContent;

    CheckBox orderPublic;

    LinearLayout getSelectOrderAddress;

    TextView orderAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        init();
    }

    public void init() {

        orderAddress = (TextView) findViewById(R.id.order_address);
        orderPublic = (CheckBox) findViewById(R.id.order_public);

        selectOrderStart = (LinearLayout) findViewById(R.id.select_order_start);
        selectOrderEnd = (LinearLayout) findViewById(R.id.select_order_end);
        getSelectOrderAddress = (LinearLayout) findViewById(R.id.select_order_address);

        selectOrderStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        selectOrderEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        orderPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }else{

                }
            }
        });

        getSelectOrderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateOrderActivity.this, MapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                if (resultCode == 1) {
                    String address = data.getStringExtra("address");
                    data.getDoubleExtra("longitude",6);
                    data.getDoubleExtra("latitude", 6);
                    orderAddress.setText(address);
                }
            }
        }
    }

    public void save(View v) {
        String title = editOrderTitle.getText().toString();
        String content = getEditOrderContent.getText().toString();
        if (TextUtils.isEmpty(title)) {

        } else {
            // select from contact list

        }
    }

    public void back(View view) {
        finish();
    }
}
