package net.basicmodel

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_facepip.*
import net.event.MessageEvent
import net.fragment.PhotoFragmentpip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FaceActivitypip:AppCompatActivity() {

    companion object{
        fun finish(){
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facepip)
        EventBus.getDefault().register(this)
        initView()
        initViewAction()
        Log.i("xxxxxxH", "FaceActivitypip")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(event: MessageEvent) {
        val msg =event.getMessage()[0] as String
        if (TextUtils.equals(msg,"finish")){
            finish()
        }
    }


    private fun initView() {
        toolbar.setTitleTextColor(Color.WHITE)
        tv_title.text = resources.getString(R.string.Photo)
        setSupportActionBar(toolbar)
        iv_close.setOnClickListener {
            finish()
        }
    }

    private fun initViewAction() {
        updateFragment(PhotoFragmentpip().newInstance())
    }


    private fun updateFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.simpleFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
}