package com.easylife.house.customer.view.popwindow;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class HouseTypeFilterPopWindow extends PopupWindow {
    private final Context mContext;
    private String[] houseType;
    private String[] houseType2;
    private LinearLayout llType;
    private TagFlowLayout flHouseType;
    private TagFlowLayout flHouseCount;

    private TextView tvType;
    private TextView tvHouseType;
    private OnConfirmClickListener mOnConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirmClick(Object[] pos1, Object[] pos2);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener l) {
        mOnConfirmClickListener = l;
    }

    public HouseTypeFilterPopWindow(Context context) {
        mContext = context;
        initView();
    }

    public HouseTypeFilterPopWindow(Context context, String[] houseType, String[] houseType2) {
        mContext = context;
        this.houseType = houseType;
        this.houseType2 = houseType2;
        initView();
    }

    public void setTypeTitle(String text, String text2) {
        tvType.setText(text);
        tvHouseType.setText(text2);
    }


    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.house_type_pop_layout, null);
        llType = (LinearLayout) popView.findViewById(R.id.llType);
        flHouseType = (TagFlowLayout) popView.findViewById(R.id.flHouseType);
        flHouseCount = (TagFlowLayout) popView.findViewById(R.id.flHouseCount);
        tvType = (TextView) popView.findViewById(R.id.tvType);
        tvHouseType = (TextView) popView.findViewById(R.id.tvHouseType);
        llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TagAdapter<String> houseTypeAdapter = new TagAdapter<String>(houseType) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.house_type_pop_item, null);
                tv.setText(s);
                return tv;
            }
        };
        TagAdapter<String> houseCountAdapter = new TagAdapter<String>(houseType2) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.house_type_pop_item, null);
                tv.setText(s);
                return tv;
            }
        };

        flHouseType.setAdapter(houseTypeAdapter);
        flHouseCount.setAdapter(houseCountAdapter);


        popView.findViewById(R.id.pop_v_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        popView.findViewById(R.id.tvClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flHouseType.onChanged();
                flHouseCount.onChanged();
            }
        });

        popView.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Object[] pos1 = flHouseType.getSelectedList().toArray();
                Object[] pos2 = flHouseCount.getSelectedList().toArray();
                if (null != mOnConfirmClickListener) {
                    mOnConfirmClickListener.onConfirmClick(pos1, pos2);
                }
            }
        });


        setContentView(popView);
        setFocusable(true);
        setOutsideTouchable(true);
    }


}
