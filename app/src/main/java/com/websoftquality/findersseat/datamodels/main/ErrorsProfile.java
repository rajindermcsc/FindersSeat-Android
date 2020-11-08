package com.websoftquality.findersseat.datamodels.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorsProfile {

    @SerializedName("error_id")
    @Expose
    private String errorId;
    @SerializedName("error_text")
    @Expose
    private String errorText;

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
