package org.nothingugly.uglydeals.jobPort.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.WindowManager;

public class Constants {

    public static void showProgessBar(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static Bitmap getBitmap(Activity activity, String picturePath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            return bitmap;
        } catch (OutOfMemoryError e) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
                return bitmap;
            } catch (Exception excepetion) {
            }
        }
        return null;
    }

    public static void hide(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}
