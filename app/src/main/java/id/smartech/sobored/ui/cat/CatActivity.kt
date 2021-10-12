package id.smartech.sobored.ui.cat

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityCatBinding
import java.io.File

class CatActivity : BaseActivity<ActivityCatBinding>() {
    private var STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cat
        super.onCreate(savedInstanceState)

        bind.back.setOnClickListener {
            startDownload()
        }


    }

    private fun setOnClick() {

    }

    private fun startDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            } else {
                downloadImage("MTUxOTE0Nw.jpg","https://cdn2.thecatapi.com/images/MTUxOTE0Nw.jpg")
            }
        } else {
            downloadImage("MTUxOTE0Nw.jpg","https://cdn2.thecatapi.com/images/MTUxOTE0Nw.jpg")
        }
    }

    private fun downloadImage(fileName: String, url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The File is Downloading")
        request.setMimeType("image/jpeg")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + fileName)

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

}