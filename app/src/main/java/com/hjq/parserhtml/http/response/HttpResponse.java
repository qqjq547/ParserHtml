package com.hjq.parserhtml.http.response;

/**
 * Created by codeest on 16/8/28.
 */

public class HttpResponse<T> {

    private String statusCode;
    private String message;
    private T data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
