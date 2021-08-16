package net.basicmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_albumimages.*
import net.adapter.PhoneAlbumImagesAdapter
import net.utils.Share

class AlbumImagesActivity : AppCompatActivity() {

    private var gridLayoutManager: GridLayoutManager? = null
    private var albumAdapter: PhoneAlbumImagesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albumimages)
        setToolbar()
        initView()
    }

    private fun initView() {
        gridLayoutManager = GridLayoutManager(this@AlbumImagesActivity, 2)
        rcv_album_images.layoutManager = gridLayoutManager
        val al_album_images = intent.getStringArrayListExtra("image_list")
        albumAdapter = PhoneAlbumImagesAdapter(this, al_album_images)
        rcv_album_images.adapter = albumAdapter
    }

    private fun setToolbar() {
        val albumName = intent.extras!!.getString(Share.KEYNAME.ALBUM_NAME)
        tv_title.text = albumName
        setSupportActionBar(toolbar)
        iv_back.setOnClickListener { finish() }
    }
}