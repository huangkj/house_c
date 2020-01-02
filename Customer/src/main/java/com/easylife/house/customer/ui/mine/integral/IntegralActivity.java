package com.easylife.house.customer.ui.mine.integral;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.IntegralAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.IntegralBean;
import com.easylife.house.customer.bean.OriginalIntegralBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.view.stickyitemdecoration.DividerHelper;
import com.easylife.house.customer.view.stickyitemdecoration.StickyHeadContainer;
import com.easylife.house.customer.view.stickyitemdecoration.StickyItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.adapter.IntegralAdapter.TYPE_STICKY_HEAD;

public class IntegralActivity extends BaseActivity {

    @Bind(R.id.tvIntegralValue)
    TextView tvIntegralValue;
    @Bind(R.id.tvFailureDateIntegral)
    TextView tvFailureDateIntegral;
    @Bind(R.id.rcvIntegral)
    RecyclerView rcvIntegral;
    @Bind(R.id.shc)
    StickyHeadContainer container;
    private IntegralAdapter integralAdapter;
    ArrayList<IntegralBean> integralBeans = new ArrayList<>();

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_integral, null);
    }

    @Override
    protected void initView() {
        integralAdapter = new IntegralAdapter(null);
        rcvIntegral.setLayoutManager(new LinearLayoutManager(this));
        final TextView tvDateIntegralHead = (TextView) container.findViewById(R.id.tvDateIntegralHead);
        container.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
//                tvDateIntegralHead.setText(pos + "ffffffffffffffff");
                IntegralBean item = integralAdapter.getItem(pos);
                tvDateIntegralHead.setText(item.getDate());
            }
        });
        StickyItemDecoration stickyItemDecoration = new StickyItemDecoration(container, TYPE_STICKY_HEAD);
        rcvIntegral.addItemDecoration(stickyItemDecoration);
        rcvIntegral.addItemDecoration(new SpaceItemDecoration(rcvIntegral.getContext()));
        rcvIntegral.setAdapter(integralAdapter);
        mDao.pointSignIn(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                integralBeans.clear();
                OriginalIntegralBean originalIntegralBean = GsonUtils.fromJson(response, OriginalIntegralBean.class);

                if (originalIntegralBean == null) {
//                    ToastUtils.showShort("data:" + response);
                    return;
                }


                tvIntegralValue.setText(originalIntegralBean.getPoint() + "");
                if (originalIntegralBean.getPoint() <= 0 || originalIntegralBean.getExpire_point() <= 0) {
//                    tvFailureDateIntegral.setText(originalIntegralBean.getExpire_point() + "积分 有效期至");
                    tvFailureDateIntegral.setVisibility(View.GONE);
                } else {
                    tvFailureDateIntegral.setText(originalIntegralBean.getExpire_point() + "积分 有效期至" + TimeUtils.millis2String(originalIntegralBean.getExpire_time(),
                            new SimpleDateFormat("yyyy年MM月dd日")));
                    tvFailureDateIntegral.setVisibility(View.VISIBLE);
                }


                //TimeUtils.millis2String(prizeBean.getWinTime(), new SimpleDateFormat("yyyy年MM月dd日")));


                List<OriginalIntegralBean.PointDetailsBean> pointDetails = originalIntegralBean.getPoint_details();


                for (int i = 0; i < pointDetails.size(); i++) {

                    OriginalIntegralBean.PointDetailsBean pointDetailsBean = pointDetails.get(i);

                    IntegralBean integralBean = new IntegralBean();
                    integralBean.type = TYPE_STICKY_HEAD;

                    integralBean.setXh(pointDetailsBean.getXh());
                    integralBean.setDate(pointDetailsBean.getDate());
                    integralBeans.add(integralBean);

                    List<OriginalIntegralBean.PointDetailsBean.DetailBean> detail = pointDetailsBean.getDetail();


                    for (int j = 0; j < detail.size(); j++) {
                        OriginalIntegralBean.PointDetailsBean.DetailBean detailBean = detail.get(j);

                        IntegralBean integralBeanInner = new IntegralBean();

                        integralBeanInner.setBehavior_name(detailBean.getBehavior_name());
                        integralBeanInner.setState(detailBean.getState());
                        integralBeanInner.setTime(detailBean.getTime());
                        integralBeanInner.setBehavior(detailBean.getBehavior());
                        integralBeanInner.setBalance_point(detailBean.getBalance_point());
                        integralBeanInner.setPoint(detailBean.getPoint());
                        integralBeanInner.setM_id(detailBean.getM_id());
                        integralBeans.add(integralBeanInner);

                    }

                }
                integralAdapter.setNewData(integralBeans);


            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort("onFail:" + code.msg);

            }
        });
//        fakeData();
    }

    private void fakeData() {
        ArrayList<IntegralBean> integralBeans = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            IntegralBean integralBean = new IntegralBean();
            if (i % 3 == 0) {
                integralBean.type = TYPE_STICKY_HEAD;
            }
            integralBeans.add(integralBean);
        }
        integralAdapter.setNewData(integralBeans);

    }

    @Override
    protected void setActionBarDetail() {

    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        public SpaceItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
//            mDivider = a.getDrawable(0);
            mDivider = ContextCompat.getDrawable(IntegralActivity.this, R.drawable.circular_rect_gray_bg);
            a.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                DividerHelper.drawBottomAlignItem(c, mDivider, child, params);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int type = parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view));
            if (type != IntegralAdapter.TYPE_DATA && type != IntegralAdapter.TYPE_SMALL_STICKY_HEAD_WITH_DATA) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }
        }
    }


}
