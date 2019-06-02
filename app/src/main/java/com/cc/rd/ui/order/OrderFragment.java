package com.cc.rd.ui.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cc.rd.R;
import com.cc.rd.adapter.RecycleViewAdapter;
import com.cc.rd.bean.OrderItem;
import com.cc.rd.bean.vo.OrderVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.enums.Constant;
import com.cc.rd.enums.GenderEnum;
import com.cc.rd.login.MainActivity;
import com.cc.rd.ui.UpdateUserActivity;
import com.cc.rd.util.ErrorCodeEnum;
import com.cc.rd.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecycleViewAdapter mRecycleViewAdapter;

    private Button btn;

    private List<OrderItem> mData;

    private ProgressDialog progDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_order, container, false);
    }

    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在加载...");
        progDialog.show();
    }

    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        progDialog = new ProgressDialog(getContext());

        showDialog();
        /*
        new Thread(new Runnable() {
            public void run() {
                getOrderList();
            }
        }).start();*/
        getOrderList();
        bindViews();
        mData = new ArrayList<>();
        //初始化线性布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mRecycleViewAdapter = new RecycleViewAdapter(mData);
        //设置适配器
        mRecyclerView.setAdapter(mRecycleViewAdapter);

        btn = (Button) getView().findViewById(R.id.order_add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateOrderActivity.class));
            }
        });
    }

    private void bindViews() {
        mRecyclerView = getView().findViewById(R.id.recycle_view);
    }

    private void initData() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId("xxx");
        orderItem.setTitle("主题:测试");
        orderItem.setNumber("人数：10");
        orderItem.setTime("起止时间:2019-05-24 14:00 ~ 2019-05-24 15:30");
        orderItem.setPlace("地点:湖南科技大学逸夫楼");
        mData.add(orderItem);
    }

    private void getOrderList() {
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
                dismissDialog();
                Log.i("result", htmlStr);
                try {
                    JSONObject object = new JSONObject(htmlStr);
                    int code = object.getInt("code");
                    if (code == 200) {
                        //获取
                        Gson gson = new Gson();
                        OrderVO orderVO = gson.fromJson(object.getString("result"), OrderVO.class);
                        List<OrderItem> orderItemList = orderVO.getOrderList();
                        mData.addAll(orderItemList);
                        //initData();
                    } else if (code == ErrorCodeEnum.NOT_AUTH.getCode()) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
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
