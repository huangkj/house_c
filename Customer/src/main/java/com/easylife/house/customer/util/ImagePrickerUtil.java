package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.easylife.house.customer.bean.BeanFile;
import com.easylife.house.customer.bean.PublicImage;
import com.easylife.house.customer.ui.pub.PdfReviewActivity;
import com.easylife.house.customer.ui.pub.PhotoViewActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mars on 2018/3/28 14:04.
 * 描述：图片选择工具类
 */

public class ImagePrickerUtil {
    public static String getRandomName() {
        return System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 5);
    }

    public static void reviewPdf(Context context, BeanFile pdfFile) {
        if (pdfFile == null || TextUtils.isEmpty(pdfFile.url)) {
            ToastUtils.showShort(context, "无效文件地址");
            return;
        }
        if (pdfFile.url.contains("http")) {
            PdfReviewActivity.startActivity(context, pdfFile.name, pdfFile.url);
        } else {
//            LocalPDFActivity.startActivity(context, pdfFile.url);
        }
    }

    public static void reviewPhotos(Context activity, List<PublicImage> single_photos, int position) {
        ArrayList<PublicImage> photoPaths = new ArrayList<>();
        for (PublicImage photo : single_photos) {
            if (!photo.isAdd)
                photoPaths.add(photo);
        }
        PhotoViewActivity.startActivity(activity, position, photoPaths);
    }

    public static void pickerImage(Activity activity, int numberLimit, int requestCode) {

        Matisse.from(activity)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.easylife.house.customer.fileprovider"))
                .maxSelectable(numberLimit)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(120)
                .forResult(requestCode);
    }

    public static List<String> getSelectImagesPath(Intent data) {
        if (data == null)
            return null;
        List<String> photos = Matisse.obtainPathResult(data);
        return photos;
    }

    public static String getImageFirst(Intent data) {
        if (data == null)
            return null;
        List<String> photos = getSelectImagesPath(data);
        if (photos == null || photos.size() == 0)
            return null;
        return photos.get(0);
    }
}

