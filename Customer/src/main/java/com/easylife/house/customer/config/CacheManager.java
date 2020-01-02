package com.easylife.house.customer.config;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.util.LogOut;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 路径及缓存管理
 */
public class CacheManager {
    public static final String DIR_CACHE = Environment
            .getExternalStorageDirectory() + "/easylife/goodlife/";
    public static final String DIR_NAME = "img";

    /**
     * 获取图片缓存路径 如果不存在，则创建路径
     *
     * @param context
     * @return
     */
    public static String getImgDir(Context context) {
        String path = DIR_CACHE + DIR_NAME + "/.img/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 默认下载路径
     *
     * @param context
     * @return
     */
    public static String getDownLoadDir(Context context) {
        String path = DIR_CACHE + DIR_NAME + "/download/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 清除缓存图片
     */
    public static void chearImgCache(Context context) {
        try {
            String imgDir = getImgDir(context);
            File dir = new File(imgDir);
            if (dir != null && dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            LogOut.d("chearImgCache", e.toString());
        }
    }

    /**
     * 获取二维码保存的路径
     *
     * @param context
     * @return
     */
    public static String getQrcodeDir(Context context) {
        String path = DIR_CACHE
                + context.getResources().getString(R.string.app_name) + "/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static void initCenterCropImage(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.image_empty)
                .placeholder(R.mipmap.image_empty)
                .centerCrop()
                .into(img);
    }


    public static void initImageClientList(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.image_empty)
                .placeholder(R.mipmap.image_empty)
                .into(img);
    }

    public static void initSearchHouseList(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.image_empty_dev)
                .placeholder(R.mipmap.image_empty_dev)
                .into(img);
    }

    public static void initImagePagerList(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.map_detail_bg)
                .error(R.mipmap.map_detail_bg)
                .into(img);
    }

    public static void initImageUserHeader(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.image_empty)
                .error(R.mipmap.user_default_head)
                .bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                .into(img);
    }

    public static void initImagePubAdd(Context context, ImageView img, String imgUrl) {
        Glide
                .with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.image_empty)
                .error(R.color.GrayLight)
                .into(img);
    }
}
