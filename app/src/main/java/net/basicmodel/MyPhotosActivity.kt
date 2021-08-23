package net.basicmodel

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_my_photos.*
import net.fragment.FavouriteFragment
import net.fragment.MyPhotosFragment
import net.utils.Share
import java.io.File
import java.util.*

class MyPhotosActivity : AppCompatActivity() {


    var al_my_photos = ArrayList<File>()
    private var allFiles: Array<File>? = null


    private val STORAGE_PERMISSION_CODE = 23
    private val listPermissionsNeeded: ArrayList<String> = ArrayList()
    var Fragment = ""
    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_photos)
        Log.i("xxxxxxH", "MyPhotosActivity")
        if (checkAndRequestPermissions()) {
            setListner()
            setData()
            updateFragment(MyPhotosFragment().newInstance())
        }
    }

    private fun updateFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.simpleFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setData() {
        val path: File = File(Share.IMAGE_PATH)
        al_my_photos.clear()

        if (path.exists()) {
            allFiles = path.listFiles { dir, name ->
                name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(
                    ".png"
                )
            }
            if (allFiles!!.size > 0) {
                iv_all_delete.alpha = 1f
                iv_all_delete.isEnabled = true
                for (i in allFiles!!.indices) {
                    val mBitmap = BitmapFactory.decodeFile(allFiles!!.get(i).toString())
                    al_my_photos.add(allFiles!!.get(i))
                }
                Collections.sort(al_my_photos, Collections.reverseOrder<File>())
            } else {
                iv_all_delete.alpha = 0.5f
                iv_all_delete.isEnabled = false
            }
        } else {
            iv_all_delete.alpha = 0.5f
            iv_all_delete.isEnabled = false
        }
    }

    private fun setListner() {
        tvAll.setOnClickListener {
            System.gc()
            Runtime.getRuntime().gc()
            tvAll.setTextColor(resources.getColor(R.color.white))
            tvAll.setBackgroundResource(R.drawable.back_txt_left_selected)

            tvFav.setTextColor(resources.getColor(R.color.black))
            tvFav.setBackgroundResource(R.drawable.back_txt_right_unselected)
            fragment = MyPhotosFragment().newInstance()
            updateFragment(fragment!!)
        }
        tvFav.setOnClickListener {
            System.gc()
            Runtime.getRuntime().gc()
            tvFav.setTextColor(resources.getColor(R.color.white))
            tvFav.setBackgroundResource(R.drawable.back_txt_right_selected)

            tvAll.setTextColor(resources.getColor(R.color.black))
            tvAll.setBackgroundResource(R.drawable.back_txt_left_unselected)
            fragment = FavouriteFragment().newInstance()
            updateFragment(fragment!!)
        }

        iv_back_my_photos.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkAndRequestPermissions()) {
            setData()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        listPermissionsNeeded.clear()
        val writeStorage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readStorage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        Log.e("TAG", "listPermissionsNeeded===>$listPermissionsNeeded")
        return listPermissionsNeeded.isEmpty()
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
                    .setMessage("Please allow permission for Storage")
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
        } else {

            //saveImage();
        }
    }
}