package com.cc.rd.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.callback.PhotoCallBack;
import com.cc.rd.mvp.contract.login.UpdateContract;
import com.cc.rd.mvp.presenter.user.UpdatePresenter;
import com.cc.rd.util.FileUtils;
import com.cc.rd.util.Result;
import com.cc.rd.view.AlertView;
import com.spark.submitbutton.SubmitButton;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OnceActivity extends BaseMvpActivity<UpdatePresenter> implements UpdateContract.View  {

    @BindView(R.id.iv_avater)
    private CircleImageView ivAvater;

    @BindView(R.id.genderRadioGroup)
    private RadioGroup radioGroup;

    private String gender;

    public PhotoCallBack callBack;
    public String path = "";
    public Uri photoUri;
    private File file;

    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ivAvater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvater();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();//获得按下单选框的ID,并保存在radioButtonId
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)findViewById(radioButtonId);//根据ID将RB和单选框绑定在一起
                Toast.makeText(OnceActivity.this, rb.getText().toString(), Toast.LENGTH_LONG).show();
                gender = rb.getText().toString();
            }
        });
    }

    public void onceBtn(View view) {
        Toast.makeText(OnceActivity.this, gender, Toast.LENGTH_LONG).show();
    }

    private void changeAvater(){
        callBack = new PhotoCallBack() {
            @Override
            public void doSuccess(String path) {
                //将图片传给服务器
//                File file = new File(path);
//                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Builder builder = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)//表单类型
//                        .addFormDataPart("id", SaveUserInfo.getUid());
//                builder.addFormDataPart("head_portrait", file.getName(), imageBody);
//                List<MultipartBody.Part> partList=builder.build().parts();
//                mPresenter.startUpdateHeadRequest(partList,SaveUserInfo.getUid());
            }

            @Override
            public void doError() {

            }
        };
        comfireImgSelection(getApplicationContext(), ivAvater);
    }

    // 拍照
    public void comfireImgSelection(Context context, CircleImageView my_info) {
        ivAvater = my_info;
        new AlertView(null, null, "取消", null, new String[]{"从手机相册选择", "拍照"}, this, AlertView.Style.ActionSheet,
                (o, position) -> {
                    if (position == 0) {
                        // 从相册中选择
                        if (checkPermission(READ_EXTERNAL_STORAGE)) {
                            Intent i = new Intent(
                                    // 相册
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        } else {//申请拍照权限和读取权限
                            startRequestrReadPermision();
                        }
                    } else if (position == 1) {
                        // 拍照
                        if (checkPermission(CAMERA_PERMISSION)) {
                            photo();
                        } else {//申请拍照权限和读取权限
                            startRequestPhotoPermision();
                        }
                    }
                }).show();
    }

    private void startRequestrReadPermision() {
        RxPermissions.getInstance(OnceActivity.this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)//多个权限用","隔开
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            Intent i = new Intent(
                                    // 相册
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            return;
                        }
                    }
                });
    }

    private void startRequestPhotoPermision() {
        //请求多个权限
        RxPermissions.getInstance(OnceActivity.this)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)//多个权限用","隔开
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            photo();
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            return;
                        }
                    }
                });
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.e("checkPermission", "PERMISSION_GRANTED" + ContextCompat.checkSelfPermission(this, permission));
                return true;
            } else {
                Log.e("checkPermission", "PERMISSION_DENIED" + ContextCompat.checkSelfPermission(this, permission));
                return false;
            }
        } else {
            Log.e("checkPermission", "M以下" + ContextCompat.checkSelfPermission(this, permission));
            return true;
        }
    }

    public void photo() {

        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
            file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 有sd卡，是否有myImage文件夹
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 是否有headImg文件
                long l = System.currentTimeMillis();
                file = new File(sdcardPathDir + l + ".JPEG");
            }
            if (file != null) {
                path = file.getPath();

                photoUri = Uri.fromFile(file);
                if (Build.VERSION.SDK_INT >= 24) {
                    photoUri = FileProvider.getUriForFile(this,"com.barnettwong.changeavaterview.fileProvider", file);
                } else {
                    photoUri = Uri.fromFile(file);
                }
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (file != null && file.exists())
                    startPhotoZoom(photoUri);
                break;
            case RESULT_LOAD_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    if (path != null && path.length() != 0) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //给头像设置图片源
                        ivAvater.setImageBitmap(bitmap);
                        if (callBack != null)
                            callBack.doSuccess(path);
                    }
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String address = sDateFormat.format(new Date());
            if (!FileUtils.isFileExist("")) {
                FileUtils.createSDDir("");
            }

            Uri imageUri = Uri.parse("file:///sdcard/formats/" + address + ".JPEG");
            final Intent intent = new Intent("com.android.camera.action.CROP");

            // 照片URL地址
            intent.setDataAndType(uri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            // 输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // 不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            intent.putExtra("fileurl", FileUtils.SDPATH + address + ".JPEG");
            path = FileUtils.SDPATH + address + ".JPEG";
            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mPresenter = new UpdatePresenter();
        mPresenter.attachView(this);

    }

    @Override
    public void onSuccess(Result result) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
