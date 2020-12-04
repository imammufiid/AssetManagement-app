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

    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.getInstance()

    fun login(email: String?, password: String?) {
        state.value = UserState.IsLoading(true)
        CompositeDisposable().add(api.login(email, password).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("LOGLOGIN", it.toString())
                when (it.status) {
                    200 -> state.value = it.data?.let { data -> UserState.IsSuccess(data) }
                    400 -> state.value = UserState.IsFailed(it.message)
                    else -> state.value = UserState.IsFailed(it.message)
                }
                state.value = UserState.IsLoading()
            }, {
                val httpException = it as HttpException
                when(httpException.code()) {
                    400 -> {
                        val message = "User Not Found"
                        state.value = UserState.Error(message)
                    }
                    else -> state.value = UserState.Error(it.message())
                }
                state.value = UserState.IsLoading()
            })
        )
    }

    fun getState() = state
}

sealed class UserState {
    data class ShowToast(var message: String) : UserState()
    data class IsLoading(var state: Boolean = false): UserState()
    data class Error(var err: String): UserState()
    data class IsSuccess(var user: User) : UserState()
    data class IsFailed(var message: String? = null): UserState()
    data class UserValidation(
        var username: String? = null,
        var password: String? = null
    ): UserState()
    object Reset: UserState()
}