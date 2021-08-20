package net.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_my_photos.*
import kotlinx.android.synthetic.main.fragment_favourite.*
import net.adapter.MyFavouriteAdapter
import net.basicmodel.FullScreenImageActivity
import net.basicmodel.R
import net.database.DBAdapter
import net.utils.Share
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Copyright (C) 2021,2021/8/20, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class FavouriteFragment : Fragment() {

    private var gridLayoutManager: GridLayoutManager? = null
    private var myFavouriteAdapter: MyFavouriteAdapter? = null
    private val al_my_photos = ArrayList<File>()
    private val al_dummy = ArrayList<File>()
    private val allFiles: Array<File>? = null
    var dbAdapter: DBAdapter? = null
    var cursor: Cursor? = null
    var data: String? = null
    private val KEY_IMAGE_PATH = "image_path"
    private val listPermissionsNeeded: ArrayList<String> = ArrayList()
    var posi = 0

    fun newInstance(): FavouriteFragment {
        val args = Bundle()
        val fragment = FavouriteFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkAndRequestPermissions()) {
            dbAdapter = DBAdapter(activity)
            gridLayoutManager = GridLayoutManager(activity, 2)
            rcv_images.layoutManager = gridLayoutManager
            rcv_images.adapter = myFavouriteAdapter
            Share.Fragment = "FavouriteFragment"
            onclick()
        }

    }

    private fun checkAndRequestPermissions(): Boolean {
        listPermissionsNeeded.clear()
        val writeStorage =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val readStorage =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
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
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Log.e("TAG", "onRequestPermissionsResult: deny")
            } else {
                Log.e("TAG", "onRequestPermissionsResult: dont ask again")
                val alertDialogBuilder = AlertDialog.Builder(
                    context
                )
                alertDialogBuilder.setTitle("Permissions Required")
                    .setMessage("Please allow permission for Storage")
                    .setPositiveButton(
                        "Cancel"
                    ) { dialog, which -> }
                    .setNegativeButton("Ok") { dialog, which ->
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", requireActivity().packageName, null)
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

    override fun onResume() {
        super.onResume()
        if (checkAndRequestPermissions()) {
            Share.Fragment = "FavouriteFragment"
            fill_Array()
        }
    }

    private fun fill_Array() {
        al_dummy.clear()
        al_my_photos.clear()
        Share.al_my_photos_favourite.clear()
        cursor = dbAdapter!!.favData
        Log.e("PATH------", cursor!!.count.toString() + "fgbuh")
        if (cursor!!.count == 0) {
            iv_all_delete.alpha = 0.5f
            iv_all_delete.isEnabled = false
            rl_my_photos.visibility = View.GONE
            ll_no_photos.visibility = View.VISIBLE
        } else {
            if (cursor!!.moveToFirst()) {
                do {
                    data = cursor!!.getString(cursor!!.getColumnIndex(KEY_IMAGE_PATH))
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
                iv_all_delete.alpha = 1f
                iv_all_delete.isEnabled = true
                rl_my_photos.visibility = View.VISIBLE
                ll_no_photos.visibility = View.GONE
            }
        }
        for (i in al_dummy.indices.reversed()) {
            Log.e("TAG", "i1 = $i")
            al_my_photos.add(al_dummy[i])
        }
        Share.al_my_photos_photo.clear()
        Share.al_my_photos_favourite.addAll(al_my_photos)
        //myFavouriteAdapter = new MyFavouriteAdapter(getActivity(), al_my_photos);
        myFavouriteAdapter =
            MyFavouriteAdapter(
                activity, al_my_photos
            ) { view, position ->
                posi = position
                go_on_fullscreenimage()
            }
        rcv_images.adapter = myFavouriteAdapter
    }

    private fun go_on_fullscreenimage() {
        nextActivity()
    }

    private fun nextActivity() {
        val intent = Intent(
            activity,
            FullScreenImageActivity::class.java
        )
        Share.Fragment = "FavouriteFragment"
        Share.my_favourite_position = posi
        requireActivity().startActivity(intent)
        val activity = activity as Activity?
        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    private fun onclick() {
        iv_all_delete.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(
                requireActivity(), R.style.MyAlertDialog
            )
            builder.setMessage("Are you sure want to delete all Favourite photos ?")

            builder.setPositiveButton(
                "Yes"
            ) { dialog, which ->
                dbAdapter!!.deleteFavData()
                Log.e("count", dbAdapter!!.GetRowCountofTable().toString() + "")
                val count = dbAdapter!!.GetRowCountofTable()
                if (count == 0L) {
                    Log.e(
                        "count1",
                        dbAdapter!!.GetRowCountofTable().toString() + ""
                    )
                    iv_all_delete.alpha = 0.5f
                    iv_all_delete.isEnabled = false
                    rl_my_photos.visibility = View.GONE
                    ll_no_photos.visibility = View.VISIBLE
                }
                dialog.cancel()
            }
            builder.setNegativeButton(
                "No"
            ) { dialog, which -> dialog.cancel() }
            builder.show()
        }
    }
}