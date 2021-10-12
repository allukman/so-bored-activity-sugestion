package id.smartech.sobored.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import id.smartech.sobored.R
import id.smartech.sobored.base.BaseActivity
import id.smartech.sobored.databinding.ActivityMainBinding
import id.smartech.sobored.ui.cat.CatActivity
import id.smartech.sobored.ui.jokes.JokesActivity
import id.smartech.sobored.ui.suggestion.SuggestionActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setOnClick()
    }

    private fun setOnClick() {
        bind.activities.setOnClickListener {
            intents<SuggestionActivity>(this)
        }

        bind.jokes.setOnClickListener {
            intents<JokesActivity>(this)
        }

        bind.cat.setOnClickListener {
            intents<CatActivity>(this)
        }

        bind.instagram.setOnClickListener {
            intentBrowser("https://www.instagram.com/karsacode/")
        }
    }

    private fun intentBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}