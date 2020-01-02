package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.accountsafe.SetTradePswVerificationCodeActivity;
import com.easylife.house.customer.ui.mine.accountsafe.SetTraderPasswordActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.view.ButtonTouch;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.REFRESH_ID_CARD_NUM;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_BIND_BANK_CARD;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_IDCARD_IDENTIFICATION;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_SET_TRADE_PSW;

public class IDCardNextActivity extends BaseActivity {

    @Bind(R.id.etNameIdCardNext)
    EditText etNameIdCardNext;
    @Bind(R.id.etIdCardIdCardNext)
    EditText etIdCardIdCardNext;
    @Bind(R.id.ivIdCardFront)
    ImageView ivIdCardFront;
    @Bind(R.id.ivIdCardBack)
    ImageView ivIdCardBack;
    @Bind(R.id.llImageIdCard)
    LinearLayout llImageIdCard;
    @Bind(R.id.tvText2IdCard)
    TextView tvText2IdCard;
    @Bind(R.id.cbAgreeIdCard)
    CheckBox cbAgreeIdCard;
    @Bind(R.id.tvUserBookIdCard)
    TextView tvUserBookIdCard;
    @Bind(R.id.btnNextIdCard)
    ButtonTouch btnNextIdCard;
    private String name;
    private String picFrontPath;
    private String idCard;
    private String picBackPath;
    private static int mRequestCode;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_idcard_next, null);
    }

    public static void startActivity(Activity activity, String name, String idCard, String picFrontPath, String picBackPath, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, IDCardNextActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("idCard", idCard);
        intent.putExtra("picFrontPath", picFrontPath);
        intent.putExtra("picBackPath", picBackPath);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        idCard = getIntent().getStringExtra("idCard");
        picFrontPath = getIntent().getStringExtra("picFrontPath");
        picBackPath = getIntent().getStringExtra("picBackPath");


        etNameIdCardNext.setText(name);
        etIdCardIdCardNext.setText(idCard);

        Glide.with(this).load(picFrontPath).into(ivIdCardFront);
        Glide.with(this).load(picBackPath).into(ivIdCardBack);


    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.btnNextIdCard, R.id.tvUserBookIdCard})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextIdCard:
                if (!cbAgreeIdCard.isChecked()) {
                    ToastUtils.showShort("请先阅读并同意《好生活用户协议》");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort("请输入姓名");
                    return;
                }


                if (TextUtils.isEmpty(idCard)) {
                    ToastUtils.showShort("请输入身份证号");
                    return;
                }


                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        submit();
                    }
                }).start();


                break;

            case R.id.tvUserBookIdCard:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;
        }
    }

    private void submit() {
        String base64PicFrontString = Base64Util.imageToBase64(picFrontPath);
        String base64PicBackString = Base64Util.imageToBase64(picBackPath);

        mDao.idCardSubmit(0, name, idCard, base64PicFrontString, base64PicBackString, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                Customer customer = mDao.getCustomer();
                customer.identityCardNum = idCard;
                mDao.saveCustomer(customer);
                ToastUtils.showShort("身份认证成功");
                EventBus.getDefault().post(new MessageEvent(REFRESH_ID_CARD_NUM));

                if (mRequestCode == REQUEST_CDOE_IDCARD_IDENTIFICATION) {//身份认证
//                    IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_IDCARD_IDENTIFICATION);
                    setResult(RESULT_OK);
                    finish();
                } else if (mRequestCode == REQUEST_CDOE_SET_TRADE_PSW) {//设置交易密码
                    SetTradePswVerificationCodeActivity.startActivity(IDCardNextActivity.this, REQUEST_CDOE_SET_TRADE_PSW);
//                    IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_SET_TRADE_PSW);
                } else if (mRequestCode == REQUEST_CDOE_BIND_BANK_CARD) {//添加银行卡
                    if (!TextUtils.isEmpty(customer.transactionPassword)) {
                        //已经设置了交易密码
                        InputTraderPasswordActivity.startActivity(IDCardNextActivity.this, ServerDao.ADD_BANK_CARD_INPUT_TRADE_PASSWORD);
                    } else {
                        // 添加银行卡页面跳转的设置交易密码流程中，不再验证手机号
                        SetTraderPasswordActivity.startActivity(IDCardNextActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
                    }

                } else {
                    setResult(RESULT_OK);
                    finish();

                }


            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
                ToastUtils.showShort("onFail:" + code.msg);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
