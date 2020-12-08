package com.mufiid.assetmanagement.api

import com.mufiid.assetmanagement.models.Asset
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
}