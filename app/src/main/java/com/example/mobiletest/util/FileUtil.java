package com.example.mobiletest.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/11/3
 * desc  :
 */
public class FileUtil {
    private static final String TAG = "FILE_UTIL";

    /**
     * 把content uri转为 文件路径
     *
     * @param contentUri      要转换的content uri
     * @param contentResolver 解析器
     * @return
     */
    public static String getFilePathFromContentUri(Uri contentUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(contentUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 删除相册资源
     */
    public static void deleteMediaFile(Context context, File file) {
        if (file.isFile()) {
            String filePath = file.getPath();
            if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".bmp")) {
                int res = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);
                if (res > 0) {
                    file.delete();
                } else {
                    Log.d(TAG, "删除文件失败");
                }
            } else {
                file.delete();
            }
            //删除多媒体数据库中的数据
            return;
        }

    }

    /**
     * 删除文件
     */
    public static boolean deleteFolder(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // 删除文件
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 实例化目录下所有的文件
                if (files != null) { // 遍历目录下所有的文件，并删除
                    for (int i = 0; i < files.length; i++) {
                        deleteFolder(files[i]);
                    }
                }
            }
            boolean isSuccess = file.delete();
            if (!isSuccess) {
                return false;
            }
        }
        return true;
    }

    public static Bitmap compImg(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Log.e("wechat", "质量压缩前baos.toByteArray().length" + baos.toByteArray().length / 1024 + "字节");

        /*if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩30%，把压缩后的数据存放到baos中
        }*/
        Log.e("wechat", "baos.toByteArray().length" + baos.toByteArray().length / 1024 + "字节");
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        int w = image.getWidth();
        int h = image.getHeight();
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为1280f
        float ww = 720f;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        Log.e("wechat", "comp压缩bitmap图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
                + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 保存图片到自定义路径，然后再刷新到系统相册
     *
     * @return
     */
    public static Uri saveBitmap(Context context, Bitmap bitmap, String bitName) {
        Log.d(TAG, "Build.BRAND============" + Build.BRAND);
        createFolder();
        String fileName;
        //图片创建路径
        File file;
        fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/mobilePhoto/.nomedia/" + bitName;
        file = new File(fileName);
        Log.d(TAG, "saveBitmap: " + file.getAbsolutePath());
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "saveBitmap: 已删除");
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        return Uri.parse("file://" + fileName);
    }

    public static void createFolder() {
        String rootFolder = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
        String folder1 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/mobilePhoto/";
        String folder2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/mobilePhoto/.nomedia/";
        String fileName;
        File mobilePhotoFile;
        File nomediaFile;
        //创建隐藏照片的文件夹
        mobilePhotoFile = new File(folder1);
        if (!mobilePhotoFile.exists()) {
            createFolderInDCIM(rootFolder, "mobilePhoto");
            nomediaFile = new File(folder2);
            if (!nomediaFile.exists()) createFolderInDCIM(folder1, ".nomedia");
        }
    }

    /**
     * 创建隐藏资源文件夹
     */
    public static void createFolderInDCIM(String path, String FolderName) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("SD存在");
            File file = new File(path, FolderName);
            if (file.exists()) {
                System.out.println("文件已经存在");
            } else {
                file.mkdirs();
            }
        } else {
            System.out.println("没有发现SD卡");
        }
    }

    public static Bitmap strToBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bitmapToStr(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            String bitmapStr = Base64AndPic.imgToBase64String(bitmap);
            return bitmapStr;
        }
        return "";
    }
}
