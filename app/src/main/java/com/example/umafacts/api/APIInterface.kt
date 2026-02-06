package com.example.umafacts.api

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.umafacts.model.UmamusumeDetail
import com.example.umafacts.model.CharacterImageResponse

interface APIInterface {
    @GET("/api/v1/character/info")
    suspend fun getUmamusumeInfo(): Response<List<UmamusumeDetail>>

    @GET("/api/v1/character/images/{id}")
    suspend fun getCharacterImages(@Path("id") id: Int): Response<List<CharacterImageResponse>>

    companion object {
        private const val BASE_URL = "https://umapyoi.net"

        fun create(): APIInterface {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(APIInterface::class.java)
        }
    }
}