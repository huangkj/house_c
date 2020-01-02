package com.easylife.house.customer.ui.houses.housesdetail.comment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.addphoto.GlideImageLoader;
import com.easylife.house.customer.view.addphoto.ImagePickerAdapter;
import com.easylife.house.customer.view.addphoto.SelectDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_HOUSE_DETAIL_COMMENT;

/**
 * 楼盘评价
 */
public class CommentActivity extends MVPBaseActivity<CommentContract.View, CommentPresenter> implements
        CommentContract.View, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    @Bind(R.id.rb_price)
    MaterialRatingBar rbPrice;
    @Bind(R.id.rb_local)
    MaterialRatingBar rbLocal;
    @Bind(R.id.rb_des)
    MaterialRatingBar rbDes;
    @Bind(R.id.rb_traffic)
    MaterialRatingBar rbTraffic;
    @Bind(R.id.rb_env)
    MaterialRatingBar rbEnv;
    @Bind(R.id.et_comment_content)
    EditText etCommentContent;
    @Bind(R.id.tv_source)
    TextView tvSource;
    @Bind(R.id.btn_commit)
    TextView btnCommit;

    private float rbPriceNum = 3.0f;
    private float rbLocalNum = 3.0f;
    private float rbDesNum = 3.0f;
    private float rbTrafficNum = 3.0f;
    private float rbEnvNum = 3.0f;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private String project_id;
//    private String avGscores;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_comment, null);
    }

    public static void startActivity(Activity activity, String projectId, int reqestCode) {
        activity.startActivityForResult(new Intent(activity, CommentActivity.class)
                        .putExtra("projectId", projectId)
                , reqestCode
        );
    }

    @Override
    protected void initView() {
        hideNoNetView();
        project_id = getIntent().getStringExtra("projectId");
//        avGscores = getIntent().getStringExtra("AVGscores");

        initImagePicker();
        initWidget();
        rbPrice.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rbPriceNum = rating;
                sumRatingSource(rbPriceNum, rbLocalNum, rbDesNum, rbTrafficNum, rbEnvNum);
            }
        });
        rbLocal.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rbLocalNum = rating;
                sumRatingSource(rbPriceNum, rbLocalNum, rbDesNum, rbTrafficNum, rbEnvNum);
            }
        });
        rbDes.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rbDesNum = rating;
                sumRatingSource(rbPriceNum, rbLocalNum, rbDesNum, rbTrafficNum, rbEnvNum);
            }
        });
        rbTraffic.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rbTrafficNum = rating;
                sumRatingSource(rbPriceNum, rbLocalNum, rbDesNum, rbTrafficNum, rbEnvNum);
            }
        });
        rbEnv.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rbEnvNum = rating;
                sumRatingSource(rbPriceNum, rbLocalNum, rbDesNum, rbTrafficNum, rbEnvNum);
            }
        });
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
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 计算评价评分
     *
     * @param rbPriceNum
     * @param rbLocalNum
     * @param rbDesNum
     * @param rbTrafficNum
     * @param rbEnvNum
     */
    public void sumRatingSource(float rbPriceNum, float rbLocalNum, float rbDesNum, float rbTrafficNum, float rbEnvNum) {

        float sum = (rbPriceNum + rbLocalNum + rbDesNum + rbTrafficNum + rbEnvNum) / 5;
        String source = (sum + "").substring(0, 3);
        tvSource.setText(source);

    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("我要评价");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onItemClick(View view, int position) {

        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
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
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(CommentActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(CommentActivity.this, ImageGridActivity.class);
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
    }

    @OnClick({R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            //提交评价
            case R.id.btn_commit:
                try {
                    List<ImageBean> imgList = new ArrayList<>();
                    if (selImageList.size() != 0) {
                        for (ImageItem item : selImageList) {
                            ImageBean bean = new ImageBean();
                            bean.name = item.name;
                            bean.pic = CustomerUtils.base64Recode(item.path);
                            imgList.add(bean);
                        }
                    }
                    if (TextUtils.isEmpty(etCommentContent.getText().toString().trim()) || Float.parseFloat(tvSource.getText().toString().trim()) < 0.1) {
                        CustomerUtils.showTip(this, "请填写评价内容或评分");
                    } else {
                        showLoading();
                        mPresenter.uploadImage(imgList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        if (dialog.isShowing()) {
            cancelLoading();
        }
        UserCommentBean commentBean = new UserCommentBean();
        if (imageResultList != null && imageResultList.size() > 0) {
            for (ImageResult.DataBean bean : imageResultList) {
                UserCommentBean.ReviewimgBean reviewimgBean = new UserCommentBean.ReviewimgBean();
                reviewimgBean.url = bean.url;
                commentBean.reviewimg.add(reviewimgBean);
            }
        }

        String content = etCommentContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            if (content.length() < 15) {
                CustomerUtils.showTip(getApplicationContext(), "评价不能少于15字");
                return;
            } else {
                commentBean.content = content;
            }
        } else {
            CustomerUtils.showTip(getApplicationContext(), "评价内容不能为空");
        }
        Customer customer = dao.getCustomer();

        commentBean.headImg = customer.headimg;
        commentBean.userName = customer.username;
        commentBean.userId = customer.id;
        if (!TextUtils.isEmpty(project_id)) {
            commentBean.projectId = project_id;
        }

        if (!TextUtils.isEmpty(tvSource.getText().toString().trim())) {
            commentBean.AVGscores = tvSource.getText().toString().trim();
        } else {
            commentBean.AVGscores = "0";
        }
        commentBean.scoreDistrict = rbLocalNum + "";
        commentBean.scoreEnvi = rbEnvNum + "";
        commentBean.scorePrice = rbPriceNum + "";
        commentBean.scoreTraffic = rbTrafficNum + "";
        commentBean.scoreMating = rbDesNum + "";
        mPresenter.commitComment(commentBean);

    }

    /**
     * 图片上传失败
     */
    @Override
    public void showUploadFail() {
        CustomerUtils.showTip(getApplicationContext(), "请求失败");
    }

    @Override
    public void showCommitComment() {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        CustomerUtils.showTip(getApplicationContext(), "提交成功");
        setResult(RESULT_OK);
        finish();
        EventBus.getDefault().post(new MessageEvent(UPDATE_HOUSE_DETAIL_COMMENT));
    }
}
