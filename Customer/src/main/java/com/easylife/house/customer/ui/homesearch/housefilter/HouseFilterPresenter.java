package com.easylife.house.customer.ui.homesearch.housefilter;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.bean.CityAreaBean;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.bean.Subway;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.util.CustomerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class HouseFilterPresenter extends BasePresenterImpl<HouseFilterContract.View> implements HouseFilterContract.Presenter, RequestManagerImpl {

    @Override
    public void getAreaData(String city, String cityId) {
        mDao.getCityArea(mDao.HOUSES_DETAIL_MAIN_TYPE, "2", cityId, this);

//        List<ItemSelect> locationList = new ArrayList<>();
//        locationList.add(new ItemSelect("朝阳区", "1"));
//        locationList.add(new ItemSelect("西城区", "2"));
//        locationList.add(new ItemSelect("海淀区", "3"));
//        locationList.add(new ItemSelect("丰台区", "4"));
//        locationList.add(new ItemSelect("石景山区", "5"));
//        locationList.add(new ItemSelect("东城区", "6"));
//        locationList.add(new ItemSelect("通州区", "7"));
//        locationList.add(new ItemSelect("顺义区", "8"));
//        locationList.add(new ItemSelect("房山区", "9"));
//        locationList.add(new ItemSelect("大兴区", "10"));
//        locationList.add(new ItemSelect("昌平区", "11"));
//        locationList.add(new ItemSelect("怀柔区", "12"));
//        locationList.add(new ItemSelect("平谷区", "13"));
//        locationList.add(new ItemSelect("门头沟区", "14"));
//        locationList.add(new ItemSelect("密云区", "15"));
//        locationList.add(new ItemSelect("延庆区", "16"));
    }

    @Override
    public void getSubwayData(String cityID) {
        mDao.selectSubway(1, cityID, this);
//        List<ItemSelect> subwayList = new ArrayList<>();
//        subwayList.add(new ItemSelect("1号线", "1"));
//        subwayList.add(new ItemSelect("2号线外环", "2"));
//        subwayList.add(new ItemSelect("2号线内环", "3"));
//        subwayList.add(new ItemSelect("4号线", "4"));
//        subwayList.add(new ItemSelect("5号线", "5"));
//        subwayList.add(new ItemSelect("6号线", "6"));
//        subwayList.add(new ItemSelect("7号线", "7"));
//        subwayList.add(new ItemSelect("8号线", "8"));
//        subwayList.add(new ItemSelect("9号线", "9"));
//        subwayList.add(new ItemSelect("10号线", "10"));
//        subwayList.add(new ItemSelect("10号线(区间)", "11"));
//        subwayList.add(new ItemSelect("13号线", "12"));
//        subwayList.add(new ItemSelect("14号线(西段)", "13"));
//        subwayList.add(new ItemSelect("14号线(东段)", "14"));
//        subwayList.add(new ItemSelect("15号线", "15"));
//        subwayList.add(new ItemSelect("16号线", "16"));
//        subwayList.add(new ItemSelect("S2号线", "17"));
//        subwayList.add(new ItemSelect("八通线", "18"));
//        subwayList.add(new ItemSelect("昌平线", "19"));
//        subwayList.add(new ItemSelect("房山线", "20"));
//        subwayList.add(new ItemSelect("机场线", "21"));
//        subwayList.add(new ItemSelect("亦庄线", "22"));
//        subwayList.add(new ItemSelect("3号线(未开通)", "23"));
//        subwayList.add(new ItemSelect("11号线(未开通)", "24"));
//        subwayList.add(new ItemSelect("12号线(未开通)", "25"));
//        mView.initSubwayData(subwayList);
    }

    /**
     * @param city            文字：  北京
     * @param addressDistrict 文字 ： 朝阳区
     * @param subway          文字 ： 7号线
     * @param type            文字 ：  普通住宅
     * @param budgetSet       id：  2
     * @param devRoomInfoSet  文字： 一居
     * @param houseAreaIdSet  id ： 3
     * @param sortPosition    id： 0
     */
    @Override
    public void submit(String city, String addressDistrict, String subway, Map<Integer, Integer> houseTypeSet, Map<Integer, Integer> budgetSet,
                       Map<Integer, Integer> devRoomInfoSet, Map<Integer, Integer> houseAreaIdSet, String sortPosition, String type) {
        String url = "customer/login";
        Map<String, Object> mapData = new ArrayMap<>();
        SearchSingleton searchSingleton = SearchSingleton.getIstance();
        SearchRequestBean params = new SearchRequestBean();
        params.userCode = !mDao.isLogin() ? "" : mDao.getLoginCache().userCode;
        params.type = type; // 0搜索，1近期开盘，2品牌楼盘
        params.city = city;
        params.cityId = searchSingleton.cityId;
        params.addressDistrict = addressDistrict;
        params.subway = subway;
        params.priceMapList = searchSingleton.priceMapList;
        params.beforeTime = searchSingleton.beforeTime;
        if (searchSingleton.isIndexHome) {
            params.devName = searchSingleton.whereHouse;
        } else {
            params.devName = searchSingleton.whereHouseSearch;
        }

        if (houseTypeSet != null) {
            ArrayList<String> propertyList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : houseTypeSet.entrySet()) {
                switch (entry.getKey()) {
                    case 0:
                        propertyList.add("0");
                        break;
                    case 1:
                        propertyList.add("1");
                        break;
                    case 2:
                        propertyList.add("2");
                        break;
                    case 3:
                        propertyList.add("3");
                        break;
                    case 4:
                        propertyList.add("4");
                        break;
                }
            }

            params.propertyType = propertyList;

        }

        if (budgetSet != null) {
            ArrayList<Map<String, String>> bugetList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : budgetSet.entrySet()) {
                switch (entry.getKey()) {
                    case 0:
                        // 10000以下
                        Map<String, String> bugetMap1 = new HashMap<>();
                        bugetMap1.put("minAvgPrice", "0");
                        bugetMap1.put("maxAvgPrice", "10000");
                        bugetList.add(bugetMap1);
                        break;
                    case 1:
                        //  10000- 20000
                        Map<String, String> bugetMap2 = new HashMap<>();
                        bugetMap2.put("minAvgPrice", "10000");
                        bugetMap2.put("maxAvgPrice", "20000");
                        bugetList.add(bugetMap2);
                        break;
                    case 2:
                        //  20000- 30000
                        Map<String, String> bugetMap3 = new HashMap<>();
                        bugetMap3.put("minAvgPrice", "20000");
                        bugetMap3.put("maxAvgPrice", "30000");
                        bugetList.add(bugetMap3);
                        break;
                    case 3:
                        //  30000- 50000
                        Map<String, String> bugetMap4 = new HashMap<>();
                        bugetMap4.put("minAvgPrice", "30000");
                        bugetMap4.put("maxAvgPrice", "50000");
                        bugetList.add(bugetMap4);
                        break;
                    case 4:
                        //  50000- 80000
                        Map<String, String> bugetMap5 = new HashMap<>();
                        bugetMap5.put("minAvgPrice", "50000");
                        bugetMap5.put("maxAvgPrice", "80000");
                        bugetList.add(bugetMap5);
                        break;
                    case 5:
                        //  80000以上
                        Map<String, String> bugetMap6 = new HashMap<>();
                        bugetMap6.put("minAvgPrice", "80000");
                        bugetMap6.put("maxAvgPrice", "");
                        bugetList.add(bugetMap6);
                        break;
                    default:
                        break;
                }
            }

            params.bugetMapList = bugetList;

        }
