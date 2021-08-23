package net.basicmodel

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import net.adapter.CustomPagerAdapter
import net.database.DBAdapter
import net.utils.Share
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Copyright (C) 2021,2021/8/19, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class FullScreenImageActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE = 23
    private val REQUEST_SETTINGS_PERMISSION = 102
    private val listPermissionsNeeded: ArrayList<String> = ArrayList()

    var customPagerAdapter: CustomPagerAdapter? = null
    private val is_click = true
    private var al_my_photos = ArrayList<File>()
    private var allFiles: Array<File>? = null
    var dbHelper: DBAdapter? = null
    var b: Bitmap? = null
    var cursor: Cursor? = null
    var data: String? = null
    private val KEY_IMAGE_PATH = "image_path"


    private val al_dummy = ArrayList<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        Log.i("xxxxxxH", "FullScreenImageActivity")
        System.gc()
        Runtime.getRuntime().gc()


        if (checkAndRequestPermissions()) {
            getSHAKey()
            getarray()
            setReffrance()
            setListner()
            //loadFBAdsBanner();
        }
    }

    private fun setListner() {
        iv_close_pager.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }
        iv_delete_pager_items.setOnClickListener {
            try {
                deleteImage(viewpager!!.currentItem)
                dbHelper!!.deleteDrawDetails(al_my_photos[viewpager!!.currentItem].toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        iv_instagram_share.setOnClickListener {
            try {
                if (appInstalledOrNot("com.instagram.android")) {
                    val share = Intent(Intent.ACTION_SEND)
                    share.setPackage("com.instagram.android")
                    share.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.fromFile(al_my_photos[viewpager!!.currentItem])
                    )
                    share.putExtra(
                        Intent.EXTRA_TEXT, """Make more pics with app link 
 https://play.google.com/store/apps/details?id=$packageName"""
                    )
                    share.type = "image/jpeg"
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(share, "Share Picture"))
                } else {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.instagram.android")
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android")
                            )
                        )
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        iv_facebook_share.setOnClickListener {
            try {
                if (appInstalledOrNot("com.facebook.katana")) {
                    val share = Intent(Intent.ACTION_SEND)
                    share.setPackage("com.facebook.katana")
                    share.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.fromFile(al_my_photos[viewpager!!.currentItem])
                    )
                    //            Share.putExtra(Intent.EXTRA_TEXT, "Make more pics with app link \n https://play.google.com/store/apps/details?id=" + FullScreenImageActivity.this.getPackageName());
                    share.type = "image/jpeg"
                    startActivity(Intent.createChooser(share, "Share Picture"))
                } else {
                    val appPackageName = "com.facebook.katana"
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "market://details?id=$appPackageName"
                                )
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "https://play.google.com/store/apps/details?id=$appPackageName"
                                )
                            )
                        )
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        iv_email_share.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.fromFile(al_my_photos[viewpager!!.currentItem])
                )
                intent.putExtra(
                    Intent.EXTRA_TEXT, """Make more pics with app link 
 https://play.google.com/store/apps/details?id=$packageName"""
                )
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(intent, "Share Picture"))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Mail app have not been installed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        iv_whatsup_share.setOnClickListener {
            try {
                if (appInstalledOrNot("com.whatsapp")) {
                    val share = Intent(Intent.ACTION_SEND)
                    share.setPackage("com.whatsapp")
                    share.type = "image/jpeg"
                    share.putExtra(
                        Intent.EXTRA_TEXT, """Make more pics with app link 
 https://play.google.com/store/apps/details?id=$packageName"""
                    )
                    share.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.fromFile(al_my_photos[viewpager!!.currentItem])
                    )
                    startActivity(Intent.createChooser(share, "Select"))
                } else Toast.makeText(
                    applicationContext,
                    "Whatsapp have not been installed",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        iv_share_image.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                intent.type = "image/jpeg"
                intent.putExtra(
                    Intent.EXTRA_TEXT, """Make more pics with app link 
 https://play.google.com/store/apps/details?id=$packageName"""
                )
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.fromFile(al_my_photos[viewpager!!.currentItem])
                )
                startActivity(Intent.createChooser(intent, "Share Picture"))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        iv_fav.setOnClickListener {
            iv_fav.visibility = View.GONE
            iv_unfav.visibility = View.VISIBLE
            handleFav()
        }
        iv_unfav.setOnClickListener {
            iv_fav.visibility = View.VISIBLE
            iv_unfav.visibility = View.GONE
            handleFav()
        }
    }

    fun handleFav() {
        if (iv_fav.visibility == View.VISIBLE) {
            Share.image_path = al_my_photos[viewpager!!.currentItem].toString()
            Share.SELECTED_BITMAP =
                BitmapFactory.decodeFile(al_my_photos[viewpager!!.currentItem].toString())
            // byte[] img = getBitmapAsByteArray(Share.SELECTED_BITMAP);
            val img: ByteArray? = null
            dbHelper!!.saveMessageData(img, Share.image_path)
            val count = dbHelper!!.GetRowCountofTable()
            Log.e("count", "" + count)
        } else {
            Share.image_path = al_my_photos[viewpager!!.currentItem].toString()
            if (Share.Fragment.equals("FavouriteFragment", ignoreCase = true)) {
                if (iv_unfav.visibility == View.VISIBLE) {
                    val builder =
                        AlertDialog.Builder(this@FullScreenImageActivity, R.style.MyAlertDialog)
                    builder.setMessage("Are you sure you want to unfavorite it ?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Yes"
                        ) { dialog, id ->
                            dbHelper!!.deleteDrawDetails(Share.image_path)
                            if (al_my_photos.size > 0) {
                                al_my_photos.removeAt(viewpager!!.currentItem)
                                val current_page = viewpager!!.currentItem
                                if (al_my_photos.size == 0) {
                                    tv_current_page.text = "00" + " / "
                                    onBackPressed()
                                }
                                customPagerAdapter!!.notifyDataSetChanged()
                                viewpager!!.adapter = customPagerAdapter
                                viewpager!!.currentItem = current_page - 1
                                displayMetaInfo(viewpager!!.currentItem)
                            }
                            val data = dbHelper!!.getSingleFavData(Share.image_path)
                            if (Share.image_path.equals(data, ignoreCase = true)) {
                                iv_fav.visibility = View.VISIBLE
                                iv_unfav.visibility = View.GONE
                            } else {
                                iv_fav.visibility = View.GONE
                                iv_unfav.visibility = View.VISIBLE
                            }
                            val row = dbHelper!!.GetRowCountofTable()
                            if (Share.Fragment.equals("MyPhotosFragment", ignoreCase = true)) {
                            } else {
                                if (row == 0L) {
                                    finish()
                                }
                            }
                            Log.e("dbhelper", "" + row)
                        }
                        .setNegativeButton(
                            "No"
                        ) { dialog, id ->
                            val data = dbHelper!!.getSingleFavData(Share.image_path)
                            if (Share.image_path.equals(data, ignoreCase = true)) {
                                iv_fav.visibility = View.VISIBLE
                                iv_unfav.visibility = View.GONE
                            } else {
                                iv_fav.visibility = View.GONE
                                iv_unfav.visibility = View.VISIBLE
                            }
                            val row = dbHelper!!.GetRowCountofTable()
                            Log.e("dbhelper", "" + row)
                        }
                    val alert = builder.create()
                    alert.show()
                }
            } else {
                if (iv_unfav.visibility == View.VISIBLE) {
                    dbHelper!!.deleteDrawDetails(Share.image_path)
                    val data = dbHelper!!.getSingleFavData(Share.image_path)
                    if (Share.image_path.equals(data, ignoreCase = true)) {
                        iv_fav.visibility = View.VISIBLE
                        iv_unfav.visibility = View.GONE
                    } else {
                        iv_fav.visibility = View.GONE
                        iv_unfav.visibility = View.VISIBLE
                    }
                    val row = dbHelper!!.GetRowCountofTable()
                    if (Share.Fragment.equals("MyPhotosFragment", ignoreCase = true)) {
                    } else {
                        if (row == 0L) {
                            finish()
                        }
                    }
                    Log.e("dbhelper", "" + row)
                }
            }
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    private fun deleteImage(position: Int) {
        val builder = AlertDialog.Builder(this@FullScreenImageActivity, R.style.MyAlertDialog)
        //builder.setMessage(getResources().getString(R.string.delete_emoji_msg));
        builder.setMessage("Are you sure want to delete photo ?")
        builder.setPositiveButton("Yes") { dialog, which ->
            Log.e("TAG", "al_my_photos.size():" + al_my_photos.size)
            if (al_my_photos.size > 0) {
                val isDeleted = al_my_photos[position].delete()
                Log.e("TAG", "isDeleted:$isDeleted")
                if (isDeleted) {
                    if (al_my_photos[viewpager!!.currentItem].exists()) {
                        al_my_photos[viewpager!!.currentItem].delete()
                        //
                    }
                    val f = File(al_my_photos[viewpager!!.currentItem].toString())
                    if (f.exists()) {
                        Log.e("TAG", "img:$f")
                        f.delete()
                    }
                    al_my_photos.removeAt(position)
                    if (al_my_photos.size == 0) {
                        onBackPressed()
                    }
                    customPagerAdapter!!.notifyDataSetChanged()
                    viewpager!!.adapter = customPagerAdapter
                    viewpager!!.currentItem = position - 1
                    displayMetaInfo(viewpager!!.currentItem)
                } else {
                }
            }
            dialog.cancel()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
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

    private fun getSHAKey() {
        dbHelper = DBAdapter(this@FullScreenImageActivity)
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    private fun getarray() {
        if (Share.Fragment.equals("MyPhotosFragment", ignoreCase = true)) {
            setData()
        } else {
            fill_Array()
        }
    }

    private fun setReffrance() {
        customPagerAdapter = CustomPagerAdapter(this, al_my_photos)
        val height = (applicationContext.resources.displayMetrics.heightPixels * 0.11).toInt()
        ll_pager_indicator.layoutParams.height = height
        favorite_layout.layoutParams.height = height
        viewpager!!.adapter = customPagerAdapter
        if (intent.extras != null && intent.extras!!.containsKey("avairy")) {
            viewpager!!.currentItem = 0
        } else {
            if (Share.Fragment.equals("MyPhotosFragment", ignoreCase = true)) {
                viewpager!!.currentItem = Share.my_photos_position
            } else {
                viewpager!!.currentItem = Share.my_favourite_position
            }
            Log.e("TAG", "Share.my_photos_position=>" + Share.my_photos_position)
        }

        val bottomUp = AnimationUtils.loadAnimation(
            this@FullScreenImageActivity,
            R.anim.bottom_up
        )

        //ll_pager_indicator.startAnimation(bottomUp);

//        tv_total_page.setText(String.valueOf(al_my_photos.size()));
        if (al_my_photos.size < 10) {
            tv_total_page.text = "0" + al_my_photos.size.toString()
        } else {
            tv_total_page.text = (al_my_photos.size + 1).toString()
        }

        viewpager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                displayMetaInfo(position)

                check_like_data()
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        if (Share.Fragment.equals("MyPhotosFragment", ignoreCase = true)) {
            iv_delete_pager_items.isEnabled = true
        } else {
            iv_delete_pager_items.alpha = 0.5f
            iv_delete_pager_items.isEnabled = false
        }
    }

    private fun displayMetaInfo(position: Int) {
        if (position < 9) {
            tv_current_page.text = "0" + (position + 1) + " / "
        } else {
            tv_current_page.text = (position + 1).toString() + " / "
        }
        Log.e("TAG", "al_my phots==>" + al_my_photos.size)
        if (al_my_photos.size < 10) {
            tv_total_page.text = "0" + al_my_photos.size.toString()
        } else {
            tv_total_page.text = al_my_photos.size.toString()
        }
    }

    private fun check_like_data() {
        try {
            Share.image_path = al_my_photos[viewpager!!.currentItem].toString()
            val data = dbHelper!!.getSingleFavData(Share.image_path)
            if (Share.image_path.equals(data, ignoreCase = true)) {
                //  iv_fav.setChecked(true);
                iv_fav.visibility = View.VISIBLE
                iv_unfav.visibility = View.GONE
            } else {
                //iv_fav.setChecked(false);
                iv_fav.visibility = View.GONE
                iv_unfav.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData() {
        val path = File(Share.IMAGE_PATH)
        Log.e("TAG", "PATH ===>$path")
        if (path.exists()) {
            allFiles = path.listFiles { dir, name ->
                name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(
                    ".png"
                )
            }
        }
        if (allFiles != null) {
            if (allFiles!!.size > 0) {
                for (i in allFiles!!.indices) {
                    /*  Bitmap mBitmap = BitmapFactory.decodeFile(String.valueOf(allFiles[i]));*/
                    al_my_photos.add(allFiles!![i])
                }
                Collections.sort(al_my_photos, Collections.reverseOrder())
            }
        }
    }

    private fun fill_Array() {
        al_my_photos.clear()
        al_my_photos = ArrayList()
        cursor = dbHelper!!.favData
        if (!cursor!!.moveToFirst() || cursor!!.count == 0) {
        } else {
            if (cursor!!.moveToFirst()) {
                do {
                    data =
                        cursor!!.getString(cursor!!.getColumnIndex(KEY_IMAGE_PATH))
                    Log.e(
                        "PATH------",
                        cursor!!.getString(cursor!!.getColumnIndex(KEY_IMAGE_PATH)) + ""
                    )
                    val path = File(data).absoluteFile
                    if (!path.exists()) {
                        try {
                            path.createNewFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    al_dummy.add(path)
                } while (cursor!!.moveToNext())
            }
            for (i in al_dummy.indices.reversed()) {
                al_my_photos.add(al_dummy[i])
            }
        }
    }
}