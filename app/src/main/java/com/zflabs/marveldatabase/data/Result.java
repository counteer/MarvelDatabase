package com.zflabs.marveldatabase.data;

public class Result<T> {
    Data<T> data;
    int code;
    String status;
    String copyright;
    String attributionText;
    String attributionHTML;
    String etag;

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }
}
