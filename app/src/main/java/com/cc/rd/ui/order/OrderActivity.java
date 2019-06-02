package com.cc.rd.ui.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cc.rd.R;
import com.cc.rd.adapter.RecycleViewAdapter;
import com.cc.rd.bean.OrderItem;
import com.cc.rd.bean.vo.OrderVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.enums.Constant;
import com.cc.rd.login.MainActivity;
import com.cc.rd.util.ErrorCodeEnum;
import com.cc.rd.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecycleViewAdapter mRecycleViewAdapter;

    private List<OrderItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        bindViews();
        initData();
        //初始化线性布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mRecycleViewAdapter = new RecycleViewAdapter(mData);
        //设置适配器
        mRecyclerView.setAdapter(mRecycleViewAdapter);
    }

    private void bindViews() {
        mRecyclerView = findViewById(R.id.recycle_view);
    }

    private void initData() {
        mData = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId("xxx");
        orderItem.setTitle("主题:测试");
        orderItem.setNumber("人数：10");
        orderItem.setTime("起止时间:2019-05-24 14:00 ~ 2019-05-24 15:30");
        orderItem.setPlace("地点:湖南科技大学逸夫楼");
        mData.add(orderItem);
        mData.add(orderItem);
    }

    public void save(View v) {
        startActivity(new Intent(this, CreateOrderActivity.class));
    }

    private void getLoginUser() {
        Request request = new Request.Builder()
                .url(Constant.url + "/orders")
                .get()
                .addHeader("Authorization", SharedPreferencesUtils.getToken())
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                Log.i("result", htmlStr);
                try {
                    JSONObject object = new JSONObject(htmlStr);
                    int code = object.getInt("code");
                    if (code == 200) {
                        //获取
                        Gson gson = new Gson();
                        OrderVO orderVO = gson.fromJson(object.getString("result"), OrderVO.class);
                        //设值
                    } else if (code == ErrorCodeEnum.NOT_AUTH.getCode()) {
                        Intent i = new Intent(OrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
