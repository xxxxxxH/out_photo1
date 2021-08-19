package net.basicmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.adapter.StickerAdapter;
import net.entity.StickerModel;
import net.utils.DisplayMetricsHandler;
import net.utils.Share;
import net.utils.Util;
import net.widget.CustomImageView;
import net.widget.DrawableSticker;
import net.widget.StickerView;

import java.io.File;
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


public class BokehEffectActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout seeklayout;
    private static final String TAG = BokehEffectActivity.class.getSimpleName();
    boolean isInForeGround;
    private final int STORAGE_PERMISSION_CODE = 20;
    private final List<String> listPermissionsNeeded = new ArrayList<>();

    private LayoutInflater inflater;

    //Image Filters - GPU Image
    private final String[] filter_name = {"CONTRAST", "INVERT", "PIXELATION", "HUE",
            "GAMMA", "SEPIA", "GRAYSCALE", "SHARPEN", "EMBOSS",
            "SOBEL_EDGE_DETECTION", "POSTERIZE", "FILTER_GROUP", "SATURATION", "VIGNETTE",
            "KUWAHARA", "SKETCH", "TOON", "HAZE", "LEVELS_FILTER_MIN"};
    GPUImage gpu_image_filter;
    private FilterList filters;
    ImageView iv_effect_item;

    public static StickerView stickerView;
    private final ArrayList<StickerModel> list = new ArrayList<>();
    public static List<DrawableSticker> drawables_sticker = new ArrayList<>();

    private final String image_name = "";
    private AssetManager assetManager;
    ViewTreeObserver vto;
    int imgFinalHeight = 0, imgFinalWidth = 0;
    int maxOpacity = 70;

    //Top Layout
    ImageView iv_save, iv_my_albums;

    //Main Layout
    private RelativeLayout rl_main, rl_opacity;
    LinearLayout ll_cancel;
    ImageView img_main, img_rotated;
    CustomImageView img_effect;

    private SeekBar sb_opacity;
    private TextView tv_opacity;

    AsyncTask save_task;

    //Bottom Layout
    private Animation bottomUp, bottomDown;
    LinearLayout ll_menu;
    private ImageView iv_color_effect, iv_effect, iv_text, iv_vintage, iv_sticker;
    RelativeLayout rl_background;
    private HorizontalScrollView hv_scroll_color_effect, hv_scroll_effect;
    private LinearLayout ll_row_color_effect, ll_row_effect, ll_row_sticker, ll_row_vintage;
    public static Bitmap bmEffected_img = null;
    RecyclerView rv_sticker;
    ImageView iv_no_effect;
    ImageView iv_no_color_effect, iv_no_vintage;
    RelativeLayout rv_main;

    TextView txtbokeh, txteffect, txttext, txtsticker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bokeh_effect);
        Share.screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        Share.screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        initView();
        Log.e("TAG", "Share.fromCamera--->: " + Share.fromCamera);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isInForeGround = true;

        if (Share.FONT_FLAG) {

            Share.FONT_FLAG = false;

            stickerView = findViewById(R.id.stickerView);

            Log.e("TAG", "onResume: Bokeh Effect ");

            DrawableSticker drawableSticker = Share.TEXT_DRAWABLE;
            drawableSticker.setTag("text");
            stickerView.addSticker(drawableSticker);

            if (drawables_sticker == null)
                drawables_sticker = new ArrayList<>();

            drawables_sticker.add(drawableSticker);
            Share.isStickerAvail = true;
            Share.isStickerTouch = true;
            stickerView.setLocked(false);
        }
    }

    private void initView() {

        txtbokeh = findViewById(R.id.txtbokeh);
        txteffect = findViewById(R.id.txteffect);
        txttext = findViewById(R.id.txttext);
        txtsticker = findViewById(R.id.txtsticker);
        seeklayout = findViewById(R.id.seeklayout);

        Log.e("Call", "initView");

        assetManager = getAssets();
        iv_no_effect = findViewById(R.id.iv_no_effect);
        gpu_image_filter = new GPUImage(BokehEffectActivity.this);
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


        rv_main = findViewById(R.id.rv_main);
        iv_save = findViewById(R.id.iv_save);
        iv_my_albums = findViewById(R.id.iv_my_albums);

        rl_main = findViewById(R.id.rl_main);
        img_main = findViewById(R.id.img_main);
        img_effect = findViewById(R.id.img_effect);
        img_rotated = findViewById(R.id.img_rotated);
        ll_cancel = findViewById(R.id.ll_cancel);
        rl_opacity = findViewById(R.id.rl_opacity);

        ll_menu = findViewById(R.id.ll_menu);
        iv_color_effect = findViewById(R.id.iv_color_effect);
        iv_effect = findViewById(R.id.iv_effect);
        iv_text = findViewById(R.id.iv_text);

        iv_sticker = findViewById(R.id.iv_sticker);

        rl_background = findViewById(R.id.rl_background);
        hv_scroll_color_effect = findViewById(R.id.hv_scroll_color_effect);
        hv_scroll_effect = findViewById(R.id.hv_scroll_effect);
        ll_row_color_effect = findViewById(R.id.ll_row_color_effect);
        ll_row_effect = findViewById(R.id.ll_row_effect);
        ll_row_sticker = findViewById(R.id.ll_row_sticker);


        stickerView = findViewById(R.id.stickerView);

        rv_sticker = findViewById(R.id.rv_sticker);
        LinearLayoutManager manager = new LinearLayoutManager(BokehEffectActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_sticker.setLayoutManager(manager);

        sb_opacity = findViewById(R.id.sb_opacity);
        tv_opacity = findViewById(R.id.tv_opacity);


        iv_no_color_effect = findViewById(R.id.iv_no_color_effect);

        iv_no_color_effect.setScaleType(ImageView.ScaleType.FIT_XY);

        iv_save.setOnClickListener(this);
        iv_my_albums.setOnClickListener(this);

        ll_cancel.setOnClickListener(this);
        iv_color_effect.setOnClickListener(this);
        iv_effect.setOnClickListener(this);
        iv_text.setOnClickListener(this);

        iv_sticker.setOnClickListener(this);
        iv_no_effect.setOnClickListener(this);
        iv_no_color_effect.setOnClickListener(this);


        if (Share.CROPPED_IMAGE != null) {
            img_main.setImageBitmap(Share.CROPPED_IMAGE);
            Share.IMAGE_BITMAP = Share.CROPPED_IMAGE;

            final int height = (int) Math.ceil(Share.screenWidth * (float) img_main.getDrawable().getIntrinsicHeight() / img_main.getDrawable().getIntrinsicWidth());
            img_main.getLayoutParams().height = height;

            vto = img_main.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {

                    img_main.getViewTreeObserver().removeOnPreDrawListener(this);

                    imgFinalHeight = img_main.getMeasuredHeight();
                    imgFinalWidth = img_main.getMeasuredWidth();

                    // Manage image width based on height
                    if (height > imgFinalWidth) {
//                        Log.e("Image", "Taller");
                        imgFinalWidth = (int) Math.ceil(imgFinalHeight * (float) img_main.getDrawable().getIntrinsicWidth() / img_main.getDrawable().getIntrinsicHeight());
                    }

                    img_main.getLayoutParams().width = imgFinalWidth;

                    RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(imgFinalWidth, imgFinalHeight);
                    /*RLParams.weight = 1.0f;
                    RLParams.gravity = Gravity.CENTER;*/
                    RLParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    rl_main.setLayoutParams(RLParams);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imgFinalWidth, imgFinalHeight);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    img_effect.setLayoutParams(layoutParams);

                    RelativeLayout.LayoutParams stickerParams = new RelativeLayout.LayoutParams(imgFinalWidth, imgFinalHeight);
                    stickerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    stickerView.setLayoutParams(layoutParams);

                    return true;
                }
            });

            Util.bitmapPhoto = Share.IMAGE_BITMAP;
            gpu_image_filter.setImage(Util.bitmapPhoto);
        }

        Share.isStickerAvail = false;
        if (!Share.isStickerAvail) {
            Share.isStickerTouch = false;
            stickerView.setLocked(true);
        }

        list.clear();
        Share.View_List_Effects.clear();
        Share.View_List_Vintage.clear();
        Share.EFFECT_BITMAP_OPACITY = 70;
        //changeSeekbarColor(sb_opacity, getResources().getColor(R.color.white), Color.WHITE, getResources().getColor(R.color.seek_grey));

    }

    @Override
    public void onClick(View v) {

        if (v == iv_color_effect) {

            iv_color_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), PorterDuff.Mode.SRC_IN);

            iv_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_text.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            txtbokeh.setTextColor(getResources().getColor(R.color.yellowcolor));

            txteffect.setTextColor(getResources().getColor(R.color.greycolor));
            txttext.setTextColor(getResources().getColor(R.color.greycolor));
            txtsticker.setTextColor(getResources().getColor(R.color.greycolor));

            hv_scroll_color_effect.setVisibility(View.VISIBLE);
            hv_scroll_effect.setVisibility(View.GONE);

            ll_row_sticker.setVisibility(View.GONE);

            setColorEffectThumbRow();

            //rl_background.setVisibility (View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            //rl_background.startAnimation (bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            // ll_menu.startAnimation(bottomDown);
            //ll_menu.setVisibility(View.GONE);

            ll_cancel.setVisibility(View.VISIBLE);
            rl_opacity.setVisibility(View.VISIBLE);
            seeklayout.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_cancel.startAnimation(bottomDown);

            sb_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    img_effect.setAlpha((float) progress / 100);
                    tv_opacity.setText((100 * progress) / maxOpacity + "");

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } else if (v == iv_text) {


            iv_color_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            iv_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_text.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), PorterDuff.Mode.SRC_IN);
            iv_sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            txtbokeh.setTextColor(getResources().getColor(R.color.greycolor));

            txteffect.setTextColor(getResources().getColor(R.color.greycolor));
            txttext.setTextColor(getResources().getColor(R.color.yellowcolor));
            txtsticker.setTextColor(getResources().getColor(R.color.greycolor));

