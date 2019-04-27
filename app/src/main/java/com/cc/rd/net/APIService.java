package com.cc.rd.net;

import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.request.user.UserUpdateRequest;
import com.cc.rd.bean.vo.CaptchaVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.bean.request.user.LoginRequest;
import com.cc.rd.bean.request.user.ResetPasswordRequest;
import com.cc.rd.bean.request.user.TelphoneCodeRequest;
import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    /**
     * 登陆
     * @return
     */
    @POST("/accounts/login")
    Flowable<JSONResult<UserLoginVo>> login(@Body LoginRequest loginRequest);

    @POST("/accounts/newsendsms/{telphone}")
    Flowable<Result> sendNewCode(@Path("telphone") String telphone);

    @POST("/accounts/oldsendsms/{telphone}")
    Flowable<Result> sendOldCode(@Path("telphone") String telphone);

    @POST("/accounts/checksms")
    Flowable<Result> checkSms(@Body TelphoneCodeRequest telphoneCodeRequest);

    @POST("/accounts/passwords/reset")
    Flowable<Result> resetPwd(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("/accounts/registers")
    Flowable<Result> registerUser(@Body UserAddRequest userAddRequest);

    /**
     * 图片验证码
     */
    @GET("/captcha")
    Flowable<JSONResult<CaptchaVO>> getCaptcha();

    /**
     * 修改用户信息
     * @param request
     * @return
     */
    @PUT("/users")
    Flowable<Result> changeUserImage(@Body UserUpdateRequest request);


}
