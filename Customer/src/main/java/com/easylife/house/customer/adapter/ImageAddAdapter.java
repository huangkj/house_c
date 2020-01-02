package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.ui.pub.PhotoViewActivity;

/**
 * 九宫格图片列表，第一项可是添加按钮
 *
 * @author Mars
 */
public class ImageAddAdapter extends BaseQuickAdapter<ImageBean, BaseViewHolder> {
    private Activity activity;
    public static int COUNT_RECORD_IMAGE_MAX = 20;

    public ImageAddAdapter(Activity activity) {
        super(R.layout.list_item_image_refund, null);
        this.activity = activity;
        notifyAdapterDataSetChanged();
    }

    /**
     * 更新数据时同步更新最后一个添加按钮
     */
    public void notifyAdapterDataSetChanged() {
        // 处理最后的添加文件按钮
        if (getItemCount() == 0) {
            addData(new ImageBean());
        } else {
            ImageBean itemLast = getItem(getItemCount() - 1);
            if (itemLast != null) {
                if (TextUtils.isEmpty(itemLast.pic)) {
                    // 最后一项已经是添加按钮,不做处理
                } else {
                    // 最后一项不是添加按钮，判断文件数量然后确定是否要显示添加按钮
                    if (getItemCount() == COUNT_RECORD_IMAGE_MAX) {
                        // 已经添加到最大数量，不再显示添加按钮
                    } else {
                        // 不足最大数量，仍可以添加文件
                        addData(new ImageBean());
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private IResult result;

    @Override
    protected void convert(final BaseViewHolder helper, final ImageBean item) {
        if (TextUtils.isEmpty(item.pic)) {
            helper.setImageResource(R.id.iv_img, R.mipmap.image_add_refund);
            helper.setGone(R.id.imgDelete, false);
        } else {
            Glide.with(activity).load(item.pic).into((ImageView) helper.getView(R.id.iv_img));
            helper.setGone(R.id.imgDelete, true);
        }
        helper.setOnClickListener(R.id.iv_img, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(item.pic)) {
                    result.add();
                } else {
                    PhotoViewActivity.startActivity(activity, getData(), helper.getPosition());
                }
            }
        });
        helper.setOnClickListener(R.id.imgDelete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result != null)
                    result.del(helper.getPosition());
            }
        });
    }

    public interface IResult {
        void add();

        void del(int position);
    }

    public void setAddForResult(IResult result) {
        this.result = result;
    }

}
