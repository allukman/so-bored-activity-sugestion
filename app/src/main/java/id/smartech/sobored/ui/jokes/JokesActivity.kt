package id.smartech.sobored.ui.jokes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityJokesBinding
import id.smartech.sobored.ui.jokes.model.JokesModel

class JokesActivity : BaseActivity<ActivityJokesBinding>() {
    private lateinit var viewModel: JokesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_jokes
        super.onCreate(savedInstanceState)

        setViewModel()
        subscribeLiveData()
        setOnClick()
    }

    private fun setOnClick() {
        bind.btnNext.setOnClickListener {
            viewModel.getJokesPlease()
        }

        bind.back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(JokesViewModel::class.java)
        viewModel.setService(createApiActivities(this))
        viewModel.getJokesPlease()
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

    private fun setData(data: JokesModel) {
        bind.category.text = "Category : ${data.category}"

        if(data.type == "single") {
            bind.setup.visibility = View.VISIBLE
            bind.punchline.visibility = View.GONE

            bind.setup.text = data.joke
        } else {
            bind.setup.visibility = View.VISIBLE
            bind.punchline.visibility = View.VISIBLE

            bind.setup.text = data.setup
            bind.punchline.text = data.delivery
        }
    }
}