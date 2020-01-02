package com.easylife.house.customer.ui.houses.exportshop.commentlist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.UserCommentListAdapter;
import com.easylife.house.customer.bean.CommentSubmit;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 专家店铺评论列表
 * zgm
 */
public class ExportCommentListActivity extends MVPBaseActivity<ExportCommentListContract.View, ExportCommentListPresenter> implements ExportCommentListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.activity_export_comment_list)
    LinearLayout rlEmpty;
    @Bind(R.id.tv_percent)
    TextView tvPercent;
    @Bind(R.id.tv0)
    TextView tv0;
    @Bind(R.id.tvCountAll)
    TextView tvCountAll;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tvCountMiddle)
    TextView tvCountMiddle;
    @Bind(R.id.tv00)
    TextView tv00;
    @Bind(R.id.tvCountGood)
    TextView tvCountGood;
    @Bind(R.id.tv11)
    TextView tv11;
    @Bind(R.id.tvCountBad)
    TextView tvCountBad;
    private UserCommentListAdapter mAdapter;
    List<ExportUserCommentBean> commentBeanList = new ArrayList<>();
    public String brokeCode;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_export_comment_list, null);
    }

    @Override
    protected void initView() {
        brokeCode = getIntent().getStringExtra("BROKECODE");
        showLoading();
        mPresenter.requestUserCommentGood(brokeCode);//好评率汇总
        mPresenter.requestUserComment(brokeCode, "1");
        mAdapter = new UserCommentListAdapter(R.layout.export_comment_list, commentBeanList);
        mAdapter.openLoadAnimation();
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("评价列表");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestUserComment(brokeCode, "1");
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showUserCommentList(List<ExportUserCommentBean> commentBeanList) {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        this.commentBeanList = commentBeanList;
        if (page == Constants.PAGE_START) {
            mAdapter.setNewData(commentBeanList);

        } else {
            mAdapter.addData(commentBeanList);
        }

    }

    /**
     * 好评率
     *
     * @param commentBean
     */
    @Override
    public void showCommentGood(CommentSubmit commentBean) {
        tvPercent.setText(commentBean.favorableRate+"%");
        tvCountAll.setText(commentBean.allCount);
        tvCountMiddle.setText(commentBean.middleCont);
        tvCountGood.setText(commentBean.goodCount);
        tvCountBad.setText(commentBean.differenceCount);
    }

    @Override
    public void showFail(String msg) {
        CustomerUtils.showTip(this, msg);
        if (dialog.isShowing()) {
            cancelLoading();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (commentBeanList != null && commentBeanList.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.requestUserComment(brokeCode, page + "");
                } else {
                    mAdapter.loadMoreComplete();
                    CustomerUtils.showTip(ExportCommentListActivity.this, "没有更多了");
                }
            }
        }, 500);

    }

}
