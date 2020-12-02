package com.mufiid.assetmanagement.ui.addupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.assetmanagement.R

class AddUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update)

        initSupportActionBar()
    }

    private fun initSupportActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }
}