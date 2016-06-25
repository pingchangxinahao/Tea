package com.example.administrator.httplib;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class RequestQueue {
    private BlockingDeque<Request> requestqueue = new LinkedBlockingDeque<>();
    private final int MAX_THREAD =3;
    private NetworkDispatcher[] works = new NetworkDispatcher[MAX_THREAD];

    public RequestQueue(){
        initDispatcher();
    }

    private void initDispatcher() {
        for(int i= 0;i<works.length;i++){
            works[i] = new NetworkDispatcher(requestqueue);
            works[i].start();
        }
    }
    public void addRequest(Request request){
        requestqueue.add(request);
    }
    public void stop(){
        for(int i=0;i<works.length;i++){
            works[i].flag = false;
            works[i].interrupt();
        }
    }
}
