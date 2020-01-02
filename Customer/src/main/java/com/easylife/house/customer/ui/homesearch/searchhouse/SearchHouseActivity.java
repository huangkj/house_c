package com.easylife.house.customer.ui.homesearch.searchhouse;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.allhouse.AllHouseActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.SEARCH_HOUSE_FINISH;

/**
 * - @Description:  1.3版本楼盘搜索
 * - @Author:  hkj
 * - @Time:  2018/9/4 15:31
 */
public class SearchHouseActivity extends MVPBaseActivity<SearchHouseContract.View, SearchHousePresenter> implements SearchHouseContract.View {

    @Bind(R.id.etSearchText)
    EditText etSearchText;
    @Bind(R.id.ivClear)
    ImageView ivClear;
    @Bind(R.id.tvCancel)
    TextView tvCancel;
    private String cityId;
    private String city;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_search_house, null);
    }


    public static void startActivity(Activity activity, String cityId, String city, int requestCode) {
        activity.startActivityForResult(new Intent(activity, SearchHouseActivity.class)
                        .putExtra("cityId", cityId)
                        .putExtra("city", city)
                , requestCode);
    }


    protected void initView() {
        MobclickAgent.onEvent(activity, "home_search");

        cityId = getIntent().getStringExtra("cityId");
        city = getIntent().getStringExtra("city");
        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = etSearchText.getText().toString().trim();
//                    if (!TextUtils.isEmpty(text)) {
//                        EventBus.getDefault().post(new MessageEvent(SEARCH_HOUSE_FINISH));
//                        AllHouseActivity.startActivity(SearchHouseActivity.this, 0, text, 0);
//                    } else {
//                        ToastUtils.showShort("请输入楼盘名称");
//                    }

                    EventBus.getDefault().post(new MessageEvent(SEARCH_HOUSE_FINISH));
                    AllHouseActivity.startActivity(SearchHouseActivity.this, 0, text, cityId, city, "", 0);
                }
                return true;
            }
        });


        etSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClear.setVisibility(TextUtils.isEmpty(s.toString().trim()) ? View.GONE : View.VISIBLE);

            }
        });

        etSearchText.setFocusable(true);
        etSearchText.setFocusableInTouchMode(true);
        etSearchText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }


    @OnClick({R.id.ivClear, R.id.tvCancel})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivClear:
                etSearchText.setText(null);
                break;
            case R.id.tvCancel:
                finish();
                break;
        }
    }

    @Override
    protected void tryRequestData() {

    }


    @Override
    public void showTip(String msg) {

    }
}
