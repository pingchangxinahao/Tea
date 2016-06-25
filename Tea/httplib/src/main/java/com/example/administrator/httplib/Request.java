package com.example.administrator.httplib;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
abstract public class Request<T> {
    private String url;
    private Method method;
    private Callback callback;

    public Request(String url, Method method,Callback callback ) {
        this.url = url;

        this.method = method;
        this.callback = callback;
    }

    public enum Method{
        GET,POST
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    public void onError(Exception e){
        callback.onError(e);
    }
    public void onResponse(T response){
        callback.onResponse(response);
    }
    public interface Callback<T>{
        void onError(Exception e);
        void onResponse(T reponse);
    }
    public HashMap<String, String>getPostParams(){
        return null;
    }
    public void disPatchContent(byte[] content){

    }
}
