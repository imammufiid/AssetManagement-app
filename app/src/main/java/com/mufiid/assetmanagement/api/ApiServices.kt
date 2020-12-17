package com.mufiid.assetmanagement.api

import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.models.MerkMobil
import com.mufiid.assetmanagement.models.ModelMobil
import com.mufiid.assetmanagement.models.User
import com.mufiid.assetmanagement.responses.MessageResponse
import com.mufiid.assetmanagement.responses.WrappedListResponses
import com.mufiid.assetmanagement.responses.WrappedResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ApiServices {

    // LOGIN
    @FormUrlEncoded
    @POST("Auth")
    fun login(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<WrappedResponse<User>>

    @GET("Assets")
    fun getData(
        @Header("API-KEY-TOKEN") token: String?,
        @Query("user_id") userId: String?
    ): Observable<WrappedListResponses<Asset>>

    @GET("Mobil/merk")
    fun getMerkMobil(
        @Header("API-KEY-TOKEN") token: String?
    ): Observable<WrappedListResponses<MerkMobil>>

    @GET("Mobil/model")
    fun getModelMobil(
        @Header("API-KEY-TOKEN") token: String?,
        @Query("merk_id") merkId: String?
    ): Observable<WrappedListResponses<ModelMobil>>

    @GET("Assets/search")
    fun searchData(
        @Header("API-KEY-TOKEN") token: String?,
        @Query("query") query: String?,
        @Query("user_id") userId: String?,
    ): Observable<WrappedListResponses<Asset>>

    @FormUrlEncoded
    @POST("Assets")
    fun insertAsset(
        @Header("API-KEY-TOKEN") token: String?,
        @Field("user_id") userId: String?,
        @Field("plat_mobil") platMobil: String?,
        @Field("model_id") modelId: String?,
        @Field("no_rangka") noRangka: String?,
        @Field("no_mesin") noMesin: String?,
        @Field("owner_name") ownerName: String?,
        @Field("date_oil") dateOil: String?
    ): Observable<MessageResponse>

    @FormUrlEncoded
    @PUT("Assets")
    fun updateAsset(
        @Header("API-KEY-TOKEN") token: String?,
        @Field("id") id: String?,
        @Field("user_id") userId: String?,
        @Field("plat_mobil") platMobil: String?,
        @Field("model_id") modelId: String?,
        @Field("no_rangka") noRangka: String?,
        @Field("no_mesin") noMesin: String?,
        @Field("owner_name") ownerName: String?,
        @Field("date_oil") dateOil: String?
    ): Observable<MessageResponse>

    @DELETE("Assets/{id}")
    fun deleteAsset(
        @Header("API-KEY-TOKEN") token: String?,
        @Path("id") id: String?,
    ): Observable<MessageResponse>
}