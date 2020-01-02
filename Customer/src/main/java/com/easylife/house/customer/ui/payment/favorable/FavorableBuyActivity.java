package com.easylife.house.customer.ui.payment.favorable;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.bean.ResultCheckOrder;
import com.easylife.house.customer.bean.ResultMakeOrder;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.payment.PayTypeActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.InputTools;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.util.DoubleFomat.format2;

/**
 * Created by Mars on 2017/4/7 16:32.
 * 描述：购买优惠
 */

public class FavorableBuyActivity extends BaseActivity implements RequestManagerImpl {
    @Bind(R.id.imgCover)
    ImageView imgCover;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.tvDevPrice)
    TextView tvDevPrice;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tvDevAddress)
    TextView tvDevAddress;
    @Bind(R.id.edName)
    EditText edName;
    @Bind(R.id.btnEdit)
    ImageView btnEdit;
    @Bind(R.id.tvPhone)
    EditText tvPhone;
    @Bind(R.id.tvType)
    TextView tvType;
    @Bind(R.id.tvPayment)
    TextView tvPayment;
    @Bind(R.id.tvPaymentAll)
    TextView tvPaymentAll;
    @Bind(R.id.btnPayAll)
    RadioButton btnPayAll;
    @Bind(R.id.btnPayBatches)
    RadioButton btnPayBatches;
    @Bind(R.id.tvMoneySurplus)
    TextView tvMoneySurplus;
    @Bind(R.id.edVerifyCode)
    EditText edVerifyCode;
    @Bind(R.id.layPayBatches)
    LinearLayout layPayBatches;
    @Bind(R.id.layVerifyCode)
    LinearLayout layVerifyCode;
    @Bind(R.id.tvMoneyReal)
    EditText tvMoneyReal;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.tvMoneyBottom)
    TextView tvMoneyBottom;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    @Bind(R.id.btnGetVerifyCode)
    ButtonTouch btnGetVerifyCode;


    /**
     * @param activity
     * @param orderCode   订单号
     * @param detail      楼盘数据 : devId ,devName ,address, distribution[] ,projectName
     * @param parameter   订单参数 ： orderDiscount（优惠信息:content（优惠文本），discountType （OrderParameter.Favorable.TYPE_VIP -认筹）），
     *                    menberDiscountId（认筹的优惠id），orderLog（payOnThisTime ， pay）
     * @param payment     要支付的金额
     * @param total       合计
     * @param requestCode
     */
    public static void startActivity(Activity activity, String orderCode, HousesDetailBaseBean detail, OrderParameter parameter, String payment, String total, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FavorableBuyActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("detail", detail)
                        .putExtra("parameter", parameter)
                        .putExtra("total", total)
                        .putExtra("paymentStr", payment)
                , requestCode
        );
    }

    public static void startActivity(Fragment fragment, String orderCode, HousesDetailBaseBean detail, OrderParameter parameter, String payment, String total, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), FavorableBuyActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("detail", detail)
                        .putExtra("parameter", parameter)
                        .putExtra("total", total)
                        .putExtra("paymentStr", payment)
                , requestCode
        );
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_favorable_buy, null);
    }

    /**
     * 待支付。真实支付价格
     */
    private String paymentStr, paymentRealStr;
    /**
     * 合计
     */
    private String total;
    private String name, phone;
    private String orderCode;
    private String localSerial;
    private HousesDetailBaseBean detail;

    private String temp;

    private OrderParameter parameter;
    private SmsButtonUtil smsButtonUtil;

    @Override
    protected void initView() {
        smsButtonUtil = new SmsButtonUtil(btnGetVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");

        name = mDao.getCustomer().username;
        phone = mDao.getCustomer().phone;

        orderCode = getIntent().getStringExtra("orderCode");
        detail = (HousesDetailBaseBean) getIntent().getSerializableExtra("detail");
        paymentRealStr = paymentStr = getIntent().getStringExtra("paymentStr");
        total = getIntent().getStringExtra("total");
        parameter = (OrderParameter) getIntent().getSerializableExtra("parameter");

        if (!TextUtils.isEmpty(orderCode)) {
            btnEdit.setVisibility(View.GONE);
        }
        tvPaymentAll.setText(DoubleFomat.format(total));// 合计

        tvDevName.setText(detail.devName);
        if (TextUtils.isEmpty(detail.addressDistrict)) {
            tvDevAddress.setText("地址：" + detail.addressDetail);
        } else {
            tvDevAddress.setText("地址：" + detail.addressDistrict + detail.addressTown + detail.addressDetail);
        }
        tvDevPrice.setText(detail.averPrice + "元/㎡");
        if (detail.effectId != null && detail.effectId.size() != 0) {
            CacheManager.initImageClientList(activity, imgCover, detail.effectId.get(0).url);
        } else {
            CacheManager.initImageClientList(activity, imgCover, null);
        }
//        if (!TextUtils.isEmpty(favorableText)) {
//            tvType.setText(favorableText);
//        }

        edName.setText(name);
        tvPayment.setText("￥" + DoubleFomat.format(paymentStr)); // 待支付
        if (parameter != null && !TextUtils.isEmpty(parameter.customerPhone)) {
            phone = parameter.customerPhone;
            parameter.orderLog.pay = paymentStr;
            tvPhone.setText(phone);
        } else {
            if (TextUtils.isEmpty(phone)) {
                layVerifyCode.setVisibility(View.VISIBLE);
                btnGetVerifyCode.setVisibility(View.VISIBLE);

                tvPhone.setFocusable(true);
                tvPhone.setFocusableInTouchMode(true);
            } else {
                tvPhone.setText(phone);
            }
        }


        tvMoneyReal.setText(DoubleFomat.format(paymentRealStr));// 本次支付金额
        tvMoneyBottom.setText(DoubleFomat.format(paymentRealStr)); // 实付款


        tvMoneyReal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 随时计算
                if (TextUtils.isEmpty(s.toString()))
                    return;
                double m = Double.parseDouble(paymentStr);
                double cur = Double.parseDouble(s.toString());
                if (cur > m) {
                    CustomerUtils.showTip(activity, "超出需支付金额，请重新输入");
                    tvMoneyReal.setText(temp);
                    return;
                }
                temp = s.toString();
                tvMoneyBottom.setText(s);// 实付款
                paymentRealStr = format2(cur);
                tvMoneySurplus.setText("￥" + DoubleFomat.format(m - cur)); // 剩余支付金额
                Editable etext = tvMoneyReal.getText();
                Selection.setSelection(etext, etext.length());
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
    }

    private PubTipDialog dialog;

    private void showTip() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, null);
        }
        dialog.showPayTip(R.layout.pub_toast_tip_pay_desc);
    }

    @OnClick({R.id.btnEdit, R.id.btnPayAll, R.id.btnGetVerifyCode, R.id.btnPayBatches, R.id.tvAgreement, R.id.btnSubmit, R.id.tvDesc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDesc:
                showTip();
                break;
            case R.id.btnGetVerifyCode:
                phone = tvPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomerUtils.showTip(activity, "请输入手机号");
                    return;
                }
                mDao.getVerifyCode(1, ServerDao.TYPE_VERIFYCODE_BIND_USER, phone, this);
                break;
            case R.id.btnEdit:
                edName.setFocusable(true);
                edName.setFocusableInTouchMode(true);
                edName.requestFocus();
                CharSequence text = edName.getText();
                if (!TextUtils.isEmpty(text) && text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
                InputTools.showKeyboard(edName);
                btnEdit.setVisibility(View.GONE);
                break;
            case R.id.btnPayAll:
                layPayBatches.setVisibility(View.GONE);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                tvMoneyReal.setFocusable(false);
                tvMoneyReal.setFocusableInTouchMode(false);

                tvMoneyReal.setText(DoubleFomat.format(paymentStr));
                tvMoneyBottom.setText(DoubleFomat.format(paymentStr));
                break;
            case R.id.btnPayBatches:
                layPayBatches.setVisibility(View.VISIBLE);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                tvMoneyReal.setFocusable(true);
                tvMoneyReal.setFocusableInTouchMode(true);

                tvMoneyReal.setText(null);
                tvMoneyReal.setHint("请输入金额");
                tvMoneyBottom.setText("0");
                tvMoneySurplus.setText("￥" + DoubleFomat.format((paymentStr)));// 剩余支付金额
                break;
            case R.id.tvAgreement:
                //    好生活购房优惠说明书  + name=李四&phone=13001998378
                WebViewActivity.startActivity(activity, "好生活会员增值服务协议书", Constants.URL_FAVORABLE_VIP + "name=" + name + "&phone=" + phone, "我已阅读");
                break;
            case R.id.btnSubmit:

                name = edName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomerUtils.showTip(activity, "请输入客户名称");
                    return;
                }
                phone = tvPhone.getText().toString();
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(activity, "请输入有效号码");
                    return;
                }
                if (layVerifyCode.getVisibility() == View.VISIBLE) {
                    String verifyCode = edVerifyCode.getText().toString();
                    if (TextUtils.isEmpty(verifyCode)) {
                        CustomerUtils.showTip(activity, "请输入验证码");
                        return;
                    }
                    mDao.bindPhone(3, name, phone, verifyCode, this);
                } else {
                    parameter.realName = name;
                    parameter.customerPhone = phone;

                    String priceStr = tvMoneyReal.getText().toString();
                    if (TextUtils.isEmpty(priceStr)) {
                        CustomerUtils.showTip(activity, "请输入支付金额");
                        return;
                    }
                    double price = Double.parseDouble(priceStr);
                    if (price == 0) {
                        CustomerUtils.showTip(activity, "请输入支付金额");
                        return;
                    }
                    parameter.orderLog.payOnThisTime = priceStr;
                    if (TextUtils.isEmpty(orderCode)) {
                        mDao.makeOrder(2, parameter, this);
                    } else {
                        // TODO  支付前先判断订单状态，订单状态ok再继续支付。 待测试
                        mDao.checkOrderState(7, orderCode, new RequestManagerImpl() {
                            @Override
                            public void onSuccess(String response, int requestType) {
                                ResultCheckOrder resultCheckOrder = new Gson().fromJson(response, ResultCheckOrder.class);
                                if (resultCheckOrder == null)
                                    return;
                                if (ResultCheckOrder.CheckState.ALL.name().equals(resultCheckOrder.settleStatus)) {
                                    // 已全部支付
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    // 订单可以继续支付，跳转支付类型选择页面
                                    mDao.payContinue(4, orderCode, parameter.orderLog.payOnThisTime, FavorableBuyActivity.this);
                                }
                            }

                            @Override
                            public void onFail(NetBaseStatus code, int requestType) {
                                CustomerUtils.showTip(activity, (code == null || TextUtils.isEmpty(code.msg)) ? "订单信息获取失败，请去我的订单中继续支付" : code.msg);
                                setResult(RESULT_OK);
                                finish();
                            }
                        });

                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //   继续支付，checkOrder，刷新待支付数据
                    if (!TextUtils.isEmpty(orderCode)) {
                        mDao.checkOrderState(5, orderCode, new RequestManagerImpl() {
                            @Override
                            public void onSuccess(String response, int requestType) {
                                ResultCheckOrder resultCheckOrder = new Gson().fromJson(response, ResultCheckOrder.class);
                                if (resultCheckOrder == null)
                                    return;
                                if (ResultCheckOrder.CheckState.ALL.name().equals(resultCheckOrder.settleStatus)) {
                                    // 已全部支付
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    // 更新支付数据，继续支付
                                    btnEdit.setVisibility(View.GONE);
                                    btnPayAll.setChecked(true);
                                    layVerifyCode.setVisibility(View.GONE);
                                    btnGetVerifyCode.setVisibility(View.GONE);
                                    tvPhone.setFocusable(false);
                                    tvPhone.setFocusableInTouchMode(false);

                                    paymentRealStr = paymentStr = resultCheckOrder.tobepay;

                                    tvPayment.setText("￥" + DoubleFomat.format(paymentStr)); // 待支付

                                    tvMoneyReal.setText(DoubleFomat.format(paymentRealStr));// 本次支付金额
                                    tvMoneyBottom.setText(DoubleFomat.format(paymentRealStr)); // 实付款

                                    localSerial = resultCheckOrder.localSerial;
                                }
                            }

                            @Override
                            public void onFail(NetBaseStatus code, int requestType) {
                                CustomerUtils.showTip(activity, "订单信息获取失败，请去我的订单中继续支付");
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                    break;
            }
        }
    }

    private String orderName = "新房委托会员服务费";
    private String orderMark;

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 获取验证码 ，开始倒计时
                smsButtonUtil.startCountDown();
                break;
            case 2:
                // 订单生成成功
                mDao.pointBuyFavorable(detail.devId, detail.devName);

                ResultMakeOrder resultMakeOrder = new Gson().fromJson(response, ResultMakeOrder.class);
                orderName = "新房委托会员服务费";
                orderMark = "会员服务费-[" + (detail == null ? "项目名称" : detail.projectName) + "]";
                orderCode = resultMakeOrder.orderCode;
                localSerial = resultMakeOrder.localSerial;
                PayTypeActivity.startActivity(activity, orderCode, localSerial, orderName, orderMark, parameter, 1);
                break;
            case 3:
                // 绑定手机号成功
                parameter.realName = name;
                parameter.customerPhone = phone;
                Customer c = mDao.getCustomer();
                c.phone = phone;
                mDao.saveCustomer(c);
                String priceStr = tvMoneyReal.getText().toString();
                double price = Double.parseDouble(priceStr);
                if (price == 0) {
                    CustomerUtils.showTip(activity, "请输入支付金额");
                    return;
                }
                parameter.orderLog.payOnThisTime = priceStr;
                mDao.makeOrder(2, parameter, this);
                break;
            case 4:
                ResultCheckOrder checkOrder = new Gson().fromJson(response, ResultCheckOrder.class);
                if (checkOrder == null) {
                    CustomerUtils.showTip(activity, "订单信息错误，请重试");
                    return;
                }
                localSerial = checkOrder.localSerial;

                orderName = "新房委托会员服务费";
                orderMark = "会员服务费-[" + (detail == null ? "珠江帝景" : detail.projectName) + "]";
                PayTypeActivity.startActivity(activity, orderCode, localSerial, orderName, orderMark, parameter, 1);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        CustomerUtils.showTip(activity, TextUtils.isEmpty(code.msg) ? "失败" : code.msg);
    }
}
