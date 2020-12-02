package com.mufiid.assetmanagement.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.assetmanagement.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initSetSupportActionBar()
    }

    private fun initSetSupportActionBar() {
        supportActionBar?.let {
            it.title = getString(R.string.detail)
        }
    }
}