package com.easylife.house.customer.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.util.InputTools;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.util.ValidatorUtils;

import java.util.Timer;
import java.util.TimerTask;

public class HouseNoticeDialog extends Dialog implements View.OnClickListener {

    private final Context mContext;
    private EditText etPhoneNumber;
    private SmsButtonUtil smsButtonUtil;
    private View contentView;
    private TextView tvTitle;
    private TextView tvTips;
    private TextView tvSubCount;
    private OnConfirmClickListener mOnConfirmClickListener;
    private EditText etVerificationCode;
    private OnVerificationCodeListener mOnVerificationCodeListener;


    public interface OnConfirmClickListener {
        void onConfirmClick(String phone, String vrificationCode);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener l) {
        mOnConfirmClickListener = l;
    }

    public interface OnVerificationCodeListener {
        void onGetCodeClick(String phone);
    }

    public void setOnVerificationCodeListener(OnVerificationCodeListener l) {
        mOnVerificationCodeListener = l;
    }


    public HouseNoticeDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {


        contentView = View.inflate(getContext(), R.layout.house_notice_dialog, null);
        setContentView(contentView);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        setCanceledOnTouchOutside(false);


/*
* <!--ivClose tvTitle tvTips tvSubCount etPhoneNumber ivClear etVerificationCode

tvGetVerificationCode tvConfirm
-->
* */

        ImageView ivClose = (ImageView) contentView.findViewById(R.id.ivClose);
        final ImageView ivClear = (ImageView) contentView.findViewById(R.id.ivClear);
        tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
        tvTips = (TextView) contentView.findViewById(R.id.tvTips);
        tvSubCount = (TextView) contentView.findViewById(R.id.tvSubCount);
        etPhoneNumber = (EditText) contentView.findViewById(R.id.etPhoneNumber);
        etVerificationCode = (EditText) contentView.findViewById(R.id.etVerificationCode);
        TextView tvGetVerificationCode = (TextView) contentView.findViewById(R.id.tvGetVerificationCode);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tvConfirm);


        ivClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        tvGetVerificationCode.setOnClickListener(this);
        smsButtonUtil = new SmsButtonUtil(tvGetVerificationCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");


        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClear.setVisibility(TextUtils.isEmpty(s.toString().trim()) ? View.GONE : View.VISIBLE);
            }
        });


    }


    public void setData(String title, String subNum, String tip) {
        tvTitle.setText(title);
        tvSubCount.setText(subNum);
        tvTips.setText(tip);
    }


    @Override
    public void onClick(View v) {
        String phone = etPhoneNumber.getText().toString();
        String verificationCode = etVerificationCode.getText().toString();
        switch (v.getId()) {
            case R.id.ivClose:
                KeyboardUtils.hideSoftInput(etPhoneNumber);
                dismiss();
                break;

            case R.id.ivClear:
                etPhoneNumber.setText(null);
                break;
            case R.id.tvConfirm:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }


                if (TextUtils.isEmpty(verificationCode)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }


                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onConfirmClick(phone, verificationCode);
                }
                KeyboardUtils.hideSoftInput(etPhoneNumber);
//                dismiss();


                break;

            case R.id.tvGetVerificationCode:

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }

                if (mOnVerificationCodeListener != null) {
                    mOnVerificationCodeListener.onGetCodeClick(phone);
                }
                smsButtonUtil.startCountDown();
                break;
        }
    }

    public String getPhone() {
        return etPhoneNumber.getText().toString();
    }
}
