package com.easylife.house.customer.ui.mine.card;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardOcrRequestBean;
import com.easylife.house.customer.bean.BankCardOcrResult;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.util.BitmapTool;
import com.easylife.house.customer.util.FileToBytesUtil;
import com.easylife.house.customer.util.FileUtils;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.ActionSheetDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.util.FileUtils.SDPATH;

/**
 * 绑定银行卡 hkj
 */
@RuntimePermissions
public class BindBankCardActivity extends BaseActivity {

    @Bind(R.id.tvUserNameTip)
    TextView tvUserNameTip;
    @Bind(R.id.ivbankCardFront)
    ImageView ivbankCardFront;
    @Bind(R.id.ivbankCardBack)
    ImageView ivbankCardBack;
    @Bind(R.id.llImagebankCard)
    LinearLayout llImagebankCard;
    @Bind(R.id.cbAgreebankCard)
    CheckBox cbAgreebankCard;
    @Bind(R.id.tvUserBookbankCard)
    TextView tvUserBookbankCard;
    @Bind(R.id.btnNextbankCard)
    ButtonTouch btnNextbankCard;

    // 正面照
    private boolean isHaveImageFront;
    private Uri dataUriFront;
    private String imageNameFront;
    private String picPathFront;
    // 反面照
    private boolean isHaveImageBack;
    private Uri dataUriBack;
    private String imageNameBack;
    private String picPathBack;

    /**
     * 正面识别结果
     */
    private static final int FRONT_IDENTIFICATIO_SUCC = 1;
    private static final int FRONT_IDENTIFICATIO_FAIL = 2;
    /**
     * 反面识别结果
     */
    private static final int BACK_IDENTIFICATIO_SUCC = 3;
    private static final int BACK_IDENTIFICATIO_FAIL = 4;
    /**
     * 证件照-正面-选择图片
     */
    private final int REQUEST_CODE_FRONT_IMAGE = 0;
    /**
     * 证件照-正面-照相
     */
    private final int REQUEST_CODE_FRONT_CAMEARA = 1;
    /**
     * 证件照-反面-选择图片
     */
    private final int REQUEST_CODE_BACK_IMAGE = 2;
    /**
     * 证件照-反面-照相
     */
    private final int REQUEST_CODE_BACK_CAMEARA = 3;

    private final int REQUEST_CODE_ADD_BANK = 111;

