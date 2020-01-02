package com.easylife.house.customer.ui.mine.card;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemClickListener2;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.BankCardOcrRequestBean;
import com.easylife.house.customer.bean.BankCardOcrResult;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.JsonAddress;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.util.BitmapTool;
import com.easylife.house.customer.util.CityUtils;
import com.easylife.house.customer.util.FileToBytesUtil;
import com.easylife.house.customer.util.FileUtils;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.ActionSheetDialog;
import com.easylife.house.customer.view.ProvinceCitySelectPopWindow;
import com.easylife.house.customer.view.photoview.RoundedImageView;
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

import static com.easylife.house.customer.dao.ServerDao.UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.util.FileUtils.SDPATH;

/**
 * 银行卡详情
 */
@RuntimePermissions
public class BankCardDetailActivity extends BaseActivity {

    @Bind(R.id.ivbankCardFront)
    RoundedImageView ivbankCardFront;
    @Bind(R.id.ivbankCardBack)
    RoundedImageView ivbankCardBack;
    @Bind(R.id.btnNextbankCard)
    TextView btnNextbankCard;
    @Bind(R.id.tvBankCardNum)
    TextView tvBankCardNum;
    @Bind(R.id.tvBankAddress)
    TextView tvBankAddress;
    @Bind(R.id.tvBankSub)
    TextView tvBankSub;

    // 正面照
    private Uri dataUriFront;
    private String imageNameFront;
    private String picPathFront;
    // 反面照
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
    private final int REQUEST_CODE_FRONT_IMAGE = 1;
    /**
     * 证件照-正面-照相
     */
    private final int REQUEST_CODE_FRONT_CAMEARA = 2;
    /**
     * 证件照-反面-选择图片
     */
    private final int REQUEST_CODE_BACK_IMAGE = 3;
    /**
     * 证件照-反面-照相
     */
    private final int REQUEST_CODE_BACK_CAMEARA = 4;

    private final int REQUEST_CODE_ADD_BANK = 111;

    private MyHandler myHandler = new MyHandler(this);
    private BankCardOcrResult bankCardOcrResult;

    public static void startActivity(Activity activity, String bankId, int requestCode) {
        Intent intent = new Intent(activity, BankCardDetailActivity.class);
        intent.putExtra("bankId", bankId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_bank_card_detail, null);
    }

    @Override
    protected void initView() {
        bankId = getIntent().getStringExtra("bankId");

        mDao.bankCardDetail(1, bankId, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                bankCardDetail = new Gson().fromJson(response, BankCardListBean.class);
                if (bankCardDetail == null)
                    return;

                linkNumber = bankCardDetail.getLinkNumber();
                bankSubName = bankCardDetail.getOpenBranchBank();

                tvBankAddress.setText(bankCardDetail.getProvinceName() + " " + bankCardDetail.getCityName());
                String last4Num = bankCardDetail.getBankCardNum().substring(bankCardDetail.getBankCardNum().length() - 4);
                tvBankCardNum.setText(bankCardDetail.getBelongToBank() + "(" + last4Num + ")");
                tvBankSub.setText(bankCardDetail.getOpenBranchBank());

                if (!TextUtils.isEmpty(bankCardDetail.getBankImgFront())) {
                    picPathFront = bankCardDetail.getBankImgFront();
                    ivbankCardFront.setEnabled(false);
                    Glide.with(activity).load(bankCardDetail.getBankImgFront()).into(ivbankCardFront);
                }
                if (!TextUtils.isEmpty(bankCardDetail.getBankImgBack())) {
                    picPathBack = bankCardDetail.getBankImgBack();
                    Glide.with(activity).load(bankCardDetail.getBankImgBack()).into(ivbankCardBack);
                }

                new Handler().postDelayed(mRunnable, 100);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {

            }
        });
    }


    static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        private MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BankCardDetailActivity activity = (BankCardDetailActivity) mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case FRONT_IDENTIFICATIO_FAIL://正面识别失败
                    case BACK_IDENTIFICATIO_FAIL://反面识别失败
                        activity.cancleLoading();
                        ToastUtils.showShort("银行卡信息识别失败，请重新上传");
                        break;
                    case FRONT_IDENTIFICATIO_SUCC://正面成功
                        activity.cancleLoading();
