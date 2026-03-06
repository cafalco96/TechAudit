package com.example.techaudit.network

import com.example.techaudit.model.AuditItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuditApiService {
    @POST("equipos")
    suspend fun syncItem(@Body item: AuditItem): Response<AuditItem>

    companion object {
        private const val BASE_URL = "https://69a4d94d885dcb6bd6a6b6d0.mockapi.io/api/v1/"

        fun create(): AuditApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuditApiService::class.java)
        }
    }
}
