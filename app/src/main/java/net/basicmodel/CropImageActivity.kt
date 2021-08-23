package net.basicmodel

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import net.event.MessageEvent
import net.fragment.MainFragment
import net.utils.Share
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*

class CropImageActivity:AppCompatActivity() {

    var imageUrl: String? = null
    private val STORAGE_PERMISSION_CODE = 23
    private val REQUEST_SETTINGS_PERMISSION = 102
    private val listPermissionsNeeded: List<String> = ArrayList()

    var isOpenPermissionDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)
        //if (Share.RestartApp(this)) {


        //loadFBAdsBanner();
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment().getInstance()).commit()
        }
        Log.i("xxxxxxH", "CropImageActivity")
    }

    fun startResultActivity(uri: Bitmap?) {
        EventBus.getDefault().post(MessageEvent("finish1"))
//        if (GalleryActivity.getGalleryActivity() != null) {
//            GalleryActivity.getGalleryActivity().finish()
//        }
//        if (AlbumImagesActivity.albumImagesActivity != null) {
//            AlbumImagesActivity.albumImagesActivity.finish()
//        }
        val cropfile: String = Share.saveFaceInternalStorage(this, uri)
        val externalFile: File = File(Share.saveFaceInternalStorage(this, uri))
        val external = Uri.fromFile(externalFile)
        val i = Intent(
            this@CropImageActivity,
            BokehEffectActivity::class.java
        )
        i.putExtra("cropfile", cropfile)
        Log.e("TAG", "startResultActivity:----> $cropfile")
        Log.e("TAG", "startResultActivity uri:----> $external")
        startActivity(i)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isEmpty()) {
            return
        }
        var allPermissionsGranted = true
        if (grantResults.isNotEmpty()) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
        }

        if (!allPermissionsGranted) {
            var somePermissionsForeverDenied = false
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@CropImageActivity,
                        permission
                    )
                ) {
                    //denied
                    Log.e("denied", permission)
                    ActivityCompat.requestPermissions(
                        this@CropImageActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        1
                    )
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            this@CropImageActivity,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //allowed
                        Log.e("allowed", "permission ====>$permission")
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission)
                        somePermissionsForeverDenied = true
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                if (!isOpenPermissionDialog) {
                    if (Share.msg.equals("")) Share.msg = "camera"
                    val alertDialogBuilder = AlertDialog.Builder(this@CropImageActivity)
                    alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for " + Share.msg)
                        .setNegativeButton("Ok") { dialog, which -> // dialog.dismiss();
                            dialog.dismiss()
                            isOpenPermissionDialog = false
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .setPositiveButton(
                            "Cancel"
                        ) { dialog, which -> isOpenPermissionDialog = false }
                        .setCancelable(false)
                        .create()
                        .show()
                    isOpenPermissionDialog = true
                }
            }
        } else {
            when (requestCode) {
                1 -> Log.e("TAG", "Permission Allowed")
                else -> {
                }
            }
        }
    }
}