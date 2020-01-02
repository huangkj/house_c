package com.easylife.house.customer.ui.homesearch.wherebuy;

import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.SearchHistoryBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.search.SearchActivity;
import com.easylife.house.customer.util.PubTipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;

/**
 * 搜索地点筛选
 */
public class WhereBuyActivity extends MVPBaseActivity<WhereBuyContract.View, WhereBuyPresenter> implements WhereBuyContract.View {

    @Bind(R.id.ll_history)
    LinearLayout llHistory;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_city1)
    TextView tvCity1;
    @Bind(R.id.tv_city2)
    TextView tvCity2;
    @Bind(R.id.tv_city3)
    TextView tvCity3;
    @Bind(R.id.tv_city4)
    TextView tvCity4;
    @Bind(R.id.tv_city5)
    TextView tvCity5;
    @Bind(R.id.tv_choose)
    TextView tvChoose;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_house)
    TextView tvHouse;

    private TreeMap<String, City> mapCitys;
    private SearchSingleton searchSingleton;
    private int pageMargin10;
    private String city;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_where_buy, null);
    }

    @Override
    protected void initView() {
        mPresenter.getCityList();
        if (dao != null && dao.getLocateCache() != null) {
            city = dao.getLocateCache().city;
            tvLocation.setText(city);
        }

        pageMargin10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
                .getDisplayMetrics());
        searchSingleton = SearchSingleton.getIstance();
        if (searchSingleton.isIndexHome) {
            if (!TextUtils.isEmpty(searchSingleton.buyWhere)) {
                tvCity.setText(searchSingleton.buyWhere);
            }

            if (!TextUtils.isEmpty(searchSingleton.whereHouse)) {
                tvHouse.setText(searchSingleton.whereHouse);
            }
        }

        //获取搜索历史
