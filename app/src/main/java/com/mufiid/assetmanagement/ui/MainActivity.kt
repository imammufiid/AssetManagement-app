package com.mufiid.assetmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.ui.home.HomeActivity
import com.mufiid.assetmanagement.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSupportActionBar()

        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }

    private fun initSupportActionBar() {
        supportActionBar?.let {
            it.hide()
        }
    }
}