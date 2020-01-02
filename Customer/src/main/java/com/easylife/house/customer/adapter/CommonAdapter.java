package com.easylife.house.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 项目名称：gaocaisheng3.0
 * 类描述：通用的adpter，调用时需要传人上下文、数据、布局文件id
 * 创建人：lh
 * 创建时间：2017/2/8 17:29
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    protected List<T> mDatas;
    protected int layoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    public CommonAdapter(Context context, int layoutId) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getCconvertView();
    }

    /**
     * 根据holder设置数据，调用时传入通用的ViewHolder、javaBean即可
     *
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getCount() {
        return null == mDatas ? 0 : mDatas.size();
    }
}
