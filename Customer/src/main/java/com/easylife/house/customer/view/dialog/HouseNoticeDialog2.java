package com.easylife.house.customer.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.easylife.house.customer.R;

public class HouseNoticeDialog2 extends Dialog implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvTips;
    private TextView tvSubCount;
    private HouseNoticeDialog2.OnConfirmClickListener mOnConfirmClickListener;
    private TextView tvPhoneNumber;

    public HouseNoticeDialog2(@NonNull Context context) {
        super(context, R.style.dialog);
        init();
    }


    public interface OnConfirmClickListener {
        void onConfirmClick(String phone);
    }

    public void setOnConfirmClickListener(HouseNoticeDialog2.OnConfirmClickListener l) {
        mOnConfirmClickListener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.house_notice_dialog2, null);
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
        tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
        tvTips = (TextView) contentView.findViewById(R.id.tvTips);
        tvSubCount = (TextView) contentView.findViewById(R.id.tvSubCount);
        tvPhoneNumber = (TextView) contentView.findViewById(R.id.tvPhoneNumber);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tvConfirm);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ivClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

    }

    public void setData(String title, String subNum, String tip, String phone) {
        tvTitle.setText(title);
        tvSubCount.setText(subNum);
        tvTips.setText(tip);
        tvPhoneNumber.setText(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                dismiss();
                break;

            case R.id.tvConfirm:
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onConfirmClick(tvPhoneNumber.getText().toString());
                }
                break;
        }
    }

    public String getPhone() {
        return tvPhoneNumber.getText().toString();
    }
}
