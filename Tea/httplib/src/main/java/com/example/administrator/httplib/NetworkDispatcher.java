package com.example.administrator.httplib;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class NetworkDispatcher extends Thread {
    private BlockingDeque<Request> mRequest;
    public boolean flag =true;


    public NetworkDispatcher(BlockingDeque<Request> request){
        mRequest = request;
    }

    @Override
    public void run() {

        /**
         * 如果当线程的表记是可运行并且当前线程没有被打断 从请求队列里面取出请求进行网络访问
         */
        while (flag && !isInterrupted()) {

            try {
                //从请求队列中取出一个请求
                final Request req = mRequest.take();
                byte[] result = null;
                try {
                    //获取网络请求的内容
                    result = getNetworkResponse(req);
                    if (result != null) {
                        //当内容不为空的时候回调正常的返回结果
                        req.disPatchContent(result);
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    req.onError(e);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                flag = false;
            }
        }
    }


    public byte[] getNetworkResponse(Request request) throws Exception {
        if(TextUtils.isEmpty(request.getUrl())){
            throw new Exception("url is null");
        }
        if (request.getMethod()==Request.Method.GET){
            return getResponseByGet(request);
        }
        if(request.getMethod() == Request.Method.POST){
            return getResponsePost(request);
        }
        return null;
    }

    private byte[] getResponsePost(Request request) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setDoOutput(true);
            String str = getPostEncondString(request);

            byte[] content = null;
            if(str!=null){

                content = str.getBytes();
                connection.setRequestProperty("content-length",""+content.length);

            }

            out = connection.getOutputStream();
            if(content!=null){
                out.write(content);
                out.flush();
            }
            int code = connection.getResponseCode();
            if(code!=200){
                throw new Exception("http code error");

            }

            in = connection.getInputStream();
            int len =0;
            byte[] buf = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (true){
                len = in.read(buf);
                if(len<0){
                    break;
                }
                outputStream.write(buf,0,len);
            }

            return outputStream.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in !=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
        }
        return null;
    }

    private String getPostEncondString(Request request) {
        HashMap<String,String> map = request.getPostParams();
        StringBuilder buf = new StringBuilder();
        if(map !=null){
            Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
            int i = 0;
            while(iterator.hasNext()){
                if(i>0){
                    buf.append("&");

                }
                Map.Entry<String,String> value = iterator.next();
                String str = value.getKey() +"=" +value.getValue();
                buf.append(str);
                i++;

            }
            return buf.toString();
        }

        return null;
    }

    private byte[] getResponseByGet(Request request) {
        InputStream in= null;
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            in = connection.getInputStream();
            int len =0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while(true){
                len = in.read(buf);
                if(len<0){
                    break;
                }
                out.write(buf,0,len);
            }
            return out.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
