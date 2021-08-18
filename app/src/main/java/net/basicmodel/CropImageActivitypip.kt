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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.event.MessageEvent
import net.fragment.MainFragmentpip
import net.utils.Share
import org.greenrobot.eventbus.EventBus
import java.io.File

class CropImageActivitypip:AppCompatActivity() {
    var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        imageUrl = if (intent.hasExtra(Share.KEYNAME.SELECTED_IMAGE)) {
            intent.extras!!.getString(Share.KEYNAME.SELECTED_IMAGE)
        } else {
            intent.getStringExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE)
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragmentpip().getInstance()).commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
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
                    .setNegativeButton("Ok") { dialog, which ->
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
        }
    }

    fun startResultActivity(uri: Bitmap?) {
//        if (AlbumImagesActivitpip.activity != null) AlbumImagesActivitpip.finish()
//        if (FaceActivitypip != null) FaceActivitypip.finish()
        EventBus.getDefault().post(MessageEvent("finish"))
        val cropfile: String = Share.saveFaceInternalStorage(this, uri)
        jump(cropfile)
    }

    private fun jump(cropfile: String) {

        //Share.imgs = imageUrl;
        Log.e("Crop Activity--->", "cropfile$cropfile")
        val file = File(cropfile)
        if (file.exists()) {
            Log.e("DAG", "Share.Flag_First " + Share.Flag_First)
            if (Share.Flag_First) {
                val i1 = Intent(
                    this@CropImageActivitypip,
                    PhotoDisplayPipActivity::class.java
                )
                startActivity(i1)
                Share.Flag_First = false
                finish()
                if (Share.Activity_Gallery_View != null) {
                    Share.Activity_Gallery_View.finish()
                }


                //add();
            } else {
                finish()
                if (Share.Activity_Gallery_View != null) {
                    Share.Activity_Gallery_View.finish()
                }
            }
            Share.imgs = cropfile
            Log.e("DAG", "IMG//////Share.imgs ---- " + Share.imgs)

            // make something with the name
        } else {
            Toast.makeText(this@CropImageActivitypip, "file not exist", Toast.LENGTH_SHORT).show()
        }
    }
}