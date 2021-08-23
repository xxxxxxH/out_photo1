package net.basicmodel

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_albumimagespip.*
import net.adapter.PhoneAlbumImagesAdapterpip
import net.event.MessageEvent
import net.utils.Share
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AlbumImagesActivitpip:AppCompatActivity() {
    companion object {
        @JvmStatic
        var activity = this
        fun finish(){
            finish()
        }
    }


    private var gridLayoutManager: GridLayoutManager? = null
    private var albumAdapter: PhoneAlbumImagesAdapterpip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albumimagespip)
        EventBus.getDefault().register(this)
        setToolbars()
        initViews()
        Log.i("xxxxxxH","AlbumImagesActivitpip")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(event: MessageEvent) {
        val msg =event.getMessage()[0] as String
        if (TextUtils.equals(msg,"finish")){
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initViews(){
        gridLayoutManager = GridLayoutManager(this@AlbumImagesActivitpip, 2)
        rcv_album_images.layoutManager = gridLayoutManager
        val al_album_images = intent.getStringArrayListExtra("image_list")
        albumAdapter = PhoneAlbumImagesAdapterpip(this, al_album_images)
        rcv_album_images.adapter = albumAdapter
        iv_back.setOnClickListener { finish() }
    }

    private fun setToolbars(){
        val albumName = intent.extras!!.getString(Share.KEYNAME.ALBUM_NAME)
        toolbar.setTitleTextColor(Color.WHITE)
        tv_title.text = albumName
        setSupportActionBar(toolbar)
    }
}