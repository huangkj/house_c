package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemSelectAdapter;
import com.easylife.house.customer.bean.ItemSelect;

import java.util.List;


public class PopuWindowUtil {
    private PopupWindow popupWindow;
    public ItemSelectAdapter selectAdapter;

    /**
     * 华为部分手机需要另行判断设置
     *
     * @param activity
     * @param v
     * @param mPopupWindow
     */
    public static void showAsDropDown(Activity activity, View v, PopupWindow mPopupWindow) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            int[] a = new int[2];
            v.getLocationInWindow(a);
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, a[1] + v.getHeight());
        } else {
            mPopupWindow.showAsDropDown(v);
        }
    }

    public PopuWindowUtil(Context context, List<ItemSelect> data, final String defaultItem, final onPopuItemClick itemClick) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.pop_top_select, null);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.listview);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        selectAdapter = new ItemSelectAdapter(R.layout.pub_item_select, null);
        recyclerView.setAdapter(selectAdapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                itemClick.onItemClick((ItemSelect) baseQuickAdapter.getItem(i), i);
            }
        });
        popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.GrayLighter));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    /**
     * onItemClick
     */
    public interface onPopuItemClick {
        void onItemClick(ItemSelect item, int position);
    }

}
