package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.util.DateUtil;

import java.util.List;

/**
 * Created by Mars on 2017/4/1 17:04.
 * 描述：楼盘订阅
 */

public class DevSubscriptionsAdapter extends BaseQuickAdapter<MsgDevSub, BaseViewHolder> {
    private Activity activity;

    public DevSubscriptionsAdapter(int layoutResId, List<MsgDevSub> data) {
        super(layoutResId, data);
    }

    public DevSubscriptionsAdapter(Activity activity, int layoutResId, List<MsgDevSub> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final MsgDevSub messageBean) {
        baseViewHolder.setText(R.id.tvTitle,messageBean.title);
        baseViewHolder.setText(R.id.tvText, messageBean.text);
        baseViewHolder.setText(R.id.tvDate,
                DateUtil.phpToDate(messageBean.createTime, "yyyy-MM-dd HH:mm:ss"));
        ImageView img = baseViewHolder.getView(R.id.imgCover);
        CacheManager.initImageClientList(mContext, img, messageBean.logoUrl);

        baseViewHolder.getView(R.id.btnDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBean.devId != null)
                    HouseDetailActivity.startActivity(activity, messageBean.devId, false, 0);
            }
        });
    }
}
