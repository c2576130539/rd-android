package com.cc.rd.bean;

public class JSONResult<T> {
    /**
     * status : 1
     * msg : 获取成功
     * result : {} 对象
     */
    private int code;
    private boolean success;
    private T result;
    private T error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
