package com.easylife.house.customer.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class ButtonTouch extends Button {

    public ButtonTouch(Context context) {
        super(context);
    }

    public ButtonTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonTouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final float Trans = -15f;
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
                    if (getBackground() != null) {
                        if (mPressFilter == null) {
                            mPressFilter = new ColorMatrixColorFilter(BT_SELECTED);
                        }
                        getBackground().setColorFilter(mPressFilter);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (getBackground() != null) {
                        getBackground().clearColorFilter();
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

}