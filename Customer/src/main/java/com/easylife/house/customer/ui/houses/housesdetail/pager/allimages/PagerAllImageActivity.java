package com.easylife.house.customer.ui.houses.housesdetail.pager.allimages;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.AllPagerImgAdapter;
import com.easylife.house.customer.bean.HousesToPagerImgBean;
import com.easylife.house.customer.bean.PagerAllImageBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.HOUSES_DETAIL_PAGER;

/**
 * 全部图片显示
 */
public class PagerAllImageActivity extends MVPBaseActivity<PagerAllImageContract.View, PagerAllImagePresenter> {

    @Bind(R.id.recycle_all)
    RecyclerView recycleAll;
    private HousesToPagerImgBean pagerImgBean;
    private List<PagerAllImageBean> list = new ArrayList<>();
    private AllPagerImgAdapter mAdapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_pager_all_image, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        pagerImgBean = (HousesToPagerImgBean) getIntent().getSerializableExtra("IMAGE_BEAN");
        for (int i = 0; i < pagerImgBean.allBeanList.size(); i++) {
            if(pagerImgBean.allBeanList.get(i).img.size() != 0){
                PagerAllImageBean allBean = new PagerAllImageBean(true,pagerImgBean.allBeanList.get(i).name,pagerImgBean.allBeanList.get(i).img.size());
                list.add(allBean);
                for (int j = 0; j < pagerImgBean.allBeanList.get(i).img.size(); j++) {
                    list.add(new PagerAllImageBean(pagerImgBean.allBeanList.get(i).img.get(j)));
                }
            }
        }

        mAdapter = new AllPagerImgAdapter(R.layout.houses_all_image_item,R.layout.houses_all_image_item_title,list);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recycleAll.setLayoutManager(gridLayoutManager);
        recycleAll.setAdapter(mAdapter);

//        recycleAll.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
//                EventBus.getDefault().post(new MessageEvent(HOUSES_DETAIL_PAGER,position));
//                finish();
//            }
//        });

        mAdapter.setImagerOnclickLisenear(new AllPagerImgAdapter.ImagerOnclickLisenear() {
            @Override
            public void imageClickLisenear(int position) {
                EventBus.getDefault().post(new MessageEvent(HOUSES_DETAIL_PAGER,position));
                finish();
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("楼盘相册");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }
}
