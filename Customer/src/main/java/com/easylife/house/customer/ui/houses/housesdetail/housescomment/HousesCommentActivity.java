package com.easylife.house.customer.ui.houses.housesdetail.housescomment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.CommentListAdapter;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.CommentUrlBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.houses.housedetailsimple.HousesDetailSimpleFragment.BASE_BEAN;

/**
 * 评价列表页
 * zgm
 */
public class HousesCommentActivity extends MVPBaseActivity<HousesCommentContract.View, HousesCommentPresenter> implements
        HousesCommentContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    //    @Bind(R.id.imgEmpty)
//    ImageView imgEmpty;
    @Bind(R.id.activity_houses_comment)
    LinearLayout rlComment;
    @Bind(R.id.tv_comment_btn)
    TextView tvCommentBtn;

    private CommentListAdapter mAdapter;
    private String projectid;
    List<CommentListBean.ReviewsBean> reviews = new ArrayList<>();
    private HousesDetailBaseBean baseBean;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_houses_comment, null);
    }

    @Override
    protected void initView() {
        projectid = getIntent().getStringExtra("PROJECTID");
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra(BASE_BEAN);
        showLoading();
        mPresenter.loadMoreData(projectid, page + "");
        mAdapter = new CommentListAdapter(R.layout.houses_comment_item_adapter, null);
        mAdapter.openLoadAnimation();
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        //设置空数据view
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlComment, false);
        mAdapter.setEmptyView(emptyView);

        mAdapter.setCommentImgOnclickListenear(new CommentListAdapter.CommentImgOnclickListenear() {
            @Override
            public void setCommentOnclick(List<CommentListBean.ReviewsBean.ReviewimgBean> reviewimg, int position) {
                CommentUrlBean urlBean = new CommentUrlBean();
                urlBean.beanList = reviewimg;
                urlBean.position = position;
                startActivity(new Intent(HousesCommentActivity.this, CommentPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                Log.e("postion", position + "");
            }

            @Override
            public void setJumpCommentListClick() {

            }
        });
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
        mPresenter.loadMoreData(projectid, page + "");
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (reviews != null && reviews.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.loadMoreData(projectid, page + "");
                } else {
                    CustomerUtils.showTip(HousesCommentActivity.this, "没有更多了");
                    mAdapter.loadMoreEnd();
                }
            }
        }, 500);
    }

    @Override
    public void showMoreData(CommentListBean commentListBean) {
        cancelLoading();
        this.reviews = commentListBean.reviews;
        if (0 != reviews.size()) {
            if (page == Constants.PAGE_START) {
                mAdapter.setNewData(reviews);
            } else {
                mAdapter.addData(reviews);
                mAdapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
    }

    @OnClick(R.id.tv_comment_btn)
    public void onClick(View view) {

        switch (view.getId()) {
            //我要评价
            case R.id.tv_comment_btn:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 12);
                    return;
                }
                if(TextUtils.isEmpty(projectid)){
                    if (baseBean != null){
                        startActivityForResult(new Intent(this, CommentActivity.class).putExtra("projectId", baseBean.estateProjectId), 10);
                    }
                }else {
                    startActivityForResult(new Intent(this, CommentActivity.class).putExtra("projectId", projectid), 10);
                }

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 10){
            if (baseBean != null) {
                mPresenter.loadMoreData(projectid, page + "");
            }
        }
    }
}
