package com.lerenard.bible.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by mc on 21-Dec-16.
 */

public class TextImage {
    public static Drawable getDrawable(
            Context context, CharSequence text, int width, int height, int color, float textSize,
            Typeface typeface) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(typeface);
        canvas.drawText(text.toString(), width / 2f, height / 2f, paint);

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        Drawable res = imageView.getDrawable();
        res.setBounds(0, 0, width, height);
        return res;
    }
}
