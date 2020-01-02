package com.easylife.house.customer.ui.houses.housesdetail.baninfo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BuildingInfoBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.view.PinchImageView;
import com.easylife.house.customer.view.card.ShadowTransformer;

import java.util.List;

import butterknife.Bind;

/**
 * 楼栋信息页面
 */
public class BuildingActivity extends MVPBaseActivity<BuildingContract.View, BuildingPresenter> implements BuildingContract.View {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.iv_top)
    PinchImageView ivTop;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    private ShadowTransformer mCardShadowTransformer;
    private CardPagerAdapter mCardAdapter;
    private String devId;
    private String houses_url;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_ban, null);
    }

    @Override
    protected void initView() {
        devId = getIntent().getStringExtra("DEV_ID");
        houses_url = getIntent().getStringExtra("HOUSES_URL");
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = ivTop.getLayoutParams();
        layoutParams.height = height/2;
        ivTop.setLayoutParams(layoutParams);
        CacheManager.initCenterCropImage(this,ivTop, houses_url);
        mPresenter.requestBanInfo(devId);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("楼栋信息");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        mPresenter.requestBanInfo(devId);
        CacheManager.initCenterCropImage(this,ivTop, houses_url);
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showBanInfo(List<BuildingInfoBean> mData) {
        if(mData == null || mData.size() == 0){
            llEmpty.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.GONE);
        }else {
            llEmpty.setVisibility(View.GONE);
            llContent.setVisibility(View.VISIBLE);
        }
        mCardAdapter = new CardPagerAdapter(mData);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mCardShadowTransformer.enableScaling(true);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void showFail(String code) {
        if("9001".equals(code)){
            llEmpty.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.GONE);
        }else {
            llEmpty.setVisibility(View.GONE);
            llContent.setVisibility(View.VISIBLE);
        }
    }
}