//        params.minAvgPrige = minPrice;
//        params.maxAvgPrige = maxPrice;

//        params.devBedroomInfo = "";
        if (devRoomInfoSet != null) {
            ArrayList<String> devRoomInfoList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : devRoomInfoSet.entrySet()) {
                switch (entry.getKey()) {
                    case 0:
//                        params.devBedroomInfo = "一居室";
                        devRoomInfoList.add("一居室");
                        break;
                    case 1:
//                        params.devBedroomInfo = "二居室";
                        devRoomInfoList.add("二居室");
                        break;
                    case 2:
//                        params.devBedroomInfo = "三居室";
                        devRoomInfoList.add("三居室");
                        break;
                    case 3:
//                        params.devBedroomInfo = "四居室";
                        devRoomInfoList.add("四居室");
                        break;
                    case 4:
//                        params.devBedroomInfo = "五居室及以上";
                        devRoomInfoList.add("五居室及以上");
                        break;
                }
            }
            params.devRoomInfo = devRoomInfoList;

        }


//        String minHouseSize = "";
//        String maxHouseSize = "";
        if (houseAreaIdSet != null) {
            String date = "";
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(dt);
            for (Map.Entry<Integer, Integer> entry : houseAreaIdSet.entrySet()) {
                switch (entry.getKey()) {
                    case 0:
                        try {
                            date = CustomerUtils.subMonth(dateNow, 1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            date = CustomerUtils.subMonth(dateNow, 3);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            date = CustomerUtils.subMonth(dateNow, 6);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            params.beforeTime = date;
        }
//        params.minHouseSize = minHouseSize;
//        params.maxHouseSize = maxHouseSize;

        params.sort = sortPosition;

//        searchSingleton.propertyType = params.propertyType;
        //// TODO: 2017/7/6/006
//        searchSingleton.bugetMapList = params.bugetMapList;
//        searchSingleton.maxAvgPrige = params.maxAvgPrige;
        //// TODO: 2017/7/6/006
//        searchSingleton.areaMapList = params.areaMapList;
//        searchSingleton.maxHouseSize = params.maxHouseSize;
        searchSingleton.sort = params.sort;
        //// TODO: 2017/7/6/006
//        searchSingleton.devRoomInfo = params.devRoomInfo;
        searchSingleton.addressDistrict = addressDistrict;

//        mDao.getSearchData(1, params, this);
        if (mView != null) mView.submitParams(params);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    List<ItemSelect> subwayList = new ArrayList<>();
                    List<Subway> data = new Gson().fromJson(response, new TypeToken<List<Subway>>() {
                    }.getType());
                    if (data != null && data.size() != 0)
                        for (Subway areaBean : data) {
                            subwayList.add(new ItemSelect(areaBean.getText(), areaBean.getId()));
                        }
                    mView.initSubwayData(subwayList);
                    break;
                case ServerDao.HOUSES_DETAIL_MAIN_TYPE:
                    List<ItemSelect> locationList = new ArrayList<>();
                    List<CityAreaBean> areaBeanList = new Gson().fromJson(response, new TypeToken<List<CityAreaBean>>() {
                    }.getType());
                    if (areaBeanList != null && areaBeanList.size() != 0)
                        for (CityAreaBean areaBean : areaBeanList) {
                            locationList.add(new ItemSelect(areaBean.area, areaBean.areaid));
                        }
                    mView.initAreaData(locationList);
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