//                        activity.bankCardOcr(false); // 反面不识别，只上传
                        activity.uploadImgs();
                        break;
                    case BACK_IDENTIFICATIO_SUCC://反面成功
                        activity.uploadImgs();
                        break;
                }
            }
        }
    }


    public void uploadImgs() {
        List<ImageBean> beanList = new ArrayList<>();
        if (TextUtils.isEmpty(bankCardDetail.getBankImgFront())) {
            ImageBean imgFont = new ImageBean();
            imgFont.name = imageNameFront;
            imgFont.pic = encodeImage(picPathFront);
            beanList.add(imgFont);
        }
        ImageBean imgBack = new ImageBean();
        imgBack.name = imageNameBack;
        imgBack.pic = encodeImage(picPathBack);
        beanList.add(imgBack);
        showLoading();
        mDao.updateImg(0, beanList, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                LogOut.d("图片上传结果", response);
                ImageResult resultList = new Gson().fromJson(response, ImageResult.class);
                if (resultList == null || resultList.data == null) {
                    return;
                }
                String bankImgFont = bankCardDetail.getBankImgFront();
                String bankImgBack = bankCardDetail.getBankImgBack();
                for (ImageResult.DataBean data : resultList.data) {
                    if ("1000".equals(data.code)) {
                        if (data.name.equals(imageNameFront)) {
                            bankImgFont = data.url;
                        }
                        if (data.name.equals(imageNameBack)) {
                            bankImgBack = data.url;
                        }
                    }
                }
                if (TextUtils.isEmpty(bankImgFont) || TextUtils.isEmpty(bankImgBack)) {
                    ToastUtils.showShort("银行卡图片上传失败，请重试");
                    return;
                }
                updateInfo(bankImgFont, bankImgBack);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
            }
        });
    }

    private String encodeImage(String image) {
        byte[] bytes = FileToBytesUtil.File2byte(image);
        return Base64Util.encode(bytes);
    }

    private void updateInfo(String bankImgFont, String bankImgBack) {
        showLoading();
        mDao.updateBankCardInfo(0, bankId, linkNumber, bankSubName,
                bankImgFont, bankImgBack, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        cancleLoading();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        cancleLoading();
                        ToastUtils.showShort(TextUtils.isEmpty(code.msg) ? "更新银行卡信息失败" : code.msg);
                    }
                });
    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("解除绑定");
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 解除绑定
                InputTraderPasswordActivity.startActivity(activity, bankCardDetail, UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD);
            }
        });
    }

    private boolean hasUpdateBackImage;
    private String bankId;
    private BankCardListBean bankCardDetail;
    private AlertDialog tipDialog;
    private String linkNumber, bankSubName;
    private List<JsonAddress.Data> dataProvince;
    private List<JsonAddress.Data> dataCity;
    private JsonAddress.Data province, city;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            dataProvince = new Gson().fromJson(CityUtils.getProvinceJson(activity), JsonAddress.class).RECORDS;
            dataCity = new Gson().fromJson(CityUtils.getCityJson(activity), JsonAddress.class).RECORDS;

            if (bankCardDetail != null && !TextUtils.isEmpty(bankCardDetail.getProvinceId())) {
                for (JsonAddress.Data p : dataProvince) {
                    if (bankCardDetail.getProvinceId().equals(p.provinceid)) {
                        province = p;
                        break;
                    }
                }
            }
            if (bankCardDetail != null && !TextUtils.isEmpty(bankCardDetail.getCityId())) {
                for (JsonAddress.Data c : dataCity) {
                    if (bankCardDetail.getCityId().equals(c.cityid)) {
                        city = c;
                        break;
                    }
                }
            }

            selectPopWindow = new ProvinceCitySelectPopWindow(activity, new ItemClickListener2<JsonAddress.Data>() {
                @Override
                public void itemClick(JsonAddress.Data data1, JsonAddress.Data data2) {
                    province = data1;
                    city = data2;
                    tvBankAddress.setText(province.province + city.city);
                }
            });
            selectPopWindow.setData(dataProvince, dataCity);
        }
    };

    private ProvinceCitySelectPopWindow selectPopWindow;
    private Dialog dialog;

    @OnClick({R.id.tvSubTip, R.id.tvBankAddress, R.id.tvBankSub, R.id.tvTipBack, R.id.btnNextbankCard, R.id.ivbankCardFront, R.id.ivbankCardBack})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubTip:
                if (dialog == null)
                    dialog = new android.app.AlertDialog.Builder(this)
                            .setMessage("1.您可拨打银行卡背面处客户电话查询。\n\n2.您可就近前往开户银行柜台查询。\n\n3.您可在银行开户行手机银行App在线查询")
                            .setNegativeButton("确定", null)
                            .create();
                dialog.show();
                break;
            case R.id.tvBankAddress:
                // 选择开户行地区
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
                            tvBankAddress.setText(province.province + city.city);
                        }
                    });
                    selectPopWindow.setData(dataProvince, dataCity);
                }
                showPopWindow2View(selectPopWindow, layActionBar);
                break;
            case R.id.tvBankSub:
                // 选择支行
                if (province == null || city == null) {
                    ToastUtils.showShort("请先选择开户地区");
                    new Handler().postDelayed(mRunnable, 100);
                    return;
                }
                SelectSubBankActivity.startActivity(activity, bankCardDetail.getBankCode(), province.provinceid, city.cityid, 6);
                break;
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
                if (TextUtils.isEmpty(bankCardDetail.getOpenBranchBank()) && province == null) {
                    ToastUtils.showShort("请选择开户地区");
                    return;
                }
                if (TextUtils.isEmpty(bankCardDetail.getLinkNumber()) && linkNumber == null) {
                    ToastUtils.showShort("请选择开户行支行");
                    return;
                }
                if (TextUtils.isEmpty(picPathFront)) {
                    ToastUtils.showShort("请选择银行卡正面照");
                    return;
                }
                if (TextUtils.isEmpty(picPathBack)) {
                    ToastUtils.showShort("请选择银行卡反面照");
                    return;
                }

                if (TextUtils.isEmpty(bankCardDetail.getBankImgFront())) {
                    // 2张图片都没有，扫描更新
                    showLoading();
                    bankCardOcr(true);
                } else if (hasUpdateBackImage) {
                    // 反面更新了图片，扫描更新
                    uploadImgs();
                } else {
                    // 都无变动
                    updateInfo(bankCardDetail.getBankImgFront(), bankCardDetail.getBankImgBack());
                }
                break;
            case R.id.ivbankCardFront:
                choosePicture(REQUEST_CODE_FRONT_CAMEARA,
                        REQUEST_CODE_FRONT_IMAGE);
                break;
            case R.id.ivbankCardBack:
                choosePicture(REQUEST_CODE_BACK_CAMEARA,
                        REQUEST_CODE_BACK_IMAGE);
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
                        if (isFont && !bankCardDetail.getBankCardNum().equals(bankCardOcrResult.getCard_num())) {
                            // 银行卡号不一致
                            cancleLoading();
                            ToastUtils.showShort("上传内容与添加银行卡号不一致，请重新上传");
                            return;
                        }
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

    private void choosePicture(final int REQUEST_CODE_CAMERA,
                               final int REQUEST_CODE_IMAGE) {
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
                                BankCardDetailActivityPermissionsDispatcher.openCameraWithCheck(BankCardDetailActivity.this, REQUEST_CODE_CAMERA, REQUEST_CODE_IMAGE);
                            }
                        })
                .addSheetItem("相	册", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                BankCardDetailActivityPermissionsDispatcher.chooseImageWithCheck(BankCardDetailActivity.this, REQUEST_CODE_IMAGE);
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
    public void openCamera(final int REQUEST_CODE_CAMERA,
                           final int REQUEST_CODE_IMAGE) {
        FileUtils.createDirs();
        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (REQUEST_CODE_CAMERA == REQUEST_CODE_FRONT_CAMEARA || REQUEST_CODE_IMAGE == REQUEST_CODE_FRONT_IMAGE) {
            imageNameFront = new Date().getTime() + "";
            FileUtils.createDirs();
            intentFromCapture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(SDPATH, imageNameFront)));
        } else {
            imageNameBack = new Date().getTime() + "";
            FileUtils.createDirs();
            intentFromCapture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(SDPATH, imageNameBack)));
        }

        startActivityForResult(intentFromCapture,
                REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FRONT_CAMEARA:// 正面-照相
                    picPathFront = SDPATH + imageNameFront;
                    Glide.with(this).load(picPathFront).into(ivbankCardFront);
                    break;
                case REQUEST_CODE_FRONT_IMAGE:// 正面-相册中选择
                    if (null != data) {
                        imageNameFront = new Date().getTime() + "";
                        dataUriFront = data.getData();
                        picPathFront = BitmapTool.getImageAbsolutePath(this, dataUriFront);
                        Glide.with(this).load(picPathFront).into(ivbankCardFront);
                    }
                    break;
                case REQUEST_CODE_BACK_CAMEARA:// 反面-照相
                    hasUpdateBackImage = true;
                    picPathBack = SDPATH + imageNameBack;
                    Glide.with(this).load(picPathBack).into(ivbankCardBack);
                    break;
                case REQUEST_CODE_BACK_IMAGE:// 反面-相册中选择
                    if (null != data) {
                        hasUpdateBackImage = true;
                        imageNameBack = new Date().getTime() + "";
                        dataUriBack = data.getData();
                        picPathBack = BitmapTool.getImageAbsolutePath(this, dataUriBack);
                        Glide.with(this).load(picPathBack).into(ivbankCardBack);
                    }
                    break;
                case 6:
                    // 选择开户支行
                    linkNumber = data.getStringExtra("id");
                    bankSubName = data.getStringExtra("bankBranchName");
                    tvBankSub.setText(bankSubName);
                    break;
                case UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD:
                    // 解除绑定成功
                    setResult(RESULT_OK);
                    finish();
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
        BankCardDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

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
