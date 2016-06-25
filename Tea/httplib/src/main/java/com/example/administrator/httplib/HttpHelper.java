package com.example.administrator.httplib;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class HttpHelper {
    private static HttpHelper Instance;
    private RequestQueue requestQueue;

    private static HttpHelper getInstance(){
        if(Instance ==null){
            synchronized (HttpHelper.class){
                if(Instance ==null){
                    Instance =  new HttpHelper();
                }
            }
        }
        return Instance;
    }

    private HttpHelper(){
        requestQueue = new RequestQueue();
    }

    public static void addRequest(Request request){

        getInstance().requestQueue.addRequest(request);
    }

}