    private BindBankCardActivity.MyHandler myHandler = new BindBankCardActivity.MyHandler(this);
    private BankCardOcrResult bankCardOcrResult;


    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, BindBankCardActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        private MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BindBankCardActivity activity = (BindBankCardActivity) mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case FRONT_IDENTIFICATIO_FAIL://正面识别失败
                    case BACK_IDENTIFICATIO_FAIL://反面识别失败
                        activity.cancleLoading();
                        ToastUtils.showShort("银行卡信息识别失败，请重新上传");
                        break;
                    case FRONT_IDENTIFICATIO_SUCC://正面成功
                        //                        activity.bankCardOcr(false); // 反面不识别，只上传
                        activity.uploadImgs();
                        break;
                    case BACK_IDENTIFICATIO_SUCC://反面成功
                        // 上传图片
                        activity.uploadImgs();
                        break;
                }
            }
        }

    }

    public void uploadImgs() {
        List<ImageBean> beanList = new ArrayList<>();
        ImageBean imgFont = new ImageBean();
        imgFont.name = imageNameFront;
        imgFont.pic = encodeImage(picPathFront);
        beanList.add(imgFont);
        ImageBean imgBack = new ImageBean();
        imgBack.name = imageNameBack;
        imgBack.pic = encodeImage(picPathBack);
        beanList.add(imgBack);
        mDao.updateImg(0, beanList, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                LogOut.d("图片上传", response);
                ImageResult resultList = new Gson().fromJson(response, ImageResult.class);
                if (resultList.data == null) {
                    return;
                }
                String bankImgFont = "";
                String bankImgBack = "";
                for (ImageResult.DataBean data : resultList.data) {
                    if ("1000".equals(data.code)) {
                        if (imageNameFront.equals(data.name)) {
                            bankImgFont = data.url;
                        }
                        if (imageNameBack.equals(data.name)) {
                            bankImgBack = data.url;
                        }
                    }
                }
                if (TextUtils.isEmpty(bankImgFont) || TextUtils.isEmpty(bankImgBack)) {
                    ToastUtils.showShort("银行卡图片上传失败，请重试");
                    return;
                }
                AddBankCardActivity.startActivity(activity, bankCardOcrResult.getCard_num(), bankImgFont,
                        bankImgBack, REQUEST_CODE_ADD_BANK);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
            }
        });
    }

    private String encodeImage(String image) {
        byte[] bytes = FileToBytesUtil.File2byte(image);
        return Base64Util.encode(bytes);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_bind_bank_card, null);
    }

    @Override
    protected void initView() {
        tvUserNameTip.setText(mDao.getCustomer().realName);
    }

    @Override
    protected void setActionBarDetail() {
    }

    private AlertDialog tipDialog;

    @OnClick({R.id.tvTipBack, R.id.btnNextbankCard, R.id.ivbankCardFront, R.id.ivbankCardBack, R.id.tvUserBookbankCard})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvTipBack:
                // 银行卡反面提示弹窗
                if (tipDialog == null) {
                    final View tipView = LayoutInflater.from(this).inflate(R.layout.tip_bankcard_back, null);
                    tipView.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tipDialog.dismiss();
                        }
                    });
                    tipDialog = new AlertDialog.Builder(this)
                            .setView(tipView)
                            .create();
                }
                tipDialog.show();
                break;
            case R.id.btnNextbankCard:
                if (TextUtils.isEmpty(picPathFront)) {
                    ToastUtils.showShort("请选择银行卡正面照");
                    return;
                }
                if (TextUtils.isEmpty(picPathBack)) {
                    ToastUtils.showShort("请选择银行卡反面照");
                    return;
                }
                if (!cbAgreebankCard.isChecked()) {
                    ToastUtils.showShort("请先阅读并同意《好生活用户协议》");
                    return;
                }
                showLoading();
                bankCardOcr(true);
                break;
            case R.id.ivbankCardFront:
                choosePicture(REQUEST_CODE_FRONT_CAMEARA,
                        REQUEST_CODE_FRONT_IMAGE);
                break;
            case R.id.ivbankCardBack:
                choosePicture(REQUEST_CODE_BACK_CAMEARA,
                        REQUEST_CODE_BACK_IMAGE);
                break;
            case R.id.tvUserBookbankCard:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;
        }
    }

    public void bankCardOcr(final boolean isFont) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String base64PicString = Base64Util.imageToBase64(isFont ? picPathFront : picPathBack);
                BankCardOcrRequestBean bankCardOcrRequestBean = new BankCardOcrRequestBean();
                bankCardOcrRequestBean.setImage(base64PicString);
                mDao.bankCardOcr(0, bankCardOcrRequestBean, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        bankCardOcrResult = GsonUtils.fromJson(response, BankCardOcrResult.class);
                        myHandler.sendEmptyMessage(isFont ? FRONT_IDENTIFICATIO_SUCC : BACK_IDENTIFICATIO_SUCC);
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        myHandler.sendEmptyMessage(isFont ? FRONT_IDENTIFICATIO_FAIL : BACK_IDENTIFICATIO_FAIL);
                    }
                });
            }
        }).start();
    }

    /**
     * 选择图片
     *
     * @param
     */

    private void choosePicture(final int REQUEST_CODE_CAMERA, final int REQUEST_CODE_IMAGE) {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setTxtCancel()
                .setWhiteBackGroud()
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍	照", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                BindBankCardActivityPermissionsDispatcher.openCameraWithCheck(BindBankCardActivity.this
                                        , REQUEST_CODE_CAMERA, REQUEST_CODE_IMAGE);
                            }
                        })
                .addSheetItem("相	册", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                BindBankCardActivityPermissionsDispatcher.chooseImageWithCheck(BindBankCardActivity.this, REQUEST_CODE_IMAGE);

                            }
                        }).show();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void chooseImage(int REQUEST_CODE_IMAGE) {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery,
                REQUEST_CODE_IMAGE);
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera(final int REQUEST_CODE_CAMERA, final int REQUEST_CODE_IMAGE) {
        FileUtils.createDirs();
        if (REQUEST_CODE_CAMERA == REQUEST_CODE_FRONT_CAMEARA || REQUEST_CODE_IMAGE == REQUEST_CODE_FRONT_IMAGE) {
            imageNameFront = new Date().getTime() + "";
            FileUtils.createDirs();
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(SDPATH, imageNameFront))), REQUEST_CODE_CAMERA);
        } else {
            imageNameBack = new Date().getTime() + "";
            FileUtils.createDirs();
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(SDPATH, imageNameBack))), REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FRONT_CAMEARA:// 正面-照相
                    isHaveImageFront = true;
                    picPathFront = SDPATH + imageNameFront;
                    Glide.with(this).load(picPathFront).into(ivbankCardFront);
                    break;
                case REQUEST_CODE_FRONT_IMAGE:// 正面-相册中选择
                    if (null != data) {
                        imageNameFront = new Date().getTime() + "";
                        isHaveImageFront = true;
                        dataUriFront = data.getData();
                        picPathFront = BitmapTool.getImageAbsolutePath(this, dataUriFront);
                        Glide.with(this).load(picPathFront).into(ivbankCardFront);
                    }
                    break;
                case REQUEST_CODE_BACK_CAMEARA:// 反面-照相
                    isHaveImageBack = true;
                    picPathBack = SDPATH + imageNameBack;
                    Glide.with(this).load(picPathBack).into(ivbankCardBack);
                    break;
                case REQUEST_CODE_BACK_IMAGE:// 反面-相册中选择
                    if (null != data) {
                        imageNameBack = new Date().getTime() + "";
                        isHaveImageBack = true;
                        dataUriBack = data.getData();
                        picPathBack = BitmapTool.getImageAbsolutePath(this, dataUriBack);
                        Glide.with(this).load(picPathBack).into(ivbankCardBack);
                    }
                    break;
                case REQUEST_CODE_ADD_BANK:
                    finish();
                    break;
            }
        }
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
        BindBankCardActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
