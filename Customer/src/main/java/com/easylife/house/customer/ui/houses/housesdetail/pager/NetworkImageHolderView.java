package com.easylife.house.customer.ui.houses.housesdetail.pager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;

/**
 * Created by zgm on 2017/3/23.
 */

public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = ((ImageView) View.inflate(context, R.layout.banner_item, null));
        imageView.setImageResource(R.mipmap.iv_house_banner_def);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.drawable.muti_default_pic);
        //Glide 加载图片简单用法
        Glide.with(context).load(data).placeholder(R.mipmap.iv_house_banner_def).error(R.mipmap.iv_house_banner_def).
                into(imageView);
    }
}
