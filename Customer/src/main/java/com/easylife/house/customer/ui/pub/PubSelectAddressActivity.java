package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.JsonAddress;
import com.easylife.house.customer.util.CityUtils;
import com.easylife.house.customer.util.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 城市联动选择器
 */
public class PubSelectAddressActivity extends Activity {

    public static void startActivity(Activity activity,
                                     String defaultProvince,
                                     String defaultCity,
                                     String defaultArea,
                                     String defaultProvinceId,
                                     String defaultCityId,
                                     String defaultAreaId,
                                     int requestCode) {
        Intent intent = new Intent(activity, PubSelectAddressActivity.class);
        intent.putExtra("defaultProvince", defaultProvince);
        intent.putExtra("defaultCity", defaultCity);
        intent.putExtra("defaultArea", defaultArea);
        intent.putExtra("defaultProvinceId", defaultProvinceId);
        intent.putExtra("defaultCityId", defaultCityId);
        intent.putExtra("defaultAreaId", defaultAreaId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static String getText(Intent data) {
        if (data == null)
            return null;
        return data.getStringExtra("province") +
                data.getStringExtra("city") +
                data.getStringExtra("area");
    }

    public static String[] getTexts(Intent data) {
        if (data == null)
            return null;
        return new String[]{data.getStringExtra("province"),
                data.getStringExtra("city"),
                data.getStringExtra("area")};
    }

    public static String[] getIds(Intent data) {
        if (data == null)
            return null;
        return new String[]{data.getStringExtra("provinceId"),
                data.getStringExtra("cityId"),
                data.getStringExtra("areaId")
        };
    }

    private WheelView wheeViewProvince;
    private WheelView wheeViewCity;
    private WheelView wheeViewArea;
    private TextView mTvCancel, mTvOk;

    private String defaultProvince;
    private String defaultCity;
    private String defaultArea;
    private String defaultProvinceId;
    private String defaultCityId;
    private String defaultAreaId;
    private int defaultIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_wheeview_3);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        defaultProvince = getIntent().getStringExtra("defaultProvince");
        defaultCity = getIntent().getStringExtra("defaultCity");
        defaultArea = getIntent().getStringExtra("defaultArea");
        defaultProvinceId = getIntent().getStringExtra("defaultProvinceId");
        defaultCityId = getIntent().getStringExtra("defaultCityId");
        defaultAreaId = getIntent().getStringExtra("defaultAreaId");

        dataProvince = new Gson().fromJson(CityUtils.getProvinceJson(this), JsonAddress.class).RECORDS;
        dataCity = new Gson().fromJson(CityUtils.getCityJson(this), JsonAddress.class).RECORDS;
        dataArea = new Gson().fromJson(CityUtils.getAreaJson(this), JsonAddress.class).RECORDS;

        wheeViewProvince = (WheelView) findViewById(R.id.wheeViewProvince);
        wheeViewCity = (WheelView) findViewById(R.id.wheeViewCity);
        wheeViewArea = (WheelView) findViewById(R.id.wheeViewArea);
        mTvCancel = (TextView) findViewById(R.id.tvcacle);
        mTvOk = (TextView) findViewById(R.id.tvok);

        wheeViewProvince.setCyclic(false);
        wheeViewCity.setCyclic(false);
        wheeViewArea.setCyclic(false);

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.layout_parent).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                defaultArea = dataAreaTemp.get(wheeViewArea.getCurrentItem()).getPickerViewText();
                defaultAreaId = dataAreaTemp.get(wheeViewArea.getCurrentItem()).getPickerViewId();

