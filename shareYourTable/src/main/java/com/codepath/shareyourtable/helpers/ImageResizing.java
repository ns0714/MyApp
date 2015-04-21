package com.codepath.shareyourtable.helpers;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

public class ImageResizing {

    public static byte[] bitmapToByteArray(Bitmap bmp, Bitmap.CompressFormat format) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(format, 100, stream);
        return (stream.toByteArray());
    }
    
}
