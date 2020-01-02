package com.easylife.house.customer.ui.houses.housetype.housesTypeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.TypeImageBean;
import com.easylife.house.customer.view.PinchImageView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 大图浏览
 */
public class PagerDetailActivity extends Activity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_current)
    TextView tvCurrent;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.pager)
    ViewPager pager;
    private HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_detail);
        ButterKnife.bind(this);

        final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        housesTypeDetailBean = (HousesTypeBean.HouseLayoutDataBean) getIntent().getSerializableExtra("PAGER_BEAN");
        tvTotal.setText("/"+housesTypeDetailBean.houseImg.size());
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return housesTypeDetailBean.houseImg.size();
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
                    piv = new PinchImageView(PagerDetailActivity.this);
                }

                TypeImageBean houseImgBean = housesTypeDetailBean.houseImg.get(position);
                Glide.with(getApplicationContext()).load(houseImgBean.url).into(piv);

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
                tvCurrent.setText((position+1)+"");
            }
        });
    }
}
