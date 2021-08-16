package net.basicmodel

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_facepip.*
import net.fragment.PhotoFragmentpip

class FaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facepip)
        initView()
        initViewAction()
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