package com.easylife.house.customer.ui.mine.card;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BackIdCardIdentificationResult;
import com.easylife.house.customer.bean.FrontIdCardIdentificationResult;
import com.easylife.house.customer.bean.IdCardImageBase64;
import com.easylife.house.customer.bean.IdCardOcrRequestBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.util.BitmapTool;
import com.easylife.house.customer.util.FileUtils;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.view.ActionSheetDialog;
import com.easylife.house.customer.view.ButtonTouch;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_BIND_BANK_CARD;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_IDCARD_IDENTIFICATION;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_SET_TRADE_PSW;
import static com.easylife.house.customer.util.FileUtils.SDPATH;

/**
 * 身份认证 hkj
 */
@RuntimePermissions
public class IDCardIdentificationActivity extends BaseActivity {


    @Bind(R.id.tvTextIdCard)
    TextView tvTextIdCard;
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

    /**
     * 证件照-正面-选择图片
     */
    private final int REQUST_CODE_FRONT_IMAGE = 0;
    /**
     * 证件照-正面-照相
     */
    private final int REQUST_CODE_FRONT_CAMEARA = 1;
    /**
     * 证件照-反面-选择照片
     */
    private final int REQUST_CODE_BACK_IMAGE = 2;
    /**
     * 证件照-反面-照相
     */
    private final int REQUST_CODE_BACK_CAMEARA = 3;
    private static final int REQUST_CODE_NEXT_ACTIVITY = 10;
    private final String SIDE_FRONT = "face";
    private final String SIDE_BACK = "back";


    //handler start
    private static final int IMG_TRANSFORM_BASE64_COMPLETE = 0;
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
     * 正反面都成功
     */
    private static final int BOTH_IDENTIFICATIO_SUCC = 5;
    //handler end


    // 正面照
    private boolean isHaveImageFront;
    private Uri dataUriFront;
    private String imageNameFront;
    private boolean isCameraFront;
    private String picPathFront;
    /**
     * 正面是否识别成功
     */
    private boolean isIdentificationFrontSucc;


    // 反面
    private boolean isHaveImageBack;
    private Uri dataUriBack;
    private String imageNameBack;
    private boolean isCameraBack;
    /**
     * 反面是否识别成功
     */
    private boolean isIdentificationBackSucc;
    private String picPathBack;
    private MaterialDialog materialDialog;
    /**
     * 正面识别结果
     */
    private FrontIdCardIdentificationResult frontIdCardIdentificationResult;
    /**
     * 反面识别结果
     */
    private BackIdCardIdentificationResult backIdCardIdentificationResult;

    private static int mRequestCode;

