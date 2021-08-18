package net.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import net.utils.*;
import net.widget.DrawableSticker;

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
}
