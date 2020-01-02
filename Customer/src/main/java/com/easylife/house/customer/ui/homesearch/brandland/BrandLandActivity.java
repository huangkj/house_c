package com.easylife.house.customer.ui.homesearch.brandland;

import android.view.View;
import android.widget.ImageView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.pub.PubWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.pub.PubWebViewActivity.HOUSE_BRAND_HOPSON;
import static com.easylife.house.customer.ui.pub.PubWebViewActivity.HOUSE_BRAND_ZHUJIANG;

public class BrandLandActivity extends BaseActivity {

    @Bind(R.id.ivHopson)
    ImageView ivHopson;
    @Bind(R.id.ivZhujiang)
    ImageView ivZhujiang;
    private SearchSingleton singletonSearch;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_brand_land, null);
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.ivHopson, R.id.ivZhujiang})
    public void onViewClick(View view) {
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.ivHopson:
                map.put("brand_list", "合生创展");
                PubWebViewActivity.startActivity(this, "合生专场", Constants.BRAND_HOUSE_URL + "?showProject=" + 4 + "&cityId=" + SearchSingleton.getIstance().cityId + "&city=" + SearchSingleton.getIstance().city, HOUSE_BRAND_HOPSON);
                break;
            case R.id.ivZhujiang:
                map.put("brand_list", "珠江投资");
                PubWebViewActivity.startActivity(this, "珠江投资", Constants.BRAND_HOUSE_URL + "?showProject=" + 5 + "&cityId=" + SearchSingleton.getIstance().cityId + "&city=" + SearchSingleton.getIstance().city, HOUSE_BRAND_ZHUJIANG);
                break;
        }
        MobclickAgent.onEvent(activity, "brand_list", map);
    }

    @Override
    protected void setActionBarDetail() {

    }

}
