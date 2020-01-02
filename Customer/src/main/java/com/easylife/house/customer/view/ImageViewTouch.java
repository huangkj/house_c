package com.easylife.house.customer.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageViewTouch extends ImageView {

    public ImageViewTouch(Context context) {
        super(context);
    }

    public ImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewTouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final float Trans = -25f;
    /**
     * 是否触摸变暗,默认是变暗
     */
    public boolean canTouchChange = true;

    private final static float[] BT_SELECTED = new float[]{1, 0, 0, 0, Trans,
            0, 1, 0, 0, Trans, 0, 0, 1, 0, Trans, 0, 0, 0, 1, 0};

    private ColorMatrixColorFilter mPressFilter;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canTouchChange) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (getDrawable() != null) {
                        if (mPressFilter == null) {
                            mPressFilter = new ColorMatrixColorFilter(BT_SELECTED);
                        }
                        getDrawable().setColorFilter(mPressFilter);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (getDrawable() != null) {
                        getDrawable().clearColorFilter();
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

}