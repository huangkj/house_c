package com.easylife.house.customer.view;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemClickListener2;
import com.easylife.house.customer.bean.JsonAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * 省市二级联动选择列表弹窗
 */
public class ProvinceCitySelectPopWindow extends PopupWindow {
    private final Context mContext;
    private ItemClickListener2<JsonAddress.Data> listener;

    public ProvinceCitySelectPopWindow(Context context, ItemClickListener2<JsonAddress.Data> listener) {
        mContext = context;
        this.listener = listener;
        initView();
    }

    public void setData(List<JsonAddress.Data> dataProvince, List<JsonAddress.Data> dataCity) {
        this.dataProvince = dataProvince;
        this.dataCity = dataCity;
        initData();
    }

    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.pop_province_city_select, null);
        wheelProvince = popView.findViewById(R.id.wheelProvince);
        wheelCity = popView.findViewById(R.id.wheelCity);
        wheelProvince.setCyclic(false);
        wheelCity.setCyclic(false);

        popView.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popView.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.itemClick(defaultProvince, defaultCity);
                dismiss();
            }
        });
        popView.findViewById(R.id.pop_v_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(popView);
        setFocusable(true);
        setOutsideTouchable(true);
    }


    private List<JsonAddress.Data> dataProvince = new ArrayList<>();
    private List<JsonAddress.Data> dataCity = new ArrayList<>();
    private List<JsonAddress.Data> dataCityTemp = new ArrayList<>();
    private JsonAddress.Data defaultProvince;
    private JsonAddress.Data defaultCity;
    private WheelView wheelProvince;
    private WheelView wheelCity;

    private void initData() {
        if (dataProvince == null || dataProvince.size() == 0) {
            return;
        }
        if (defaultProvince == null) {
            defaultProvince = dataProvince.get(0);
        }
        wheelProvince.setAdapter(new ArrayWheelAdapter<>(dataProvince));
        dataCityTemp.clear();
        for (JsonAddress.Data item : dataCity) {
            if (defaultProvince.provinceid.equals(item.provinceid)) {
                dataCityTemp.add(item);
            }
        }
        defaultCity = dataCityTemp.get(0);
        wheelCity.setAdapter(new ArrayWheelAdapter<>(dataCityTemp));

        wheelProvince.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                defaultProvince = dataProvince.get(index);

                dataCityTemp.clear();
                for (JsonAddress.Data item : dataCity) {
                    if (defaultProvince.provinceid.equals(item.provinceid)) {
                        dataCityTemp.add(item);
                    }
                }
                defaultCity = dataCityTemp.get(0);
                wheelCity.setAdapter(new ArrayWheelAdapter<>(dataCityTemp));
            }
        });
        wheelCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                defaultCity = dataCityTemp.get(index);
            }
        });
    }
}
