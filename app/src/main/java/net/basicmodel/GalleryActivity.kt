package net.basicmodel

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_face.*
import net.fragment.PhotoFragment

class GalleryActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
        setToolbar()
        initViewAction()
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