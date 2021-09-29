package id.smartech.sobored.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityMainBinding
import id.smartech.sobored.ui.model.ResultModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setViewModel()
        subscribeLiveData()
        setOnClick()
    }

    private fun setOnClick() {
        bind.btnNext.setOnClickListener {
            viewModel.getActivityResult()
        }

        bind.instagram.setOnClickListener {
            intentBrowser("https://www.instagram.com/karsacode/")
        }
    }

    private fun intentBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setService(createApi(this))
        viewModel.getActivityResult()
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
            viewModel.onFailLiveData.observe(it) { message ->
                Log.d("onFail", message)
            }
        }
    }

    private fun setData(data: ResultModel) {
        bind.activity.text = data.activity
        bind.type.text = "Type : ${data.type}"

        if(data.link.isNullOrEmpty()) {
            bind.activity.setTextColor(Color.parseColor("#FF000000"))
        } else {
            bind.activity.setTextColor(Color.parseColor("#0804e4"))
            bind.activity.setOnClickListener {
               intentBrowser(data.link)
            }
        }
    }
}