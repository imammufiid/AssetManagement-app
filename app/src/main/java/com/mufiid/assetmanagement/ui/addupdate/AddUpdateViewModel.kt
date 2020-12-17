package com.mufiid.assetmanagement.ui.addupdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mufiid.assetmanagement.api.ApiClient
import com.mufiid.assetmanagement.models.MerkMobil
import com.mufiid.assetmanagement.models.ModelMobil
import com.mufiid.assetmanagement.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddUpdateViewModel : ViewModel() {

    private var state: SingleLiveEvent<AssetState> = SingleLiveEvent()
    private var merkMobil = MutableLiveData<List<MerkMobil>>()
    private var modelMobil = MutableLiveData<List<ModelMobil>>()
    private var api = ApiClient.getInstance()

    fun insertAsset(
        token: String?,
        user_id: String?,
        plat_mobil: String?,
        model_id: String?,
        no_rangka: String?,
        no_mesin: String?,
        owner_name: String?,
        date_oil: String?
    ) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.insertAsset(
                token,
                user_id,
                plat_mobil,
                model_id,
                no_rangka,
                no_mesin,
                owner_name,
                date_oil
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

    fun updateAsset(
        token: String?,
        idAsset: String?,
        user_id: String?,
        plat_mobil: String?,
        model_id: String?,
        no_rangka: String?,
        no_mesin: String?,
        owner_name: String?,
        date_oil: String?
    ) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.updateAsset(
                token,
                idAsset,
                user_id,
                plat_mobil,
                model_id,
                no_rangka,
                no_mesin,
                owner_name,
                date_oil
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

    fun validate(
        platMobil: String?,
        modelId: String?,
        noRangka: String?,
        noMesin: String?,
        ownerName: String?,
        dateOil: String?
    ): Boolean {
        state.value = AssetState.Reset
        if (platMobil != null) {
            if (platMobil.isEmpty()) {
                state.value = AssetState.Error("Plat Mobil tidak boleh kosong!")
                return false
            }
        }

        if(modelId != null) {
            if (modelId.isEmpty()) {
                state.value = AssetState.Error("Merk Mobil tidak boleh kosong")
                return false
            }
        }

        if(noRangka != null) {
            if(noRangka.isEmpty()) {
                state.value = AssetState.Error("No Rangka tidak boleh kosong")
                return false
            }
        }

        if(noMesin != null) {
            if(noMesin.isEmpty()) {
                state.value = AssetState.Error("No Mesin tidak boleh kosong")
                return false
            }
        }

        if(ownerName != null) {
            if(ownerName.isEmpty()) {
                state.value = AssetState.Error("Nama Pemilik tidak boleh kosong")
                return false
            }
        }

        if(dateOil != null) {
            if(dateOil.isEmpty()) {
                state.value = AssetState.Error("Tanggal tidak boleh kosong")
                return false
            }
        }
        return true
    }

    fun setMerkMobil(token: String?) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.getMerkMobil(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> merkMobil.postValue(it.data)
                        else -> state.value = AssetState.Error(it.message)
                    }
                    state.value = AssetState.IsLoading()
                }, {
                    state.value = AssetState.Error(it.message)
                    state.value = AssetState.IsLoading()
                })
        )
    }

    fun setModelMobil(token: String?, merkMobilId: String?) {
        state.value = AssetState.IsLoading(true)
        CompositeDisposable().add(
            api.getModelMobil(token, merkMobilId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> modelMobil.postValue(it.data)
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
    fun getMerkMobil() = merkMobil
    fun getModelMobil() = modelMobil
}

sealed class AssetState() {
    data class ShowToast(var message: String?) : AssetState()
    data class IsLoading(var state: Boolean = false) : AssetState()
    data class Error(var err: String?) : AssetState()
    data class IsSuccess(var what: Int? = null, var message: String?) : AssetState()
    data class AssetValidation(
        var plat_mobil: String? = null,
        var merk_mobil: String? = null,
        var no_rangka: String? = null,
        var no_mesin: String? = null,
        var owner_name: String? = null,
        var date_oil: String? = null,
        var next_date_oil: String? = null
    ) : AssetState()

    object Reset : AssetState()
}