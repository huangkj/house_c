package com.easylife.house.customer.ui.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.PaletteView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/7/4 17:19.
 * 描述：签章
 */

public class SignatureActivity extends BaseActivity implements RequestManagerImpl {


    public static void startActivity(Activity activity, String orderCode, String customerName, int requestCode) {
        activity.startActivityForResult(new Intent(activity, SignatureActivity.class)
                .putExtra("orderCode", orderCode)
                .putExtra("customerName", customerName), requestCode);
    }

    public static void startActivity(Fragment fragment, String orderCode, String customerName, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), SignatureActivity.class)
                .putExtra("orderCode", orderCode)
                .putExtra("customerName", customerName), requestCode);
    }

    @Bind(R.id.signatureView)
    PaletteView signatureView;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_signature, null);
    }

    private String orderCode;
    private String customerName;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        customerName = getIntent().getStringExtra("customerName");

        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        tvTitle.setText(customerName + "  " + date);
    }

    private PubTipDialog dialog;

    private void showTip() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, new PubTipDialog.InsideListener() {

                @Override
                public void note(boolean isOK) {
                    if (!isOK) {
                        finish();
                    }
                }
            });
        }
        dialog.showdialog("", "请在空白区域签字", null, "取消", "确定");
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.edit_clear);
    }

    private String signatureUrl;

    @OnClick({R.id.btnClear, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                signatureView.clear();
                break;
            case R.id.btnSave:
                File fileSave = signatureView.saveImage();
                if (fileSave == null) {
                    CustomerUtils.showTip(activity, "签字生成失败，请重试");
                } else {
                    // 上传签章图片,成功后绑定订单
                    mDao.updateImgSingle(1, new Date().getTime() + "", fileSave.getAbsolutePath(), this);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 图片上传成功
                ImageResult.DataBean data = mDao.analyzeUploadSingleImageResult(response);
                if (data == null)
                    return;
                signatureUrl = data.url;
                mDao.signature(2, orderCode, signatureUrl, this);
                break;
            case 2:
                // 签章绑定成功
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
