package com.easylife.house.customer.ui.payment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ImageAddAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.brokerage.SelectBankActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.ImagePrickerUtil;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_ORDER_STATUS;

/**
 * Created by Mars on 2017/7/18 14:58.
 * 描述：申请退款页面
 */
@RuntimePermissions
public class ApplyRefundActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.tvCardName)
    TextView tvCardName;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    @Bind(R.id.edPhone)
    TextView edPhone;
    @Bind(R.id.edCode)
    EditText edCode;
    @Bind(R.id.btnVerifyCode)
    ButtonTouch btnVerifyCode;
    @Bind(R.id.btnCancel)
    ButtonTouch btnCancel;
    @Bind(R.id.btnOK)
    ButtonTouch btnOK;
    @Bind(R.id.layRefundApply)
    RelativeLayout layRefundApply;
    @Bind(R.id.layClick)
    LinearLayout layClick;

    public static void startActivity(Activity activity, String orderCode, String price, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ApplyRefundActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("price", price)
                , requestCode
        );
    }

    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.edDesc)
    EditText edDesc;
    @Bind(R.id.rabtn0)
    RadioButton rabtn0;
    @Bind(R.id.rabtn1)
    RadioButton rabtn1;
    @Bind(R.id.rabtn2)
    RadioButton rabtn2;
    @Bind(R.id.rabtn3)
    RadioButton rabtn3;
    @Bind(R.id.rabtn4)
    RadioButton rabtn4;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_apply_refund, null);
    }

    private String orderCode, price, phone;
    private String desc = "不想要了";
    private String[] imgs;
    private SmsButtonUtil smsButtonUtil;
    private BankCardListBean bankCard;
    private ImageAddAdapter adapter;
    private String verifyCode;
    private int count;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        price = getIntent().getStringExtra("price");

        try {
            phone = mDao.getCustomer().phone;
        } catch (Exception e) {
            LogOut.d("  getPhone;", e.toString());
        }

        adapter = new ImageAddAdapter(this);
        adapter.setAddForResult(new ImageAddAdapter.IResult() {
                                    @Override
                                    public void add() {
                                        if (TextUtils.isEmpty(adapter.getItem(adapter.getItemCount() - 1).pic)) {
                                            count = ImageAddAdapter.COUNT_RECORD_IMAGE_MAX - adapter.getItemCount() + 1;
                                        } else {
                                            count = ImageAddAdapter.COUNT_RECORD_IMAGE_MAX - adapter.getItemCount();
                                        }
                                        ApplyRefundActivityPermissionsDispatcher.pickImageWithCheck(ApplyRefundActivity.this);
                                    }

                                    @Override
                                    public void del(int position) {
                                        showTipDelImage(position);
                                    }
                                }
        );
        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        smsButtonUtil = new SmsButtonUtil(btnVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");
        edPhone.setText(phone);

        tvMoney.setText(price + "元");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rabtn0:
                        desc = rabtn0.getText().toString();
                        edDesc.setFocusable(false);
                        edDesc.setFocusableInTouchMode(false);
                        break;
                    case R.id.rabtn1:
                        desc = rabtn1.getText().toString();
                        edDesc.setFocusable(false);
                        edDesc.setFocusableInTouchMode(false);
                        break;
                    case R.id.rabtn2:
                        desc = rabtn2.getText().toString();
                        edDesc.setFocusable(false);
                        edDesc.setFocusableInTouchMode(false);
                        break;
                    case R.id.rabtn3:
                        desc = rabtn3.getText().toString();
                        edDesc.setFocusable(false);
                        edDesc.setFocusableInTouchMode(false);
                        break;
                    case R.id.rabtn4:
                        edDesc.setFocusable(true);
                        edDesc.setFocusableInTouchMode(true);
                        break;
                }
            }
        });
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void pickImage() {
        ImagePrickerUtil.pickerImage(activity, count, 5);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ApplyRefundActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private AlertDialog alertDialog;

    private void showTipDelImage(final int position) {
        View contentView = getLayoutInflater().inflate(R.layout.toast_pub_tip_del_image, null);
        alertDialog = new AlertDialog.Builder(activity, R.style.UpdateAlertDialogStyle).create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(contentView);

        contentView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(position);
                adapter.notifyAdapterDataSetChanged();
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.tvCardName, R.id.tvAgreement, R.id.btnSubmit, R.id.btnVerifyCode, R.id.btnCancel, R.id.btnOK, R.id.layRefundApply, R.id.layClick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCardName:
                // 选择银行卡
                SelectBankActivity.startActivity(activity, bankCard == null ? null : bankCard.getBankCardNum(), true, 4);
                break;
            case R.id.layClick:
                LogOut.d("layClick", "layClick");
                break;
            case R.id.tvAgreement:
                WebViewActivity.startActivity(activity, "好生活会员服务退款函", Constants.URL_REFUND_DESC);
                break;
            case R.id.btnSubmit:
                if (bankCard == null) {
                    CustomerUtils.showTip(activity, "请选择到账银行卡");
                    return;
                }
                if (adapter.getItemCount() <= 1) {
                    CustomerUtils.showTip(activity, "请上传退款凭证");
                    return;
                }
                if (rabtn4.isChecked()) {
                    desc = edDesc.getText().toString();
                    if (TextUtils.isEmpty(desc)) {
                        CustomerUtils.showTip(activity, "请输入您的退款原因");
                        return;
                    }
                }
                if (!cbAgreement.isChecked()) {
                    CustomerUtils.showTip(activity, "请确认并同意退款函");
                    return;
                }
                layRefundApply.setVisibility(View.VISIBLE);
                break;
            case R.id.btnVerifyCode:
                showLoading();
                mDao.getVerifyCode(1, ServerDao.TYPE_VERIFYCODE_APPLY_REFUND, phone, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        cancleLoading();
                        smsButtonUtil.startCountDown();
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        cancleLoading();
                        CustomerUtils.showTip(activity, code.msg);
                    }
                });
                break;
            case R.id.btnCancel:
                layRefundApply.setVisibility(View.GONE);
                break;
            case R.id.btnOK:
                verifyCode = edCode.getText().toString();
                if (TextUtils.isEmpty(verifyCode)) {
                    CustomerUtils.showTip(activity, "请输入验证码");
                    return;
                }
                showLoading();
                List<ImageBean> imageBeanList = new ArrayList<>(adapter.getData());
                for (ImageBean image : imageBeanList) {
                    if (TextUtils.isEmpty(image.pic)) {
                        imageBeanList.remove(image);
                    } else {
                        image.pic = CustomerUtils.base64Recode(image.pic);
                    }
                }
                mDao.updateImg(3, imageBeanList, this);
                break;
            case R.id.layRefundApply:
                layRefundApply.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 退款进度页面
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 2:
                    // 填写卡资料页面
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 3:
                    // 资料上传页面
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 4:
                    // 选择银行卡返回并显示
                    bankCard = (BankCardListBean) data.getSerializableExtra("bankCardItem");
                    if (bankCard == null)
                        return;
                    String last4Num = bankCard.getBankCardNum();
                    if (!TextUtils.isEmpty(bankCard.getBankCardNum()))
                        last4Num = last4Num.substring(bankCard.getBankCardNum().length() - 4);
                    tvCardName.setText(bankCard.getBelongToBank() + " (" + last4Num + ")");
                    break;
                case 5:
                    // 选择图片返回
                    List<String> selectImagesPath = ImagePrickerUtil.getSelectImagesPath(data);
                    if (selectImagesPath != null && selectImagesPath.size() != 0) {
                        uploadLocalImage(selectImagesPath);
                    }
                    break;
            }
        }


    }

    private void uploadLocalImage(List<String> paths) {
        if (adapter == null) {
            return;
        }
        if (adapter.getItemCount() != 0 && TextUtils.isEmpty(adapter.getItem(adapter.getItemCount() - 1).pic))
            adapter.remove(adapter.getItemCount() - 1);
        for (String path : paths) {
            ImageBean image = new ImageBean();
            image.name = ImagePrickerUtil.getRandomName();
            image.pic = path;
            adapter.addData(image);
        }
        adapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 3:
                // 上传图片
                ImageResult resultList = new Gson().fromJson(response, ImageResult.class);
                if (resultList == null || resultList.data == null || resultList.data.size() == 0) {
                    cancleLoading();
                    CustomerUtils.showTip(activity, "图片上传失败，请重试");
                    return;
                }
                List<ImageResult.DataBean> images = new ArrayList<>();
                for (ImageResult.DataBean img : resultList.data) {
                    if ("1000".equals(img.code)) {
                        images.add(img);
                    }
                }
                if (images.size() != resultList.data.size()) {
                    cancleLoading();
                    CustomerUtils.showTip(activity, "部分图片上传失败，请重试");
                    return;
                }
                imgs = new String[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    ImageResult.DataBean image = images.get(i);
                    imgs[i] = image.url;
                }

                mDao.applyRefund(2, orderCode, price, desc, phone, verifyCode, bankCard.getCardName(), bankCard.getOpenBranchBank(),
                        bankCard.getBankCardNum(), bankCard.getApplyUserCardNum(), bankCard.getLinkNumber(), imgs, ApplyRefundActivity.this);
                break;
            case 2:
                // 提交申请
                cancleLoading();

                EventBus.getDefault().post(new MessageEvent(UPDATE_ORDER_STATUS, Order.OrderType.REFUND.code));
                CustomerUtils.showTip(activity, "退款申请成功");
                RefundRateActivity.startActivity(activity, orderCode, 1);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancleLoading();
    }
}
