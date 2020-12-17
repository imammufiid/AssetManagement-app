package com.mufiid.assetmanagement.ui.detail

import androidx.lifecycle.ViewModel
import com.mufiid.assetmanagement.api.ApiClient
import com.mufiid.assetmanagement.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailViewModel:ViewModel() {
    private var state: SingleLiveEvent<AssetState> = SingleLiveEvent()

    private var api = ApiClient.getInstance()

    fun deleteAsset(
        token: String?,
        idAsset: String?,
    ) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.deleteAsset(
                token,
                idAsset
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> state.value = AssetState.IsSuccess(null, it.message)
                        else -> state.value = AssetState.Error(it.message)
                    }
                    state.value = AssetState.IsLoading()
                }, {
                    state.value = AssetState.Error(it.message)
                    state.value = AssetState.IsLoading()
                })
        )
    }

    fun getState() = state
}


sealed class AssetState() {
    data class IsLoading(var state: Boolean = false) : AssetState()
    data class Error(var err: String?) : AssetState()
    data class IsSuccess(var what: Int? = null, var message: String?) : AssetState()
}