//            Intent i = new Intent(BokehEffectActivity.this, FontActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (v == iv_effect) {


            iv_color_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            iv_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), PorterDuff.Mode.SRC_IN);
            iv_text.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            txtbokeh.setTextColor(getResources().getColor(R.color.greycolor));

            txteffect.setTextColor(getResources().getColor(R.color.yellowcolor));
            txttext.setTextColor(getResources().getColor(R.color.greycolor));
            txtsticker.setTextColor(getResources().getColor(R.color.greycolor));

            hv_scroll_color_effect.setVisibility(View.GONE);
            hv_scroll_effect.setVisibility(View.GONE);


            ll_row_sticker.setVisibility(View.GONE);

            rl_opacity.setVisibility(View.VISIBLE);
            if (seeklayout.getVisibility() == View.VISIBLE) {

                seeklayout.setVisibility(View.INVISIBLE);
            }
            setEffectThumbRow();

        } else if (v == iv_no_effect) {

            Share.IMAGE_BITMAP = Util.bitmapPhoto;
            updateEffectRowFrame();

            img_main.setImageBitmap(Share.IMAGE_BITMAP);

        } else if (v == iv_no_color_effect) {

//            img_effect.destroyDrawingCache();
//            img_effect.invalidate();
            img_effect.setVisibility(View.GONE);
            updateRowFrame();


        } else if (v == iv_sticker) {


            iv_color_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);

            iv_effect.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_text.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greycolor), PorterDuff.Mode.SRC_IN);
            iv_sticker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.yellowcolor), PorterDuff.Mode.SRC_IN);

            txtbokeh.setTextColor(getResources().getColor(R.color.greycolor));

            txteffect.setTextColor(getResources().getColor(R.color.greycolor));
            txttext.setTextColor(getResources().getColor(R.color.greycolor));
            txtsticker.setTextColor(getResources().getColor(R.color.yellowcolor));


            rl_opacity.setVisibility(View.VISIBLE);
            if (seeklayout.getVisibility() == View.VISIBLE) {

                seeklayout.setVisibility(View.INVISIBLE);
            }

            hv_scroll_color_effect.setVisibility(View.GONE);
            hv_scroll_effect.setVisibility(View.GONE);

            ll_row_sticker.setVisibility(View.GONE);

            setStickerThumb();

        } else if (v == iv_my_albums) {


            onBackPressed();

        } else if (v == iv_save) {

            hv_scroll_color_effect.setVisibility(View.GONE);
            hv_scroll_effect.setVisibility(View.GONE);
            ll_row_sticker.setVisibility(View.GONE);

            stickerView.setControlItemsHidden();

            if (rl_background.getChildCount() > 0) {
                saveImage();

                // ll_menu.setVisibility(View.VISIBLE);
                //rl_background.setVisibility (View.GONE);
                ll_cancel.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(BokehEffectActivity.this, "Blank image not save", Toast.LENGTH_SHORT).show();
            }

        } else if (v == ll_cancel) {

            hv_scroll_color_effect.setVisibility(View.GONE);
            hv_scroll_color_effect.setVisibility(View.GONE);
            hv_scroll_effect.setVisibility(View.GONE);
            ll_row_sticker.setVisibility(View.GONE);

//            Util.bitmapPhoto = Share.IMAGE_BITMAP;
//            gpu_image_filter.setImage(Util.bitmapPhoto);

            // ll_menu.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            // ll_menu.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            // rl_background.startAnimation (bottomDown);
            // rl_background.setVisibility (View.GONE);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_cancel.startAnimation(bottomDown);
            ll_cancel.setVisibility(View.INVISIBLE);

            if (rl_opacity.getVisibility() == View.VISIBLE) {
                bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                rl_opacity.startAnimation(bottomDown);
                rl_opacity.setVisibility(View.INVISIBLE);
            }
        }

    }


    /* ------------------- Color Image Blend Sub Menu Option ------------------- */
    private void setDefaultColorEffect() {
        try {
            final String[] effectPath = assetManager.list("overlay_image");

            try {
                InputStream inputstream = getAssets().open("overlay_image/" + effectPath[0]);
                if (inputstream != null) {
                    Drawable drawable = Drawable.createFromStream(inputstream, null);

                    img_effect.invalidate();
                    img_effect.setImageBitmap(Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), Share.CROPPED_IMAGE.getWidth(), Share.CROPPED_IMAGE.getHeight(), false));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    private void setColorEffectThumbRow() {

        ll_row_color_effect.removeAllViews();
        try {
            final String[] imgPath = assetManager.list("overlay_thumb");
            sortArray(imgPath, "overlay_");

            final String[] effectPath = assetManager.list("overlay_image");
            sortArray(effectPath, "blend_");

            for (int j = 0; j < imgPath.length; j++) {

                InputStream is = assetManager.open("overlay_thumb/" + imgPath[j]);
                Drawable drawable = Drawable.createFromStream(is, null);

                inflater = LayoutInflater.from(getApplicationContext());
                final View viewFrame = inflater.inflate(R.layout.background_row, null);

                ImageView iv_frame = viewFrame.findViewById(R.id.iv_bg);
                iv_frame.setImageDrawable(drawable);
                iv_frame.setBackgroundResource(R.drawable.selected_effect_bokeh);

                final int finalJ = j;

                if (iv_no_color_effect.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.selected_effect_bokeh).getConstantState()))

                    if (Share.SELECTED_OVERLAY_POS == finalJ) {
                        iv_frame.setBackgroundResource(R.drawable.effect_bokeh);
                    } else {
                        iv_frame.setBackgroundResource(R.drawable.selected_effect_bokeh);
                    }

                // if (iv_no_color_effect.getBackground ().get == Color.WHITE)

                viewFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Log.e("path", effectPath[finalJ]);
                            InputStream inputstream = assetManager.open("overlay_image/" + effectPath[finalJ]);
                            if (inputstream != null) {

                                iv_no_color_effect.setBackgroundResource(R.drawable.selected_effect_bokeh);
                                //iv_no_color_effect.setBorderColor (Color.WHITE);
                                for (int k = 0; k < ll_row_color_effect.getChildCount(); k++) {
                                    ImageView iv = ll_row_color_effect.getChildAt(k).findViewById(R.id.iv_bg);

                                    if (finalJ == k) {
                                        iv.setBackgroundResource(R.drawable.effect_bokeh);
                                    } else {
                                        iv.setBackgroundResource(R.drawable.selected_effect_bokeh);
                                    }
                                }

                                Share.SELECTED_OVERLAY_POS = finalJ;

                                img_effect.setVisibility(View.VISIBLE);
                                Drawable drawable = Drawable.createFromStream(inputstream, null);
                                img_effect.invalidate();
                                img_effect.setImageBitmap(Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), Share.CROPPED_IMAGE.getWidth(), Share.CROPPED_IMAGE.getHeight(), false));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                ll_row_color_effect.addView(viewFrame);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRowFrame() {

//        iv_no_color_effect.setBorderColor (getResources ().getColor (R.color.yellowcolor));
        iv_no_color_effect.setBackgroundResource(R.drawable.effect_bokeh);
        for (int k = 0; k < ll_row_color_effect.getChildCount(); k++) {
            ImageView iv = ll_row_color_effect.getChildAt(k).findViewById(R.id.iv_bg);
            iv.setBackgroundResource(R.drawable.selected_effect_bokeh);
        }
    }

    /* ------------------- Effect Sub Menu Option ------------------- */
    private void showEffectViewRow() {
        hv_scroll_effect.setVisibility(View.VISIBLE);

        //  rl_background.setVisibility (View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        //  rl_background.startAnimation (bottomDown);

        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        // ll_menu.startAnimation(bottomDown);
        // ll_menu.setVisibility(View.GONE);

        ll_cancel.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        ll_cancel.startAnimation(bottomDown);
    }

    private void setEffectThumbRow() {

        try {
            if (Share.View_List_Effects.size() == 0) {
                new setEffectRowThumb().execute();

            } else {

                ll_row_effect.removeAllViews();
                showEffectViewRow();

                if (Share.View_List_Effects.size() > 0) {

                    for (int i = 0; i < Share.View_List_Effects.size(); i++) {
                        RemoveParent(Share.View_List_Effects.get(i));
                        ll_row_effect.addView(Share.View_List_Effects.get(i));
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

            effect_progress = Share.createProgressDialog(BokehEffectActivity.this);
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

                ll_row_effect.removeAllViews();

                for (int i = 0; i < Share.View_List_Effects.size(); i++) {
                    RemoveParent(Share.View_List_Effects.get(i));
                    ll_row_effect.addView(Share.View_List_Effects.get(i));
                }
                showEffectViewRow();
                effect_progress.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void effect_row(final int i) {

        final View view = getLayoutInflater().inflate(R.layout.row_gpu_effect, null, false);
        try {

            iv_effect_item = view.findViewById(R.id.effect);
            iv_effect_item.setScaleType(ImageView.ScaleType.FIT_XY);

            GPUImage gpuImage = new GPUImage(this);
            gpuImage.setImage(Share.IMAGE_BITMAP);
            gpuImage.setFilter(createFilterForType(BokehEffectActivity.this, filters.filters.get(i)));

            iv_effect_item.setImageBitmap(gpuImage.getBitmapWithFilterApplied());

            if (i == 0) {
                iv_effect_item.setBackgroundResource(R.drawable.selected_effect_bokeh);
            }

            final int finalI = i;

            iv_effect_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    iv_no_effect.setBackgroundResource(R.drawable.selected_effect_bokeh);
                    for (int k = 0; k < ll_row_effect.getChildCount(); k++) {
                        ImageView iv = ll_row_effect.getChildAt(k).findViewById(R.id.effect);

                        if (i == k) {
                            iv.setBackgroundResource(R.drawable.effect_bokeh);
                        } else {
                            iv.setBackgroundResource(R.drawable.selected_effect_bokeh);
                        }
                    }

                    if (Util.bitmapPhoto != null) {
                        gpu_image_filter.setFilter(createFilterForType(BokehEffectActivity.this, filters.filters.get(finalI)));
                        new getEffectedBitmap().execute();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        Share.View_List_Effects.add(view);
    }

    public void updateEffectRowFrame() {

        iv_no_effect.setBackgroundResource(R.drawable.effect_bokeh);
        for (int k = 0; k < ll_row_effect.getChildCount(); k++) {
            ImageView iv = ll_row_effect.getChildAt(k).findViewById(R.id.effect);
            iv.setBackgroundResource(R.drawable.selected_effect_bokeh);
        }
    }

    public class getEffectedBitmap extends AsyncTask<Void, Void, Void> {

        ProgressDialog apply_effect_progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            apply_effect_progress = Share.createProgressDialog(BokehEffectActivity.this);
            apply_effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getGPUImage();
            return null;
        }

        private void getGPUImage() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Share.IMAGE_BITMAP = gpu_image_filter.getBitmapWithFilterApplied();
//                    Util.bitmapPhoto = Share.IMAGE_BITMAP;
                    img_main.invalidate();
                    img_main.setImageBitmap(null);
                    img_main.setImageBitmap(Share.IMAGE_BITMAP);
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


    /* ------------------- Sticker Sub Menu Option ------------------- */
    private void setStickerThumb() {

        if (list.size() == 0) {
            new AsynTask().execute();
        } else {
            showStickerRow();
        }
    }

    private class AsynTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Share.createProgressDialog(BokehEffectActivity.this);
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                final String[] imgPath = assetManager.list("stickers");
                sortArray(imgPath, "sticker_");

                for (int j = 0; j < imgPath.length; j++) {

                    InputStream is = assetManager.open("stickers/" + imgPath[j]);
                    Drawable drawable = Drawable.createFromStream(is, null);

                    StickerModel stickerModel = new StickerModel();
                    stickerModel.setDrawable(drawable);
                    list.add(stickerModel);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
            showStickerRow();

            StickerAdapter stickerAdapter = new StickerAdapter(BokehEffectActivity.this, list);
            rv_sticker.setAdapter(stickerAdapter);
        }
    }

    private void showStickerRow() {

        ll_row_sticker.setVisibility(View.VISIBLE);

        // rl_background.setVisibility (View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        // rl_background.startAnimation (bottomDown);

        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        // ll_menu.startAnimation(bottomDown);
        //ll_menu.setVisibility(View.GONE);

        ll_cancel.setVisibility(View.VISIBLE);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
        ll_cancel.startAnimation(bottomDown);
    }


    /* ------------------- Image Editing Processing Methods ------------------- */
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

    public void ChangeVintageEffectBlend() {

        // For apply Effect to Original Image
        bmEffected_img = Util.bitmapPhoto;

        Bitmap localBitmap = Bitmap.createBitmap(Share.IMAGE_BITMAP.getWidth(), Share.IMAGE_BITMAP.getHeight(), Bitmap.Config.ARGB_8888);
        Share.EFFECT_BITMAP = Bitmap.createScaledBitmap(Share.EFFECT_BITMAP, Share.IMAGE_BITMAP.getWidth(), Share.IMAGE_BITMAP.getHeight(), true);

        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);

        localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        localCanvas.drawBitmap(bmEffected_img, 0.0F, 0.0F, null);
        localCanvas.drawBitmap(Share.EFFECT_BITMAP, 0.0F, 0.0F, localPaint);
        localPaint.setXfermode(null);

        Share.IMAGE_BITMAP = localBitmap;

        img_main.invalidate();
        img_main.setImageBitmap(null);
        img_main.setImageBitmap(Share.IMAGE_BITMAP);
    }

    /* ------------------- Save Edited Image ------------------- */
    private void saveImage() {

        rl_main.setDrawingCacheEnabled(true);
        Share.EDITED_IMAGE = Bitmap.createBitmap(rl_main.getDrawingCache());
        rl_main.setDrawingCacheEnabled(false);

        save_task = new saveImage().execute();
    }

    public class saveImage extends AsyncTask<Void, Void, Void> {

        Dialog saveDialog;
        Bitmap finalBmp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            saveDialog = Share.showProgress(BokehEffectActivity.this, "Saving...");
            saveDialog.show();
            finalBmp = Share.EDITED_IMAGE;
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
                Toast.makeText(BokehEffectActivity.this, getString(R.string.save_image), Toast.LENGTH_LONG).show();
                Share.Fragment = "MyPhotosFragment";
                nextActivity();
            }
        }

    }

    private void nextActivity() {

        save_task = null;
        Intent intent = new Intent(BokehEffectActivity.this, FullScreenImageActivity.class);
        Share.Fragment = "MyPhotosFragment";
        intent.putExtra("avairy", "");
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /* ------------------- Helper Methods ------------------- */
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

    public void RemoveParent(View child) {

        LinearLayout ll_test = (LinearLayout) child.getParent();
        if (ll_test != null && ll_test.getChildCount() > 0) {
            ll_test.removeAllViews();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //((ViewGroup) rv_main.getParent()).removeView(rv_main);

        if (gpu_image_filter != null) {
            gpu_image_filter = null;

        }
        if (img_effect != null) {
            img_effect = null;
            System.gc();
            Runtime.getRuntime().gc();
        }

        if (bmEffected_img != null) {

            bmEffected_img = null;
            System.gc();
            Runtime.getRuntime().gc();
        }
        if (filters != null) {

            filters = null;
            System.gc();
            Runtime.getRuntime().gc();
        }
        if (Share.BG_GALLERY != null) {

            Share.BG_GALLERY = null;
            System.gc();
            Runtime.getRuntime().gc();
        }
        if (Share.CROPPED_IMAGE != null && Share.CROPPED_IMAGE.isRecycled()) {

            Share.CROPPED_IMAGE.recycle();
            Share.CROPPED_IMAGE = null;
            System.gc();
            Runtime.getRuntime().gc();
        }

        if (drawables_sticker != null) {

            drawables_sticker = null;
            System.gc();
            Runtime.getRuntime().gc();
        }
        Share.selectedFragment = null;
        System.gc();
        Runtime.getRuntime().gc();

        isInForeGround = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isInForeGround = false;
    }

    @Override
    public void onBackPressed() {

        System.gc();
        Runtime.getRuntime().gc();

        final Dialog dialog = new Dialog(BokehEffectActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


                //((ViewGroup) rv_main.getParent()).removeView(rv_main);
                if (gpu_image_filter != null) {
                    gpu_image_filter = null;

                }
                if (img_effect != null) {
                    img_effect = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }

                if (bmEffected_img != null) {

                    bmEffected_img = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }
                if (filters != null) {

                    filters = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }
                if (Share.BG_GALLERY != null) {

                    Share.BG_GALLERY = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }
                if (Share.CROPPED_IMAGE != null && Share.CROPPED_IMAGE.isRecycled()) {

                    Share.CROPPED_IMAGE.recycle();
                    Share.CROPPED_IMAGE = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }

                if (drawables_sticker != null) {

                    drawables_sticker = null;
                    System.gc();
                    Runtime.getRuntime().gc();
                }
                Share.selectedFragment = null;
                System.gc();
                Runtime.getRuntime().gc();
                finish();
                dialog.dismiss();
            }
        });
        if (dialog.isShowing()) {

            dialog.cancel();
        } else {
            dialog.show();
        }
        //
       /* System.gc();
        Runtime.getRuntime().gc();

        final Dialog dialog = new Dialog(BokehEffectActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_editing);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                com.sketch.photo.maker.pencil.art.drawing.Share.Share.selectedFragment = null;
                finish();
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

        if (dialog != null && !dialog.isShowing())
            dialog.show();*/
    }
}
