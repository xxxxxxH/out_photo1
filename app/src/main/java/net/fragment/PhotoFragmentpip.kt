package net.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_photo.*
import net.adapter.PhoneAlbumAdapterpip
import net.basicmodel.R
import net.entity.PhoneAlbum
import net.entity.PhonePhoto
import java.util.*

class PhotoFragmentpip:Fragment() {
    var phoneAlbums: Vector<PhoneAlbum> = Vector<PhoneAlbum>()
    var albumsNames = Vector<String>()
    private val al_album: ArrayList<PhoneAlbum> = ArrayList<PhoneAlbum>()
    private val albumAdapter: PhoneAlbumAdapterpip? = null

    fun newInstance(): PhotoFragmentpip {
        val args = Bundle()
        val fragment = PhotoFragmentpip()
        fragment.arguments = args
        return fragment
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewAction()
    }

    private fun initView(){
        val gridLayoutManager = GridLayoutManager(activity, 2)
        rcv_album.layoutManager = gridLayoutManager
        val albumAdapter = PhoneAlbumAdapterpip(activity, phoneAlbums)
        rcv_album.adapter = albumAdapter
        albumAdapter.setActivity(activity as AppCompatActivity?)
    }

    private fun initViewAction() {
        try {
            // which image properties are we querying
            val BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
            // which image properties are we querying
            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
            )

            // content: style URI for the "primary" external storage volume
            val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            // Make the query.
            val cur = requireActivity().contentResolver.query(
                images,
                projection,  // Which columns to return
                BUCKET_GROUP_BY,  // Which rows to return (all rows)
                null,  // Selection arguments (none)
                BUCKET_ORDER_BY // Ordering
            )
            if (cur != null && cur.count > 0) {
                Log.i("DeviceImageManager", " query count=" + cur.count)
                if (cur.moveToFirst()) {
                    var bucketName: String
                    var data: String?
                    var imageId: String?
                    val bucketNameColumn =
                        cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                    val imageUriColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA)
                    val imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media._ID)
                    do {
                        // Get the field values
                        bucketName = cur.getString(bucketNameColumn)
                        data = cur.getString(imageUriColumn)
                        imageId = cur.getString(imageIdColumn)

                        // Adding a new PhonePhoto object to phonePhotos vector
                        var  phonePhoto = PhonePhoto()
                        phonePhoto.albumName = bucketName
                        phonePhoto.photoUri = data
                        phonePhoto.id = Integer.valueOf(imageId)
                        if (albumsNames.contains(bucketName)) {
                            for (album in phoneAlbums) {
                                if (album.name.equals(bucketName)) {
                                    album.albumPhotos.add(phonePhoto)
                                    Log.i(
                                        "DeviceImageManager",
                                        "A photo was added to album => $bucketName"
                                    )
                                    break
                                }
                            }
                        } else {
                            val album = PhoneAlbum()
                            Log.i(
                                "DeviceImageManager",
                                "A new album was created => $bucketName"
                            )
                            album.id = phonePhoto.getId()
                            album.name = bucketName
                            album.coverUri = phonePhoto.getPhotoUri()
                            album.albumPhotos.add(phonePhoto)
                            Log.i(
                                "DeviceImageManager",
                                "A photo was added to album => $bucketName"
                            )
                            phoneAlbums.add(album)
                            albumsNames.add(bucketName)
                        }
                        albumAdapter?.notifyDataSetChanged()
                    } while (cur.moveToNext())
                }
                cur.close()
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}