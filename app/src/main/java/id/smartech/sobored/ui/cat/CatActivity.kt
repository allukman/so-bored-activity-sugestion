package id.smartech.sobored.ui.cat

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityCatBinding
import id.smartech.sobored.ui.cat.model.CatModel
import id.smartech.sobored.ui.main.MainActivity
import id.smartech.sobored.ui.util.KarsaLogger
import java.io.File

class CatActivity : BaseActivity<ActivityCatBinding>() {
    private lateinit var viewModel: CatViewModel
    private var STORAGE_PERMISSION_CODE: Int = 1000
    private var imageUrl: String? = null
    private var msg: String? = ""
    private var lastMsg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cat
        super.onCreate(savedInstanceState)

        setOnClick()
        setViewModel()
        subscribeLiveData()
    }

    private fun setOnClick() {
        bind.download.setOnClickListener {
            if (imageUrl.isNullOrBlank()) {
                noticeToast("Url image not found")
            } else {
                doDownload()
            }
        }
        bind.btnNext.setOnClickListener {
            viewModel.getCatImage()
        }

        bind.back.setOnClickListener {
            intents<MainActivity>(this)
        }

        bind.image.setOnClickListener {
            val intent = Intent(this, DetailCatActivity::class.java)
            intent.putExtra("urlImage", imageUrl)
            startActivity(intent)
        }
    }


    private fun setData(data: CatModel) {
        Glide.with(this)
            .load(data.url)
            .placeholder(R.drawable.white)
            .error(R.drawable.white)
            .into(bind.image)
        imageUrl = data.url
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(CatViewModel::class.java)
        viewModel.setService(createApiCat(this))
        viewModel.getCatImage()
    }

    private fun subscribeLiveData() {
        this.let {
            viewModel.isLoadingLiveData.observe(it) { isLoading ->
                if (isLoading) {
                    bind.progressBar.visibility = View.VISIBLE
                } else {
                    bind.progressBar.visibility = View.GONE
                }
            }
        }

        this.let {
            viewModel.onSuccessLiveData.observe(it) { data ->
                setData(data[0])
                KarsaLogger.print(Gson().toJson(data))
            }
        }
    }

    private fun doDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        } else {
            downloadImage(imageUrl!!)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
        } else {
            downloadImage(imageUrl!!)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadImage(imageUrl!!)
                } else {
                    noticeToast("Tidak bisa melakukan download karena tidak diberi izin")
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


    @SuppressLint("Range")
    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        noticeToast(msg.toString())
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }
}