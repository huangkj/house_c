package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 拍照或选取
 */
public class PhotoUtil {

    /**
     * 外置存储拍照请求
     */
    // public static final int REQUEST_IMAGE_CAPTURE_OUTPUT = 0x101;
    /**
     * 图片压缩成功
     */
    private static final int IMAGE_COMPRESS_SUCCESS = 200;

    /**
     * 开始压缩图片
     */
    private static final int IMAGE_COMPRESS_START = 100;
    /**
     * 图片压缩失败
     */
    private static final int IMAGE_COMPRESS_FAIL = 404;

    /**
     * 拍照并保存到指定路径
     */
    public static Uri takePhotoCustomerPath(Activity activity, String cacheDir,
                                            String imageName, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (TextUtils.isEmpty(imageName)) {
            imageName = createDefaultName();
        }
        File file = new File(cacheDir);
        // 路径不存在则创建路径
        if (!file.exists() && file.isDirectory()) {
            file.mkdirs();
        }
        Uri uri = Uri.fromFile(new File(cacheDir + imageName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * 默认创建以时间格式的图片名称
     *
     * @return
     */
    public static String createDefaultName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmssSSS", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()))
                + ".jpg";
    }

    /**
     * 打开相册
     *
     * @param activity
     * @param requestCode
     */
    public static void pickPhoto(Activity activity, int requestCode) {
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("houseImg/*");
        // 此方法比较兼容
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开相册
     *
     * @param activity
     * @param requestCode
     */
    public static void pickPhoto(Fragment activity, int requestCode) {
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("houseImg/*");
        // 此方法比较兼容
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开相机
     */
    public static void openCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开相机
     */
    public static void openCamera(Fragment activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照获取的结果
     *
     * @param data
     * @return
     */
    public static Bitmap getBitmapFromResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = (Bitmap) bundle.get("data");
                // 获取相机返回的数据，并转换为Bitmap图片格式
                return bitmap;
            }
        }
        return null;

    }

    /**
     * 打开图片裁剪
     *
     * @param activity
     * @param uri
     * @param outputX     输出的宽
     * @param outputY     输出的高
     * @param requestCode
     */
    public static void openCropImage(Activity activity, Uri uri, Uri outUri,
                                     int outputX, int outputY, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "houseImg/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开图片裁剪
     *
     * @param activity
     * @param uri
     * @param outputX     输出的宽
     * @param outputY     输出的高
     * @param requestCode
     */
    public static void openCropImage(Fragment activity, Uri uri, int outputX,
                                     int outputY, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "houseImg/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        // intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开图片裁剪
     *
     * @param activity
     * @param uri
     * @param aspectX     裁剪框的比例-宽
     * @param aspectY     裁剪框的比例-高
     * @param outputX     输出的宽
     * @param outputY     输出的高
     * @param requestCode
     */
    public static void openCropImage(Activity activity, Uri uri, Uri outUri,
                                     int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "houseImg/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // 裁剪后输出图片的尺寸大小
        if (outputX != 0 && outputY != 0) {
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
        }

        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 压缩图片进度回调
     *
     * @author heiyue heiyue623@126.com
     * @ClassName: ImageZoomCallBack
     * @Description:
     * @date 2014-6-18 下午5:35:36
     */
    public interface ImageZoomCallBack {
        void onImgZoomStart();

        void onImgZoomSuccess(String newPath);

        void onImgZoomFail();
    }

    /**
     * 压缩图片
     */
    public static void zoomImage(final Context context, final Uri imageOldPath,
                                 final String newPath, final int width, final int height,
                                 final int quality, final ImageZoomCallBack callBack,
                                 final boolean needRemote) {
        final Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case IMAGE_COMPRESS_START:
                        if (callBack != null) {
                            callBack.onImgZoomStart();
                        }
                        break;
                    case IMAGE_COMPRESS_SUCCESS:
                        if (callBack != null) {
                            callBack.onImgZoomSuccess(newPath);
                        }
                        break;
                    case IMAGE_COMPRESS_FAIL:
                        if (callBack != null) {
                            callBack.onImgZoomFail();
                        }
                        break;
                    default:
                        break;
                }
            }

            ;
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(IMAGE_COMPRESS_START);
                try {
                    Bitmap pathBitmap = getPathBitmap(context, imageOldPath,
                            width, height);
                    if (pathBitmap != null) {
                        boolean saveSuccess = saveImageFileByBitmap(
                                imageOldPath.getPath(), newPath, quality,
                                pathBitmap, needRemote);
                        if (saveSuccess) {
                            handler.sendEmptyMessage(IMAGE_COMPRESS_SUCCESS);
                        } else {
                            handler.sendEmptyMessage(IMAGE_COMPRESS_FAIL);
                        }
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(IMAGE_COMPRESS_FAIL);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 相册获取图片路径
     *
     * @param context
     * @param data
     * @return
     */
    public static Uri getPhotoPath(Context context, Intent data) {
        if (data == null) {
            return null;
        }
        Uri path = null;
        Uri uri = data.getData();
        if (uri != null) {
            Cursor c = null;
            try {
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                c = context.getContentResolver().query(uri, filePathColumns,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                if (imagePath != null) {
                    path = Uri.fromFile(new File(imagePath));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        path = FileProvider.getUriForFile(context, "com.easylife.house.customer.fileprovider", new File(imagePath));
                    }
                }
            } catch (Exception e) {

            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        return path;
    }

    /**
     * 显示需要压缩大图片大小
     *
     * @param context
     * @param imageFilePath
     * @param dw            需要压缩的宽度
     * @param dh            需要压缩高度
     * @return
     * @throws FileNotFoundException
     */
    private static Bitmap getPathBitmap(Context context, Uri imageFilePath,
                                        int dw, int dh) throws FileNotFoundException {
        // 获取屏幕的宽和高
        /**
         * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片
         * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true
         * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。
         * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值
         */
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // 由于使用了MediaStore存储，这里根据URI获取输入流的形式
        Bitmap pic = BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(imageFilePath), null, op);

        int wRatio = (int) Math.ceil(op.outWidth / (float) dw); // 计算宽度比例
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh); // 计算高度比例

        /**
         * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 如果高和宽不是全都超出了屏幕，那么无需缩放。
         * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 这需要判断wRatio和hRatio的大小
         * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 缩放使用的还是inSampleSize变量
         */
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio >= hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false; // 注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
        pic = BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(imageFilePath), null, op);

        return pic;
    }

    /**
     * JPG格式保存压缩图片
     *
     * @param savaFilePath 图片保存路径
     * @param quality      压缩比例 最小30 最大100
     * @param bitmap
     * @return
     */
    public static boolean saveImageFileByBitmap(String oldFilePath,
                                                String savaFilePath, int quality, Bitmap bitmap, boolean needRemote) {
        BufferedOutputStream bos = null;
        try {
            // 判断图片是否旋转了，旋转图片
            if (needRemote) {
                bitmap = rotateBitmap(bitmap, readPictureDegree(oldFilePath));
            }
            File image = new File(savaFilePath);
            if (!image.exists()) {
                image.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(image));
            if (quality < 30) {
                quality = 30;
            }
            if (quality > 100) {
                quality = 100;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                //
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片的角度
     *
     * @param bitmap
     * @param degress
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

}
