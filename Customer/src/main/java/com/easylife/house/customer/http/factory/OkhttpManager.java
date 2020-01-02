package com.easylife.house.customer.http.factory;

import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.easylife.house.customer.App;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.bean.NetBaseStatus1;
import com.easylife.house.customer.http.config.NetConfig;
import com.easylife.house.customer.http.impl.INetResultCode;
import com.easylife.house.customer.http.impl.RequestCallbackImpl;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.LogOut;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 类描述：okhttp网络请求管理
 * Created by zgm on 2017/2/22 0021.
 */

public class OkhttpManager implements RequestCallbackImpl {

    public String TAG = "OkhttpManager";

    private final OkHttpClient mOkhttpClient;
    private final Handler mHandler;
    String strUrl = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OkhttpManager getInstance() {
        return SingleTon.INSTANCE;
    }


    public static class SingleTon {
        public static OkhttpManager INSTANCE = new OkhttpManager();
    }

    public OkhttpManager() {

        InputStream server = null;
        try {
            server = App.applicationContext.getAssets().open("certificate.der");
        } catch (IOException e) {
            e.printStackTrace();
        }

//定义一个信任所有证书的TrustManager
//        final X509TrustManager trustAllCert = new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//            }
//
//            @Override
//            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//            }
//
//            @Override
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                return new java.security.cert.X509Certificate[]{};
//            }
//        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("message", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mOkhttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(NetConfig.setCertificates(new InputStream[]{server}))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler();
    }


    /**
     * get请求
     *
     * @param url
     * @param arrayMap       参数集合
     * @param requestManager
     */
    @Override
    public void get(int requestType, String url, ArrayMap<String, Object> arrayMap, RequestManagerImpl requestManager) {
        StringBuilder sb = new StringBuilder();
        String sbUrl = "";
        if (arrayMap != null && arrayMap.size() != 0) {
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("http")) {
                    sb.append(url).append("?");
                } else {
                    sb.append(Constants.HOST).append(url).append("?");
                }
            } else {
                sb.append(Constants.HOST.substring(0, Constants.HOST.length() - 1)).append("?");
            }

