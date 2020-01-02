package com.easylife.house.customer.ui.houses.map.mapfindhouse;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AreaHousesNum;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.housefilter.HouseFilterActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.util.CloudOverlay;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.SearchResultOverlay;
import com.easylife.house.customer.util.UiUtil;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.ImageViewTouch;
import com.easylife.house.customer.view.PathView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 周边配套-地图展示
 */
@RuntimePermissions
public class MapFindHouseActivity extends MVPBaseActivity<MapFindHouseContract.View, MapFindHousePresenter>
        implements MapFindHouseContract.View, AMap.OnMarkerClickListener,
        AMapLocationListener, AMap.OnCameraChangeListener, LocationSource, CloudSearch.OnCloudSearchListener {

    public static final float ZOOM_LEVEL_MAX = 18;
    public static final float ZOOM_LEVEL_STREET = 15;
    public static final float ZOOM_LEVEL_LIMIT = 12;
    public static final float ZOOM_LEVEL_CITY = 10;

    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.pathView)
    PathView pathView;
    @Bind(R.id.btnFilter)
    ButtonTouch btnFilter;
    @Bind(R.id.btnLocate)
    ImageViewTouch btnLocate;
    @Bind(R.id.btnDrawable)
    ImageViewTouch btnDrawable;
//    @Bind(R.id.viewPathBg)
//    View viewPathBg;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.map_activity_house_find, null);
    }

    public static void startActivity(Activity activity, String city, String cityId) {
        activity.startActivityForResult(new Intent(activity, MapFindHouseActivity.class)
                .putExtra("city", city)
                .putExtra("cityId", cityId), 0);
    }


    private Polygon polygon;
    private AMap aMap;
    private UiSettings mUiSettings;
    private boolean isAreaData = true;
    private boolean isShowSearchResult = false;
    private View detailPopView;
    private ImageView imgBg;
    private LinearLayout detailPopViewContent;
    private TextView tvHouseName;
    private TextView tvHousePrice;
    private TextView tvHouseStructure;
    private LinearLayout layHouseStructure;
    private LinearLayout layArea;
    private TextView tvHouseArea;
    private FlowViewGroup groupTags;

    private SearchRequestBean searBean = new SearchRequestBean();

    private String mTableIDArea = "58d4d87e2376c11121f9a54d";
    private String mTableIDHouse = "58d209c32376c11121ce764f";
    private CloudSearch mCloudSearch;
    private CloudOverlay mPoiCloudOverlay;
    private SearchResultOverlay mSearchOverlay;
    private CloudSearch.Query mQuery;

    private TreeMap<String, Map<String, CloudItem>> mapHouses = new TreeMap<>();// 区域云图数据缓存:  城市-楼盘名称-楼盘数据
    private TreeMap<String, Map<String, CloudItem>> mapAreas = new TreeMap<>();// 楼盘云图数据缓存:  城市-楼盘名称-楼盘数据

    private TreeMap<String, HousesDetailBaseBean> mapSearchResultHouses;
    private TreeMap<String, City> mapCitys;
    private Map<String, LatLonPoint> mapPointCitys;

    private PubTipDialog dialog;
    private String city;
    private String cityId;
    private boolean isLocateFirst = true;
    private String devID;
    private String avgprice;
    private String devbedroom;
    private String devsquaremetre;
    private String feature;
    private String image;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private float zoomCurrent;

    @Override
    protected void initView() {
        hideNoNetView();
        mapView.onCreate(savedInstanceState);
        city = getIntent().getStringExtra("city");
        cityId = getIntent().getStringExtra("cityId");

        detailPopView = getLayoutInflater().inflate(R.layout.map_house_detail, null);
        layHouseStructure = (LinearLayout) detailPopView.findViewById(R.id.layHouseStructure);
        layArea = (LinearLayout) detailPopView.findViewById(R.id.layArea);
        tvHouseName = (TextView) detailPopView.findViewById(R.id.tvHouseName);
        imgBg = (ImageView) detailPopView.findViewById(R.id.imgBg);
        detailPopViewContent = (LinearLayout) detailPopView.findViewById(R.id.detailView);
        tvHousePrice = (TextView) detailPopView.findViewById(R.id.tvHousePrice);
        tvHouseStructure = (TextView) detailPopView.findViewById(R.id.tvHouseStructure);
        tvHouseArea = (TextView) detailPopView.findViewById(R.id.tvHouseArea);
        groupTags = (FlowViewGroup) detailPopView.findViewById(R.id.groupTags);
        detailPopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devID = null;
                detailPopView.setVisibility(View.GONE);
            }
        });
        detailPopViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 楼盘详情
                HouseDetailActivity.startActivity(activity, devID, false, 0);
            }
        });
        contentLayout.addView(detailPopView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        detailPopView.setVisibility(View.GONE);

        mPresenter.getCityList();
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        pathView.setOnFinishListener(new PathView.OnFinishListener() {
            @Override
            public void onFinish(Path p, RectF r) {
                pathView.setVisibility(View.GONE);
//                viewPathBg.setVisibility(View.GONE);
                addOverPolygon();
                searchByPolygon();
            }
        });
    }

    /**
     * 添加圈图图层
     */
    private void addOverPolygon() {
        Point point = new Point(0, 0);
        Point point1 = new Point(pathView.getWidth(), pathView.getHeight());

        LatLng southwest = aMap.getProjection().fromScreenLocation(point);
        LatLng northeast = aMap.getProjection().fromScreenLocation(point1);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(southwest)
                .include(northeast)
                .build();

        aMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0f)
                .image(BitmapDescriptorFactory
                        .fromBitmap(MapUtil.convertViewToBitmap(pathView)))
                .positionFromBounds(bounds));

        List<Point> points = pathView.getPoints();
        if (points == null || points.size() == 0)
            return;
        PolygonOptions options = new PolygonOptions()
                .fillColor(Color.argb(50, 1, 1, 1))
                .visible(false)
                .strokeWidth(0);
        for (Point p : points) {
            options.add(aMap.getProjection().fromScreenLocation(p));
        }
        polygon = aMap.addPolygon(options);
    }

    private void setUpMap() {
        mCloudSearch = new CloudSearch(this);
        mCloudSearch.setOnCloudSearchListener(this);

        mUiSettings.setRotateGesturesEnabled(false);// 禁止旋转
        mUiSettings.setScaleControlsEnabled(true);// 显示比例尺
        mUiSettings.setZoomControlsEnabled(false);// 显示缩放按钮
//        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setMaxZoomLevel(ZOOM_LEVEL_MAX); // 设置最大放大级别

        aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        aMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听
        mUiSettings.setMyLocationButtonEnabled(false); //显示默认的定位按钮
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        aMap.setOnCameraChangeListener(this);
    }

    @NeedsPermission((Manifest.permission.ACCESS_FINE_LOCATION))
    public void startLocate() {
        LogOut.d(TAG, city + " startLocate " + cityId);
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(cityId)) {
            if (mlocationClient != null) {
                mlocationClient.startLocation();//启动定位
            }
        } else {
            searBean = new SearchRequestBean();
            searBean.city = city;
            searBean.cityId = cityId;

            btnRightText.setText(city);

            isLocateFirst = false;
            moveToCityCenter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapFindHouseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void addMarker(LatLonPoint latLonPoint, String title, String snippet) {
        if (latLonPoint == null) {
            return;
        }
        LogOut.d("addMarker : ", title + " ,  " + snippet + " , latLonPoint:(" + latLonPoint.getLatitude() + "," + latLonPoint.getLongitude() + ")");
        View layMarker = LayoutInflater.from(activity).inflate(R.layout.layout_marker, null);
        ((TextView) layMarker.findViewById(R.id.title)).setText(title);
        ((TextView) layMarker.findViewById(R.id.snippet)).setText(isAreaData ? snippet : (PriceUtil.formatPrice(snippet, "元/㎡")));
        aMap.addMarker(
                new MarkerOptions()
                        .position(MapUtil.convertToLatLng(latLonPoint))
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromView(layMarker))
                        .zIndex(4)
                        .snippet(snippet));
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this, msg);
    }

    @Override
    public void moveToCityCenter() {
        LogOut.d(" moveToCityCenter :  ", city);
        if (TextUtils.isEmpty(city))
            return;
        if (mapPointCitys == null) {
            mapPointCitys = new HashMap<>();
        }
        LatLonPoint point = mapPointCitys.get(cityId);
        if (point == null) {
            DistrictSearch districtSearch = new DistrictSearch(this);
            districtSearch.setOnDistrictSearchListener(
                    new DistrictSearch.OnDistrictSearchListener() {
                        @Override
                        public void onDistrictSearched(DistrictResult result) {
                            LogOut.d(" onDistrictSearched :  ", result.toString());
                            if (result != null) {
                                if (result.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {
                                    List<DistrictItem> district = result.getDistrict();
                                    // 将查询得到的区划的下级区划写入缓存
                                    DistrictItem districtItem = district.get(0);
                                    mapPointCitys.put(districtItem.getAdcode(),
                                            districtItem.getCenter());
                                    MapUtil.moveToPoint(aMap, districtItem.getCenter().getLatitude(), districtItem.getCenter().getLongitude(), ZOOM_LEVEL_CITY);
                                } else {
                                    LogOut.d(TAG, result.getAMapException().getErrorCode());
                                }
                            }

                        }
                    });
            // 异步查询行政区
            DistrictSearchQuery query = new DistrictSearchQuery();
            query.setKeywords(city);
            districtSearch.setQuery(query);
            districtSearch.searchDistrictAsyn();
        } else {
            MapUtil.moveToPoint(aMap, point.getLatitude(), point.getLongitude(), ZOOM_LEVEL_CITY);
        }
        searchByLocal();
    }

    @Override
    public void initCity(List<City> data) {
        cancelLoading();
        LogOut.d(TAG, "initCity :" + city);
        mapCitys = new TreeMap<>();
        try {
            for (City c : data) {
                mapCitys.put(c.getId(), c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MapFindHouseActivityPermissionsDispatcher.startLocateWithCheck(this);
    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("选择城市");
        UiUtil.drawableRight(activity, btnRightText, R.mipmap.arrow_down);
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailPopView.getVisibility() == View.VISIBLE) {
                    devID = null;
                    detailPopView.setVisibility(View.GONE);
                }
                if (dialog == null) {
                    dialog = new PubTipDialog(activity);
                }
                if (mapCitys == null || mapCitys.size() == 0) {
                    showTip("城市数据获取失败");
                    return;
                }
                dialog.showDialogList("请选择购房城市", city, new ArrayList<>(mapCitys.values()), new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        cityId = MapUtil.checkCity(date.getId());
                        city = date.getText();

                        btnRightText.setText(city);

                        searBean = new SearchRequestBean();
                        searBean.city = city;
                        searBean.cityId = cityId;
                        isShowSearchResult = false;
                        isAreaData = true;
                        moveToCityCenter();
                    }
                });
            }
        });
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

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView == null)
            return;
        mapView.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!isShowSearchResult && isAreaData) {
            moveToPoint(marker, ZOOM_LEVEL_LIMIT);
        } else {
            moveToPoint(marker, ZOOM_LEVEL_STREET);
            initHouseDetail(marker.getTitle());
        }
        return true;
    }

    /**
     * 显示楼盘弹窗
     *
     * @param devName
     */
    private void initHouseDetail(String devName) {
        detailPopView.setVisibility(View.VISIBLE);

        if (isShowSearchResult) {
            HousesDetailBaseBean bean = mapSearchResultHouses.get(devName);
            devID = bean.devId;
            avgprice = bean.averPrice;
            devbedroom = bean.devBedroom;
            devsquaremetre = bean.devSquareMetre;
            feature = bean.feature;
            try {
                image = bean.distribution.get(0).url;
            } catch (Exception e) {
                LogOut.d("image: ", e.toString());
            }
        } else {
            CloudItem item = mapHouses.get(city).get(devName);
            devID = MapUtil.checkParams("devid", item);
            avgprice = MapUtil.checkParams("avgprice", item);
            devbedroom = MapUtil.checkParams("devbedroom", item);
            devsquaremetre = MapUtil.checkParams("devsquaremetre", item);
            feature = MapUtil.checkParams("feature", item);
            image = MapUtil.checkParams("image", item);
        }

        tvHouseName.setText(devName);
        String avgpriceStr = PriceUtil.formatPrice(avgprice, "元/㎡");
        if ("价格待定".equals(avgpriceStr)) {
            tvHousePrice.setText(avgpriceStr);
        } else {
            tvHousePrice.setText("均价" + PriceUtil.formatPrice(avgprice, "元/㎡"));
        }

        if (TextUtils.isEmpty(devbedroom)) {
            layHouseStructure.setVisibility(View.GONE);
        } else {
            tvHouseStructure.setText(devbedroom);
            layHouseStructure.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(devsquaremetre)) {
            layArea.setVisibility(View.GONE);
        }else{
            tvHouseArea.setText(devsquaremetre);
            layArea.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(image)) {
            CacheManager.initCenterCropImage(activity, imgBg, image);
        }

        String[] tags = feature.split(",");
        if (tags == null || tags.length == 0)
            return;
        groupTags.addViews(R.layout.item_map_house_detail_tag, tags, null);
    }

    private boolean isPolygon;

    @OnClick({R.id.btnFilter, R.id.btnLocate, R.id.btnDrawable})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilter:
                HouseFilterActivity.startActivity(activity, true, city, cityId, 3);
                break;
            case R.id.btnDrawable:
                aMap.clear();
                if (isPolygon) {
                    isPolygon = false;

                    pathView.setVisibility(View.GONE);
//                    viewPathBg.setVisibility(View.GONE);
                    btnDrawable.setImageResource(R.mipmap.map_find_drawable_n);

                    reload(); // 关闭圈图，重新加载数据
                } else {
                    isPolygon = true;


                    if (zoomCurrent < ZOOM_LEVEL_STREET) {
                        zoomCurrent = ZOOM_LEVEL_STREET;
                        MapUtil.moveToPoint(aMap, aMap.getCameraPosition().target.latitude, aMap.getCameraPosition().target.longitude, zoomCurrent);
                    }

                    pathView.reset();
//                    viewPathBg.setVisibility(View.VISIBLE);
                    pathView.setVisibility(View.VISIBLE);
                    btnDrawable.setImageResource(R.mipmap.map_find_drawable_y);
                }
                break;
            case R.id.btnLocate:
                if (mAmapLocation != null) {
                    mListener.onLocationChanged(mAmapLocation);
                } else {
                    MapFindHouseActivityPermissionsDispatcher.startLocateWithCheck(this);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    SearchRequestBean s = (SearchRequestBean) data.getSerializableExtra(SearchRequestBean.SEARCH_BEAN);
                    if (s == null) {
                        searBean = new SearchRequestBean();
                        searBean.city = city;
                        searBean.cityId = cityId;
                    } else {
                        searBean = s;
                    }
                    isPolygon = false;
                    isShowSearchResult = true;
//                    if (isAreaData) {
//                        mPresenter.getSearchResultArea(searBean);
//                    } else {
                    zoomCurrent = ZOOM_LEVEL_STREET;
                    mPresenter.getSearchResultDev(searBean);
//                    }
                    break;
            }
        }
    }

    /**
     * 地图的缩放级别一共分为 18 级，从 3 到 21。数字越大，展示的图面信息越精细。
     */
    private void moveToPoint(Marker marker, float zoomLevel) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(marker.getPosition().latitude,
                        marker.getPosition().longitude), zoomLevel));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        zoomCurrent = cameraPosition.zoom;
        boolean temp = isAreaData;
        if (zoomCurrent < ZOOM_LEVEL_LIMIT) {
            // 区域级别，显示
            isAreaData = true;
        } else {
            // 显示详细楼盘信息，街道级别
            isAreaData = false;
        }
        if (isPolygon)
            return;
        if (temp != isAreaData) {
            if (isShowSearchResult) {
                if (isAreaData) {
                    mPresenter.getSearchResultArea(searBean);
                } else {
                    mPresenter.getSearchResultDev(searBean);
                }
            } else {
                searchByLocal();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        Log.d(TAG, "  activate *---------------");
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
            mLocationOption.setInterval(1000);
            mLocationOption.setOnceLocationLatest(true);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }
    }

    @Override
    public void deactivate() {
        Log.d(TAG, "  deactivate *---------------");
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private AMapLocation mAmapLocation;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.d(TAG, "  onLocationChanged *---------------");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mAmapLocation = amapLocation;
                city = mAmapLocation.getCity();
                cityId = MapUtil.checkCity(mAmapLocation.getAdCode());

                if (!isLocateFirst) {
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                }
                mlocationClient.stopLocation();
            } else {
                if (isLocateFirst) {
                    // 定位失败，使用默认城市-北京
                    city = "北京市";
                    cityId = "110100";
                }
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                LogOut.d(TAG, errText);
            }
        }
        searBean.city = city;
        searBean.cityId = cityId;

        btnRightText.setText(city);

        isLocateFirst = false;
        moveToCityCenter();
    }

    @Override
    public void onCloudSearched(final CloudResult result, final int i) {
        LogOut.d(TAG, "  onCloudSearched   ");
        if (i == AMapException.CODE_AMAP_SUCCESS && result != null && result.getQuery() != null) {
            initCloudResult(result);
        } else {
            cancelLoading();
            showTip("数据获取失败");
        }
    }

    /**
     * 本地检索
     */
    public void searchByLocal() {
        showLoading();
        aMap.clear();

        LogOut.d(TAG, " searchByLocal " + city
                + "  ,mTableIDArea - " + mTableIDArea
                + " ,mTableIDHouse - " + mTableIDHouse
        );
        if (mapCitys != null && mapCitys.size() > 0) {
            mTableIDArea = mapCitys.get(cityId).tableId;
            mTableIDHouse = mapCitys.get(cityId).tableDevId;
        }

        Map<String, CloudItem> mCloudItems = null;
        // 数据的初始化
        if (isAreaData) {
            mCloudItems = mapAreas.get(city);
        } else {
            mCloudItems = mapHouses.get(city);
        }
        if (mCloudItems != null && mCloudItems.size() > 0) {
            List<CloudItem> list = new ArrayList<>(mCloudItems.values());
            mPoiCloudOverlay = new CloudOverlay(aMap, list);
            mPoiCloudOverlay.removeFromMap();
            mPoiCloudOverlay.addToMap();
            for (CloudItem item : list) {
                addMarker(item.getLatLonPoint(), item.getTitle(), MapUtil.checkParams(isAreaData ? "housenumber" : "avgprice", item));
            }
            cancelLoading();
        } else {
            CloudSearch.SearchBound bound = new CloudSearch.SearchBound(city);
            try {
                mQuery = new CloudSearch.Query(isAreaData ? mTableIDArea : mTableIDHouse, "", bound);
                mCloudSearch.searchCloudAsyn(mQuery);
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多边形检索
     */
    public void searchByPolygon() {
        int countResult = 0;
        if (polygon != null) {
            if (isShowSearchResult) {
                if (mapSearchResultHouses == null || mapSearchResultHouses.size() == 0)
                    return;
                Collection values = mapSearchResultHouses.values();
                for (Object item : values) {
                    HousesDetailBaseBean c = (HousesDetailBaseBean) item;
                    if (c.projectCoordinate != null && c.projectCoordinate.size() == 2) {
                        countResult++;

                        if (polygon.contains(new LatLng(c.projectCoordinate.get(1), c.projectCoordinate.get(0))))
                            addMarker(new LatLonPoint(c.projectCoordinate.get(1), c.projectCoordinate.get(0)),
                                    c.devName, c.averPrice);
                    }
                }
            } else {
                if (mapHouses == null || mapHouses.size() == 0)
                    return;
                Map<String, CloudItem> devs = mapHouses.get(city);
                Collection values = devs.values();
                for (Object item : values) {
                    CloudItem c = (CloudItem) item;
                    if (polygon.contains(MapUtil.convertToLatLng(c.getLatLonPoint()))) {
                        countResult++;

                        addMarker(c.getLatLonPoint(), c.getTitle(), MapUtil.checkParams("avgprice", c));
                    }
                }
            }
        }
        if (countResult == 0) {
            showTip("该区域内暂无房源");
        }
    }

    private void reload() {
        searchByLocal();
    }

    @Override
    public void initCloudResult(CloudResult result) {
        LogOut.d(TAG, " initCloudResult ");
        aMap.clear();
        if (result.getQuery().equals(mQuery)) {
            Map<String, CloudItem> cloudItems = null;
            // 数据的初始化
            if (isAreaData) {
                cloudItems = mapAreas.get(city);
                if (cloudItems == null || cloudItems.size() == 0) {
                    List<CloudItem> list = result.getClouds();
                    cloudItems = new HashMap<>();
                    for (CloudItem item : list) {
                        cloudItems.put(item.getTitle(), item);
                    }
                    mapAreas.put(city, cloudItems);
                }
            } else {
                cloudItems = mapHouses.get(city);
                if (cloudItems == null || cloudItems.size() == 0) {
                    List<CloudItem> list = result.getClouds();
                    cloudItems = new HashMap<>();
                    for (CloudItem item : list) {
                        cloudItems.put(item.getTitle(), item);
                    }
                    mapHouses.put(city, cloudItems);
                }
            }
            //　显示数据
            if (cloudItems != null && cloudItems.size() > 0) {
                List<CloudItem> list = new ArrayList<>(cloudItems.values());
                mPoiCloudOverlay = new CloudOverlay(aMap, list);
                mPoiCloudOverlay.removeFromMap();
                mPoiCloudOverlay.addToMap();
                for (CloudItem item : list) {
                    addMarker(item.getLatLonPoint(), item.getTitle(), MapUtil.checkParams(isAreaData ? "housenumber" : "avgprice", item));
                }
            } else {
                CustomerUtils.showTip(this, "没有结果");
            }
        }
        cancelLoading();
    }


    @Override
    public void initSearchResultDev(List<HousesDetailBaseBean> results) {
        cancelLoading();
        aMap.clear();
        int count = 0;
        if (results != null && results.size() > 0) {
            mapSearchResultHouses = new TreeMap<>();
            mSearchOverlay = new SearchResultOverlay(aMap, results);
            mSearchOverlay.removeFromMap();
            mSearchOverlay.addToMap();
            LatLonPoint point = null;
            for (HousesDetailBaseBean item : results) {
                mapSearchResultHouses.put(item.devName, item);
                if (item.projectCoordinate != null) {
                    count++;
                    point = new LatLonPoint(item.projectCoordinate.get(1), item.projectCoordinate.get(0));
                    addMarker(point, item.devName, item.averPrice);
                }
            }
            MapUtil.moveToPoint(aMap, point.getLatitude(), point.getLongitude(), zoomCurrent);
        }
        if (count == 0) {
            showTip("没有符合条件的楼盘");
        }
    }

    @Override
    public void initSearchResultArea(List<AreaHousesNum> results) {
        cancelLoading();
        aMap.clear();
        int count = 0;
        if (results != null && results.size() > 0) {
            LatLonPoint point = null;
            for (AreaHousesNum item : results) {
                if (mapAreas.get(city) != null && mapAreas.get(city).get(item.name) != null
                        && mapAreas.get(city).get(item.name).getLatLonPoint() != null) {
                    count++;
                    point = mapAreas.get(city).get(item.name).getLatLonPoint();
                    addMarker(point, item.name, item.count);
                }
            }
            MapUtil.moveToPoint(aMap, point.getLatitude(), point.getLongitude(), zoomCurrent);
        }
        if (count == 0) {
            showTip("没有符合条件的楼盘");
        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {
    }
}