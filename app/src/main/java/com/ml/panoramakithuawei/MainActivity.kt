package com.ml.panoramakithuawei

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.huawei.hms.panorama.Panorama
import com.huawei.hms.panorama.PanoramaInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        loadImageInfo.setOnClickListener {
            panoramaInterfaceLoadImageInfo()
        }
        loadImageInfoWithType.setOnClickListener {
            panoramaInterfaceLoadImageInfoWithType()
        }
        localInterface.setOnClickListener {
            panoramaInterfaceLocalInterface()
        }
        requestPermission()
    }

    private fun panoramaInterfaceLocalInterface() {
        val intent = Intent(this@MainActivity, LocalInterfaceActivity::class.java)
        intent.apply {
            data = returnResource(R.raw.pano)
            putExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL)
        }
        startActivity(intent)
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            Log.i(TAG, "permission ok")
        }
    }

    private fun panoramaInterfaceLoadImageInfo() {
        Panorama.getInstance().loadImageInfoWithPermission(this, returnResource(R.raw.pano))
            .setResultCallback(ResultCallbackImpl(this@MainActivity))
    }

    private fun panoramaInterfaceLoadImageInfoWithType() {
        Panorama.getInstance()
            .loadImageInfoWithPermission(
                this,
                returnResource(R.raw.pano2),
                PanoramaInterface.IMAGE_TYPE_RING
            )
            .setResultCallback(ResultCallbackImpl(this@MainActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun returnResource(drawable: Int): Uri {
        return Uri.parse("android.resource://$packageName/$drawable")
    }
}