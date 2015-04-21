package com.codepath.shareyourtable.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * Created by nsamant on 4/3/15.
 */
public class BitMapHelper {
    private static Bitmap bitmap;
    public static Bitmap getBitMapFromFile(ParseFile imgFile){
        byte[] bitmapdata;
        if (imgFile != null) {
            try {
                bitmapdata = imgFile.getData();
                bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0,
                        bitmapdata.length);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        //Canvas canvas = new Canvas(bitmap);
        //drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        //drawable.draw(canvas);

        return bitmap;
    }
}
