package com.mufiid.assetmanagement.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.helpers.CustomView
import com.mufiid.assetmanagement.ui.MainActivity
import com.mufiid.assetmanagement.ui.home.HomeActivity
import com.mufiid.assetmanagement.utils.Constants
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loading: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initSupportActionBar()
        init()
    }

    private fun init() {
        loading = ProgressDialog(this)
        btn_login.setOnClickListener(this)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })

        checkIsLogin()
    }

    private fun checkIsLogin() {
        Constants.getIsLoggedIn(this)?.let {
            if (it) {
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }).also { finish() }
            }
        }
    }

    private fun handleUIState(userState: UserState?) {
        when (userState) {
            is UserState.Reset -> {
                setUsernameError(null)
                setPasswordError(null)
            }
            is UserState.ShowToast -> showToast(userState.message)
            is UserState.IsSuccess -> isSuccess(userState)
            is UserState.IsFailed -> {
                isLoading(false)
                userState.message?.let { message -> showToast(message) }
            }
            is UserState.Error -> {
                isLoading(false)
                showToast(userState.err, false)
            }
            is UserState.IsLoading -> isLoading(userState.state)
            is UserState.UserValidation -> {
                userState.username?.let {
                    setUsernameError(it)
                }
                userState.password?.let {
                    setPasswordError(it)
                }
            }
        }
    }

    private fun setPasswordError(err: String?) {
        in_password.error = err
    }

    private fun setUsernameError(err: String?) {
        in_username.error = err
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading.apply {
                setMessage(getString(R.string.loading))
                setCanceledOnTouchOutside(false)
            }.show()
            btn_login.isEnabled = false
        } else {
            loading.dismiss()
            btn_login.isEnabled = true
        }
    }

    private fun isSuccess(it: UserState.IsSuccess) {
        Constants.setUserData(this, it.user)
        Constants.setIsLoggedIn(this, true)
        startActivity(Intent(this, HomeActivity::class.java)).also {
            finish()
        }
    }

    private fun showToast(message: String, isSuccess: Boolean? = true) {
        isSuccess?.let {
            if (it) {
                CustomView.customToast(this, message, true, isSuccess = true)
            } else {
                CustomView.customToast(this, message, true, isSuccess = false)
            }
        }

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
                        loginViewModel.login(username, password)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }
}