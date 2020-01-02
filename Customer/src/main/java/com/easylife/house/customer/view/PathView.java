package com.easylife.house.customer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2017/4/27 15:44.
 * 描述：地图圈图画板
 */

public class PathView extends View {
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private float pathX;
    private float pathY;
    private Boolean isActionUp = false;
    private OnFinishListener listener;
    private List<Point> points;
    private int color = Color.parseColor("#661B85FF");

    public PathView(Context context) {
        super(context);
        init(context);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setPathColor(int color) {
        if (color == 0)
            return;
        this.color = color;
    }

    private void init(Context context) {
        Log.d("path", "init");
        //设置画笔颜色
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5.0f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isActionUp) {
            isActionUp = false;
            mPaint.setStyle(Paint.Style.STROKE);
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mPath.close();
                mPaint.setStyle(Paint.Style.FILL);
                RectF r = new RectF();
                mPath.computeBounds(r, true);
                if (listener != null)
                    listener.onFinish(mPath, r);
                break;

        }
        postInvalidate();
        return true;
    }

    private void touchMove(MotionEvent event) {
        float disX = Math.abs(event.getX() - pathX);
        float disy = Math.abs(event.getY() - pathY);
        if (disX > 5 || disy > 5) {
            mPath.lineTo(event.getX(), event.getY());
            pathX = event.getX();
            pathY = event.getY();
            points.add(new Point((int) pathX, (int) pathY));
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public void reset() {
        points = new ArrayList<>();
        mPath.reset();
        postInvalidate();
    }

    private void touchDown(MotionEvent event) {
        points = new ArrayList<>();
        mPath.reset();
        pathX = event.getX();
        pathY = event.getY();
        points.add(new Point((int) pathX, (int) pathY));
        mPath.moveTo(pathX, pathY);
        isActionUp = true;
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public interface OnFinishListener {
        public void onFinish(Path p, RectF r);
    }

}