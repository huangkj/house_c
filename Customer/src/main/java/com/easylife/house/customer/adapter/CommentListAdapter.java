package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 */

public class CommentListAdapter extends BaseQuickAdapter<CommentListBean.ReviewsBean, BaseViewHolder> {

    public CommentImgOnclickListenear commentImgOnclickListenear;

    public void setCommentImgOnclickListenear(CommentImgOnclickListenear commentImgOnclickListenear) {
        this.commentImgOnclickListenear = commentImgOnclickListenear;
    }

    public interface CommentImgOnclickListenear {
        void setCommentOnclick(List<CommentListBean.ReviewsBean.ReviewimgBean> reviewimg, int position);

        void setJumpCommentListClick();
    }

    public CommentListAdapter(int layoutResId, List<CommentListBean.ReviewsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentListBean.ReviewsBean commentListBean) {

        try {

            holder.setText(R.id.tv_username, commentListBean.userName);
            holder.setText(R.id.tv_job, "1".equals(commentListBean.userType) ? "(小编)" : "(购房者)");
            holder.setText(R.id.tv_comment_content, commentListBean.content);
            holder.setText(R.id.tv_comment_time, CustomerUtils.dateTransSdfMinutes(commentListBean.createTime));
            if(!TextUtils.isEmpty(commentListBean.aVGscores)){
                ((RatingBar) holder.getView(R.id.houses_content_star)).setRating(Float.parseFloat(commentListBean.aVGscores));
            }
            if ("1".equals(commentListBean.userType)) {
                holder.setImageResource(R.id.iv_circle, R.mipmap.user_head_operation);
            } else {
                CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.iv_circle), commentListBean.headImg);
            }

            if(getData() != null && getData().size()-1 == holder.getPosition()){
                holder.setVisible(R.id.line,false);
            }else {
                holder.setVisible(R.id.line,true);
            }


            holder.getView(R.id.ll_comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentImgOnclickListenear.setJumpCommentListClick();
                }
            });
            if (commentListBean.reviewimg != null && commentListBean.reviewimg.size() > 0) {
                holder.getView(R.id.rl_comment_img).setVisibility(View.VISIBLE);
                final List<CommentListBean.ReviewsBean.ReviewimgBean> reviewimg = commentListBean.reviewimg;
                if (commentListBean.reviewimg.size() > 4) {
                    holder.setVisible(R.id.tv_comment_total, true);
                    holder.setText(R.id.tv_comment_total, commentListBean.reviewimg.size()+"");
                } else {
                    holder.setVisible(R.id.tv_comment_total, false);
                }

                if (commentListBean.reviewimg.size() > 0) {
                    holder.setVisible(R.id.rl_comment_img, true);
                    switch (commentListBean.reviewimg.size()) {
                        case 1:
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment1), reviewimg.get(0).url);
                            holder.getView(R.id.iv_comment1).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment2).setVisibility(View.GONE);
                            holder.getView(R.id.iv_comment3).setVisibility(View.GONE);
                            holder.getView(R.id.iv_comment4).setVisibility(View.GONE);
                            break;
                        case 2:
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment1), reviewimg.get(0).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment2), reviewimg.get(1).url);
                            holder.getView(R.id.iv_comment1).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment2).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment3).setVisibility(View.GONE);
                            holder.getView(R.id.iv_comment4).setVisibility(View.GONE);
                            break;
                        case 3:
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment1), reviewimg.get(0).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment2), reviewimg.get(1).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment3), reviewimg.get(2).url);
                            holder.getView(R.id.iv_comment1).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment2).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment3).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment4).setVisibility(View.GONE);
                            break;
                        default:
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment1), reviewimg.get(0).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment2), reviewimg.get(1).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment3), reviewimg.get(2).url);
                            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_comment4), reviewimg.get(3).url);
                            holder.getView(R.id.iv_comment1).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment2).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment3).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_comment4).setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    holder.setVisible(R.id.rl_comment_img, false);
                }


                holder.getView(R.id.rl_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setJumpCommentListClick();
                    }
                });

                holder.getView(R.id.tv_comment_content).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setJumpCommentListClick();
                    }
                });

                (holder.getView(R.id.iv_comment1)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setCommentOnclick(reviewimg, 0);
                    }
                });

                (holder.getView(R.id.iv_comment2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setCommentOnclick(reviewimg, 1);
                    }
                });

                (holder.getView(R.id.iv_comment3)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setCommentOnclick(reviewimg, 2);
                    }
                });

                (holder.getView(R.id.iv_comment4)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentImgOnclickListenear.setCommentOnclick(reviewimg, 3);
                    }
                });
            } else {
                holder.getView(R.id.rl_comment_img).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
