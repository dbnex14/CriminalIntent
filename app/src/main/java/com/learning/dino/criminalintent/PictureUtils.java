package com.learning.dino.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by dbulj on 19/10/2014.
 */
public class PictureUtils {

    /**
     * Get a BitmapDrawable from a local file that is scalled down to fit the current Window size.
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a, String path){
        Display display = a.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        //read image dimensions from the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth){
            if (srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight / destHeight);
            }else{
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(a.getResources(), bitmap);
    }

    //CHALLANGE, Ch.20-ImageOrientation
    public static BitmapDrawable getPortraitBitmapDrawable(ImageView iView, BitmapDrawable origImage){
        Matrix m = new Matrix();
        m.postRotate(90);

        Bitmap br = Bitmap.createBitmap(origImage.getBitmap(), 0, 0, origImage.getIntrinsicWidth(), origImage.getIntrinsicHeight(), m, true);
        origImage = new BitmapDrawable(iView.getResources(), br);

        return  origImage;
    }

    public static void cleanImageView(ImageView imageView){
        if (!(imageView.getDrawable() instanceof BitmapDrawable)){
            return;
        }

        //Clean up the view's image for the sake of memory
        BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
