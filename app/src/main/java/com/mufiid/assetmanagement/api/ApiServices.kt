package com.mufiid.assetmanagement.api

import com.mufiid.assetmanagement.models.User
import com.mufiid.assetmanagement.responses.MessageResponse
import com.mufiid.assetmanagement.responses.WrappedResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    // LOGIN
    @FormUrlEncoded
    @POST("auth")
    fun login(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<WrappedResponse<User>>
}