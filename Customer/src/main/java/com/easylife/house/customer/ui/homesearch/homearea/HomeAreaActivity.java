package com.easylife.house.customer.ui.homesearch.homearea;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.search.SearchActivity;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.RangeSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;

/**
 * 搜索面积筛选
 */
public class HomeAreaActivity extends MVPBaseActivity<HomeAreaContract.View, HomeAreaPresenter> implements HomeAreaContract.View {

    @Bind(R.id.floviewgroup)
    FlowViewGroup mFlowViewTime;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_money1)
    TextView tvMoney1;
    @Bind(R.id.tv_choose)
    TextView tvChoose;
    @Bind(R.id.rangeSeekBar)
    RangeSeekBar rangeSeekBar;
    private int minValueChoose;
    private int maxValueChoose;
//    private ArrayList<Integer> chooseSet = new ArrayList<>();
    private Map<Integer,Integer> chooseSet = new HashMap();
    private String[] mTimes = new String[]
            {"50㎡以下", "50-70㎡", "70-90㎡", "90-110㎡", "110-150㎡", "150-200㎡", "200-300㎡", "300㎡以上"};
    private SearchSingleton singleton;
    private boolean isClear;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isClear = intent.getBooleanExtra("isClear",false);
        if(isClear){
            rangeSeekBar.setValue(0,2f);
        }
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_homearea, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        singleton = SearchSingleton.getIstance();
        if(singleton.isIndexHome){
            minValueChoose = singleton.minBugetValueArea;
            maxValueChoose = singleton.maxBugetValueArea;
        }else {
            minValueChoose = singleton.minBugetValueBrandArea;
            maxValueChoose = singleton.maxBugetValueBrandArea;
        }
        rangeSeekBar.setRules(0,2f,0.01f,1);
        rangeSeekBar.setValue(0,2f);

        //流布局点击事件
        mFlowViewTime.addViewsCheckBox(R.layout.item_flow, mTimes, new FlowViewGroup.TabClickListener() {
            @Override
            public void onTabClick(int position, String text) {
                //点击多选时,seekBar初始化 无限小 无限大
                rangeSeekBar.setValue(0,2f);
                minValueChoose = 0;
                maxValueChoose = 2000;
                tvMoney.setText(minValueChoose+"㎡");
                tvMoney1.setText(maxValueChoose+"㎡");


                CheckBox radiobtn = (CheckBox) mFlowViewTime.getChildAt(position);
                //更改背景颜色
                if (radiobtn.isChecked()) {
                    chooseSet.put(position,position);
                    radiobtn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    if(chooseSet.size() != 0 ){
                        chooseSet.remove(position);
                    }
                    radiobtn.setTextColor(getResources().getColor(R.color.gradient_end));
                }

                if(chooseSet.size() == 0 ){
                    maxValueChoose = 2000;
                }
            }
        });

        //seekBar变动
        rangeSeekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {
                minValueChoose = (int) (min * 1000);
                maxValueChoose = (int) (max * 1000);
                tvMoney.setText(minValueChoose+"㎡");
                tvMoney1.setText(maxValueChoose+"㎡");

                for (int i = 0; i < mFlowViewTime.getChildCount(); i++) {
                    ((CheckBox) mFlowViewTime.getChildAt(i)).setChecked(false);
                    ((CheckBox) mFlowViewTime.getChildAt(i)).setTextColor(getResources().getColor(R.color.gradient_end));
                    chooseSet.clear();
                }
            }
        });

        if(singleton.isIndexHome){
            //首页搜索进入
            if(singleton.chooseSetArea != null && singleton.chooseSetArea.size() != 0){
                chooseSet = singleton.chooseSetArea;
                for(Map.Entry<Integer,Integer> entry : singleton.chooseSetArea.entrySet()){
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setChecked(true);
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setTextColor(getResources().getColor(R.color.white));
                }
            }else if(singleton.minBugetValueArea != 0 || singleton.maxBugetValueArea != 0){
                //设置seekBar的选中值
                tvMoney.setText(singleton.minBugetValueArea+"㎡");
                tvMoney1.setText(singleton.maxBugetValueArea+"㎡");
                float min = (float)singleton.minBugetValueArea / 1000;
                float max = (float)singleton.maxBugetValueArea / 1000;
                rangeSeekBar.setValue(min,max);
            }
        }else {
            //品牌楼盘进来
            if(singleton.chooseSetBrandArea != null && singleton.chooseSetBrandArea.size() != 0){
                chooseSet = singleton.chooseSetBrandArea;
                for(Map.Entry<Integer,Integer> entry : singleton.chooseSetBrandArea.entrySet()){
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setChecked(true);
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setTextColor(getResources().getColor(R.color.white));
                }
            }else if(singleton.minBugetValueBrandArea != 0 || singleton.maxBugetValueBrandArea != 0){
                //设置seekBar的选中值
                tvMoney.setText(singleton.minBugetValueBrandArea+"㎡");
                tvMoney1.setText(singleton.maxBugetValueBrandArea+"㎡");
                float min = (float)singleton.minBugetValueBrandArea / 1000;
                float max = (float)singleton.maxBugetValueBrandArea / 1000;
                rangeSeekBar.setValue(min,max);
            }
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


    @OnClick({R.id.tv_choose,R.id.iv_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose:
                mPresenter.chooseFlowChild(chooseSet);
                break;
        }

    }

    @Override
    public void showChooseTime(ArrayList<Map<String,String>> chooseList) {

        //是否首页进来
        if(singleton.isIndexHome){
            //多选值判断
            if(chooseSet.size() == 0 ){
                if(minValueChoose == 0 && maxValueChoose == 0){
                    singleton.openTime = "家的大小";
                }else {
                    Map<String,String> seekMap = new HashMap<>();
                    seekMap.put("minHouseSize",minValueChoose+"");
                    seekMap.put("maxHouseSize",maxValueChoose+"");
                    chooseList.add(seekMap);
                    singleton.openTime = (minValueChoose)+"-"+(maxValueChoose)+"㎡";
                    singleton.minBugetValueArea = minValueChoose;
                    singleton.maxBugetValueArea = maxValueChoose;
                }
                //seekbar值获取
            }else if(chooseSet.size() != 0){
                singleton.minBugetValueArea = 0;
                singleton.maxBugetValueArea = 2000;
                StringBuilder sb = new StringBuilder();
                if(chooseSet.size() != 1){
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]).append(",");
                    }
                    singleton.openTime =  sb.toString().substring(0,sb.toString().length()-1);
                }else {
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]);
                    }
                    singleton.openTime =  sb.toString();
                }

            }
            EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA,singleton));
            singleton.chooseSetArea = chooseSet;
        }else {
            singleton.chooseSetBrandArea = chooseSet;
            if(chooseSet.size() == 0){
                if(minValueChoose == 0 && maxValueChoose == 0){
                    singleton.openTimeSearch = "预算多少";
                }else {
                    Map<String,String> seekMap = new HashMap<>();
                    seekMap.put("minPrice",minValueChoose+"");
                    seekMap.put("maxPrice",maxValueChoose+"");
                    chooseList.add(seekMap);
                    singleton.openTimeSearch = (minValueChoose)+"-"+(maxValueChoose)+"㎡";
                    singleton.minBugetValueBrandArea = minValueChoose;
                    singleton.maxBugetValueBrandArea = maxValueChoose;
                }
            }else if(chooseSet.size() != 0){
                singleton.minBugetValueBrandArea = 0;
                singleton.maxBugetValueBrandArea = 2000;
                StringBuilder sb = new StringBuilder();
                if(chooseSet.size() != 1){
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]).append(",");
                    }
                    singleton.openTimeSearch =  sb.toString().substring(0,sb.toString().length()-1);
                }else {
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]);
                    }
                    singleton.openTimeSearch =  sb.toString();
                }

            }
        }

        singleton.areaMapList = chooseList;


        SearchRequestBean searchRequestBean = new SearchRequestBean();
        searchRequestBean.areaMapList = chooseList;
        startActivity(new Intent(this, SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN,searchRequestBean));
        finish();
    }
}
