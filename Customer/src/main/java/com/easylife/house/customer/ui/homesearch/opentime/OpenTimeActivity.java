package com.easylife.house.customer.ui.homesearch.opentime;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.search.SearchActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.FlowViewGroup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;

/**
 * 开盘时间筛选
 */
public class OpenTimeActivity extends MVPBaseActivity<OpenTimeContract.View, OpenTimePresenter> implements
        OpenTimeContract.View, DatePickerDialog.OnDateSetListener {

    @Bind(R.id.floviewgroup)
    FlowViewGroup mFlowViewTime;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_choose)
    TextView tvChoose;

    public String chooseDatePosition = "";
    private RadioButton tvSort;
    private String[] mTimes = new String[]
            {"不限", "近1个月", "近3个月", "近半年"};
    private SearchSingleton singleton;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_open_time, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        singleton = SearchSingleton.getIstance();
        tvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvDate.getPaint().setAntiAlias(true);//抗锯齿

        for (int i = 0; i < mTimes.length; i++) {
            tvSort = (RadioButton) LayoutInflater.from(this).inflate(R.layout.open_time_item_flow, mFlowViewTime, false);
            tvSort.setText(mTimes[i]);
            tvSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < mFlowViewTime.getChildCount(); j++) {
                        RadioButton radiobtn = (RadioButton) mFlowViewTime.getChildAt(j);
                        if (radiobtn.isChecked()) {
                            radiobtn.setTextColor(getResources().getColor(R.color.white));
                            tvDate.setText("                   ");
                            chooseDatePosition = j + "";
                        } else {
                            radiobtn.setTextColor(getResources().getColor(R.color.gradient_end));
                        }

                    }
                }
            });
            mFlowViewTime.addView(tvSort);
        }
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }


    @OnClick({R.id.tv_date, R.id.tv_choose,R.id.iv_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_date:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(getResources().getColor(R.color.gradient_start));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                break;
            case R.id.tv_choose:
                mPresenter.chooseOpenTime(chooseDatePosition);
//                CustomerUtils.showTip(this,tvDate.getText().toString().trim());
//                startActivity(new Intent(this, SearchActivity.class));
                break;
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tvDate.setText("  " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "  ");
        chooseDatePosition = tvDate.getText().toString().trim();
        for (int i = 0; i < mFlowViewTime.getChildCount(); i++) {
            RadioButton radiobtn = (RadioButton) mFlowViewTime.getChildAt(i);
            radiobtn.setChecked(false);
            radiobtn.setTextColor(getResources().getColor(R.color.gradient_end));
        }
    }

    @Override
    public void showChooseTime(String date) {

        try {
            if (!TextUtils.isEmpty(tvDate.getText().toString().trim())) {
                date = tvDate.getText().toString().trim();
            }

            if(singleton.isIndexHome){
                if(!TextUtils.isEmpty(date)){
                    singleton.openTime = date;
                }else {
                    singleton.openTime = "家的大小";
                }
                EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA, singleton));
            }else {
                if(!TextUtils.isEmpty(date)){
                    singleton.openTimeSearch = date;
                }else {
                    singleton.openTimeSearch = "家的大小";
                }
            }

            int secondsFromDate = CustomerUtils.getSecondsFromDate(date, "yyyy-MM-dd");
            SearchRequestBean searchRequestBean = new SearchRequestBean();
            if(secondsFromDate != 0){
                searchRequestBean.beforeTime = secondsFromDate + "";
                singleton.beforeTime = secondsFromDate + "";
            }else {
                searchRequestBean.beforeTime = "";
                singleton.beforeTime = "";
            }
            searchRequestBean.cityId = SearchSingleton.getIstance().cityId;
            startActivity(new Intent(this, SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN, searchRequestBean));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
