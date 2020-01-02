package com.easylife.house.customer.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.easylife.house.customer.App;
import com.nanchen.compresshelper.CompressHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 类描述：将文件转换为bytep[] 流
 * Created by zgm on 2017/2/27.
 */

public class FileToBytesUtil {

    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File oldFile = new File(filePath);
            File file = CompressHelper.getDefault(App.applicationContext).compressToFile(oldFile);
            Log.e("File",oldFile.length()+"-------"+file.length());
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }


    public static void byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public File comPressImg(File oldFile){
        File newFile = new CompressHelper.Builder(App.applicationContext)
                .setQuality(50)    // 默认压缩质量为80
                .setCompressFormat(Bitmap.CompressFormat.PNG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(oldFile);
        Log.e("File",newFile.length()+"");
        return newFile;
    }
}
