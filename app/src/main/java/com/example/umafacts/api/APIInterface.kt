package com.example.umafacts.api

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.umafacts.model.DadesAPI

interface APIInterface {
    @GET("/api/v1/character/info")
    suspend fun getUmamusumeInfo(): Response<DadesAPI>
    companion object {
        val BASE_URL = "https://umapyoi.net"
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