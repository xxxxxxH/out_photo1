package net.basicmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_album_images1.*
import net.adapter.PhoneAlbumImagesAdapter1
import net.utils.Share

class AlbumImagesPixelActivity : AppCompatActivity() {
    private var gridLayoutManager: GridLayoutManager? = null
    private var albumAdapter: PhoneAlbumImagesAdapter1? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_images1)
        setToolbar()
        initView()
    }

    private fun initView() {
        gridLayoutManager = GridLayoutManager(this@AlbumImagesPixelActivity, 2)
        rcv_album_images.layoutManager = gridLayoutManager
        val al_album_images = intent.getStringArrayListExtra("image_list")
        albumAdapter = PhoneAlbumImagesAdapter1(this, al_album_images)
        rcv_album_images.adapter = albumAdapter
        iv_back.setOnClickListener { finish() }
    }

    private fun setToolbar() {
        val albumName = intent.extras!!.getString(Share.KEYNAME.ALBUM_NAME)
        tv_title.text = albumName
        setSupportActionBar(toolbar)
    }

}