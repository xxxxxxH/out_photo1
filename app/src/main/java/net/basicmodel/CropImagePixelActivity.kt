package net.basicmodel

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import net.event.MessageEvent
import net.fragment.MainFragmentPixel
import net.utils.Share
import org.greenrobot.eventbus.EventBus

class CropImagePixelActivity : AppCompatActivity() {
    var imageUrl: String? = null

    var isInForeGround = false
    var isOpenPermissionDialog = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragmentPixel().getInstance()).commit()
        }
        Log.i("xxxxxxH", "CropImagePixelActivity")
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
                        this@CropImagePixelActivity,
                        permission
                    )
                ) {
                    //denied
                    Log.e("denied", permission)
                    ActivityCompat.requestPermissions(
                        this@CropImagePixelActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        1
                    )
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            this@CropImagePixelActivity,
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
                    if (Share.msg.equals("")) Share.msg =
                        "camera"
                    val alertDialogBuilder = AlertDialog.Builder(this@CropImagePixelActivity)
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

    fun startResultActivity(uri: Bitmap?) {
        try {
            if (uri != null) {
                Log.e("uri", uri.toString() + "")
                getMetrics(resources)
                Share.CROPPED_IMAGE = uri
                EventBus.getDefault().post(MessageEvent("finish2"))
//                if (AlbumImagesPixelActivity.getAlbumImagesActivity1() != null) AlbumImagesPixelActivity.getAlbumImagesActivity1()
//                    .finish()
//                if (GalleryPixelActivity.getGalleryActivity() != null) GalleryPixelActivity.getGalleryActivity()
//                    .finish()
                Log.e("SPLASH_GALLERY_FLAG", Share.SPLASH_GALLERY_FLAG.toString() + "")
                if (Share.SPLASH_GALLERY_FLAG) {
                    Share.SPLASH_GALLERY_FLAG = false
                    val intent = Intent(
                        this@CropImagePixelActivity,
                        PixelEffectPixelActivity::class.java
                    )
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    finish()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                } else {
                    finish()
                }
                //finish();
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMetrics(res: Resources) {
        val metrics = res.displayMetrics
    }

    override fun onDestroy() {
        super.onDestroy()
        isInForeGround = false
    }

    override fun onStop() {
        super.onStop()
        isInForeGround = false
    }
}