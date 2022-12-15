package cn.mofufin.morf.ui.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Justin_Liu
 * on 2019/8/28
 */
public class MyGsonConverterFactory extends Converter.Factory {
    public static MyGsonConverterFactory create() {
        return create(new Gson());
    }

    public static MyGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MyGsonConverterFactory(gson);
    }

    private final Gson gson;

    private MyGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter responseBodyConverter(Type type, Annotation[] annotations,
                                           Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyGsonResponseBodyConverter<>(gson,adapter);
    }

    @Override
    public Converter requestBodyConverter(Type type,
                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter adapter = gson.getAdapter(TypeToken.get(type));
        return new MyGsonRequestBodyConverter(gson, adapter);
    }
}
