package com.easylife.house.customer.ui.houses.findhouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.CompareHouseItem;
import com.easylife.house.customer.bean.CompareListBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.view.vhtableview.VHBaseAdapter;
import com.easylife.house.customer.view.vhtableview.VHTableView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 好房对比
 */
public class CompareHouseActivity extends BaseActivity {
    @Bind(R.id.vht_table)
    VHTableView vhtTable;
    //可以得到屏幕的宽度  按 屏幕的比例去划分  后续修改
    private int title0Width;
    private int titleWidth;
    private int titleHieght;
    private int cellHieght;
    private int titleLeftPadding;

    private ArrayList<CompareListBean> titleData = new ArrayList<>();
    ArrayList<ArrayList<CompareHouseItem>> contentAllData = new ArrayList<>();//显示数据

    public static void startActivity(Activity activity, ArrayList<String> compareDevIds, int requestCode) {
        Intent intent = new Intent(activity, CompareHouseActivity.class);
        intent.putStringArrayListExtra("compareDevIds", compareDevIds);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_compare_house, null);
    }

    @Override
    protected void initView() {
        ArrayList<String> compareDevIds = getIntent().getStringArrayListExtra("compareDevIds");


        String json = "[\n" +
                "        {\n" +
                "            \"propertyCompany\": \"公园懿府物业\",\n" +
                "            \"expires\": \"70年\",\n" +
                "            \"address\": \"丰台区西红门南一街602\",\n" +
                "            \"companyName\": \"众合伊韵\",\n" +
                "            \"devName\": \"公园懿府\",\n" +
                "            \"plotRatio\": \"1.00\",\n" +
                "            \"devSquareMetre\": \"47.0-68.0\",\n" +
                "            \"parkRatio\": \"1\",\n" +
                "            \"decorateLevel\": \"简装\",\n" +
                "            \"averPrice\": 60000.0,\n" +
                "            \"buildType\": \"小高层\",\n" +
                "            \"propertyType\": \"住宅\",\n" +
                "            \"liveTime\": 1589385600,\n" +
                "            \"propertyFee\": \"30.00\",\n" +
                "            \"openTime\": 1537459200\n" +
                "        },\n" +
                "        {\n" +
                "            \"propertyCompany\": \"康景物业\",\n" +
                "            \"expires\": \"40年\",\n" +
                "            \"address\": \"通州区耿庄桥北600m\",\n" +
                "            \"companyName\": \"北京珠江房地产开发有限公司\",\n" +
                "            \"devName\": \"珠江丽景家园（商铺）\",\n" +
                "            \"plotRatio\": \"1.00\",\n" +
                "            \"devSquareMetre\": \"1000.85-8686.33\",\n" +
                "            \"parkRatio\": \"1:1\",\n" +
                "            \"decorateLevel\": \"毛坯\",\n" +
                "            \"averPrice\": 0.0,\n" +
                "            \"buildType\": \"低层\",\n" +
                "            \"propertyType\": \"住宅\",\n" +
                "            \"liveTime\": 1544544000,\n" +
                "            \"propertyFee\": \"5.00\",\n" +
                "            \"openTime\": 1543593600\n" +
                "        },\n" +
                "        {\n" +
                "            \"propertyCompany\": \"康景物业\",\n" +
                "            \"expires\": \"40年\",\n" +
                "            \"address\": \"通州区耿庄桥北600m\",\n" +
                "            \"companyName\": \"北京珠江房地产开发有限公司\",\n" +
                "            \"devName\": \"珠江丽景家园（商铺）\",\n" +
                "            \"plotRatio\": \"1.00\",\n" +
                "            \"devSquareMetre\": \"1000.85-8686.33\",\n" +
                "            \"parkRatio\": \"1:1\",\n" +
                "            \"decorateLevel\": \"毛坯\",\n" +
                "            \"averPrice\": 0.0,\n" +
                "            \"buildType\": \"低层\",\n" +
                "            \"propertyType\": \"住宅\",\n" +
                "            \"liveTime\": 1544544000,\n" +
                "            \"propertyFee\": \"5.00\",\n" +
                "            \"openTime\": 1543593600\n" +
                "        }\n" +
                "    ]";


        mDao.devContrast(0, compareDevIds, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                initData(response);

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.code);
            }
        });
    }

    private void initData(String json) {
        List<CompareListBean> dataList = new Gson().fromJson(json, new TypeToken<List<CompareListBean>>() {
        }.getType());

        //title 第一个固定文字
        CompareListBean compareListBeanTitle = new CompareListBean();
        compareListBeanTitle.setDevName("");
        titleData.add(compareListBeanTitle);

        //设置title数据
        for (CompareListBean bean : dataList
        ) {
            CompareListBean compareListTitleBean = new CompareListBean();
            compareListTitleBean.setDevName(bean.getDevName());
            compareListTitleBean.setDevSquareMetre(bean.getDevSquareMetre());
            compareListTitleBean.setAverPrice(bean.getAverPrice());
            titleData.add(compareListTitleBean);
        }

        dataList.add(0, new CompareListBean());//添加一个空对象 占位 每行第一列的固定标题

        for (int i = 0; i < 13; i++) {
            ArrayList<CompareHouseItem> contentRowData = new ArrayList<>();
            for (int j = 0; j < dataList.size(); j++) {//每一列第一个 标题 自己加
                CompareHouseItem rowData = new CompareHouseItem();
                CompareListBean compareListBean = dataList.get(j);

                if (i == 0) {//第一行 开始时间
                    String name = "开始时间";
                    String rowTitle = "基础信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(TimeUtils.millis2String(compareListBean.getOpenTime() * 1000, new SimpleDateFormat("yyyy/MM/dd")));
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        //确保每行第一列的header属性正确即可，第一个为判断依据
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);
                } else if (i == 1) {//第二行 交房时间

                    String name = "交房时间";
                    String rowTitle = "基础信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(TimeUtils.millis2String(compareListBean.getLiveTime() * 1000, new SimpleDateFormat("yyyy/MM/dd")));
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);


                } else if (i == 2) {//第三行 开发商

                    String name = "开发商";
                    String rowTitle = "基础信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getCompanyName() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);


                } else if (i == 3) {//第四行 地址

                    String name = "地址";
                    String rowTitle = "基础信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getAddress() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);


                } else if (i == 4) {//第五行 开盘时间

                    String name = "均价";
                    String rowTitle = "价格信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getAverPrice() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);


                } else if (i == 5) {//第六行 物业类型

                    String name = "物业类型";
                    String rowTitle = "物业信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getPropertyType() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 6) {//第7行 物业类型

                    String name = "物业公司";
                    String rowTitle = "物业信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getPropertyCompany() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 7) {//第8行 物业费

                    String name = "物业费";
                    String rowTitle = "物业信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getPropertyFee() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 8) {//第9行 建筑类别

                    String name = "建筑类别";
                    String rowTitle = "其他信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getBuildType() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 9) {//第10行 产权年限

                    String name = "产权年限";
                    String rowTitle = "其他信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getExpires() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 10) {//第11行 装修标准

                    String name = "装修标准";
                    String rowTitle = "其他信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getDecorateLevel() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 11) {//第12行 容积率

                    String name = "容积率";
                    String rowTitle = "其他信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getPlotRatio() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                } else if (i == 12) {//第13行 车位配比

                    String name = "车位配比";
                    String rowTitle = "其他信息";
                    if (!TextUtils.isEmpty(compareListBean.getDevName())) {
                        rowData.setName(compareListBean.getParkRatio() + "");
                    } else {
                        rowData.setName(name);
                        rowData.setRowTitle(rowTitle);
                    }

                    //判断行标题
                    if (!contentAllData.isEmpty()) {//判断行标题
                        CompareHouseItem lastCompareHouseItem = contentAllData.get(contentAllData.size() - 1).get(0);
                        rowData.setHeader(!rowTitle.equals(lastCompareHouseItem.getRowTitle()));
                    }
                    contentRowData.add(rowData);

                }


            }
            contentAllData.add(contentRowData);
        }

        setAdapter();
    }

    private void setAdapter() {
        VHTableAdapter tableAdapter = new VHTableAdapter(CompareHouseActivity.this);
//                    vhtTable.setFirstColumnIsMove(true);//设置第一列是否可移动,默认不可移动
//                    vhtTable.setShowTitle(false);//设置是否显示标题行,默认显示
        //一般表格都只是展示用的，所以这里没做刷新，真要刷新数据的话，重新setadaper一次吧
        vhtTable.setAdapter(tableAdapter);


        vhtTable.setCurrentTouchView(vhtTable.getFirstHListViewScrollView());
        if (vhtTable.getTitleLayout() != null) {
            View suspensionView = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_cell_header, null);
            final TextView tvSuspension = (TextView) suspensionView.findViewById(R.id.tvCellTitle);

            vhtTable.addTitleLayout(suspensionView);

            vhtTable.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    Log.i("w", "setOnScrollListener" + firstVisibleItem + "==" + visibleItemCount + "===" + totalItemCount);
                    if (contentAllData.size() - 1 <= firstVisibleItem)
                        return;
                    String rowTitle = contentAllData.get(firstVisibleItem).get(0).getRowTitle();
                    String nextRowTitle = contentAllData.get(firstVisibleItem + 1).get(0).getRowTitle();

                    tvSuspension.setText(rowTitle);

                    if (rowTitle == nextRowTitle) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) vhtTable.getTitleLayout().getLayoutParams();
                        params.topMargin = 0;
                        vhtTable.getTitleLayout().setLayoutParams(params);
                    }

                    if (rowTitle != nextRowTitle) {
                        View childView = view.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = vhtTable.getTitleLayout().getHeight();
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) vhtTable.getTitleLayout().getLayoutParams();
                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                params.topMargin = (int) pushedDistance;
                                vhtTable.getTitleLayout().setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    vhtTable.getTitleLayout().setLayoutParams(params);
                                }
                            }
                        }
                    }
                }
            });
        }
    }


    public class VHTableAdapter implements VHBaseAdapter {
        private Context context;

        public VHTableAdapter(Context context) {
            this.context = context;
            title0Width = (int) getResources().getDimensionPixelSize(R.dimen.dp_62);
            titleWidth = (int) getResources().getDimensionPixelSize(R.dimen.dp_137);
            titleHieght = (int) getResources().getDimensionPixelSize(R.dimen.dp_104);
            cellHieght = (int) getResources().getDimension(R.dimen.dp_84);
            titleLeftPadding = (int) getResources().getDimensionPixelSize(R.dimen.dp_3);
        }

        @Override
        public int getContentRows() {
            return contentAllData.size();
        }

        @Override
        public int getContentColumn() {
            return titleData.size();
        }


        //顶部第一行 不向上滑动
        @Override
        public View getTitleView(final int columnPosition, ViewGroup parent) {
            View titleView = null;
            CompareListBean compareListBean = titleData.get(columnPosition);
            if (columnPosition == 0) {
                titleView = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_header_first, null);
//                titleView = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_header, null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(title0Width, titleHieght);
                titleView.setLayoutParams(layoutParams);
//                titleView = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_header, null);


            } else {
                titleView = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_header, null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(titleWidth, titleHieght);
                titleView.setLayoutParams(layoutParams);
                TextView tvTitleDevName = (TextView) titleView.findViewById(R.id.tvTitleDevName);
                TextView tvTitleDevSquareMetre = (TextView) titleView.findViewById(R.id.tvTitleDevSquareMetre);
                TextView tvTitlePrice = (TextView) titleView.findViewById(R.id.tvTitlePrice);
                ImageView ivDel = (ImageView) titleView.findViewById(R.id.ivDel);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (titleData.size() - 1 > 2) {//大于2个可以删除
                            //删除
                            titleData.remove(columnPosition);
                            for (ArrayList<CompareHouseItem> comparisonCarItems : contentAllData) {
                                comparisonCarItems.remove(columnPosition);
                            }
                            setAdapter();
                            ToastUtils.showShort("删除成功");
                        } else {
                            ToastUtils.showShort("至少保留2个对比楼盘");

                        }


                    }
                });
                tvTitleDevName.setText(compareListBean.getDevName());
                tvTitleDevSquareMetre.setText("建面 " + compareListBean.getDevSquareMetre() + "㎡");
                tvTitlePrice.setText(compareListBean.getAverPrice() + "元/㎡");
            }


            return titleView;
        }

        @Override
        public View getTableCellView(int contentRow, int contentColum, View view, ViewGroup parent) {
            TableCellView tableCellView = null;
            if (null == view) {
                tableCellView = new TableCellView();
                view = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_table_cell, null);
//                tableCellView.flContent = view.findViewById(R.id.rl_content);
                tableCellView.relFirst = view.findViewById(R.id.relFirst);
                tableCellView.relContent = view.findViewById(R.id.relContent);
                tableCellView.tvFirst = view.findViewById(R.id.tvFirst);
                tableCellView.tvContent = view.findViewById(R.id.tvContent);
                tableCellView.vCut = view.findViewById(R.id.vCut);
                view.setTag(tableCellView);
            } else {
                tableCellView = (TableCellView) view.getTag();
            }

            ArrayList<CompareHouseItem> compareHouseItems = contentAllData.get(contentRow);
            CompareHouseItem compareHouseItem = compareHouseItems.get(contentColum);

            if (contentColum == 0) {
                //第一列
                tableCellView.relFirst.setVisibility(View.VISIBLE);
                tableCellView.relContent.setVisibility(View.GONE);
                String name = compareHouseItem.getName();
                if (name.length() > 3) {
                    tableCellView.tvFirst.setMaxEms(2);
                } else {
                    tableCellView.tvFirst.setMaxEms(10);
                }
                tableCellView.tvFirst.setText(compareHouseItem.getName());


                //每组最后一行 隐藏分割线
                if (contentRow < contentAllData.size() - 1) {
                    CompareHouseItem nextRowcompareHouseItem = contentAllData.get(contentRow + 1).get(0);
                    tableCellView.vCut.setVisibility(nextRowcompareHouseItem.isHeader() ? View.INVISIBLE : View.VISIBLE);
                } else {
                    tableCellView.vCut.setVisibility(View.INVISIBLE);
                }
            } else {
                tableCellView.relFirst.setVisibility(View.GONE);
                tableCellView.relContent.setVisibility(View.VISIBLE);
                tableCellView.tvContent.setText(compareHouseItem.getName());
            }


            return view;
        }

        @Override
        public View getTableRowTitlrView(int contentRow, View view) {
            TableRowTitlrView tableRowTitlrView = null;
            if (null == view) {
                tableRowTitlrView = new TableRowTitlrView();
                view = LayoutInflater.from(CompareHouseActivity.this).inflate(R.layout.layout_comparison_cell_header, null);
                tableRowTitlrView.tvCellTitle = (TextView) view.findViewById(R.id.tvCellTitle);
            } else {
                tableRowTitlrView = (TableRowTitlrView) view.getTag();
            }

            CompareHouseItem compareHouseItem = contentAllData.get(contentRow).get(0);
            int visibility = compareHouseItem.isHeader() ? View.VISIBLE : View.GONE;
            if (visibility != view.getVisibility()) {
                view.setVisibility(visibility);
            }
            if (!tableRowTitlrView.tvCellTitle.getText().equals(compareHouseItem.getRowTitle())) {
                tableRowTitlrView.tvCellTitle.setText(compareHouseItem.getRowTitle());
            }

            return view;
        }

        @Override
        public View getFooterView(ListView view) {
            View footer = LayoutInflater.from(context).inflate(R.layout.empty_pub, null);
            footer.findViewById(R.id.tvEmpty).setVisibility(View.INVISIBLE);
            return footer;
        }

        @Override
        public Object getItem(int contentRow) {
            return contentAllData.get(contentRow);
        }

        @Override
        public void OnClickContentRowItem(int row, View convertView) {

        }

        class TableCellView {
            //            FrameLayout flContent;
            RelativeLayout relContent;
            RelativeLayout relFirst;
            TextView tvContent;
            TextView tvFirst;
            View vCut;
        }


        class TableRowTitlrView {
            TextView tvCellTitle;
        }

    }


    @Override
    protected void setActionBarDetail() {

    }

}
