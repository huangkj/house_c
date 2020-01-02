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
import com.easylife.house.customer.bean.CityAreaBean;

import java.util.List;

public class AreaPopWindow extends PopupWindow {

    private List<CityAreaBean> areas;


    private final Context mContext;
    private RecyclerView rcvArea;
    private LinearLayout llArea;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(CityAreaBean areaBean);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public AreaPopWindow(Context context) {
        mContext = context;
        initView();
    }

    public AreaPopWindow(Context context, List<CityAreaBean> areas) {
        mContext = context;
        this.areas = areas;
        CityAreaBean cityAreaBean = new CityAreaBean();
        cityAreaBean.area = "全部";
        cityAreaBean.areaid = "";
        areas.add(0, cityAreaBean);
        initView();
    }

    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.area_pop_layout, null);
        llArea = (LinearLayout) popView.findViewById(R.id.llArea);
        rcvArea = (RecyclerView) popView.findViewById(R.id.rcvArea);


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) (ScreenUtils.getScreenHeight() * 0.5)));
        llArea.setLayoutParams(layoutParams);


        AreaAdapter areaAdapter = new AreaAdapter(areas);

        rcvArea.setLayoutManager(new LinearLayoutManager(mContext));
        rcvArea.setAdapter(areaAdapter);


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


    class AreaAdapter extends BaseQuickAdapter<CityAreaBean, BaseViewHolder> {
        private int currentSelectPos = -1;

        public AreaAdapter(List<CityAreaBean> data) {
            super(R.layout.area_pop_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final CityAreaBean area) {
            final int position = baseViewHolder.getAdapterPosition();
            final TextView tv = (TextView) baseViewHolder.getView(R.id.tv);

            baseViewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectPos = position;
                    notifyDataSetChanged();
                    dismiss();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(area);
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
            baseViewHolder.setText(R.id.tv, area.area);


        }
    }


}
