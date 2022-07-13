package id.smartech.sobored.ui.cat

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityCatBinding
import id.smartech.sobored.ui.cat.model.CatModel
import java.io.File

class CatActivity : BaseActivity<ActivityCatBinding>() {
    private lateinit var viewModel: CatViewModel
    private var STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cat
        super.onCreate(savedInstanceState)

        setViewModel()
        subscribeLiveData()
    }

    private fun setData(data: CatModel) {
        Glide.with(this)
            .load(data.url)
            .placeholder(R.drawable.white)
            .error(R.drawable.white)
            .into(bind.image)
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(CatViewModel::class.java)
        viewModel.setService(createApiCat(this))
        viewModel.getCatImage()
    }

    private fun subscribeLiveData() {
        this.let {
            viewModel.isLoadingLiveData.observe (it) { isLoading ->
                if (isLoading) {
                    bind.progressBar.visibility = View.VISIBLE
                } else {
                    bind.progressBar.visibility = View.GONE
                }
            }
        }

        this.let {
            viewModel.onSuccessLiveData.observe(it) { data ->
                setData(data)
            }
        }

        this.let {
            viewModel.onFailLiveData.observe(it) { onFail ->
                Log.d("onFail", "")
            }
        }
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