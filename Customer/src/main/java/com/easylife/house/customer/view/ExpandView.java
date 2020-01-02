package com.easylife.house.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.util.CustomerUtils;

import butterknife.Bind;

/**
 * 展开收起
 * Created by zgm on 2017/10/18/018.
 */

public class ExpandView extends FrameLayout {

    @Bind(R.id.tv_dvp_value)
    TextView tvDvpValue;
    @Bind(R.id.tv_open_value)
    TextView tvOpenValue;
    @Bind(R.id.tv_complete_value)
    TextView tvCompleteValue;
    @Bind(R.id.tv_agree_value)
    TextView tvAgreeValue;
    @Bind(R.id.tv_type_value)
    TextView tvTypeValue;
    @Bind(R.id.tv_build_value)
    TextView tvBuildValue;
    @Bind(R.id.tv_year_value)
    TextView tvYearValue;
    @Bind(R.id.tv_standard_value)
    TextView tvStandardValue;
    @Bind(R.id.tv_rate_value)
    TextView tvRateValue;
    @Bind(R.id.tv_green_value)
    TextView tvGreenValue;
    @Bind(R.id.tv_car_value)
    TextView tvCarValue;
    @Bind(R.id.tv_money_value)
    TextView tvMoneyValue;
    @Bind(R.id.tv_company_value)
    TextView tvCompanyValue;
    @Bind(R.id.tv_address_value)
    TextView tvAddressValue;
    @Bind(R.id.tv_project_address)
    TextView tvProjectAddress;
    @Bind(R.id.tv_project_address_value)
    TextView tvProjectAddressValue;


    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;


    public ExpandView(Context context) {
        this(context, null);
    }

    public ExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initExpandView();
    }

    private void initExpandView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_expand, this, true);
        tvDvpValue = (TextView) findViewById(R.id.tv_dvp_value);
        tvOpenValue = (TextView) findViewById(R.id.tv_open_value);
        tvCompleteValue = (TextView) findViewById(R.id.tv_complete_value);
        tvAgreeValue = (TextView) findViewById(R.id.tv_agree_value);
        tvTypeValue = (TextView) findViewById(R.id.tv_type_value);
        tvBuildValue = (TextView) findViewById(R.id.tv_build_value);
        tvYearValue = (TextView) findViewById(R.id.tv_year_value);
        tvStandardValue = (TextView) findViewById(R.id.tv_standard_value);
        tvRateValue = (TextView) findViewById(R.id.tv_rate_value);
        tvGreenValue = (TextView) findViewById(R.id.tv_green_value);
        tvCarValue = (TextView) findViewById(R.id.tv_car_value);
        tvMoneyValue = (TextView) findViewById(R.id.tv_money_value);
        tvCompanyValue = (TextView) findViewById(R.id.tv_company_value);
        tvAddressValue = (TextView) findViewById(R.id.tv_address_value);
        tvProjectAddress = (TextView) findViewById(R.id.tv_project_address);
        tvProjectAddressValue = (TextView) findViewById(R.id.tv_project_address_value);

        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }
        });

    }

    /**
     * 填充需要设置的内容
     */
    public void setMoreDatas(HousesDetailBaseBean baseBean) {
        try {
            if (baseBean != null) {
                //售楼处地址
                CustomerUtils.setEmptyTv(tvAddressValue, baseBean.addressDistrict + " " + baseBean.addressTown + " " + baseBean.addressDetail, "", "暂无数据");
                //项目地址
                CustomerUtils.setEmptyTv(tvProjectAddressValue, baseBean.saleAddressDistrict + " " + baseBean.saleAddressTown + " " + baseBean.saleAddressDetail, "", "暂无数据");
                CustomerUtils.setEmptyTv(tvAgreeValue, baseBean.salesLicence, "", "暂无数据");//预售许可证
                CustomerUtils.setEmptyTv(tvBuildValue, baseBean.buildType, "", "暂无数据");//建筑类型
                CustomerUtils.setEmptyTv(tvCarValue, baseBean.parkRatio, "", "暂无数据");//车位配比
                CustomerUtils.setEmptyTv(tvCompanyValue, baseBean.propertyCompany, "", "暂无数据");//物业公司
                if (baseBean.liveTime == 0) {
                    tvCompleteValue.setText("暂无数据");
                } else {
                    tvCompleteValue.setText(CustomerUtils.dateTransSdf(baseBean.liveTime));//预计交房
                }

                if (baseBean.openTime == 0) {
                    tvOpenValue.setText("暂无数据");
                } else {
                    tvOpenValue.setText(CustomerUtils.dateTransSdf(baseBean.openTime));//最新开盘
                }
                CustomerUtils.setEmptyTv(tvDvpValue, baseBean.companyName, "", "暂无数据");//开发商
                CustomerUtils.setEmptyTv(tvGreenValue, baseBean.greenRatio, "%", "暂无数据");//绿化率
                CustomerUtils.setEmptyTv(tvMoneyValue, baseBean.propertyFee, "元/㎡/月", "暂无数据");//物业费
                CustomerUtils.setEmptyTv(tvRateValue, baseBean.plotRatio, "", "暂无数据");//容积率
                CustomerUtils.setEmptyTv(tvStandardValue, baseBean.decorateLevel, "", "暂无数据");//装修标准
                CustomerUtils.setEmptyTv(tvTypeValue, baseBean.propertyType, "", "暂无数据");//物业类型
                CustomerUtils.setEmptyTv(tvYearValue, baseBean.expires, "", "暂无数据");//产权年限
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);
        }
    }

    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setContentView() {
        View view = null;
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_expand, null);
        removeAllViews();
        addView(view);
    }

}
