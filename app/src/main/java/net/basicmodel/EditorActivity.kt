package net.basicmodel

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_editor.*
import net.database.DBAdapter
import net.utils.Share
import net.widget.ImagePicker
import java.io.File
import java.io.InputStream
import java.util.*

class EditorActivity : AppCompatActivity() {

    var divice_width = 0f
    private val file_mediaFile: File? = null
    var camera_imgs: String? = null

    var databaseInputStream1: InputStream? = null
    val dba: DBAdapter = DBAdapter(this)

    private var image_name = ""

    val STORAGE_PERMISSION_CODE = 23
    val STORAGE_PERMISSION_CODE_CAMERA = 22
    private val REQUEST_SETTINGS_PERMISSION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        initView()
    }

    private fun initView() {
        iv_bokeh.setOnClickListener {
            customDialog()
        }

        iv_color.setOnClickListener {
            selectImageColor()
        }

        iv_shattering.setOnClickListener {
            selectImageShattering()
        }

        iv_pixel.setOnClickListener {
            selectImage()
        }

        iv_pipcamera.setOnClickListener {
            selectImage1()
        }
    }

    //   ---------------------------------bokeh  start----------------------------------------------
    private fun customDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gallerydialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takephoto = dialog.findViewById<View>(R.id.takephoto) as LinearLayout
        val choosephoto = dialog.findViewById<View>(R.id.choosephoto) as LinearLayout
        val cancel = dialog.findViewById<View>(R.id.cancel) as ImageView
        takephoto.setOnClickListener {
            dialog.dismiss()
            cemeraclickBokeh()
        }
        choosephoto.setOnClickListener {
            dialog.dismiss()
            photosclickBokeh()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun cemeraclickBokeh() {
        Share.fromCamera = 1
        Share.isfrom = 1
        image_name = "camerabokeh"
        ImagePicker.pickImage(this, "Select your image:")

    }

    private fun photosclickBokeh() {
        image_name = "gallerybokeh"
        Share.fromCamera = 0
        startActivity(Intent(this@EditorActivity, GalleryActivity::class.java))
        overridePendingTransition(R.anim.right_in, R.anim.left_out)

    }
    //   ---------------------------------bokeh  end------------------------------------------------

    //   ---------------------------------Color  start----------------------------------------------
    fun selectImageColor() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gallerydialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takephoto = dialog.findViewById<View>(R.id.takephoto) as LinearLayout
        val choosephoto = dialog.findViewById<View>(R.id.choosephoto) as LinearLayout
        val cancel = dialog.findViewById<View>(R.id.cancel) as ImageView
        takephoto.setOnClickListener {
            dialog.dismiss()
            cemeraclickColor()
        }
        choosephoto.setOnClickListener {
            dialog.dismiss()
            photosclickColor()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun cemeraclickColor() {
        Share.isfrom = 2
        image_name = "cameracolor"
        ImagePicker.pickImage(this, "Select your image:")
    }

    private fun photosclickColor() {
        image_name = "gallerycolor"
        val i = Intent(this, GalleryActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }
    //   ---------------------------------Color  end------------------------------------------------

    //   ---------------------------------Shattering  start-----------------------------------------
    fun selectImageShattering() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gallerydialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takephoto = dialog.findViewById<View>(R.id.takephoto) as LinearLayout
        val choosephoto = dialog.findViewById<View>(R.id.choosephoto) as LinearLayout
        val cancel = dialog.findViewById<View>(R.id.cancel) as ImageView
        takephoto.setOnClickListener {
            dialog.dismiss()
            cemeraclickShattering()
        }
        choosephoto.setOnClickListener {
            dialog.dismiss()
            photosclickShattering()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun cemeraclickShattering() {
        Share.isfrom = 3
        image_name = "camerashattering"
        ImagePicker.pickImage(this, "Select your image:")

    }

    private fun photosclickShattering() {
        image_name = "galleryshattering"
        val i = Intent(
            this@EditorActivity, FaceActivity::class.java
        )
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)

    }

    //   ---------------------------------Shattering  end-------------------------------------------

    //   ---------------------------------pixel start-----------------------------------------------
    fun selectImage() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gallerydialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takephoto = dialog.findViewById<View>(R.id.takephoto) as LinearLayout
        val choosephoto = dialog.findViewById<View>(R.id.choosephoto) as LinearLayout
        val cancel = dialog.findViewById<View>(R.id.cancel) as ImageView
        takephoto.setOnClickListener {
            dialog.dismiss()
            cemeraclick()
        }
        choosephoto.setOnClickListener {
            dialog.dismiss()
            photosclick()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun photosclick() {
        Share.SPLASH_GALLERY_FLAG = true
        image_name = "gallery"
        startActivity(Intent(this@EditorActivity, GalleryPixelActivity::class.java))
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    private fun cemeraclick() {
        Share.isfrom = 0
        Share.SPLASH_GALLERY_FLAG = true
        image_name = "camera"
        ImagePicker.pickImage(this, "Select your image:")
    }
    //   ---------------------------------pixel end-------------------------------------------------

    //   ---------------------------------pip start-------------------------------------------------
    fun selectImage1() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gallerydialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takephoto = dialog.findViewById<View>(R.id.takephoto) as LinearLayout
        val choosephoto = dialog.findViewById<View>(R.id.choosephoto) as LinearLayout
        val cancel = dialog.findViewById<View>(R.id.cancel) as ImageView
        takephoto.setOnClickListener {
            Share.isfrom = 5
            dialog.dismiss()
            Share.camera_activity_flag = 1
            cemeraclick1()
        }
        choosephoto.setOnClickListener {
            dialog.dismiss()
            photosclick1()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun photosclick1() {
        image_name = "gallerypip"
        val i = Intent(this@EditorActivity, FaceActivitypip::class.java)
        startActivity(i)
    }

    private fun cemeraclick1() {
        Share.isfrom = 5
        image_name = "camerapip"
        ImagePicker.pickImage(this, "Select your image:")

    }
    //   ---------------------------------pip end-------------------------------------------------
}