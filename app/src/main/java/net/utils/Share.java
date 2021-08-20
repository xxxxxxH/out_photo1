package net.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.basicmodel.R;
import net.utils.*;
import net.widget.DrawableSticker;
import net.widget.DrawableStickerPixel;
import net.widget.StickerPixel;

public class Share {

    public static int fromCamera = 0;

    public static int isfrom = 0;

    public static int SELECTED_VINTAGE_POS = 0;
    public static int SELECTED_OVERLAY_POS = 0;

    public static ArrayList<View> View_List_Effects = new ArrayList<>();
    public static ArrayList<View> View_List_Vintage = new ArrayList<>();

    public static Bitmap CROPPED_IMAGE = null;
    public static Bitmap EDITED_IMAGE = null;

    public static Bitmap IMAGE_BITMAP = null;
    public static Bitmap EFFECT_BITMAP = null;
    public static int EFFECT_BITMAP_OPACITY = 255;

    public static Uri BG_GALLERY = null;
    public static boolean isStickerTouch = false;
    public static boolean isStickerAvail = false;
    public static int STICKER_POSITION = 0;
    public static boolean FONT_FLAG = false;
    public static String FONT_TEXT = "";
    public static String FONT_STYLE = "";
    public static String FONT_EFFECT = "6";
    public static Integer COLOR = Color.parseColor("#00BFFF");
//    public static DrawableSticker TEXT_DRAWABLE;
public static DrawableSticker TEXT_DRAWABLE;
    public static boolean SPLASH_GALLERY_FLAG = false;
    public static int camera_activity_flag = 0;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static String imageUrl;
    public static boolean IsSelectFrame = false;
    public static DrawableStickerPixel TEXT_DRAWABLE1;

    public static ArrayList<View> View_List_Sticker = new ArrayList<> ();
    public class KEYNAME {
        public static final String ALBUM_ID = "album_id";
        public static final String ALBUM_NAME = "album_name";
        public static final String SELECTED_IMAGE = "selected_image";
        public static final String SELECTED_PHONE_IMAGE = "selected_phone_image";
    }

    public static Boolean Flag_First = true;
    public static Activity Activity_Gallery_View;
    public static String imgs = "";
    public static String msg;
    public static boolean is_home_back = false;
    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Photo Editor";
    public static String Fragment = "MyPhotosFragmen" + "t";
    public static Fragment selectedFragment;
    public static int IMG_HEIGHT = 0;
    public static int IMG_WIDTH = 0;
    public static boolean isFrameAvailable = false;
    public static String BG_COLOR = "#FFFFFF";
    public static Drawable EFFECT_DRAWABLE = null;
    public static int flag = 0;
    public static int COLOR_POS = 0;
    public static ArrayList<StickerPixel> drawable_list = new ArrayList<> ();
    public static int my_photos_position = 0;
    public static int my_favourite_position = 0;
    public static String image_path;
    public static Bitmap SELECTED_BITMAP;
    public static ArrayList<File> al_my_photos_photo = new ArrayList<>();
    public static ArrayList<File> al_my_photos_favourite = new ArrayList<>();
    public static Bitmap bitmapPhoto = null;
    public static Bitmap IMAGE_BITMAP_BACKGROUND_1;
    public static Bitmap image_bg = null;
    public static Bitmap image_fg = null;
    public static Bitmap IMAGE_BITMAP_BACKGROUND;
    public static Bitmap bitmapPhoto_background = null;
    public static int no_select_image = 1;
    public static int image_flag = 1;
    public static Bitmap IMAGE_BITMAP_1;
    public static int camera_flag = 0;
    public static Bitmap b = null;
    public static ArrayList<View> View_List_Effects_Background = new ArrayList<>();
    public static int effect_flag_bg = 0;
    public static int effect_flag_fg = 0;
    public static Bitmap SAVED_IMAGE = null;
    public static int resume_flag = 0;
    public static int last_posi = 0;
    public static String saveFaceInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);


        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        if (bitmapImage != null) {
            File mypath = new File(directory, "profile.png");
            Log.e("TAG", "" + mypath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);

                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "Not Saved Image------------------------------------------------------->");
        }
        return directory.getAbsolutePath();
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyTheme);
        try {
            if (dialog.isShowing()) {
                dialog.dismiss();
                createProgressDialog(mContext);
            } else {
                dialog.show();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setProgressDrawable((new ColorDrawable(Color.parseColor("#E45E46"))));
        dialog.setContentView(R.layout.progress_dialog_layout);
        return dialog;
    }

    public static Dialog showProgress(Context mContext, String text) {

        Dialog mDialog = new Dialog(mContext, R.style.MyTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.dialog_progress, null);
        mDialog.setContentView(layout);

        TextView mTextView = (TextView) layout.findViewById(R.id.text);
        if (text.equals(""))
            mTextView.setVisibility(View.GONE);
        else
            mTextView.setText(text);

        mDialog.setCancelable(false);

        return mDialog;
    }

    public static void showAlert(final Activity activity, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyAlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public static Boolean RestartApp(Activity activity) {

        if (!Share.checkAndRequestPermissionss(activity, 1)) {
            Intent i = activity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAndRequestPermissionss(Activity act, int code) {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
}
