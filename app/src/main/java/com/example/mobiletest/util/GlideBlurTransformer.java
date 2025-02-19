package com.example.mobiletest.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.internal.FastBlur;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/10/20
 * desc  :
 */
public class GlideBlurTransformer extends BitmapTransformation {
    private static final int VERSION = 1;
    private static final String ID = "BlurTransformation." + VERSION;

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private int radius;
    private int sampling;

    public GlideBlurTransformer() {
        this(MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public GlideBlurTransformer(int radius) {
        this(radius, DEFAULT_DOWN_SAMPLING);
    }

    public GlideBlurTransformer(int radius, int sampling) {
        this.radius = radius;
        this.sampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);
        bitmap = FastBlur.blur(bitmap, radius, true);

        return bitmap;
    }

    @Override public String toString() {
        return "GlideBlurTransformer(radius=" + radius + ", sampling=" + sampling + ")";
    }

    @Override public boolean equals(Object o) {
        return o instanceof GlideBlurTransformer &&
                ((GlideBlurTransformer) o).radius == radius &&
                ((GlideBlurTransformer) o).sampling == sampling;
    }

    @Override public int hashCode() {
        return ID.hashCode() + radius * 1000 + sampling * 10;
    }

    @Override public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + sampling).getBytes(CHARSET));
    }
}

