package id.smartech.sobored.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import id.smartech.sobored.remote.ApiClient

abstract class BaseActivity<ActivityBinding : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var bind: ActivityBinding
    protected var setLayout: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this@BaseActivity, setLayout!!)
    }

    protected inline fun <reified ClassActivity> intents(context: Context) {
        context.startActivity(Intent(context, ClassActivity::class.java))
    }

    protected inline fun <reified ClassActivity> intentsResults(
        context: Context,
        requestCode: Int
    ) {
        startActivityForResult(Intent(context, ClassActivity::class.java), requestCode)
    }

    protected inline fun <reified ApiService> createApiActivities(context: Context): ApiService {
        return ApiClient.getApiActivities(context).create(ApiService::class.java)
    }

    protected inline fun <reified ApiService> createApiJokes(context: Context): ApiService {
        return ApiClient.getApiJokes(context).create(ApiService::class.java)
    }

    protected inline fun <reified ApiService> createApiDog(context: Context): ApiService {
        return ApiClient.getApiDog(context).create(ApiService::class.java)
    }

    protected inline fun <reified ApiService> createApiCat(context: Context): ApiService {
        return ApiClient.getApiCat(context).create(ApiService::class.java)
    }

    protected fun noticeToast(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

}