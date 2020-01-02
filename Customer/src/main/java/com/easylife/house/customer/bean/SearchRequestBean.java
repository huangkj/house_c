package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by zgm on 2017/3/31.
 * 搜索请求bean
 */

public class SearchRequestBean implements Serializable{

    public static final String SEARCH_BEAN = "SEARCH_BEAN";
    public String userCode = "";
    public String city = "";
    public String cityId = "";
    public String devName = "";
    public String beforeTime = "";
    public String type = "0";  // 接口请求类型: 0搜索，1近期开盘，2品牌楼盘
    public String addressDistrict = "";
    public String subway = "";
//    public String minAvgPrige = "";
//    public String maxAvgPrige = "";
//    public String minHouseSize = "";
//    public String maxHouseSize = "";
    public String sort = "0";
    public ArrayList<String> propertyType; //  物业类型
    public ArrayList<String> devRoomInfo; //  居室
    public ArrayList<Map<String,String>> areaMapList;
    public ArrayList<Map<String,String>> bugetMapList;//均价
    public ArrayList<Map<String,String>> priceMapList;//预算多少
}
