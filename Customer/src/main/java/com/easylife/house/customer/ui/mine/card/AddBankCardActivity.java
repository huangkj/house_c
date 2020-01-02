package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemClickListener2;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.JsonAddress;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CityUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.ProvinceCitySelectPopWindow;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class AddBankCardActivity extends BaseActivity {

    @Bind(R.id.etBankUserName)
    EditText etBankUserName;
    @Bind(R.id.etBankCardNumber)
    EditText etBankCardNumber;
    @Bind(R.id.etCreateBank)
    TextView etCreateBank;
    @Bind(R.id.etIdCardNumber)
    EditText etIdCardNumber;
    @Bind(R.id.etCreateBankSub)
    TextView etCreateBankSub;
    @Bind(R.id.etBankPhoneNumber)
    EditText etBankPhoneNumber;
    @Bind(R.id.etCreateBankAddress)
    TextView etCreateBankAddress;
    @Bind(R.id.tvText2AddBank)
    TextView tvText2AddBank;
    @Bind(R.id.cbAgreeAddBank)
    CheckBox cbAgreeAddBank;
    @Bind(R.id.tvUserBookAddBank)
    TextView tvUserBookAddBank;
    @Bind(R.id.btnNextAddBank)
    ButtonTouch btnNextAddBank;

    private String bankCardNum;
    private String bankImgFont;
    private String bankImgBack;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_add_bank_card, null);
    }


    public static void startActivity(Activity activity, String bankCardNum, String bankImgFont, String bankImgBack, int requestCode) {
        Intent intent = new Intent(activity, AddBankCardActivity.class);
        intent.putExtra("bankCardNum", bankCardNum);
        intent.putExtra("bankImgFont", bankImgFont);
        intent.putExtra("bankImgBack", bankImgBack);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        bankCardNum = getIntent().getStringExtra("bankCardNum");
        bankImgFont = getIntent().getStringExtra("bankImgFont");
        bankImgBack = getIntent().getStringExtra("bankImgBack");

        etBankCardNumber.setText(bankCardNum);
        Customer customer = mDao.getCustomer();
        if (customer != null)
            etIdCardNumber.setText(customer.identityCardNum);

        new Handler().postDelayed(mRunnable, 100);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            dataProvince = new Gson().fromJson(CityUtils.getProvinceJson(activity), JsonAddress.class).RECORDS;
            dataCity = new Gson().fromJson(CityUtils.getCityJson(activity), JsonAddress.class).RECORDS;

            selectPopWindow = new ProvinceCitySelectPopWindow(activity, new ItemClickListener2<JsonAddress.Data>() {
                @Override
                public void itemClick(JsonAddress.Data data1, JsonAddress.Data data2) {
                    province = data1;
                    city = data2;
                    etCreateBankAddress.setText(province.province + city.city);
                }
            });
            selectPopWindow.setData(dataProvince, dataCity);
        }
    };

    @Override
    protected void setActionBarDetail() {

    }

    private JsonAddress.Data province;
    private JsonAddress.Data city;
    private String bankId;
    private String openBranchBank;
    private String bankBranchNum;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 选择银行卡返回
                    String bankName = data.getStringExtra("name");
                    bankId = data.getStringExtra("id");
                    etCreateBank.setText(bankName);
                    break;
                case 3:
                    // 选择开户行支行返回
                    openBranchBank = data.getStringExtra("name");
                    bankBranchNum = data.getStringExtra("id");

                    etCreateBankSub.setText(openBranchBank);
                    break;
                default:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    private Dialog dialog;

    @OnClick({R.id.imgTipSub, R.id.llCreateBank, R.id.btnNextAddBank, R.id.tvUserBookAddBank, R.id.llCreateBankAddress, R.id.llCreateBankSub})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.imgTipSub:
                // 支行提示
                if (dialog == null)
                    dialog = new AlertDialog.Builder(this)
                            .setMessage("1.您可拨打银行卡背面处客户电话查询。\n\n2.您可就近前往开户银行柜台查询。\n\n3.您可在银行开户行手机银行App在线查询")
                            .setNegativeButton("确定", null)
                            .create();
                dialog.show();
                break;
            case R.id.llCreateBank:
                // 选择银行卡
                BankListActivity.startActivity(activity, 1);
                break;
            case R.id.btnNextAddBank:
                if (!cbAgreeAddBank.isChecked()) {
                    ToastUtils.showShort("请先阅读并同意《好生活用户协议》");
                    return;
                }

                final String userName = etBankUserName.getText().toString().trim();
                final String bankCardNumber = etBankCardNumber.getText().toString().trim();
                final String createBank = etCreateBank.getText().toString().trim();
                final String idCardNumber = etIdCardNumber.getText().toString().trim();
                final String bankPhoneNumber = etBankPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.showShort("请输入持卡人姓名");
                    return;
                }

                if (TextUtils.isEmpty(bankCardNumber)) {
                    ToastUtils.showShort("请输入银行卡号");
                    return;
                }

                if (TextUtils.isEmpty(createBank)) {
                    ToastUtils.showShort("请输入开户行");
                    return;
                }

                if (TextUtils.isEmpty(idCardNumber)) {
                    ToastUtils.showShort("请输入身份证号");
                    return;
                }

                if (TextUtils.isEmpty(openBranchBank)) {
                    ToastUtils.showShort("请选择开户行支行");
                    return;
                }

                if (TextUtils.isEmpty(bankPhoneNumber)) {
                    ToastUtils.showShort("请输入预留手机号");
                    return;
                }

                mDao.checkBindCard(0, bankCardNumber, idCardNumber, bankPhoneNumber, userName, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        //跳转获取验证码
                        BankVerificationCodeActivity.startActivity(
                                AddBankCardActivity.this, userName, bankCardNumber, createBank, idCardNumber,
                                openBranchBank, bankBranchNum, bankPhoneNumber, bankImgFont, bankImgBack, 0);
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        ToastUtils.showShort(code.msg);
                    }
                });
                break;
            case R.id.tvUserBookAddBank:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;
            case R.id.llCreateBankAddress://选择开户地区
                if (dataProvince == null || dataProvince.size() == 0) {
                    ToastUtils.showShort("省市数据读取失败，请重试");
                    new Handler().postDelayed(mRunnable, 100);
                    return;
                }

                if (selectPopWindow == null) {
                    selectPopWindow = new ProvinceCitySelectPopWindow(this, new ItemClickListener2<JsonAddress.Data>() {
                        @Override
                        public void itemClick(JsonAddress.Data data1, JsonAddress.Data data2) {
                            province = data1;
                            city = data2;
                            etCreateBankAddress.setText(province.province + city.city);
                        }
                    });
                    selectPopWindow.setData(dataProvince, dataCity);
                }
                showPopWindow2View(selectPopWindow, layActionBar);
                break;
            case R.id.llCreateBankSub://选择开户行支行
                if (province == null || city == null) {
                    ToastUtils.showShort("请先选择开户行地区");
                    return;
                }
                SelectSubBankActivity.startActivity(activity, bankId, province.provinceid, city.cityid, 3);
                break;
        }
    }

    private ArrayList<JsonAddress.Data> dataProvince = new ArrayList<>();
    private ArrayList<JsonAddress.Data> dataCity = new ArrayList<>();
    private ProvinceCitySelectPopWindow selectPopWindow;

    public void showPopWindow2View(PopupWindow pop, View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pop.setHeight(height);
            pop.showAsDropDown(anchor);
        } else {
            pop.showAsDropDown(anchor);
        }
    }
}
