package net.basicmodel

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_albumimages.*
import net.adapter.PhoneAlbumImagesAdapter
import net.event.MessageEvent
import net.utils.Share
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AlbumImagesActivity : AppCompatActivity() {

    private var gridLayoutManager: GridLayoutManager? = null
    private var albumAdapter: PhoneAlbumImagesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_albumimages)
        setToolbar()
        initView()
        Log.i("xxxxxxH","AlbumImagesActivity")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(event: MessageEvent) {
        val msg =event.getMessage()[0] as String
        if (TextUtils.equals(msg,"finish1")){
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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