package com.example.codesteembullyguard.models;

public class LoginResponseModel {

    boolean success;
    int status;
    LoginResponseSubModel result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LoginResponseSubModel getResult() {
        return result;
    }

    public void setResult(LoginResponseSubModel result) {
        this.result = result;
    }
}
