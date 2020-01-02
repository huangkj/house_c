package com.easylife.house.customer.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.TopMessageBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class TopMessageListAdapter extends BaseQuickAdapter<TopMessageBean.ListBean, BaseViewHolder> {
    public static final int ONE_IMG = 1;
    public static final int NO_IMG = 2;
    public static final int MULTI_IMG = 3;

    public TopMessageListAdapter() {
        super(null);
        setMultiTypeDelegate(new MultiTypeDelegate<TopMessageBean.ListBean>() {
            @Override
            protected int getItemType(TopMessageBean.ListBean entity) {
                List<String> pictureUrl = entity.getPictureUrl();
                if (pictureUrl != null && pictureUrl.size() > 0) {

                    if (pictureUrl.size() >= 3) {
                        return MULTI_IMG;
                    } else {
                        return ONE_IMG;
                    }
                } else {
                    return NO_IMG;
                }

            }
        });
        getMultiTypeDelegate()
                .registerItemType(ONE_IMG, R.layout.message_one_img_item)
                .registerItemType(NO_IMG, R.layout.message_no_imgs_item)
                .registerItemType(MULTI_IMG, R.layout.message_imgs_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TopMessageBean.ListBean listBean) {
        baseViewHolder.setText(R.id.tvMessageTitle, listBean.getTitle());
        baseViewHolder.setText(R.id.tvDateMessage,
                TimeUtils.millis2String(Long.parseLong(listBean.getCreateTime() + "000"), new SimpleDateFormat("yyyy-MM-dd")));
        baseViewHolder.setText(R.id.tvReadCountMessage, "浏览: " + listBean.getCount());
        switch (baseViewHolder.getItemViewType()) {
            case ONE_IMG:
                Glide.with(mContext).load(listBean.getPictureUrl().get(0)).into(((ImageView) baseViewHolder.getView(R.id.ivImgMessage)));
                break;

            case NO_IMG:

                break;

            case MULTI_IMG:
                Glide.with(mContext).load(listBean.getPictureUrl().get(0)).into(((ImageView) baseViewHolder.getView(R.id.ivImg1Message)));
                Glide.with(mContext).load(listBean.getPictureUrl().get(1)).into(((ImageView) baseViewHolder.getView(R.id.ivImg2Message)));
                Glide.with(mContext).load(listBean.getPictureUrl().get(2)).into(((ImageView) baseViewHolder.getView(R.id.ivImg3Message)));

                break;
        }
    }
}
