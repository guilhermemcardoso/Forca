package me.gmcardoso.forca.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForcaApi {

    @GET("identificadores/{id}")
    fun retrieveIdentificadores(@Path("id") id: Int): Call<Identifier>

    @GET("palavra/{id}")
    fun retrievePalavra(@Path("id") id: Int): Call<Word>
}