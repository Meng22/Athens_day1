package com.example.athens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        if (ContextCompat.checkSelfPermission(this,  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setMessage("我需要相機才能送貨，給我權限吧？")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("No") { _, _ -> finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }


        button.setOnClickListener {
            val intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent,1)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish()
                }
                println("=========$permissions")
                println("=========$grantResults")
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                if (data != null){
                    val result : String = data.getStringExtra(CodeUtils.RESULT_STRING)
                    textView2.setText("$result")
                }
            }
        }
    }

}