    public static void startActivity(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, IDCardIdentificationActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_idcard_identification, null);
    }

    @Override
    protected void initView() {
    }

    private MyHandler myHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        private MyHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IDCardIdentificationActivity activity = (IDCardIdentificationActivity) mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case FRONT_IDENTIFICATIO_FAIL://正面识别失败
                        activity.cancleLoading();
                        ToastUtils.showShort("身份证正面识别失败，请重新选择照片");
                        break;

                    case BACK_IDENTIFICATIO_FAIL://反面识别失败
                        activity.cancleLoading();
                        ToastUtils.showShort("身份证反面识别失败，请重新选择照片");
                        break;

                    case BOTH_IDENTIFICATIO_SUCC://正反面识别成功
                        activity.cancleLoading();
                        ToastUtils.showShort("识别成功");
                        IDCardNextActivity.startActivity(activity, activity.frontIdCardIdentificationResult.getName(),
                                activity.frontIdCardIdentificationResult.getNum(), activity.picPathFront, activity.picPathBack, mRequestCode);

                        break;


                }
            }
        }
    }

    private void idCardOcr(IdCardImageBase64 obj) {


        IdCardOcrRequestBean idCardOcrRequestFront = new IdCardOcrRequestBean();
        IdCardOcrRequestBean idCardOcrRequestBack = new IdCardOcrRequestBean();


        idCardOcrRequestFront.setImage(obj.base64PicFrontString);
        idCardOcrRequestFront.setConfigure(new IdCardOcrRequestBean.ConfigureBean(SIDE_FRONT));

        idCardOcrRequestBack.setImage(obj.base64PicBackString);
        idCardOcrRequestBack.setConfigure(new IdCardOcrRequestBean.ConfigureBean(SIDE_BACK));


        mDao.iDcardOcr(0, idCardOcrRequestFront, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                Log.d(TAG, "正面成功");
                frontIdCardIdentificationResult = GsonUtils.fromJson(response, FrontIdCardIdentificationResult.class);
                isIdentificationFrontSucc = true;
                if (isIdentificationBackSucc) {
                    myHandler.sendEmptyMessage(BOTH_IDENTIFICATIO_SUCC);
                }

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                Log.d(TAG, "正面失败");
                isIdentificationFrontSucc = false;
                myHandler.sendEmptyMessage(FRONT_IDENTIFICATIO_FAIL);


            }
        });


        mDao.iDcardOcr(0, idCardOcrRequestBack, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                Log.d(TAG, "反面成功");
                backIdCardIdentificationResult = GsonUtils.fromJson(response, BackIdCardIdentificationResult.class);
                isIdentificationBackSucc = true;
                if (isIdentificationFrontSucc) {
                    myHandler.sendEmptyMessage(BOTH_IDENTIFICATIO_SUCC);
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                Log.d(TAG, "反面失败");
                isIdentificationBackSucc = false;
                myHandler.sendEmptyMessage(BACK_IDENTIFICATIO_FAIL);
            }
        });
    }

    @Override
    public void showLoading() {
        materialDialog = new MaterialDialog.Builder(this).progress(true, 0).progressIndeterminateStyle(false).
                content("身份证图像识别中,请耐心等待").show();
    }

    @Override
    public void cancleLoading() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

    @OnClick({R.id.btnNextIdCard, R.id.ivIdCardFront, R.id.ivIdCardBack, R.id.tvUserBookIdCard})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextIdCard://提交

                if (TextUtils.isEmpty(picPathFront) || TextUtils.isEmpty(picPathBack)) {
                    ToastUtils.showShort("请选择身份证正反面");
                    return;
                }

                if (!cbAgreeIdCard.isChecked()) {
                    ToastUtils.showShort("请先阅读并同意《好生活用户协议》");
                    return;
                }


                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String base64PicFrontString = Base64Util.imageToBase64(picPathFront);
                        String base64PicBackString = Base64Util.imageToBase64(picPathBack);

                        IdCardImageBase64 idCardImageBase64 = new IdCardImageBase64(base64PicFrontString, base64PicBackString);
                        idCardOcr(idCardImageBase64);
                    }
                }).start();

                break;
            case R.id.ivIdCardFront://正面
                IDCardIdentificationActivityPermissionsDispatcher.choosePictureWithCheck(IDCardIdentificationActivity.this
                        , REQUST_CODE_FRONT_CAMEARA, REQUST_CODE_FRONT_IMAGE);
                break;
            case R.id.ivIdCardBack://反面
                IDCardIdentificationActivityPermissionsDispatcher.choosePictureWithCheck(IDCardIdentificationActivity.this
                        , REQUST_CODE_BACK_CAMEARA, REQUST_CODE_BACK_IMAGE);
                break;

            case R.id.tvUserBookIdCard:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;
        }
    }


    @Override
    protected void setActionBarDetail() {

    }


    /**
     * 选择图片
     *
     * @param
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void choosePicture(final int REQUST_CODE_CAMEARA,
                              final int REQUST_CODE_IMAGE) {
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
                                FileUtils.createDirs();
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (REQUST_CODE_CAMEARA == REQUST_CODE_FRONT_CAMEARA || REQUST_CODE_IMAGE == REQUST_CODE_FRONT_IMAGE) {
                                    imageNameFront = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                                    FileUtils.createDirs();
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(
                                                    SDPATH,//把图片保存到/sdcard/jinfu文件夹
                                                    imageNameFront)));
                                } else if (REQUST_CODE_CAMEARA == REQUST_CODE_BACK_CAMEARA || REQUST_CODE_IMAGE == REQUST_CODE_BACK_IMAGE) {
                                    imageNameBack = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(
                                                    SDPATH,//把图片保存到/sdcard/jinfu文件夹
                                                    imageNameBack)));
                                }

                                startActivityForResult(intentFromCapture,
                                        REQUST_CODE_CAMEARA);
                            }
                        })
                .addSheetItem("相	册", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        REQUST_CODE_IMAGE);

                            }
                        }).show();
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
        IDCardIdentificationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUST_CODE_FRONT_CAMEARA:// 正面-照相
                    isCameraFront = true;
                    isHaveImageFront = true;
                    picPathFront = SDPATH + imageNameFront;
                    Glide.with(this).load(picPathFront).into(ivIdCardFront);

                    break;
                case REQUST_CODE_FRONT_IMAGE:// 正面-相册中选择
                    if (null != data) {
                        isHaveImageFront = true;
                        isCameraFront = false;
                        dataUriFront = data.getData();
                        picPathFront = BitmapTool.getImageAbsolutePath(this, dataUriFront);
                        Glide.with(this).load(picPathFront).into(ivIdCardFront);
                    }

                    break;

                case REQUST_CODE_BACK_CAMEARA:// 反面-照相
                    isCameraBack = true;
                    isHaveImageBack = true;
                    picPathBack = SDPATH + imageNameBack;
                    Glide.with(this).load(picPathBack).into(ivIdCardBack);
                    break;
                case REQUST_CODE_BACK_IMAGE:// 反面-相册中选择
                    if (null != data) {
                        isCameraBack = false;
                        isHaveImageBack = true;
                        dataUriBack = data.getData();
                        picPathBack = BitmapTool.getImageAbsolutePath(this, dataUriBack);
                        Glide.with(this).load(picPathBack).into(ivIdCardBack);
                    }

                    break;
                case REQUST_CODE_NEXT_ACTIVITY:
                    //身份证认证提交成功
                    setResult(RESULT_OK);
                    finish();
                    break;

                case REQUEST_CDOE_IDCARD_IDENTIFICATION:
                    //身份证认证提交成功
                    setResult(RESULT_OK);
                    finish();
                    break;
                case REQUEST_CDOE_SET_TRADE_PSW:
                    //身份证认证提交成功
                    setResult(RESULT_OK);
                    finish();
                    break;

                case REQUEST_CDOE_BIND_BANK_CARD:
                    //身份证认证提交成功
                    setResult(RESULT_OK);
                    finish();
                    break;
            }

        }
    }

}
