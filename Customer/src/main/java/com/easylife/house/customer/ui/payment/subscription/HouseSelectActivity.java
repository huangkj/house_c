package com.easylife.house.customer.ui.payment.subscription;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.FavorablePurchaseAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/6/27 14:40.
 * 描述：在线选房下单流程1
 */

public class HouseSelectActivity extends BaseActivity implements RequestManagerImpl {

    public static void startActivity(Activity activity, String orderCode, String devId, String cityId, String cityName, String image, String devName,
                                     String cellName, String unitId, String floor, String buildId, String buildName,
                                     String buildNo, String structure,
                                     String fArea, String price, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HouseSelectActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("devId", devId)
                        .putExtra("cityId", cityId)
                        .putExtra("cityName", cityName)
                        .putExtra("image", image)
                        .putExtra("devName", devName)
                        .putExtra("cellName", cellName)
                        .putExtra("unitId", unitId)
                        .putExtra("floor", floor)
                        .putExtra("buildId", buildId)
                        .putExtra("buildName", buildName)
                        .putExtra("buildNo", buildNo)
                        .putExtra("structure", structure)
                        .putExtra("fArea", fArea)
                        .putExtra("price", price)
                , requestCode);
    }

    @Bind(R.id.imgCover)
    ImageView imgCover;
    @Bind(R.id.tvHouseName)
    TextView tvHouseName;
    @Bind(R.id.tvHouseStructure)
    TextView tvHouseStructure;
    @Bind(R.id.tvDevSignDesc)
    TextView tvDevSignDesc;
    @Bind(R.id.tvHouseArea)
    TextView tvHouseArea;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.tvHousePrice)
    TextView tvHousePrice;
    @Bind(R.id.radioGroupFavorable)
    RadioGroup radioGroupFavorable;
    @Bind(R.id.raBtnAll)
    RadioButton raBtnAll;
    @Bind(R.id.raBtnLoan)
    RadioButton raBtnLoan;
    @Bind(R.id.edCustomerName)
    EditText edCustomerName;
    @Bind(R.id.edCustomerPhone)
    EditText edCustomerPhone;
    @Bind(R.id.edCustomerIDCard)
    EditText edCustomerIDCard;
    @Bind(R.id.edVerifyCode)
    EditText edVerifyCode;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    @Bind(R.id.btnGetVerifyCode)
    ButtonTouch btnGetVerifyCode;
    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.layVerifyCode)
    LinearLayout layVerifyCode;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_house_buy, null);
    }

    private String cityId;
    private String cityName;
    private String orderCode;
    private String devId;
    private String image;
    private String devName = "合生中山";
    private String buildNo;
    private String structure;
    private String fArea;
    private String price;
    private String cellName;
    private String unitId;
    private String floor;
    private String buildId;
    private String buildName;
    private String pay;

    private FavorablePurchaseAdapter adapter;

    private SmsButtonUtil smsButtonUtil;

    private DevFavorable devFavorable;

    @Override
    protected void initView() {
        smsButtonUtil = new SmsButtonUtil(btnGetVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");

        orderCode = getIntent().getStringExtra("orderCode");
        devId = getIntent().getStringExtra("devId");
//        cityId = getIntent().getStringExtra("cityId");
//        cityName = getIntent().getStringExtra("cityName");
        image = getIntent().getStringExtra("image");
        devName = getIntent().getStringExtra("devName");

        buildNo = getIntent().getStringExtra("buildNo");
        structure = getIntent().getStringExtra("structure");
        fArea = getIntent().getStringExtra("fArea");
        price = getIntent().getStringExtra("price");

        cellName = getIntent().getStringExtra("cellName");
        unitId = getIntent().getStringExtra("unitId");
        floor = getIntent().getStringExtra("floor");
        buildId = getIntent().getStringExtra("buildId");
        buildName = getIntent().getStringExtra("buildName");

        adapter = new FavorablePurchaseAdapter(R.layout.item_favorable_cb, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(this));
        recycleFavorable.setAdapter(adapter);

        initUserInfo();
        initHouseDetail();

        // 获取楼盘优惠信息
        mDao.selectEstateProjectDev(1, devId, this);
    }

    private void initUserInfo() {
        Customer customer = mDao.getCustomer();
        if (customer != null) {
            edCustomerName.setText(customer.username);

            if (TextUtils.isEmpty(customer.phone)) {
                layVerifyCode.setVisibility(View.VISIBLE);
            } else {
                edCustomerPhone.setText(customer.phone);
            }
        }
    }

    private void initHouseDetail() {

        CacheManager.initCenterCropImage(activity, imgCover, image);
        tvHouseName.setText(devName + " " + buildNo);
        tvHouseStructure.setText(structure);
        tvHouseArea.setText(fArea + "㎡");
        tvHousePrice.setText(price + "万元");

        tvDevSignDesc.setText(
                Html.fromHtml(
                        "信息填写说明： 以上信息将用于《" +
                                "<font   color=\"#FF6800\">" +
                                devName +
                                " 认购通知书</font>" +
                                "》中已保证双方权益，请慎重填写"));
    }

    private void initFavorableInfo(DevFavorable data) {
        if (data == null)
            return;

        this.devFavorable = data;

        pay = data.subscription;
        cityId = data.cityId;
        cityName = data.cityName;

        tvAgreement.setText(
                Html.fromHtml(
                        "我已阅读并知悉" +
                                "<font   color=\"#FF6800\">《" +
                                cityName +
                                "购房管理约定》</font>"));
        adapter.setNewData(data.favourable);
        if (data.discount != null) {
            radioGroupFavorable.setVisibility(View.VISIBLE);
            raBtnAll.setText(Html.fromHtml(
                    "全款" +
                            "<font   color=\"#FF6800\">" +
                            data.discount.discountAll +
                            "折</font>"));
            raBtnLoan.setText(Html.fromHtml(
                    "贷款" +
                            "<font   color=\"#FF6800\">" +
                            data.discount.discountDai +
                            "折</font>"));
            if ("1".equals(data.discount.discountAllType)) {
            }
            raBtnAll.setVisibility("1".equals(data.discount.discountAllType) ? View.VISIBLE : View.GONE);
            raBtnLoan.setVisibility("1".equals(data.discount.discountDaiType) ? View.VISIBLE : View.GONE);
//            raBtnAll.setChecked("1".equals(data.discount.discountAllType));
//            raBtnLoan.setChecked("1".equals(data.discount.discountDaiType));
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.btnGetVerifyCode, R.id.tvAgreement, R.id.tvDevSignDesc, R.id.btnSubmit})
    public void onClick(View view) {
        String name = "";
        String phone = "";
        switch (view.getId()) {
            case R.id.btnGetVerifyCode:
                name = edCustomerName.getText().toString();
                phone = edCustomerPhone.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomerUtils.showTip(activity, "请输入用户名");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(activity, "请输入有效的手机号");
                    return;
                }
                mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_BIND_USER, phone, this);
                break;
            case R.id.tvDevSignDesc:
                // 楼盘认购通知书
                WebViewActivity.startActivity(activity, devName + " 认购通知书",
                        Constants.URL_FAVORABLE_DEV + orderCode);
                break;
            case R.id.tvAgreement:
                // 城市购房管理约定
                WebViewActivity.startActivity(activity, cityName + " 购房管理约定",
                        Constants.URL_CITY_RULE + cityId);
                break;
            case R.id.btnSubmit:
                name = edCustomerName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomerUtils.showTip(activity, "请输入用户名");
                    return;
                }
                phone = edCustomerPhone.getText().toString();
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(activity, "请输入正确的手机号");
                    return;
                }
                if (layVerifyCode.getVisibility() == View.VISIBLE) {
                    name = edCustomerName.getText().toString();
                    phone = edCustomerPhone.getText().toString();

                    String verifyCode = edVerifyCode.getText().toString();
                    if (TextUtils.isEmpty(verifyCode)) {
                        CustomerUtils.showTip(activity, "请输入验证码");
                        return;
                    }
                    mDao.bindPhone(3, name, phone, verifyCode, this);
                    return;
                }
                String idCard = edCustomerIDCard.getText().toString();
                if (!ValidatorUtils.isIDCard(idCard)) {
                    CustomerUtils.showTip(activity, "请输入有效的身份证号");
                    return;
                }
                mDao.unfinish(4, idCard, this);
                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        String name = "";
        String phone = "";
        String card = "";
        switch (requestType) {
            case 1:
                // 获取楼盘优惠信息
                DevFavorable data = new Gson().fromJson(response, DevFavorable.class);
                initFavorableInfo(data);
                break;
            case 2:
                // 获取绑定手机号的验证码,然后绑定手机号
                smsButtonUtil.startCountDown();
                break;
            case 3:
                // 绑定手机号
                card = edCustomerIDCard.getText().toString();
                if (!ValidatorUtils.isIDCard(card)) {
                    CustomerUtils.showTip(activity, "请输入有效的身份证件");
                    return;
                }
                mDao.unfinish(4, card, this);
                break;
            case 4:
                // 验证非完成订单
                name = edCustomerName.getText().toString();
                card = edCustomerIDCard.getText().toString();
                phone = edCustomerPhone.getText().toString();

                OrderParameter parameter = new OrderParameter();
                parameter.customerIdCardNum = card;
                parameter.customerPhone = phone;
                parameter.realName = name;
                parameter.followType = "3";
                parameter.estateProjectDevId = devId;
                parameter.estateProjectDevName = devName;

                // TODO  临时的错误数据
                if (TextUtils.isEmpty(pay)) {
                    pay = "1000000";
                }
                parameter.orderLog.pay = pay;

                parameter.customerPurchase.cellName = cellName;
                parameter.customerPurchase.unitId = unitId;
                parameter.customerPurchase.floor = floor;
                parameter.customerPurchase.buildId = buildId;
                parameter.customerPurchase.buildName = buildName;
                List<DevFavorable.Favorable> favorables = adapter.getSelectedItems();
                if (radioGroupFavorable.getVisibility() == View.VISIBLE) {
                    OrderParameter.Favorable f = new OrderParameter.Favorable();
                    f.discountType = OrderParameter.Favorable.TYPE_DEV;
                    if (raBtnAll.isChecked()) {
                        f.discount = devFavorable.discount.discountAll;
                        f.content = "全款";
                    } else if (raBtnLoan.isChecked()) {
                        f.discount = devFavorable.discount.discountDai;
                        f.content = "贷款";
                    }
                    parameter.orderDiscount.add(f);
                }
                if (favorables != null && favorables.size() != 0) {
                    for (DevFavorable.Favorable favorable : favorables) {
                        OrderParameter.Favorable f = new OrderParameter.Favorable();
                        f.discountType = OrderParameter.Favorable.TYPE_DEV;
                        f.discount = favorable.value;
                        f.content = favorable.key;
                        parameter.orderDiscount.add(f);
                    }
                }

                HousePayActivity.startActivity(activity, parameter, null, 0);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        switch (requestType) {
            case 4:
                if (code != null)
                    showTip(code.msg);
                break;
        }
    }

    private PubTipDialog dialog;

    private void showTip(String msg) {
        if (dialog == null) {
            dialog = new PubTipDialog(this, new PubTipDialog.InsideListener() {

                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        // 查看订单
                    } else {
                        // 放弃下单
                    }
                }
            });
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "您有其他房产认购订单未完成";
        }
        dialog.showdialog("提示", msg, null, "放弃", "查看订单");
    }
}
