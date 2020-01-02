package com.easylife.house.customer.ui.pub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.easylife.house.customer.R;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.PhotoUtil;
import com.easylife.house.customer.view.photo.Item;
import com.easylife.house.customer.view.photo.PhotoAlbumActivity;

import java.io.File;
import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 通用的照片拍摄或选取照片 Author:heiyue Email:heiyue623@126.com 2015-5-16下午1:41:02
 */
@RuntimePermissions
public class TakePhotoActivity extends Activity {
    private Activity context;
    /**
     * 是否为多图选择
     */
    public static final String IS_CHOOSE_MANY = "is_choose_many";
    /**
     * 多图选择，当前选择的张数
     */
    public static final String CHOOSE_MANY_CURRENT_SIZE = "choose_many_current_count";
    /**
     * 是否需要裁剪，对拍照和单图有效
     */
    public static final String NEED_CROP = "need_crop";
    // 多图选择模式
    private boolean isChooseMany;
    // 是否需要裁剪
    private boolean needCrop;
    // 多图选择当前的张数
    private int chooseManyCurrentCout;
    /**
     * 返回结果，单图或拍照返回单张的路径
     */
    public static final String SINGLE_PHOTO_PATH = "single_photo_path";
    /**
     * 多图选择返回的多图路基String arrayList
     */
    public static final String MANY_PHOTO_PATH_ARR = "many_photo_path_arr";
    /**
     * 裁剪框的比例-宽
     */
    private int aspectX = 0;
    /**
     * 裁剪框的比例-高
     */
    private int aspectY = 0;
    /**
     * 输出的宽
     */
    private int outputX = 0;
    /**
     * 输出的高
     */
    private int outputY = 0;
    /**
     * 是否显示拍照这个选项，默认为：true
     */
    private boolean isShowCamera = true;
    public static final String key_isShowCamera = "isShowCamera";
    /**
     * 是否显示相册这个选项，默认为：true
     */
    private boolean isShowPhoto = true;
    public static final String key_isShowPhoto = "isShowPhoto";
    /**
     * 是否显示系统图库，默认为：false
     */
    private boolean isShowSystemPhoto = false;
    public static final String key_isShowSystemPhoto = "isShowSystemPhoto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pub_activity_take_photo);
        isChooseMany = getIntent().getBooleanExtra(IS_CHOOSE_MANY, false);
        chooseManyCurrentCout = getIntent().getIntExtra(
                CHOOSE_MANY_CURRENT_SIZE, 0);
        needCrop = getIntent().getBooleanExtra(NEED_CROP, false);

        isShowCamera = getIntent().getBooleanExtra(key_isShowCamera, true);
        isShowPhoto = getIntent().getBooleanExtra(key_isShowPhoto, true);
        isShowSystemPhoto = getIntent().getBooleanExtra(key_isShowSystemPhoto,
                false);

        aspectX = getIntent().getIntExtra("aspectX", 0);
        aspectY = getIntent().getIntExtra("aspectY", 0);
        outputX = getIntent().getIntExtra("outputX", 0);
        outputY = getIntent().getIntExtra("outputY", 0);

