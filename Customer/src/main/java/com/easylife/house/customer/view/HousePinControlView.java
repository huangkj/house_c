package com.easylife.house.customer.view;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ModelHouse;
import com.easylife.house.customer.bean.ModelUnit;
import com.easylife.house.customer.bean.StatusManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 房源销控自定义控件
 */
public class HousePinControlView extends View implements StatusManager {
    private final boolean DBG = false;

    Paint paint = new Paint();
    Paint overviewPaint = new Paint();
    Paint lineNumberPaint;
    float lineNumberTxtHeight;
    private ModelHouse houseSelected;
    /**
     * 楼层是否是倒序
     */
    private boolean isInvertedRow = false;

    /**
     * 设置行号 默认显示 1,2,3....数字
     *
     * @param lineNumbers
     */
    public void setLineNumbers(ArrayList<String> lineNumbers) {
        this.lineNumbers = lineNumbers;
        invalidate();
    }

    /**
     * 用来保存所有行号
     */
    ArrayList<String> lineNumbers = new ArrayList<>();

    Paint.FontMetrics lineNumberPaintFontMetrics;
    Matrix matrix;

    /**
     * 座位水平间距
     */
    int horSpacing;

    /**
     * 座位垂直间距
     */
    int verSpacing;

    /**
     * 行号宽度
     */
    int numberWidth;

    /**
     * 行数
     */
    int row;

    /**
     * 列数
     */
    int column;

    /**
     * 可选时座位的图片
     */
    Bitmap bitmapAvailable;
    int seatAvailableResID;
    Bitmap bitmapAvailableOver;
    int seatAvailableResIDOver;
    /**
     * 选中时座位的图片
     */
    Bitmap bitmapChecked;
    int seatCheckedResID;
    Bitmap bitmapCheckedOver;
    int seatCheckedResIDOver;

    /**
     * 座位已经售出时的图片
     */
    Bitmap bitmapSold;
    int seatSoldResID;
    Bitmap bitmapSoldOver;
    int seatSoldResIDOver;

    Bitmap bitmapSelected;
    int seatSelectedResID;
    Bitmap bitmapSelectedOver;
    int seatSelectedResIDOver;

    Bitmap overviewBitmap;

    int lastX;
    int lastY;

    /**
     * 整个座位图的宽度
     */
    int seatAllBitmapWidth;

    /**
     * 整个座位图的高度
     */
    int seatAllBitmapHeight;

    /**
     * 标识是否需要绘制座位图
     */
    boolean isNeedDrawSeatBitmap = true;

    /**
     * 概览图白色方块高度
     */
    float rectHeight;

    /**
     * 概览图白色方块的宽度
     */
    float rectWidth;

    /**
     * 概览图上方块的水平间距
     */
    float overviewSpacing;

    /**
     * 概览图上方块的垂直间距
     */
    float overviewVerSpacing;

    /**
     * 概览图的比例
     */
    float overviewScale = 2.0f;

    /**
     * 荧幕高度
     */
    float screenHeight;

    /**
     * 标识是否正在缩放
     */
    boolean isScaling;
    float scaleX, scaleY;

    /**
     * 是否是第一次缩放
     */
    boolean firstScale = true;

    /**
     * 最多可以选择的座位数量
     */
    int maxSelected = 1;


    /**
     * 整个概览图的宽度
     */
    float rectW;

    /**
     * 整个概览图的高度
     */
    float rectH;

    Paint headPaint;

    /**
     * 标识是否需要绘制概览图
     */
    boolean isDrawOverview = false;

    /**
     * 标识是否需要更新概览图
     */
    boolean isUpdateOverview = true;

    int overview_selected;
    int overview_checked;
    int overview_sold;
    int txt_color;

    boolean isOnClick;

    private int downX, downY;

    /**
     * 是否是多点触控
     */
    private boolean isPointers;

    Paint pathPaint;
    RectF rectF;

    /**
     * 头部下面横线的高度
     */
    int borderHeight = 1;
    Paint redBorderPaint;

    /**
     * 默认的座位图宽度,如果使用的自己的座位图片比这个尺寸大或者小,会缩放到这个大小
     */
    private float defaultImgW = 88;

    /**
     * 默认的座位图高度
     */
    private float defaultImgH = 80;

    /**
     * 座位图片的宽度
     */
    private int seatWidth;

