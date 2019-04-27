package com.cc.rd.bean.request.user;

public class CaptchaCheckRequest {

    private String code;

    private String token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
