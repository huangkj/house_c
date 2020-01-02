package com.easylife.house.customer.ui.homesearch.housefilter;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.search.SearchActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 筛选页面
 */

public class HouseFilterActivity extends MVPBaseActivity<HouseFilterContract.View, HouseFilterPresenter> implements HouseFilterContract.View {

    @Bind(R.id.tvSelectArea)
    TextView tvSelectArea;
    @Bind(R.id.tvSelectLine)
    TextView tvSelectLine;
    @Bind(R.id.groupTagsHouseType)
    FlowViewGroup groupTagsHouseType;
    @Bind(R.id.groupTagsBudget)
    FlowViewGroup groupTagsBudget;
    @Bind(R.id.groupTagsHouseStructur)
    FlowViewGroup groupTagsHouseStructur;
    @Bind(R.id.groupTagsHouseArea)
    FlowViewGroup groupTagsHouseArea;
    @Bind(R.id.groupTagsSorts)
    FlowViewGroup groupTagsSorts;
    @Bind(R.id.btnReset)
    ButtonTouch btnReset;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    @Bind(R.id.laySort)
    RelativeLayout laySort;
    private SearchSingleton searchSingleton;

    public static void startActivity(Activity activity, boolean isFromMap, String city, String cityId, String type) {
        activity.startActivityForResult(new Intent(activity, HouseFilterActivity.class)
                .putExtra("isFromMap", isFromMap)
                .putExtra("city", city)
                .putExtra("type", type)
                .putExtra("cityId", cityId), 0);
    }

    public static void startActivity(Activity activity, boolean isFromMap, String city, String cityId, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HouseFilterActivity.class)
                .putExtra("isFromMap", isFromMap)
                .putExtra("city", city)
                .putExtra("cityId", cityId), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.house_activity_filter, null);
    }

    private PubTipDialog dialogLocation;
    private List<ItemSelect> locationList;
    private PubTipDialog dialogSubway;
    private List<ItemSelect> subwayList;

    private String defaultLocation;
    private String defaultLocationId;
    private String defaultSubway;
//    private String defaultSubwayId;

    private String structure;
    //    private String structureId;
    private String budget;
    //    private String budgetId;
    private String houseType;
    private String houseArea;
