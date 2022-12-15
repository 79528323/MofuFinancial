package cn.mofufin.morf.ui.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.base.BaseApplication;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yhz on 2016/4/18.
 */
public class RetrofitUtils {
    private final static String TAG=RetrofitUtils.class.getSimpleName();
    private static Retrofit retrofit;
    private static final int REQUEST_TIME_OUT=20;
    private static final TimeUnit REQUEST_TIME_UNIT= TimeUnit.SECONDS;

    public static Retrofit getInstance(){
        Retrofit _retrofit = retrofit;
        if (retrofit == null) {
            synchronized (RetrofitUtils.class) {
                _retrofit = retrofit;
                if (retrofit == null) {
                    _retrofit=init();
                    retrofit = _retrofit;
                }
            }
        }
        return _retrofit;
    }

    private static Retrofit init(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(cacheInterceptor)
                .cache(new Cache(BaseApplication.cacheDirectory, 1024 * 1024 * 10))
                .readTimeout(REQUEST_TIME_OUT, REQUEST_TIME_UNIT)
                .connectTimeout(REQUEST_TIME_OUT, REQUEST_TIME_UNIT)
                .build();
        return new Retrofit.Builder()
                .baseUrl(Common.HOST)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RequestBody createRequestBody(List<Pair<String, String>> params, List<Pair<String, File>> files){
        RequestBody formBody=null;
        if(params!=null || files!=null) {
            if(files==null){
                FormBody.Builder builder=new FormBody.Builder();
                for(Pair<String, String> param : params){
                    Logger.i(TAG, "params --> " + param.first + "=" + param.second);
                    builder.add(param.first, param.second);
                }
                formBody = builder.build();
            }else{
                MultipartBody.Builder builder=new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for(Pair<String, File> file : files){
                    if(file!=null) {
                        Logger.i(TAG, "file --> " + file.first + "##" + file.second.getPath());
                        builder.addFormDataPart(file.first, file.second.getName(),
                                RequestBody.create(null, file.second));
                    }
                }
                if(params!=null){
                    for(Pair<String, String> param : params){
                        Logger.i(TAG, "params --> " + param.first + "=" + param.second);
                        builder.addFormDataPart(param.first, param.second);
                    }
                }
                formBody=builder.build();
            }
        }
        return formBody;
    }

    //请求拦截
    private static Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if(!TextUtils.equals(request.method(), "GET")){
                Logger.e("method="+request.method());
                return chain.proceed(request);
            }
            boolean cache=!isNetworkAvailable(BaseApplication.context);
            if (cache) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                //Logger.i("no network");
            }

            Response response = chain.proceed(request);

            if (!cache) {
                int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                Logger.i("has network maxAge="+maxAge);
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                //Logger.i("response no network");
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                //Logger.i("has maxStale="+maxStale);
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
                //Logger.i("response build maxStale="+maxStale);
            }
            return response;
        }
    };

    @SuppressLint("MissingPermission")
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable();
    }
}
