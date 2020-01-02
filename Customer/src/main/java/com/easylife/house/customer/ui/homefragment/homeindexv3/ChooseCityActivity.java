package com.easylife.house.customer.ui.homefragment.homeindexv3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PYListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.PYBean;
import com.easylife.house.customer.bean.py.Area;
import com.easylife.house.customer.bean.py.City;
import com.easylife.house.customer.bean.py.PinyinComparator;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.SideBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.dao.ServerDao.KEY_CITY;
import static com.easylife.house.customer.dao.ServerDao.KEY_REGIST_AREA;


/**
 * 选择城市
 */
@RuntimePermissions
public class ChooseCityActivity extends BaseActivity implements RequestManagerImpl {

    private Button btnOk;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.user_activity_city, null);
    }

    /**
     * @param activity
     * @param cityId
     * @param city
     * @param getAreaData 是否是获取区域
     * @param requestCode
     */
    public static void startActivity(Activity activity, String cityId, String city, boolean getAreaData, int requestCode) {
        Intent intent = new Intent(activity, ChooseCityActivity.class);
        intent.putExtra("areaid", cityId);
        intent.putExtra("area", city);
        intent.putExtra("getAreaData", getAreaData);
        activity.startActivityForResult(intent, requestCode);
    }

    public static String getSelectedId(Intent data) {
        if (data == null)
            return null;
        return data.getStringExtra("areaid");
    }

    public static String getSelectedText(Intent data) {
        if (data == null)
            return null;
        return data.getStringExtra("area");
    }

    @Override
    protected void initView() {
        sideBar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        tvLocalCity = (TextView) findViewById(R.id.tvLocationCity);
        btnOk = (Button) findViewById(R.id.btnLogin);
        sideBar.setTextView(dialog);
        mPYListView = (ListView) findViewById(R.id.mPyList);

        initData();
    }

    private String city = "北京市", cityId = "110100";
    private String cityLocate, cityIdLocate;
    private boolean getAreaData;

    protected void initData() {
        // 建立关联
        adapter = new PYListAdapter(this);
        mPYListView.setAdapter(adapter);

        city = getIntent().getStringExtra("area");
        cityId = getIntent().getStringExtra("areaid");
        getAreaData = getIntent().getBooleanExtra("getAreaData", false);
        ChooseCityActivityPermissionsDispatcher.locationStartWithCheck(ChooseCityActivity.this);

//        if (!TextUtils.isEmpty(city)) {
//            tvLocalCity.setText(city);
//        }

        findViewById(R.id.btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseCityActivityPermissionsDispatcher.locationStartWithCheck(ChooseCityActivity.this);
            }
        });
        findViewById(R.id.rl_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = cityLocate;
                cityId = cityIdLocate;
                setLocation();
                finish();
            }
        });
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
                finish();
            }
        });
        getNetData();

        // set event
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mPYListView.setSelection(position);
                }
            }
        });
        mPYListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onClickItem(adapter.getList().get(position));
            }
        });


    }

    @Override
    protected void setActionBarDetail() {

    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            cancleLoading();
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    cityLocate = amapLocation.getCity();
                    cityIdLocate = amapLocation.getAdCode();
                    if (!TextUtils.isEmpty(cityIdLocate) && cityIdLocate.length() > 4)
                        cityIdLocate = cityIdLocate.substring(0, 4) + "00";
                    if (TextUtils.isEmpty(cityLocate)) {
                        ToastUtils.showShort("定位失败，请到信号良好的位置重试");
                        return;
                    }
                    try {
                        cityIdLocate = mapCity.get(cityLocate).getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogOut.d(TAG, cityLocate + "," + cityIdLocate);
                    city = cityLocate;
                    cityId = cityIdLocate;
//                    setResult(RESULT_OK, new Intent().putExtra("area", cityLocate).putExtra("areaid", cityIdLocate));
                    tvLocalCity.setText(cityLocate);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    /**
     * 注册定位
     */
    @NeedsPermission((Manifest.permission.ACCESS_FINE_LOCATION))
    public void locationStart() {
        LogOut.d("cityLocate:", cityLocate);
        if (!TextUtils.isEmpty(cityLocate)) {
            setResult(RESULT_OK, new Intent().putExtra("area", cityLocate).putExtra("areaid", cityIdLocate));
            tvLocalCity.setText(cityLocate);
            return;
        }
//        showLoading();
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(10000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChooseCityActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 有字母排序的ListView
     */
    private ListView mPYListView;
    private PYListAdapter adapter;
    private Map<String, City> mapCity;
    private SideBar sideBar;
    private TextView dialog, tvLocalCity;

    protected void onClickItem(final PYBean item) {
        // 点击事件处理
        if (item.getSelected()) {
            ((City) item).select(false);
        } else {
            ((City) item).select(true);
            City c = (City) item;
            city = c.city;
            cityId = c.cityid;

            for (PYBean it : adapter.getList()) {
                if (!it.getId().equals(c.getId())) {
                    ((City) it).select(false);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void setLocation() {
        String area = city;
        String areaID = cityId;

        if (TextUtils.isEmpty(area)) {
            area = "北京市";
            areaID = "110100";
        }

        Intent intent = new Intent();
        intent.putExtra("area", area);
        intent.putExtra("areaid", areaID);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void getNetData() {
        // 获取全部城市时id传0
        showLoading();
        if (getAreaData) {
            mDao.selectArea(KEY_REGIST_AREA, ServerDao.TYPE_SELECT_AREA, cityId, this);
        } else {
            mDao.selectArea(KEY_CITY, ServerDao.TYPE_SELECT_CITY, "0", this);
        }

    }

    @Override
    public void onSuccess(String response, int requestType) {
        cancleLoading();
        switch (requestType) {
            case KEY_CITY:
                mapCity = new HashMap<>();
                List<City> citys = new Gson().fromJson(response, new TypeToken<List<City>>() {
                }.getType());
                if (citys == null || citys.size() == 0) {
                    return;
                }
                List<City> dataC = new ArrayList<>();
                for (City a : citys) {
                    City s = new City(a.cityid, a.city);
                    dataC.add(s);
                    mapCity.put(s.city, s);
                }
                Collections.sort(dataC, new PinyinComparator());
                adapter.setData(dataC);
                break;
            case KEY_REGIST_AREA:
                List<Area> areas = new Gson().fromJson(response, new TypeToken<List<Area>>() {
                }.getType());
                if (areas == null || areas.size() == 0) {
                    return;
                }
                List<Area> data = new ArrayList<>();
                for (Area a : areas) {
                    Area s = new Area(a.areaid, a.area);
                    data.add(s);
                }
                Collections.sort(data, new PinyinComparator());
                adapter.setData(data);
                break;
        }
    }


    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancleLoading();
    }
}
