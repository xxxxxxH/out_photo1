package net.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import com.isseiaoki.simplecropview.util.Utils
import kotlinx.android.synthetic.main.fragment_main.*
import net.basicmodel.CropImagePixelActivity
import net.basicmodel.EditorActivity
import net.basicmodel.R
import net.utils.Share
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import java.io.File
import java.util.*

class MainFragmentPixel:Fragment() {
    private val REQUEST_PICK_IMAGE = 10011
    private val REQUEST_SAF_PICK_IMAGE = 10012
    private val PROGRESS_DIALOG = "ProgressDialog"

    val STORAGE_PERMISSION_CODE = 23

    private val listPermissionsNeeded: ArrayList<String> = ArrayList()
    fun getInstance(): MainFragmentPixel {
        val fragment = MainFragmentPixel()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        // cropImageView.setDebug(true);
        // set bitmap to CropImageView
        if (cropImageView.imageBitmap == null) {
            if (Share.BG_GALLERY != null && !Share.BG_GALLERY.equals("")) {
                Log.e("TAG", "MainFragmentPixel:==>" + Share.BG_GALLERY)
                Glide.with(this)
                    .load(Share.BG_GALLERY)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(300, 300)
                    .into(cropImageView)
            }
            cropImageView.setCropMode(CropImageView.CropMode.FREE)
        }
        onclikc()
    }

    override fun onResume() {
        super.onResume()
        if (cropImageView.getImageBitmap() == null) {
            if (Share.BG_GALLERY != null && !Share.BG_GALLERY.equals("")) {
                Glide.with(this)
                    .load(Share.BG_GALLERY)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(300, 300)
                    .into(cropImageView)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        cropImageView.setImageDrawable(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            showProgress()
            cropImageView.startLoad(data?.data, mLoadCallback)
        } else if (requestCode == REQUEST_SAF_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            showProgress()
            cropImageView.startLoad(Utils.ensureUriPermission(context, data), mLoadCallback)
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        listPermissionsNeeded.clear()
        val storage = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readStorage =
            ContextCompat.checkSelfPermission( requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
        val camera = ContextCompat.checkSelfPermission( requireActivity(), Manifest.permission.CAMERA)
        if (storage != PackageManager.PERMISSION_GRANTED) {
            Share.msg = "storage."
           listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
           Share.msg = "storage."
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            if (Share.msg == null) {
                Share.msg = "camera."
            } else {
                Share.msg += ""
            }
           listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        return listPermissionsNeeded.isEmpty()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> if (ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    requireActivity(),
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
                        .setMessage("Please allow permission for camera")
                        .setPositiveButton(
                            "Cancel"
                        ) { dialog, which -> }
                        .setNegativeButton(
                            "Ok"
                        ) { dialog, which ->
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
            } else {
                cropImage()
            }
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),REQUEST_PICK_IMAGE
            )
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE)
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun cropImage() {
        showProgress()
        cropImageView.startCrop(createSaveUri(), mCropCallback, mSaveCallback)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForPick(request: PermissionRequest?) {
        if (request != null) {
            showRationaleDialog(R.string.permission_pick_rationale, request)
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForCrop(request: PermissionRequest?) {
        if (request != null) {
            showRationaleDialog(R.string.permission_crop_rationale, request)
        }
    }
    fun createSaveUri(): Uri? {
        return Uri.fromFile(File(requireActivity().cacheDir, "cropped"))
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        androidx.appcompat.app.AlertDialog.Builder(requireActivity())
            .setPositiveButton(
                R.string.button_allow
            ) { dialog, which -> request.proceed() }
            .setNegativeButton(
                R.string.button_deny
            ) { dialog, which -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }
    private fun onclikc(){
        buttonDone.setOnClickListener {
            if (checkAndRequestPermissions()) {
                cropImage()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    listPermissionsNeeded.toTypedArray<String>(),
                    STORAGE_PERMISSION_CODE
                )
            }
        }
        buttonFitImage.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE)
            buttonFitImage.setTextColor(resources.getColor(R.color.yellowcolor))

            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        button1_1.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.SQUARE)
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )
            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        button3_4.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4)
            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        button4_3.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3)
            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        button9_16.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16)
            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))

        }
        button16_9.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        buttonCustom.setOnClickListener {
            cropImageView.setCustomRatio(7, 5)
            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
            buttonFree.setTextColor(resources.getColor(R.color.greycolor))
        }
        buttonFree.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.FREE)

            buttonFree.setTextColor(resources.getColor(R.color.yellowcolor))

            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
        }
        buttonCircle.setOnClickListener {
            cropImageView.setCropMode(CropImageView.CropMode.CIRCLE)
            imgcircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.yellowcolor),
                PorterDuff.Mode.SRC_IN
            )

            buttonFree.setTextColor(resources.getColor(R.color.greycolor))

            img77.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            img169.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img916.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img43.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )

            img34.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            imgsquare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.greycolor),
                PorterDuff.Mode.SRC_IN
            )
            buttonFitImage.setTextColor(resources.getColor(R.color.greycolor))
        }
        buttonRotateLeft.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D) }
        buttonRotateRight.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D) }
        tv_back.setOnClickListener {
            if (Share.is_home_back) {
                val splashMenuIntent = Intent(
                    activity,
                    EditorActivity::class.java
                )
                splashMenuIntent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(splashMenuIntent)
                (activity as CropImagePixelActivity).finish()
            } else (activity as CropImagePixelActivity).finish()
        }
    }
    fun showProgress() {
//        val f: ProgressDialogFragment = ProgressDialogFragment.getInstance()
//        fragmentManager?.beginTransaction()
//            ?.add(f, PROGRESS_DIALOG)
//            ?.commitAllowingStateLoss()
    }

    fun dismissProgress() {
        if (!isAdded) return
        val manager = fragmentManager ?: return
        val f =
            manager.findFragmentByTag(PROGRESS_DIALOG) as ProgressDialogFragment?
        if (f != null) {
            requireFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss()
        }
    }
    // Callbacks ///////////////////////////////////////////////////////////////////////////////////
    private val mLoadCallback: LoadCallback = object : LoadCallback {
        override fun onError(e: Throwable) {
            dismissProgress()
        }

        override fun onSuccess() {
            dismissProgress()
        }
    }

    private val mCropCallback: CropCallback = object : CropCallback {
        override fun onError(e: Throwable) {}
        override fun onSuccess(cropped: Bitmap) {
            Log.e("TAG", "mCropCallback:==>$cropped")
            dismissProgress()
            (activity as CropImagePixelActivity).startResultActivity(cropped)
        }
    }

    private val mSaveCallback: SaveCallback = object : SaveCallback {
        override fun onError(e: Throwable) {
            dismissProgress()
        }

        override fun onSuccess(outputUri: Uri) {
            dismissProgress()
            //((CropImagePixelActivity) getActivity()).startResultActivity(outputUri);
        }
    }
}