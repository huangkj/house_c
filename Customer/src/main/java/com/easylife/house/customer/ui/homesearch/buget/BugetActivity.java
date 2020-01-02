package com.easylife.house.customer.ui.homesearch.buget;

import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
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
 * 搜索预算筛选
 */
public class BugetActivity extends MVPBaseActivity<BugetContract.View, BugetPresenter> implements BugetContract.View {

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
            {"50万以下", "50-100万", "100-200万", "200-300万", "300-500万", "500-800万", "800-1000万", "1000万以上"};
    private SearchSingleton singleton;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_buget, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        singleton = SearchSingleton.getIstance();
        rangeSeekBar.setValue(0,1);
        rangeSeekBar.setRules(0,1,0.001f,1);

        //流布局点击事件
        mFlowViewTime.addViewsCheckBox(R.layout.item_flow, mTimes, new FlowViewGroup.TabClickListener() {
            @Override
            public void onTabClick(int position, String text) {
                //点击多选时,seekBar初始化 无限小 无限大
                rangeSeekBar.setValue(0,1);
                minValueChoose = 0;
                maxValueChoose = 10000;
                tvMoney.setText(minValueChoose+"万元");
                tvMoney1.setText(maxValueChoose+"万元");


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
                    maxValueChoose = 0;
                }
            }
        });

        //seekBar变动
        rangeSeekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {
                minValueChoose = (int) (min * 10000);
                maxValueChoose = (int) (max * 10000);
                tvMoney.setText(minValueChoose+"万元");
                tvMoney1.setText(maxValueChoose+"万元");

                for (int i = 0; i < mFlowViewTime.getChildCount(); i++) {
                    ((CheckBox) mFlowViewTime.getChildAt(i)).setChecked(false);
                    ((CheckBox) mFlowViewTime.getChildAt(i)).setTextColor(getResources().getColor(R.color.gradient_end));
                    chooseSet.clear();
                }
            }
        });

        if(singleton.isIndexHome){
            //首页搜索进入
            if(singleton.chooseSet != null && singleton.chooseSet.size() != 0){
                chooseSet = singleton.chooseSet;
                for(Map.Entry<Integer,Integer> entry : singleton.chooseSet.entrySet()){
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setChecked(true);
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setTextColor(getResources().getColor(R.color.white));
                }
            }else if(singleton.minBugetValue != 0 || singleton.maxBugetValue != 0){
                //设置seekBar的选中值
                tvMoney.setText(singleton.minBugetValue+"万元");
                tvMoney1.setText(singleton.maxBugetValue+"万元");
                minValueChoose = singleton.minBugetValue;
                maxValueChoose = singleton.maxBugetValue;
                float min = (float)singleton.minBugetValue / 10000;
                float max = (float)singleton.maxBugetValue / 10000;
                rangeSeekBar.setValue(min,max);
            }
        }else {
            //品牌楼盘进来
            if(singleton.chooseSetBrand != null && singleton.chooseSetBrand.size() != 0){
                chooseSet = singleton.chooseSetBrand;
                for(Map.Entry<Integer,Integer> entry : singleton.chooseSetBrand.entrySet()){
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setChecked(true);
                    ((CheckBox)mFlowViewTime.getChildAt(entry.getKey())).setTextColor(getResources().getColor(R.color.white));
                }
            }else if(singleton.minBugetValueBrand != 0 || singleton.maxBugetValueBrand != 0){
                //设置seekBar的选中值
                tvMoney.setText(singleton.minBugetValueBrand+"万元");
                tvMoney1.setText(singleton.maxBugetValueBrand+"万元");
                minValueChoose = singleton.minBugetValueBrand;
                maxValueChoose = singleton.maxBugetValueBrand;
                float min = (float)singleton.minBugetValueBrand / 10000;
                float max = (float)singleton.maxBugetValueBrand / 10000;
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
            if(chooseSet.size() == 0){
                if(minValueChoose == 0 && maxValueChoose == 0){
                    singleton.budget = "预算多少";
                }else {
                    Map<String,String> seekMap = new HashMap<>();
                    seekMap.put("minPrice",minValueChoose+"");
                    seekMap.put("maxPrice",maxValueChoose+"");
                    chooseList.add(seekMap);
                    singleton.budget = (minValueChoose)+"-"+(maxValueChoose)+"万";
                    singleton.minBugetValue = minValueChoose;
                    singleton.maxBugetValue = maxValueChoose;
                }
                //seekbar值获取
            }else if(chooseSet.size() != 0){
                singleton.minBugetValue = 0;
                singleton.maxBugetValue = 0;
                StringBuilder sb = new StringBuilder();
                if(chooseSet.size() != 1){
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]).append(",");
                    }
                    singleton.budget =  sb.toString().substring(0,sb.toString().length()-1);
                }else {
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]);
                    }
                    singleton.budget =  sb.toString();
                }

            }
            EventBus.getDefault().post(new MessageEvent(UPDATE_SEARCH_DATA,singleton));
            singleton.chooseSet = chooseSet;
        }else {
            singleton.chooseSetBrand = chooseSet;
            if(chooseSet.size() == 0){
                if(minValueChoose == 0 && maxValueChoose == 0){
                    singleton.budgetSearch = "预算多少";
                }else {
                    Map<String,String> seekMap = new HashMap<>();
                    seekMap.put("minPrice",minValueChoose+"");
                    seekMap.put("maxPrice",maxValueChoose+"");
                    chooseList.add(seekMap);
                    singleton.budgetSearch = (minValueChoose)+"-"+(maxValueChoose)+"万";
                    singleton.minBugetValueBrand = minValueChoose;
                    singleton.maxBugetValueBrand = maxValueChoose;
                }
            }else if(chooseSet.size() != 0){
                singleton.minBugetValueBrand = 0;
                singleton.maxBugetValueBrand = 0;
                StringBuilder sb = new StringBuilder();
                if(chooseSet.size() != 1){
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]).append(",");
                    }
                    singleton.budgetSearch =  sb.toString().substring(0,sb.toString().length()-1);
                }else {
                    for(Map.Entry<Integer,Integer> entry: chooseSet.entrySet()){
                        sb.append(mTimes[entry.getKey()]);
                    }
                    singleton.budgetSearch =  sb.toString();
                }

            }
        }

        if(chooseList != null && chooseList.size() != 0){
            for (int i = 0; i < chooseList.size(); i++) {

                if(chooseList.get(i) != null && chooseList.get(i).size() != 0){
                    for(Map.Entry<String,String> entry: chooseList.get(i).entrySet()){
                        if(!TextUtils.isEmpty(entry.getValue())){
                            int value = Integer.parseInt(entry.getValue()) * 10000;
                            chooseList.get(i).put(entry.getKey(),value+"");
                        }
                    }
                }

            }
        }
        singleton.priceMapList = chooseList;


        SearchRequestBean searchRequestBean = new SearchRequestBean();
        searchRequestBean.priceMapList = chooseList;
        startActivity(new Intent(this, SearchActivity.class).putExtra(SearchRequestBean.SEARCH_BEAN,searchRequestBean));
        finish();
    }
}
