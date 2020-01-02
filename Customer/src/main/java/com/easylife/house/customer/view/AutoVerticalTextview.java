package com.easylife.house.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HeadLineBean;

import java.util.ArrayList;

/**
 * Created by admin on 2017/9/26.
 */

public class AutoVerticalTextview extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;

    private float mTextSize = 16;
    private int mPadding = 0;
    private int textColor = Color.BLACK;
    private ArrayList<HeadLineBean.ListBean> mDataList;

    /**
     * @param textSize  字号
     * @param padding   内边距
     * @param textColor 字体颜色
     */
    public void setText(float textSize, int padding, int textColor) {
        mTextSize = textSize;
        mPadding = padding;
        this.textColor = textColor;
    }

    private OnItemClickListener itemClickListener;
    private Context mContext;
    private int currentId = -1;
    //    private ArrayList<String> textList;
    private Handler handler;

    public AutoVerticalTextview(Context context) {
        this(context, null);
        mContext = context;
    }

    public AutoVerticalTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mDataList = new ArrayList<HeadLineBean.ListBean>();
    }

    public void setAnimTime(long animDuration) {
        setFactory(this);

//        Animation in = new TranslateAnimation(0, 0, animDuration, 0);
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in);
        in.setDuration(animDuration);
//        in.setInterpolator(new AccelerateInterpolator());
//        Animation out = new TranslateAnimation(0, 0, 0, -animDuration);
        Animation out = AnimationUtils.loadAnimation(mContext, R.anim.push_up_out);
        out.setDuration(animDuration);
//        out.setInterpolator(new AccelerateInterpolator());
        setInAnimation(in);
        setOutAnimation(out);
    }

    /**
     * 间隔时间
     *
     * @param time
     */
    public void setTextStillTime(final long time) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_START_AUTO_SCROLL:
                        if (mDataList.size() > 0) {
                            currentId++;
//                            if (mDataList.get(currentId % mDataList.size()).getNotifyType() == 2) {
//                                ((TextView) getNextView()).setTextColor(getResources().getColor(R.color.paper_error));
//                            } else {
//                                ((TextView) getNextView()).setTextColor(getResources().getColor(R.color.gray_font_color_7a));
//                            }
                            setText(mDataList.get(currentId % mDataList.size()).getTitle());
                        }
                        handler.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, time);
                        break;
                    case FLAG_STOP_AUTO_SCROLL:
                        handler.removeMessages(FLAG_START_AUTO_SCROLL);
                        break;
                }
            }
        };
    }

    /**
     * 设置数据源
     *
     * @param titles
     */
    public void setTextList(ArrayList<String> titles) {
//        textList.clear();
//        textList.addAll(titles);
        currentId = -1;
    }

    public void setDataList(ArrayList<HeadLineBean.ListBean> datas) {
        mDataList.clear();
        this.mDataList = datas;
        currentId = -1;
    }

    /**
     * 开始滚动
     */
    public void startAutoScroll() {
        handler.sendEmptyMessage(FLAG_START_AUTO_SCROLL);
    }

    /**
     * 停止滚动
     */
    public void stopAutoScroll() {
        handler.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL);
    }

    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        t.setMaxLines(1);
        t.setPadding(mPadding, mPadding, mPadding, mPadding);
        t.setTextColor(textColor);
        t.setTextSize(mTextSize);
        t.setEllipsize(TextUtils.TruncateAt.END);
        t.setSingleLine();
        t.setClickable(true);
        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && mDataList.size() > 0 && currentId != -1) {
                    itemClickListener.onItemClick(currentId % mDataList.size());
                }
            }
        });
        return t;
    }

    /**
     * 设置点击事件监听
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 轮播文本点击监听器
     */
    public interface OnItemClickListener {
        /**
         * 点击回调
         *
         * @param position 当前点击ID
         */
        void onItemClick(int position);
    }

}
