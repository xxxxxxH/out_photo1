package net.basicmodel

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_privacy_policy_screen.*

class PrivacyPolicyScreen:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy_screen)
        iv_back.setOnClickListener(View.OnClickListener { finish() })

        val webView = findViewById<View>(R.id.webview) as WebView
        webView.loadUrl("file:///android_asset/privacyPolicy.html")
    }
}