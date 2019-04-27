package com.cc.rd.bean.request.user;

public class UserAddRequest extends TelphoneCodeRequest {

    private Integer authenticationSource;

    private String password;

    private String confirmPassword;

    public Integer getAuthenticationSource() {
        return authenticationSource;
    }

    public void setAuthenticationSource(Integer authenticationSource) {
        this.authenticationSource = authenticationSource;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
