package com.mars.cloud.core.util;

import com.mars.core.constant.MarsCloudConstant;
import com.mars.core.util.SerializableUtil;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 */
public class CloudHttpUtil {

    /**
     * 超时时间
     */
    private static long timOut;


    /**
     * 发起请求，以序列化方式传递数据
     * @param url 路径
     * @param params 参数
     * @return 结果
     * @throws Exception 异常
     */
    public static String request(String url, Object params) throws Exception {

        /* 将参数序列化成byte[] */
        byte[] param = SerializableUtil.serialization(params);

        OkHttpClient okHttpClient = getOkHttpClient();

        /* 发起post请求 将数据传递过去 */
        MediaType formData = MediaType.parse("multipart/form-data");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),param);
        MultipartBody body = new MultipartBody.Builder()
                .setType(formData)
                .addFormDataPart(MarsCloudConstant.PARAM,"params",fileBody)
                .addFormDataPart(MarsCloudConstant.REQUEST_TYPE,MarsCloudConstant.REQUEST_TYPE)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }

    /**
     * 发起get请求
     * @param strUrl 链接
     * @param params 参数
     * @return 响应结果
     * @throws Exception 异常
     */
    public static String get(String strUrl, Map<String,Object> params) throws Exception {
        String url = strUrl+"?"+getParams(params);
        OkHttpClient okHttpClient = getOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        return okCall(okHttpClient,request);
    }

    /**
     * 组装参数
     * @param params 参数
     * @return 结果
     */
    private static String getParams(Map<String,Object> params){
        StringBuffer stringBuffer = new StringBuffer();
        if(params != null){
            for(String key : params.keySet()){
                stringBuffer.append(key);
                stringBuffer.append("=");
                stringBuffer.append(params.get(key));
                stringBuffer.append("&");
            }
        }
        return stringBuffer.substring(0,stringBuffer.length()-1);
    }

    /**
     * 开始请求
     * @param okHttpClient 客户端
     * @param request 请求
     * @return 结果
     * @throws Exception 异常
     */
    private static String okCall(OkHttpClient okHttpClient,Request request) throws Exception {
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        int code = response.code();
        ResponseBody responseBody = response.body();
        if(code != 200){
            throw new Exception("请求接口出现异常:"+responseBody.string());
        }
        return responseBody.string();
    }

    /**
     * 获取okHttp客户端
     * @return 客户端
     * @throws Exception 异常
     */
    private static OkHttpClient getOkHttpClient() throws Exception {
        init();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timOut, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(timOut, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        return okHttpClient;
    }

    /**
     * 初始化timeOut
     * @throws Exception 异常
     */
    private static void init() throws Exception {
        Object obj = CloudConfigUtil.getCloudConfig("timeOut");
        if(obj == null){
            timOut = 10000;
        } else {
            timOut = Long.parseLong(obj.toString());
        }
    }
}
