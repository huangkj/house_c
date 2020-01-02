package com.easylife.house.customer.ui.payment.refundinfoupdate;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.ParamRefundInfo;
import com.easylife.house.customer.bean.ResultRefund;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.payment.RefundRateActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.addphoto.GlideImageLoader;
import com.easylife.house.customer.view.addphoto.ImagePickerDelAdapter;
import com.easylife.house.customer.view.addphoto.SelectDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


/**
 * 提交退款申请材料
 */
public class RefundInfoUpdateActivity extends MVPBaseActivity<RefundInfoUpdateContract.View, RefundInfoUpdatePresenter> implements RefundInfoUpdateContract.View, ImagePickerDelAdapter.OnRecyclerViewItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerDelAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.order_activity_refund_info_update, null);
    }

    public static void startActivity(Activity activity, String orderCode, ResultRefund resultRefund, ParamRefundInfo param, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RefundInfoUpdateActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("resultRefund", resultRefund)
                        .putExtra("param", param)
                , requestCode);
    }

    private String orderCode;
    private ResultRefund resultRefund;
    private TextView textDesc;
    private ParamRefundInfo param;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        resultRefund = (ResultRefund) getIntent().getSerializableExtra("resultRefund");
        param = (ParamRefundInfo) getIntent().getSerializableExtra("param");

        resultRefund = new ResultRefund();
        if (resultRefund == null) {
            resultRefund.isOvertime = false;
            resultRefund.isOnline = false;
            resultRefund.isPurchase = false;
        }

        textDesc = (TextView) findViewById(R.id.textDesc);

        textDesc.setText(Html.fromHtml("请上传<font   color=\"#FF6800\">" +
                "pos支付小票、优惠凭证等" +
                "</font>，信息越详细，越易于退款审核。建议上传jpg、png图片，大小1M以内"));
        if (resultRefund.isPurchase) {
            // 是认购
            if (resultRefund.isOnline) {
                // 线上支付
                if (resultRefund.isOvertime) {
                    // 已超时 - 资料上传（退房确认函） - 提交成功 - 跳转退款进度页面
                    textDesc.setText(Html.fromHtml("请上传<font   color=\"#FF6800\">" +
                            "退房确认函" +
                            "</font>，信息越详细，越易于退款审核。建议上传jpg、png图片，大小1M以内"));
                } else {
                    // 未超时 - 资料上传（退房确认函） - 提交成功 - 跳转退款进度页面
                    textDesc.setText(Html.fromHtml("请上传<font   color=\"#FF6800\">" +
                            "退房确认函" +
                            "</font>，信息越详细，越易于退款审核。建议上传jpg、png图片，大小1M以内"));
                }
            } else {
                //　线下支付 - 卡信息填写 - 资料上传（pos小票、优惠书、退房确认函） - 提交成功 - 跳转退款进度页面
                textDesc.setText(Html.fromHtml("请上传<font   color=\"#FF6800\">" +
                        "pos支付小票、优惠凭证、退房确认函等" +
                        "</font>，信息越详细，越易于退款审核。建议上传jpg、png图片，大小1M以内"));
            }
        } else {
            // 非认购
            if (resultRefund.isOnline) {
                // 线上
                if (resultRefund.isOvertime) {
                    // 超时 - 卡信息填写 - 提交成功 - 跳转退款进度页面
                } else {
                    // 未超时 - 提交成功 - 跳转退款进度页面
                }
            } else {
                // 线下 - 卡信息填写 - 资料上传（pos小票、优惠书） - 提交成功 - 跳转退款进度页面
                textDesc.setText(Html.fromHtml("请上传<font   color=\"#FF6800\">" +
                        "pos支付小票、优惠凭证等" +
                        "</font>，信息越详细，越易于退款审核。建议上传jpg、png图片，大小1M以内"));
            }
        }

        hideNoNetView();

        initImagePicker();
        initWidget();
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerDelAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.icon_im);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 客服IM
                if (dao.isLogin()) {
                    ToastUtils.showShort("im已移除");
//                    startActivity(new Intent(RefundInfoUpdateActivity.this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
                } else {
                    startActivityForResult(new Intent(RefundInfoUpdateActivity.this, LoginByVerifyCodeActivity.class), 1);
                }
            }
        });
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    private int count;

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.iv_img:

                switch (position) {
                    case IMAGE_ITEM_ADD:
                        List<String> names = new ArrayList<>();
                        names.add("拍照");
                        names.add("相册");
                        count = maxImgCount - selImageList.size();
                        showDialog(new SelectDialog.SelectDialogListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0: // 直接调起相机
                                        /**
                                         * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                         *
                                         * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                         *
                                         * 如果实在有所需要，请直接下载源码引用。
                                         */
                                        //打开选择,本次允许选择的数量
                                        ImagePicker.getInstance().setSelectLimit(count);
                                        Intent intent = new Intent(activity, ImageGridActivity.class);
                                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                                        break;
                                    case 1:
                                        //打开选择,本次允许选择的数量
                                        ImagePicker.getInstance().setSelectLimit(count);
                                        Intent intent1 = new Intent(activity, ImageGridActivity.class);
                                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }, names);


                        break;
                    default:
                        //打开预览
                        Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                        break;
                }
                break;
            case R.id.imgDelete:
                if (selImageList != null)
                    selImageList.remove(position);
                adapter.setImages(selImageList);
                break;
        }
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    private PubTipDialog tipSubmit;

    private void showTipSubmit(final List<ImageBean> imgList) {
        if (imgList == null) {
            CustomerUtils.showTip(this, "请上传图片资料");
            return;
        }
        if (dialog == null) {
            tipSubmit = new PubTipDialog(this, new PubTipDialog.InsideListener() {

                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        showLoading();
                        mPresenter.uploadImage(imgList);
                    }
                }
            });
        }
        tipSubmit.showdialog("提示", "确认上传退款材料图片" + imgList.size() + "张。", null, "取消", "确认");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @OnClick({R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                //提交评价 - 判断是否添加图片 - 提示确定上传 - 提交退款申请

                List<ImageBean> imgList = new ArrayList<>();
                if (selImageList.size() != 0) {
                    for (ImageItem item : selImageList) {
                        ImageBean bean = new ImageBean();
                        bean.name = item.name;
                        bean.pic = CustomerUtils.base64Recode(item.path);
                        imgList.add(bean);
                    }
                    showTipSubmit(imgList);
                } else {
                    CustomerUtils.showTip(this, "请上传图片资料");
                }
                break;
        }
    }

    /**
     * 图片上传成功
     *
     * @param imageResultList
     */
    @Override
    public void showUploadSuccess(List<ImageResult.DataBean> imageResultList) {
        cancelLoading();
        if (param == null) {
            showTip("参数异常，请重试");
            return;
        }
//        CustomerUtils.showTip(getApplicationContext(), "图片上传成功");
        List<ImageResult.DataBean> images = new ArrayList<>();
        for (ImageResult.DataBean img : imageResultList) {
            if ("1000".equals(img.code)) {
                images.add(img);
            }
        }
        String[] imgs = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            ImageResult.DataBean image = images.get(i);
            imgs[i] = image.url;
        }
        param.img = imgs;
        mPresenter.submit(orderCode, param);
    }

    /**
     * 图片上传失败
     */
    @Override
    public void showUploadFail() {
        CustomerUtils.showTip(getApplicationContext(), "图片上传失败");
    }

    @Override
    public void showSubmitSuccess() {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        CustomerUtils.showTip(getApplicationContext(), "提交成功");
        RefundRateActivity.startActivity(activity, orderCode, 3);
    }

}
