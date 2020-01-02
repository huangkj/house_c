package com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportCommentBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.photoview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 专家评价
 */
public class ExportCommentActivity extends MVPBaseActivity<ExportCommentContract.View, ExportCommentPresenter> implements ExportCommentContract.View {

    @Bind(R.id.iv_head)
    RoundedImageView ivHead;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_good)
    TextView tvGood;
    @Bind(R.id.tv_good_value)
    TextView tvGoodValue;
    @Bind(R.id.tv_integral)
    TextView tvIntegral;
    @Bind(R.id.tv_integral_value)
    TextView tvIntegralValue;
    @Bind(R.id.tv_shop_level)
    TextView tvShopLevel;
    @Bind(R.id.rb_shop_level)
    RatingBar rbShopLevel;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_time_value)
    TextView tvTimeValue;
    @Bind(R.id.export_line)
    View exportLine;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.rb_comment)
    MaterialRatingBar rbComment;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.export_comment_flowgroup)
    FlowViewGroup exportCommentFlowgroup;
    @Bind(R.id.et_comment_content)
    EditText etCommentContent;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    public String chooseValue;
    private ExportShopBaseBean baseBean;

    private String[] str = new String[]{"服务热情", "专业", "素质高", "办事快", "非常满意"};
    private List<Integer> choseList = new ArrayList<>();
    private String brokeCode;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_export_comment, null);
    }

    public static void startActivity(Activity activity, String brokeCode, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ExportCommentActivity.class)
                        .putExtra("brokerCode", brokeCode)
                , requestCode);
    }

    public static void startActivity(Activity activity, String brokeCode, ExportShopBaseBean baseBean, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ExportCommentActivity.class)
                        .putExtra("brokerCode", brokeCode)
                        .putExtra("SHOP_BEAN", baseBean)
                , requestCode);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        baseBean = (ExportShopBaseBean) getIntent().getSerializableExtra("SHOP_BEAN");
        brokeCode = getIntent().getStringExtra("brokerCode");
        if (baseBean != null) {
            tvUsername.setText(baseBean.name);
            CacheManager.initImageUserHeader(this, ivHead, baseBean.brokerImg);
            tvGoodValue.setText(baseBean.praiseRate + "%");
            tvIntegralValue.setText(baseBean.integral + "分");
            tvTimeValue.setText(CustomerUtils.dateTransSdf(baseBean.time));
            if (!TextUtils.isEmpty(baseBean.level)) {
                rbShopLevel.setRating(Float.parseFloat(baseBean.level));
            }
        }

        for (int i = 0; i < str.length; i++) {
            final CheckBox checkBox = (CheckBox) LayoutInflater.from(this).inflate(R.layout.export_flow_comment, exportCommentFlowgroup, false);
            checkBox.setText(str[i]);
            final int finalI = i;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < exportCommentFlowgroup.getChildCount(); j++) {
                        CheckBox checkBoxChoose = (CheckBox) exportCommentFlowgroup.getChildAt(j);
                        if (checkBoxChoose.isChecked()) {
                            checkBoxChoose.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            checkBoxChoose.setTextColor(getResources().getColor(R.color.gradient_end));
                        }
                    }

                    if (checkBox.isChecked()) {
                        choseList.add(finalI);
                    } else {
                        if (choseList.contains(finalI)) {
                            Integer index = choseList.indexOf(finalI);
                            if (index != -1)
                                choseList.remove(index);
                        }
                    }
                }
            });


            exportCommentFlowgroup.addView(checkBox);
        }

        mPresenter.requestShopBase(brokeCode);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("给TA评价");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    /**
     * 专家店铺基础信息
     */
    @Override
    public void showShopBaseData(ExportShopBaseBean baseBean) {
        this.baseBean = baseBean;
        if (!TextUtils.isEmpty(baseBean.level)) {
            rbShopLevel.setRating(Float.parseFloat(baseBean.level));
        }
        CacheManager.initImageUserHeader(this, ivHead, baseBean.brokerImg);
        tvUsername.setText(baseBean.name);
        tvGoodValue.setText(baseBean.praiseRate + "%");
        tvIntegralValue.setText(baseBean.integral + "分");
        tvTimeValue.setText(CustomerUtils.dateTransSdf(baseBean.time));
    }

    @Override
    public void showCommitSuccess() {
        cancelLoading();
        CustomerUtils.showTip(this, "评价成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showCommitFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this, msg);
    }

    @OnClick({R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:

                if (rbComment.getRating() == 0.0) {
                    CustomerUtils.showTip(this, "请添加评分");
                    return;
                }

                if (choseList == null || choseList.size() == 0) {
                    CustomerUtils.showTip(this, "请添加标签评价");
                    return;
                }

                if (TextUtils.isEmpty(etCommentContent.getText().toString().trim())) {
                    CustomerUtils.showTip(this, "评论不能为空");
                    return;
                }

                if (etCommentContent.getText().length() < 15) {
                    CustomerUtils.showTip(this, "评论内容不能少于15字");
                    return;
                }

                ExportCommentBean commentBean = new ExportCommentBean();
                for (int value : choseList) {
                    switch (value) {
                        case 0:
                            commentBean.scoreService = "1";
                            break;
                        case 1:
                            commentBean.scorePro = "1";
                            break;
                        case 2:
                            commentBean.scoreQuality = "1";
                            break;
                        case 3:
                            commentBean.scoreEfficiency = "1";
                            break;
                        case 4:
                            commentBean.scoreSatisfied = "1";
                            break;
                    }
                }

                try {
                    commentBean.brokerCode = brokeCode;
                    commentBean.userCode = dao.getLoginCache().userCode;
                    commentBean.token = dao.getLoginCache().token;
                    commentBean.score = rbComment.getRating() + "";
                    commentBean.customerComent = etCommentContent.getText().toString().trim();
                    showLoading();
                    mPresenter.commitComment(commentBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
