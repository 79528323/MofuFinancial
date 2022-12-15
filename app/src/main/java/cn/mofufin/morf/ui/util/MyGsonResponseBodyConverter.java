package cn.mofufin.morf.ui.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import cc.ruis.lib.util.Logger;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Justin_Liu
 * on 2019/8/28
 */
public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JSONReader reader = new JSONReader(value.charStream());
        String readStr = reader.readString();
        Logger.e("readStr="+readStr);
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