            for (String key : arrayMap.keySet()) {
                sb.append(key).append("=").append(arrayMap.get(key)).append("&");
            }
            sbUrl = sb.toString().substring(0, sb.toString().length() - 1);
        } else {
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("http")) {
                    sb.append(url).append("?");
                } else {
                    sb.append(Constants.HOST).append(url).append("?");
                }
                sbUrl = sb.toString();
            } else {
                sbUrl = Constants.HOST + url;
            }
        }
        Request request = new Request.Builder()
                .url(sbUrl)
                .get()
                .build();
        addRequestCallback(requestType, request, requestManager);
    }


    /**
     * post请求 键值对
     *
     * @param jsonStr
     */
    @Override
    public void postSellingPointRequest(String jsonStr) {
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        LogOut.d(TAG, "网络请求参数：" + Constants.URL_SELLING_POINT + "\n" + jsonStr);
        Request request = new Request.Builder()
                .url(Constants.URL_SELLING_POINT)
                .post(requestBody)
                .build();
        addRequestCallback(0, request, null);
    }

    /**
     * post 请求
     *
     * @param url
     * @param arrayMap
     * @param requestManager
     */
    @Override
    public void post(int requestType, String url, Map<String, Object> arrayMap, RequestManagerImpl requestManager, boolean isImg) {
        RequestBody requestBody = null;
        if (url.contains("http")) {
            strUrl = url;
        } else {
            strUrl = Constants.HOST + url;
        }
        String requestJson = new Gson().toJson(arrayMap);
        LogOut.d(TAG, "网络请求参数：" + strUrl + "\n" + requestJson);
        requestBody = RequestBody.create(JSON, requestJson);

        if (null != requestBody) {
            Request request = new Request.Builder()
                    .url(strUrl)
                    .post(requestBody)
                    .build();

            if (isImg) {
                addUploadCallback(requestType, request, requestManager);
            } else {
                addRequestCallback(requestType, request, requestManager);
            }
        } else {
            new Throwable("请求数据异常，请查看请求封装参数");
        }

    }

    @Override
    public void post(int requestType, String url, String json, RequestManagerImpl requestManager, boolean isImg) {
        RequestBody requestBody = null;
        if (url.contains("http")) {
            strUrl = url;
        } else {
            strUrl = Constants.HOST + url;
        }
        LogOut.d(TAG, "网络请求参数：" + strUrl + "\n" + json);
        requestBody = RequestBody.create(JSON, json);
        if (null != requestBody) {
            Request request = new Request.Builder()
                    .url(strUrl)
                    .post(requestBody)
                    .build();

            if (isImg) {
                addUploadCallback(requestType, request, requestManager);
            } else {
                addRequestCallback(requestType, request, requestManager);
            }
        } else {
            new Throwable("请求数据异常，请查看请求封装参数");
        }

    }

    /**
     * 第三方接口  身份认证ocr
     *
     * @param requestType
     * @param url
     * @param json
     * @param requestManager
     */
    public void ocr(int requestType, String url, String json, String appcode, RequestManagerImpl requestManager) {
        RequestBody requestBody = null;
        LogOut.d(TAG, "网络请求参数：" + url + "\n" + json);
        requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "APPCODE " + appcode)
                .build();
        addRequestCallbackAllData(requestType, request, requestManager);
    }


    /**
     * 请求回调
     *
     * @param request
     * @param requestManager
     */
    private void addRequestCallback(final int requestType, final Request request, final RequestManagerImpl requestManager) {
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogOut.d(TAG, "网络连接失败：" + request.url());
                        NetBaseStatus status = new NetBaseStatus();
                        status.msg = "网络连接失败";
                        if (requestManager != null)
                            requestManager.onFail(status, requestType);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String json = response.body().string();
                final NetBaseStatus status = getBaseNetBaseStatus(json);
                LogOut.d(TAG, "网络连接成功：" + request.url() + "  json : \n" + json);
                if (response.isSuccessful() && status.succ) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String data = new JSONObject(json).opt("data").toString();
                                requestManager.onSuccess(data, requestType);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (requestManager != null)
                                    requestManager.onSuccess(null, requestType);
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (requestManager != null)
                                requestManager.onFail(status, requestType);
                        }
                    });
                }
            }
        });
    }


    /**
     * 请求回调
     *
     * @param request
     * @param requestManager
     */
    private void addRequestCallbackAllData(final int requestType, final Request request, final RequestManagerImpl requestManager) {
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogOut.d(TAG, "网络连接失败：" + request.url());
                        NetBaseStatus status = new NetBaseStatus();
                        status.msg = "网络连接失败";
                        if (requestManager != null)
                            requestManager.onFail(status, requestType);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String json = response.body().string();
                final NetBaseStatus status = new NetBaseStatus();
                LogOut.d(TAG, "网络连接成功：" + request.url() + "  json : \n" + json);
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                String data = new JSONObject(json).opt("data").toString();
                                requestManager.onSuccess(json, requestType);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (requestManager != null)
                                    requestManager.onSuccess(json, requestType);
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (requestManager != null) {
                                status.msg = json;
                                requestManager.onFail(status, requestType);
                            }
                        }
                    });
                }
            }
        });
    }


    public NetBaseStatus getBaseNetBaseStatus(String content) {
        NetBaseStatus status = new NetBaseStatus();
        try {
            NetBaseStatus1 status1 = new Gson().fromJson(content, NetBaseStatus1.class);
            if (status1 == null || status1.status == null) {
                status = new Gson().fromJson(content, NetBaseStatus.class);
            } else {
                status = status1.status;
            }
            if (status == null) {
                status = new NetBaseStatus();
                status.code = INetResultCode.CODE_NET_RESULT_SUCC;
            }
            status.succ = INetResultCode.CODE_NET_RESULT_SUCC.equals(status.code) || INetResultCode.CODE_NET_RESULT_SUCC_1.equals(status.code);
//            status.succ = !INetResultCode.CODE_NET_RESULT_PARAMS_ERROR.equals(status.code);
//            status.succ = !INetResultCode.CODE_STRING_RESULT_PARAMS_ERROR.equals(status.code);
//            status.succ = !INetResultCode.CODE_STRING_RESULT_VERIFYCODE_ERROR_1.equals(status.code);
//            status.succ = !INetResultCode.CODE_STRING_RESULT_VERIFYCODE_ERROR_2.equals(status.code);

            // 成功的话在成功的回调里面去执行文本显示，不用在这里更新文本
            if (TextUtils.isEmpty(status.msg)) {
                if (INetResultCode.CODE_NET_RESULT_SUCC.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_SUCC;
                } else if (INetResultCode.CODE_NET_RESULT_FAIL.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_FAIL;
                    if (INetResultCode.CODE_STRING_RESULT_VERIFYCODE_ERROR_1.equals(status.msg)) {
                        status.msg = "发送失败";
                    } else if (INetResultCode.CODE_STRING_RESULT_VERIFYCODE_ERROR_2.equals(status.msg)) {
                        status.msg = "号码格式错误，请重新发送";
                    }
                } else if (INetResultCode.CODE_NET_RESULT_DATA_NULL.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_DATA_NULL;
                } else if (INetResultCode.CODE_NET_RESULT_PARAMS_ERROR.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_PARAMS_ERROR;
                } else if (INetResultCode.CODE_NET_RESULT_PASS_FIT.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_PASS_FIT;
                } else if (INetResultCode.CODE_NET_RESULT_SERVER_EXCEPTION.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_SERVER_EXCEPTION;
                } else if (INetResultCode.CODE_NET_RESULT_USER_EXIST.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_USER_EXIST;
                } else if (INetResultCode.CODE_NET_RESULT_USER_EXIST_NO.equals(status.code)) {
                    status.msg = INetResultCode.CODE_STRING_RESULT_USER_EXIST_NO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 图片上传请求回调
     *
     * @param request
     * @param requestManager
     */
    private void addUploadCallback(final int requestType, Request request, final RequestManagerImpl requestManager) {
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.onFail(null, requestType);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String json = response.body().string();
                if (response.isSuccessful()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                requestManager.onSuccess(json, requestType);
                            } catch (Exception e) {
                                e.printStackTrace();
                                requestManager.onSuccess(null, requestType);
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestManager.onFail(null, requestType);
                        }
                    });
                }
            }
        });
    }


}
