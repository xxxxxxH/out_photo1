package net.basicmodel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_splash)
        requestPermissions()
    }

    private fun requestPermissions() {
        if (checkPermission(permissions[0]) && checkPermission(permissions[1])) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            ActivityCompat.requestPermissions(this, permissions, 321)
        }
    }

    private fun checkPermission(per: String): Boolean {
        return ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }

}