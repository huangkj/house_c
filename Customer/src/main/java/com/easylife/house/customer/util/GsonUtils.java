package com.easylife.house.customer.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：gaocaisheng3.0
 * 类描述：项目的util工具 gson解析
 * 创建人：lh
 * 创建时间：2017/2/8 11:17
 */
public class GsonUtils {
    public static Gson gson = new Gson();

    /**
     * 说明：如果解析抛异常返回null
     *
     * @param result 要解析的json字符串
     * @param clazz  对应的javabean的字节码
     * @return 返回 对应的javabean 对象
     */
    public static <T> T fromJson(String result, Class<T> clazz) {
        try {
            if (gson == null) {
                gson = new Gson();
            }
            return gson.fromJson(result, clazz);
        } catch (Exception e) {

            return null;
        }
    }

    public static String toJson(Object obj) {
        if (null == gson) {
            gson = new Gson();
        }
        return gson.toJson(obj);
    }


    public static String getMsg(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
