package com.easylife.house.customer.ui.mine.prize;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.bean.MyPrizeBean;
import com.easylife.house.customer.bean.PrizeBeanDetail;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.AddressManagerActivity;
import com.easylife.house.customer.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * - @Description:  奖品详情
 * - @Author:  hkj
 * - @Time:  2018/9/19 16:57
 */
public class PrizeDetailActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.ivPrizeDetail)
    ImageView ivPrizeDetail;
    @Bind(R.id.tvTitlePrizeDetail)
    TextView tvTitlePrizeDetail;
    @Bind(R.id.tvDatePrizeDetail)
    TextView tvDatePrizeDetail;
    @Bind(R.id.tvDescPrizeDetail)
    TextView tvDescPrizeDetail;
    @Bind(R.id.tvRulePrizeDetail)
    TextView tvRulePrizeDetail;
    @Bind(R.id.tvSubmitPrizeDetail)
    TextView tvSubmitPrizeDetail;
    /**
     * (0, "实物奖品"), (1, "虚拟奖品"), (2, "积分奖品");
     */
    private int prizeCategory;
    private MyPrizeBean prizeBean;
    private PrizeBeanDetail prizeBeanDetail;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_prize_detail, null);
    }


    public static void startActivity(Activity activity, MyPrizeBean prizeBean, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PrizeDetailActivity.class)
                        .putExtra("prizeBean", prizeBean)
                , requestCode);
    }


    @Override
    protected void initView() {
        prizeBean = (MyPrizeBean) getIntent().getSerializableExtra("prizeBean");
        prizeCategory = prizeBean.getPrizeCategory();


        mDao.myPrizeDetail(0, prizeBean.getId(), new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                prizeBeanDetail = GsonUtils.fromJson(response, PrizeBeanDetail.class);


                if (!TextUtils.isEmpty(prizeBeanDetail.getImg())) {
                    Glide.with(PrizeDetailActivity.this).load(prizeBeanDetail.getImg()).into(ivPrizeDetail);
                }

                tvDatePrizeDetail.setText("中奖时间：" + TimeUtils.millis2String(prizeBean.getWinTime(), new SimpleDateFormat("yyyy年MM月dd日")));

                tvTitlePrizeDetail.setText(prizeBeanDetail.getName());


                tvDescPrizeDetail.setText(prizeBeanDetail.getExplainInfo());
                tvRulePrizeDetail.setText(prizeBeanDetail.getGetRuleExplain());


//                if (prizeCategory == 2 && !TextUtils.isEmpty(prizeBeanDetail.getExpireDt())) {
//                    long timeSpanByNow = TimeUtils.getTimeSpanByNow(Long.parseLong(prizeBeanDetail.getExpireDt()), TimeConstants.DAY);
//                    if (timeSpanByNow < 0) {
//                        tvSubmitPrizeDetail.setBackgroundColor(ContextCompat.getColor(PrizeDetailActivity.this, R.color._dedede));
//                        tvSubmitPrizeDetail.setClickable(false);
//                    } else {
//                        tvSubmitPrizeDetail.setBackgroundColor(ContextCompat.getColor(PrizeDetailActivity.this, R.color.orange21));
//                        tvSubmitPrizeDetail.setClickable(true);
//                    }
//
//
//                }

                updateBtnStatus(prizeBeanDetail.isGetStatus());


            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                if (code != null)
                    ToastUtils.showShort(code.msg);
            }
        });


    }

    private void getAddress() {
        mDao.addressList(0, this);
    }


    @OnClick({R.id.tvSubmitPrizeDetail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmitPrizeDetail:
                switch (prizeCategory) {
                    case 0://实物
                        getAddress();
                        break;
                    case 1://虚拟
                        mDao.clickClaimPrize(2, "", prizeBean.getId(), "", "", prizeCategory, this);
                        break;
                    case 2://积分
                        if (prizeBeanDetail != null) {
                            if (prizeBeanDetail.getStatus().equals("1")) {
                                //弃用
                                MaterialDialog.Builder builder = new MaterialDialog.Builder(PrizeDetailActivity.this);
                                builder.title("温馨提示").content(prizeBeanDetail.getStatusMsg()).positiveText("确认").show();
                            } else {
                                mDao.clickClaimPrize(3, "", prizeBean.getId(), "", "", prizeCategory, this);
                            }

                        }
                        break;
                }


                break;


        }


    }

    @Override
    protected void setActionBarDetail() {

    }


    /**
     * 实现文本复制功能
     * add by wangqianzhou
     */
    public static void copy(String text, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);//text是内容
        cmb.setPrimaryClip(myClip);

    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0://获取地址列表
                final List<AddressBean> beanList = new Gson().fromJson(response, new TypeToken<List<AddressBean>>() {
                }.getType());

                if (beanList != null && beanList.size() > 0) {
                    ArrayList<String> address = new ArrayList<>();

                    for (AddressBean bean : beanList
                            ) {
                        address.add(bean.getAddressFull());
                    }
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(PrizeDetailActivity.this);
                    builder.itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            AddressBean addressBean = beanList.get(which);
                            mDao.clickClaimPrize(1, text.toString(), prizeBean.getId(), addressBean.getUserName(), addressBean.getPhoneNum(), prizeCategory, PrizeDetailActivity.this);
                            return false;
                        }
                    });
                    builder.items(address);
                    builder.positiveText("确定");
                    builder.negativeText("取消");
                    builder.title("选择地址");
                    builder.show();
                    break;
                } else {//没有地址，前往完善
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(PrizeDetailActivity.this);
                    builder.positiveText("立即前往")
                            .content("请前往我的-编辑个人信息完善地址")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    AddressManagerActivity.startActivity(activity, 0);
                                }
                            });
                    builder.show();
                }
                break;
            case 1://实物领取成功
                ToastUtils.showShort("领取成功");
                updateBtnStatus(true);
                break;
            case 2://虚拟领取成功
                updateBtnStatus(true);
                final MaterialDialog.Builder builder3 = new MaterialDialog.Builder(this);
                builder3.title("序列号");
                builder3.content(prizeBean.getRedeemCode());
                builder3.positiveText("点击复制");
                builder3.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        copy(prizeBean.getRedeemCode(), PrizeDetailActivity.this);
                        ToastUtils.showShort("复制成功");
                    }
                });
                builder3.show();
                break;
            case 3://积分领取成功
                updateBtnStatus(true);
                ToastUtils.showShort("领取成功");
                break;
        }
    }

    /**
     * 是否领取了
     *
     * @param status
     */
    private void updateBtnStatus(boolean status) {
        if (status) {
            if (prizeCategory == 1) {
                tvSubmitPrizeDetail.setText("查看序列号");
                tvSubmitPrizeDetail.setBackgroundColor(ContextCompat.getColor(PrizeDetailActivity.this, R.color.orange21));
                tvSubmitPrizeDetail.setClickable(true);
            } else {
                tvSubmitPrizeDetail.setBackgroundColor(ContextCompat.getColor(PrizeDetailActivity.this, R.color._dedede));
                tvSubmitPrizeDetail.setText("已领取");
                tvSubmitPrizeDetail.setClickable(false);
            }


        } else {
            tvSubmitPrizeDetail.setClickable(true);
            tvSubmitPrizeDetail.setBackgroundColor(ContextCompat.getColor(PrizeDetailActivity.this, R.color.orange21));
            tvSubmitPrizeDetail.setText("点击领取");
//            if (prizeCategory == 0 || prizeCategory == 2) {
//                tvSubmitPrizeDetail.setText("点击领取");
//            }
//            else {
//                tvSubmitPrizeDetail.setText("查看序列号");
//            }

        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code != null)
            ToastUtils.showShort(code.msg);
    }

}
