package id.smartech.sobored.ui.suggestion

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivitySuggestionBinding
import id.smartech.sobored.ui.main.MainActivity
import id.smartech.sobored.ui.suggestion.model.ResultModel
import id.smartech.sobored.ui.util.KarsaLogger

class SuggestionActivity : BaseActivity<ActivitySuggestionBinding>() {
    private lateinit var viewModel: SuggestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_suggestion
        super.onCreate(savedInstanceState)


        setViewModel()
        subscribeLiveData()
        setOnClick()
    }

    private fun setOnClick() {
        bind.btnNext.setOnClickListener {
            viewModel.getActivityResult()
        }
        
        bind.back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(SuggestionViewModel::class.java)
        viewModel.setService(createApiActivities(this))
        viewModel.getActivityResult()
    }

    private fun subscribeLiveData() {
        this.let {
            viewModel.isLoadingLiveData.observe (it) { isLoading ->
                if (isLoading) {
                    bind.progressBar.visibility = View.VISIBLE
                    bind.btnNext.visibility = View.GONE
                } else {
                    bind.progressBar.visibility = View.GONE
                    bind.btnNext.visibility = View.VISIBLE
                }
            }
        }

        this.let {
            viewModel.onSuccessLiveData.observe(it) { data ->
                setData(data)
                KarsaLogger.print(Gson().toJson(data))
            }
        }

        this.let {
            viewModel.onFailLiveData.observe(it) { message ->
                Log.d("onFail", message)
            }
        }
    }

    private fun intentBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun setData(data: ResultModel) {
        bind.activity.text = data.activity
        bind.type.text = "Type : ${data.type}"

        if(data.link.isEmpty()) {
            bind.activity.setTextColor(Color.parseColor("#FF000000"))
        } else {
            bind.activity.setTextColor(Color.parseColor("#0804e4"))
            bind.activity.setOnClickListener {
               intentBrowser(data.link)
            }
        }
    }
}