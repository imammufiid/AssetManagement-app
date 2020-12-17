package com.mufiid.assetmanagement.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mufiid.assetmanagement.api.ApiClient
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private var assets = MutableLiveData<List<Asset>>()

    private var state: SingleLiveEvent<AssetState> = SingleLiveEvent()

    private var api = ApiClient.getInstance()

    fun getAllData(user_id: String?, token: String?) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.getData(token, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> assets.postValue(it.data)
                        else -> state.value = AssetState.Error(it.message)
                    }
                    state.value = AssetState.IsLoading()
                }, {
                    state.value = AssetState.Error(it.message)
                    state.value = AssetState.IsLoading()
                })
        )
    }

    fun getAllDataSwipeRefresh(user_id: String?, token: String?) {
        CompositeDisposable().add(
            api.getData(token, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> assets.postValue(it.data)
                        else -> state.value = AssetState.Error(it.message)
                    }
                }, {
                    state.value = AssetState.Error(it.message)
                })
        )
    }

    fun searchData(token: String?, query: String?, userId: String?) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.searchData(token, query, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> assets.postValue(it.data)
                        else -> state.value = AssetState.Error(it.message)
                    }
                    state.value = AssetState.IsLoading()
                }, {
                    state.value = AssetState.Error(it.message)
                    state.value = AssetState.IsLoading()
                })
        )
    }

    fun getAssets() = assets
    fun getState() = state
}

sealed class AssetState() {
    data class IsLoading(var state: Boolean = false): AssetState()
    data class Error(var err: String?): AssetState()
}