package net.basicmodel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.makeramen.roundedimageview.RoundedImageView;

import net.entity.DimensionsModel;
import net.utils.DisplayMetricsHandler;
import net.utils.Share;
import net.utils.SharedPrefs;
import net.widget.DrawableStickerPip;
import net.widget.MaskableFrameLayout;
import net.widget.StickerPip;
import net.widget.StickerViewPip;
import net.widget.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;

public class PhotoDisplayPipActivity extends AppCompatActivity implements View.OnClickListener {

    private final int STORAGE_PERMISSION_CODE = 23;
    private static final int REQUEST_SETTINGS_PERMISSION = 102;
    private final List<String> listPermissionsNeeded = new ArrayList<>();

    String TAG = "TAG";

    Activity activity;

    AsyncTask save_task;
    Dialog saveDialog;

    private AssetManager assetManager;
    TouchImageView imag_display, bg_img_display;
    RelativeLayout l1;
    ImageView watch;
    boolean isInForeGround;
    TouchImageView imag_display_02, imag_display_03, imag_display_04, imag_display_05, imag_display_06, imag_display_07, imag_display_08, imag_display_09, imag_display_10, imag_display_11, imag_display_12,
            imag_display_13, imag_display_14, imag_display_15, imag_display_16, imag_display_17, imag_display_18;
    RelativeLayout l2;
    ImageView glass, frame_03, frame_04, frame_05, frame_06, frame_07, frame_08, frame_09, frame_10, frame_11, frame_12, frame_13, frame_14, frame_15, frame_16, frame_17, frame_18;

    public float img_divice_width;

    public int frame_position, frame_posi, posi_fram, frame_position1;

    ByteArrayOutputStream bytearrayoutputstream;

    Bitmap compress_img_path_drawable, bg_img_path_drawable;
    public int pos = -1;
    public int position;
    public int pos1 = -1;
    public int position1;

    private final String[] filter_name = {"CONTRAST", "INVERT", "PIXELATION", "HUE",
            "GAMMA", "SEPIA", "GRAYSCALE", "SHARPEN", "EMBOSS",
            "SOBEL_EDGE_DETECTION", "POSTERIZE", "FILTER_GROUP", "SATURATION", "VIGNETTE",
            "KUWAHARA", "SKETCH", "TOON", "HAZE", "LEVELS_FILTER_MIN"};
    GPUImage gpu_image_filter, gpu_image_filter_background;
    private FilterList filters;


    public String path_bg, path_fg;
    ImageView save, back, my_photos;
    ImageView iv_effect_item, iv_effect_item1;
    ImageView iv_no_effect_background, iv_no_effect_foreground;
    float devicewidth;
    public static String SUB_FRAME_NAME;

    RelativeLayout option_layout;
    public static ProgressDialog pd;

    HorizontalScrollView hv_scroll_sticker, hv_scroll_foreground_effect, hv_scroll_frames, hv_scroll_bg_effect;
    LinearLayout ll_main, layout, ll_row_sticker, ll_main_linearlayout, ll_menu, ll_row_foreground_effect, ll_row_frames, ll_row_bg_effect;

    GridView frame_grid;
    RelativeLayout rl_background, iv_cancel, main_layout;
    LinearLayout all_menu_with_bg;

    ImageView pip, backgound_image, forground_image, sticker, options;

    ImageView iv_sticker_item, iv_overlay_item, setframe, iv_overlay_item_backgroung;

    RoundedImageView iv_frame_item;

    RelativeLayout frame_01, frame_02;

    LinearLayout frame_01_layout, frame_02_layout, frame_03_layout, frame_04_layout, frame_05_layout,
            frame_06_layout,
            frame_07_layout,
            frame_08_layout,
            frame_09_layout,
            frame_10_layout,
            frame_11_layout,
            frame_12_layout,
            frame_13_layout, frame_14_layout, frame_15_layout,
            frame_16_layout,
            frame_17_layout, frame_18_layout;

    Animation bottomDown;
    String compress_img_path, bg_img_path;

    public static StickerViewPip sticker_view;

    public static Integer clicked = 0;

    public static Bitmap EFFECT_BITMAP_FOREGROUND;
    public static Bitmap IMAGE_BITMAP_FOREGROUND;


    public static Bitmap EFFECT_BITMAP_BACKGROUND;
    public static Bitmap IMAGE_BITMAP_BACKGROUNG;


    public static Drawable FONT_STICKER_DRAWABLE = null;


    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;


    Bitmap blurred;

    public static FrameLayout frame_layout1, frame_layout2, frame_layout3, frame_layout4, frame_layout5, frame_layout6, frame_layout7, frame_layout8, frame_layout9, frame_layout10, frame_layout11, frame_layout12,
            frame_layout13, frame_layout14, frame_layout15, frame_layout16, frame_layout17, frame_layout18;

    ArrayList<DimensionsModel> frame1_dimen_list = new ArrayList<>();
    float f1x, f1y, f1width, f1height;


    ArrayList<DimensionsModel> frame2_dimen_list = new ArrayList<>();
    float f2x, f2y, f2width, f2height;

    ArrayList<DimensionsModel> frame3_dimen_list = new ArrayList<>();
    float f3x, f3y, f3width, f3height;


    ArrayList<DimensionsModel> frame4_dimen_list = new ArrayList<>();
    float f4x, f4y, f4width, f4height;

    ArrayList<DimensionsModel> frame5_dimen_list = new ArrayList<>();
    float f5x, f5y, f5width, f5height;

    ArrayList<DimensionsModel> frame6_dimen_list = new ArrayList<>();
    float f6x, f6y, f6width, f6height;

    ArrayList<DimensionsModel> frame7_dimen_list = new ArrayList<>();
    float f7x, f7y, f7width, f7height;

    ArrayList<DimensionsModel> frame8_dimen_list = new ArrayList<>();
    float f8x, f8y, f8width, f8height;

    ArrayList<DimensionsModel> frame9_dimen_list = new ArrayList<>();
    float f9x, f9y, f9width, f9height;

    ArrayList<DimensionsModel> frame10_dimen_list = new ArrayList<>();
    float f10x, f10y, f10width, f10height;

    ArrayList<DimensionsModel> frame11_dimen_list = new ArrayList<>();
    float f11x, f11y, f11width, f11height;

    ArrayList<DimensionsModel> frame12_dimen_list = new ArrayList<>();
    float f12x, f12y, f12width, f12height;

    ArrayList<DimensionsModel> frame13_dimen_list = new ArrayList<>();
    float f13x, f13y, f13width, f13height;

    ArrayList<DimensionsModel> frame14_dimen_list = new ArrayList<>();
    float f14x, f14y, f14width, f14height;

    ArrayList<DimensionsModel> frame15_dimen_list = new ArrayList<>();
    float f15x, f15y, f15width, f15height;

    ArrayList<DimensionsModel> frame16_dimen_list = new ArrayList<>();
    float f16x, f16y, f16width, f16height;

    ArrayList<DimensionsModel> frame17_dimen_list = new ArrayList<>();
    float f17x, f17y, f17width, f17height;

    ArrayList<DimensionsModel> frame18_dimen_list = new ArrayList<>();
    float f18x, f18y, f18width, f18height;


    public static int sticker_flag = 0;

    public static Bitmap SAVED_BITMAP;

    public static final String PREFS_NAME = "bg_fg";
    SharedPreferences applicationpreferences;

    SharedPreferences.Editor editor;

    MaskableFrameLayout maskableFrameLayout1, maskableFrameLayout2, maskableFrameLayout3, maskableFrameLayout4, maskableFrameLayout5,
            maskableFrameLayout6,
            maskableFrameLayout7,
            maskableFrameLayout8,
            maskableFrameLayout9,
            maskableFrameLayout10,
            maskableFrameLayout11,
            maskableFrameLayout12,
            maskableFrameLayout13,
            maskableFrameLayout14,
            maskableFrameLayout15,
            maskableFrameLayout16,
            maskableFrameLayout17,
            maskableFrameLayout18;

    LinearLayout linearLayout;