    /**
     * 座位图片的高度
     */
    private int seatHeight;

    public HousePinControlView(Context context) {
        super(context);
    }

    public HousePinControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeatTableView);
        overview_selected = typedArray.getColor(R.styleable.SeatTableView_overview_checked, Color.GREEN);
        overview_checked = typedArray.getColor(R.styleable.SeatTableView_overview_checked, Color.YELLOW);
        overview_sold = typedArray.getColor(R.styleable.SeatTableView_overview_sold, Color.DKGRAY);
        txt_color = typedArray.getColor(R.styleable.SeatTableView_txt_color, Color.WHITE);
        seatCheckedResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_checked, R.mipmap.icon_house_checked);
        seatCheckedResIDOver = typedArray.getResourceId(R.styleable.SeatTableView_seat_checked, R.mipmap.icon_house_checked_s);
        seatSoldResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold, R.mipmap.icon_house_sold);
        seatSoldResIDOver = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold, R.mipmap.icon_house_sold_s);
        seatSelectedResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_selected, R.mipmap.icon_house_selected);
        seatSelectedResIDOver = typedArray.getResourceId(R.styleable.SeatTableView_overview_selected, R.mipmap.icon_house_selected_s);
        seatAvailableResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_available, R.mipmap.icon_house_available);
        seatAvailableResIDOver = typedArray.getResourceId(R.styleable.SeatTableView_seat_available, R.mipmap.icon_house_available_s);
        typedArray.recycle();
    }

    public HousePinControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float xScale1 = 1;
    float yScale1 = 1;

    private void init() {
        horSpacing = (int) dip2Px(5);
        verSpacing = (int) dip2Px(10);

        bitmapAvailable = BitmapFactory.decodeResource(getResources(), seatAvailableResID);
        bitmapAvailableOver = BitmapFactory.decodeResource(getResources(), seatAvailableResIDOver);

        float scaleX = defaultImgW / bitmapAvailable.getWidth();
        float scaleY = defaultImgH / bitmapAvailable.getHeight();
        xScale1 = scaleX;
        yScale1 = scaleY;

        seatHeight = (int) (bitmapAvailable.getHeight() * yScale1);
        seatWidth = (int) (bitmapAvailable.getWidth() * xScale1);

        bitmapChecked = BitmapFactory.decodeResource(getResources(), seatCheckedResID);
        bitmapCheckedOver = BitmapFactory.decodeResource(getResources(), seatCheckedResIDOver);
        bitmapSold = BitmapFactory.decodeResource(getResources(), seatSoldResID);
        bitmapSoldOver = BitmapFactory.decodeResource(getResources(), seatSoldResIDOver);
        bitmapSelected = BitmapFactory.decodeResource(getResources(), seatSelectedResID);
        bitmapSelectedOver = BitmapFactory.decodeResource(getResources(), seatSelectedResIDOver);

        seatAllBitmapWidth = (int) (column * bitmapAvailable.getWidth() * xScale1 + (column - 1) * horSpacing);
        seatAllBitmapHeight = (int) (row * bitmapAvailable.getHeight() * yScale1 + (row - 1) * verSpacing);
        paint.setColor(Color.RED);
        numberWidth = (int) dip2Px(30);

        screenHeight = dip2Px(20);

        headPaint = new Paint();
        headPaint.setStyle(Paint.Style.FILL);
        headPaint.setTextSize(24);
        headPaint.setColor(Color.WHITE);
        headPaint.setAntiAlias(true);

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(Color.parseColor("#e2e2e2"));

        redBorderPaint = new Paint();
        redBorderPaint.setAntiAlias(true);
        redBorderPaint.setColor(Color.RED);
        redBorderPaint.setStyle(Paint.Style.STROKE);
        redBorderPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 1);

        rectF = new RectF();

        rectHeight = seatHeight / overviewScale;
        rectWidth = seatWidth / overviewScale;
        overviewSpacing = horSpacing / overviewScale / 4;
        overviewVerSpacing = verSpacing / overviewScale / 4;

        rectW = column * rectWidth + (column - 1) * overviewSpacing + overviewSpacing * 2;
        rectH = row * rectHeight + (row - 1) * overviewVerSpacing + overviewVerSpacing * 2;
        overviewBitmap = Bitmap.createBitmap((int) rectW, (int) rectH, Bitmap.Config.ARGB_4444);

        lineNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineNumberPaint.setColor(bacColor);
        lineNumberPaint.setTextSize(getResources().getDisplayMetrics().density * 16);
        lineNumberTxtHeight = lineNumberPaint.measureText("4");
        lineNumberPaintFontMetrics = lineNumberPaint.getFontMetrics();
        lineNumberPaint.setTextAlign(Paint.Align.CENTER);

        if (lineNumbers == null || lineNumbers.size() == 0) {
            lineNumbers = new ArrayList<>();
            for (int i = 0; i < row; i++) {
                if (isInvertedRow) {
                    lineNumbers.add((row - i) + "#");
                } else {
                    lineNumbers.add((i + 1) + "#");
                }
            }
        }

        if (matrix == null) {
            matrix = new Matrix();
            matrix.postTranslate(numberWidth + horSpacing, screenHeight + borderHeight + verSpacing);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        startX = 0;

        if (row <= 0 || column == 0) {
            return;
        }

        leftOffset = 0;
        if (seatAllBitmapWidth < getWidth() - numberWidth) {
            leftOffset = (getWidth() - seatAllBitmapWidth) / 2 - numberWidth;
        }

        drawSeat(canvas);
        drawNumber(canvas);
        if (data != null && data.size() != 0) {
            int columnOffset = 0;
            for (int unitIndex = 0; unitIndex < data.size(); unitIndex++) {
                ModelUnit unit = data.get(unitIndex);
                columnOffset += unit.column + 1;
                drawScreen(canvas, unit.unitName, unit.column, columnOffset);
            }
        }

        if (isDrawOverview) {
            if (isUpdateOverview) {
                overviewBitmap = getOverviewBitmap();
            }
            canvas.drawBitmap(overviewBitmap, 0, 0, null);
            drawOverviewStroke(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();
        if (pointerCount > 1) {
            isPointers = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPointers = false;
                downX = x;
                downY = y;
                isDrawOverview = true;
                isUpdateOverview = true;
                handler.removeCallbacks(hideOverviewRunnable);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && !isOnClick) {
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !isPointers) {
                        int dx = x - lastX;
                        int dy = y - lastY;
                        if (matrix != null)
                            matrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.postDelayed(hideOverviewRunnable, 1500);

//                autoScale();
//                int downDX = Math.abs(x - downX);
//                int downDY = Math.abs(y - downY);
//                if ((downDX > 10 || downDY > 10) && !isPointers) {
//                    autoScroll();
//                }

                break;
        }
        isOnClick = false;
        lastY = y;
        lastX = x;

        return true;
    }

    private Runnable hideOverviewRunnable = new Runnable() {
        @Override
        public void run() {
            isDrawOverview = false;
            isUpdateOverview = false;
            invalidate();
        }
    };

    /**
     * 画顶部单元名称时计算X轴坐标
     */
    private float startX;
    /**
     * 绘图时的整体左侧冗余，以计算单元较少时的居中处理
     */
    private float leftOffset;

    /**
     * 绘制标题
     *
     * @param canvas
     * @param unitName
     * @param column       当前单元每楼层户数
     * @param columnOffset 之前单元综合（包括空白冗余列）
     */
    void drawScreen(Canvas canvas, String unitName, int column, int columnOffset) {
        if (TextUtils.isEmpty(unitName))
            unitName = "单元名称";
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(Color.parseColor("#e2e2e2"));
        pathPaint.setStrokeWidth(5);

        float unitSeatWidth = column * (seatWidth + horSpacing) - horSpacing;

        float startY = borderHeight;

        float centerX = leftOffset + startX + unitSeatWidth * getMatrixScaleX() / 2 + getTranslateX();

        float screenWidth = pathPaint.measureText(unitName) + dip2Px(20);

        startX = columnOffset * (seatWidth + horSpacing) * getMatrixScaleX();

        Path path = new Path();
        path.moveTo(centerX, startY);
        path.lineTo(centerX - screenWidth / 2, startY);
        path.lineTo(centerX - screenWidth / 2, screenHeight * getMatrixScaleY() + startY);
        path.lineTo(centerX + screenWidth / 2, screenHeight * getMatrixScaleY() + startY);
        path.lineTo(centerX + screenWidth / 2, startY);

        canvas.drawPath(path, pathPaint);

        pathPaint.setColor(Color.BLACK);
        pathPaint.setTextSize(20 * getMatrixScaleX());

        canvas.drawText(unitName, centerX - pathPaint.measureText(unitName) / 2, getBaseLine(pathPaint, startY, startY + screenHeight * getMatrixScaleY()), pathPaint);
    }

    Matrix tempMatrix = new Matrix();

    void drawSeat(Canvas canvas) {
        zoom = getMatrixScaleX();
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;

        if (data == null || data.size() == 0)
            return;

        for (int i = 0; i < row; i++) {
            float top = i * bitmapAvailable.getHeight() * yScale1 * scaleY + i * verSpacing * scaleY + translateY;

            float bottom = top + bitmapAvailable.getHeight() * yScale1 * scaleY;
            if (bottom < 0 || top > getHeight()) {
                continue;
            }

            for (int j = 0; j < column; j++) {
                float left = leftOffset + j * bitmapAvailable.getWidth() * xScale1 * scaleX + j * horSpacing * scaleX + translateX;

                float right = (left + bitmapAvailable.getWidth() * xScale1 * scaleY);

                if (right < 0 || left > getWidth()) {
                    continue;
                }

                // 获取房源状态
                int seatType = Integer.parseInt(getSeatType(i, j));

                // 移动到开始绘画点
                tempMatrix.setTranslate(left, top);
                // 缩放到初始比例绘画
                tempMatrix.postScale(xScale1, yScale1, left, top);
                // 缩放到当前比例
                tempMatrix.postScale(scaleX, scaleY, left, top);

                switch (seatType) {
                    case STATUS_AVAILABLE:
                        canvas.drawBitmap(bitmapAvailable, tempMatrix, paint);
                        break;
                    case STATUS_SOLD_CAN:
                    case STATUS_SOLD_WAIT:
                    case STATUS_SOLD_HAD:
                    case STATUS_SOLD_FORBID:
                    case STATUS_LOCKED:
                        canvas.drawBitmap(bitmapSold, tempMatrix, paint);
                        break;
                    case STATUS_NULL:
                        continue;
                }

                updateChecked(canvas, tempMatrix, paint, i, j);
                updateSelected(canvas, tempMatrix, paint, i, j);
            }
        }
    }

    //   更新正在查看的房源图标
    private void updateSelected(Canvas canvas, Matrix tempMatrix, Paint paint, int row, int column) {
        ModelHouse houseCurrent = getHouse(row, column);
        if (houseSelected != null && houseCurrent != null &&
                !TextUtils.isEmpty(houseSelected.id) &&
                houseSelected.id.equals(houseCurrent.id)
//                && (houseSelected.state == STATUS_AVAILABLE || houseSelected.state == STATUS_SOLD_CAN || houseSelected.state == STATUS_SOLD_WAIT)
                ) {
            canvas.drawBitmap(bitmapSelected, tempMatrix, paint);
        }
    }

    private void updateSelectedOver(Canvas canvas, float left, float top, Paint paint, int row, int column) {
        ModelHouse houseCurrent = getHouse(row, column);
        if (houseSelected != null && houseCurrent != null &&
                !TextUtils.isEmpty(houseSelected.id) &&
                houseSelected.id.equals(houseCurrent.id)
//                && (houseSelected.state == STATUS_AVAILABLE || houseSelected.state == STATUS_SOLD_CAN || houseSelected.state == STATUS_SOLD_WAIT)
                ) {
            canvas.drawBitmap(bitmapSelectedOver, left, top, paint);
        }
    }

    private void updateChecked(Canvas canvas, Matrix tempMatrix, Paint paint, int row, int column) {
        if (dataChecked != null && dataChecked.size() != 0) {
            ModelHouse houseCurrent = getHouse(row, column);

            for (ModelUnit unit : dataChecked) {
                if (unit == null || unit.estateRoomBeanList == null || unit.estateRoomBeanList.size() == 0)
                    continue;
                for (ModelHouse house : unit.estateRoomBeanList) {
                    if (house != null && houseCurrent != null &&
                            !TextUtils.isEmpty(house.id) &&
                            house.id.equals(houseCurrent.id)
                            ) {
                        canvas.drawBitmap(bitmapChecked, tempMatrix, paint);
                    }
                }
            }
        }
    }

    private void updateCheckedOver(Canvas canvas, float left, float top, Paint paint, int row, int column) {
        if (dataChecked != null && dataChecked.size() != 0) {
            ModelHouse houseCurrent = getHouse(row, column);

            for (ModelUnit unit : dataChecked) {
                if (unit == null || unit.estateRoomBeanList == null || unit.estateRoomBeanList.size() == 0)
                    continue;
                for (ModelHouse house : unit.estateRoomBeanList) {
                    if (house != null && houseCurrent != null &&
                            !TextUtils.isEmpty(house.id) &&
                            house.id.equals(houseCurrent.id)
                            ) {
                        canvas.drawBitmap(bitmapCheckedOver, left, top, paint);
                    }
                }
            }
        }
    }

    private String getSeatType(int row, int column) {
        if (data == null || data.size() == 0)
            return STATUS_NULL + "";
        int columnOffset = 0;
        if (isInvertedRow) {
            row = this.row - row - 1;
        }
        for (int i = 0; i < data.size(); i++) {
            ModelUnit unit = data.get(i);
            if (unit.estateRoomBeanList == null || unit.estateRoomBeanList.size() == 0) {
                columnOffset += data.get(i).column + 1;
                continue;
            }
            for (ModelHouse house : unit.estateRoomBeanList) {
                if (house.rowPosition == row && house.columnPosition + columnOffset == column) {
                    return house.state;
                }
            }
            columnOffset += data.get(i).column + 1;
        }
        return STATUS_NULL + "";
    }

    /**
     * 获取房源信息
     *
     * @param rowPosition
     * @param columnPosition
     * @return
     */
    private ModelHouse getHouse(int rowPosition, int columnPosition) {
        if (isInvertedRow) {
            rowPosition = this.row - rowPosition - 1;
        }

        if (data != null && data.size() != 0) {
            int columnOffset = 0;
            for (ModelUnit unit : data) {
                if (columnPosition - columnOffset >= unit.column) {
                    columnOffset += unit.column + 1;
                } else {
                    if (unit.estateRoomBeanList != null && unit.estateRoomBeanList.size() != 0) {
                        for (ModelHouse house : unit.estateRoomBeanList) {
                            if (house.rowPosition == rowPosition && house.columnPosition == columnPosition - columnOffset) {
                                house.unitId = unit.unitId;
                                house.unitName = unit.unitName;
                                house.buildName = unit.buildName;
                                house.buildId = unit.buildId;
                                return house;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    int bacColor = Color.parseColor("#7e000000");

    /**
     * 绘制行号
     */
    void drawNumber(Canvas canvas) {
        lineNumberPaint.setColor(bacColor);
        int translateY = (int) getTranslateY();
        float scaleY = getMatrixScaleY();

        rectF.top = translateY - lineNumberTxtHeight / 2 - 20;
        rectF.bottom = translateY + (seatAllBitmapHeight * scaleY) + lineNumberTxtHeight / 2 + 20;
        rectF.left = 0;
        rectF.right = numberWidth;
        canvas.drawRoundRect(rectF, numberWidth / 2, numberWidth / 2, lineNumberPaint);

        lineNumberPaint.setColor(Color.WHITE);

        for (int i = 0; i < lineNumbers.size(); i++) {
            float top = (i * seatHeight + i * verSpacing) * scaleY + translateY;
            float bottom = (i * seatHeight + i * verSpacing + seatHeight) * scaleY + translateY;
            float baseline = (bottom + top - lineNumberPaintFontMetrics.bottom - lineNumberPaintFontMetrics.top) / 2;

            canvas.drawText(lineNumbers.get(i), numberWidth / 2, baseline, lineNumberPaint);
        }

    }

    /**
     * 绘制概览图
     */
    void drawOverviewStroke(Canvas canvas) {

        //绘制红色框
        int left = (int) -getTranslateX();
        if (left < 0) {
            left = 0;
        }
        left /= overviewScale;
        left /= getMatrixScaleX();

        int currentWidth = (int) (getTranslateX() + (column * seatWidth + horSpacing * (column - 1)) * getMatrixScaleX());
        if (currentWidth > getWidth()) {
            currentWidth = currentWidth - getWidth();
        } else {
            currentWidth = 0;
        }
        int right = (int) (rectW - currentWidth / overviewScale / getMatrixScaleX());

        float top = -getTranslateY();
        if (top < 0) {
            top = 0;
        }
        top /= overviewScale;
        top /= getMatrixScaleY();
        if (top > 0) {
            top += overviewVerSpacing;
        }

        int currentHeight = (int) (getTranslateY() + (row * seatHeight + verSpacing * (row - 1)) * getMatrixScaleY());
        if (currentHeight > getHeight()) {
            currentHeight = currentHeight - getHeight();
        } else {
            currentHeight = 0;
        }
        int bottom = (int) (rectH - currentHeight / overviewScale / getMatrixScaleY());

        canvas.drawRect(left, top, right, bottom, redBorderPaint);
    }

    Bitmap getOverviewBitmap() {
        isUpdateOverview = false;

        int bac = Color.parseColor("#CC000000");
        overviewPaint.setColor(bac);
        overviewPaint.setAntiAlias(true);
        overviewPaint.setStyle(Paint.Style.FILL);
        overviewBitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(overviewBitmap);
        //绘制透明灰色背景
        canvas.drawRect(0, 0, rectW, rectH, overviewPaint);

        for (int i = 0; i < row; i++) {
            float top = i * rectHeight + i * overviewVerSpacing + overviewVerSpacing;
            for (int j = 0; j < column; j++) {

                int seatType = Integer.parseInt(getSeatType(i, j));
                float left;
                left = j * rectWidth + j * overviewSpacing + overviewSpacing;

                switch (seatType) {
                    case STATUS_AVAILABLE:
                        canvas.drawBitmap(bitmapAvailableOver, left, top, paint);
                        break;
                    case STATUS_NULL:
                        continue;
                    case STATUS_SOLD_CAN:
                    case STATUS_SOLD_WAIT:
                    case STATUS_SOLD_HAD:
                    case STATUS_SOLD_FORBID:
                    case STATUS_LOCKED:
                        // 锁定的数据以及图标暂无
                        canvas.drawBitmap(bitmapSoldOver, left, top, paint);
                        break;
                }
                updateCheckedOver(canvas, left, top, paint, i, j);
                updateSelectedOver(canvas, left, top, paint, i, j);
            }
        }

        return overviewBitmap;
    }


    /**
     * 自动回弹
     * 整个大小不超过控件大小的时候:
     * 往左边滑动,自动回弹到行号右边
     * 往右边滑动,自动回弹到右边
     * 往上,下滑动,自动回弹到顶部
     * <p>
     * 整个大小超过控件大小的时候:
     * 往左侧滑动,回弹到最右边,往右侧滑回弹到最左边
     * 往上滑动,回弹到底部,往下滑动回弹到顶部
     */
    private void autoScroll() {
        float currentSeatBitmapWidth = seatAllBitmapWidth * getMatrixScaleX();
        float currentSeatBitmapHeight = seatAllBitmapHeight * getMatrixScaleY();
        float moveYLength = 0;
        float moveXLength = 0;

        //处理左右滑动的情况
        if (currentSeatBitmapWidth < getWidth()) {
            if (getTranslateX() < 0 || getMatrixScaleX() < numberWidth + horSpacing) {
                //计算要移动的距离

                if (getTranslateX() < 0) {
                    moveXLength = (-getTranslateX()) + numberWidth + horSpacing;
                } else {
                    moveXLength = numberWidth + horSpacing - getTranslateX();
                }

            }
        } else {

            if (getTranslateX() < 0 && getTranslateX() + currentSeatBitmapWidth > getWidth()) {

            } else {
                //往左侧滑动
                if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    moveXLength = getWidth() - (getTranslateX() + currentSeatBitmapWidth);
                } else {
                    //右侧滑动
                    moveXLength = -getTranslateX() + numberWidth + horSpacing;
                }
            }

        }

        float startYPosition = screenHeight * getMatrixScaleY() + verSpacing * getMatrixScaleY() + borderHeight;

        //处理上下滑动
        if (currentSeatBitmapHeight < getHeight()) {

            if (getTranslateY() < startYPosition) {
                moveYLength = startYPosition - getTranslateY();
            } else {
                moveYLength = -(getTranslateY() - (startYPosition));
            }

        } else {

            if (getTranslateY() < 0 && getTranslateY() + currentSeatBitmapHeight > getHeight()) {

            } else {
                //往上滑动
                if (getTranslateY() + currentSeatBitmapHeight < getHeight()) {
                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                } else {
                    moveYLength = -(getTranslateY() - (startYPosition));
                }
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        moveAnimate(start, end);
    }

    private void autoScale() {
        if (getMatrixScaleX() > 2.2) {
            zoomAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < 0.98) {
            zoomAnimate(getMatrixScaleX(), 1.0f);
        }
    }

    Handler handler = new Handler();

    public void setOnHouseClickListener(OnHouseClickListener listener) {
        this.listener = listener;
    }

    OnHouseClickListener listener;

    public interface OnHouseClickListener {
        void onHouseClick(ModelHouse house);
    }

    float[] m = new float[9];

    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[4];
    }

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float dip2Px(float value) {
        return getResources().getDisplayMetrics().density * value;
    }

    private float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseline;
    }

    private void moveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation moveAnimation = new MoveAnimation();
        valueAnimator.addUpdateListener(moveAnimation);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void zoomAnimate(float cur, float tar) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private float zoom;

    private void zoom(float zoom) {
        float z = zoom / getMatrixScaleX();
        matrix.postScale(z, z, scaleX, scaleY);
        invalidate();
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();

            move(p);
        }
    }

    class MoveEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            zoom = (Float) animation.getAnimatedValue();
            zoom(zoom);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }

    List<ModelUnit> data = new ArrayList<>();
    List<ModelUnit> dataChecked = new ArrayList<>();

    public void setData(boolean isInvertedRow, int rowMax, int columnMax, ArrayList<String> floors, List<ModelUnit> units, List<ModelUnit> unitsChecked) {
        this.isInvertedRow = isInvertedRow;
        this.row = rowMax;
        this.column = columnMax;
        this.lineNumbers = floors;

        dataChecked = unitsChecked;

        if (data != null) {
            int columnAll = 0;
            data = new ArrayList<>(units);
            for (int unitIndex = 0; unitIndex < data.size(); unitIndex++) {
                ModelUnit unit = data.get(unitIndex);
                columnAll += unit.column + 1;
            }
            if (columnAll != 0) {
                columnAll--;
            }
            if (column < columnAll) {
                column = columnAll;
            }
        }
        init();
        invalidate();
    }

    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            isScaling = true;
            float scaleFactor = detector.getScaleFactor();
            if (getMatrixScaleY() * scaleFactor > 3) {
                scaleFactor = 3 / getMatrixScaleY();
            }
            if (firstScale) {
                scaleX = detector.getCurrentSpanX();
                scaleY = detector.getCurrentSpanY();
                firstScale = false;
            }

            if (getMatrixScaleY() * scaleFactor < 0.5) {
                scaleFactor = 0.5f / getMatrixScaleY();
            }
            matrix.postScale(scaleFactor, scaleFactor, scaleX, scaleY);
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            firstScale = true;
        }
    });

    //   滑动事件 ，后续处理单机事件
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            isOnClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();

            //    因为默认居中，有一个左边距，需要计算在内
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    int minTempX = (int) ((leftOffset + j * seatWidth + j * horSpacing) * getMatrixScaleX() + getTranslateX());
                    int maxTempX = (int) (minTempX + seatWidth * getMatrixScaleX());

                    int minTempY = (int) ((i * seatHeight + i * verSpacing) * getMatrixScaleY() + getTranslateY());
                    int maxTempY = (int) (minTempY + seatHeight * getMatrixScaleY());

                    if (x >= minTempX && x <= maxTempX && y >= minTempY && y <= maxTempY) {

                        ModelHouse house = getHouse(i, j);

                        if (house != null && (STATUS_AVAILABLE + "").equals(house.state)
//                                && (house.state == STATUS_AVAILABLE || house.state == STATUS_SOLD_CAN || house.state == STATUS_SOLD_WAIT)
                                ) {
                            if (houseSelected == null) {
                                houseSelected = house;
                            } else {
                                if (houseSelected.id.equals(house.id)) {
                                    houseSelected = null;
                                } else {
                                    houseSelected = house;
                                }
                            }
                        }
                        if (listener != null) {
                            listener.onHouseClick(houseSelected);
                        }

                        isNeedDrawSeatBitmap = true;
                        isUpdateOverview = true;
                        float currentScaleY = getMatrixScaleY();

//                        if (currentScaleY < 1.7) {
//                            scaleX = x;
//                            scaleY = y;
//                            zoomAnimate(currentScaleY, 1.9f);
//                        }

                        invalidate();
                        break;
                    }
                }
            }

            return super.onSingleTapConfirmed(e);
        }
    });

}
