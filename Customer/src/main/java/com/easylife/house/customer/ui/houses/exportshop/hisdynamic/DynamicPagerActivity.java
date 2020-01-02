package com.easylife.house.customer.ui.houses.exportshop.hisdynamic;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.DynamicUrlPagerBean;
import com.easylife.house.customer.view.PinchImageView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TA的动态列表图片查看
 */

public class DynamicPagerActivity extends Activity {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    private DynamicUrlPagerBean urlBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_pager);
        ButterKnife.bind(this);

        final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();
//        final DisplayImageOptions thumbOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).build();
//        final DisplayImageOptions originOptions = new DisplayImageOptions.Builder().build();

        urlBean = (DynamicUrlPagerBean) getIntent().getSerializableExtra("ITEM_BEAN");

        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urlBean.urlList.size();
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
                    piv = new PinchImageView(DynamicPagerActivity.this);
                }
//                ImageSource houseImg = Global.getTestImage(position);
               String url  = urlBean.urlList.get(position);
                Glide.with(getApplicationContext()).load(url).into(piv);

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
                tvTotal.setText((position+1)+"/"+ urlBean.urlList.size());
            }
        });

        pager.setCurrentItem(urlBean.position);

    }
}