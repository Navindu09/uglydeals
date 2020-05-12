package org.nothingugly.uglydeals.jobPort.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Constants {

    public static void showProgessBar(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static void show(ProgressBar progressBar, Activity activity) {
        showProgessBar(activity);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg + "", Toast.LENGTH_SHORT).
                show();
    }

    public static void hide(ProgressBar progressBar, Activity activity) {
        hide(activity);
        progressBar.setVisibility(View.GONE);
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

    public static String getString(String myString) {
        return myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase();
    }
}
