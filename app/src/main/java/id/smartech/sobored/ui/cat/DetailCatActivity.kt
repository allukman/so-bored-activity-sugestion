package id.smartech.sobored.ui.cat

import android.os.Bundle
import com.bumptech.glide.Glide
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityDetailCatBinding

class DetailCatActivity : BaseActivity<ActivityDetailCatBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_detail_cat
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("urlImage")

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.white)
            .error(R.drawable.white)
            .into(bind.image)
    }
}