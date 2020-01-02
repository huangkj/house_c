package com.easylife.house.customer.ui.houses.housesdetail.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PagerTitleAdapter;
import com.easylife.house.customer.bean.HousesToPagerImgBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.PageTitleBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.ui.houses.housesdetail.pager.allimages.PagerAllImageActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.view.PinchImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 楼盘详情大图浏览
 */

public class PagerActivity extends Activity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_center_tip)
    ImageView ivCenterTip;
    @Bind(R.id.view_alpha)
    View ivViewApha;
    @Bind(R.id.tv_current)
    TextView tvCurrent;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_all)
    TextView tvAll;
    @Bind(R.id.tv_small_num)
    TextView tvSmallNum;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.recycle_title)
    RecyclerView recycleTitle;
    private LinkedList<PinchImageView> viewCache;
    private List<PageTitleBean> titleList;
    private HousesToPagerImgBean pagerImgBean;
    private int pagerImgSize;
    private List<HousesTopImgBean.ImgBean> imgBeanList;
    private PagerTitleAdapter titleAdapter;
    private int titlePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);//注册EventBus
        //获取楼盘详情传来的图片集合bean
        pagerImgBean = (HousesToPagerImgBean) getIntent().getSerializableExtra("PAGER_BEAN");
        titleList = pagerImgBean.titleList;
        imgBeanList = pagerImgBean.imgBeanList;
        pagerImgSize = pagerImgBean.pagetImgSize;

        viewCache = new LinkedList<PinchImageView>();
        initDatas();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == MessageEvent.HOUSES_DETAIL_PAGER) {
            pager.setCurrentItem((Integer) event.obj, false);
        }
    }

    private void initDatas() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleTitle.setLayoutManager(layoutManager);
        titleAdapter = new PagerTitleAdapter(R.layout.houses_pager_item, titleList);
        recycleTitle.setAdapter(titleAdapter);

        recycleTitle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                titlePosition = position;
                pager.setCurrentItem(pagerImgBean.currentMap.get(position),false);
//                pager.setCurrentItem(i, false);
                for (int j = 0; j < titleList.size(); j++) {
                    if (j == position) {
                        titleList.get(j).isChoose = true;
                    } else {
                        titleList.get(j).isChoose = false;
                    }
                }
                titleAdapter.notifyDataSetChanged();
            }
        });


        tvTotal.setText("/" + pagerImgSize);

        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pagerImgSize;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(PagerActivity.this);
                }

                final HousesTopImgBean.ImgBean bean = getTestImage(position);
                if (!TextUtils.isEmpty(bean.vrUrl)) {
                    CacheManager.initImagePagerList(getApplicationContext(),piv,bean.imgUrl.trim());
                    piv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PagerActivity.this, WebViewActivity.class)
                                    .putExtra("title", "VR").putExtra("mUrl", bean.vrUrl.trim()));
                        }
                    });
                } else if (!TextUtils.isEmpty(bean.videoUrl)) {
                    CacheManager.initImagePagerList(getApplicationContext(),piv,bean.imgUrl.trim());
                    piv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PagerActivity.this, VideoActivity.class)
                                    .putExtra("VIDEO_URL", bean.videoUrl.trim()));
                        }
                    });
                } else {
                    ivCenterTip.setVisibility(View.GONE);
                    CacheManager.initImagePagerList(getApplicationContext(),piv,bean.url.trim());
                }
                container.addView(piv);
                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                HousesTopImgBean.ImgBean bean = getTestImage(position);
                tvCurrent.setText((position + 1) + "");

                for (PageTitleBean titlebean : titleList) {
                    if (titlebean.title.equals(bean.name)) {
                        titlebean.isChoose = true;
                        tvSmallNum.setText(bean.current + "/" + titlebean.num);
                    } else {
                        titlebean.isChoose = false;
                    }
                    titleAdapter.notifyDataSetChanged();
                }

                if (pagerImgBean.allBeanList.get(titlePosition).name.equals("VR")) {
                    ivViewApha.setVisibility(View.VISIBLE);
                    ivCenterTip.setVisibility(View.VISIBLE);
                    ivCenterTip.setImageResource(R.mipmap.vr);
                } else if (pagerImgBean.allBeanList.get(titlePosition).name.equals("视频")) {
                    ivViewApha.setVisibility(View.VISIBLE);
                    ivCenterTip.setVisibility(View.VISIBLE);
                    ivCenterTip.setImageResource(R.mipmap.video_player);
                } else {
                    ivViewApha.setVisibility(View.GONE);
                    ivCenterTip.setVisibility(View.GONE);
                }

                if (position + 1 != imgBeanList.size()) {
                    //防止recycleView滑动到最后一个的时候不能向前滑
                    recycleTitle.scrollToPosition(titlePosition);
                } else {
//                    recycleTitle.scrollToPosition(imgBeanList.get(position).position);
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String name = getTestImage(position).name;
                titlePosition = getTestImage(position).position;
                if (name.equals("VR")) {
                    ivViewApha.setVisibility(View.VISIBLE);
                    ivCenterTip.setVisibility(View.VISIBLE);
                    ivCenterTip.setImageResource(R.mipmap.vr);
                } else if (name.equals("视频")) {
                    ivViewApha.setVisibility(View.VISIBLE);
                    ivCenterTip.setVisibility(View.VISIBLE);
                    ivCenterTip.setImageResource(R.mipmap.video_player);
                } else {
                    ivViewApha.setVisibility(View.GONE);
                    ivCenterTip.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pager.setCurrentItem(pagerImgBean.currentPosition, false);
        recycleTitle.scrollToPosition(pagerImgBean.currentPosition);
    }

    @OnClick({R.id.iv_back, R.id.tv_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_all:
                startActivity(new Intent(this, PagerAllImageActivity.class).putExtra("IMAGE_BEAN", pagerImgBean));
                break;
        }
    }


    public HousesTopImgBean.ImgBean getTestImage(int i) {
        if (i >= 0 && i < imgBeanList.size()) {
            return imgBeanList.get(i);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}