//        mPresenter.getSearchHistory(dao.localDao);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.text_normal));
    }

    @Override
    protected void tryRequestData() {
        mPresenter.getCityList();
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showSearchHistory(List<SearchHistoryBean> historyList) {
        //显示历史记录
        if (historyList != null) {
            for (final SearchHistoryBean bean : historyList) {
                TextView text = new TextView(this);
                if (TextUtils.isEmpty(bean.houses) && !TextUtils.isEmpty(bean.city)) {
                    text.setText(bean.city);
                } else {
                    if (!TextUtils.isEmpty(bean.city)) {
                        text.setText(bean.city + "·" + bean.houses);
                    } else {
                        text.setText(bean.houses);
                    }
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
                text.setPadding(0, pageMargin10, 0, 0);
                text.setLayoutParams(layoutParams);
                llHistory.addView(text);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CustomerUtils.showTip(activity,bean.city+"·"+bean.estateRoomBeanList);
                        if (!TextUtils.isEmpty(bean.city)) {
                            tvCity.setText(bean.city);
                        }

                        if (!TextUtils.isEmpty(bean.houses)) {
                            tvHouse.setText(bean.houses);
                        }
                    }
                });
            }
            llHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initCity(List<City> data) {
        mapCitys = new TreeMap<>();
        for (City c : data) {
            mapCitys.put(c.getId(), c);
        }
    }


    @OnClick({R.id.ll_history, R.id.tv_location, R.id.tv_city1, R.id.tv_city2, R.id.tv_city3,
            R.id.tv_city4, R.id.tv_city5, R.id.tv_choose, R.id.tv_city, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_history:
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_location:
                if(!TextUtils.isEmpty(dao.localDao.getCity())){
                    tvCity.setText(dao.localDao.getCity());
                    searchSingleton.city = dao.localDao.getCity();
                    searchSingleton.buyWhere = dao.localDao.getCity();
                }else {
                    tvCity.setText("北京市");
                    searchSingleton.city = "北京市";
                    searchSingleton.buyWhere = "北京市";
                }
                if (searchSingleton != null) {
                    if(dao.localDao != null && !TextUtils.isEmpty(dao.localDao.getCityId())){
                        searchSingleton.cityId = dao.localDao.getCityId();
                    }else {
                        searchSingleton.cityId = "110100";
                    }
                }
                break;
            case R.id.tv_city:
                dialogCity();
                break;
            case R.id.tv_city1:
                tvCity.setText(tvCity1.getText().toString().trim());
                if (searchSingleton != null) {
                    searchSingleton.buyWhere = tvCity1.getText().toString().trim();
                    searchSingleton.city = tvCity1.getText().toString().trim();
                    searchSingleton.cityId = "110100";
                    if (searchSingleton.isIndexHome) {
                        EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                    }

                }
                break;
            case R.id.tv_city2:
                tvCity.setText(tvCity2.getText().toString().trim());
                if (searchSingleton != null) {
                    searchSingleton.buyWhere = tvCity2.getText().toString().trim();
                    searchSingleton.city = tvCity2.getText().toString().trim();
                    searchSingleton.cityId = "310100";
                    if (searchSingleton.isIndexHome) {
                        EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                    }

                }
                break;
            case R.id.tv_city3:
                tvCity.setText(tvCity3.getText().toString().trim());
                if (searchSingleton != null) {
                    searchSingleton.buyWhere = tvCity3.getText().toString().trim();
                    searchSingleton.city = tvCity3.getText().toString().trim();
                    searchSingleton.cityId = "120100";
                    if (searchSingleton.isIndexHome) {
                        EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                    }

                }
                break;
            case R.id.tv_city4:
                tvCity.setText(tvCity4.getText().toString().trim());
                if (searchSingleton != null) {
                    searchSingleton.buyWhere = tvCity4.getText().toString().trim();
                    searchSingleton.city = tvCity4.getText().toString().trim();
                    searchSingleton.cityId = "440100";
                    if (searchSingleton.isIndexHome) {
                        EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                    }

                }
                break;
            case R.id.tv_city5:
                tvCity.setText(tvCity5.getText().toString().trim());
                if (searchSingleton != null) {
                    searchSingleton.buyWhere = tvCity5.getText().toString().trim();
                    searchSingleton.city = tvCity5.getText().toString().trim();
                    searchSingleton.cityId = "440300";
                    if (searchSingleton.isIndexHome) {
                        EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                    }

                }
                break;
            case R.id.tv_choose:
                //保存搜索历史
                String city = searchSingleton.city;
                String house = tvHouse.getText().toString().trim();
                if (searchSingleton.isIndexHome) {
                    searchSingleton.buyWhere = city;
                    searchSingleton.whereHouse = house;
                    EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, searchSingleton));
                } else {
                    searchSingleton.buyWhereSearch = city;
                    searchSingleton.whereHouseSearch = house;
                }


                if (!TextUtils.isEmpty(city) || !TextUtils.isEmpty(house)) {
                    SearchHistoryBean historyBean = new SearchHistoryBean();
                    if (searchSingleton.isIndexHome) {
                        historyBean.cityId = searchSingleton.cityId;
                    } else {
                        historyBean.cityId = searchSingleton.cityIdSearch;
                    }
                    historyBean.city = city;
                    historyBean.houses = house;
                    mPresenter.saveSearchHistory(dao.localDao, historyBean);
                }


                SearchRequestBean bean = new SearchRequestBean();
                bean.devName = house;
                bean.city = city;
                bean.cityId = searchSingleton.cityId;
                startActivity(new Intent(this, SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, bean));
                finish();
                break;
        }
    }

    /**
     * 选择城市
     */
    private PubTipDialog dialog;

    public void dialogCity() {
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
                city = date.getText();
                tvCity.setText(city);

                if (searchSingleton.isIndexHome) {
                    searchSingleton.city = city;
                    searchSingleton.cityId = date.getId();
                } else {
                    searchSingleton.citySearch = city;
                    searchSingleton.cityIdSearch = date.getId();
                }

            }
        });
    }
}
