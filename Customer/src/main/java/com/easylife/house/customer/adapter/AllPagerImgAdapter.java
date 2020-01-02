package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.PagerAllImageBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/3/23.
 * 楼盘相册
 */

public class AllPagerImgAdapter extends BaseSectionQuickAdapter<PagerAllImageBean, BaseViewHolder> {

    int position;
    public ImagerOnclickLisenear imagerOnclickLisenear;
    public void setImagerOnclickLisenear(ImagerOnclickLisenear imagerOnclickLisenear){
        this.imagerOnclickLisenear = imagerOnclickLisenear;
    }
    public interface ImagerOnclickLisenear{
        void imageClickLisenear(int position);
    }

    public AllPagerImgAdapter(int layoutResId, int sectionHeadResId, List<PagerAllImageBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder holder, PagerAllImageBean pagerAllImageBean) {
        holder.setText(R.id.tv_title,pagerAllImageBean.header+"("+pagerAllImageBean.num+")");
    }

    @Override
    public long getItemId(int position) {
        this.position = position;
        return super.getItemId(position);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final PagerAllImageBean pagerAllImageBean) {
        final HousesTopImgBean.ImgBean imgBean = pagerAllImageBean.t;
       ImageView imageView = holder.getView(R.id.iv_small);
        WindowManager wm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width/3,width/3));

        if(imgBean.vrUrl != null && !TextUtils.isEmpty(imgBean.vrUrl)){

        }else if(imgBean.videoUrl != null && !TextUtils.isEmpty(imgBean.videoUrl)){

        }

        if(imgBean.thumbnailImage != null && !TextUtils.isEmpty(imgBean.thumbnailImage))
        CacheManager.initImageClientList(mContext, imageView,imgBean.thumbnailImage.trim());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new MessageEvent(120,imgBean.position));
                imagerOnclickLisenear.imageClickLisenear((holder.getPosition()-(imgBean.position+1)));
            }
        });
    }
}
