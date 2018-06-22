package com.cdkj.gckq.http;

import com.google.gson.Gson;

public class JsonUtils {

    public static <T> T json2Bean(String json, Class<T> clazz) {
        checkFormat(json);
        T t = null;
        try {
            Gson gson = new Gson();
            t = (T) gson.fromJson(json, clazz);
            System.out.println(t);
        } catch (Exception e) {
            throw new ValidException("830xxx",
                "json2Bean不正�?:" + e.getMessage());
        }
        return t;
    }

    public static String string2Json(String key, String value) {
        return "{\"" + key + "\":\"" + value + "\"}";
    }

    public static void checkFormat(String json) {
        boolean isJson = new JsonValidator().validate(json);
        if (!isJson) {
            throw new ValidException("830xxx", "json格式不正�?");
        }
    }

    public static String object2Json(Object bean) {
        Gson gson = new Gson();
        return gson.toJson(bean);
    }
}
