package com.mufiid.assetmanagement.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initSupportActionBar()
        init()
    }

    private fun init() {
        btn_login.setOnClickListener(this)
    }

    private fun initSupportActionBar() {
        supportActionBar?.let {
            it.hide()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val username = et_username.text.toString()
                val password = et_password.text.toString()
                when {
                    username.isEmpty() -> {
                        et_username.error = "This field required!"
                    }
                    password.isEmpty() -> {
                        et_password.error = "This field required!"
                    }
                    else -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                }
            }
        }
    }
}