    private enum FilterType {
        CONTRAST, INVERT, PIXELATION, HUE, GAMMA, SEPIA, GRAYSCALE, SHARPEN, EMBOSS, SOBEL_EDGE_DETECTION,
        POSTERIZE, FILTER_GROUP, SATURATION, VIGNETTE, KUWAHARA, SKETCH, TOON, HAZE, LEVELS_FILTER_MIN
    }

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        Log.i("xxxxxxH", "PhotoDisplayPipActivity");
        if (Share.RestartApp(this)) {

            activity = PhotoDisplayPipActivity.this;

            Log.e("Photo activity onCreate", "Share.imgs" + Share.imgs);

            gpu_image_filter = new GPUImage(PhotoDisplayPipActivity.this);
            gpu_image_filter_background = new GPUImage(PhotoDisplayPipActivity.this);
            filters = new FilterList();

            filters.addFilter("Contrast", FilterType.CONTRAST);
            filters.addFilter("Invert", FilterType.INVERT);
            filters.addFilter("Pixelation", FilterType.PIXELATION);
            filters.addFilter("Hue", FilterType.HUE);
            filters.addFilter("Gamma", FilterType.GAMMA);
            filters.addFilter("Sepia", FilterType.SEPIA);
            filters.addFilter("Grayscale", FilterType.GRAYSCALE);
            filters.addFilter("Sharpness", FilterType.SHARPEN);
            filters.addFilter("Emboss", FilterType.EMBOSS);
            filters.addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION);
            filters.addFilter("Posterize", FilterType.POSTERIZE);
            filters.addFilter("Grouped filters", FilterType.FILTER_GROUP);
            filters.addFilter("Saturation", FilterType.SATURATION);
            filters.addFilter("Vignette", FilterType.VIGNETTE);
            filters.addFilter("Kuwahara", FilterType.KUWAHARA);
            filters.addFilter("Sketch", FilterType.SKETCH);
            filters.addFilter("Toon", FilterType.TOON);
            filters.addFilter("Haze", FilterType.HAZE);
            filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);

            bytearrayoutputstream = new ByteArrayOutputStream();


            linearLayout = findViewById(R.id.linearLayout);
            back = findViewById(R.id.iv_back);
            save = findViewById(R.id.iv_done);
            imag_display = findViewById(R.id.image_display);
            bg_img_display = findViewById(R.id.bg_image_display);
            watch = findViewById(R.id.watch_img);
            l1 = findViewById(R.id.center_image_layout_01);

            imag_display_02 = findViewById(R.id.image_display_02);

            glass = findViewById(R.id.frame_glass);

            imag_display_03 = findViewById(R.id.image_display_03);
            frame_03 = findViewById(R.id.frame_03);

            imag_display_04 = findViewById(R.id.image_display_04);
            frame_04 = findViewById(R.id.frame_04);

            imag_display_05 = findViewById(R.id.image_display_05);
            frame_05 = findViewById(R.id.frame_05);

            imag_display_06 = findViewById(R.id.image_display_06);
            frame_06 = findViewById(R.id.frame_06);

            imag_display_07 = findViewById(R.id.image_display_07);
            frame_07 = findViewById(R.id.frame_07);

            imag_display_08 = findViewById(R.id.image_display_08);
            frame_08 = findViewById(R.id.frame_08);

            imag_display_09 = findViewById(R.id.image_display_09);
            frame_09 = findViewById(R.id.frame_09);

            imag_display_10 = findViewById(R.id.image_display_10);
            frame_10 = findViewById(R.id.frame_10);

            imag_display_11 = findViewById(R.id.image_display_11);
            frame_11 = findViewById(R.id.frame_11);

            imag_display_12 = findViewById(R.id.image_display_12);
            frame_12 = findViewById(R.id.frame_12);

            imag_display_13 = findViewById(R.id.image_display_13);
            frame_13 = findViewById(R.id.frame_13);

            imag_display_14 = findViewById(R.id.image_display_14);
            frame_14 = findViewById(R.id.frame_14);

            imag_display_15 = findViewById(R.id.image_display_15);
            frame_15 = findViewById(R.id.frame_15);

            imag_display_16 = findViewById(R.id.image_display_16);
            frame_16 = findViewById(R.id.frame_16);

            imag_display_17 = findViewById(R.id.image_display_17);
            frame_17 = findViewById(R.id.frame_17);

            imag_display_18 = findViewById(R.id.image_display_18);
            frame_18 = findViewById(R.id.frame_18);


            hv_scroll_sticker = findViewById(R.id.hv_scroll_sticker);
            hv_scroll_foreground_effect = findViewById(R.id.hv_scroll_foreground_effect);
            hv_scroll_bg_effect = findViewById(R.id.hv_scroll_bg_effect);
            hv_scroll_frames = findViewById(R.id.hv_scroll_frames);

            pip = findViewById(R.id.pip);
            backgound_image = findViewById(R.id.background);
            forground_image = findViewById(R.id.foreground);
            sticker = findViewById(R.id.sticker);
            options = findViewById(R.id.option);
            //my_photos = (ImageView) findViewById (R.id.my_photos);


            iv_no_effect_background = findViewById(R.id.iv_no_effect_background);
            iv_no_effect_foreground = findViewById(R.id.iv_no_effect_foreground);
            ll_menu = findViewById(R.id.ll_menu);
            all_menu_with_bg = findViewById(R.id.all_menu_with_bg);
            ll_row_sticker = findViewById(R.id.ll_row_sticker);
            ll_row_foreground_effect = findViewById(R.id.ll_row_foreground_effect);
            ll_row_bg_effect = findViewById(R.id.ll_row_bg_effect);
            ll_row_frames = findViewById(R.id.ll_row_frames);
            rl_background = findViewById(R.id.rl_background);
            iv_cancel = findViewById(R.id.iv_cancel);


            frame_02_layout = findViewById(R.id.frame_glass_layout);

            frame_01_layout = findViewById(R.id.frame_watch_layout);
            frame_03_layout = findViewById(R.id.frame_03_layout);
            frame_04_layout = findViewById(R.id.frame_04_layout);
            frame_05_layout = findViewById(R.id.frame_05_layout);
            frame_06_layout = findViewById(R.id.frame_06_layout);
            frame_07_layout = findViewById(R.id.frame_07_layout);
            frame_08_layout = findViewById(R.id.frame_08_layout);
            frame_09_layout = findViewById(R.id.frame_09_layout);
            frame_10_layout = findViewById(R.id.frame_10_layout);
            frame_11_layout = findViewById(R.id.frame_11_layout);
            frame_12_layout = findViewById(R.id.frame_12_layout);
            frame_13_layout = findViewById(R.id.frame_13_layout);
            frame_14_layout = findViewById(R.id.frame_14_layout);
            frame_15_layout = findViewById(R.id.frame_15_layout);
            frame_16_layout = findViewById(R.id.frame_16_layout);
            frame_17_layout = findViewById(R.id.frame_17_layout);
            frame_18_layout = findViewById(R.id.frame_18_layout);

            frame_01 = findViewById(R.id.frame_01_watch);
            sticker_view = findViewById(R.id.sticker_view);

            assetManager = getAssets();
            System.gc();
            Runtime.getRuntime().gc();


            sticker_view.setOnStickerOperationListener(new StickerViewPip.OnStickerOperationListener() {
                @Override
                public void onStickerClicked(StickerPip sticker) {

                }

                @Override
                public void onStickerDeleted(StickerPip sticker) {
                    Log.e("TAG", "sticker_deleted");

                }

                @Override
                public void onStickerDragFinished(StickerPip sticker) {

                }

                @Override
                public void onStickerZoomFinished(StickerPip sticker) {

                }

                @Override
                public void onStickerFlipped(StickerPip sticker) {

                }
            });


            pip.setOnClickListener(this);
            backgound_image.setOnClickListener(this);
            forground_image.setOnClickListener(this);
            sticker.setOnClickListener(this);
            options.setOnClickListener(this);
            iv_cancel.setOnClickListener(this);

            back.setOnClickListener(this);
            save.setOnClickListener(this);
            //my_photos.setOnClickListener (this);
            iv_no_effect_background.setOnClickListener(this);
            iv_no_effect_foreground.setOnClickListener(this);


            frame_layout1 = findViewById(R.id.frame_layout1);
            frame_layout1 = findViewById(R.id.frame_layout1);
            frame_layout2 = findViewById(R.id.frame_layout2);
            frame_layout3 = findViewById(R.id.frame_layout3);
            frame_layout4 = findViewById(R.id.frame_layout4);
            frame_layout5 = findViewById(R.id.frame_layout5);
            frame_layout6 = findViewById(R.id.frame_layout6);
            frame_layout7 = findViewById(R.id.frame_layout7);
            frame_layout8 = findViewById(R.id.frame_layout8);
            frame_layout9 = findViewById(R.id.frame_layout9);
            frame_layout10 = findViewById(R.id.frame_layout10);
            frame_layout11 = findViewById(R.id.frame_layout11);
            frame_layout12 = findViewById(R.id.frame_layout12);
            frame_layout13 = findViewById(R.id.frame_layout13);
            frame_layout14 = findViewById(R.id.frame_layout14);
            frame_layout15 = findViewById(R.id.frame_layout15);
            frame_layout16 = findViewById(R.id.frame_layout16);
            frame_layout17 = findViewById(R.id.frame_layout17);
            frame_layout18 = findViewById(R.id.frame_layout18);


            maskableFrameLayout1 = findViewById(R.id.maskable_layout01);

            maskableFrameLayout2 = findViewById(R.id.maskable_layout02);

            maskableFrameLayout3 = findViewById(R.id.maskable_layout03);

            maskableFrameLayout4 = findViewById(R.id.maskable_layout04);

            maskableFrameLayout5 = findViewById(R.id.maskable_layout05);
            maskableFrameLayout6 = findViewById(R.id.maskable_layout06);
            maskableFrameLayout7 = findViewById(R.id.maskable_layout07);
            maskableFrameLayout8 = findViewById(R.id.maskable_layout08);
            maskableFrameLayout9 = findViewById(R.id.maskable_layout09);
            maskableFrameLayout10 = findViewById(R.id.maskable_layout10);
            maskableFrameLayout11 = findViewById(R.id.maskable_layout11);
            maskableFrameLayout12 = findViewById(R.id.maskable_layout12);
            maskableFrameLayout13 = findViewById(R.id.maskable_layout13);
            maskableFrameLayout14 = findViewById(R.id.maskable_layout14);
            maskableFrameLayout15 = findViewById(R.id.maskable_layout15);
            maskableFrameLayout16 = findViewById(R.id.maskable_layout16);
            maskableFrameLayout17 = findViewById(R.id.maskable_layout17);
            maskableFrameLayout18 = findViewById(R.id.maskable_layout18);


            l1.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            l1.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            img_divice_width = DisplayMetricsHandler.getScreenWidth();
            new waiting().execute();
            init_frame1_dimensions();
            init_frame2_dimensions();
            init_frame3_dimensions();
            init_frame4_dimensions();
            init_frame5_dimensions();
            init_frame6_dimensions();
            init_frame7_dimensions();
            init_frame8_dimensions();
            init_frame9_dimensions();
            init_frame10_dimensions();
            init_frame11_dimensions();
            init_frame12_dimensions();
            init_frame13_dimensions();
            init_frame14_dimensions();
            init_frame15_dimensions();
            init_frame16_dimensions();
            init_frame17_dimensions();
            init_frame18_dimensions();


            watch.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            watch.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            bg_img_display.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            bg_img_display.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());


            glass.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            glass.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_03.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_03.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_04.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_04.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_05.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_05.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_06.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_06.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_07.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_07.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_08.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_08.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_09.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_09.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_10.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_10.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_11.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_11.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_12.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_12.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_13.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_13.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_14.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_14.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_15.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_15.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_16.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_16.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_17.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_17.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            frame_18.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            frame_18.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            sticker_view.getLayoutParams().height = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());
            sticker_view.getLayoutParams().width = (int) (1.0 * DisplayMetricsHandler.getScreenWidth());

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            Log.d("TAG", "width     " + width);
            Log.d("TAG", "height     " + height);
        }

    }

    private class waiting extends AsyncTask<Void, Void, Void> {


        ProgressDialog effect_progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            effect_progress = Share.createProgressDialog(PhotoDisplayPipActivity.this);
            effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {


            set_bg_fg_images();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            System.gc();
            Runtime.getRuntime().gc();
            effect_progress.dismiss();
            setimages();
            super.onPostExecute(aVoid);


        }
    }

    private void setimages() {

        imag_display.setImageBitmap(bg_img_path_drawable);
        imag_display_02.setImageBitmap(bg_img_path_drawable);
        imag_display_03.setImageBitmap(bg_img_path_drawable);
        imag_display_04.setImageBitmap(bg_img_path_drawable);
        imag_display_05.setImageBitmap(bg_img_path_drawable);
        imag_display_06.setImageBitmap(bg_img_path_drawable);
        imag_display_07.setImageBitmap(bg_img_path_drawable);
        imag_display_08.setImageBitmap(bg_img_path_drawable);
        imag_display_09.setImageBitmap(bg_img_path_drawable);
        imag_display_10.setImageBitmap(bg_img_path_drawable);
        imag_display_11.setImageBitmap(bg_img_path_drawable);
        imag_display_12.setImageBitmap(bg_img_path_drawable);
        imag_display_13.setImageBitmap(bg_img_path_drawable);
        imag_display_14.setImageBitmap(bg_img_path_drawable);
        imag_display_15.setImageBitmap(bg_img_path_drawable);
        imag_display_16.setImageBitmap(bg_img_path_drawable);
        imag_display_17.setImageBitmap(bg_img_path_drawable);
        imag_display_18.setImageBitmap(bg_img_path_drawable);

        gpu_image_filter.setImage(Share.bitmapPhoto);


        Log.e("TAG", "posi  ==" + frame_posi);
        Log.e("TAG", "posi1  ==" + frame_position1);
        if (frame_position1 == 0) {
            init_frame1();

            maskableFrameLayout1.setMask(R.mipmap.circle);
            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.VISIBLE);
            //maskableFrameLayout3.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.VISIBLE);
            frame_02_layout.setVisibility(View.GONE);

        } else if (frame_position1 == 1) {


            init_frame2();
            maskableFrameLayout2.setMask(R.mipmap.mask_glass);


            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.VISIBLE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.VISIBLE);

        } else if (frame_position1 == 2) {

            init_frame3();

            maskableFrameLayout3.setMask(R.mipmap.mask_frame_03);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.VISIBLE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            //maskableFrameLayout3.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.VISIBLE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);

        } else if (frame_position1 == 3) {
            init_frame4();

            maskableFrameLayout4.setMask(R.mipmap.mask_frame_04);

            frame_layout4.setVisibility(View.VISIBLE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.VISIBLE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 4) {
            init_frame5();

            maskableFrameLayout5.setMask(R.mipmap.mask_frame_05);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.VISIBLE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.VISIBLE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 5) {
            init_frame6();

            maskableFrameLayout6.setMask(R.mipmap.mask_frame_06);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.VISIBLE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.VISIBLE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);

        } else if (frame_position1 == 6) {
            init_frame7();

            maskableFrameLayout7.setMask(R.mipmap.mask_frame_07);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.VISIBLE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.VISIBLE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);

        } else if (frame_position1 == 7) {
            init_frame8();

            maskableFrameLayout8.setMask(R.mipmap.mask_frame_08);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.VISIBLE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.VISIBLE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);
        } else if (frame_position1 == 8) {
            init_frame9();

            maskableFrameLayout9.setMask(R.mipmap.mask_frame_09);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.VISIBLE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.VISIBLE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 9) {
            init_frame10();

            maskableFrameLayout10.setMask(R.mipmap.mask_frame_10);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.VISIBLE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.VISIBLE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 10) {
            init_frame11();

            maskableFrameLayout11.setMask(R.mipmap.mask_frame_11);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.VISIBLE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.VISIBLE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 11) {
            init_frame12();

            maskableFrameLayout12.setMask(R.mipmap.mask_frame_12);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.VISIBLE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.VISIBLE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 12) {
            init_frame13();

            maskableFrameLayout13.setMask(R.mipmap.mask_frame_13);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.VISIBLE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.VISIBLE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 13) {

            init_frame14();

            maskableFrameLayout14.setMask(R.mipmap.mask_frame_14);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.VISIBLE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.VISIBLE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 14) {
            init_frame15();

            maskableFrameLayout15.setMask(R.mipmap.mask_frame_15);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.VISIBLE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);

            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.VISIBLE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);

            frame_01_layout.setVisibility(View.GONE);

            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 15) {
            init_frame16();

            maskableFrameLayout16.setMask(R.mipmap.mask_frame_16);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.VISIBLE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);

            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.VISIBLE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);

            frame_01_layout.setVisibility(View.GONE);

            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 16) {
            init_frame17();

            maskableFrameLayout17.setMask(R.mipmap.mask_frame_017);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.VISIBLE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);

            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.VISIBLE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else if (frame_position1 == 17) {
            init_frame18();

            maskableFrameLayout18.setMask(R.mipmap.mask_frame_18);

            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.VISIBLE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.GONE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.VISIBLE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.GONE);
            frame_02_layout.setVisibility(View.GONE);


        } else {

            init_frame1();

            maskableFrameLayout1.setMask(R.mipmap.circle);
            frame_layout4.setVisibility(View.GONE);
            frame_layout5.setVisibility(View.GONE);
            frame_layout6.setVisibility(View.GONE);
            frame_layout7.setVisibility(View.GONE);
            frame_layout8.setVisibility(View.GONE);
            frame_layout9.setVisibility(View.GONE);
            frame_layout10.setVisibility(View.GONE);
            frame_layout11.setVisibility(View.GONE);
            frame_layout12.setVisibility(View.GONE);
            frame_layout13.setVisibility(View.GONE);
            frame_layout14.setVisibility(View.GONE);
            frame_layout15.setVisibility(View.GONE);
            frame_layout16.setVisibility(View.GONE);
            frame_layout17.setVisibility(View.GONE);
            frame_layout18.setVisibility(View.GONE);
            frame_layout3.setVisibility(View.GONE);
            frame_layout2.setVisibility(View.GONE);
            frame_layout1.setVisibility(View.VISIBLE);
            frame_04_layout.setVisibility(View.GONE);
            frame_05_layout.setVisibility(View.GONE);
            frame_06_layout.setVisibility(View.GONE);
            frame_07_layout.setVisibility(View.GONE);
            frame_08_layout.setVisibility(View.GONE);
            frame_09_layout.setVisibility(View.GONE);
            frame_10_layout.setVisibility(View.GONE);
            frame_11_layout.setVisibility(View.GONE);
            frame_12_layout.setVisibility(View.GONE);
            frame_13_layout.setVisibility(View.GONE);
            frame_14_layout.setVisibility(View.GONE);
            frame_15_layout.setVisibility(View.GONE);
            frame_16_layout.setVisibility(View.GONE);
            frame_17_layout.setVisibility(View.GONE);
            frame_18_layout.setVisibility(View.GONE);
            frame_03_layout.setVisibility(View.GONE);
            frame_01_layout.setVisibility(View.VISIBLE);
            frame_02_layout.setVisibility(View.GONE);

        }


        IMAGE_BITMAP_FOREGROUND = bg_img_path_drawable;

        Log.d("bitmap  :   ", "" + IMAGE_BITMAP_FOREGROUND);
        blurred = fastblur(compress_img_path_drawable, 1, 10);

        bg_img_display.setImageDrawable(new BitmapDrawable(getResources(), blurred));


        Share.IMAGE_BITMAP_BACKGROUND_1 = fastblur(Share.image_bg, 1, 10);
        Share.IMAGE_BITMAP_BACKGROUND = blurred;

        Log.e("TAG", "bitmap background  ,,,," + Share.IMAGE_BITMAP_BACKGROUND);
        Share.bitmapPhoto_background = Share.IMAGE_BITMAP_BACKGROUND_1;
        gpu_image_filter_background.setImage(Share.bitmapPhoto_background);

        Log.e("TAG", "bitmap background 11 ,,,," + Share.bitmapPhoto_background);


    }

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        System.gc();
        Runtime.getRuntime().gc();


        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    private void init_frame1() {

        Log.e("width_subframe_1", frame1_dimen_list.get(0).getX() + "");
        int constant = 1080;
        Log.e("device_width_1", devicewidth + "");

        maskableFrameLayout1.getLayoutParams().width = frame1_dimen_list.get(0).getWidth();
        maskableFrameLayout1.getLayoutParams().height = frame1_dimen_list.get(0).getHeight();
        maskableFrameLayout1.setX(frame1_dimen_list.get(0).getX());//left margin
        maskableFrameLayout1.setY(frame1_dimen_list.get(0).getY());//topmargin...

    }


    private void init_frame2() {

        Log.e("width_subframe_2", frame2_dimen_list.get(0).getX() + "");
        int constant = 1080;
        Log.e("device_width_2", devicewidth + "");

        Log.e("left_margin", "" + frame2_dimen_list.get(0).getX());

        maskableFrameLayout2.getLayoutParams().width = frame2_dimen_list.get(0).getWidth();
        maskableFrameLayout2.getLayoutParams().height = frame2_dimen_list.get(0).getHeight();
        maskableFrameLayout2.setX(frame2_dimen_list.get(0).getX());//left margin
        maskableFrameLayout2.setY(frame2_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame3() {

        Log.e("width_subframe_3", frame3_dimen_list.get(0).getX() + "");
        int constant = 480;
        Log.e("device_width_3", devicewidth + "");


        maskableFrameLayout3.getLayoutParams().width = frame3_dimen_list.get(0).getWidth();
        maskableFrameLayout3.getLayoutParams().height = frame3_dimen_list.get(0).getHeight();
        maskableFrameLayout3.setX(frame3_dimen_list.get(0).getX());//left margin
        maskableFrameLayout3.setY(frame3_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame5() {

        Log.e("width_subframe_5", frame5_dimen_list.get(0).getX() + "");
        int constant = 580;
        Log.e("device_width_5", devicewidth + "");


        maskableFrameLayout5.getLayoutParams().width = frame5_dimen_list.get(0).getWidth();
        maskableFrameLayout5.getLayoutParams().height = frame5_dimen_list.get(0).getHeight();
        maskableFrameLayout5.setX(frame5_dimen_list.get(0).getX());//left margin
        maskableFrameLayout5.setY(frame5_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame4() {

        Log.e("width_subframe_4", frame4_dimen_list.get(0).getX() + "");
        int constant = 480;
        Log.e("device_width_4", devicewidth + "");


        maskableFrameLayout4.getLayoutParams().width = frame4_dimen_list.get(0).getWidth();
        maskableFrameLayout4.getLayoutParams().height = frame4_dimen_list.get(0).getHeight();
        maskableFrameLayout4.setX(frame4_dimen_list.get(0).getX());//left margin
        maskableFrameLayout4.setY(frame4_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame6() {

        Log.e("width_subframe_6", frame6_dimen_list.get(0).getX() + "");
        int constant = 680;
        Log.e("device_width_6", devicewidth + "");


        maskableFrameLayout6.getLayoutParams().width = frame6_dimen_list.get(0).getWidth();
        maskableFrameLayout6.getLayoutParams().height = frame6_dimen_list.get(0).getHeight();
        maskableFrameLayout6.setX(frame6_dimen_list.get(0).getX());//left margin
        maskableFrameLayout6.setY(frame6_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame7() {

        Log.e("width_subframe_7", frame7_dimen_list.get(0).getX() + "");
        int constant = 780;
        Log.e("device_width_7", devicewidth + "");


        maskableFrameLayout7.getLayoutParams().width = frame7_dimen_list.get(0).getWidth();
        maskableFrameLayout7.getLayoutParams().height = frame7_dimen_list.get(0).getHeight();
        maskableFrameLayout7.setX(frame7_dimen_list.get(0).getX());//left margin
        maskableFrameLayout7.setY(frame7_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame8() {

        Log.e("width_subframe_8", frame8_dimen_list.get(0).getX() + "");
        int constant = 880;
        Log.e("device_width_8", devicewidth + "");

        maskableFrameLayout8.getLayoutParams().width = frame8_dimen_list.get(0).getWidth();
        maskableFrameLayout8.getLayoutParams().height = frame8_dimen_list.get(0).getHeight();
        maskableFrameLayout8.setX(frame8_dimen_list.get(0).getX());//left margin
        maskableFrameLayout8.setY(frame8_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame9() {

        Log.e("width_subframe_9", frame9_dimen_list.get(0).getX() + "");
        int constant = 980;
        Log.e("device_width_9", devicewidth + "");

        maskableFrameLayout9.getLayoutParams().width = frame9_dimen_list.get(0).getWidth();
        maskableFrameLayout9.getLayoutParams().height = frame9_dimen_list.get(0).getHeight();
        maskableFrameLayout9.setX(frame9_dimen_list.get(0).getX());//left margin
        maskableFrameLayout9.setY(frame9_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame10() {

        Log.e("width_subframe_10", frame10_dimen_list.get(0).getX() + "");
        int constant = 1080;
        Log.e("device_width_10", devicewidth + "");

        maskableFrameLayout10.getLayoutParams().width = frame10_dimen_list.get(0).getWidth();
        maskableFrameLayout10.getLayoutParams().height = frame10_dimen_list.get(0).getHeight();
        maskableFrameLayout10.setX(frame10_dimen_list.get(0).getX());//left margin
        maskableFrameLayout10.setY(frame10_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame11() {

        Log.e("width_subframe_11", frame11_dimen_list.get(0).getX() + "");
        int constant = 1180;
        Log.e("device_width_11", devicewidth + "");

        maskableFrameLayout11.getLayoutParams().width = frame11_dimen_list.get(0).getWidth();
        maskableFrameLayout11.getLayoutParams().height = frame11_dimen_list.get(0).getHeight();
        maskableFrameLayout11.setX(frame11_dimen_list.get(0).getX());//left margin
        maskableFrameLayout11.setY(frame11_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame12() {

        Log.e("width_subframe_12", frame12_dimen_list.get(0).getX() + "");
        int constant = 1280;
        Log.e("device_width_12", devicewidth + "");

        maskableFrameLayout12.getLayoutParams().width = frame12_dimen_list.get(0).getWidth();
        maskableFrameLayout12.getLayoutParams().height = frame12_dimen_list.get(0).getHeight();
        maskableFrameLayout12.setX(frame12_dimen_list.get(0).getX());//left margin
        maskableFrameLayout12.setY(frame12_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame13() {

        Log.e("width_subframe_13", frame13_dimen_list.get(0).getX() + "");
        int constant = 1380;
        Log.e("device_width_13", devicewidth + "");

        maskableFrameLayout13.getLayoutParams().width = frame13_dimen_list.get(0).getWidth();
        maskableFrameLayout13.getLayoutParams().height = frame13_dimen_list.get(0).getHeight();
        maskableFrameLayout13.setX(frame13_dimen_list.get(0).getX());//left margin
        maskableFrameLayout13.setY(frame13_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame14() {

        Log.e("width_subframe_14", frame14_dimen_list.get(0).getX() + "");
        int constant = 1480;
        Log.e("device_width_14", devicewidth + "");

        maskableFrameLayout14.getLayoutParams().width = frame14_dimen_list.get(0).getWidth();
        maskableFrameLayout14.getLayoutParams().height = frame14_dimen_list.get(0).getHeight();
        maskableFrameLayout14.setX(frame14_dimen_list.get(0).getX());//left margin
        maskableFrameLayout14.setY(frame14_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame15() {

        Log.e("width_subframe_15", frame15_dimen_list.get(0).getX() + "");
        int constant = 1580;
        Log.e("device_width_15", devicewidth + "");

        maskableFrameLayout15.getLayoutParams().width = frame15_dimen_list.get(0).getWidth();
        maskableFrameLayout15.getLayoutParams().height = frame15_dimen_list.get(0).getHeight();
        maskableFrameLayout15.setX(frame15_dimen_list.get(0).getX());//left margin
        maskableFrameLayout15.setY(frame15_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame16() {

        Log.e("width_subframe_16", frame16_dimen_list.get(0).getX() + "");
        int constant = 1680;
        Log.e("device_width_16", devicewidth + "");

        maskableFrameLayout16.getLayoutParams().width = frame16_dimen_list.get(0).getWidth();
        maskableFrameLayout16.getLayoutParams().height = frame16_dimen_list.get(0).getHeight();
        maskableFrameLayout16.setX(frame16_dimen_list.get(0).getX());//left margin
        maskableFrameLayout16.setY(frame16_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame17() {

        Log.e("width_subframe_17", frame17_dimen_list.get(0).getX() + "");
        int constant = 1780;
        Log.e("device_width_17", devicewidth + "");

        maskableFrameLayout17.getLayoutParams().width = frame17_dimen_list.get(0).getWidth();
        maskableFrameLayout17.getLayoutParams().height = frame17_dimen_list.get(0).getHeight();
        maskableFrameLayout17.setX(frame17_dimen_list.get(0).getX());//left margin
        maskableFrameLayout17.setY(frame17_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame18() {

        Log.e("width_subframe_18", frame18_dimen_list.get(0).getX() + "");
        int constant = 1880;
        Log.e("device_width_18", devicewidth + "");

        maskableFrameLayout18.getLayoutParams().width = frame18_dimen_list.get(0).getWidth();
        maskableFrameLayout18.getLayoutParams().height = frame18_dimen_list.get(0).getHeight();
        maskableFrameLayout18.setX(frame18_dimen_list.get(0).getX());//left margin
        maskableFrameLayout18.setY(frame18_dimen_list.get(0).getY());//topmargin...

    }

    private void init_frame1_dimensions() {

        //frame1_dimen_list.add(new DimensionsModel(width, height, left margin, top margin));

        Log.d("TAG", "get widthof device  : " + img_divice_width);
        if (img_divice_width == 480.0) {
            frame1_dimen_list.add(new DimensionsModel(273, 260, 183, 168));             // micromax
        } else if (img_divice_width == 720.0) {
            frame1_dimen_list.add(new DimensionsModel(400, 400, 280, 250));             // mi chotu , lava   --   xxhdpi
        } else if (img_divice_width == 800.0) {
            frame1_dimen_list.add(new DimensionsModel(450, 450, 310, 280));            //  samsung tab   --  large hdpi
        } else if (img_divice_width == 1200.0) {
            frame1_dimen_list.add(new DimensionsModel(650, 650, 470, 420));            // nexus pad   ===  600dpi
        } else if (img_divice_width == 1536.0) {
            frame1_dimen_list.add(new DimensionsModel(810, 820, 615, 545));            //  mi pad     ---  700dpi
        } else {
            frame1_dimen_list.add(new DimensionsModel(590, 590, 430, 380));            // mi note 3   --   xxxhdpi
        }

    }

    private void init_frame2_dimensions() {
        if (img_divice_width == 480.0) {
            frame2_dimen_list.add(new DimensionsModel(130, 150, 50, 315));
        } else if (img_divice_width == 720.0) {
            frame2_dimen_list.add(new DimensionsModel(200, 225, 75, 475));
        } else if (img_divice_width == 800.0) {
            frame2_dimen_list.add(new DimensionsModel(220, 255, 80, 520));
        } else if (img_divice_width == 1200.0) {
            frame2_dimen_list.add(new DimensionsModel(325, 375, 120, 790));
        } else if (img_divice_width == 1536.0) {
            frame2_dimen_list.add(new DimensionsModel(420, 460, 145, 1015));
        }
        frame2_dimen_list.add(new DimensionsModel(310, 330, 95, 705));

    }

    // x=left margin  && y == top margin
    private void init_frame3_dimensions() {
        if (img_divice_width == 480.0) {
            frame3_dimen_list.add(new DimensionsModel(335, 292, 122, 70));
        } else if (img_divice_width == 720.0) {
            frame3_dimen_list.add(new DimensionsModel(500, 445, 200, 100));
        } else if (img_divice_width == 800.0) {
            frame3_dimen_list.add(new DimensionsModel(550, 500, 215, 110));
        } else if (img_divice_width == 1200.0) {
            frame3_dimen_list.add(new DimensionsModel(860, 760, 300, 145));
        } else if (img_divice_width == 1536.0) {
            frame3_dimen_list.add(new DimensionsModel(1100, 950, 390, 210));
        }
        frame3_dimen_list.add(new DimensionsModel(775, 690, 270, 130));//1

    }

    private void init_frame4_dimensions() {
        if (img_divice_width == 480.0) {
            frame4_dimen_list.add(new DimensionsModel(170, 320, 40, 60));
        } else if (img_divice_width == 720.0) {
            frame4_dimen_list.add(new DimensionsModel(250, 480, 60, 90));
        } else if (img_divice_width == 800.0) {
            frame4_dimen_list.add(new DimensionsModel(282, 530, 68, 100));
        } else if (img_divice_width == 1200.0) {
            frame4_dimen_list.add(new DimensionsModel(420, 780, 107, 155));
        } else if (img_divice_width == 1536.0) {
            frame4_dimen_list.add(new DimensionsModel(550, 1000, 130, 200));
        }
        frame4_dimen_list.add(new DimensionsModel(390, 710, 90, 145));//1

    }

    private void init_frame5_dimensions() {
        if (img_divice_width == 480.0) {
            frame5_dimen_list.add(new DimensionsModel(360, 360, 30, 95));
        } else if (img_divice_width == 720.0) {
            frame5_dimen_list.add(new DimensionsModel(550, 540, 40, 145));
        } else if (img_divice_width == 800.0) {
            frame5_dimen_list.add(new DimensionsModel(605, 595, 50, 160));
        } else if (img_divice_width == 1200.0) {
            frame5_dimen_list.add(new DimensionsModel(905, 890, 75, 240));
        } else if (img_divice_width == 1536.0) {
            frame5_dimen_list.add(new DimensionsModel(1175, 1150, 80, 305));
        }
        frame5_dimen_list.add(new DimensionsModel(825, 822, 63, 210));//1

    }

    private void init_frame6_dimensions() {
        if (img_divice_width == 480.0) {
            frame6_dimen_list.add(new DimensionsModel(225, 240, 105, 225));
        } else if (img_divice_width == 720.0) {
            frame6_dimen_list.add(new DimensionsModel(348, 350, 155, 345));
        } else if (img_divice_width == 800.0) {
            frame6_dimen_list.add(new DimensionsModel(380, 400, 170, 375));
        } else if (img_divice_width == 1200.0) {
            frame6_dimen_list.add(new DimensionsModel(560, 600, 265, 565));
        } else if (img_divice_width == 1536.0) {
            frame6_dimen_list.add(new DimensionsModel(720, 790, 335, 710));
        }
        frame6_dimen_list.add(new DimensionsModel(520, 550, 230, 500));//1

    }

    private void init_frame7_dimensions() {
        if (img_divice_width == 480.0) {
            frame7_dimen_list.add(new DimensionsModel(180, 235, 230, 155));
        } else if (img_divice_width == 720.0) {
            frame7_dimen_list.add(new DimensionsModel(276, 345, 340, 240));
        } else if (img_divice_width == 800.0) {
            frame7_dimen_list.add(new DimensionsModel(307, 385, 380, 267));
        } else if (img_divice_width == 1200.0) {
            frame7_dimen_list.add(new DimensionsModel(463, 565, 572, 403));
        } else if (img_divice_width == 1536.0) {
            frame7_dimen_list.add(new DimensionsModel(580, 725, 735, 520));
        }
        frame7_dimen_list.add(new DimensionsModel(409, 525, 515, 356));//1

    }

    private void init_frame8_dimensions() {
        if (img_divice_width == 480.0) {
            frame8_dimen_list.add(new DimensionsModel(172, 380, 27, 80));
        } else if (img_divice_width == 720.0) {
            frame8_dimen_list.add(new DimensionsModel(260, 570, 40, 115));
        } else if (img_divice_width == 800.0) {
            frame8_dimen_list.add(new DimensionsModel(285, 630, 50, 125));
        } else if (img_divice_width == 1200.0) {
            frame8_dimen_list.add(new DimensionsModel(435, 930, 70, 195));
        } else if (img_divice_width == 1536.0) {
            frame8_dimen_list.add(new DimensionsModel(540, 1250, 100, 220));
        }
        frame8_dimen_list.add(new DimensionsModel(380, 880, 70, 175));//1

    }

    private void init_frame9_dimensions() {
        if (img_divice_width == 480.0) {
            frame9_dimen_list.add(new DimensionsModel(220, 250, 230, 75));
        } else if (img_divice_width == 720.0) {
            frame9_dimen_list.add(new DimensionsModel(340, 385, 343, 120));
        } else if (img_divice_width == 800.0) {
            frame9_dimen_list.add(new DimensionsModel(375, 415, 375, 130));
        } else if (img_divice_width == 1200.0) {
            frame9_dimen_list.add(new DimensionsModel(540, 620, 570, 175));
        } else if (img_divice_width == 1536.0) {
            frame9_dimen_list.add(new DimensionsModel(700, 780, 750, 240));
        }
        frame9_dimen_list.add(new DimensionsModel(550, 580, 485, 160));//1

    }

    private void init_frame10_dimensions() {
        if (img_divice_width == 480.0) {
            frame10_dimen_list.add(new DimensionsModel(180, 250, 245, 210));
        } else if (img_divice_width == 720.0) {
            frame10_dimen_list.add(new DimensionsModel(300, 390, 335, 300));
        } else if (img_divice_width == 800.0) {
            frame10_dimen_list.add(new DimensionsModel(345, 450, 380, 330));
        } else if (img_divice_width == 1200.0) {
            frame10_dimen_list.add(new DimensionsModel(500, 670, 560, 490));
        } else if (img_divice_width == 1536.0) {
            frame10_dimen_list.add(new DimensionsModel(600, 830, 770, 650));
        }
        frame10_dimen_list.add(new DimensionsModel(450, 600, 530, 450));//1

    }

    private void init_frame11_dimensions() {
        if (img_divice_width == 480.0) {
            frame11_dimen_list.add(new DimensionsModel(193, 270, 35, 165));
        } else if (img_divice_width == 720.0) {
            frame11_dimen_list.add(new DimensionsModel(293, 440, 50, 240));
        } else if (img_divice_width == 800.0) {
            frame11_dimen_list.add(new DimensionsModel(325, 487, 55, 270));
        } else if (img_divice_width == 1200.0) {
            frame11_dimen_list.add(new DimensionsModel(495, 660, 85, 425));
        } else if (img_divice_width == 1536.0) {
            frame11_dimen_list.add(new DimensionsModel(613, 900, 112, 520));
        }
        frame11_dimen_list.add(new DimensionsModel(442, 650, 75, 370));//1

    }

    private void init_frame12_dimensions() {
        if (img_divice_width == 480.0) {
            frame12_dimen_list.add(new DimensionsModel(160, 150, 195, 130));
        } else if (img_divice_width == 720.0) {
            frame12_dimen_list.add(new DimensionsModel(245, 250, 280, 190));
        } else if (img_divice_width == 800.0) {
            frame12_dimen_list.add(new DimensionsModel(272, 280, 310, 210));
        } else if (img_divice_width == 1200.0) {
            frame12_dimen_list.add(new DimensionsModel(420, 410, 450, 320));
        } else if (img_divice_width == 1536.0) {
            frame12_dimen_list.add(new DimensionsModel(530, 530, 590, 410));
        }
        frame12_dimen_list.add(new DimensionsModel(390, 380, 400, 290));//1

    }

    private void init_frame13_dimensions() {
        if (img_divice_width == 480.0) {
            frame13_dimen_list.add(new DimensionsModel(210, 220, 80, 120));
        } else if (img_divice_width == 720.0) {
            frame13_dimen_list.add(new DimensionsModel(310, 310, 130, 210));
        } else if (img_divice_width == 800.0) {
            frame13_dimen_list.add(new DimensionsModel(340, 340, 150, 230));
        } else if (img_divice_width == 1200.0) {
            frame13_dimen_list.add(new DimensionsModel(490, 490, 240, 360));
        } else if (img_divice_width == 1536.0) {
            frame13_dimen_list.add(new DimensionsModel(650, 635, 290, 450));
        }
        frame13_dimen_list.add(new DimensionsModel(450, 450, 210, 320));//1

    }

    private void init_frame14_dimensions() {
        if (img_divice_width == 480.0) {
            frame14_dimen_list.add(new DimensionsModel(110, 95, 150, 250));
        } else if (img_divice_width == 720.0) {
            frame14_dimen_list.add(new DimensionsModel(180, 150, 210, 355));
        } else if (img_divice_width == 800.0) {
            frame14_dimen_list.add(new DimensionsModel(210, 195, 230, 370));
        } else if (img_divice_width == 1200.0) {
            frame14_dimen_list.add(new DimensionsModel(265, 245, 365, 600));
        } else if (img_divice_width == 1536.0) {
            frame14_dimen_list.add(new DimensionsModel(385, 300, 425, 780));
        }
        frame14_dimen_list.add(new DimensionsModel(230, 200, 340, 580));//1

    }

    private void init_frame15_dimensions() {
        if (img_divice_width == 480.0) {
            frame15_dimen_list.add(new DimensionsModel(170, 170, 155, 155));
        } else if (img_divice_width == 720.0) {
            frame15_dimen_list.add(new DimensionsModel(250, 250, 240, 240));
        } else if (img_divice_width == 800.0) {
            frame15_dimen_list.add(new DimensionsModel(300, 300, 255, 260));
        } else if (img_divice_width == 1200.0) {
            frame15_dimen_list.add(new DimensionsModel(420, 420, 385, 385));
        } else if (img_divice_width == 1536.0) {
            frame15_dimen_list.add(new DimensionsModel(550, 545, 475, 490));
        }
        frame15_dimen_list.add(new DimensionsModel(370, 370, 350, 360));//1

    }

    private void init_frame16_dimensions() {
        if (img_divice_width == 480.0) {
            frame16_dimen_list.add(new DimensionsModel(200, 350, 110, 70));
        } else if (img_divice_width == 720.0) {
            frame16_dimen_list.add(new DimensionsModel(290, 520, 165, 100));
        } else if (img_divice_width == 800.0) {
            frame16_dimen_list.add(new DimensionsModel(325, 570, 180, 110));
        } else if (img_divice_width == 1200.0) {
            frame16_dimen_list.add(new DimensionsModel(490, 845, 260, 170));
        } else if (img_divice_width == 1536.0) {
            frame16_dimen_list.add(new DimensionsModel(645, 1200, 320, 210));
        }
        frame16_dimen_list.add(new DimensionsModel(430, 770, 250, 150));//1

    }

    private void init_frame17_dimensions() {
        if (img_divice_width == 480.0) {
            frame17_dimen_list.add(new DimensionsModel(250, 310, 160, 20));
        } else if (img_divice_width == 720.0) {
            frame17_dimen_list.add(new DimensionsModel(390, 453, 250, 25));
        } else if (img_divice_width == 800.0) {
            frame17_dimen_list.add(new DimensionsModel(430, 490, 282, 33));
        } else if (img_divice_width == 1200.0) {
            frame17_dimen_list.add(new DimensionsModel(642, 735, 426, 50));
        } else if (img_divice_width == 1536.0) {
            frame17_dimen_list.add(new DimensionsModel(870, 960, 500, 55));
        }
        frame17_dimen_list.add(new DimensionsModel(600, 670, 380, 40));//1

    }

    private void init_frame18_dimensions() {
        if (img_divice_width == 480.0) {
            frame18_dimen_list.add(new DimensionsModel(328, 376, 61, 60));
        } else if (img_divice_width == 720.0) {
            frame18_dimen_list.add(new DimensionsModel(495, 565, 90, 90));
        } else if (img_divice_width == 800.0) {
            frame18_dimen_list.add(new DimensionsModel(549, 631, 100, 97));
        } else if (img_divice_width == 1200.0) {
            frame18_dimen_list.add(new DimensionsModel(820, 936, 153, 150));
        } else if (img_divice_width == 1536.0) {
            frame18_dimen_list.add(new DimensionsModel(1053, 1205, 193, 190));
        }
        frame18_dimen_list.add(new DimensionsModel(740, 845, 135, 135));//1

    }

    private void set_bg_fg_images() {

        System.gc();
        Runtime.getRuntime().gc();
        applicationpreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = applicationpreferences.edit();

        frame_position1 = applicationpreferences.getInt("fram_position", 0);
        frame_posi = applicationpreferences.getInt("fram_posi", 0);

        Log.e("Photo Display", "Share.imgs" + Share.imgs);

        path_bg = applicationpreferences.getString("path_b_g", Share.imgs);
        path_fg = applicationpreferences.getString("path_f_g", Share.imgs);


        if (Share.camera_activity_flag == 1) {

            if (Share.flag == 1) {

                editor.putString("path_b_g", Share.imgs);
                editor.putString("path_f_g", path_fg);
                editor.putInt("fram_posi", frame_position1);
                editor.commit();

                Log.e("Tag", "camrera_frame_posi_1--" + frame_posi);
                Log.e("Tag", "bg_path1" + path_bg);

                if (Share.no_select_image == 1) {

                    StickerViewPip.mStickers.clear();
                    sticker_flag = 0;
                    sticker_view.setVisibility(View.GONE);

                    compress_img_path_drawable = getTempBitmap();

                    Share.image_bg = compress_img_path_drawable;
                } else {
                    compress_img_path_drawable = Share.IMAGE_BITMAP_BACKGROUND;
                    Share.no_select_image = 1;
                }

                Log.e("path_--fg_111  : ", "" + path_fg);
                Log.e("path_--bg_111  : ", "" + path_bg);


                bg_img_path_drawable = Share.IMAGE_BITMAP;
                Log.e("Tag", "bg_img_path_drawable while change bg_fg" + bg_img_path_drawable);

                Share.flag = 0;

            } else if (Share.flag == 2) {


                editor.putString("path_f_g", Share.imgs);

                editor.putString("path_b_g", path_bg);
                editor.putInt("fram_posi", frame_position1);
                editor.commit();
                Log.e("Tag", "camrera_frame_posi_2--" + frame_posi);

                compress_img_path_drawable = Share.IMAGE_BITMAP_BACKGROUND;


                Log.e("Tag", "compress_img_path_drawable while change bg_fg" + compress_img_path_drawable);


                Log.e("path_--fg_222  : ", "" + path_fg);
                Log.e("path_--bg_222  : ", "" + path_bg);


                if (Share.no_select_image == 1) {
                    StickerViewPip.mStickers.clear();
                    sticker_flag = 0;
                    sticker_view.setVisibility(View.GONE);

                    bg_img_path_drawable = getTempBitmap();

                    Share.image_fg = bg_img_path_drawable;
                } else {
                    bg_img_path_drawable = Share.IMAGE_BITMAP;
                    Share.no_select_image = 1;
                }


                Share.flag = 0;

            } else {


                if (Share.image_flag == 1) {
                    path_bg = EditorActivity.attrs.INSTANCE.getCamera_imgs();
                    path_fg = EditorActivity.attrs.INSTANCE.getCamera_imgs();


                    editor.putString("path_b_g", Share.imgs);
                    editor.putString("path_f_g", Share.imgs);


                } else {
                    editor.putString("path_b_g", path_bg);
                    editor.putString("path_f_g", path_fg);
                    Share.image_flag = 2;

                }

                editor.putInt("fram_posi", 0);
                editor.commit();

                Log.e("Tag", "camrera_frame_posi_0--" + frame_posi);

                Log.e("path_--bg_000  : ", "" + path_bg);
                Log.e("path_--fg_000 : ", "" + path_fg);

                Log.e("Tag", "bg_path2" + Share.imgs);
                //compress_img_path = compressImage(Camera_View_Activity.camera_image_path);
                compress_img_path_drawable = getTempBitmap();

                bg_img_path_drawable = compress_img_path_drawable;

                Share.image_bg = compress_img_path_drawable;


                Share.image_fg = bg_img_path_drawable;

            }

            Share.IMAGE_BITMAP_1 = Share.image_fg;
            Share.IMAGE_BITMAP = bg_img_path_drawable;

            Log.e("TAG", "bitmap camera  ,,,," + Share.IMAGE_BITMAP);
            Share.bitmapPhoto = Share.IMAGE_BITMAP_1;
            // gpu_image_filter.setImage(Share.bitmapPhoto);

            Log.e("TAG", "bitmap camera 11 ,,,," + Share.bitmapPhoto);


            Share.camera_flag = 1;

            Log.e("TAG", "camera_flag ,,,," + Share.bitmapPhoto);


        } else {

            Log.e("Tag", "gallery          : gallery ");

            if (Share.flag == 1) {


                Log.e("Tag", "image___back" + Share.imgs);


                editor.putString("path_b_g", Share.imgs);
                editor.putString("path_f_g", path_fg);
                editor.putInt("fram_position", frame_position1);
                editor.commit();


                Log.e("Tag", "gallery_frame_posi_1--" + frame_posi);
                Log.e("Tag", "bg_path1" + path_bg);
                //compress_img_path = compressImage(PhotosActivity.imgs);

                Log.e("tag", "no_select_image a- :" + Share.no_select_image);
                if (Share.no_select_image == 1) {

                    StickerViewPip.mStickers.clear();
                    sticker_flag = 0;

                    sticker_view.setVisibility(View.GONE);

                    Log.e("Tag", "image___back1" + Share.imgs);
                    compress_img_path_drawable = getTempBitmap();

                    Share.image_bg = compress_img_path_drawable;
                    Log.e("tag", "no_select_image aa--   :" + Share.no_select_image);
                } else {
                    Log.e("Tag", "image___back11" + Share.imgs);
                    Log.e("tag", "no_select_image aaa---  :" + Share.no_select_image);
                    compress_img_path_drawable = Share.IMAGE_BITMAP_BACKGROUND;
                    Share.no_select_image = 1;
                }

                System.gc();
                Runtime.getRuntime().gc();


                Log.e("path_--fg_1  : ", "" + path_fg);
                Log.e("path_--bg_1  : ", "" + path_bg);

                bg_img_path_drawable = Share.IMAGE_BITMAP;
                Log.e("Tag", "bg_img_path_drawable while change bg_fg" + bg_img_path_drawable);


                Share.flag = 0;

            } else if (Share.flag == 2) {


                Log.e("Tag", "image___fore" + Share.imgs);

                editor.putString("path_f_g", Share.imgs);

                editor.putString("path_b_g", path_bg);
                editor.putInt("fram_position", frame_position1);
                editor.commit();


                Log.e("Tag", "gallery_frame_posi_2--" + frame_posi);

                compress_img_path_drawable = Share.IMAGE_BITMAP_BACKGROUND;


                Log.e("Tag", "compress_img_path_drawable while change bg_fg" + compress_img_path_drawable);
                Log.e("path_--fg_2  : ", "" + path_fg);
                Log.e("path_--bg_2  : ", "" + path_bg);


                if (Share.no_select_image == 1) {
                    Log.e("Tag", "image___fore1" + Share.imgs);


                    StickerViewPip.mStickers.clear();
                    sticker_view.setVisibility(View.GONE);

                    sticker_flag = 0;

                    bg_img_path_drawable = getTempBitmap();

                    Share.image_fg = bg_img_path_drawable;
                } else {
                    Log.e("Tag", "image___fore11" + Share.imgs);
                    bg_img_path_drawable = Share.IMAGE_BITMAP;
                    Share.no_select_image = 1;
                }


                Share.flag = 0;

            } else {


                Log.e("Tag", "Share.image_flag   ==========  :  ===" + Share.image_flag);

                if (Share.image_flag == 1) {

                    editor.putString("path_b_g", Share.imgs);
                    editor.putString("path_f_g", Share.imgs);

                    editor.putInt("fram_posi", 0);

                    editor.commit();


                    Log.e("Tag", "gallery_frame_posi_0--" + frame_posi);

                    Log.e("path_--bg_  : ", "" + path_bg);
                    Log.e("path_--fg_  : ", "" + path_fg);

                    Log.e("Tag", "bg_path2" + Share.imgs);
                    //compress_img_path = compressImage(PhotosActivity.imgs);
                    compress_img_path_drawable = getTempBitmap();

                    Share.image_bg = compress_img_path_drawable;
                    bg_img_path_drawable = getTempBitmap();

                    Share.image_fg = bg_img_path_drawable;
                } else {

                    compress_img_path_drawable = Share.IMAGE_BITMAP_BACKGROUND;

                    bg_img_path_drawable = Share.IMAGE_BITMAP;

                }


            }

            Share.IMAGE_BITMAP_1 = Share.image_fg;
            Share.IMAGE_BITMAP = bg_img_path_drawable;

            Log.e("TAG", "bitmap  ,,,," + Share.IMAGE_BITMAP);
            Share.bitmapPhoto = Share.IMAGE_BITMAP_1;


            Log.e("TAG", "bitmap 11 ,,,," + Share.bitmapPhoto);


            Share.camera_flag = 0;
        }

    }

    public Bitmap getTempBitmap() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File f = new File(directory, "profile.png");
        try {
            Share.b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Share.b;
    }

    private void setOverlayList_for_frames() {

        System.gc();
        Runtime.getRuntime().gc();
        ll_row_frames.removeAllViews();

        try {

            final String[] imgPath = assetManager.list("frames");
            Log.e("TAG", "imgPath :" + imgPath);


            for (int j = 0; j < imgPath.length; j++) {

                int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);
                int width = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);

                InputStream is = assetManager.open("frames/" + imgPath[j]);

                iv_frame_item = new RoundedImageView(this);

                iv_frame_item.setOval(true);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, width, 1f);
                params.setMargins(15, 0, 0, 0);
                iv_frame_item.setPadding(3, 3, 3, 3);

                iv_frame_item.setLayoutParams(params);
                iv_frame_item.setBackgroundDrawable(getResources().getDrawable(R.drawable.whiteborder));
                iv_frame_item.setScaleType(ImageView.ScaleType.FIT_XY);
                System.gc();

                iv_frame_item.setImageDrawable(Drawable.createFromStream(is, null));

                /* Glide.with(PhotoDisplayPipActivity.this).load(is)
                        .placeholder(R.mipmap.ic_launcher).fitCenter()
                        .into(iv_frame_item);*/

                if (j == clicked) {

                    Log.e("TAG", "clicked position" + j);
                    iv_frame_item.setBackgroundDrawable(getResources().getDrawable(R.drawable.blackcborder));

                }
                // compress_img_path = compressImage(PhotosActivity.imgs);
                // default assign the image for .... suppose select null theme

                final int finalJ = j;

                iv_frame_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.gc();
                        Runtime.getRuntime().gc();

                        Log.e("TAG", "position" + finalJ);

                        Drawable d;
                        Log.e("click", "click" + finalJ);


                        frame_position = finalJ;

                        editor.putInt("fram_position", frame_position);
                        editor.commit();

                        try {

                            imag_display.setZoom(1);
                            imag_display.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imag_display_02.setZoom(1);
                            imag_display_02.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_03.setZoom(1);
                            imag_display_03.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_04.setZoom(1);
                            imag_display_04.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_05.setZoom(1);
                            imag_display_05.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_06.setZoom(1);
                            imag_display_06.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_07.setZoom(1);
                            imag_display_07.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_08.setZoom(1);
                            imag_display_08.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_09.setZoom(1);
                            imag_display_09.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_10.setZoom(1);
                            imag_display_10.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_11.setZoom(1);
                            imag_display_11.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_12.setZoom(1);
                            imag_display_12.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_13.setZoom(1);
                            imag_display_13.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imag_display_14.setZoom(1);
                            imag_display_14.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imag_display_15.setZoom(1);
                            imag_display_15.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imag_display_16.setZoom(1);
                            imag_display_16.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imag_display_17.setZoom(1);
                            imag_display_17.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            imag_display_18.setZoom(1);
                            imag_display_18.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            bg_img_display.setZoom(1);
                            bg_img_display.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            switch (finalJ) {

                                case 0:

                                    init_frame1();

                                    maskableFrameLayout1.setMask(R.mipmap.circle);
                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.VISIBLE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.VISIBLE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);

                                    break;

                                case 1:


                                    init_frame2();
                                    maskableFrameLayout2.setMask(R.mipmap.mask_glass);


                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.VISIBLE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.VISIBLE);

                                    break;
                                case 2:

                                    init_frame3();
                                    maskableFrameLayout3.setMask(R.mipmap.mask_frame_03);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.VISIBLE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.VISIBLE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);

                                    break;

                                case 3:
                                    init_frame4();

                                    maskableFrameLayout4.setMask(R.mipmap.mask_frame_04);

                                    frame_layout4.setVisibility(View.VISIBLE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.VISIBLE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 4:
                                    init_frame5();

                                    maskableFrameLayout5.setMask(R.mipmap.mask_frame_05);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.VISIBLE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.VISIBLE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 5:
                                    init_frame6();

                                    maskableFrameLayout6.setMask(R.mipmap.mask_frame_06);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.VISIBLE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.VISIBLE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 6:
                                    init_frame7();

                                    maskableFrameLayout7.setMask(R.mipmap.mask_frame_07);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.VISIBLE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.VISIBLE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 7:
                                    init_frame8();

                                    maskableFrameLayout8.setMask(R.mipmap.mask_frame_08);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.VISIBLE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.VISIBLE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 8:
                                    init_frame9();

                                    maskableFrameLayout9.setMask(R.mipmap.mask_frame_09);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.VISIBLE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.VISIBLE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 9:
                                    init_frame10();

                                    maskableFrameLayout10.setMask(R.mipmap.mask_frame_10);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.VISIBLE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.VISIBLE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 10:
                                    init_frame11();

                                    maskableFrameLayout11.setMask(R.mipmap.mask_frame_11);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.VISIBLE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.VISIBLE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 11:
                                    init_frame12();

                                    maskableFrameLayout12.setMask(R.mipmap.mask_frame_12);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.VISIBLE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.VISIBLE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 12:
                                    init_frame13();

                                    maskableFrameLayout13.setMask(R.mipmap.mask_frame_13);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.VISIBLE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.VISIBLE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 13:
                                    init_frame14();

                                    maskableFrameLayout14.setMask(R.mipmap.mask_frame_14);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.VISIBLE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.VISIBLE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 14:
                                    init_frame15();

                                    maskableFrameLayout15.setMask(R.mipmap.mask_frame_15);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.VISIBLE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.VISIBLE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 15:
                                    init_frame16();

                                    maskableFrameLayout16.setMask(R.mipmap.mask_frame_16);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.VISIBLE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.VISIBLE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 16:
                                    init_frame17();

                                    maskableFrameLayout17.setMask(R.mipmap.mask_frame_017);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.VISIBLE);
                                    frame_layout18.setVisibility(View.GONE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.VISIBLE);
                                    frame_18_layout.setVisibility(View.GONE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;

                                case 17:
                                    init_frame18();

                                    maskableFrameLayout18.setMask(R.mipmap.mask_frame_18);

                                    frame_layout4.setVisibility(View.GONE);
                                    frame_layout5.setVisibility(View.GONE);
                                    frame_layout6.setVisibility(View.GONE);
                                    frame_layout7.setVisibility(View.GONE);
                                    frame_layout8.setVisibility(View.GONE);
                                    frame_layout9.setVisibility(View.GONE);
                                    frame_layout10.setVisibility(View.GONE);
                                    frame_layout11.setVisibility(View.GONE);
                                    frame_layout12.setVisibility(View.GONE);
                                    frame_layout13.setVisibility(View.GONE);
                                    frame_layout14.setVisibility(View.GONE);
                                    frame_layout15.setVisibility(View.GONE);
                                    frame_layout16.setVisibility(View.GONE);
                                    frame_layout17.setVisibility(View.GONE);
                                    frame_layout18.setVisibility(View.VISIBLE);
                                    frame_layout3.setVisibility(View.GONE);
                                    frame_layout2.setVisibility(View.GONE);
                                    frame_layout1.setVisibility(View.GONE);
                                    //maskableFrameLayout3.setVisibility(View.GONE);
                                    frame_04_layout.setVisibility(View.GONE);
                                    frame_05_layout.setVisibility(View.GONE);
                                    frame_06_layout.setVisibility(View.GONE);
                                    frame_07_layout.setVisibility(View.GONE);
                                    frame_08_layout.setVisibility(View.GONE);
                                    frame_09_layout.setVisibility(View.GONE);
                                    frame_10_layout.setVisibility(View.GONE);
                                    frame_11_layout.setVisibility(View.GONE);
                                    frame_12_layout.setVisibility(View.GONE);
                                    frame_13_layout.setVisibility(View.GONE);
                                    frame_14_layout.setVisibility(View.GONE);
                                    frame_15_layout.setVisibility(View.GONE);
                                    frame_16_layout.setVisibility(View.GONE);
                                    frame_17_layout.setVisibility(View.GONE);
                                    frame_18_layout.setVisibility(View.VISIBLE);
                                    frame_03_layout.setVisibility(View.GONE);
                                    // maskableFrameLayout1.setVisibility(View.VISIBLE);
                                    frame_01_layout.setVisibility(View.GONE);
                                    //maskableFrameLayout2.setVisibility(View.GONE);
                                    frame_02_layout.setVisibility(View.GONE);


                                    break;


                                default:


                                    break;
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        clicked = finalJ;
                        clicked = clicked;
                        SharedPrefs.saveclick(PhotoDisplayPipActivity.this, "clickframe", clicked);
                        setOverlayList_for_frames();
                    }


                    // }

                });
                ll_row_frames.addView(iv_frame_item);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
        Runtime.getRuntime().gc();

    }

    private void setEffectThumbRow_background() {
        try {


            Log.e("TAG", "size of       ----------- View_List_Effects_Background" + Share.View_List_Effects_Background.size());

            if (Share.View_List_Effects_Background.size() == 0) {

                new setEffectRowThumb_background().execute();


            } else {


                ll_row_bg_effect.removeAllViews();
                showBackgroundEffectRow();

                if (Share.View_List_Effects_Background.size() > 0) {


                    for (int i = 0; i < Share.View_List_Effects_Background.size(); i++) {
                        RemoveParent(Share.View_List_Effects_Background.get(i));
                        ll_row_bg_effect.addView(Share.View_List_Effects_Background.get(i));
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemoveParent(View child) {

        LinearLayout ll_test = (LinearLayout) child.getParent();
        if (ll_test != null && ll_test.getChildCount() > 0) {
            ll_test.removeAllViews();
        }
    }

    private void showBackgroundEffectRow() {

        hv_scroll_bg_effect.setVisibility(View.VISIBLE);

        rl_background.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        rl_background.startAnimation(bottomDown);

        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        //ll_menu.startAnimation (bottomDown);
        //ll_menu.setVisibility (View.GONE);

        iv_cancel.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        iv_cancel.startAnimation(bottomDown);


    }

    private class setEffectRowThumb_background extends AsyncTask<Void, Void, Void> {

        ProgressDialog effect_progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            effect_progress = Share.createProgressDialog(PhotoDisplayPipActivity.this);
            effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                for (int i = 0; i < filter_name.length; i++) {
                    effect_row_background(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                System.gc();
                Runtime.getRuntime().gc();
                ll_row_bg_effect.removeAllViews();


                for (int i = 0; i < Share.View_List_Effects_Background.size(); i++) {
                    RemoveParent(Share.View_List_Effects_Background.get(i));
                    ll_row_bg_effect.addView(Share.View_List_Effects_Background.get(i));
                }
                showBackgroundEffectRow();
                effect_progress.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static GPUImageFilter createFilterForType(final Context context, final FilterType type) {
        switch (type) {
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
            case PIXELATION:
                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
            case GAMMA:
                return new GPUImageGammaFilter(2.0f);
            case SEPIA:
                return new GPUImageSepiaFilter();
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SHARPEN:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case EMBOSS:
                return new GPUImageEmbossFilter();
            case SOBEL_EDGE_DETECTION:
                return new GPUImageSobelEdgeDetection();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case FILTER_GROUP:
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case KUWAHARA:
                return new GPUImageKuwaharaFilter();
            case SKETCH:
                return new GPUImageSketchFilter();
            case TOON:
                return new GPUImageToonFilter();
            case HAZE:
                return new GPUImageHazeFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    private void effect_row_background(final int i) {


        final View view1 = getLayoutInflater().inflate(R.layout.row_gpu_effectt, null, false);
        try {


            iv_effect_item1 = view1.findViewById(R.id.effect);
            iv_effect_item1.setScaleType(ImageView.ScaleType.FIT_XY);

            int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);
            int width = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);
            iv_effect_item = view1.findViewById(R.id.effect);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, width, 1f);
            params.setMargins(15, 0, 0, 0);
            iv_effect_item1.setPadding(3, 3, 3, 3);

            iv_effect_item1.setLayoutParams(params);
            iv_no_effect_background.setLayoutParams(params);
            iv_effect_item1.setBackgroundDrawable(getResources().getDrawable(R.drawable.whiteborder1));

            //iv_no_effect_background.setScaleType (ImageView.ScaleType.FIT_XY);

            GPUImage gpuImage = new GPUImage(this);
            gpuImage.setImage(Share.IMAGE_BITMAP_BACKGROUND_1);
            gpuImage.setFilter(createFilterForType(PhotoDisplayPipActivity.this, filters.filters.get(i)));

            //Bitmap filter_bitmap = getResizedBitmap (gpuImage.getBitmapWithFilterApplied (), 50, 50);
            iv_effect_item1.setImageBitmap(gpuImage.getBitmapWithFilterApplied());

//-----------------------------------------------------------------------------------------------------


            if (i == 0) {
                iv_effect_item1.setBackgroundResource(R.drawable.whiteborder1);
                iv_no_effect_background.setBackgroundResource(R.drawable.blackcborder1);
            }

            final int finalI = i;

            iv_effect_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    iv_no_effect_background.setBackgroundResource(R.drawable.whiteborder1);
                    for (int k = 0; k < ll_row_bg_effect.getChildCount(); k++) {
                        ImageView iv = ll_row_bg_effect.getChildAt(k).findViewById(R.id.effect);

                        if (i == k) {
                            iv.setBackgroundResource(R.drawable.blackcborder1);
                        } else {
                            iv.setBackgroundResource(R.drawable.whiteborder1);
                        }
                    }

                    if (Share.bitmapPhoto != null) {

                        position = finalI;
                        gpu_image_filter_background.setFilter(createFilterForType(PhotoDisplayPipActivity.this, filters.filters.get(finalI)));
                        new getEffectedBitmap_background().execute();


                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        Share.View_List_Effects_Background.add(view1);
        System.gc();
        Runtime.getRuntime().gc();
    }

    public class getEffectedBitmap_background extends AsyncTask<Void, Void, Void> {

        ProgressDialog apply_effect_progress1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            apply_effect_progress1 = Share.createProgressDialog(PhotoDisplayPipActivity.this);
            apply_effect_progress1.setCancelable(false);
            /*if (pos == -1) {
                Log.e ("TAG", "POSITIN-----" + pos + "-----------" + position);
                pos = position;
                Log.e ("TAG", "POSITION-----" + pos + "-----------" + position);
                apply_effect_progress1 = Share.createProgressDialog (PhotoDisplayPipActivity.this);
                apply_effect_progress1.setCancelable (false);

            } else {
                if (pos == position) {
                    Log.e ("TAG", "POSITION-1----" + pos + "-----------" + position);
                    Log.e ("TAG", "NO PROCESS");
                } else {
                    Log.e ("TAG", "POSITION-11----" + pos + "-----------" + position);
                    pos = position;
                    Log.e ("TAG", "POSITION-11----" + pos + "-----------" + position);
                    apply_effect_progress1 = Share.createProgressDialog (PhotoDisplayPipActivity.this);
                    apply_effect_progress1.setCancelable (false);
                }
            }*/

        }

        @Override
        protected Void doInBackground(Void... params) {

            System.gc();
            Runtime.getRuntime().gc();
            Log.e("doInBackground", "doInBackground");
            getGPUImage_back_ground();
            return null;
        }

        private void getGPUImage_back_ground() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.gc();
                    Runtime.getRuntime().gc();
                    Log.e("TAG", "bitmap   :  " + Share.IMAGE_BITMAP_BACKGROUND);

                    Share.IMAGE_BITMAP_BACKGROUND = gpu_image_filter_background.getBitmapWithFilterApplied();


                    Log.e("TAG ", "effect on Share.IMAGE_BITMAP_BACKGROUND   ==" + Share.IMAGE_BITMAP_BACKGROUND);
                    bg_img_display.invalidate();
                    bg_img_display.setImageBitmap(null);


                    bg_img_display.setImageBitmap(Share.IMAGE_BITMAP_BACKGROUND);


                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (apply_effect_progress1 != null && apply_effect_progress1.isShowing()) {
                Log.e("progress", "dissmiss");
                apply_effect_progress1.dismiss();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void showForegroundEffectRow() {

        hv_scroll_foreground_effect.setVisibility(View.VISIBLE);

        rl_background.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        rl_background.startAnimation(bottomDown);

        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        // ll_menu.startAnimation (bottomDown);
        // ll_menu.setVisibility (View.GONE);

        iv_cancel.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        iv_cancel.startAnimation(bottomDown);
    }

    private void setEffectThumbRow() {

        try {


            Log.e("TAG", "size of       ----------- View_List_Effects" + Share.View_List_Effects.size());

            if (Share.View_List_Effects.size() == 0) {

                new setEffectRowThumb().execute();


            } else {


                ll_row_foreground_effect.removeAllViews();
                showForegroundEffectRow();

                if (Share.View_List_Effects.size() > 0) {


                    for (int i = 0; i < Share.View_List_Effects.size(); i++) {
                        RemoveParent(Share.View_List_Effects.get(i));
                        ll_row_foreground_effect.addView(Share.View_List_Effects.get(i));
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class setEffectRowThumb extends AsyncTask<Void, Void, Void> {


        ProgressDialog effect_progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            effect_progress = Share.createProgressDialog(PhotoDisplayPipActivity.this);
            effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                for (int i = 0; i < filter_name.length; i++) {
                    effect_row(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                System.gc();
                Runtime.getRuntime().gc();
                ll_row_foreground_effect.removeAllViews();


                for (int i = 0; i < Share.View_List_Effects.size(); i++) {
                    RemoveParent(Share.View_List_Effects.get(i));
                    ll_row_foreground_effect.addView(Share.View_List_Effects.get(i));
                }

                Log.e("TAG", "size of View_List_Effects" + Share.View_List_Effects.size());
                showForegroundEffectRow();
                effect_progress.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void effect_row(final int i) {


        final View view = getLayoutInflater().inflate(R.layout.row_gpu_effectt, null, false);
        try {

            int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);
            int width = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.07);
            iv_effect_item = view.findViewById(R.id.effect);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, width, 1f);
            params.setMargins(15, 0, 0, 0);
            iv_effect_item.setPadding(3, 3, 3, 3);

            iv_effect_item.setLayoutParams(params);
            iv_no_effect_foreground.setLayoutParams(params);


            iv_effect_item.setBackgroundDrawable(getResources().getDrawable(R.drawable.whiteborder1));
            iv_effect_item.setScaleType(ImageView.ScaleType.FIT_XY);
            iv_no_effect_foreground.setScaleType(ImageView.ScaleType.FIT_XY);


            GPUImage gpuImage = new GPUImage(this);
            gpuImage.setImage(Share.IMAGE_BITMAP_1);
            gpuImage.setFilter(createFilterForType(PhotoDisplayPipActivity.this, filters.filters.get(i)));

            Bitmap filter_bitmap = getResizedBitmap(gpuImage.getBitmapWithFilterApplied(), 50, 50);
            iv_effect_item.setImageBitmap(filter_bitmap);

            if (i == 0) {
                iv_effect_item.setBackgroundResource(R.drawable.whiteborder1);
                iv_no_effect_foreground.setBackgroundResource(R.drawable.blackcborder1);
            }

            final int finalI = i;

            iv_effect_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    iv_no_effect_foreground.setBackgroundResource(R.drawable.whiteborder1);
                    for (int k = 0; k < ll_row_foreground_effect.getChildCount(); k++) {
                        ImageView iv = ll_row_foreground_effect.getChildAt(k).findViewById(R.id.effect);

                        if (i == k) {
                            iv.setBackgroundResource(R.drawable.blackcborder1);
                        } else {
                            iv.setBackgroundResource(R.drawable.whiteborder1);
                        }
                    }

                    if (Share.bitmapPhoto != null) {

                        position1 = finalI;
                        gpu_image_filter.setFilter(createFilterForType(PhotoDisplayPipActivity.this, filters.filters.get(finalI)));

                        new getEffectedBitmap().execute();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        Share.View_List_Effects.add(view);
        System.gc();
        Runtime.getRuntime().gc();
    }

    public class getEffectedBitmap extends AsyncTask<Void, Void, Void> {

        ProgressDialog apply_effect_progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pos1 == -1) {
                Log.e("TAG", "POSITIN-----" + pos1 + "-----------" + position1);
                pos1 = position1;
                Log.e("TAG", "POSITION-----" + pos1 + "-----------" + position1);
                apply_effect_progress = Share.createProgressDialog(PhotoDisplayPipActivity.this);
                apply_effect_progress.setCancelable(false);

            } else {
                if (pos1 == position1) {
                    Log.e("TAG", "POSITION-1----" + pos + "-----------" + position);
                    Log.e("TAG", "NO PROCESS");
                } else {
                    Log.e("TAG", "POSITION-11----" + pos1 + "-----------" + position1);
                    pos1 = position1;
                    Log.e("TAG", "POSITION-11----" + pos1 + "-----------" + position1);
                    apply_effect_progress = Share.createProgressDialog(PhotoDisplayPipActivity.this);
                    apply_effect_progress.setCancelable(false);
                }
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            System.gc();
            Runtime.getRuntime().gc();
            Log.e("doInBackground", "doInBackground");
            getGPUImage();
            return null;
        }

        private void getGPUImage() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.gc();
                    Runtime.getRuntime().gc();
                    Log.e("TAG", "bitmap   :  " + Share.IMAGE_BITMAP);

                    Share.IMAGE_BITMAP = gpu_image_filter.getBitmapWithFilterApplied();

                    Log.e("TAG ", "effect on Share.IMAGE_BITMAP   ==" + Share.IMAGE_BITMAP);

                    imag_display.invalidate();
                    imag_display.setImageBitmap(null);
                    imag_display.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_02.invalidate();
                    imag_display_02.setImageBitmap(null);
                    imag_display_02.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_03.invalidate();
                    imag_display_03.setImageBitmap(null);
                    imag_display_03.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_04.invalidate();
                    imag_display_04.setImageBitmap(null);
                    imag_display_04.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_05.invalidate();
                    imag_display_05.setImageBitmap(null);
                    imag_display_05.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_06.invalidate();
                    imag_display_06.setImageBitmap(null);
                    imag_display_06.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_07.invalidate();
                    imag_display_07.setImageBitmap(null);
                    imag_display_07.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_08.invalidate();
                    imag_display_08.setImageBitmap(null);
                    imag_display_08.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_09.invalidate();
                    imag_display_09.setImageBitmap(null);
                    imag_display_09.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_10.invalidate();
                    imag_display_10.setImageBitmap(null);
                    imag_display_10.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_11.invalidate();
                    imag_display_11.setImageBitmap(null);
                    imag_display_11.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_12.invalidate();
                    imag_display_12.setImageBitmap(null);
                    imag_display_12.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_13.invalidate();
                    imag_display_13.setImageBitmap(null);
                    imag_display_13.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_14.invalidate();
                    imag_display_14.setImageBitmap(null);
                    imag_display_14.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_15.invalidate();
                    imag_display_15.setImageBitmap(null);
                    imag_display_15.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_16.invalidate();
                    imag_display_16.setImageBitmap(null);
                    imag_display_16.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_17.invalidate();
                    imag_display_17.setImageBitmap(null);
                    imag_display_17.setImageBitmap(Share.IMAGE_BITMAP);

                    imag_display_18.invalidate();
                    imag_display_18.setImageBitmap(null);
                    imag_display_18.setImageBitmap(Share.IMAGE_BITMAP);

                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (apply_effect_progress != null && apply_effect_progress.isShowing()) {
                Log.e("progress", "dissmiss");
                apply_effect_progress.dismiss();
            }
        }
    }

    public void OptionDialog() {


        View view = getLayoutInflater().inflate(R.layout.option_dialog, null);

        Button change_bg, change_fg, cancel_option;
        change_bg = view.findViewById(R.id.change_bg);
        change_fg = view.findViewById(R.id.change_fg);
        cancel_option = view.findViewById(R.id.cancel_option);

        final Dialog mBottomSheetDialog = new Dialog(PhotoDisplayPipActivity.this, R.style.DialogAnimation);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        change_bg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.gc();
                Runtime.getRuntime().gc();
                Share.Flag_First = false;
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), FaceActivitypip.class);

                startActivity(intent);
                Share.flag = 1;


                editor.putInt("fram_posi", frame_position1);
                editor.commit();


                Share.effect_flag_bg = 1;
                Log.e("frame_position   =", "====" + frame_posi);
                //bg_img_display.setImageBitmap(blurred);
                bg_img_display.setImageDrawable(new BitmapDrawable(getResources(), blurred));
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);

                mBottomSheetDialog.dismiss();

            }
        });
        change_fg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                System.gc();
                Runtime.getRuntime().gc();
                Share.Flag_First = false;
                // TODO Auto-generated method stub
                Intent intent2 = new Intent(getApplicationContext(), FaceActivitypip.class);

                startActivity(intent2);
                Share.flag = 2;

                editor.putInt("fram_posi", frame_position1);
                editor.commit();

                Share.effect_flag_fg = 1;

                Log.e("frame_position   =", "====" + frame_posi);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);


                mBottomSheetDialog.dismiss();
            }
        });
        cancel_option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Share.flag = 0;

                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);

                mBottomSheetDialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.pip:

                pip.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), android.graphics.PorterDuff.Mode.SRC_IN);

                backgound_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite2));
                forground_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite));
                //backgound_image.setColorFilter (ContextCompat.getColor (getApplicationContext (), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                // forground_image.setColorFilter (ContextCompat.getColor (getApplicationContext (), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                options.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);


                System.gc();
                Runtime.getRuntime().gc();

                setOverlayList_for_frames();

                hv_scroll_frames.setVisibility(View.VISIBLE);
                hv_scroll_sticker.setVisibility(View.GONE);
                hv_scroll_foreground_effect.setVisibility(View.GONE);
                hv_scroll_bg_effect.setVisibility(View.GONE);

                ll_row_frames.setVisibility(View.VISIBLE);

                rl_background.setVisibility(View.VISIBLE);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                rl_background.startAnimation(bottomDown);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                // ll_menu.startAnimation (bottomDown);
                // ll_menu.setVisibility (View.GONE);
                iv_cancel.setVisibility(View.VISIBLE);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                iv_cancel.startAnimation(bottomDown);
                break;


            case R.id.background:

                pip.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                backgound_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite2_gol));
                forground_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite));

                // backgound_image.setColorFilter (ContextCompat.getColor (getApplicationContext (), R.color.yellowcolor), android.graphics.PorterDuff.Mode.SRC_IN);
                // forground_image.setColorFilter (ContextCompat.getColor (getApplicationContext (), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                options.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                System.gc();
                Runtime.getRuntime().gc();

                Share.image_flag = 2;

                hv_scroll_bg_effect.setVisibility(View.GONE);
                hv_scroll_sticker.setVisibility(View.GONE);
                hv_scroll_frames.setVisibility(View.GONE);
                hv_scroll_foreground_effect.setVisibility(View.GONE);


                setEffectThumbRow_background();
                break;

            case R.id.foreground:

                pip.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                backgound_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite2));
                forground_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite_gol));

                sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                options.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                System.gc();
                Runtime.getRuntime().gc();

                Share.image_flag = 2;
                hv_scroll_foreground_effect.setVisibility(View.GONE);
                hv_scroll_sticker.setVisibility(View.GONE);
                hv_scroll_frames.setVisibility(View.GONE);
                hv_scroll_bg_effect.setVisibility(View.GONE);

                setEffectThumbRow();
                break;

            case R.id.sticker:

                pip.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                backgound_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite2));
                forground_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite));
                sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), android.graphics.PorterDuff.Mode.SRC_IN);
                options.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                System.gc();
                Runtime.getRuntime().gc();
                setStickerlist();
                hv_scroll_sticker.setVisibility(View.VISIBLE);
                hv_scroll_foreground_effect.setVisibility(View.GONE);
                hv_scroll_frames.setVisibility(View.GONE);
                // option_layout.setVisibility(View.GONE);
                hv_scroll_bg_effect.setVisibility(View.GONE);

                ll_row_sticker.setVisibility(View.VISIBLE);

                rl_background.setVisibility(View.VISIBLE);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                rl_background.startAnimation(bottomDown);

                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                // ll_menu.startAnimation (bottomDown);
                // ll_menu.setVisibility (View.GONE);

                iv_cancel.setVisibility(View.VISIBLE);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                iv_cancel.startAnimation(bottomDown);
                break;

            case R.id.option:

                pip.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);

                backgound_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite2));
                forground_image.setImageDrawable(getResources().getDrawable(R.mipmap.opposite));
                sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), android.graphics.PorterDuff.Mode.SRC_IN);
                options.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), android.graphics.PorterDuff.Mode.SRC_IN);


                System.gc();
                Runtime.getRuntime().gc();


                OptionDialog();

                break;

            case R.id.iv_cancel:

                hv_scroll_frames.setVisibility(View.GONE);
                hv_scroll_bg_effect.setVisibility(View.GONE);
                hv_scroll_foreground_effect.setVisibility(View.GONE);
                hv_scroll_sticker.setVisibility(View.GONE);

                System.gc();
                Runtime.getRuntime().gc();
                // ll_menu.setVisibility (View.VISIBLE);
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                //ll_menu.startAnimation (bottomDown);

                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                rl_background.startAnimation(bottomDown);
                //rl_background.setVisibility (View.GONE);

                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                iv_cancel.startAnimation(bottomDown);
                iv_cancel.setVisibility(View.INVISIBLE);

                hv_scroll_bg_effect.postDelayed(new Runnable() {
                    public void run() {
                        hv_scroll_bg_effect.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 300);
                hv_scroll_foreground_effect.postDelayed(new Runnable() {
                    public void run() {
                        hv_scroll_foreground_effect.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 300);
                hv_scroll_frames.postDelayed(new Runnable() {
                    public void run() {
                        hv_scroll_frames.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 300);
                hv_scroll_sticker.postDelayed(new Runnable() {
                    public void run() {
                        hv_scroll_sticker.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 300);
                break;

            case R.id.iv_back:


                System.gc();

                Runtime.getRuntime().gc();

                final Dialog dialog = new Dialog(PhotoDisplayPipActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.new_exit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(DisplayMetricsHandler.getScreenWidth() - 50, Toolbar.LayoutParams.WRAP_CONTENT);


                LinearLayout tv_yes = dialog.findViewById(R.id.btn_yes);
                LinearLayout tv_no = dialog.findViewById(R.id.btn_no);

                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Share.selectedFragment = null;


                        System.gc();

                        Runtime.getRuntime().gc();

                        clicked = 0;
                        //Share.no_select_image  = 1;

                        editor.putString("path_b_g", "");
                        editor.putString("path_f_g", "");
                        editor.putInt("fram_position", 0);
                        editor.putInt("fram_posi", 0);
                        editor.commit();


                        if (Share.camera_flag == 1) {
                            Share.image_flag = 1;
                            Share.Flag_First = true;
                            // Share.camera_view_activity.finish();
                            Share.camera_activity_flag = 0;
                            Share.camera_flag = 0;
                            Share.IMAGE_BITMAP = null;
                            Share.IMAGE_BITMAP_BACKGROUND = null;
                            Share.IMAGE_BITMAP_BACKGROUND_1 = null;
                            Share.IMAGE_BITMAP_1 = null;
                            Share.image_bg = null;
                            Share.image_fg = null;
                            finish();
                        } else {
                            Share.image_flag = 1;
                            Share.camera_activity_flag = 0;
                            Log.e("TAG", "no_select_image------------ : " + Share.no_select_image);
                            Share.no_select_image = 1;
                            Share.IMAGE_BITMAP = null;
                            Share.IMAGE_BITMAP_BACKGROUND = null;
                            Share.IMAGE_BITMAP_BACKGROUND_1 = null;
                            Share.IMAGE_BITMAP_1 = null;
                            Log.e("tag", "Share.IMAGE_BITMAP_BACKGROUND  = " + Share.IMAGE_BITMAP_BACKGROUND + " Share.IMAGE_BITMAP =  " + Share.IMAGE_BITMAP);
                            //  Share.photos_activity.finish();
                            if (Share.Activity_Gallery_View != null) {
                                Share.Activity_Gallery_View.finish();
                            }
                            Share.View_List_Effects.clear();
                            finish();

                            Share.Flag_First = true;
                            Share.View_List_Effects_Background.clear();

                            Share.image_bg = null;
                            Share.image_fg = null;
                        }


                        Share.View_List_Effects_Background.clear();
                        Share.View_List_Effects.clear();
                        Log.e("path_--fg____  : ", "" + path_fg);
                        Log.e("path_--bg____  : ", "" + path_bg);

                        finish();
                        dialog.dismiss();

                    }
                });
                if (dialog.isShowing()) {
                    dialog.cancel();
                } else {
                    dialog.show();
                }

                System.gc();

                Runtime.getRuntime().gc();

                break;


            case R.id.iv_done:

                System.gc();
                Runtime.getRuntime().gc();

                if (rl_background.getChildCount() > 0) {

                    System.gc();
                    Runtime.getRuntime().gc();
                    saveImage();
                    System.gc();

                    hv_scroll_frames.setVisibility(View.GONE);
                    hv_scroll_bg_effect.setVisibility(View.GONE);
                    hv_scroll_foreground_effect.setVisibility(View.GONE);
                    hv_scroll_sticker.setVisibility(View.GONE);

                    ll_menu.setVisibility(View.VISIBLE);
                    //rl_background.setVisibility (View.GONE);
                    iv_cancel.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(PhotoDisplayPipActivity.this, "Blank image not save", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.iv_no_effect_background:


                System.gc();
                Runtime.getRuntime().gc();
                Share.IMAGE_BITMAP_BACKGROUND = Share.bitmapPhoto_background;

                Log.e("TAG", "bitmap background  ========,,,," + Share.IMAGE_BITMAP_BACKGROUND);

                bg_img_display.setImageBitmap(Share.IMAGE_BITMAP_BACKGROUND);
                updateRowFrame_background();

                //ChangeColorEffectBlend();
                break;

            case R.id.iv_no_effect_foreground:


                System.gc();
                Runtime.getRuntime().gc();

                Share.IMAGE_BITMAP = Share.bitmapPhoto;

                Log.e("TAG", "bitmap foreground  ========,,,," + Share.IMAGE_BITMAP);

                imag_display.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_02.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_03.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_04.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_05.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_06.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_07.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_08.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_09.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_10.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_11.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_12.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_13.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_14.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_15.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_16.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_17.setImageBitmap(Share.IMAGE_BITMAP);
                imag_display_18.setImageBitmap(Share.IMAGE_BITMAP);

                updateRowFrame_foreground();

                break;


        }

    }

    private void saveImage() {

        sticker_view.setControlItemsHidden();
        l1.setDrawingCacheEnabled(true);
        Share.SAVED_IMAGE = Bitmap.createBitmap(l1.getDrawingCache());
        l1.setDrawingCacheEnabled(false);

        save_task = new saveImage().execute();

    }

    public class saveImage extends AsyncTask<Void, Void, Void> {

        Bitmap finalBmp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            saveDialog = Share.showProgress(activity, "Saving...");
            saveDialog.show();
            finalBmp = Share.SAVED_IMAGE;

        }

        @Override
        protected Void doInBackground(Void... params) {

            File path = new File(Share.IMAGE_PATH);
            if (!path.exists())
                path.mkdirs();


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            try {
                if (finalBmp != null) {

                    File imageFile = new File(path, timeStamp + ".png");
                    Log.e("TAG", "imageFile=>" + imageFile);

                    if (!imageFile.exists())
                        imageFile.createNewFile();
                    FileOutputStream fos = null;

                    try {

                        fos = new FileOutputStream(imageFile);
                        finalBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();

                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                            }

                            @Override
                            public void onScanCompleted(String path, final Uri uri) {
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null)
                                fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e("TAG", "Not Saved Image------------------------------------------------------->");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (saveDialog.isShowing()) {

                saveDialog.dismiss();

                Toast.makeText(activity, "Image save sucessfully", Toast.LENGTH_LONG).show();

                nextActivity();

            }
        }
    }

    private void nextActivity() {

        save_task = null;
        Intent intent = new Intent(activity, FullScreenImageActivity.class);
        Share.Fragment = "MyPhotosFragment";
        intent.putExtra("avairy", "");
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.e("TAG", "onRequestPermissionsResult: deny");

            } else {
                Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for Storage")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }

        } else {

            saveImage();

        }

    }

    private void updateRowFrame_background() {
        iv_no_effect_background.setBackgroundResource(R.drawable.blackcborder1);
        for (int k = 0; k < ll_row_bg_effect.getChildCount(); k++) {
            ImageView iv = ll_row_bg_effect.getChildAt(k).findViewById(R.id.effect);
            iv.setBackgroundResource(R.drawable.whiteborder1);
        }

    }

    private void setStickerlist() {

        System.gc();
        Runtime.getRuntime().gc();
        ll_row_sticker.removeAllViews();

        try {

            final String[] imgPath = assetManager.list("stickers");
            sortArray(imgPath, "sticker_");


            for (int j = 0; j < imgPath.length; j++) {
                int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.06);
                int width = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.06);
                InputStream is = assetManager.open("stickers/" + imgPath[j]);

                iv_sticker_item = new ImageView(this);
                iv_sticker_item.setScaleType(ImageView.ScaleType.FIT_XY);

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap filter_bitmap_sticker = getResizedBitmap(bitmap, 50, 50);

                iv_sticker_item.setImageBitmap(filter_bitmap_sticker);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, width, 1f);
                params.setMargins(10, 0, 0, 0);
                iv_sticker_item.setLayoutParams(params);

                final int finalJ = j;
                iv_sticker_item.setOnClickListener(this);

                iv_sticker_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sticker_view.setVisibility(View.VISIBLE);
                        try {
                            InputStream inputstream = PhotoDisplayPipActivity.this.getAssets().open("stickers/" + imgPath[finalJ]);
                            if (inputstream != null) {

                                sticker_flag = sticker_flag + 1;
                                Log.e("sticker_flag", "sticker_flag    :  " + sticker_flag);
                                Log.e("sticker_added", "sticker_added");
                                DrawableStickerPip sticker = new DrawableStickerPip(FONT_STICKER_DRAWABLE);

                                Bitmap bitmap = BitmapFactory.decodeStream(inputstream);                       //  1st create inputstream to bitmap


                                Drawable d = new BitmapDrawable(getResources(), bitmap);                        // then  convert bitmap to drawable
                                sticker_view.addSticker(new DrawableStickerPip(d));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                ll_row_sticker.addView(iv_sticker_item);

                // Log.e("TAG","size of ll_row_sticker  == "+ ll_row_sticker.len)

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();
        Runtime.getRuntime().gc();
    }

    private void sortArray(String[] arrayList, final String frontStr) {

        Arrays.sort(arrayList, new Comparator<String>() {
            @Override
            public int compare(String entry1, String entry2) {
                Integer file1 = Integer.parseInt((entry1.split(frontStr)[1]).split("\\.")[0]);
                Integer file2 = Integer.parseInt((entry2.split(frontStr)[1]).split("\\.")[0]);
                return file1.compareTo(file2);
            }
        });
    }

    public void updateRowFrame_foreground() {

        iv_no_effect_foreground.setBackgroundResource(R.drawable.blackcborder);
        for (int k = 0; k < ll_row_foreground_effect.getChildCount(); k++) {
            ImageView iv = ll_row_foreground_effect.getChildAt(k).findViewById(R.id.effect);
            iv.setBackgroundResource(R.drawable.whiteborder);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Share.RestartApp(this)) {


            System.gc();

            Runtime.getRuntime().gc();


            isInForeGround = true;

            Log.e("TAG", "Share.effect_flag_fg" + Share.effect_flag_fg);

            if (Share.effect_flag_fg == 1) {
                //add();
                sticker_flag = 0;
                gpu_image_filter = new GPUImage(PhotoDisplayPipActivity.this);
                Share.b = null;
                Share.View_List_Effects.clear();
                Log.e("TAG", "onresume   :  foreground ");


                Share.effect_flag_fg = 0;

                set_bg_fg_images();
                setimages();
            }

            Log.e("TAG", "Share.effect_flag_bg" + Share.effect_flag_bg);
            if (Share.effect_flag_bg == 1) {
                //StickerView.mStickers.clear();
                //add();
                sticker_flag = 0;
                gpu_image_filter_background = new GPUImage(PhotoDisplayPipActivity.this);
                //Share.IMAGE_BITMAP_BACKGROUND = null;
                Share.b = null;
                Share.View_List_Effects_Background.clear();
                Log.e("TAG", "onresume   :  background ");

                Share.effect_flag_bg = 0;

                set_bg_fg_images();
                setimages();
            }


            if (Share.resume_flag == 1) {
                Log.e("TAG ", "on resume    : " + "from on puse");

                bg_img_display.invalidate();
                bg_img_display.setImageBitmap(null);
                bg_img_display.setImageBitmap(Share.IMAGE_BITMAP_BACKGROUND);


                imag_display.invalidate();
                imag_display.setImageBitmap(null);
                imag_display.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_02.invalidate();
                imag_display_02.setImageBitmap(null);
                imag_display_02.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_03.invalidate();
                imag_display_03.setImageBitmap(null);
                imag_display_03.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_04.invalidate();
                imag_display_04.setImageBitmap(null);
                imag_display_04.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_05.invalidate();
                imag_display_05.setImageBitmap(null);
                imag_display_05.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_06.invalidate();
                imag_display_06.setImageBitmap(null);
                imag_display_06.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_07.invalidate();
                imag_display_07.setImageBitmap(null);
                imag_display_07.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_08.invalidate();
                imag_display_08.setImageBitmap(null);
                imag_display_08.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_09.invalidate();
                imag_display_09.setImageBitmap(null);
                imag_display_09.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_10.invalidate();
                imag_display_10.setImageBitmap(null);
                imag_display_10.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_11.invalidate();
                imag_display_11.setImageBitmap(null);
                imag_display_11.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_12.invalidate();
                imag_display_12.setImageBitmap(null);
                imag_display_12.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_13.invalidate();
                imag_display_13.setImageBitmap(null);
                imag_display_13.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_14.invalidate();
                imag_display_14.setImageBitmap(null);
                imag_display_14.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_15.invalidate();
                imag_display_15.setImageBitmap(null);
                imag_display_15.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_16.invalidate();
                imag_display_16.setImageBitmap(null);
                imag_display_16.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_17.invalidate();
                imag_display_17.setImageBitmap(null);
                imag_display_17.setImageBitmap(Share.IMAGE_BITMAP);

                imag_display_18.invalidate();
                imag_display_18.setImageBitmap(null);
                imag_display_18.setImageBitmap(Share.IMAGE_BITMAP);


                Share.resume_flag = 0;
            }

            Log.e("onresume", "onresume");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Share.RestartApp(this)) {

            if (compress_img_path_drawable != null && compress_img_path_drawable.isRecycled()) {
                compress_img_path_drawable.isRecycled();
                compress_img_path_drawable = null;
                System.gc();
                Runtime.getRuntime().gc();
            }
            if (bg_img_path_drawable != null && bg_img_path_drawable.isRecycled()) {
                bg_img_path_drawable.isRecycled();
                bg_img_path_drawable = null;
                System.gc();
                Runtime.getRuntime().gc();
            }


            Log.e("TAG", "ondestroy");
            Share.camera_activity_flag = 0;

            System.gc();
            Runtime.getRuntime().gc();


            Share.last_posi = 0;
            frame_position1 = 0;

            editor.putString("path_b_g", "");
            editor.putString("path_f_g", "");
            editor.putInt("fram_position", 0);


            //editor.putInt("fram_position", frame_position1);
            editor.putInt("fram_posi", 0);
            editor.commit();

            Log.e("TAG", "fram_position:" + frame_position1);

        }

    }

    @Override
    public void onBackPressed() {

        System.gc();

        Runtime.getRuntime().gc();

        final Dialog dialog = new Dialog(PhotoDisplayPipActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(DisplayMetricsHandler.getScreenWidth() - 50, Toolbar.LayoutParams.WRAP_CONTENT);


        LinearLayout tv_yes = dialog.findViewById(R.id.btn_yes);
        LinearLayout tv_no = dialog.findViewById(R.id.btn_no);

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitActivity();
                dialog.dismiss();

            }
        });
        if (dialog.isShowing()) {
            dialog.cancel();
        } else {
            dialog.show();
        }

        //
        System.gc();
        Runtime.getRuntime().gc();

    }

    private void exitActivity() {

        Share.selectedFragment = null;
        System.gc();
        Runtime.getRuntime().gc();

        clicked = 0;
        //Share.no_select_image  = 1;
        editor.putString("path_b_g", "");
        editor.putString("path_f_g", "");
        editor.putInt("fram_position", 0);
        editor.putInt("fram_posi", 0);
        editor.commit();
        StickerViewPip.mStickers.clear();
        sticker_flag = 0;

        /* frame_position1=0;*/

        if (Share.camera_flag == 1) {

            Share.image_flag = 1;
            Share.Flag_First = true;
            Share.camera_activity_flag = 0;
            Share.camera_flag = 0;
            Share.IMAGE_BITMAP = null;
            Share.IMAGE_BITMAP_BACKGROUND = null;
            Share.IMAGE_BITMAP_BACKGROUND_1 = null;
            Share.IMAGE_BITMAP_1 = null;
            Share.image_bg = null;
            Share.image_fg = null;
            finish();
        } else {
            Share.image_flag = 1;
            Share.camera_activity_flag = 0;
            Log.e("TAG", "no_select_image------------ : " + Share.no_select_image);
            Share.no_select_image = 1;
            Share.IMAGE_BITMAP = null;
            Share.IMAGE_BITMAP_BACKGROUND = null;
            Share.IMAGE_BITMAP_BACKGROUND_1 = null;
            Share.IMAGE_BITMAP_1 = null;
            Log.e("tag", "Share.IMAGE_BITMAP_BACKGROUND  = " + Share.IMAGE_BITMAP_BACKGROUND + " Share.IMAGE_BITMAP =  " + Share.IMAGE_BITMAP);
//            Share.photos_activity.finish();

            if (Share.Activity_Gallery_View != null) {
                Share.Activity_Gallery_View.finish();
            }

            Share.View_List_Effects.clear();
            finish();

            Share.Flag_First = true;
            Share.View_List_Effects_Background.clear();

            Share.image_bg = null;
            Share.image_fg = null;
        }


        Share.View_List_Effects_Background.clear();
        Share.View_List_Effects.clear();
        Log.e("path_--fg____  : ", "" + path_fg);
        Log.e("path_--bg____  : ", "" + path_bg);

        finish();

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "onstop");


    }


}
