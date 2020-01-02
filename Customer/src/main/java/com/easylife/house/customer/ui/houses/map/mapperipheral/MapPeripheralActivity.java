package com.easylife.house.customer.ui.houses.map.mapperipheral;


import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.map.MyCloundOverlay;
import com.easylife.house.customer.ui.houses.map.MyPoiOverlay;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.R.id.snippet;

/**
 * 周边配套-地图展示
 */
public class MapPeripheralActivity extends MVPBaseActivity<MapPeripheralContract.View, MapPeripheralPresenter> implements
        MapPeripheralContract.View, AMap.OnMarkerClickListener, PoiSearch.OnPoiSearchListener,
        AMap.InfoWindowAdapter, AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {

    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.btnBus)
    RadioButton btnBus;
    @Bind(R.id.btnSubway)
    RadioButton btnSubway;
    @Bind(R.id.btnSchool)
    RadioButton btnSchool;
    @Bind(R.id.btnHouse)
    RadioButton btnHouse;
    @Bind(R.id.btnHospital)
    RadioButton btnHospital;
    @Bind(R.id.btnBank)
    RadioButton btnBank;
    @Bind(R.id.btnShop)
    RadioButton btnShop;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.map_activity_house_peripheral, null);
    }

    public static void startActivity(Activity activity, HousesDetailBaseBean detail) {
        activity.startActivity(new Intent(activity, MapPeripheralActivity.class)
                .putExtra("detail", detail)
        );
    }

    private AMap aMap;
    private String mTableIDHouse;
    private UiSettings mUiSettings;

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private MyPoiOverlay poiOverlay;// poi图层
    private MyCloundOverlay cloundOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private String mKeySearch;
    private Marker mMarkerHouse;
    private HousesDetailBaseBean detail;
    private double lat, lon;

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;


    @Override
    protected void initView() {
        hideNoNetView();
        detail = (HousesDetailBaseBean) getIntent().getSerializableExtra("detail");

        try {
            mTableIDHouse = detail.tableDevId;
            lat = detail.projectCoordinate.get(1);
            lon = detail.projectCoordinate.get(0);
        } catch (Exception e) {
            LogOut.d(TAG, e.toString());
            showTip("楼盘数据错误");
            finish();
        }


//        mTableIDHouse = "58d209c32376c11121ce764f";
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
    }

    private void setUpMap() {

        mUiSettings.setRotateGesturesEnabled(false);// 禁止旋转
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);

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

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView == null)
            return;
        mapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        cancelLoading();
        aMap.clear();
        addLocateMarker();
    }

    private void addLocateMarker() {
        MapUtil.moveToCurrentLocation(aMap, lat, lon);

        View window = getLayoutInflater().inflate(R.layout.custom_info_window_current, null);
        ((TextView) window.findViewById(R.id.title)).setText(detail.devName);
        ((TextView) window.findViewById(snippet)).setText(detail.addressDistrict + detail.addressTown + detail.addressDetail);
        mMarkerHouse = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
//                .title(detail.devName)
//                .snippet(detail.addressDetail)
                .icon(BitmapDescriptorFactory.fromView(window)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getPosition().latitude == mMarkerHouse.getPosition().latitude && marker.getPosition().longitude == mMarkerHouse.getPosition().longitude) {
            LogOut.d("onMarkerClick - mlocationClient == null ：  ", mlocationClient == null);
            if (mlocationClient != null) {
                showLoading();
                mlocationClient.startLocation();
            }
            return true;
        }
        return false;
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
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        //清理之前搜索结果的marker
                        if (poiOverlay != null) {
                            poiOverlay.removeFromMap();
                        }
                        poiOverlay = new MyPoiOverlay(activity, aMap, poiItems, getPointIcon());
                        aMap.clear();
                        poiOverlay.addToMap();
                        addLocateMarker();
                        MapUtil.moveToCurrentLocation(aMap, mMarkerHouse.getPosition().latitude, mMarkerHouse.getPosition().longitude);
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
    }

    private BitmapDescriptor getPointIcon() {
        if ("公交".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_bus));
        } else if ("地铁".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_subway));
        } else if ("学校".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_school));
        } else if ("楼盘".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_house));
        } else if ("医院".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_hospital));
        } else if ("银行".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_bank));
        } else if ("购物".equals(mKeySearch)) {
            return BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.mipmap.location_shop));
        }
        return BitmapDescriptorFactory.defaultMarker();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
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
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @OnClick({R.id.btnBus, R.id.btnSubway, R.id.btnSchool, R.id.btnHouse, R.id.btnHospital, R.id.btnBank, R.id.btnShop})
    public void onClick(View view) {
        if (mMarkerHouse == null) {
            showTip("楼盘坐标错误");
            return;
        }
        if (poiOverlay != null) {
            poiOverlay.removeFromMap();
        }
        if (cloundOverlay != null) {
            cloundOverlay.removeFromMap();
        }
        switch (view.getId()) {
            case R.id.btnBus:
                doSearchQuery("公交");
                break;
            case R.id.btnSubway:
                doSearchQuery("地铁");
                break;
            case R.id.btnSchool:
                doSearchQuery("学校");
                break;
            case R.id.btnHouse:
                doSearchQuery("楼盘");
                break;
            case R.id.btnHospital:
                doSearchQuery("医院");
                break;
            case R.id.btnBank:
                doSearchQuery("银行");
                break;
            case R.id.btnShop:
                doSearchQuery("购物");
                break;
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View window = null;
        if (marker.getPosition().latitude == mMarkerHouse.getPosition().latitude && marker.getPosition().longitude == mMarkerHouse.getPosition().longitude) {
        } else {
            window = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            ((TextView) window.findViewById(R.id.title)).setText(marker.getTitle());
            ((TextView) window.findViewById(snippet)).setText(marker.getSnippet());
        }
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
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

}
