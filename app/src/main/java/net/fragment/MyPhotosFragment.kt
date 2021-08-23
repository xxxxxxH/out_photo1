package net.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_my_photo.*
import net.adapter.MyPhotosAdapter
import net.basicmodel.FullScreenImageActivity
import net.basicmodel.MyPhotosActivity
import net.basicmodel.R
import net.database.DBAdapter
import net.utils.Share
import java.io.File
import java.util.*

/**
 * Copyright (C) 2021,2021/8/20, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class MyPhotosFragment : Fragment() {
    private var gridLayoutManager: GridLayoutManager? = null
    private var myPhotosAdapter: MyPhotosAdapter? = null
    private val al_my_photos = ArrayList<File>()
    private var allFiles: Array<File>? = null

    var dbAdapter: DBAdapter? = null

    //    ImageView iv_all_delete;
    var posi = 0
    var iv_all_delete: ImageView? = null
    private val listPermissionsNeeded: ArrayList<String> = ArrayList()

    fun newInstance(): MyPhotosFragment {
        val args = Bundle()
        val fragment = MyPhotosFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_all_delete = (activity as MyPhotosActivity).findViewById(R.id.iv_all_delete) as ImageView
        if (checkAndRequestPermissions()) {
            dbAdapter = DBAdapter(activity)
            gridLayoutManager = GridLayoutManager(activity, 2)
            rcv_images.layoutManager = gridLayoutManager
            Share.Fragment = "MyPhotosFragment"
            Log.e("fragment", "oncreate")
        }
    }

    private fun setData() {
        val path = File(Share.IMAGE_PATH)
        al_my_photos.clear()
        Share.al_my_photos_photo.clear()
        allFiles = null
        if (path.exists()) {
            Log.e("if1", "if1")
            allFiles = path.listFiles { dir, name ->
                name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(
                    ".png"
                )
            }
            Log.e("array_size", "" + allFiles!!.size)
            if (allFiles!!.size > 0) {
                Log.e("if2", "if2")
                rl_my_photos.visibility = View.VISIBLE
                ll_no_photos.visibility = View.GONE
                iv_all_delete!!.alpha = 1f
                iv_all_delete!!.isEnabled = true
                for (i in allFiles!!.indices) {
                    al_my_photos.add(allFiles!![i])
                    ///Now set this bitmap on imageview
                }
                Share.al_my_photos_favourite.clear()
                Collections.sort(al_my_photos, Collections.reverseOrder())
                Share.al_my_photos_photo.addAll(al_my_photos)
                // myPhotosAdapter = new MyPhotosAdapter(getActivity(), Share.al_my_photos_photo);
                myPhotosAdapter = MyPhotosAdapter(
                    activity,
                    Share.al_my_photos_photo
                ) { view, position ->
                    posi = position
                    go_on_fullscreenimage()
                }
                rcv_images.adapter = myPhotosAdapter
            } else {
                Log.e("else2", "else2")
                al_my_photos.clear()
                iv_all_delete!!.alpha = 0.5f
                iv_all_delete!!.isEnabled = false
                rl_my_photos.visibility = View.GONE
                ll_no_photos.visibility = View.VISIBLE
            }
        } else {
            Log.e("else1", "else1")
            al_my_photos.clear()
            iv_all_delete!!.alpha = 0.5f
            iv_all_delete!!.isEnabled = false
            rl_my_photos.visibility = View.GONE
            ll_no_photos.visibility = View.VISIBLE
        }
    }

private fun onclick(){
    iv_all_delete!!.setOnClickListener {
        val builder = androidx.appcompat.app.AlertDialog.Builder(
            requireActivity(), R.style.MyAlertDialog
        )
        //builder.setMessage(getResources().getString(R.string.delete_emoji_msg));
        //builder.setMessage(getResources().getString(R.string.delete_emoji_msg));
        builder.setMessage("Are you sure want to delete all photos ?")
        builder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            for (i in al_my_photos.indices) {
                val f = File(al_my_photos[i].toString())
                Log.e(
                    "images file 12345 :  ",
                    " ==============" + al_my_photos[i] + "  -----------" + f.toString()
                )
                f.delete()
                al_my_photos[i].delete()
                //al_my_photos.remove(i);
            }
            al_my_photos.clear()
            dbAdapter!!.deleteFavData()
            Log.e("count", dbAdapter!!.GetRowCountofTable().toString() + "")
            val count = dbAdapter!!.GetRowCountofTable()
            if (count == 0L) {
                Log.e(
                    "count1",
                    dbAdapter!!.GetRowCountofTable().toString() + ""
                )
                iv_all_delete!!.alpha = 0.5f
                iv_all_delete!!.isEnabled = false
                rl_my_photos.visibility = View.GONE
                ll_no_photos.visibility = View.VISIBLE
            }
            if (al_my_photos.size == 0) {
                iv_all_delete!!.alpha = 0.5f
                iv_all_delete!!.isEnabled = false
                rl_my_photos.visibility = View.GONE
                ll_no_photos.visibility = View.VISIBLE
            }
            setData()
            myPhotosAdapter!!.notifyDataSetChanged()
            al_my_photos.clear()
            dialog.cancel()
        }

        builder.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }
}
    private fun go_on_fullscreenimage() {
        nextActivity()
    }

    private fun nextActivity() {
        val intent = Intent(
            activity,
            FullScreenImageActivity::class.java
        )
        Share.Fragment = "MyPhotosFragment"
        Share.my_photos_position = posi
        requireActivity().startActivity(intent)
        val activity = activity as Activity?
        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
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
                    .setMessage("Please allow permission for storage")
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
            Share.Fragment = "MyPhotosFragment"
            setData()
            Log.e("fragment", "onresume")
        }
    }
}