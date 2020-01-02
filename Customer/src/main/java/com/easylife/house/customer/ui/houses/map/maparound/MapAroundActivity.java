package com.easylife.house.customer.ui.houses.map.maparound;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureSupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PoiItemAdapter;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.nowsub.NowSubActivity;
import com.easylife.house.customer.ui.houses.map.mapperipheral.MapPeripheralActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;
import com.easylife.house.customer.view.ButtonTouch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.R.mipmap.phone;
import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;

/**
 * 周边配套 - 列表展示
 */
@RuntimePermissions
public class MapAroundActivity extends MVPBaseActivity<MapAroundContract.View, MapAroundPresenter>
        implements MapAroundContract.View, PoiSearch.OnPoiSearchListener,
        AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {

    @Bind(R.id.btnMoreBus)
    CheckBox btnMoreBus;
    @Bind(R.id.btnMoreSubway)
    CheckBox btnMoreSubway;
    @Bind(R.id.listBus)
    RecyclerView listBus;
    @Bind(R.id.listSubway)
    RecyclerView listSubway;
    @Bind(R.id.btnMoreSchool)
    CheckBox btnMoreSchool;
    @Bind(R.id.listSchool)
    RecyclerView listSchool;
    @Bind(R.id.btnMoreHouse)
    CheckBox btnMoreHouse;
    @Bind(R.id.listHouse)
    RecyclerView listHouse;
    @Bind(R.id.btnMoreHospital)
    CheckBox btnMoreHospital;
    @Bind(R.id.listHospital)
    RecyclerView listHospital;
    @Bind(R.id.btnMoreBank)
    CheckBox btnMoreBank;
    @Bind(R.id.listBank)
    RecyclerView listBank;
    @Bind(R.id.btnMoreShop)
    CheckBox btnMoreShop;
    @Bind(R.id.listShop)
    RecyclerView listShop;
    @Bind(R.id.rl_collect)
    RelativeLayout rl_collect;
    @Bind(R.id.rl_ask)
    RelativeLayout rl_ask;
    @Bind(R.id.rl_sub)
    RelativeLayout rl_sub;
    @Bind(R.id.lay1)
    LinearLayout lay1;

    @Bind(R.id.iv_collect)
    CheckBox iv_collect;

    @Bind(R.id.tv_look)
    TextView tv_look;
    @Bind(R.id.tv_call)
    TextView tv_call;
    @Bind(R.id.btnNavigation)
    ButtonTouch btnNavigation;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.map_activity_around, null);
    }

    public static void startActivity(Activity activity, String dev_id, HousesDetailBaseBean detail, boolean isSimple) {
        activity.startActivity(new Intent(activity, MapAroundActivity.class)
                .putExtra("dev_id", dev_id)
                .putExtra("baseBean", detail)
                .putExtra("isSimple", isSimple)
        );
    }

    private AMap aMap;
    private String mTableIDHouse;
    private UiSettings mUiSettings;
    private CloudSearch.Query mQuery;
    private String mIdHouse;
    private PoiSearch.Query query;// Poi查询条件类

    private PoiSearch poiSearch;
    private List<PoiItem> poiItemsHouse;
    private List<PoiItem> poiItemsBus;// poi数据
    private List<PoiItem> poiItemsSubway;// poi数据
    private List<PoiItem> poiItemsSchool;// poi数据
    private List<PoiItem> poiItemsHospital;// poi数据
    private List<PoiItem> poiItemsBank;// poi数据
    private List<PoiItem> poiItemsShop;// poi数据
    private String mKeySearch;
    private Marker mMarkerHouse;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private PoiItemAdapter adapterHouse;
    private PoiItemAdapter adapterBus;
    private PoiItemAdapter adapterSubway;
    private PoiItemAdapter adapterSchool;
    private PoiItemAdapter adapterHospital;
    private PoiItemAdapter adapterBank;
    private PoiItemAdapter adapterShop;

    private HousesDetailBaseBean baseBean;
    private SearchSingleton searchSingleton;
    private String dev_id;
    private double lat, lon;
    private boolean isSimple;

    @Override
    protected void initView() {

        hideNoNetView();
        searchSingleton = SearchSingleton.getIstance();
        isSimple = getIntent().getBooleanExtra("isSimple", false);
        dev_id = getIntent().getStringExtra("dev_id");
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("baseBean");

        lay1.setVisibility(isSimple ? View.GONE : View.VISIBLE);

        try {
            mTableIDHouse = baseBean.tableDevId;
            lat = baseBean.projectCoordinate.get(1);
            lon = baseBean.projectCoordinate.get(0);
        } catch (Exception e) {
            LogOut.d(TAG, e.toString());
            showTip("楼盘数据错误");
            finish();
        }

        if (searchSingleton.collectList.contains(dev_id)) {
            iv_collect.setChecked(true);
        } else {
            iv_collect.setChecked(false);
        }

        adapterHouse = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterBus = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterSubway = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterSchool = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterHospital = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterBank = new PoiItemAdapter(R.layout.item_map_around, null);
        adapterShop = new PoiItemAdapter(R.layout.item_map_around, null);

        listBus.setAdapter(adapterBus);
        listSubway.setAdapter(adapterSubway);
        listSchool.setAdapter(adapterSchool);
        listHouse.setAdapter(adapterHouse);
        listHospital.setAdapter(adapterHospital);
        listBank.setAdapter(adapterBank);
        listShop.setAdapter(adapterShop);
        listBus.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listSubway.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listSchool.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listHouse.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listHospital.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listBank.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listShop.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        if (aMap == null) {
            aMap = ((TextureSupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapView)).getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
    }

    private void setUpMap() {
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setAllGesturesEnabled(false);
        aMap.setOnMapLoadedListener(this);

        aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);

    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void onMapLoaded() {
        aMap.clear();
        MapUtil.moveToCurrentLocation(aMap, lat, lon);
//        mMarkerHouse = aMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat, lon))
//                .title(baseBean.devName)
//                .snippet(baseBean.addressDetail)
//                .icon(BitmapDescriptorFactory.defaultMarker()));

        addMarker(new LatLng(lat, lon), baseBean.devName, baseBean.averPrice);
//        mMarkerHouse.showInfoWindow();

        doSearchQuery("地铁");
    }

    private void addMarker(LatLng latLonPoint, String title, String snippet) {
        if (latLonPoint == null) {
            return;
        }
        aMap.clear();
        LogOut.d("addMarker : ", title + " ,  " + snippet + " , latLonPoint:(" + latLonPoint.latitude + "," + latLonPoint.longitude + ")");
        View layMarker = LayoutInflater.from(this).inflate(R.layout.layout_marker, null);

        TextView titleView = (TextView) layMarker.findViewById(R.id.title);
        TextView snippetView = (TextView) layMarker.findViewById(R.id.snippet);
        titleView.setText(title);
        if (!TextUtils.isEmpty(snippet) && !"0".equals(snippet)) {
            snippetView.setText(snippet + "元/m²");
        } else {
            snippetView.setText("价格待定");
        }
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPeripheralActivity.startActivity(activity, baseBean);
            }
        });

        mMarkerHouse = aMap.addMarker(
                new MarkerOptions()
                        .position(latLonPoint)
//                        .title(title)
                        .icon(BitmapDescriptorFactory.fromView(layMarker))
                        .zIndex(4)
//                        .snippet(snippet)
        );
    }

    protected void doSearchQuery(String key) {
        mKeySearch = key;
        query = new PoiSearch.Query(key, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页

        if (mMarkerHouse != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(MapUtil.convertToLatLngPoint(mMarkerHouse.getPosition()), 5000, true));
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if ("购物".equals(mKeySearch)) {
            cancelLoading();
        }
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    List<PoiItem> items = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    Collections.sort(items, new Comparator<PoiItem>() {
                        @Override
                        public int compare(PoiItem object1,
                                           PoiItem object2) {
                            return object1.getDistance() - object2.getDistance();
                        }
                    });
                    if (items != null && items.size() > 0) {
                        if ("公交".equals(mKeySearch)) {
                            poiItemsBus = new ArrayList<>(items);
                            resetListData(poiItemsBus, adapterBus, btnMoreBus);
                        } else if ("地铁".equals(mKeySearch)) {
                            poiItemsSubway = new ArrayList<>(items);
                            resetListData(poiItemsSubway, adapterSubway, btnMoreSubway);
                        } else if ("学校".equals(mKeySearch)) {
                            poiItemsSchool = new ArrayList<>(items);
                            resetListData(poiItemsSchool, adapterSchool, btnMoreSchool);
                        } else if ("楼盘".equals(mKeySearch)) {
                            poiItemsHouse = new ArrayList<>(items);
                            resetListData(poiItemsHouse, adapterHouse, btnMoreHouse);
                        } else if ("医院".equals(mKeySearch)) {
                            poiItemsHospital = new ArrayList<>(items);
                            resetListData(poiItemsHospital, adapterHospital, btnMoreHospital);
                        } else if ("银行".equals(mKeySearch)) {
                            poiItemsBank = new ArrayList<>(items);
                            resetListData(poiItemsBank, adapterBank, btnMoreBank);
                        } else if ("购物".equals(mKeySearch)) {
                            poiItemsShop = new ArrayList<>(items);
                            resetListData(poiItemsShop, adapterShop, btnMoreShop);
                        }
                    } else {
                        showTip("无搜索结果");
                    }
                }
            } else {
                showTip("无搜索结果");
            }
        } else {
            showTip("错误码：" + rcode);
        }
        if ("地铁".equals(mKeySearch)) {
            doSearchQuery("公交");
        } else if ("公交".equals(mKeySearch)) {
            doSearchQuery("学校");
        } else if ("学校".equals(mKeySearch)) {
            doSearchQuery("楼盘");
        } else if ("楼盘".equals(mKeySearch)) {
            doSearchQuery("医院");
        } else if ("医院".equals(mKeySearch)) {
            doSearchQuery("银行");
        } else if ("银行".equals(mKeySearch)) {
            doSearchQuery("购物");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        LogOut.d(TAG, "******activate****************************");
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }
    }

    @Override
    public void deactivate() {
        LogOut.d(TAG, "****** deactivate ****************************");
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private double myLat, myLon;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            myLat = aMapLocation.getLatitude();
            myLon = aMapLocation.getLongitude();
            mlocationClient.stopLocation();
            cancelLoading();
            MapUtil.startNavigation(activity, myLat, myLon, mMarkerHouse.getPosition().latitude, mMarkerHouse.getPosition().longitude, "楼盘地址");
        }
    }

    private void resetListData(List<PoiItem> items, PoiItemAdapter adapter, CheckBox checkBox) {
        if (items.size() <= 3) {
            checkBox.setVisibility(View.GONE);
            adapter.setNewData(items);
        } else {
            checkBox.setVisibility(View.VISIBLE);
            adapter.setNewData(items.subList(0, 3));
        }
    }

    private void exchangeListData(List<PoiItem> items, PoiItemAdapter adapter, CheckBox checkBox) {
        if (checkBox.getVisibility() != View.VISIBLE) {
            return;
        }
        if (checkBox.isChecked()) {
            checkBox.setText("收起");
            adapter.setNewData(items);
        } else {
            checkBox.setText("查看更多");
            adapter.setNewData(items.subList(0, 3));
        }
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    @OnClick({R.id.btnNavigation, R.id.btnMoreBus, R.id.btnMoreSubway, R.id.btnMoreSchool, R.id.btnMoreHouse, R.id.btnMoreHospital, R.id.btnMoreBank, R.id.btnMoreShop, R.id.rl_collect, R.id.rl_ask, R.id.rl_sub, R.id.tv_look, R.id.tv_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMoreBus:
                exchangeListData(poiItemsBus, adapterBus, btnMoreBus);
                break;
            case R.id.btnMoreSubway:
                exchangeListData(poiItemsSubway, adapterSubway, btnMoreSubway);
                break;
            case R.id.btnMoreSchool:
                exchangeListData(poiItemsSchool, adapterSchool, btnMoreSchool);
                break;
            case R.id.btnMoreHouse:
                exchangeListData(poiItemsHouse, adapterHouse, btnMoreHouse);
                break;
            case R.id.btnMoreHospital:
                exchangeListData(poiItemsHospital, adapterHospital, btnMoreHospital);
                break;
            case R.id.btnMoreBank:
                exchangeListData(poiItemsBank, adapterBank, btnMoreBank);
                break;
            case R.id.btnMoreShop:
                exchangeListData(poiItemsShop, adapterShop, btnMoreShop);
                break;
            case R.id.rl_collect:
                final LoginResult loginCache = dao.getLoginCache();
                if (dao.isLogin()) {
                    if (!iv_collect.isChecked()) {
                        iv_collect.setChecked(true);
                        if (!TextUtils.isEmpty(dev_id) && !searchSingleton.collectList.contains(dev_id)) {
                            searchSingleton.collectList.add(dev_id);
                            EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                            mPresenter.collectHouse(dev_id, baseBean.devName, loginCache.userCode, loginCache.token, "0", "");
                        }

                    } else {
                        iv_collect.setChecked(false);
                        if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
                            searchSingleton.collectList.remove(dev_id);
                            EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                            mPresenter.delCollectHouse(dev_id, baseBean.devName, loginCache.userCode, loginCache.token, "0", "");
                        }

//                        new AlertDialog.Builder(HousesAndTypeActivity.this).setTitle("提示")
//                                .setMessage("是否取消收藏 ")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
//                                            searchSingleton.collectList.remove(dev_id);
//                                            EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
//                                            mPresenter.delCollectHouse(dev_id,loginCache.userCode,loginCache.token,"0","");
//                                        }
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        iv_collect.setChecked(true);
//                                    }
//                                })
//                                .show();

                    }
                } else {
                    iv_collect.setChecked(false);
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                }
                break;
            case R.id.rl_ask:
                CustomerUtils.showTip(this, "敬请期待");
                break;
            case R.id.rl_sub:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
                    return;
                }
                if (baseBean == null)
                    return;
                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
                break;
            case R.id.tv_look:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

                if (alredyList != null && alredyList.contains(dev_id)) {
                    CustomerUtils.showTip(this, "您已预约该楼盘");
                } else {
                    searchSingleton.lookHouse.add(activity);
                    startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                }
                break;
            case R.id.tv_call:
                if (baseBean == null)
                    return;
                call(baseBean.contactPhone);
                break;
            case R.id.btnNavigation:
                if (mlocationClient != null) {
                    showLoading();
                    mlocationClient.startLocation();
                }
                break;
        }
    }

    private List<String> alredyList;

    /**
     * 已经预约的楼盘
     *
     * @param alredyList
     */
    @Override
    public void showAlreadyHouse(List<String> alredyList) {
        this.alredyList = alredyList;
    }

    @Override
    public void showCollect() {
        CustomerUtils.showTip(this, "收藏成功");
        MessageEvent event = new MessageEvent(HOUSES_INDEXT_COLLECTION);
        event.msgI = 1;
        EventBus.getDefault().post(event);
    }

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(activity, "取消收藏");
        MessageEvent event = new MessageEvent(HOUSES_INDEXT_COLLECTION);
        event.msgI = 2;
        EventBus.getDefault().post(event);
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */
    public void call(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapAroundActivityPermissionsDispatcher.callIntentWithCheck(MapAroundActivity.this);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission((Manifest.permission.CALL_PHONE))
    public void callIntent() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapAroundActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
