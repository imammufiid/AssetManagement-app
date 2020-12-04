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
    private var asset = MutableLiveData<Asset>()

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

    fun getDataById(user_id: Int?, idAsset: Int?) {}

    fun getAssets() = assets
    fun getAsset() = asset
    fun getState() = state
}

sealed class AssetState() {
    data class ShowToast(var message: String?): AssetState()
    data class IsLoading(var state: Boolean = false): AssetState()
    data class Error(var err: String?): AssetState()
    data class IsSuccess(var what: Int? = null): AssetState()
    data class AssetValidation(
        var plat_mobil: String? = null,
        var merk_mobil: String? = null,
        var no_rangka: String? = null,
        var no_mesing: String? = null,
        var owner_name: String? = null,
        var date_oil: String? = null,
        var next_date_oil: String? = null
    ): AssetState()
    object Reset: AssetState()
}