//    private String houseAreaId;

    private String sortId;

    private boolean isFromMap;
    private String city;
    private String cityId;
    private String type;
    public Map<Integer, Integer> houseAreaIdSet = new HashMap<>();
    public Map<Integer, Integer> budgetSet = new HashMap<>();
    public Map<Integer, Integer> devRoomInfoSet = new HashMap<>();
    public Map<Integer, Integer> houseTypeSet = new HashMap<>();
    private String[] mTimes = new String[]
            {"近1个月", "近3个月", "近半年"};
    public String chooseDatePosition = "";

    @Override
    protected void initView() {
        isFromMap = getIntent().getBooleanExtra("isFromMap", false);
        city = getIntent().getStringExtra("city");
        type = getIntent().getStringExtra("type");
        cityId = getIntent().getStringExtra("cityId");
        if (!TextUtils.isEmpty(cityId) && cityId.length() == 6) {
            cityId = cityId.substring(0, 4) + "00";
        }

        mPresenter.getAreaData(city, cityId);
        mPresenter.getSubwayData(cityId);


        groupTagsHouseType.addViewsCheckBox(R.layout.item_house_filter_tag, new String[]{"住宅","公寓", "别墅", "写字楼", "商业"}, new FlowViewGroup.TabClickListener() {

            @Override
            public void onTabClick(int position, String text) {
                houseType = text;
//                houseTypeId = position + "";
                if (houseTypeSet.size() != 0) {
                    if (houseTypeSet.containsKey(position) && houseTypeSet.get(position) == 1) {
                        houseTypeSet.remove(position);
                    } else {
                        houseTypeSet.put(position, 1);
                    }
                } else {
                    houseTypeSet.put(position, 1);
                }
//                if(houseTypeSet.contains(position+"")){
//                    houseTypeSet.remove(position+"");
//                }else {
//                    houseTypeSet.add(position + "");
//                }
            }
        });
        groupTagsBudget.addViewsCheckBox(R.layout.item_house_filter_tag, new String[]{"10000元/㎡以下", "10000-20000元/㎡", "20000-30000元/㎡", "30000-50000元/㎡", "50000-80000元/㎡", "80000元/㎡上"}, new FlowViewGroup.TabClickListener() {

            @Override
            public void onTabClick(int position, String text) {
                budget = text;
//                budgetId = position + "";
//                if(budgetSet.contains(position+"")){
//                    budgetSet.remove(position+"");
//                }else {
//                    budgetSet.add(position + "");
//                }

                if (budgetSet.size() != 0) {
                    if (budgetSet.containsKey(position) && budgetSet.get(position) == 1) {
                        budgetSet.remove(position);
                    } else {
                        budgetSet.put(position, 1);
                    }
                } else {
                    budgetSet.put(position, 1);
                }
            }
        });
        groupTagsHouseStructur.addViewsCheckBox(R.layout.item_house_filter_tag, new String[]{"一居室", "二居室", "三居室", "四居室", "五居室及以上"}, new FlowViewGroup.TabClickListener() {

            @Override
            public void onTabClick(int position, String text) {
                structure = text;
//                structureId = position + "";
                if (devRoomInfoSet.size() != 0) {
                    if (devRoomInfoSet.containsKey(position) && devRoomInfoSet.get(position) == 1) {
                        devRoomInfoSet.remove(position);
                    } else {
                        devRoomInfoSet.put(position, 1);
                    }
                } else {
                    devRoomInfoSet.put(position, 1);
                }

//                if(devRoomInfoSet.contains(position+"")){
//                    devRoomInfoSet.remove(position+"");
//                }else {
//                    devRoomInfoSet.add(position + "");
//                }
            }
        });

//        groupTagsHouseArea.addViewsCheckBox(R.layout.item_house_filter_tag, new String[]{"近1个月", "近3个月", "近半年"}, new FlowViewGroup.TabClickListener() {
//
//            @Override
//            public void onTabClick(int position, String text) {
//                houseArea = text;
////                houseAreaId = position + "";
//                if(houseAreaIdSet.contains(position+"")){
//                   houseAreaIdSet.remove(position+"");
//                }else {
//                    houseAreaIdSet.add(position + "");
//                }
//
//                if (houseAreaIdSet.size() != 0) {
//                    if (houseAreaIdSet.containsKey(position) && houseAreaIdSet.get(position) == 1) {
//                        houseAreaIdSet.remove(position);
//                    } else {
//                        houseAreaIdSet.put(position, 1);
//                    }
//                } else {
//                    houseAreaIdSet.put(position, 1);
//                }
//            }
//        });

        for (int i = 0; i < mTimes.length; i++) {
            RadioButton  tvSort = (RadioButton) LayoutInflater.from(this).inflate(R.layout.open_time_item_flow, groupTagsHouseArea, false);
            tvSort.setText(mTimes[i]);
            tvSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < groupTagsHouseArea.getChildCount(); j++) {
                        RadioButton radiobtn = (RadioButton) groupTagsHouseArea.getChildAt(j);
                        if (radiobtn.isChecked()) {
                            radiobtn.setTextColor(getResources().getColor(R.color.white));
                            chooseDatePosition = j + "";
                            houseAreaIdSet.clear();
                            houseAreaIdSet.put(j, 1);
                        } else {
                            radiobtn.setTextColor(getResources().getColor(R.color.gradient_end));
                        }

                    }
                }
            });
            groupTagsHouseArea.addView(tvSort);
        }



        if (isFromMap) {
            laySort.setVisibility(View.GONE);
        } else {
            laySort.setVisibility(View.VISIBLE);
            groupTagsSorts.addViews(R.layout.item_house_filter_tag_sort, new String[]{"默认排序", "价格升序", "价格降序", "开盘时间升序", "开盘时间降序",}, new FlowViewGroup.TabClickListener() {

                @Override
                public void onTabClick(int position, String text) {
                    sortId = position + "";
                }
            });
        }

        try {
            searchSingleton = SearchSingleton.getIstance();

            if (isFromMap) {
                defaultLocation = searchSingleton.locationmap;
                defaultLocationId = searchSingleton.locationId;
                defaultSubway = searchSingleton.subwaymap;
                //// TODO: 2017/7/6/006
                if (searchSingleton.houseTypeSetMap != null) {
                    houseTypeSet.putAll(searchSingleton.houseTypeSetMap);
                }
                if (searchSingleton.houseAreaIdSetMap != null) {
                    houseAreaIdSet.putAll(searchSingleton.houseAreaIdSetMap);
                }
                if (searchSingleton.budgetSetMap != null) {
                    budgetSet.putAll(searchSingleton.budgetSetMap);
                }
                if (searchSingleton.devRoomInfoSetMap != null) {
                    devRoomInfoSet.putAll(searchSingleton.devRoomInfoSetMap);
                }

                sortId = searchSingleton.sortIdmap + "";
                tvSelectArea.setText(searchSingleton.locationmap);
                tvSelectLine.setText(searchSingleton.subwaymap);

                for (Map.Entry<Integer, Integer> entry : houseTypeSet.entrySet()) {
                    ((CheckBox) groupTagsHouseType.getChildAt(entry.getKey())).setChecked(true);
                }

                for (Map.Entry<Integer, Integer> entry : houseAreaIdSet.entrySet()) {
                    ((RadioButton) groupTagsHouseArea.getChildAt(entry.getKey())).setChecked(true);
                }

                for (Map.Entry<Integer, Integer> entry : budgetSet.entrySet()) {
                    ((CheckBox) groupTagsBudget.getChildAt(entry.getKey())).setChecked(true);
                }

                for (Map.Entry<Integer, Integer> entry : devRoomInfoSet.entrySet()) {
                    ((CheckBox) groupTagsHouseStructur.getChildAt(entry.getKey())).setChecked(true);
                }

                ((RadioButton) groupTagsSorts.getChildAt(searchSingleton.sortIdmap)).setChecked(true);
            } else {
                defaultLocation = searchSingleton.location;
                defaultSubway = searchSingleton.subway;
                if (searchSingleton.isIndexHome) {
                    if (searchSingleton.houseTypeSet != null) {
                        houseTypeSet.putAll(searchSingleton.houseTypeSet);
                    }
                    if (searchSingleton.houseAreaIdSet != null) {
                        houseAreaIdSet.putAll(searchSingleton.houseAreaIdSet);
                    }
                    if (searchSingleton.budgetSet != null) {
                        budgetSet.putAll(searchSingleton.budgetSet);
                    }
                    if (searchSingleton.devRoomInfoSet != null) {
                        devRoomInfoSet.putAll(searchSingleton.devRoomInfoSet);
                    }
                    sortId = searchSingleton.sortId + "";
                    for (Map.Entry<Integer, Integer> entry : houseTypeSet.entrySet()) {
                        ((CheckBox) groupTagsHouseType.getChildAt(entry.getKey())).setChecked(true);
                    }

                    for (Map.Entry<Integer, Integer> entry : houseAreaIdSet.entrySet()) {
                        ((RadioButton) groupTagsHouseArea.getChildAt(entry.getKey())).setChecked(true);
                        ((RadioButton) groupTagsHouseArea.getChildAt(entry.getKey())).setTextColor(getResources().getColor(R.color.white));
                    }

                    for (Map.Entry<Integer, Integer> entry : budgetSet.entrySet()) {
                        ((CheckBox) groupTagsBudget.getChildAt(entry.getKey())).setChecked(true);
                    }

                    for (Map.Entry<Integer, Integer> entry : devRoomInfoSet.entrySet()) {
                        ((CheckBox) groupTagsHouseStructur.getChildAt(entry.getKey())).setChecked(true);
                    }

                    ((RadioButton) groupTagsSorts.getChildAt(searchSingleton.sortId)).setChecked(true);
                } else {
                    if (searchSingleton.houseTypeSetSearch != null) {
                        houseTypeSet.putAll(searchSingleton.houseTypeSetSearch);
                    }
                    if (searchSingleton.houseAreaIdSetSearch != null) {
                        houseAreaIdSet.putAll(searchSingleton.houseAreaIdSetSearch);
                    }
                    if (searchSingleton.budgetSetSearch != null) {
                        budgetSet.putAll(searchSingleton.budgetSetSearch);
                    }
                    if (searchSingleton.devRoomInfoSetSearch != null) {
                        devRoomInfoSet = searchSingleton.devRoomInfoSetSearch;
                    }
                    sortId = searchSingleton.sortIdSearch + "";

                    for (Map.Entry<Integer, Integer> entry : houseTypeSet.entrySet()) {
                        ((CheckBox) groupTagsHouseType.getChildAt(entry.getKey())).setChecked(true);
                    }

                    for (Map.Entry<Integer, Integer> entry : houseAreaIdSet.entrySet()) {
                        ((RadioButton) groupTagsHouseArea.getChildAt(entry.getKey())).setChecked(true);
                    }

                    for (Map.Entry<Integer, Integer> entry : budgetSet.entrySet()) {
                        ((CheckBox) groupTagsBudget.getChildAt(entry.getKey())).setChecked(true);
                    }

                    for (Map.Entry<Integer, Integer> entry : devRoomInfoSet.entrySet()) {
                        ((CheckBox) groupTagsHouseStructur.getChildAt(entry.getKey())).setChecked(true);
                    }
                    ((RadioButton) groupTagsSorts.getChildAt(searchSingleton.sortIdSearch)).setChecked(true);
                }

                tvSelectArea.setText(searchSingleton.location);
                tvSelectLine.setText(searchSingleton.subway);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {
        mPresenter.getAreaData(city, cityId);
        mPresenter.getSubwayData(cityId);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @OnClick({R.id.tvSelectArea, R.id.tvSelectLine, R.id.btnReset, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSelectArea:
                if (dialogLocation == null) {
                    dialogLocation = new PubTipDialog(activity);
                }
                dialogLocation.showDialogList("请选择位置", defaultLocation, locationList, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultLocation = date.getText();
                        defaultLocationId = date.getId();
                        tvSelectArea.setText(defaultLocation);
                    }
                });
                break;
            case R.id.tvSelectLine:
                if (dialogSubway == null) {
                    dialogSubway = new PubTipDialog(activity);
                }
                dialogSubway.showDialogList("请选择地铁线", defaultSubway, subwayList, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultSubway = date.getText();
//                        defaultSubwayId = data.getId();
                        tvSelectLine.setText(defaultSubway);
                    }
                });
                break;
            case R.id.btnReset:
                reset();
                break;
            case R.id.btnSubmit:

                if (isFromMap) {
                    searchSingleton.locationmap = defaultLocation;
                    searchSingleton.subwaymap = defaultSubway;
                    searchSingleton.locationId = defaultLocationId;

                    // TODO: 2017/7/6/006  
                    if (houseAreaIdSet != null) {
                        searchSingleton.houseAreaIdSetMap = houseAreaIdSet;
                    }
                    if (houseTypeSet != null) {
                        searchSingleton.houseTypeSetMap = houseTypeSet;
                    }
                    // TODO: 2017/7/6/006 
                    if (devRoomInfoSet != null) {
                        searchSingleton.devRoomInfoSetMap = devRoomInfoSet;
                    }

                    if (!TextUtils.isEmpty(sortId)) {
                        searchSingleton.sortIdmap = Integer.parseInt(sortId);
                    }

                    // TODO: 2017/7/6/006 
                    if (budgetSet != null) {
                        searchSingleton.budgetSetMap = budgetSet;
                    }
                } else if (searchSingleton.isIndexHome) {
                    searchSingleton.subway = defaultSubway;
                    searchSingleton.locationId = defaultLocationId;
                    searchSingleton.location = defaultLocation;
                    // TODO: 2017/7/6/006  
                    if (houseAreaIdSet != null) {
                        searchSingleton.houseAreaIdSet = houseAreaIdSet;
                    }

                    if (houseTypeSet != null) {
                        searchSingleton.houseTypeSet = houseTypeSet;
                    }

                    // TODO: 2017/7/6/006 
                    if (devRoomInfoSet != null) {
                        searchSingleton.devRoomInfoSet = devRoomInfoSet;
                    }

                    if (!TextUtils.isEmpty(sortId)) {
                        searchSingleton.sortId = Integer.parseInt(sortId);
                    }

                    // TODO: 2017/7/6/006 
                    if (budgetSet != null) {
                        searchSingleton.budgetSet = budgetSet;
                    }
                } else {
                    searchSingleton.subway = defaultSubway;
                    searchSingleton.locationId = defaultLocationId;
                    searchSingleton.location = defaultLocation;
                    // TODO: 2017/7/6/006  
                    if (houseAreaIdSet != null) {
                        searchSingleton.houseAreaIdSetSearch = houseAreaIdSet;
                    }

                    if (houseTypeSet != null) {
                        searchSingleton.houseTypeSetSearch = houseTypeSet;
                    }

                    // TODO: 2017/7/6/006 
                    if (devRoomInfoSet != null) {
                        searchSingleton.devRoomInfoSetSearch = devRoomInfoSet;
                    }

                    if (!TextUtils.isEmpty(sortId)) {
                        searchSingleton.sortIdSearch = Integer.parseInt(sortId);
                    }

                    // TODO: 2017/7/6/006 
                    if (budgetSet != null) {
                        searchSingleton.budgetSetSearch = budgetSet;
                    }
                }

                mPresenter.submit(city, defaultLocationId, defaultSubway, houseTypeSet, budgetSet, devRoomInfoSet, houseAreaIdSet, sortId, type);
                break;
        }

    }

    @Override
    public void initAreaData(List<ItemSelect> data) {
        locationList = new ArrayList<>(data);
    }

    @Override
    public void initSubwayData(List<ItemSelect> data) {
        subwayList = new ArrayList<>(data);
    }

    @Override
    public void submitParams(SearchRequestBean searchRequestBean) {
        if (isFromMap) {
            setResult(RESULT_OK, new Intent().putExtra(SearchRequestBean.SEARCH_BEAN, searchRequestBean));
            finish();
        } else {
            startActivity(new Intent(this, SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, searchRequestBean));
            finish();
        }
    }

    @Override
    public void reset() {
        defaultLocation = null;
//        defaultLocationId = null;
        defaultSubway = null;
//        defaultSubwayId = null;

        //// TODO: 2017/7/6/006  
        devRoomInfoSet.clear();
        budgetSet.clear();
        houseTypeSet.clear();
        houseAreaIdSet.clear();
        sortId = null;

        structure = null;
        budget = null;
        houseType = null;
        houseArea = null;

        //从首页搜索进来
        searchSingleton.houseTypeId = 0;
        searchSingleton.sortId = 0;
        if (searchSingleton.budgetSet != null) {
            searchSingleton.budgetSet.clear();
        }
//        if (searchSingleton.houseAreaIdSet != null) {
//            searchSingleton.houseAreaIdSet.clear();
//        }
        if (searchSingleton.devRoomInfoSet != null) {
            searchSingleton.devRoomInfoSet.clear();
        }
        if (searchSingleton.houseTypeSet != null) {
            searchSingleton.houseTypeSet.clear();
        }

        //从品牌地产进来
        searchSingleton.houseTypeIdSearch = 0;
        searchSingleton.sortIdSearch = 0;
        if (searchSingleton.budgetSetSearch != null) {
            searchSingleton.budgetSetSearch.clear();
        }
//        if (searchSingleton.houseAreaIdSetSearch != null) {
//            searchSingleton.houseAreaIdSetSearch.clear();
//        }
        if (searchSingleton.devRoomInfoSetSearch != null) {
            searchSingleton.devRoomInfoSetSearch.clear();
        }
        if (searchSingleton.houseTypeSetSearch != null) {
            searchSingleton.houseTypeSetSearch.clear();
        }

        //从地图进来
        searchSingleton.houseTypeIdmap = 0;
        searchSingleton.sortIdmap = 0;
        if (searchSingleton.budgetSetMap != null) {
            searchSingleton.budgetSetMap.clear();
        }
        if (searchSingleton.houseAreaIdSetMap != null) {
            searchSingleton.houseAreaIdSetMap.clear();
        }
        if (searchSingleton.devRoomInfoSetMap != null) {
            searchSingleton.devRoomInfoSetMap.clear();
        }
        if (searchSingleton.houseTypeSetMap != null) {
            searchSingleton.houseTypeSetMap.clear();
        }

        searchSingleton.location = "";
        searchSingleton.subway = "";
        searchSingleton.addressDistrict = "";
        searchSingleton.subway = "";
        searchSingleton.propertyType = "";
        searchSingleton.sort = "";


        tvSelectArea.setText(null);
        tvSelectLine.setText(null);
        for (int i = 0; i < groupTagsHouseStructur.getChildCount(); i++) {
            ((CheckBox) groupTagsHouseStructur.getChildAt(i)).setChecked(false);
        }

        for (int i = 0; i < groupTagsBudget.getChildCount(); i++) {
            ((CheckBox) groupTagsBudget.getChildAt(i)).setChecked(false);
        }

        for (int i = 0; i < groupTagsHouseType.getChildCount(); i++) {
            ((CheckBox) groupTagsHouseType.getChildAt(i)).setChecked(false);
        }

        for (int i = 0; i < groupTagsHouseArea.getChildCount(); i++) {
            ((RadioButton) groupTagsHouseArea.getChildAt(i)).setChecked(false);
            ((RadioButton) groupTagsHouseArea.getChildAt(i)).setTextColor(getResources().getColor(R.color.gradient_end));;
        }

        if (!isFromMap)
            ((RadioButton) groupTagsSorts.getChildAt(0)).setChecked(true);
    }
}
