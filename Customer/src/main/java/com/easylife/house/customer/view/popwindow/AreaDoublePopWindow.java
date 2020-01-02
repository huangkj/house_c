package com.easylife.house.customer.view.popwindow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AreaDoubleBean;

import java.util.List;

public class AreaDoublePopWindow extends PopupWindow {

    private List<AreaDoubleBean> areas;


    private final Context mContext;
    private RecyclerView rcvArea;
    private LinearLayout llArea;
    private OnItemClickListener mOnItemClickListener;
    private RecyclerView rcvBusinessArea;
    private AreaSubAdapter areaSubAdapter;


    public interface OnItemClickListener {
        void onItemClick(String areaId, String tradingCode, String areaName, String tradingName);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public AreaDoublePopWindow(Context context) {
        mContext = context;
        initView();
    }

    public AreaDoublePopWindow(Context context, List<AreaDoubleBean> areas) {
        mContext = context;
        this.areas = areas;


        for (AreaDoubleBean bean :
                areas) {
            List<AreaDoubleBean.ListBean> list = bean.getList();
            AreaDoubleBean.ListBean listBean = new AreaDoubleBean.ListBean();
            listBean.setTradingName("不限");
            list.add(0, listBean);
        }

        AreaDoubleBean areaBean = new AreaDoubleBean();
        areaBean.setAreaName("全部");
        areas.add(0, areaBean);
        initView();
    }

    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.area_double_pop_layout, null);
        llArea = (LinearLayout) popView.findViewById(R.id.llArea);
        rcvArea = (RecyclerView) popView.findViewById(R.id.rcvArea);
        rcvBusinessArea = (RecyclerView) popView.findViewById(R.id.rcvBusinessArea);


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) (ScreenUtils.getScreenHeight() * 0.5)));
        llArea.setLayoutParams(layoutParams);


        AreaAdapter areaAdapter = new AreaAdapter(areas);

        rcvArea.setLayoutManager(new LinearLayoutManager(mContext));
        rcvArea.setAdapter(areaAdapter);


        areaSubAdapter = new AreaSubAdapter(null);
        rcvBusinessArea.setLayoutManager(new LinearLayoutManager(mContext));
        rcvBusinessArea.setAdapter(areaSubAdapter);


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


    class AreaAdapter extends BaseQuickAdapter<AreaDoubleBean, BaseViewHolder> {
        private int currentSelectPos = -1;

        public AreaAdapter(List<AreaDoubleBean> data) {
            super(R.layout.area_pop_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final AreaDoubleBean area) {
            final int position = baseViewHolder.getAdapterPosition();
            final TextView tv = (TextView) baseViewHolder.getView(R.id.tv);

            baseViewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectPos = position;
                    notifyDataSetChanged();
                    areaSubAdapter.areaDoubleBean = area;
                    if (position == 0) {
                        dismiss();
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick("", "", area.getAreaName(), "");
                        }

                    }
                    areaSubAdapter.setNewData(area.getList());
                }
            });

            if (currentSelectPos == position) {
                //如果被点击，设置当前TextView被选中
                tv.setSelected(true);
            } else {
                //如果没有被点击，设置当前TextView未被选中
                tv.setSelected(false);
            }
            baseViewHolder.setText(R.id.tv, area.getAreaName());
        }
    }


    class AreaSubAdapter extends BaseQuickAdapter<AreaDoubleBean.ListBean, BaseViewHolder> {
        private int currentSelectPos = -1;
        public AreaDoubleBean areaDoubleBean;

        public AreaSubAdapter(List<AreaDoubleBean.ListBean> data) {
            super(R.layout.area_pop_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final AreaDoubleBean.ListBean data) {
            final int position = baseViewHolder.getAdapterPosition();
            final TextView tv = (TextView) baseViewHolder.getView(R.id.tv);

            baseViewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectPos = position;
                    notifyDataSetChanged();
                    dismiss();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(areaDoubleBean.getAreaId(), data.getTradingCode(), areaDoubleBean.getAreaName(), data.getTradingName());
                    }
                }
            });

            if (currentSelectPos == position) {
                //如果被点击，设置当前TextView被选中
                tv.setSelected(true);
            } else {
                //如果没有被点击，设置当前TextView未被选中
                tv.setSelected(false);
            }
            baseViewHolder.setText(R.id.tv, data.getTradingName());


        }
    }


}
