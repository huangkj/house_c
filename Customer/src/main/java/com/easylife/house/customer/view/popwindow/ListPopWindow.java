package com.easylife.house.customer.view.popwindow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;

import java.util.Arrays;
import java.util.List;

public class ListPopWindow extends PopupWindow {
    private final Context mContext;
    private final String[] mTexts;
    private final int maxLength;
    private LinearLayout llPrice;
    private RecyclerView rcvPrice;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private MyAdapter myAdapter;

    public ListPopWindow(Context context, String[] texts, int maxLength) {
        mContext = context;
        mTexts = texts;
        this.maxLength = maxLength;
        initView();
    }

    private OnConfirmClickListener mOnConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirmClick(String text);

        void onConfirmClick(String min, String max);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener l) {
        mOnConfirmClickListener = l;
    }

    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.house_price_pop_layout, null);

        etMinPrice = (EditText) popView.findViewById(R.id.etMinPrice);
        etMaxPrice = (EditText) popView.findViewById(R.id.etMaxPrice);
        etMaxPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength) {
        }});
        etMinPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength) {
        }});

        llPrice = (LinearLayout) popView.findViewById(R.id.llPrice);
        rcvPrice = (RecyclerView) popView.findViewById(R.id.rcvPrice);
        llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickListener != null) {

                }
            }
        });

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) (ScreenUtils.getScreenHeight() * 0.4)));
        llPrice.setLayoutParams(layoutParams);


        myAdapter = new MyAdapter(Arrays.asList(mTexts));
        rcvPrice.setLayoutManager(new LinearLayoutManager(mContext));
        rcvPrice.setAdapter(myAdapter);
        popView.findViewById(R.id.pop_v_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popView.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min = etMinPrice.getText().toString().trim();
                String max = etMaxPrice.getText().toString().trim();
                dismiss();
                if (mOnConfirmClickListener != null) {
                    if (!TextUtils.isEmpty(min) || !TextUtils.isEmpty(max)) {
//                        //最大or最小框有数据
//                        if (Integer.parseInt(min) > Integer.parseInt(max)) {
//                            ToastUtils.showShort("最小必须大于最大");
//                            return;
//                        }
                        mOnConfirmClickListener.onConfirmClick(min, max);
                        currentSelectPos = -1;
                        myAdapter.notifyDataSetChanged();
                    }
//                    else {
//                        if (currentSelectPos == -1) {
//                            mOnConfirmClickListener.onConfirmClick(mTexts[0]);
//                        } else {
//                            mOnConfirmClickListener.onConfirmClick(mTexts[currentSelectPos]);
//                        }
//                    }
                }
            }
        });


        setContentView(popView);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    private int currentSelectPos = -1;

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MyAdapter(List<String> data) {
            super(R.layout.area_pop_item, data);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, String s) {
            final int position = baseViewHolder.getAdapterPosition();
            final TextView tv = (TextView) baseViewHolder.getView(R.id.tv);

            baseViewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectPos = position;
                    notifyDataSetChanged();
                    if (currentSelectPos == -1) {
                        mOnConfirmClickListener.onConfirmClick(mTexts[0]);
                    } else {
                        mOnConfirmClickListener.onConfirmClick(mTexts[currentSelectPos]);
                    }
                    etMinPrice.setText(null);
                    etMaxPrice.setText(null);
                    dismiss();

                }
            });

            if (currentSelectPos == position) {
                //如果被点击，设置当前TextView被选中
                tv.setSelected(true);
            } else {
                //如果没有被点击，设置当前TextView未被选中
                tv.setSelected(false);
            }
            baseViewHolder.setText(R.id.tv, s);
        }
    }
}
