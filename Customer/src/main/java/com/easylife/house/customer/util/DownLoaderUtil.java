package com.easylife.house.customer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * 创建者：zwm
 * 类描述：下载文件，回传进度的工具类
 */

public class DownLoaderUtil {
    /**
     * 将请求下载的文件写到sd卡
     *
     * @param body
     * @param l
     * @return true 保存成功，false保存失败
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String apkName, DownLoaderProgreessListener l) {
        try {
            FileUtils.createDirs();
            File file = new File(FileUtils.SDPATH, apkName);
            if (file.exists()) {//安全删除文件 防止 open failed: EBUSY (Device or resource busy)
                final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
                file.renameTo(to);
                to.delete();
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024 * 5];
                double fileSize = body.contentLength();
                double fileSizeDownloaded = 0;
                int percent = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    percent = (int) ((fileSizeDownloaded / fileSize) * 100);
                    l.progreess(percent);
                    fileSizeDownloaded += read;

                }

                outputStream.flush();
                l.complete(file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface DownLoaderProgreessListener {
        void progreess(int progreess);

        void complete(File file);
    }

}
