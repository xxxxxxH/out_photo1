package net.basicmodel

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_face.*
import net.event.MessageEvent
import net.fragment.PhotoFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GalleryActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
        EventBus.getDefault().register(this)
        setToolbar()
        initViewAction()
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

    private fun setToolbar() {
        toolbar.setTitleTextColor(Color.WHITE)
        tv_title.text = resources.getString(R.string.Photo)
        setSupportActionBar(toolbar)
        iv_close.setOnClickListener { finish() }
    }
    private fun initViewAction() {
        updateFragment(PhotoFragment().newInstance())
    }

    private fun updateFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.simpleFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

}