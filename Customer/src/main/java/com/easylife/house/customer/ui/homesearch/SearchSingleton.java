package com.easylife.house.customer.ui.homesearch;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zgm on 2017/3/20.
 * 搜索条件全局单例
 */

public class SearchSingleton {

    //    public String city = "北京市";
    public String city = "";
    public String citySearch = "北京市";
    public String cityId = "";
    //    public String cityId = "110100";
    public String cityIdSearch = "110100";

    private String[] mTimes = new String[]
            {"50万以下", "50-100万", "100-200万", "200-300万", "300-500万", "500-800万", "800-1000万", "1000万以上"};


    public String searchtype;

    public List<Activity> lookHouse = new ArrayList<>();
    public List<String> collectList = new ArrayList<>();

    //是不是首页进入
    public boolean isIndexHome = false;

    //首页记录保存
    public String buyWhere = "北京市";
    public String whereHouse = "";
    public String budget;
    public String openTime;

    public String minPrice;
    public String maxPrice;
    public String beforeTime;

    //    public String minAvgPrige;
//    public String maxAvgPrige;
    public String addressDistrict;
    public String propertyType;
    //    public String minHouseSize;
//    public String maxHouseSize;
//    public ArrayList<Map<String, String>> areaMapList;
//    public ArrayList<Map<String, String>> bugetMapList;
//    public ArrayList<String> devRoomInfo; //  居室
    public ArrayList<Map<String,String>> priceMapList;//预算多少
    public int minBugetValue;
    public int maxBugetValue;
    public Map<Integer,Integer> chooseSet;//预算多少选中的位置 首页
    public int minBugetValueBrand;
    public int maxBugetValueBrand;
    public Map<Integer,Integer> chooseSetBrand;//预算多少选中的位置 品牌地产

    public String sort;

    /**
     * 家的大小
     */
    public ArrayList<Map<String,String>> areaMapList;//预算多少
    public int minBugetValueArea;
    public int maxBugetValueArea;
    public Map<Integer,Integer> chooseSetArea;//首页
    public int minBugetValueBrandArea;
    public int maxBugetValueBrandArea;
    public Map<Integer,Integer> chooseSetBrandArea;//家的大小 品牌地产

    //首页搜索
    public Map<Integer,Integer> houseAreaIdSet;//什么时间购买
    public Map<Integer,Integer> budgetSet;//平均价格
    public Map<Integer,Integer> devRoomInfoSet; //  居室
    public Map<Integer,Integer> houseTypeSet; //  住宅类型
    //地图
    public Map<Integer,Integer> houseAreaIdSetMap;//什么时间购买
    public Map<Integer,Integer> budgetSetMap;//平均价格
    public Map<Integer,Integer> devRoomInfoSetMap; //  居室
    public Map<Integer,Integer> houseTypeSetMap; //  住宅类型
    //品牌地产
    public Map<Integer,Integer> houseAreaIdSetSearch;//什么时间购买
    public Map<Integer,Integer> budgetSetSearch;//平均价格
    public Map<Integer,Integer> devRoomInfoSetSearch; //  居室
    public Map<Integer,Integer> houseTypeSetSearch; //  住宅类型

    /**
     * 搜索结果页面 搜索内容保存
     */
    public String buyWhereSearch = "北京市";
    public String whereHouseSearch = "";
    public String budgetSearch;
    public String openTimeSearch;


    /**
     * 筛选页面保存选中值
     */
    public int houseTypeId = 0;
//    public int budgetId = 0;
//    public int structureId = 0;
//    public int houseAreaId = 0;
    public int sortId = 0;
    public String location = "";
    public String locationId = "";
    public String subway = "";

    /**
     * 品牌地产进入搜索保存筛选位置
     */
    public int houseTypeIdSearch = 0;
//    public int budgetIdSearch = 0;
//    public int structureIdSearch = 0;
//    public int houseAreaIdSearch = 0;
    public int sortIdSearch = 0;


    /**
     * 地图筛选
     * 筛选页面保存选中值
     */
    public int houseTypeIdmap = 0;
//    public int budgetIdmap = 0;
//    public int structureIdmap = 0;
//    public int houseAreaIdmap = 0;
    public int sortIdmap = 0;
    public String locationmap = "";
    public String subwaymap = "";


    // 定义一个私有构造方法
    private SearchSingleton() {

    }

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile SearchSingleton instance;

    //定义一个共有的静态方法，返回该类型实例
    public static SearchSingleton getIstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (SearchSingleton.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new SearchSingleton();
                }
            }
        }
        return instance;
    }


    @Override
    public String toString() {
        return "SearchSingleton{" +
                "buyWhere='" + buyWhere + '\'' +
                ", budget='" + budget + '\'' +
                ", openTime='" + openTime + '\'' +
                '}';
    }
}
