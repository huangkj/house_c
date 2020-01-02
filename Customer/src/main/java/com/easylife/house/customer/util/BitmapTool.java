package com.easylife.house.customer.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class BitmapTool {

    private final static String TAG = BitmapTool.class.getSimpleName();

    /**
     * 根据图像URL创建Bitmap
     *
     * @param url URL地址
     * @return bitmap
     */
    public static Bitmap CreateImage(String url) {
        // Logger.d("ImageDownloader",
        // "开始调用CreateImage():" + System.currentTimeMillis());
        Bitmap bitmap = null;
        if (url == null || url.equals("")) {
            return null;
        }
        try {
            // Logger.d(
            // "ImageDownloader",
            // "C Before SDCard decodeStream==>" + "Heap:"
            // + (Debug.getNativeHeapSize() / 1024) + "KB "
            // + "FreeHeap:"
            // + (Debug.getNativeHeapFreeSize() / 1024) + "KB "
            // + "AllocatedHeap:"
            // + (Debug.getNativeHeapAllocatedSize() / 1024)
            // + "KB" + " url:" + url);

            FileInputStream fis = new FileInputStream(url);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(fis, null, opts);

            // Logger.d(
            // "ImageDownloader",
            // "C After SDCard decodeStream==>" + "Heap:"
            // + (Debug.getNativeHeapSize() / 1024) + "KB "
            // + "FreeHeap:"
            // + (Debug.getNativeHeapFreeSize() / 1024) + "KB "
            // + "AllocatedHeap:"
            // + (Debug.getNativeHeapAllocatedSize() / 1024)
            // + "KB" + " url:" + url);
        } catch (OutOfMemoryError e) {

            System.gc();
        } catch (FileNotFoundException e) {

        }
        // Logger.d("ImageDownloader",
        // "结束调用CreateImage():" + System.currentTimeMillis());
        return bitmap;
    }

    /**
     * 图片缩放处理,并保存到SDCard
     *
     * @param byteArrayOutputStream 图片字节流
     * @param screen                屏幕宽高
     * @param url                   图片网络路径
     * @param cachePath             本地缓存父路径</br>PathCommonDefines.PHOTOCACHE_FOLDER 程序缓存图片路径;</br>
     *                              PathCommonDefines.MY_FAVOURITE_FOLDER 我的收藏图片路径
     * @param isJpg                 是否是Jpg
     * @return 缩放后的图片bitmap
     */
    public static Bitmap saveZoomBitmapToSDCard(
            ByteArrayOutputStream byteArrayOutputStream, Screen screen,
            String url, String cachePath, boolean isJpg) {

        Bitmap bitmap = null;
        try {

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inTempStorage = new byte[16 * 1024];

            // 只加载图片的边界
            options.inJustDecodeBounds = true;

            // 获取Bitmap信息
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,
                    options);

            // 获取屏幕的宽和高
            int screenWidth = screen.widthPixels;
            int screenHeight = screen.heightPixels;

            // 屏幕最大像素个数
            int maxNumOfPixels = screenWidth * screenHeight;

            // 计算采样率
            int sampleSize = computeSampleSize(options, -1, maxNumOfPixels);

            options.inSampleSize = sampleSize;

            options.inJustDecodeBounds = false;

            // 重新读入图片,此时为缩放后的图片
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length, options);

            // 压缩比例
            int quality = 100;

            // 判断是否是Jpg,png是无损压缩,所以不用进行质量压缩
            if (bitmap != null && isJpg) {

                ByteArrayOutputStream saveBaos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, saveBaos);

                // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                while (saveBaos.toByteArray().length / 1024 > 100) {

                    // 重置saveBaos即清空saveBaos
                    saveBaos.reset();

                    // 每次都减少10
                    quality -= 10;

                    // 这里压缩optionsNum%，把压缩后的数据存放到saveBaos中
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                            saveBaos);

                }
                // 把压缩后的数据ByteArrayOutputStream存放到ByteArrayInputStream中
                ByteArrayInputStream saveBais = new ByteArrayInputStream(
                        saveBaos.toByteArray());

                bitmap = BitmapFactory.decodeStream(saveBais, null, null);

            }

            // // 保存到SDCard
            // ImageSDCacher.getImageSDCacher().saveBitmapToSDCard(bitmap, url,
            // cachePath, isJpg, quality);

        } catch (Exception e) {
            Log.e("saveZoomBitmapToSDCard", "" + e);
        }

        return bitmap;
    }

    /**
     * 图片缩放处理,并保存到SDCard
     *
     * @param screen    屏幕宽高
     * @param bitmap    图片bitmap
     * @param cachePath 本地缓存父路径</br>PathCommonDefines.PHOTOCACHE_FOLDER 程序缓存图片路径;</br>
     *                  PathCommonDefines.MY_FAVOURITE_FOLDER 我的收藏图片路径
     * @param isJpg     是否是Jpg
     * @return 缩放后的图片bitmap
     */
    public static Bitmap saveZoomBitmapToSDCard(Bitmap bitmap, Screen screen,
                                                String url, String cachePath, boolean isJpg) {
        Bitmap tempBitmap = null;
        byte[] byteArray = bitmap2Bytes(bitmap);
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            // 获取屏幕的宽和高
            int screenWidth = screen.widthPixels;
            int screenHeight = screen.heightPixels;

            // 屏幕最大像素个数
            int maxNumOfPixels = screenWidth * screenHeight;

            // 计算采样率
            int sampleSize = computeSampleSize(options, -1, maxNumOfPixels);

            options.inSampleSize = sampleSize;

            options.inJustDecodeBounds = false;

            // 重新读入图片,此时为缩放后的图片
            tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length, options);

            // 压缩比例
            int quality = 100;

            // 判断是否是Jpg,png是无损压缩,所以不用进行质量压缩
            if (bitmap != null && isJpg) {

                ByteArrayOutputStream saveBaos = new ByteArrayOutputStream();

                tempBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                        saveBaos);

                // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                while (saveBaos.toByteArray().length / 1024 > 100) {

                    // 重置saveBaos即清空saveBaos
                    saveBaos.reset();

                    // 每次都减少10
                    quality -= 10;

                    // 这里压缩optionsNum%，把压缩后的数据存放到saveBaos中
                    tempBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                            saveBaos);

                }
                // 把压缩后的数据ByteArrayOutputStream存放到ByteArrayInputStream中
                ByteArrayInputStream saveBais = new ByteArrayInputStream(
                        saveBaos.toByteArray());

                tempBitmap = BitmapFactory.decodeStream(saveBais, null, null);

            }

            // 保存到SDCard
            // ImageSDCacher.getImageSDCacher().saveBitmapToSDCard(tempBitmap,
            // url, cachePath, isJpg, quality);

        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        return tempBitmap;
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // Recycle the resource of the Image
    public void recycleImage(Bitmap bitmap) {
        try {
            if (bitmap != null && !bitmap.isMutable() && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换特殊字符
     *
     * @param fileName 图片的处理前的名字
     * @return 图片处理后的名字
     */
    public static String renameUploadFile(String fileName) {

        String result = "yepcolor";

        if (fileName != null && !fileName.equals("")) {

            result = fileName.hashCode() + "";// 获得文件名称的hashcode值

        }
        return result;
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        // String regEx =
        // "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // Pattern p = Pattern.compile(regEx);
        // Matcher m = p.matcher(fileName);
        // result = m.replaceAll("").trim();

    }

    /**
     * 计算采样率
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength,

                maxNumOfPixels);

        int roundedSize;

        if (initialSize <= 8) {

            roundedSize = 1;

            while (roundedSize < initialSize) {

                roundedSize <<= 1;

            }

        } else {

            roundedSize = (initialSize + 7) / 8 * 8;

        }

        return roundedSize;

    }

    /**
     * 计算初始采样率
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;

        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :

                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128 :

                (int) Math.min(Math.floor(w / minSideLength),

                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {

            // return the larger one when there is no overlapping zone.

            return lowerBound;

        }

        if ((maxNumOfPixels == -1) &&

                (minSideLength == -1)) {

            return 1;

        } else if (minSideLength == -1) {

            return lowerBound;

        } else {

            return upperBound;

        }

    }

    /**
     * 根据图片的路径获取图片的大小
     *
     * @param item
     */
    // public static void getBitmapSize(Items item) {
    // URL url;
    // try {
    // url = new URL(item.getPicUrl());
    // URLConnection conn = url.openConnection();
    // conn.connect();
    // InputStream is = conn.getInputStream();
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // BitmapFactory.decodeStream(is, null, options);
    // options.inJustDecodeBounds = true;
    // int height = options.outHeight;
    // int width = options.outWidth;
    // item.setImageWidth(width);
    // item.setImageHeight(height);
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // }

    /**
     * Bitmap 转换成二进制
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * byte[]转换成Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * 获取屏幕的大小
     *
     * @param context
     * @return
     */
    public static Screen getScreenPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new Screen(dm.widthPixels, dm.heightPixels);
    }

    public static class Screen {
        public int widthPixels;
        public int heightPixels;

        public Screen() {
        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }
    }

    /**
     * 高斯模糊
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 15; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {

        // Let's create an empty bitmap with the same size of the bitmap we want
        // to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,
                Element.U8_4(rs));

        try {
            // Create the Allocations (in/out) with the Renderscript and the in/out
            // bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

            // Set the radius of the blur
            blurScript.setRadius(25.f);

            // Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            // Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);
        } catch (Exception e) {
            System.out.print(e);
        }


        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }


    /**
     * 质量压缩
     *
     * @param bmp  需要压缩的Bitmap
     * @param file 压缩后保存的文件
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            if (options <= 0) {
                options = 0;
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            break;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩图片大小
     *
     * @param srcPath 图片的绝对路径
     * @return
     */
    public static Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

//        newOpts.inSampleSize = 20;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        FileUtils.saveBitmap(bitmap,file.getName());
        //压缩好比例大小后再进行质量压缩
//        compressBmpToFile(bitmap, file);
        return compressImage(bitmap);
    }


    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options < 0) {
                break;
            }
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据本地Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        if (bm == null) {
            return null;
        }
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
//		if (returnBm == null) {
//			returnBm = bm;
//		}
//		if (bm != returnBm) {
//			bm.recycle();
//		}
        return returnBm;
    }


    /**
     * 适合压缩缩略图
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 根据图片需要显示的尺寸进行压缩
     *
     * @param path
     * @param imageSize
     * @return
     */
    public static Bitmap decodeSampledBitmapFromPath(String path,
                                                     ImageSize imageSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 获取图片大小，并不把图片加载到内存中
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = caculateInSampleSize(options, imageSize);

        // 使用获取到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    /**
     * 根据需求的宽、高以及图片的实际宽、高计算SampleSize
     *
     * @param options
     * @param imageSize
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options options, ImageSize imageSize) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;// 压缩取样率
        if (width > imageSize.width || height > imageSize.height) {
            int widthRadio = Math.round(width * 1.0f / imageSize.width);
            int heightRadio = Math.round(height * 1.0f / imageSize.height);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 根据ImageView获取适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewSize(ImageView imageView) {
        DisplayMetrics displayMetrics = imageView.getContext().getResources()
                .getDisplayMetrics();

        ViewGroup.LayoutParams params = imageView.getLayoutParams();

        int width = imageView.getWidth();// 获取imageview的实际宽度
        if (width <= 0) {
            width = params.width;// 获取imageView在layout中声明的宽度
        }
        if (width <= 0) {
            // width = imageView.getMaxWidth();// 检查最大值
            width = getImaeViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();// 获取imageview的实际宽度
        if (height <= 0) {
            height = params.height;// 获取imageView在layout中声明的宽度
        }
        if (height <= 0) {
            // height = imageView.getMaxHeight();// 检查最大值
            height = getImaeViewFieldValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }

        ImageSize imageSize = new ImageSize();
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取View的某个属性值,可以下兼容
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static int getImaeViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String decodeSampledBitmapFromPath(String imagePath
            , String imageName) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(imagePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 800, 480);
        //如果无需压缩，直接返回
        if (options.inSampleSize == 1) {
            return imagePath;
        }
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        FileUtils.saveBitmap(BitmapFactory.decodeFile(imagePath, options), imageName);
        return FileUtils.SDPATH + imageName;
    }

    /**
     * 获取适合imageView的bm
     *
     * @param iv
     * @param imagePath
     * @return
     */
    public static Bitmap getAppropriateBitmap(ImageView iv, String imagePath) {
        ImageSize imageSize;
        Bitmap bm;
        imageSize = BitmapTool.getImageViewSize(iv);
        // 2、压缩图片
        bm = decodeSampledBitmapFromPath(imagePath, imageSize);
        //旋转图片
        bm = BitmapTool.rotateBitmapByDegree(bm, BitmapTool.getBitmapDegree(imagePath));
        return bm;
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void startPhotoZoom(Activity context, Uri uri, int ImaeSize, int requestCode) {


        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 19) { // 修改4.4以上系统的bug
            String url = getImageAbsolutePath(context, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", ImaeSize);
        intent.putExtra("outputY", ImaeSize);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }

    public static class ImageSize {
        public ImageSize(int width, int height) {
            this.height = height;
            this.width = width;
        }

        public ImageSize() {
        }

        public int width;
        public int height;
    }

}
