package com.cc.rd.ui.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.cc.rd.R;
import com.cc.rd.bean.PickerViewData;
import com.cc.rd.bean.TimeBean;
import com.cc.rd.bean.request.order.OrderAddRequest;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.custom.ResponseConverterFactory;
import com.cc.rd.enums.Constant;
import com.cc.rd.login.MainActivity;
import com.cc.rd.net.APIService;
import com.cc.rd.ui.HomeActivity;
import com.cc.rd.ui.map.MapActivity;
import com.cc.rd.util.DateTimeUtils;
import com.cc.rd.util.ErrorCodeEnum;
import com.cc.rd.util.ParamUtils;
import com.cc.rd.util.Result;
import com.cc.rd.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateOrderActivity extends AppCompatActivity {

    LinearLayout selectOrderStart;

    TextView orderStartTime;

    LinearLayout selectOrderEnd;

    TextView orderEndTime;

    EditText editOrderTitle;

    EditText editOrderContent;

    CheckBox orderPublic;

    LinearLayout getSelectOrderAddress;

    TextView orderAddress;

    Double orderLongitude;

    Double orderLatitude;

    Integer orderAdcode;

    Integer orderCityCode;

    private ProgressDialog progDialog = null;

    APIService apiService;

    private ArrayList<TimeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    TimePickerView pvTime;
    OptionsPickerView pvOptions;
    //View vMasker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        init();
    }

    public void init() {

        orderStartTime = (TextView) findViewById(R.id.order_start_time);
        orderEndTime = (TextView) findViewById(R.id.order_end_time);
        orderAddress = (TextView) findViewById(R.id.order_address);
        editOrderTitle = (EditText) findViewById(R.id.edit_order_title);
        editOrderContent = (EditText) findViewById(R.id.edit_order_content);
        orderPublic = (CheckBox) findViewById(R.id.order_public);

        selectOrderStart = (LinearLayout) findViewById(R.id.select_order_start);
        selectOrderEnd = (LinearLayout) findViewById(R.id.select_order_end);
        getSelectOrderAddress = (LinearLayout) findViewById(R.id.select_order_address);

        orderStartTime.setText(DateTimeUtils.getMil2mmTimeFormat(String.valueOf(DateTimeUtils.utcNow())));
        orderEndTime.setText(DateTimeUtils.getMil2mmTimeFormat(String.valueOf(DateTimeUtils.utcNow() + DateTimeUtils.ONE_HOUR_IN_MILLS)));

        progDialog = new ProgressDialog(this);

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                orderStartTime.setText(getTime(date));
            }
        });

        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项1
        options1Items.add(new TimeBean("现在"));
        options1Items.add(new TimeBean("今天"));
        options1Items.add(new TimeBean("明天"));
        options1Items.add(new TimeBean("后天"));

        //选项 1 2
        ArrayList<String> options2Items_01=new ArrayList<>();
        options2Items_01.add("--");

        //22
        ArrayList<String> options2Items_02=getTodayHourData();
        //32
        ArrayList<String> options2Items_03=getHourData();
        //32
        ArrayList<String> options2Items_04=getHourData();

        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
        options2Items.add(options2Items_04);

        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_04 = new ArrayList<>();

        ArrayList<IPickerViewData> options3Items_01_01=new ArrayList<>();
        options3Items_01_01.add(new PickerViewData("--"));
        options3Items_01.add(options3Items_01_01);
        options3Items_02 =getmD2();
        options3Items_03 =getmD();
        options3Items_04 =getmD();

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);
        options3Items.add(options3Items_04);

        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);

        pvOptions.setTitle(" ");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String i = options1Items.get(options1).getPickerViewText();
                String tx = "";
                if (i.equals("现在")) {
                    tx = getTime(new Date());
                } else {
                    if (i.equals("今天")) {
                        tx = getTodayTime(new Date());
                        String hour = options2Items.get(options1).get(option2);
                        String[] hours = hour.split("点");
                        tx +=  " " + hours[0] + ":";
                        String min = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                        String[] mins = min.split("分");
                        tx += mins[0];
                    } else {
                        if (i.equals("明天")) {
                            tx = DateTimeUtils.getMil2ddTimeFormat(String.valueOf(DateTimeUtils.utcNow() + DateTimeUtils.ONE_DAY_IN_MILLS));
                            String hour = options2Items.get(options1).get(option2);
                            String[] hours = hour.split("点");
                            tx +=  " " + hours[0] + ":";
                            String min = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                            String[] mins = min.split("分");
                            tx += mins[0];
                        } else {
                            if (i.equals("后天")) {
                                tx = DateTimeUtils.getMil2ddTimeFormat(String.valueOf(DateTimeUtils.utcNow() + DateTimeUtils.ONE_DAY_IN_MILLS * 2));
                                String hour = options2Items.get(options1).get(option2);
                                String[] hours = hour.split("点");
                                tx +=  " " + hours[0] + ":";
                                String min = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                                String[] mins = min.split("分");
                                tx += mins[0];
                            }
                        }
                    }
                }
                orderEndTime.setText(tx);
                //vMasker.setVisibility(View.GONE);
            }
        });

        selectOrderStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

        selectOrderEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

        orderPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

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
                    orderLongitude = data.getDoubleExtra("longitude",6);
                    orderLatitude = data.getDoubleExtra("latitude", 6);
                    orderAdcode = Integer.valueOf(data.getStringExtra("adcode") == null ? "000000" : data.getStringExtra("adcode"));
                    orderCityCode = Integer.valueOf(data.getStringExtra("area") == null ? "000000" : data.getStringExtra("area"));
                    orderAddress.setText(address);
                }
            }
        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getTodayTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pvOptions.isShowing()||pvTime.isShowing()){
                pvOptions.dismiss();
                pvTime.dismiss();
                return true;
            }
            if(pvTime.isShowing()){
                pvTime.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 今天 点
     */
    private ArrayList<String> getTodayHourData(){
        int max =currentHour();
        if (max<23&&currentMin()>45){
            max=max+1;
        }
        ArrayList<String> lists=new ArrayList<>();
        for (int i=max;i<24;i++){
            if (i < 10) {
                lists.add("0" + i +"点");
            } else {
                lists.add(i +"点");
            }
        }
        return lists;
    }

    /**
     * 明天 后天 点
     */
    private ArrayList<String> getHourData(){
        ArrayList<String> lists=new ArrayList<>();
        for (int i=0;i<24;i++){
            if (i < 10) {
                lists.add("0" + i +"点");
            } else {
                lists.add(i +"点");
            }
        }
        return lists;
    }

    /**
     * 明天 后天  分
     */
    private ArrayList<IPickerViewData> getMinData(){
        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=0;i<6;i++){
            if (i == 0) {
                dataArrayList.add(new PickerViewData("00分"));
            } else {
                dataArrayList.add(new PickerViewData((i*10)+"分"));
            }
        }
        return dataArrayList;
    }
    /**
     * 明天 后天
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD(){
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<24;i++){
            d.add(getMinData());
        }
        return d;
    }

    /**
     * 明天 后天  2222
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD2(){
        //14
        int max =currentHour();
        if (currentMin()>45){
            max=max+1;
        }
        int value =24-max;
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<value;i++){
            if (i==0){
                d.add(getTodyMinData());
            }else {
                d.add(getMinData());
            }

        }
        return d;
    }

    /**
     * 明天 后天  分2222
     */
    private ArrayList<IPickerViewData> getTodyMinData(){

        int min = currentMin();
        int current=0;
        if (min>35&&min<=45){
            current =0;
        }else if (min>45&&min<=55){
            current=1;
        } else if (min>55){
            current=2;
        }else if (min<=5){
            current=2;
        }else if (min>5&&min<=15){
            current=3;
        }else if (min>15&&min<=25){
            current=4;
        }else if (min>25&&min<=35){
            current=5;
        }
        int max =currentHour();
        if (max>23&& min>35){
            current=5;
        }

        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=current;i<6;i++){
            if (i == 0) {
                dataArrayList.add(new PickerViewData("00分"));
            } else {
                dataArrayList.add(new PickerViewData((i*10)+"分"));
            }
        }
        return dataArrayList;
    }

    private int currentMin(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }

    private int currentHour(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
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

    public void save(View v) {
        String title = editOrderTitle.getText().toString();
        String content = editOrderContent.getText().toString();
        String place = orderAddress.getText().toString();
        String s = orderStartTime.getText().toString();
        String e = orderEndTime.getText().toString();

        if (ParamUtils.isEmpty(title)) {
            Toast.makeText(this, "主题不允许为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (ParamUtils.isEmpty(content)) {
            Toast.makeText(this, "内容不允许为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (place.equals("选择地点")) {
            Toast.makeText(this, "请选择一个地点", Toast.LENGTH_LONG).show();
            return;
        }

        OrderAddRequest request = new OrderAddRequest();
        request.setOrderTitle(title);
        request.setOrderContent(content);
        request.setOrderStarts(DateTimeUtils.timemmToStamp(s));
        request.setOrderEnds(DateTimeUtils.timemmToStamp(e));
        if (orderPublic.isChecked()) {
            request.setOrderType(0);
        } else {
            request.setOrderType(1);
        }
        request.setOrderAddress(place);
        request.setOrderLongitude(orderLongitude);
        request.setOrderLatitude(orderLatitude);
        request.setOrderAdcode(orderAdcode);
        request.setOrderCityCode(orderCityCode);

        showDialog();
        //service(request);

        new Thread(new Runnable() {
            public void run() {
                addOrder(request);
            }
        }).start();

    }

    private void addOrder(OrderAddRequest orderAddRequest) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        Gson gson = new Gson();
        String jsonStr =  gson.toJson(orderAddRequest);
        RequestBody body = RequestBody.create(JSON, jsonStr);

        //拿到body的构建器
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        builder.add("orderTitle", orderAddRequest.getOrderTitle());
        //拿到requestBody
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(Constant.url + "/orders")
                .post(body)
                .addHeader("Authorization", SharedPreferencesUtils.getToken())
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dismissDialog();
                String htmlStr = response.body().string();
                Log.i("result", htmlStr);
                try {
                    JSONObject object = new JSONObject(htmlStr);
                    int code = object.getInt("code");
                    if (code == 200) {
                        //获取
                        /*
                        Gson gson = new Gson();
                        UserLoginVo userLoginVo = gson.fromJson(object.getString("result"), UserLoginVo.class);
                        */
                        //设值
                        Toast.makeText(CreateOrderActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateOrderActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (code == ErrorCodeEnum.NOT_AUTH.getCode()) {
                        Intent i = new Intent(CreateOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void service(OrderAddRequest request) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                //设置Header
                .addInterceptor(getHeaderInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.url)
                .client(client)
                //注册自定义的工厂类
                .addConverterFactory(ResponseConverterFactory.create())
                //设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        APIService userService = retrofit.create(APIService.class);

        userService.addOrder(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Result userLoginResponse) {
                        Toast.makeText(CreateOrderActivity.this, "创建成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                if (ParamUtils.isEmpty(SharedPreferencesUtils.getToken())) {
                    Request.Builder requestBuilder = original.newBuilder();
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                } else {
                    Request.Builder requestBuilder = original.newBuilder()
                            //添加Token
                            .header("Authorization", SharedPreferencesUtils.getToken());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }
        };

    }

}
