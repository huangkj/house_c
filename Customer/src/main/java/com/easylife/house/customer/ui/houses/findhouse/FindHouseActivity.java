package com.easylife.house.customer.ui.houses.findhouse;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.ui.homefragment.homeindexv3.ChooseCityActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.kyle.radiogrouplib.NestedRadioGroup;
import com.kyle.radiogrouplib.NestedRadioLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我要找房
 */
public class FindHouseActivity extends BaseActivity {

    @Bind(R.id.tv100below)
    TextView tv100below;
    @Bind(R.id.rb100below)
    NestedRadioLayout rb100below;
    @Bind(R.id.tv100T150)
    TextView tv100T150;
    @Bind(R.id.rb100T150)
    NestedRadioLayout rb100T150;
    @Bind(R.id.tv150T200)
    TextView tv150T200;
    @Bind(R.id.rb150T200)
    NestedRadioLayout rb150T200;
    @Bind(R.id.tv200T300)
    TextView tv200T300;
    @Bind(R.id.rb200T300)
    NestedRadioLayout rb200T300;
    @Bind(R.id.tv300T500)
    TextView tv300T500;
    @Bind(R.id.rb300T500)
    NestedRadioLayout rb300T500;
    @Bind(R.id.tv500T1000)
    TextView tv500T1000;
    @Bind(R.id.rb500T1000)
    NestedRadioLayout rb500T1000;
    @Bind(R.id.tv1000up)
    TextView tv1000up;
    @Bind(R.id.rb1000up)
    NestedRadioLayout rb1000up;
    @Bind(R.id.rpHousePrice)
    NestedRadioGroup rpHousePrice;
    @Bind(R.id.tv1Room)
    TextView tv1Room;
    @Bind(R.id.rb1Room)
    NestedRadioLayout rb1Room;
    @Bind(R.id.tv2Room)
    TextView tv2Room;
    @Bind(R.id.rb2Room)
    NestedRadioLayout rb2Room;
    @Bind(R.id.tv3Room)
    TextView tv3Room;
    @Bind(R.id.rb3Room)
    NestedRadioLayout rb3Room;
    @Bind(R.id.tv4Room)
    TextView tv4Room;
    @Bind(R.id.rb4Room)
    NestedRadioLayout rb4Room;
    @Bind(R.id.tv5Room)
    TextView tv5Room;
    @Bind(R.id.rb5Room)
    NestedRadioLayout rb5Room;
    @Bind(R.id.tv5RoomUp)
    TextView tv5RoomUp;
    @Bind(R.id.rb5RoomUp)
    NestedRadioLayout rb5RoomUp;
    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.tvCity)
    TextView tvCity;
    @Bind(R.id.btnConfirm)
    TextView btnConfirm;
    @Bind(R.id.rpRoom)
    NestedRadioGroup rpRoom;
    private SearchSingleton singletonSearch;
    private String city;
    private String cityId;
    /**
     * 最小预算
     */
    private double minBudget = 0;
    /**
     * 最大预算
     */
    private double maxBudget = 0;

    /**
     * 户型  1- 一居、2-二居、3-三居、4-四居、5-五居、6-五居以上
     */
    private int room;

    private String roomString;

    private String budgetString;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FindHouseActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_find_house, null);
    }

    @Override
    protected void initView() {
        singletonSearch = SearchSingleton.getIstance();
        city = singletonSearch.city;
        cityId = singletonSearch.cityId;
        tvCity.setText(singletonSearch.city);

        rpHousePrice.setOnCheckedChangeListener(new NestedRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb100below:
                        minBudget = 0;
                        maxBudget = 100;
                        budgetString = "100万以下";
                        break;
                    case R.id.rb100T150:
                        minBudget = 100;
                        maxBudget = 150;
                        budgetString = "100万-150万";
                        break;
                    case R.id.rb150T200:
                        minBudget = 150;
                        maxBudget = 200;
                        budgetString = "150万-200万";
                        break;
                    case R.id.rb200T300:
                        minBudget = 200;
                        maxBudget = 300;
                        budgetString = "200万-300万";
                        break;
                    case R.id.rb300T500:
                        minBudget = 300;
                        maxBudget = 500;
                        budgetString = "300万-500万";
                        break;
                    case R.id.rb500T1000:
                        minBudget = 500;
                        maxBudget = 1000;
                        budgetString = "500万-1000万";
                        break;
                    case R.id.rb1000up:
                        maxBudget = 0;
                        minBudget = 1000;
                        budgetString = "1000万以上";
                        break;
                }
            }
        });


        rpRoom.setOnCheckedChangeListener(new NestedRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1Room:
                        room = 1;
                        roomString = "一室";
                        break;
                    case R.id.rb2Room:
                        room = 2;
                        roomString = "二室";

                        break;
                    case R.id.rb3Room:
                        room = 3;
                        roomString = "三室";

                        break;
                    case R.id.rb4Room:
                        room = 4;
                        roomString = "四室";

                        break;
                    case R.id.rb5Room:
                        room = 5;
                        roomString = "五室";

                        break;
                    case R.id.rb5RoomUp:
                        room = 6;
                        roomString = "五室以上";
                        break;
                }
            }
        });

    }

    @OnClick({R.id.relSelectCity, R.id.btnConfirm})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.relSelectCity:
                ChooseCityActivity.startActivity(this, singletonSearch.cityId, singletonSearch.city, false, 0);
                break;
            case R.id.btnConfirm:
                if (minBudget == 0 && maxBudget == 0) {
                    ToastUtils.showShort("请选择购房预算");
                    return;
                }

                if (room == 0) {
                    ToastUtils.showShort("请选择户型");
                    return;
                }

                FiltrateHouseActivity.startActivity(this, minBudget, maxBudget, room, cityId, budgetString, roomString, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            city = ChooseCityActivity.getSelectedText(data);
            cityId = ChooseCityActivity.getSelectedId(data);
            if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(cityId)) {
                tvCity.setText(ChooseCityActivity.getSelectedText(data));
            }
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

}
