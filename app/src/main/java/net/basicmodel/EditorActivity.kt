package net.basicmodel

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_editor.*
import net.database.DBAdapter
import net.database.ImportDatabase
import net.utils.Share
import net.widget.ImagePicker
import java.io.File
import java.io.InputStream
import java.util.*

class EditorActivity : AppCompatActivity() {

    object attrs {
        var divice_width = 0f
        var camera_imgs: String? = null
        var databaseInputStream1: InputStream? = null
        fun setdivice_width() {

        }

        fun setcamera_imgs() {

        }

        fun setdatabaseInputStream1() {

        }
    }


    private val file_mediaFile: File? = null


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

        GetDevicewidtgh()
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

    private fun GetDevicewidtgh() {
        main_layout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    main_layout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    main_layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

                //main_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                main_layout.height //height is ready
                main_layout.width
                val devicewidth = main_layout.width
                main_layout.x
                main_layout.y
                attrs.divice_width = main_layout.width.toFloat()
                Log.e("devicewidth", "" + devicewidth + "   *   " + main_layout.height)
                Log.e("devicewidth_X", "" + main_layout.x)
                Log.e("devicewidth_Y", "" + main_layout.y)
                Log.e("tree", "1")
            }
        })
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
                if (image_name === "gallerypip") {
                    val i = Intent(this, FaceActivitypip::class.java)
                    startActivity(i)
                } else if (image_name === "camerapip") {
                    Log.e(
                        "TAG",
                        "onRequestPermissionsResult:--> " + Share.isfrom
                    )
                    Share.isfrom = 5
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                } else if (image_name === "gallery") {
                    val i = Intent(this, GalleryPixelActivity::class.java)
                    startActivity(i)
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                }
                // Permission has already been granted
                if (image_name === "camerabokeh") {
                    Share.fromCamera = 1
                    Share.isfrom = 1
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                } else if (image_name === "gallerybokeh") {
                    Share.fromCamera = 0
                    val i = Intent(this, GalleryActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else if (image_name === "cameracolor") {
                    Share.isfrom = 2
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                } else if (image_name === "gallerycolor") {
                    val i = Intent(this, GalleryActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else if (image_name === "galleryshattering") {
                    val i = Intent(
                        this@EditorActivity,
                        FaceActivity::class.java
                    )
                    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(i)
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
                if (image_name === "gallerypip") {
                    val i = Intent(this, FaceActivitypip::class.java)
                    startActivity(i)
                } else if (image_name === "camerapip") {
                    Share.isfrom = 5
                    Log.e(
                        "TAG",
                        "onRequestPermissionsResult:--> " + Share.isfrom
                    )
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                }
                if (image_name === "camera") {
                    Share.isfrom = 0
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                } else if (image_name === "gallery") {
                    val i = Intent(this, GalleryPixelActivity::class.java)
                    startActivity(i)
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                }
                if (image_name === "camerabokeh") {
                    Share.fromCamera = 1
                    Share.isfrom = 1
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                } else if (image_name === "gallerybokeh") {
                    Share.fromCamera = 0
                    val i = Intent(this, GalleryActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else if (image_name === "cameracolor") {
                    Share.isfrom = 2
                    ImagePicker.pickImage(this@EditorActivity, "Select your image:")
                    //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward
                } else if (image_name === "gallerycolor") {
                    val i = Intent(this, GalleryActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                } else if (image_name === "galleryshattering") {
                    val i = Intent(
                        this@EditorActivity,
                        FaceActivity::class.java
                    )
                    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(i)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val uri = ImagePicker.getImageFromResult(this, requestCode, resultCode, data)
            Log.e("TAG", "Camera uri==>$uri")
            if (Share.isfrom == 5) {
                if (uri != null) Log.e("TAG", "captureUri  pip " + uri.path)
                if (uri != null) {
                    val file = File(uri.path)
                    if (file.exists()) {
                        Share.imageUrl = file.absolutePath
                        val intent = Intent(
                            this,
                            CropImageActivitypip::class.java
                        )
                        intent.putExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE, file.absolutePath)
                        startActivity(intent)
                    }
                }
            }
            if (Share.isfrom === 0) {
                if (uri != null) {
                    Share.BG_GALLERY = null
                    Share.SPLASH_GALLERY_FLAG = true
                    Share.BG_GALLERY = uri
                    val intent: Intent = Intent(
                        this,
                        CropImagePixelActivity::class.java
                    )
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                }
            } else if (Share.isfrom == 1) {
                if (uri != null) {
                    Share.BG_GALLERY = null
                    Share.BG_GALLERY = uri
                    Share.fromCamera = 1
                    Log.e(
                        "TAG",
                        "onActivityResult:- Share.fromCamera-->" + Share.fromCamera
                    )
                    val intent: Intent = Intent(
                        this,
                        CropImageActivity::class.java
                    )
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                }
            } else if (Share.isfrom == 2) {
                Share.BG_GALLERY = null
                Share.BG_GALLERY = uri
                val intent: Intent = Intent(this, CropImageActivity::class.java)
                intent.putExtra("isFromGallery", false)
                startActivity(intent)
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            } else if (Share.isfrom == 3) {
                if (uri != null) Log.e("TAG", "captureUri  $uri")
                if (uri != null) {
                    val file = File(uri.path)
                    if (file.exists()) {
                        Share.imageUrl = file.absolutePath
                        val intent = Intent(
                            this,
                            CropImageActivity::class.java
                        )
                        intent.putExtra(
                            Share.KEYNAME.SELECTED_PHONE_IMAGE,
                            file.absolutePath
                        )
                        //intent.putExtra(GlobalData.KEYNAME.SELECTED_PHONE_IMAGE, captureUri);
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }
        }
    }

    inner class CopyDB :
        AsyncTask<String?, Void?, Boolean>() {
        @SuppressLint("SdCardPath")
        override fun onPreExecute() {
            try {
                val f = File("/data/data/$packageName/databases/Suitdb.sql")
                Log.e("File of Local DataBase", "f  : $f")
                if (f.exists()) {
                } else {
                    try {
                        dba.open()
                        dba.close()
                        println("Database is copying.....")
                        databaseInputStream1 = assets.open("Suitdb.sql")
                        ImportDatabase.copyDataBase()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: String?): Boolean {
            var success = false
            try {
                success = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return success
        }
    }

    override fun onResume() {
        super.onResume()
        CopyDB().execute("")
    }
}