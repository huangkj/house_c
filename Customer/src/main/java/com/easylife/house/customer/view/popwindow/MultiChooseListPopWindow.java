package com.easylife.house.customer.view.popwindow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.BaseView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiChooseListPopWindow<T extends MultiChooseListPopWindow.TextImp> extends PopupWindow {
    private final Context mContext;
    private final List<T> mDatas;
    private MyAdapter myAdapter;
    private OnConfirmClickListener mOnConfirmClickListener;


    public interface TextImp {
        String getText();

        boolean isSelect();

        void setSelect(boolean select);
    }

    public MultiChooseListPopWindow(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        initView();
    }


    public interface OnConfirmClickListener<T> {
        void onConfirmClick(List<T> selectedList);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener l) {
        mOnConfirmClickListener = l;
    }


    private void initView() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        View popView = View.inflate(mContext, R.layout.multi_choose_pop_layout, null);
        LinearLayout llContent = (LinearLayout) popView.findViewById(R.id.llContent);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) (ScreenUtils.getScreenHeight() * 0.4)));
        llContent.setLayoutParams(layoutParams);


        RecyclerView rcvContent = (RecyclerView) popView.findViewById(R.id.rcvContent);
        TextView tvConfirm = (TextView) popView.findViewById(R.id.tvConfirm);


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onConfirmClick(getSelectedList());
                    dismiss();
                }
            }
        });


        myAdapter = new MyAdapter(mDatas);
        rcvContent.setLayoutManager(new LinearLayoutManager(mContext));
        rcvContent.setAdapter(myAdapter);

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


    class MyAdapter extends BaseQuickAdapter<T, BaseViewHolder> {

        public MyAdapter(@Nullable List<T> data) {
            super(R.layout.select_order_pop_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final T item) {
            final int layoutPosition = helper.getLayoutPosition();

            final TextView tvItem = (TextView) helper.getView(R.id.tvItem);
            if (layoutPosition == 0) {
                tvItem.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.tv_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvItem.setCompoundDrawables(drawable, null, null, null);
            }
            helper.setText(R.id.tvItem, item.getText());


            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutPosition == 0) {
                        item.setSelect(true);
                        setOtherItemUnSelect();
                    } else {
                        item.setSelect(!item.isSelect());
                        getData().get(0).setSelect(false);
                    }

                    notifyDataSetChanged();
                }
            });
            tvItem.setSelected(item.isSelect());

        }

    }

    /**
     * 获取选中的item
     *
     * @return
     */
    public List<T> getSelectedList() {
        ArrayList<T> ts = new ArrayList<>();
        List<T> data = myAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            T t = data.get(i);
            if (t.isSelect()) {
                ts.add(t);
            }
        }

        return ts;
    }


    /**
     * 将1~end的所有item致为未选中
     */
    private void setOtherItemUnSelect() {
        List<T> data = myAdapter.getData();
        for (int i = 1; i < data.size(); i++) {
            data.get(i).setSelect(false);
        }
    }
}
