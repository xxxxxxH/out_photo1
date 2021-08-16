package net.basicmodel

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_gallery.*
import net.fragment.PhotoFragmentPixel

class GalleryPixelActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setToolbar()
        initViewAction()
    }
    private fun setToolbar(){
        toolbar.setTitleTextColor(Color.WHITE)
        tv_title.text = resources.getString(R.string.Photo)
        setSupportActionBar(toolbar)
        iv_close.setOnClickListener { finish() }
    }

    private fun initViewAction() {
        updateFragment(PhotoFragmentPixel().newInstance())
    }


    private fun updateFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.simpleFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
}