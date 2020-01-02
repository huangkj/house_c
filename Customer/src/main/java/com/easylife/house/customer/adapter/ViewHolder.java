package com.easylife.house.customer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 项目名称：gaocaisheng3.0
 * 类描述：通用的ViewHolder，如果ViewHolder == null，初始化时需要传入上下文、ViewGroup、布局文件id、position
 * 创建人：lh
 * 创建时间：2017/2/8 17:30
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mCconvertView;
    private Context mContext;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mContext = context;
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mCconvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mCconvertView.setTag(this);

    }

    ;

    /**
     * 获取Holder 注意Holder的复用
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder getHolder(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {
        if (null == convertView) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {

        View childView = mViews.get(viewId);
        if (null == childView) {
            childView = mCconvertView.findViewById(viewId);
            mViews.put(viewId, childView);
        }
        return (T) childView;
    }

    public int getPosition() {
        return mPosition;
    }

    public View getCconvertView() {
        return mCconvertView;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setTextViewText(int viewId, String text) {
        TextView tv = (TextView) getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setTextViewText(int viewId, CharSequence text) {
        TextView tv = (TextView) getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置Button的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setButtonText(int viewId, String text) {
        Button btn = (Button) getView(viewId);
        btn.setText(text);
        return this;
    }

    /**
     * 设置Image的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = (ImageView) getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 设置Bitmap的值
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = (ImageView) getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }
//
//    /**
//     * 根据图片url设置Image的值
//     *
//     * @param viewId
//     * @return
//     */
//    public ViewHolder setImageURL(int viewId, String url) {
//        ImageView iv = (ImageView) getView(viewId);
//        ImageLoader.getInstance().displayImage(url, iv);
//        return this;
//    }

    /**
     * 设置ViewGroup的背景色
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewHolder setViewGroupBg(int viewId, int colorId) {
        ViewGroup vg = (ViewGroup) getView(viewId);
        vg.setBackgroundResource(colorId);
        return this;
    }

    /**
     * 设置TextView的顶部图片
     *
     * @param viewId TextView的Id
     * @param resId  图片资源Id
     * @return
     */
    public ViewHolder setTextViewTopDrawable(int viewId, int resId) {
        TextView textView = (TextView) getView(viewId);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
        return this;
    }

    /**
     * 设置TextView的文本颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setTextViewColor(int viewId, int color) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    /**
     * 设置TextView的文本颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setTextViewColorRes(int viewId, int color) {
        TextView textView = (TextView) getView(viewId);
        textView.setTextColor(mContext.getResources().getColor(color));
        return this;
    }

    /**
     * 设置文本的背景色
     */
    public ViewHolder setTextViewBackground(int viewId, int color) {
        TextView textView = (TextView) getView(viewId);
        textView.setBackgroundColor(mContext.getResources().getColor(color));
        return this;
    }

    /**
     * 设置控件的显示和隐藏
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public ViewHolder setViewVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * 设置EditText的内容
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setEditTextText(int viewId, String text) {
        EditText editText = getView(viewId);
        editText.setText(text);
        return this;
    }

    /**
     * 设置EditText的焦点变化监听
     *
     * @param viewId
     * @param l
     * @return
     */
    public ViewHolder setEditTextOnFocusChangedListener(int viewId, View.OnFocusChangeListener l) {
        EditText editText = getView(viewId);
        editText.setOnFocusChangeListener(l);
        return this;
    }

    /**
     * 设置TextView的点击监听
     *
     * @param viewId
     * @param l
     * @return
     */
    public ViewHolder setTextViewOnClickListener(int viewId, View.OnClickListener l) {
        TextView textView = getView(viewId);
        textView.setOnClickListener(l);
        return this;
    }
}
