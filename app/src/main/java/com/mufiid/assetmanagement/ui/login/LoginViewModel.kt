package com.mufiid.assetmanagement.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mufiid.assetmanagement.api.ApiClient
import com.mufiid.assetmanagement.models.User
import com.mufiid.assetmanagement.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException as HttpException

class LoginViewModel: ViewModel() {

    private var state: SingleLiveEvent<AuthState> = SingleLiveEvent()
    private var api = ApiClient.getInstance()

    fun login(email: String?, password: String?) {
        state.value = AuthState.IsLoading(true)
        CompositeDisposable().add(api.login(email, password).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("LOGLOGIN", it.toString())
                when (it.status) {
                    200 -> state.value = it.data?.let { data -> AuthState.IsSuccess(data) }
                    400 -> state.value = AuthState.IsFailed(it.message)
                    else -> state.value = AuthState.IsFailed(it.message)
                }
                state.value = AuthState.IsLoading()
            }, {
                val httpException = it as HttpException
                when(httpException.code()) {
                    400 -> {
                        val message = "User Not Found"
                        state.value = AuthState.Error(message)
                    }
                    else -> state.value = AuthState.Error(it.message())
                }
                state.value = AuthState.IsLoading()
            })
        )
    }

    fun getState() = state
}

sealed class AuthState {
    data class ShowToast(var message: String) : AuthState()
    data class IsLoading(var state: Boolean = false): AuthState()
    data class Error(var err: String): AuthState()
    data class IsSuccess(var user: User) : AuthState()
    data class IsFailed(var message: String? = null): AuthState()
    data class AuthValidation(
        var username: String? = null,
        var password: String? = null
    ): AuthState()
    object Reset: AuthState()
}