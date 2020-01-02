package com.easylife.house.customer.util;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 类描述：省份 城市 区域Json数据获取
 * 创建者：zgm on 2017/11/13/013.
 */

public class CityUtils {

    /**
     * 获取省份
     *
     * @return
     */
    public static String getProvinceJson(Context context) {
        String provinceJson = null;
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("sys_city_provinces.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            provinceJson = builder.toString();
//            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
//            provinceJson = testjson.getString("RECORDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceJson;
    }


    /**
     * 获取城市
     *
     * @return
     */
    public static String getCityJson(Context context) {
        String provinceJson = null;
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("sys_city_cities.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            provinceJson = builder.toString();
//            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
//            provinceJson = testjson.getString("RECORDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceJson;
    }


    /**
     * 获取区域
     *
     * @return
     */
    public static String getAreaJson(Context context) {
        String provinceJson = null;
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("sys_city_area.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            provinceJson = builder.toString();
//            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
//            provinceJson = testjson.getString("RECORDS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceJson;
    }
}
