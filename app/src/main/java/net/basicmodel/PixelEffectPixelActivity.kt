package net.basicmodel

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pixel_effect.*
import net.adapter.StickerPixelAdapter
import net.entity.StickerModel
import net.event.MessageEvent
import net.utils.DisplayMetricsHandler
import net.utils.Share
import net.widget.CircularProgressBar
import net.widget.DrawableStickerPixel
import net.widget.ImagePicker
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class PixelEffectPixelActivity : AppCompatActivity() {

    val STORAGE_PERMISSION_CODE = 23
    val STORAGE_PERMISSION_CODE_CAMERA = 22
    private val REQUEST_SETTINGS_PERMISSION = 102

    private val listPermissionsNeeded: ArrayList<String> = ArrayList()
    private val listPermissionsNeeded1: ArrayList<String> = ArrayList()

    private val TAG = PixelEffectPixelActivity::class.java.simpleName

    private var image_name = ""
    var progress: CircularProgressBar? = null
    var isInForeGround = false

    private val list: ArrayList<StickerModel> = ArrayList<StickerModel>()

    var save_task: AsyncTask<*, *, *>? = null
    var imgFinalHeight = 0
    var imgFinalWidth: Int = 0
    private var assetManager: AssetManager? = null
    var color_array: Array<String>? = null
    private val bottomUp: Animation? = null
    private var bottomDown: Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixel_effect)
        Log.i("xxxxxxH", "PixelEffectPixelActivity")
        EventBus.getDefault().register(this)
        Share.screenWidth = windowManager.defaultDisplay.width
        Share.screenHeight = windowManager.defaultDisplay.height
        initView()
        onclick()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()[0] as String
        val drawableSticker: DrawableStickerPixel = event.getStickerPixel()
        if (TextUtils.equals(msg, "addSticker")) {
            stickerView.addSticker(drawableSticker)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Share.CROPPED_IMAGE != null) {
            img_main.invalidate()
            img_main.setImageBitmap(null)
            img_main.setImageBitmap(Share.CROPPED_IMAGE)
            val a = (Share.screenWidth * img_main.drawable.intrinsicHeight).toDouble()
            val b = (img_main.drawable.intrinsicWidth).toDouble()
            val height = Math.ceil(a / b).toInt()

            img_main.layoutParams.height = height
            val vto = img_main.viewTreeObserver
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    img_main.viewTreeObserver.removeOnPreDrawListener(this)
                    imgFinalHeight = img_main.measuredHeight
                    imgFinalWidth = img_main.measuredWidth

                    // Manage image width based on height
                    if (height > imgFinalWidth) {
                        val a = (imgFinalHeight * img_main.drawable
                            .intrinsicWidth).toDouble()
                        val b = (img_main.drawable
                            .intrinsicHeight).toDouble()
                        imgFinalWidth = Math.ceil(a / b).toInt()
                    }
                    img_main.layoutParams.width = imgFinalWidth
                    val RLParams: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(imgFinalWidth, imgFinalHeight)
                    RLParams.weight = 1.0f
                    RLParams.gravity = Gravity.CENTER
                    rl_main.layoutParams = RLParams
                    val layoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(imgFinalWidth, imgFinalHeight)
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                    stickerView.layoutParams = layoutParams
                    return true
                }
            })
            Share.IMG_HEIGHT = img_main.layoutParams.height
            Share.IMG_WIDTH = img_main.layoutParams.width


        }
        if (Share.FONT_FLAG) {
            Share.FONT_FLAG = false
            Share.IsSelectFrame = false
            val drawableSticker: DrawableStickerPixel = Share.TEXT_DRAWABLE1
            drawableSticker.tag = "text"
            stickerView.addSticker(drawableSticker)
        }

    }

    private fun initView() {
        assetManager = assets
        progress = CircularProgressBar(this@PixelEffectPixelActivity)
        Share.View_List_Sticker.clear()
        list.clear()

        val manager = LinearLayoutManager(this@PixelEffectPixelActivity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        rv_sticker.layoutManager = manager

        setDefaultFrame()
        color_array = applicationContext.resources.getStringArray(R.array.al_color)
    }

    private fun onclick() {
        iv_gallery.setOnClickListener {
            iv_gallery.setColorFilter(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.yellowcolor
                ), PorterDuff.Mode.SRC_IN
            )

            iv_pixel_effect.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_color.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            iv_sticker.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_text.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            txtgallery.setTextColor(resources.getColor(R.color.yellowcolor))
            txteffect.setTextColor(resources.getColor(R.color.greycolor))
            txtcolor.setTextColor(resources.getColor(R.color.greycolor))
            txtsticker.setTextColor(resources.getColor(R.color.greycolor))
            txttext.setTextColor(resources.getColor(R.color.greycolor))

            OptionDialog()
        }
        iv_pixel_effect.setOnClickListener {
            iv_gallery.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            iv_pixel_effect.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.yellowcolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_color.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            iv_sticker.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_text.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            txtgallery.setTextColor(resources.getColor(R.color.greycolor))
            txteffect.setTextColor(resources.getColor(R.color.yellowcolor))
            txtcolor.setTextColor(resources.getColor(R.color.greycolor))
            txtsticker.setTextColor(resources.getColor(R.color.greycolor))
            txttext.setTextColor(resources.getColor(R.color.greycolor))


            Log.e("Click", "iv_pixel_effects")

            hv_scroll_frame.visibility = View.VISIBLE
            hv_scroll_frame.background = resources.getDrawable(R.color.black)
            hv_scroll_color.visibility = View.GONE
            ll_row_sticker.visibility = View.GONE

            setEffectFrameThumb()

            rl_background.visibility = View.VISIBLE
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
            rl_background.startAnimation(bottomDown)

            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_down)
            // ll_menu.startAnimation(bottomDown);
            // ll_menu.setVisibility(View.GONE);

            // ll_menu.startAnimation(bottomDown);
            // ll_menu.setVisibility(View.GONE);
            iv_cancel.visibility = View.VISIBLE
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
            iv_cancel.startAnimation(bottomDown)
        }
        iv_color.setOnClickListener {
            iv_gallery.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            iv_pixel_effect.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_color.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )
            iv_sticker.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_text.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            txtgallery.setTextColor(resources.getColor(R.color.greycolor))
            txteffect.setTextColor(resources.getColor(R.color.greycolor))
            txtcolor.setTextColor(resources.getColor(R.color.yellowcolor))
            txtsticker.setTextColor(resources.getColor(R.color.greycolor))
            txttext.setTextColor(resources.getColor(R.color.greycolor))

            Log.e("Click", "Color Menu")
            hv_scroll_frame.visibility = View.GONE
            hv_scroll_color.visibility = View.VISIBLE
            ll_row_sticker.visibility = View.GONE

            setbgColors()

            rl_background.visibility = View.VISIBLE
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
            rl_background.startAnimation(bottomDown)

            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_down)
            // ll_menu.startAnimation(bottomDown);
            // ll_menu.setVisibility(View.GONE);

            // ll_menu.startAnimation(bottomDown);
            // ll_menu.setVisibility(View.GONE);
            iv_cancel.visibility = View.VISIBLE
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
            iv_cancel.startAnimation(bottomDown)
        }
        iv_sticker.setOnClickListener {
            iv_gallery.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            iv_pixel_effect.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_color.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            iv_sticker.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.yellowcolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_text.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            txtgallery.setTextColor(resources.getColor(R.color.greycolor))
            txteffect.setTextColor(resources.getColor(R.color.greycolor))
            txtcolor.setTextColor(resources.getColor(R.color.greycolor))
            txtsticker.setTextColor(resources.getColor(R.color.yellowcolor))
            txttext.setTextColor(resources.getColor(R.color.greycolor))

            Log.e("Click", "StickerPixel Menu")
            hv_scroll_frame.visibility = View.GONE
            hv_scroll_color.visibility = View.GONE
            ll_row_sticker.visibility = View.GONE

            setStickerThumb()
        }

        iv_text.setOnClickListener {
            iv_gallery.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            iv_pixel_effect.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_color.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            iv_sticker.setColorFilter(
                ContextCompat.getColor(
                    applicationContext, R.color.greycolor
                ), PorterDuff.Mode.SRC_IN
            )
            iv_text.setColorFilter(
                ContextCompat.getColor(applicationContext, R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            txtgallery.setTextColor(resources.getColor(R.color.greycolor))
            txteffect.setTextColor(resources.getColor(R.color.greycolor))
            txtcolor.setTextColor(resources.getColor(R.color.greycolor))
            txtsticker.setTextColor(resources.getColor(R.color.greycolor))
            txttext.setTextColor(resources.getColor(R.color.yellowcolor))

            startActivity(
                Intent(
                    this@PixelEffectPixelActivity,
                    FontPixelActivity::class.java
                )
            )
        }

        iv_my_albums.setOnClickListener {
            onBackPressed()
        }
        iv_save.setOnClickListener {
            stickerView.setControlItemsHidden()
            if (rl_background.childCount > 0) {
                saveImage()

                // ll_menu.setVisibility(View.VISIBLE);
                // rl_background.setVisibility(View.GONE);
                iv_cancel.visibility = View.INVISIBLE
            } else {
                Toast.makeText(
                    this@PixelEffectPixelActivity,
                    "Blank image not save",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        iv_cancel.setOnClickListener {
            hv_scroll_frame.visibility = View.GONE
            hv_scroll_color.visibility = View.GONE
            ll_row_sticker.visibility = View.GONE

            ll_menu.visibility = View.VISIBLE
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_down)
            bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_down)
            iv_cancel.startAnimation(bottomDown)
            iv_cancel.visibility = View.INVISIBLE
        }
    }

    private fun saveImage() {
        rl_main.isDrawingCacheEnabled = true
        Share.EDITED_IMAGE = Bitmap.createBitmap(rl_main.drawingCache)
        rl_main.isDrawingCacheEnabled = false
        save_task = saveImage1().execute()
    }

    private fun setDefaultFrame() {
        try {
            val effectPath = assetManager!!.list("frame")
            val eis = assetManager!!.open("frame/" + effectPath!![0])
            try {
                val inputstream =
                    this@PixelEffectPixelActivity.assets.open("frame/" + effectPath[0])
                if (inputstream != null) {
                    val drawable = Drawable.createFromStream(inputstream, null)
                    val drawableSticker =
                        DrawableStickerPixel(makeFrameSticker(BitmapFactory.decodeStream(eis)))
                    drawableSticker.tag = "frame"
                    stickerView.addSticker(drawableSticker)

//                    Share.FRAME_POS = 0;
                    Share.IsSelectFrame = true
                    Share.isFrameAvailable = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
        }
    }

    private fun makeFrameSticker(bitmap: Bitmap): Drawable {
        val d: Drawable = BitmapDrawable(resources, bitmap)
        val mMode = PorterDuff.Mode.SRC_OUT
        d.setColorFilter(Color.parseColor(Share.BG_COLOR), mMode)
        Share.EFFECT_DRAWABLE = d
        return d
    }

    fun OptionDialog() {
        val view: View = layoutInflater.inflate(R.layout.option_dialog, null)
        val change_bg: Button
        val change_fg: Button
        val cancel_option: Button
        change_bg = view.findViewById<View>(R.id.change_bg) as Button
        change_fg = view.findViewById<View>(R.id.change_fg) as Button
        cancel_option = view.findViewById<View>(R.id.cancel_option) as Button
        val mBottomSheetDialog = Dialog(this@PixelEffectPixelActivity, R.style.DialogAnimation)
        mBottomSheetDialog.setContentView(view)
        mBottomSheetDialog.setCancelable(true)
        mBottomSheetDialog.window!!
            .setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        mBottomSheetDialog.window!!.setGravity(Gravity.BOTTOM)
        mBottomSheetDialog.show()
        change_bg.text = "Camera"
        change_fg.text = "Gallery"
        change_bg.setOnClickListener {
            mBottomSheetDialog.dismiss()
            cemeraclick()
        }
        change_fg.setOnClickListener {
            mBottomSheetDialog.dismiss()
            System.gc()
            Runtime.getRuntime().gc()
            photosclick()
        }
        cancel_option.setOnClickListener { // TODO Auto-generated method stub
            Share.flag = 0
            bottomDown = AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.bottom_down
            )
            mBottomSheetDialog.dismiss()
        }
    }

    private fun cemeraclick() {
        if (checkAndRequestPermissionscamera()) {

            //Share.SPLASH_GALLERY_FLAG = true;
            image_name = "camera"
            ImagePicker.pickImage(this@PixelEffectPixelActivity, "Select your image:")
        } else {
            image_name = "camera"
            ActivityCompat.requestPermissions(
                this@PixelEffectPixelActivity,
                listPermissionsNeeded1.toTypedArray(),
                STORAGE_PERMISSION_CODE_CAMERA
            )
        }
    }

    private fun photosclick() {
        if (checkAndRequestPermissions()) {
            image_name = "gallery"
            val i = Intent(
                this@PixelEffectPixelActivity,
                GalleryPixelActivity::class.java
            )
            startActivity(i)
            //overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            image_name = "gallery"
            ActivityCompat.requestPermissions(
                this@PixelEffectPixelActivity,
                listPermissionsNeeded.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun checkAndRequestPermissionscamera(): Boolean {
        listPermissionsNeeded1.clear()
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded1.add(Manifest.permission.CAMERA)
        }
        return listPermissionsNeeded1.isEmpty()
    }

    private fun checkAndRequestPermissions(): Boolean {
        listPermissionsNeeded.clear()
        //     listPermissionsNeeded1.clear();
        val storage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readStorage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return listPermissionsNeeded.isEmpty()
    }

    private fun sortArray(arrayList: Array<String>, frontStr: String) {
        Arrays.sort(arrayList, object : Comparator<String?> {

            override fun compare(p0: String?, p1: String?): Int {
                val file1 = p0!!.filter { it.isDigit() }
                val file2 = p1!!.filter { it.isDigit() }
                return file1.compareTo(file2)
            }
        })
    }

    private fun setEffectFrameThumb() {
        ll_row_frame.removeAllViews()
        try {
            val imgPath = assetManager!!.list("frame_thumb")
            if (imgPath != null) {
                sortArray(imgPath, "thumb_pixel_")
            }
            val effectPath = assetManager!!.list("frame")
            if (effectPath != null) {
                sortArray(effectPath, "pixel_")
            }
            for (j in imgPath!!.indices) {
                val `is` = assetManager!!.open("frame_thumb/" + imgPath[j])
                val drawable = Drawable.createFromStream(`is`, null)
                val inflater = LayoutInflater.from(applicationContext)
                val viewFrame: View = inflater.inflate(R.layout.thumb_row, null)
                val iv_frame = viewFrame.findViewById<View>(R.id.iv_bg) as ImageView
                iv_frame.setImageDrawable(drawable)
                iv_frame.setBackgroundResource(R.drawable.frame_border)
                viewFrame.setOnClickListener {
                    try {
                        val inputstream =
                            this@PixelEffectPixelActivity.assets.open("frame/" + effectPath!![j])
                        if (inputstream != null) {
                            val drawable =
                                Drawable.createFromStream(inputstream, null)
                            Log.e(
                                "Share.isFrameAvailable",
                                Share.isFrameAvailable.toString() + ""
                            )
                            Log.e(
                                "Share.IsSelectFrame",
                                Share.IsSelectFrame.toString() + ""
                            )
                            if (Share.isFrameAvailable) {
                                if (Share.IsSelectFrame) {
                                    Log.e("Frame", "replace")
                                    val drawableSticker =
                                        DrawableStickerPixel(makeFrameSticker((drawable as BitmapDrawable).bitmap))
                                    drawableSticker.tag = "frame"
                                    stickerView.replace(drawableSticker)
                                    Share.isFrameAvailable = true
                                    Share.IsSelectFrame = true
                                    //Share.FRAME_POS = finalJ;
                                } else {
                                    Log.e("select_frame", "select_frame error")
                                    Share.showAlert(
                                        this@PixelEffectPixelActivity,
                                        "",
                                        getString(R.string.select_frame)
                                    )
                                    //                                        Toast.makeText(getApplicationContext(), getString(R.string.select_frame), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.e("Frame", "add")
                                val drawableSticker =
                                    DrawableStickerPixel(makeFrameSticker((drawable as BitmapDrawable).bitmap))
                                drawableSticker.tag = "frame"
                                stickerView.addSticker(drawableSticker)
                                Share.isFrameAvailable = true
                                Share.IsSelectFrame = true
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                ll_row_frame.addView(viewFrame)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setbgColors() {
        ll_row_color.removeAllViews()
        stickerView.setControlItemsHidden()
        for (j in color_array!!.indices) {
            val inflater = LayoutInflater.from(applicationContext)
            val v: View = inflater.inflate(R.layout.background_color_row, null)
            try {
                val iv_color_select = v.findViewById<View>(R.id.iv_color_select) as ImageView
                val img_back = v.findViewById<View>(R.id.iv_color_circle) as ImageView
                img_back.setBackgroundColor(Color.parseColor(color_array!![j]))
                img_back.setBackgroundResource(R.drawable.color_border)
                val gd = img_back.background.current as GradientDrawable
                gd.setColor(Color.parseColor(color_array!![j]))
                if (Share.COLOR_POS == j) {
                    iv_color_select.visibility = View.VISIBLE
                }
            } catch (e: java.lang.Exception) {
            }
            v.setOnClickListener {

                Log.e("finalI", j.toString() + "")
                if (Share.isFrameAvailable) {
                    stickerView.setControlItemsHidden()
                    Share.BG_COLOR = color_array!![j]

                    for (i in 0 until Share.drawable_list.size) {
                        if (Share.EFFECT_DRAWABLE != null) {
                            Share.EFFECT_DRAWABLE.colorFilter = PorterDuffColorFilter(
                                Color.parseColor(Share.BG_COLOR),
                                PorterDuff.Mode.SRC_OUT
                            )
                            val drawableSticker = DrawableStickerPixel(Share.EFFECT_DRAWABLE)
                            drawableSticker.tag = "frame"
                            stickerView.replace(drawableSticker)
                        }
                    }
                } else {
                    Share.BG_COLOR = color_array!![j]
                    if (Share.EFFECT_DRAWABLE != null) {
                        val drawableSticker =
                            DrawableStickerPixel(makeFrameSticker((Share.EFFECT_DRAWABLE as BitmapDrawable).bitmap))
                        drawableSticker.tag = "frame"
                        stickerView.addSticker(drawableSticker)
                        Share.isFrameAvailable = true
                        Share.IsSelectFrame = true
                    }
                }
                Share.COLOR_POS = j
                updateColorView(j)
            }
            ll_row_color.addView(v)
        }
    }

    private fun updateColorView(finalI: Int) {
        for (i in 0 until ll_row_color.childCount) {
            val view = ll_row_color.getChildAt(i)
            val iv_color_select = view.findViewById<View>(R.id.iv_color_select) as ImageView
            if (finalI == i) {
                iv_color_select.visibility = View.VISIBLE
            } else {
                iv_color_select.visibility = View.GONE
            }
        }
    }

    private fun showStickerRow() {
        ll_row_sticker.visibility = View.VISIBLE
        rl_background.visibility = View.VISIBLE
        bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
        rl_background.startAnimation(bottomDown)
        bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_down)
        // ll_menu.startAnimation(bottomDown);
        // ll_menu.setVisibility(View.GONE);
        iv_cancel.visibility = View.VISIBLE
        bottomDown = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
        iv_cancel.startAnimation(bottomDown)
    }

    private fun setStickerThumb() {
        if (list.size == 0) {
            AsynTask().execute()
        } else {
            showStickerRow()
        }
    }

    inner class AsynTask :
        AsyncTask<Void?, Void?, Void?>() {
        var progressDialog: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog =
                Share.createProgressDialog(this@PixelEffectPixelActivity)
            progressDialog!!.setCancelable(false)
        }


        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            progressDialog!!.dismiss()
            showStickerRow()
            val stickerAdapter = StickerPixelAdapter(this@PixelEffectPixelActivity, list)
            rv_sticker.adapter = stickerAdapter
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val imgPath: Array<String> = assetManager!!.list("stickers") as Array<String>
                sortArray(imgPath, "sticker_")
                for (j in imgPath.indices) {
                    val `is`: InputStream = assetManager!!.open("stickers/" + imgPath[j])
                    val drawable = Drawable.createFromStream(`is`, null)
                    val stickerModel = StickerModel()
                    stickerModel.drawable = drawable
                    list.add(stickerModel)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    inner class saveImage1 :
        AsyncTask<Void?, Void?, Void?>() {
        var saveDialog: Dialog? = null
        var finalBmp: Bitmap? = null
        override fun onPreExecute() {
            super.onPreExecute()
            saveDialog = Share.showProgress(
                this@PixelEffectPixelActivity,
                "Saving..."
            )
            saveDialog!!.show()
            finalBmp = Share.EDITED_IMAGE
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            try {
                if (saveDialog!!.isShowing) {
                    saveDialog!!.dismiss()
                    Toast.makeText(
                        this@PixelEffectPixelActivity,
                        getString(R.string.save_image),
                        Toast.LENGTH_LONG
                    ).show()
                    Share.Fragment = "MyPhotosFragment"
                    nextActivity()
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            val path: File = File(Share.IMAGE_PATH)
            if (!path.exists()) path.mkdirs()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            try {
                if (finalBmp != null) {
                    val imageFile = File(path, "$timeStamp.png")
                    Log.e("TAG", "imageFile=>$imageFile")
                    if (!imageFile.exists()) imageFile.createNewFile()
                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(imageFile)
                        finalBmp!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        fos.close()
                        MediaScannerConnection.scanFile(
                            applicationContext,
                            arrayOf(imageFile.absolutePath),
                            null,
                            object : MediaScannerConnection.MediaScannerConnectionClient {
                                override fun onMediaScannerConnected() {}
                                override fun onScanCompleted(path: String, uri: Uri) {}
                            })
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            fos?.flush()
                            fos!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Log.e(
                        "TAG",
                        "Not Saved Image------------------------------------------------------->"
                    )
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    private fun nextActivity() {
        save_task = null
        val intent = Intent(
            this@PixelEffectPixelActivity,
            FullScreenImageActivity::class.java
        )
        Share.Fragment = "MyPhotosFragment"
        intent.putExtra("avairy", "")
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    override fun onBackPressed() {
        val dialog = Dialog(this@PixelEffectPixelActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.new_exit)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!
            .setLayout(
                (DisplayMetricsHandler.getScreenWidth() - 50),
                Toolbar.LayoutParams.WRAP_CONTENT
            )

        val tv_yes = dialog.findViewById<View>(R.id.btn_yes) as LinearLayout
        val tv_no = dialog.findViewById<View>(R.id.btn_no) as LinearLayout

        tv_no.setOnClickListener { dialog.dismiss() }

        tv_yes.setOnClickListener {
            Share.COLOR_POS = 0
            Share.BG_COLOR = "#FFFFFF"
            if (Share.CROPPED_IMAGE != null) {
                Share.CROPPED_IMAGE = null
                System.gc()
                Runtime.getRuntime().gc()
            }
            if (Share.BG_GALLERY != null) {
                Share.BG_GALLERY = null
                System.gc()
                Runtime.getRuntime().gc()
            }
            finish()
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }
        if (dialog.isShowing) {
            dialog.cancel()
        } else {
            dialog.show()
        }

        System.gc()

        Runtime.getRuntime().gc()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED //                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) //                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Log.e("TAG", "onRequestPermissionsResult: deny")
                } else {
                    Log.e("TAG", "onRequestPermissionsResult: dont ask again")
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for storage")
                        .setPositiveButton(
                            "Cancel"
                        ) { dialog, which -> }
                        .setNegativeButton(
                            "Ok"
                        ) { dialog, which ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
            } else {
                // Permission has already been granted
                if (image_name === "camera") {
                    ImagePicker.pickImage(this@PixelEffectPixelActivity, "Select your image:")
                } else if (image_name === "gallery") {
                    val i = Intent(
                        this@PixelEffectPixelActivity,
                        GalleryPixelActivity::class.java
                    )
                    startActivity(i)
                    //overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (image_name === "my_photos") {

                    /*Intent intent = new Intent(activity, PhotoPickupScrapActivity.class);
                        startActivity(intent);*/
                    // overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                }
            }
            STORAGE_PERMISSION_CODE_CAMERA -> if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    Log.e("TAG", "onRequestPermissionsResult: deny")
                } else {
                    Log.e("TAG", "onRequestPermissionsResult: dont ask again")
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for camera")
                        .setPositiveButton(
                            "Cancel"
                        ) { dialog, which -> }
                        .setNegativeButton(
                            "Ok"
                        ) { dialog, which ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
            } else {

                if (image_name === "camera") {
                    ImagePicker.pickImage(this@PixelEffectPixelActivity, "Select your image:")
                } else if (image_name === "gallery") {
                    val i = Intent(
                        this@PixelEffectPixelActivity,
                        GalleryPixelActivity::class.java
                    )
                    startActivity(i)

                } else if (image_name === "my_photos") {
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val captureUri = ImagePicker.getImageFromResult(
                this@PixelEffectPixelActivity,
                requestCode,
                resultCode,
                data
            )
            if (captureUri != null) {
                Share.BG_GALLERY = null
                Share.BG_GALLERY = captureUri
                val intent = Intent(
                    this@PixelEffectPixelActivity,
                    CropImagePixelActivity::class.java
                )
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Share.CROPPED_IMAGE != null) {
            Share.CROPPED_IMAGE = null
            System.gc()
            Runtime.getRuntime().gc()
        }
        if (Share.BG_GALLERY != null) {
            Share.BG_GALLERY = null
            System.gc()
            Runtime.getRuntime().gc()
        }
        System.gc()
        Runtime.getRuntime().gc()

        isInForeGround = false
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        isInForeGround = false
    }
}