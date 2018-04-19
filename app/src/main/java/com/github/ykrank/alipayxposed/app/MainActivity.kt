package com.github.ykrank.alipayxposed.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.ykrank.alipayxposed.R
import com.github.ykrank.androidtools.extension.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("WRITE_EXTERNAL_STORAGE PERMISSION GRANTED")
                } else {
                    toast("WRITE_EXTERNAL_STORAGE PERMISSION DENIED")
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    }
}
