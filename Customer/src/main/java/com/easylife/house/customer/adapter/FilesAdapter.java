package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BeanFile;
import com.easylife.house.customer.bean.PublicImage;
import com.easylife.house.customer.util.ImagePrickerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 退认筹详情和退认筹申请的文件列表
 */
public class FilesAdapter extends BaseQuickAdapter<BeanFile, BaseViewHolder> {


    public FilesAdapter() {
        super(R.layout.item_record_file, null);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final BeanFile item) {

        if ("1".equals(item.type)) {
            // pdf 文件
            helper.setGone(R.id.layFile, true);
            helper.setText(R.id.tvPdfName, item.name);
            if (TextUtils.isEmpty(item.size) || "null".equals(item.size)) {
                helper.setGone(R.id.tvPdfSize, false);
            } else {
                helper.setGone(R.id.tvPdfSize, true);
                helper.setText(R.id.tvPdfSize, item.size);
            }
        } else {
            // 图片文件
            helper.setGone(R.id.layFile, false);
            ImageView fileImage = helper.getView(R.id.fileImage);
            Glide.with(mContext).load(item.url).into(fileImage);
        }

        helper.setOnClickListener(R.id.layFile, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pdf文件预览
                ImagePrickerUtil.reviewPdf(mContext, item);
            }
        });
        helper.setOnClickListener(R.id.fileImage, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 预览图片文件
                List<PublicImage> images = new ArrayList<>();
                int count = 0;
                int index = 0;
                for (BeanFile file : getData()) {
                    if (!file.isAdd && !"1".equals(file.type)) {
                        PublicImage image = new PublicImage();
                        image.cover = file.url;

                        images.add(image);
                        if (file.equals(item)) {
                            index = count;
                        }
                        count++;
                    }
                }
                ImagePrickerUtil.reviewPhotos(mContext, images, index);

            }
        });
    }
}