        findViewById(R.id.tvCamera).setVisibility(View.GONE);
        findViewById(R.id.tvPhoto).setVisibility(View.GONE);
        findViewById(R.id.tvSystemPhoto).setVisibility(View.GONE);
        if (isShowCamera) {
            findViewById(R.id.tvCamera).setVisibility(View.VISIBLE);
        }
        if (isShowPhoto) {
            findViewById(R.id.tvPhoto).setVisibility(View.VISIBLE);
        }
        if (isShowSystemPhoto) {

            findViewById(R.id.tvSystemPhoto).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.pop_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        context = this;
        initCacheDir();
        layoutBtns = findViewById(R.id.layoutBtns);
        // 拍照
        findViewById(R.id.tvCamera).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        TakePhotoActivityPermissionsDispatcher.getCameraWithCheck(TakePhotoActivity.this);
                    }
                });
        // 相册选择
        findViewById(R.id.tvPhoto).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TakePhotoActivityPermissionsDispatcher.getAlbumWithCheck(TakePhotoActivity.this);
                    }
                });
        // 取消
        findViewById(R.id.tvCancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getAlbum(){
        if (isChooseMany) {
            // 多图选择
            Intent intent = new Intent(context,
                    PhotoAlbumActivity.class);
            PhotoAlbumActivity.hasCount = chooseManyCurrentCout;
            startActivityForResult(intent, REQUEST_PHOTO_GARRY);
        } else {
            // 相册选择
            PhotoUtil.pickPhoto(context,
                    REQUEST_PHOTO_GARRY_SIGLE);
        }
        layoutBtns.setVisibility(View.GONE);
    }

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getCamera(){
        String currentPhotoName = PhotoUtil.createDefaultName();
        currentPhotoPath = IMG_DIR + currentPhotoName;
        PhotoUtil.takePhotoCustomerPath(context, IMG_DIR,
                currentPhotoName, REQUEST_PHOTO_CAMERA);
        layoutBtns.setVisibility(View.GONE);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TakePhotoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 获取单张图片
     *
     * @param context
     * @param requestCode
     * @param needCrop
     */
    public static void startActivityForSiglePhoto(Activity context,
                                                  int requestCode, boolean needCrop) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra(NEED_CROP, needCrop);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取单张图片
     *
     * @param context
     * @param requestCode
     * @param needCrop
     */
    public static void startActivityForSiglePhoto(Fragment context,
                                                  int requestCode, boolean needCrop) {
        Intent intent = new Intent(context.getActivity(),
                TakePhotoActivity.class);
        intent.putExtra(NEED_CROP, needCrop);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取单张图片
     *
     * @param context
     * @param isShowCamera      是否显示拍照按钮
     * @param isShowPhoto       是否显示相册按钮
     * @param isShowSystemPhoto 是否显示系统图片按钮--无论true还是false，都不会显示
     * @param requestCode
     */
    public static void startActivityForSiglePhoto(Activity context,
                                                  boolean isShowCamera, boolean isShowPhoto,
                                                  boolean isShowSystemPhoto, int requestCode) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra(key_isShowCamera, isShowCamera);
        intent.putExtra(key_isShowPhoto, isShowPhoto);
        // intent.putExtra(key_isShowSystemPhoto, isShowSystemPhoto);
        intent.putExtra(key_isShowSystemPhoto, false);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取单张图片,并可以裁剪
     *
     * @param context
     * @param aspectX     裁剪框的比例-宽
     * @param aspectY     裁剪框的比例-高
     * @param outputX     输出的宽
     * @param outputY     输出的高
     * @param requestCode
     */

    public static void startActivityForSiglePhoto(Activity context,
                                                  int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra(NEED_CROP, true);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        context.startActivityForResult(intent, requestCode);
    }

    // 拍照获取的

    private String currentPhotoPath;
    /**
     * 图片路径
     */
    private String IMG_DIR;

    /**
     * 初始化缓存空间
     */
    private void initCacheDir() {
        IMG_DIR = CacheManager.getImgDir(context);
    }

    public static final int REQUEST_PHOTO_CAMERA = 1; // 拍照
    public static final int REQUEST_PHOTO_GARRY = 2; // 相册选择
    public static final int REQUEST_PHOTO_GARRY_SIGLE = 3; // 选择单张照片
    public static final int REQUEST_CROP = 4; // 裁剪
    public static final int REQUEST_SYSTEM_IMG = 5; // 系统图库选择

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PHOTO_GARRY:
                    // 图片多选
                    if (data != null) {
                        ArrayList<Item> items = data
                                .getParcelableArrayListExtra(PhotoAlbumActivity.RESULT_FILES);
                        if (items != null && items.size() > 0) {
                            int size = items.size();
                            pickImgsCount = size;
                            ZoomListener zoomListener = new ZoomListener(true);
                            for (int i = 0; i < size; i++) {
                                // 批量图片压缩,图片执行压缩更多的是因为选取的返回的图片是翻转的
                                Item item = items.get(i);
                                // imageAdapter.add(new File(item.getPhotoPath()));
                                PhotoUtil
                                        .zoomImage(context, Uri.fromFile(new File(
                                                        item.getPhotoPath())), IMG_DIR
                                                        + "uploadimg_" + i + "_"
                                                        + PhotoUtil.createDefaultName(),
                                                Constants.IMG_ZOOM_WIDTH_MAX,
                                                Constants.IMG_ZOOM_HEIGHT_MAX,
                                                Constants.IMG_ZOOM_QUALITY,
                                                zoomListener, true);
                            }
                        }
                    }
                    break;
                case REQUEST_PHOTO_GARRY_SIGLE:
                    // 单图选择
                    Uri photoPath = PhotoUtil.getPhotoPath(context, data);
                    if (photoPath != null) {
                        zoomImageSingle(photoPath.getPath());
                    }
                    break;
                case REQUEST_PHOTO_CAMERA:
                    // 拍照
                    if (currentPhotoPath != null) {
                        zoomImageSingle(currentPhotoPath);
                    }
                    break;
                case REQUEST_CROP:
                    // 裁剪成功
                    onSingChooseSuccess();
                    break;
                case REQUEST_SYSTEM_IMG:
                    // 获取系统图库成功
                    Intent intent = new Intent();
                    // 标识为系统图库
                    intent.putExtra("type", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
        } else {
            finish();
        }
    }

    /**
     * 压缩单张照片
     */
    private void zoomImageSingle(String oldPath) {
        Uri path = Uri.fromFile(new File(oldPath));
        PhotoUtil.zoomImage(
                context,
                path,
                CacheManager.getImgDir(context) + "uploadimg" + "_"
                        + PhotoUtil.createDefaultName(),
                Constants.IMG_ZOOM_WIDTH_MAX, Constants.IMG_ZOOM_HEIGHT_MAX,
                Constants.IMG_ZOOM_QUALITY, new ZoomListener(false), true);
    }

    // 图片选择的总张数
    public int pickImgsCount;
    // 成功压缩的张数
    public int successZoomSize;
    ArrayList<String> manyPaths = new ArrayList<String>();
    private View layoutBtns;

    /**
     * 压缩监听 Author:heiyue Email:heiyue623@126.com 2015-4-14下午4:45:26
     */
    class ZoomListener implements PhotoUtil.ImageZoomCallBack {
        // 多图选择标识
        private boolean isMany;

        public ZoomListener(boolean isMany) {
            this.isMany = isMany;
        }

        @Override
        public void onImgZoomStart() {

        }

        @Override
        public synchronized void onImgZoomSuccess(String newPath) {
            if (isMany) {
                // 多图业务执行
                successZoomSize++;
                manyPaths.add(newPath);
                // 最后一个压缩成功
                if (pickImgsCount > 0 && successZoomSize == pickImgsCount) {
                    // 并且不满最大值
                    successZoomSize = 0;
                    pickImgsCount = 0;
                    // 执行完成
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(MANY_PHOTO_PATH_ARR,
                            manyPaths);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                currentPhotoPath = newPath;
                if (needCrop) {
                    // 需要裁剪,裁剪的时候注意 有点手机不支持保存路径和路径一致
                    currentPhotoPath = CacheManager.getImgDir(context)
                            + PhotoUtil.createDefaultName();
                    LogOut.d("裁剪前：", "原路径：" + newPath + ",新路径："
                            + currentPhotoPath);

                    if (aspectX == 0 || aspectY == 0) {
                        PhotoUtil.openCropImage(context,
                                Uri.fromFile(new File(newPath)),
                                Uri.fromFile(new File(currentPhotoPath)), 400,
                                400, REQUEST_CROP);

                    } else {
                        PhotoUtil.openCropImage(context,
                                Uri.fromFile(new File(newPath)),
                                Uri.fromFile(new File(currentPhotoPath)),
                                aspectX, aspectY, outputX, outputY,
                                REQUEST_CROP);

                    }

                } else {
                    // 单图业务执行
                    onSingChooseSuccess();
                }
            }
        }

        @Override
        public void onImgZoomFail() {
            Toast.makeText(context, "图片获取失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();

    }

    /**
     * 从data中获取返回的路径
     *
     * @param data
     * @return
     */
    public static String getDataPath(Intent data) {
        if (data != null) {
            return data.getStringExtra(SINGLE_PHOTO_PATH);
        }
        return null;
    }

    /**
     * 返回选择图片的类型
     *
     * @param data
     * @return 2为拍照或相册 1为系统图库
     */
    public static int getDataType(Intent data) {
        if (data != null) {
            return data.getIntExtra("type", 2);
        }
        return 0;
    }

    /**
     * 从data中获取多图选择的路径
     *
     * @param data
     * @return
     */
    public static ArrayList<String> getDataPathArr(Intent data) {
        if (data != null) {
            return data.getStringArrayListExtra(MANY_PHOTO_PATH_ARR);
        }
        return null;
    }

    /**
     * 单张选择完成
     */
    private void onSingChooseSuccess() {
        Intent intent = new Intent();
        intent.putExtra(SINGLE_PHOTO_PATH, currentPhotoPath);
        setResult(RESULT_OK, intent);
        finish();
    }
}
