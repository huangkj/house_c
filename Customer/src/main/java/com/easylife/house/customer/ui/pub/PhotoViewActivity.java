package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.IPhoto;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.PublicImage;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.ImageTool;
import com.easylife.house.customer.view.photoview.PhotoView;
import com.easylife.house.customer.view.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 大图浏览页 Author:heiyue Email:heiyue623@126.com 2015-6-5上午9:30:34
 */
public class PhotoViewActivity extends FragmentActivity {

    public static final String IMAGE_URLS_ARR = "img_urls";
    public static final String IMAGE_POSITION = "img_position";
    public static final String IMAGE_TYPE_HEAD = "img_type_head";// 图片类型为头像
    public static List<? extends IPhoto> images;
    private int currentPos;
    private TextView tvSize;
    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pub_activity_photo_view);
        context = this;
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvSize = (TextView) findViewById(R.id.tvSize);


        currentPos = getIntent().getIntExtra(IMAGE_POSITION, 0);
        if (images != null && images.size() > 0) {
            Adapter adapter = new Adapter();
            viewPager.setAdapter(adapter);
            if (currentPos >= 0 && currentPos < images.size()
                    && images.size() > 1) {
                tvSize.setText((currentPos + 1) + " / " + images.size());
                viewPager.setCurrentItem(currentPos);
            }
        }
        /**
         * 切换效果
         */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                tvSize.setText((arg0 + 1) + " / " + images.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    /**
     * 打开大图浏览
     *
     * @param context
     * @param position
     * @param imgs
     */
    public static void startActivity(Context context, int position,
                                     List<? extends IPhoto> imgs) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(IMAGE_POSITION, position);
        PhotoViewActivity.images = imgs;
        context.startActivity(intent);
    }

    /**
     * 打开大图浏览--单张图片
     *
     * @param context
     * @param url     图片路径
     */
    public static void startActivity(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        Intent intent = new Intent(context, PhotoViewActivity.class);
        List<PublicImage> imgs = new ArrayList<PublicImage>();
        PublicImage image = new PublicImage();
        image.cover = url;
        imgs.add(image);
        intent.putExtra(IMAGE_POSITION, 0);
        PhotoViewActivity.images = imgs;
        context.startActivity(intent);

    }

    public static void startActivity(Context context, List<ImageBean> images, int position) {
        if (images == null || images.size() == 0) {
            return;
        }
        Intent intent = new Intent(context, PhotoViewActivity.class);
        List<PublicImage> imgs = new ArrayList<>();
        for (ImageBean imageBean : images) {
            PublicImage image = new PublicImage();
            image.cover = imageBean.pic;
            imgs.add(image);
        }
        intent.putExtra(IMAGE_POSITION, position);
        PhotoViewActivity.images = imgs;
        context.startActivity(intent);

    }

    class Adapter extends PagerAdapter {

        public Adapter() {
        }

        @Override
        public int getCount() {
            if (images != null) {
                return images.size();
            }
            return 0;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final IPhoto item = images.get(position);
            PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            if (item != null) {
                String photoPath = item.getPhotoPath();

                // 图片路径--这个是全路径
                String mphotoPath = null;

                if (!TextUtils.isEmpty(photoPath)) {

                    if (photoPath.startsWith("http://")) {
                        // 网络图片
                        mphotoPath = photoPath;

                    } else {
                        // 本地图片
                        mphotoPath = Uri.fromFile(new File(photoPath))
                                .toString();

                    }
                } else if (item.getPhotoRes() > 0) {
                    // 加载的是图片资源
                    mphotoPath = Uri.fromFile(new File(photoPath)).toString();

                }
                if (mphotoPath != null) {
                    Glide.with(getApplicationContext()).load(mphotoPath).into(photoView);
                }

            }
            photoView
                    .setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                        @Override
                        public void onViewTap(View view, float x, float y) {
                            finish();
                        }
                    });
            photoView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // showPopuWindow(item.getPhotoPath());
                    currentUrl = item.getPhotoPath();
                    SavePhotoPupuActivity.open(context, 1);
                    return true;
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 菜单操作
                    if (SavePhotoPupuActivity.isSave) {
                        // 保存菜单的操作
                        save(currentUrl);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private String currentUrl;

    /**
     * 保存到相册
     *
     * @param url
     */
    private void save(final String url) {
        String displayUrl = url;
        if (!url.startsWith("http://")) {
            displayUrl = Uri.fromFile(new File(url)).toString();
        }
        Glide.with(getApplicationContext())
                .load(displayUrl)
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    // 保存到相册
                    String dir = CacheManager
                            .getQrcodeDir(getApplicationContext());
                    ImageTool.saveJPGE_After(bitmap,
                            dir + url + ".jpg");
                    // 保存成功，通知系统更新
                    ImageTool.scanPhotos(dir + url
                            + ".jpg", getApplicationContext());
                    // 保存成功
                    Toast.makeText(getApplicationContext(), "成功保存到相册",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
