package com.cc.rd.util;

public enum ErrorCodeEnum {

    NOT_AUTH(401, "need to login", "请先登陆"),

    USER_NOT_EXIST(10000, "User not exist", "用户不存在"),
    USER_EXIST(10001, "User exist", "用户已存在"),
    USER_PASSWORD_ERROR(10002, "Password is wrong", "密码错误"),
    USER_CODE_ERROR(10003, "Code is wrong", "验证码错误"),
    USER_LOCK(10004, "Account lock for 24 hours", "24小时后解锁"),
    TELPHONE_ERROR(10005, "Telphone is wrong", "手机号码无效"),
    NEED_LOGIN(10006, "need to login", "需要登陆"),
    UNLOCK(10007, "Telphone unlock", "该账号已解锁"),
    USER_PASSWORD_RULE(10008, "Password length", "密码长度为8～16"),

    INVALID_INPUT_PARAM(20000, "Params can not be null", "不允许为空"),
    CAPTCHA_CREATE_FAILED(20001, "Captcha image create failed", "验证码创建失败"),
    TOKEN_CREATE_FAILED(20002, "Token create failed", "token创建失败"),
    TELPHONE_CODE_ERROR(20003, "Telphone coade is wrong", "手机验证码错误"),
    SEND_CODE_LIMIT(20004, "Can not send code", "今日验证码已发三次"),
    SEND_CODE_FAILED(20005, "Sending code fail", "短信发送失败"),
    CAPTCHA_FREASH(20006, "Captcha image need to freash", "图片验证码已过期"),
    CAPTCHA_FAILED(20007, "Code is wrong", "图片验证码错误"),
    CAPTCHA_CODE_NOT_NULL(20008, "CAPTCHA_Code not allow null", "图片验证码不允许为空"),

    FILE_NOT_EXIST(30000, "File hash is empty", "文件不存在"),
    FILE_DUPLICATE(30001, "Duplicate file hash in database", "文件重复"),
    FILE_INVALID(30002,"File id is invalid", "文件Id不合法"),
    FILE_DATA_NOT_EXIST(30003, "File data is null", "文件数据不存在"),
    FILE_NAME_NOT_EXIST(30004, "File original name is null", "文件名不存在"),
    FILE_SAVE_FAILED(30005, "File save failed", "文件保存失败"),

    LABEL_NAME_EXIST(61010, "Label name exist", "标签名已经存在");

    private Integer code;

    private String eDesc;

    private String cDesc;

    ErrorCodeEnum(Integer code, String eDesc, String cDesc) {
        this.code = code;
        this.eDesc = eDesc;
        this.cDesc = cDesc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }


    public static ErrorCodeEnum findByCode(Integer code) {
        for (ErrorCodeEnum ld : values()) {
            if (ld.getCode().equals(code)) {
                return ld;
            }
        }
        return null;
    }
}
