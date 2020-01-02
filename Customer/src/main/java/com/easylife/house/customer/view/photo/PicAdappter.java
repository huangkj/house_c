package com.easylife.house.customer.view.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.easylife.house.customer.util.KeyedLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PicAdappter extends BaseAdapter implements OnScrollListener {
	private Context context;
	private Album album;
	private Map<String, Bitmap> cacheBms = new HashMap<String, Bitmap>();

	public PicAdappter(Context context, Album album, GridView photoWall) {
		this.context = context;
		this.album = album;

		//View的实例
		photoWall.setOnScrollListener(this); //add implements OnScrollListener
	}

	@Override
	public int getCount() {
		return album.getBitList().size();
	}

	@Override
	public Object getItem(int position) {
		return album.getBitList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoGridItem item;
		if(convertView == null){
			item = new PhotoGridItem(context);
			item.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                     android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		}else{
			item = (PhotoGridItem)convertView;
		}
		item.getmSelect().setTag("select_"+position);
        item.getmImageView().setTag("image_"+position);
        if(cacheBms.get(position+"")==null){
        	if (mIsScrolling){
        		item.SetBitmap(null);
        	}else{
            	long imgId=album.getBitList().get(position).getPhotoID();
            	String imgPath=album.getBitList().get(position).getPhotoPath();
            	loadImgWithThread(item,position,imgId,imgPath);
        	}

        	//下面方法太卡
        	/*Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  album.getBitList().get(position).getPhotoID(), Thumbnails.MICRO_KIND, null);
        	int rotate = PictureManageUtil.getCameraPhotoOrientation(album.getBitList().get(position).getPhotoPath());
			bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
        	item.SetBitmap(bitmap);
        	cacheBms.put(position+"", bitmap);*/
        }else{
        	item.SetBitmap(cacheBms.get(position+""));
        }
        boolean flag = album.getBitList().get(position).isSelect();
		item.setChecked(flag);
		return item;
	}


	/**
	 * 是否在滚动中,mIsScrolling=true正在滚动
	 */
	private boolean mIsScrolling=false;
	/**
	 * 设置滚动状态
	 * 重要：一定要设置： mPhotoWall.setOnScrollListener(this); //add implements OnScrollListener
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 仅当ListView静止时才去下载图片，ListView滑动时取消所有正在下载的任务
		if (scrollState == SCROLL_STATE_IDLE) {
			System.out.println("  停止滚动.");
			mIsScrolling=false;
			this.notifyDataSetChanged();
		} else {
			mIsScrolling=true;
			cancelTask();
		}
	}
	/**
	 * mPhotoWall.setOnScrollListener(this); //add implements OnScrollListener
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
						 int totalItemCount) {
	}

	/**
	 * 加载本地图片显示
	 * @param position
	 * @param imgId
	 * @param imgPath
	 */
	public void loadImgWithThread(final PhotoGridItem item,final int position,
			final long imgId, final String imgPath) {

		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj!=null){
					Bitmap bit=(Bitmap)msg.obj;
					item.SetBitmap(bit);
				}else{
					item.SetBitmap(null);
				}
			}
		};

		//线程加载图片
		getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
                mDownloading.lock(imgPath);
                Bitmap bitmap=null;
                try {
                	//加载图片
                	bitmap=loadImg(imgId, imgPath);

    				//加入缓存
    				if (bitmap!=null) cacheBms.put(position+"", bitmap);
                } finally {
                    mDownloading.unlock(imgPath);
                }

				Message msg = handler.obtainMessage();
				msg.obj = bitmap;
				handler.sendMessage(msg);
			}
		});
	}// end loadImgWithThread

	//取图片
	public Bitmap loadImg(long imgId, String imgPath) {
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
			 context.getContentResolver(),imgId, Thumbnails.MICRO_KIND, null);
    	int rotate = PictureManageUtil.getCameraPhotoOrientation(imgPath);
		bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
		return bitmap;
	}

	/**
	 * 下载锁
	 */
    private final KeyedLock<String> mDownloading = new KeyedLock<String>();

	/**
	 * 线程下载图片
	 */
	private static final int MAX_DOWN_THREAD = 6;
	/**
	 * 下载Image的线程池
	 */
	private ExecutorService mImageThreadPool = null;

	/**
	 * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
	 * @return
	 */
	public ExecutorService getThreadPool(){
		if(mImageThreadPool == null){
			synchronized(ExecutorService.class){
				if(mImageThreadPool == null){
					//为了下载图片更加的流畅，我们用了2个线程来下载图片
					mImageThreadPool = Executors.newFixedThreadPool(MAX_DOWN_THREAD);
				}
			}
		}
		return mImageThreadPool;
	}

	/**
	 * 取消正在下载的任务
	 */
	public synchronized void cancelTask() {
		if(mImageThreadPool != null){
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
		}
	}

	/**
     * 异步下载图片的回调接口
     * @author len
     *
     */
    public interface IImageLoaderListener{
        void onImageLoader(Bitmap bitmap);
    }


}
