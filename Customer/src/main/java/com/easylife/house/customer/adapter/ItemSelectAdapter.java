package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemSelect;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：
 */

public class ItemSelectAdapter extends BaseQuickAdapter<ItemSelect, BaseViewHolder> {

    public ItemSelectAdapter(int layoutResId, List<ItemSelect> data) {
        super(layoutResId, data);
    }


    private ItemSelect defaultItem;
    private String defaultItemText;
    private int gravity = Gravity.LEFT;

    public void setDefaultItem(ItemSelect defaultItem) {
        this.defaultItem = defaultItem;
        defaultItemText = null;
        notifyDataSetChanged();
    }

    public void setDefaultItem(String defaultItemText) {
        this.defaultItemText = defaultItemText;
        defaultItem = null;
        notifyDataSetChanged();
    }

    public void setGravity(int gravity) {
        if (gravity != 0)
            this.gravity = gravity;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ItemSelect itemSelect) {
        if ((defaultItem != null && itemSelect.getId().equals(defaultItem.getId()))
                || (!TextUtils.isEmpty(defaultItemText) && defaultItemText.equals(itemSelect.getText()))) {
            baseViewHolder.setTextColor(R.id.name, mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            baseViewHolder.setTextColor(R.id.name, mContext.getResources().getColor(R.color.text_normal));
        }
        TextView name = baseViewHolder.getView(R.id.name);
        name.setGravity(gravity);
        name.setText(itemSelect.getText());
    }
}
