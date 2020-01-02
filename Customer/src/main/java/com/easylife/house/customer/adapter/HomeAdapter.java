package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.MultipleItemBean;

import java.util.List;

/**
 * Created by zgm on 2017/3/16.
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<MultipleItemBean, BaseViewHolder> {

    public ImgeViewOnclickLisenear imgeViewOnclickLisenear;
    public List<String> collectionList;

    public interface ImgeViewOnclickLisenear {
        void collectOnclick(CheckBox imageView, HomeBean homeBean);

        void itemOnclick(HomeBean homeBean);
    }

    public void setCollectionList(List<String> collectionList) {
        this.collectionList = collectionList;
    }

    public void setImgeViewOnclickLisenear(ImgeViewOnclickLisenear imgeViewOnclickLisenear) {
        this.imgeViewOnclickLisenear = imgeViewOnclickLisenear;
    }

    public HomeAdapter(List<MultipleItemBean> data) {
        super(data);
        if (data == null || data.size() == 0) {
            addItemType(MultipleItemBean.IMG_EMPTY, R.layout.empty_view);
        } else {
            addItemType(MultipleItemBean.IMG_10, R.layout.home_item_image);
        }
    }

    /**
     * 是否显示空布局
     *
     * @param isEmpty
     */
    public void setType(boolean isEmpty) {
        if (isEmpty) {
            addItemType(MultipleItemBean.IMG_EMPTY, R.layout.empty_view);
        } else {
            addItemType(MultipleItemBean.IMG_10, R.layout.home_item_image);
        }
    }

    private HomeBean getItemData(List<HomeBean> list, int position) {
        if (list == null)
            return null;
        if (list.size() - 1 < position)
            return null;
        return list.get(position);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultipleItemBean multipleItemBean) {
        try {

            switch (holder.getItemViewType()) {
                case MultipleItemBean.IMG_10:
                    if (multipleItemBean.beanList == null || multipleItemBean.beanList.size() == 0)
                        return;
                    HomeBean homeBean = getItemData(multipleItemBean.beanList, 0);
                    HomeBean homeBean1 = getItemData(multipleItemBean.beanList, 1);
                    HomeBean homeBean2 = getItemData(multipleItemBean.beanList, 2);
                    HomeBean homeBean3 = getItemData(multipleItemBean.beanList, 3);
                    HomeBean homeBean4 = getItemData(multipleItemBean.beanList, 4);
                    HomeBean homeBean5 = getItemData(multipleItemBean.beanList, 5);
                    HomeBean homeBean6 = getItemData(multipleItemBean.beanList, 6);
                    HomeBean homeBean7 = getItemData(multipleItemBean.beanList, 7);
                    HomeBean homeBean8 = getItemData(multipleItemBean.beanList, 8);
                    HomeBean homeBean9 = getItemData(multipleItemBean.beanList, 9);

                    if (homeBean != null) {
                        holder.setText(R.id.tv_name, homeBean.devName);
                        isEmptyArea(holder, homeBean, R.id.tv_area);
                        isEmpty(holder, homeBean, R.id.tv_money);
//                        holder.setText(R.id.tv_area, (TextUtils.isEmpty(homeBean.Size) ? "---" : homeBean.Size) + "㎡");
//                            holder.setText(R.id.tv_money, "¥" + homeBean.price);
                    }

                    if (homeBean1 != null) {
                        holder.setText(R.id.tv_name2, homeBean1.devName);
                        isEmptyArea(holder, homeBean1, R.id.tv_area2);
                        isEmpty(holder, homeBean1, R.id.tv_money2);
//                        holder.setText(R.id.tv_area2, (TextUtils.isEmpty(homeBean1.Size) ? "---" : homeBean1.Size) + "㎡");
//                        holder.setText(R.id.tv_money2, "¥" + homeBean1.price);
                    }

                    if (homeBean2 != null) {
                        holder.setText(R.id.tv_name3, homeBean2.devName);
                        isEmptyArea(holder, homeBean2, R.id.tv_area3);
                        isEmpty(holder, homeBean2, R.id.tv_money3);
//                        holder.setText(R.id.tv_area3, (TextUtils.isEmpty(homeBean2.Size) ? "---" : homeBean2.Size) + "㎡");
//                        holder.setText(R.id.tv_money3, "¥" + homeBean2.price);
                    }

                    if (homeBean3 != null) {
                        holder.setText(R.id.tv_name4, homeBean3.devName);
                        isEmptyArea(holder, homeBean3, R.id.tv_area4);
                        isEmpty(holder, homeBean3, R.id.tv_money4);
//                        holder.setText(R.id.tv_area4, (TextUtils.isEmpty(homeBean3.Size) ? "---" : homeBean3.Size) + "㎡");
//                        holder.setText(R.id.tv_money4, "¥" + homeBean3.price);
                    }

                    if (homeBean4 != null) {
                        holder.setText(R.id.tv_name5, homeBean4.devName);
                        isEmptyArea(holder, homeBean4, R.id.tv_area5);
                        isEmpty(holder, homeBean4, R.id.tv_money5);
//                        holder.setText(R.id.tv_area5, (TextUtils.isEmpty(homeBean4.Size) ? "---" : homeBean4.Size) + "㎡");
//                        holder.setText(R.id.tv_money5, "¥" + homeBean4.price);
                    }

                    if (homeBean5 != null) {
                        holder.setText(R.id.tv_name6, homeBean5.devName);
                        isEmptyArea(holder, homeBean5, R.id.tv_area6);
                        isEmpty(holder, homeBean5, R.id.tv_money6);
//                        holder.setText(R.id.tv_area6, (TextUtils.isEmpty(homeBean5.Size) ? "---" : homeBean5.Size) + "㎡");
//                        holder.setText(R.id.tv_money6, "¥" + homeBean5.price);
                    }

                    if (homeBean6 != null) {
                        holder.setText(R.id.tv_name7, homeBean6.devName);
                        isEmptyArea(holder, homeBean6, R.id.tv_area7);
                        isEmpty(holder, homeBean6, R.id.tv_money7);
//                        holder.setText(R.id.tv_area7, (TextUtils.isEmpty(homeBean6.Size) ? "---" : homeBean6.Size) + "㎡");
//                        holder.setText(R.id.tv_money7, "¥" + homeBean6.price);
                    }

                    if (homeBean7 != null) {
                        holder.setText(R.id.tv_name8, homeBean7.devName);
                        isEmptyArea(holder, homeBean7, R.id.tv_area8);
                        isEmpty(holder, homeBean7, R.id.tv_money8);
//                        holder.setText(R.id.tv_area8, (TextUtils.isEmpty(homeBean7.Size) ? "---" : homeBean7.Size) + "㎡");
//                        holder.setText(R.id.tv_money8, "¥" + homeBean7.price);
                    }

                    if (homeBean8 != null) {
                        holder.setText(R.id.tv_name9, homeBean8.devName);
                        isEmptyArea(holder, homeBean8, R.id.tv_area9);
                        isEmpty(holder, homeBean8, R.id.tv_money9);
//                        holder.setText(R.id.tv_area9, (TextUtils.isEmpty(homeBean8.Size) ? "---" : homeBean8.Size) + "㎡");
//                        holder.setText(R.id.tv_money9, "¥" + homeBean8.price);
                    }

                    if (homeBean9 != null) {
                        holder.setText(R.id.tv_name10, homeBean9.devName);
                        isEmptyArea(holder, homeBean9, R.id.tv_area10);
                        isEmpty(holder, homeBean9, R.id.tv_money10);
//                        holder.setText(R.id.tv_area10, (TextUtils.isEmpty(homeBean9.Size) ? "---" : homeBean9.Size) + "㎡");
//                        holder.setText(R.id.tv_money10, "¥" + homeBean9.price);
                    }


                    if (collectionList != null) {
                        if (collectionList.contains(homeBean.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect1)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect1)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean1.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect2)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect2)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean2.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect3)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect3)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean3.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect4)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect4)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean4.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect5)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect5)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean5.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect6)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect6)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean6.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect7)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect7)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean7.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect8)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect8)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean8.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect9)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect9)).setChecked(false);
                        }

                        if (collectionList.contains(homeBean9.devId)) {
                            ((CheckBox) holder.getView(R.id.iv_collect10)).setChecked(true);
                        } else {
                            ((CheckBox) holder.getView(R.id.iv_collect10)).setChecked(false);
                        }
                    }


                    //收藏点击事件
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect1), homeBean);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect2), homeBean1);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect3), homeBean2);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect4), homeBean3);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect5), homeBean4);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect6), homeBean5);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect7), homeBean6);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect8), homeBean7);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect9), homeBean8);
                    setCheckBoxOnclick((CheckBox) holder.getView(R.id.iv_collect10), homeBean9);

                    //item图片点击事件
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv1), homeBean);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv2), homeBean1);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv3), homeBean2);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv4), homeBean3);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv5), homeBean4);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv6), homeBean5);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv7), homeBean6);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv8), homeBean7);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv9), homeBean8);
                    setImageViewOnclick((ImageView) holder.getView(R.id.iv10), homeBean9);


                    setImageView(homeBean.imgUrl, (ImageView) holder.getView(R.id.iv1));
                    setImageView(homeBean1.imgUrl, (ImageView) holder.getView(R.id.iv2));
                    setImageView(homeBean2.imgUrl, (ImageView) holder.getView(R.id.iv3));
                    setImageView(homeBean3.imgUrl, (ImageView) holder.getView(R.id.iv4));
                    setImageView(homeBean4.imgUrl, (ImageView) holder.getView(R.id.iv5));
                    setImageView(homeBean5.imgUrl, (ImageView) holder.getView(R.id.iv6));
                    setImageView(homeBean6.imgUrl, (ImageView) holder.getView(R.id.iv7));
                    setImageView(homeBean7.imgUrl, (ImageView) holder.getView(R.id.iv8));
                    setImageView(homeBean8.imgUrl, (ImageView) holder.getView(R.id.iv9));
                    setImageView(homeBean9.imgUrl, (ImageView) holder.getView(R.id.iv10));
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 价格判断
     *
     * @param holder
     * @param homeBean
     * @param resId
     */
    public void isEmpty(BaseViewHolder holder, HomeBean homeBean, int resId) {
        if (!TextUtils.isEmpty(homeBean.price) && !"0".equals(homeBean.price)) {
            holder.setVisible(resId, true);
            holder.setText(resId, "¥" + homeBean.price + "元/㎡");
        } else {
//            holder.setVisible(resId,false);
            holder.getView(resId).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 面积是否为空
     *
     * @param holder
     * @param homeBean
     * @param resId
     */
    public void isEmptyArea(BaseViewHolder holder, HomeBean homeBean, int resId) {
        if (!TextUtils.isEmpty(homeBean.Size) && !"0".equals(homeBean.Size)) {
            holder.setVisible(resId, true);
            holder.setText(resId, (homeBean.Size + "㎡"));
        } else {
//            holder.setVisible(resId,false);
            holder.getView(resId).setVisibility(View.INVISIBLE);
        }
    }

    public void setImageView(String url, ImageView resId) {
        Glide.with(mContext).load(url).into(resId);
    }

    public void setImageViewOnclick(final ImageView imageView, final HomeBean homeBean) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgeViewOnclickLisenear.itemOnclick(homeBean);
            }
        });
    }

    public void setCheckBoxOnclick(final CheckBox imageView, final HomeBean homeBean) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgeViewOnclickLisenear.collectOnclick(imageView, homeBean);
            }
        });
    }

}
