package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2017/11/21 15:53.
 * 描述：adapter基类
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder> {

    protected ItemListener<T> itemListener;
    protected List<T> data = new ArrayList<>();
    protected Activity activity;

    public BaseRecycleAdapter(Activity activity, List<T> data) {
        this.activity = activity;
        if (data != null)
            this.data.addAll(data);
    }

    public void setItemClickListener(ItemListener<T> listener) {
        this.itemListener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(getLayoutId(), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRecycleAdapter.BaseViewHolder holder, final int position) {
        bindData(holder, position, data.get(position));
    }


    /**
     * 刷新数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<T> data) {
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 绑定数据
     *
     * @param holder   具体的viewHolder
     * @param position 对应的索引
     */
    protected abstract void bindData(BaseViewHolder holder, int position, T itemData);


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    public class BaseViewHolder extends RecyclerView.ViewHolder {


        protected View parentView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
        }

        /**
         * 获取设置的view
         *
         * @param id
         * @return
         */
        public View getView(int id) {
            View view = itemView.findViewById(id);
            return view;
        }

        /**
         * 设置是否显示
         *
         * @param viewId
         * @param visible
         */
        public void setVisible(int viewId, int visible) {
            View view = getView(viewId);
            if (view == null)
                return;
            view.setVisibility(visible);
        }

        /**
         * 设置文本属性
         *
         * @param viewId
         * @param text
         */
        public void setText(int viewId, String text) {
            View view = getView(viewId);
            if (view == null)
                return;
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }

        /**
         * 设置图片
         *
         * @param viewId
         * @param resource
         */
        public void setImageUrl(int viewId, int resource) {
            View view = getView(viewId);
            if (view == null)
                return;
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(resource);
            }
        }

//        /**
//         * 设置图片
//         *
//         * @param viewId
//         * @param imageUrl
//         */
//        public void setImageUrl(int viewId, String imageUrl) {
//            View view = getView(viewId);
//            if (view == null)
//                return;
//            if (view instanceof ImageView) {
//                CacheManager.initImage(activity, (ImageView) view, imageUrl);
//            }
//        }

    }

    /**
     * 获取子item
     *
     * @return
     */
    public abstract int getLayoutId();


    public interface ItemListener<T> {
        public void onClick(int viewId, int position, T data);

        /**
         * TODO 长按事件
         *
         * @param position
         * @param data
         */
        public void onLongClick(int position, T data);

        /**
         * TODO 双击事件
         *
         * @param position
         * @param data
         */
        public void onDoubleClick(int position, T data);
    }
}