                Intent data = new Intent();
                data.putExtra("province", defaultProvince);
                data.putExtra("provinceId", defaultProvinceId);
                data.putExtra("city", defaultCity);
                data.putExtra("cityId", defaultCityId);
                data.putExtra("area", defaultArea);
                data.putExtra("areaId", defaultAreaId);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        initData();
    }

    private ArrayList<JsonAddress.Data> dataProvince = new ArrayList<>();
    private ArrayList<JsonAddress.Data> dataCity = new ArrayList<>();
    private ArrayList<JsonAddress.Data> dataArea = new ArrayList<>();
    private ArrayList<JsonAddress.Data> dataCityTemp = new ArrayList<>();
    private ArrayList<JsonAddress.Data> dataAreaTemp = new ArrayList<>();


    private void initData() {
        if (dataProvince == null || dataProvince.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(defaultProvince)) {
            defaultProvinceId = dataProvince.get(0).getPickerViewId();
            defaultProvince = dataProvince.get(0).getPickerViewText();
        }
        for (int i = 0; i < dataProvince.size(); i++) {
            JsonAddress.Data a = dataProvince.get(i);
            if (TextUtils.isEmpty(a.getPickerViewText())) {
                break;
            }
            if (a.getPickerViewText().equals(defaultProvince)) {
                defaultIndex = i;
                break;
            }
        }
        wheeViewProvince.setAdapter(new ArrayWheelAdapter<>(dataProvince));
        wheeViewProvince.setCurrentItem(defaultIndex);


        dataCityTemp.clear();
        for (JsonAddress.Data item : dataCity) {
            if (defaultProvinceId.equals(item.provinceid)) {
                dataCityTemp.add(item);
            }
        }
        if (TextUtils.isEmpty(defaultCity)) {
            defaultCity = dataCityTemp.get(0).getPickerViewText();
            defaultCityId = dataCityTemp.get(0).getPickerViewId();
        }
        wheeViewCity.setAdapter(new ArrayWheelAdapter<>(dataCityTemp));


        dataAreaTemp.clear();
        for (JsonAddress.Data item : dataArea) {
            if (defaultCityId.equals(item.cityid)) {
                dataAreaTemp.add(item);
            }
        }
        if (TextUtils.isEmpty(defaultArea)) {
            defaultArea = dataAreaTemp.get(0).getPickerViewText();
            defaultAreaId = dataAreaTemp.get(0).getPickerViewId();
        }
        wheeViewArea.setAdapter(new ArrayWheelAdapter<>(dataAreaTemp));

        wheeViewProvince.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                defaultProvinceId = dataProvince.get(index).getPickerViewId();
                defaultProvince = dataProvince.get(index).getPickerViewText();

                dataCityTemp.clear();
                for (JsonAddress.Data item : dataCity) {
                    if (defaultProvinceId.equals(item.provinceid)) {
                        dataCityTemp.add(item);
                    }
                }
                if (TextUtils.isEmpty(defaultCity)) {
                    defaultCity = dataCityTemp.get(0).getPickerViewText();
                    defaultCityId = dataCityTemp.get(0).getPickerViewId();
                }
                defaultIndex = 0;
                for (int i = 0; i < dataCityTemp.size(); i++) {
                    JsonAddress.Data a = dataCityTemp.get(i);
                    if (TextUtils.isEmpty(a.getPickerViewText())) {
                        break;
                    }
                    if (a.getPickerViewText().equals(defaultCity)) {
                        defaultIndex = i;
                        break;
                    }
                }
                wheeViewCity.setAdapter(new ArrayWheelAdapter<>(dataCityTemp));
                wheeViewCity.setCurrentItem(defaultIndex);

                dataAreaTemp.clear();
                defaultIndex = 0;
                if (TextUtils.isEmpty(defaultCityId)) {
                    defaultCityId = dataCityTemp.get(0).getPickerViewId();
                }
                for (JsonAddress.Data item : dataArea) {
                    if (defaultCityId.equals(item.cityid)) {
                        dataAreaTemp.add(item);
                    }
                }
                if (TextUtils.isEmpty(defaultArea)) {
                    defaultArea = dataAreaTemp.get(0).getPickerViewText();
                    defaultAreaId = dataAreaTemp.get(0).getPickerViewId();
                }
                for (int i = 0; i < dataAreaTemp.size(); i++) {
                    JsonAddress.Data a = dataAreaTemp.get(i);
                    if (TextUtils.isEmpty(a.getPickerViewText())) {
                        break;
                    }
                    if (a.getPickerViewText().equals(defaultArea)) {
                        defaultIndex = i;
                        break;
                    }
                }
                wheeViewArea.setAdapter(new ArrayWheelAdapter<>(dataAreaTemp));
                wheeViewArea.setCurrentItem(defaultIndex);
            }
        });
        wheeViewCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                defaultCityId = dataCityTemp.get(index).getPickerViewId();
                defaultCity = dataCityTemp.get(index).getPickerViewText();

                dataAreaTemp.clear();
                defaultIndex = 0;
                for (JsonAddress.Data item : dataArea) {
                    if (defaultCityId.equals(item.cityid)) {
                        dataAreaTemp.add(item);
                    }
                }
                if (TextUtils.isEmpty(defaultArea)) {
                    defaultArea = dataAreaTemp.get(0).getPickerViewText();
                    defaultAreaId = dataAreaTemp.get(0).getPickerViewId();
                }
                for (int i = 0; i < dataAreaTemp.size(); i++) {
                    JsonAddress.Data a = dataAreaTemp.get(i);
                    if (TextUtils.isEmpty(a.getPickerViewText())) {
                        break;
                    }
                    if (a.getPickerViewText().equals(defaultArea)) {
                        defaultIndex = i;
                        break;
                    }
                }
                wheeViewArea.setAdapter(new ArrayWheelAdapter<>(dataAreaTemp));
                wheeViewArea.setCurrentItem(defaultIndex);
            }
        });
    }
}
