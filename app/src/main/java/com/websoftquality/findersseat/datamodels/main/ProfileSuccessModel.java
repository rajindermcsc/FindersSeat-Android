package com.websoftquality.findersseat.datamodels.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileSuccessModel {
    @SerializedName("data")
    @Expose
    private List<ProfileSuccessModel> data = null;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("errors")
    @Expose
    private ErrorsProfile errors;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ProfileSuccessModel> getData() {
        return data;
    }

    public void setData(List<ProfileSuccessModel> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ErrorsProfile getErrors() {
        return errors;
    }

    public void setErrors(ErrorsProfile errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
